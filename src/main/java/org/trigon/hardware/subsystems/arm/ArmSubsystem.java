package org.trigon.hardware.subsystems.arm;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.DynamicMotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.phoenix6.talonfx.TalonFXMotor;
import org.trigon.hardware.phoenix6.talonfx.TalonFXSignal;
import org.trigon.hardware.simulation.SingleJointedArmSimulation;
import org.trigon.utilities.mechanisms.SingleJointedArmMechanism2d;

public class ArmSubsystem {
    private final TalonFXMotor motor;
    private final Pose3d originPoint;
    private final String name;
    private final double
            maximumVelocity,
            maximumAcceleration,
            maximumJerk;
    private final Rotation2d angleTolerance;
    private final VoltageOut voltageRequest;
    private final DynamicMotionMagicVoltage positionRequest;
    private final SingleJointedArmMechanism2d mechanism;
    private final SysIdRoutine.Config sysIDConfig;
    private ArmState targetState;

    public ArmSubsystem(TalonFXMotor motor, ArmConfiguration config, Pose3d originPoint) {
        this.motor = motor;
        this.originPoint = originPoint;

        name = config.name;
        maximumVelocity = config.maximumVelocity;
        maximumAcceleration = config.maximumAcceleration;
        maximumJerk = config.maximumJerk;
        angleTolerance = config.angleTolerance;
        mechanism = new SingleJointedArmMechanism2d(name + "Mechanism", config.lengthMeters, config.mechanismColor);
        voltageRequest = new VoltageOut(0).withEnableFOC(config.focEnabled);
        positionRequest = new DynamicMotionMagicVoltage(0, maximumVelocity, maximumAcceleration, maximumJerk).withEnableFOC(config.focEnabled);
        sysIDConfig = new SysIdRoutine.Config(
                Units.Volts.of(config.sysIDRampRate).per(Units.Seconds),
                Units.Volts.of(config.sysIDStepVoltage),
                Units.Second.of(1000)
        );
        motor.setPhysicsSimulation(
                new SingleJointedArmSimulation(
                        config.gearbox,
                        config.gearRatio,
                        config.massKilograms,
                        config.lengthMeters,
                        config.minimumAngle,
                        config.maximumAngle,
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
                .angularPosition(Units.Rotations.of(motor.getSignal(TalonFXSignal.POSITION)))
                .angularVelocity(Units.RotationsPerSecond.of(motor.getSignal(TalonFXSignal.VELOCITY)))
                .voltage(Units.Volts.of(motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE)));
    }

    public void updateMechanism() {
        logComponentPose();

        mechanism.update(
                getAngle(),
                Rotation2d.fromRotations(motor.getSignal(TalonFXSignal.CLOSED_LOOP_REFERENCE))
        );
    }

    public void updatePeriodically() {
        motor.update();
        Logger.recordOutput(name + "/CurrentAngle", getAngle());
    }

    public void sysIDDrive(double targetVoltage) {
        motor.setControl(voltageRequest.withOutput(targetVoltage));
    }

    public boolean atState(ArmState targetState) {
        return this.targetState == targetState && atTargetState();
    }

    public boolean atTargetState() {
        if (targetState == null)
            return false;
        final double currentToTargetStateDifferenceDegrees = Math.abs(targetState.getTargetAngle().minus(getAngle()).getDegrees());
        return currentToTargetStateDifferenceDegrees < angleTolerance.getDegrees();
    }

    public Rotation2d getAngle() {
        return Rotation2d.fromRotations(motor.getSignal(TalonFXSignal.POSITION));
    }

    public void setTargetState(ArmState targetState) {
        this.targetState = targetState;
        setTargetState(targetState.getTargetAngle(), targetState.getSpeedScalar());
    }

    public void setTargetState(Rotation2d targetAngle, double speedScalar) {
        scalePositionRequestSpeed(speedScalar);
        setTargetAngle(targetAngle);
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    private void scalePositionRequestSpeed(double speedScalar) {
        positionRequest.Velocity = maximumVelocity * speedScalar;
        positionRequest.Acceleration = maximumAcceleration * speedScalar;
        positionRequest.Jerk = maximumJerk * speedScalar;
    }

    private void setTargetAngle(Rotation2d targetAngle) {
        setControl(positionRequest.withPosition(targetAngle.getRotations()));
    }

    private void logComponentPose() {
        Logger.recordOutput("Poses/Components/" + name + "Pose", calculateComponentPose());
    }

    private Pose3d calculateComponentPose() {
        final Transform3d armTransform = new Transform3d(
                new Translation3d(),
                new Rotation3d(0, getAngle().getRadians(), 0)
        );
        return originPoint.transformBy(armTransform);
    }

    public interface ArmState {
        Rotation2d getTargetAngle();

        double getSpeedScalar();
    }
}
