package org.trigon.hardware.subsystems.flywheel;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.phoenix6.talonfx.TalonFXMotor;
import org.trigon.hardware.phoenix6.talonfx.TalonFXSignal;
import org.trigon.hardware.simulation.SimpleMotorSimulation;
import org.trigon.utilities.mechanisms.SpeedMechanism2d;

public class SimpleMotorSubsystem {
    private final TalonFXMotor motor;
    private final String name;
    private final double
            maximumVelocity,
            maximumAcceleration,
            maximumJerk;
    private final VoltageOut voltageRequest;
    private final VelocityVoltage velocityRequest;
    private final SpeedMechanism2d mechanism;
    private final SysIdRoutine.Config sysIDConfig;
    private SimpleMotorState targetState;

    private boolean isUsingVoltageControl = true;

    public SimpleMotorSubsystem(TalonFXMotor motor, SimpleMotorConfiguration config) {
        this.motor = motor;
        name = config.name;
        maximumVelocity = config.maximumVelocity;
        maximumAcceleration = config.maximumAcceleration;
        maximumJerk = config.maximumJerk;
        mechanism = new SpeedMechanism2d(name + "Mechanism", config.maximumDisplayableVelocity);
        voltageRequest = new VoltageOut(0).withEnableFOC(config.focEnabled);
        velocityRequest = new VelocityVoltage(0).withEnableFOC(config.focEnabled);
        sysIDConfig = new SysIdRoutine.Config(
                Units.Volts.of(config.sysIDRampRate).per(Units.Seconds),
                Units.Volts.of(config.sysIDStepVoltage),
                Units.Second.of(1000)
        );
        motor.setPhysicsSimulation(
                new SimpleMotorSimulation(
                        config.gearbox,
                        config.gearRatio,
                        config.momentOfInertia
                ));
    }

    public String getName() {
        return name;
    }

    public SysIdRoutine.Config getSysIDConfig() {
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
                .angularPosition(Units.Degrees.of(motor.getSignal(TalonFXSignal.POSITION)))
                .angularVelocity(Units.RotationsPerSecond.of(motor.getSignal(TalonFXSignal.VELOCITY)))
                .voltage(Units.Volts.of(motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE)));
    }

    public void updateMechanism() {
        if (isUsingVoltageControl) {
            mechanism.update(getVoltage());
            return;
        }
        mechanism.update(
                    getVelocityRotationsPerSecond(),
                    targetState.getTargetVelocityRotationsPerSecond()
            );
    }

    public void updatePeriodically() {
        motor.update();
        Logger.recordOutput(name + "/CurrentVelocityRotationsPerSecond", motor.getSignal(TalonFXSignal.VELOCITY));
        Logger.recordOutput(name + "/CurrentVoltage", motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE));
    }

    public void sysIDDrive(double targetVoltage) {
        setTargetVoltage(targetVoltage);
    }

    public boolean atState(SimpleMotorSubsystem.SimpleMotorState targetState) {
        return this.targetState == targetState && atTargetState();
    }

    public boolean atTargetState() {
        if (targetState == null)
            return false;

        return isUsingVoltageControl?
            getVoltage() == targetState.getTargetVoltage():
            getVelocityRotationsPerSecond() == targetState.getTargetVelocityRotationsPerSecond();
    }

    public double getVelocityRotationsPerSecond() {
        return motor.getSignal(TalonFXSignal.VELOCITY);
    }

    public double getVoltage() {
        return motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE);
    }


    public void setTargetStateWithVoltage(SimpleMotorSubsystem.SimpleMotorState targetState) {
        isUsingVoltageControl = true;
        this.targetState = targetState;
        setTargetVoltage(targetState.getTargetVoltage());
    }

    public void setTargetStateWithVelocity(SimpleMotorSubsystem.SimpleMotorState targetState) {
        isUsingVoltageControl = false;
        this.targetState = targetState;
        setTargetVelocity(targetState.getTargetVelocityRotationsPerSecond());
    }

    public void setTargetVoltage(double targetVoltage) {
        setControl(voltageRequest.withOutput(targetVoltage));
    }

    public void setTargetVelocity(double targetVelocity) {
        setControl(velocityRequest.withVelocity(targetVelocity));
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    public interface SimpleMotorState {
        double getTargetVoltage();

        double getTargetVelocityRotationsPerSecond();
    }
}
