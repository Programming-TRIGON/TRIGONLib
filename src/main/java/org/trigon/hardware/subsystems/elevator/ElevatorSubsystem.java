package org.trigon.hardware.subsystems.elevator;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DynamicMotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.phoenix6.talonfx.TalonFXMotor;
import org.trigon.hardware.phoenix6.talonfx.TalonFXSignal;
import org.trigon.hardware.simulation.ElevatorSimulation;
import org.trigon.utilities.Conversions;
import org.trigon.utilities.mechanisms.ElevatorMechanism2d;

public class ElevatorSubsystem {
    private final TalonFXMotor motor;
    private final Pose3d[] stagesOriginPoints;
    private final String name;
    private final double
            positionToleranceMeters,
            drumRadiusMeters,
            maximumVelocity,
            maximumAcceleration,
            maximumJerk;
    private final VoltageOut voltageRequest;
    private final DynamicMotionMagicVoltage positionRequest;
    private final ElevatorMechanism2d mechanism;
    private final SysIdRoutine.Config sysIDConfig;
    private ElevatorState targetState;

    public ElevatorSubsystem(TalonFXMotor motor, ElevatorConfiguration config, Pose3d... stagesOriginPoints) {
        this.motor = motor;
        this.stagesOriginPoints = stagesOriginPoints;

        name = config.name;
        positionToleranceMeters = config.positionToleranceMeters;
        drumRadiusMeters = config.drumRadiusMeters;
        maximumVelocity = config.maximumVelocity;
        maximumAcceleration = config.maximumAcceleration;
        maximumJerk = config.maximumJerk;
        mechanism = new ElevatorMechanism2d(name + "Mechanism", config.maximumHeight, config.minimumHeight, config.mechanismColor);
        voltageRequest = new VoltageOut(0).withEnableFOC(config.focEnabled);
        positionRequest = new DynamicMotionMagicVoltage(0, maximumVelocity, maximumAcceleration, maximumJerk).withEnableFOC(config.focEnabled);
        sysIDConfig = new SysIdRoutine.Config(
                Units.Volts.of(config.sysIDRampRate).per(Units.Seconds),
                Units.Volts.of(config.sysIDStepVoltage),
                Units.Second.of(1000)
        );
        motor.setPhysicsSimulation(
                new ElevatorSimulation(
                        config.gearbox,
                        config.gearRatio,
                        config.massKilograms,
                        config.drumRadiusMeters,
                        config.minimumHeight,
                        config.maximumHeight,
                        config.shouldSimulateGravity
                ));
    }

    public String getName() {
        return name;
    }

    public SysIdRoutine.Config getSysIdConfig() {
        return sysIDConfig;
    }

    public void setBrake(boolean brake) {
        motor.setBrake(brake);
    }

    public void stop() {
        motor.stopMotor();
    }

    public void updateLog(SysIdRoutineLog log) {
        log.motor(name)
                .linearPosition(Units.Meters.of(getPositionRotations()))
                .linearVelocity(Units.MetersPerSecond.of(motor.getSignal(TalonFXSignal.VELOCITY)))
                .voltage(Units.Volts.of(motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE)));
    }

    public void updateMechanism() {
        logComponentPoses();

        mechanism.update(
                getPositionRotations(),
                motor.getSignal(TalonFXSignal.CLOSED_LOOP_REFERENCE)
        );
    }

    public void updatePeriodically() {
        motor.update();
        Logger.recordOutput(name + "/CurrentPositionMeters", getPositionMeters());
    }

    public void sysIDDrive(double targetVoltage) {
        motor.setControl(voltageRequest.withOutput(targetVoltage));
    }

    public boolean atState(ElevatorState targetState) {
        return this.targetState == targetState && atTargetState();
    }

    public boolean atTargetState() {
        if (targetState == null)
            return false;
        final double currentToTargetStateDifference = Math.abs(targetState.getTargetPositionMeters() - getPositionMeters());
        return currentToTargetStateDifference < positionToleranceMeters;
    }

    public double getPositionRotations() {
        return motor.getSignal(TalonFXSignal.POSITION);
    }

    public double getPositionMeters() {
        return rotationsToMeters(getPositionRotations());
    }

    public void setTargetState(ElevatorState targetState) {
        this.targetState = targetState;
        setTargetState(targetState.getTargetPositionMeters(), targetState.getSpeedScalar());
    }

    public void setTargetState(double targetPositionMeters, double speedScalar) {
        scalePositionRequestSpeed(speedScalar);
        setTargetPositionRotations(metersToRotations(targetPositionMeters));
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    private void scalePositionRequestSpeed(double speedScalar) {
        positionRequest.Velocity = maximumVelocity * speedScalar;
        positionRequest.Acceleration = maximumAcceleration * speedScalar;
        positionRequest.Jerk = maximumJerk * speedScalar;
    }

    private void setTargetPositionRotations(double targetPositionRotations) {
        setControl(positionRequest.withPosition(targetPositionRotations));
    }

    private void logComponentPoses() {
        for (int i = 0; i < stagesOriginPoints.length; i++)
            Logger.recordOutput("Poses/Components/" + name + "Pose" + i, calculateComponentPose(i));
    }

    private Pose3d calculateComponentPose(int targetStage) {
        final Transform3d elevatorTransform = new Transform3d(
                new Translation3d(0, 0, getPositionMeters() / (targetStage + 1)),
                new Rotation3d()
        );
        return stagesOriginPoints[targetStage].transformBy(elevatorTransform);
    }

    private double rotationsToMeters(double positionRotations) {
        return Conversions.rotationsToDistance(positionRotations, drumRadiusMeters * 2);
    }

    private double metersToRotations(double positionMeters) {
        return Conversions.distanceToRotations(positionMeters, drumRadiusMeters * 2);
    }

    public interface ElevatorState {
        double getTargetPositionMeters();

        double getSpeedScalar();
    }
}
