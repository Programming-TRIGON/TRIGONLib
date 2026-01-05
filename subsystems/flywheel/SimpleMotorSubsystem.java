package frc.trigon.lib.subsystems.flywheel;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;
import lib.hardware.phoenix6.talonfx.TalonFXMotor;
import lib.hardware.phoenix6.talonfx.TalonFXSignal;
import lib.hardware.simulation.SimpleMotorSimulation;
import frc.trigon.lib.subsystems.MotorSubsystem;
import lib.utilities.mechanisms.SpeedMechanism2d;

public class SimpleMotorSubsystem extends MotorSubsystem {
    private final TalonFXMotor motor;
    private final String name;
    private final double
            maximumVelocity,
            maximumAcceleration,
            maximumJerk;
    private final VoltageOut voltageRequest;
    private final VelocityVoltage velocityRequest;
    private final SpeedMechanism2d mechanism;
    private final double velocityTolerance;
    private final SysIdRoutine.Config sysIDConfig;
    private SimpleMotorState targetState;

    private final boolean shouldUseVoltageControl;

    public SimpleMotorSubsystem(TalonFXMotor motor, SimpleMotorSubsystemConfiguration config) {
        this.motor = motor;
        name = config.name;
        maximumVelocity = config.maximumVelocity;
        maximumAcceleration = config.maximumAcceleration;
        maximumJerk = config.maximumJerk;
        velocityTolerance = config.velocityTolerance;
        mechanism = new SpeedMechanism2d(name + "Mechanism", config.maximumDisplayableVelocity);
        voltageRequest = new VoltageOut(0).withEnableFOC(config.focEnabled);
        velocityRequest = new VelocityVoltage(0).withEnableFOC(config.focEnabled);
        shouldUseVoltageControl = config.shouldUseVoltageControl;
        sysIDConfig = new SysIdRoutine.Config(
                Units.Volts.of(config.sysIDRampRate).per(Units.Seconds),
                Units.Volts.of(config.sysIDStepVoltage),
                null
        );
        motor.setPhysicsSimulation(
                new SimpleMotorSimulation(
                        config.gearbox,
                        config.gearRatio,
                        config.momentOfInertia
                ));

        setName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SysIdRoutine.Config getSysIDConfig() {
        return sysIDConfig;
    }

    @Override
    public void setBrake(boolean brake) {
        motor.setBrake(brake);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void updateLog(SysIdRoutineLog log) {
        log.motor(name)
                .angularPosition(Units.Degrees.of(motor.getSignal(TalonFXSignal.POSITION)))
                .angularVelocity(Units.RotationsPerSecond.of(motor.getSignal(TalonFXSignal.VELOCITY)))
                .voltage(Units.Volts.of(motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE)));
    }

    @Override
    public void updateMechanism() {
        if (shouldUseVoltageControl) {
            mechanism.update(getVoltage());
            return;
        }
        mechanism.update(
                getVelocityRotationsPerSecond(),
                targetState.getTargetUnit()
        );
    }

    @Override
    public void updatePeriodically() {
        motor.update();
        Logger.recordOutput(name + "/CurrentVelocityRotationsPerSecond", motor.getSignal(TalonFXSignal.VELOCITY));
        Logger.recordOutput(name + "/CurrentVoltage", motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE));
    }

    @Override
    public void sysIDDrive(double targetVoltage) {
        setTargetVoltage(targetVoltage);
    }

    public boolean atState(SimpleMotorSubsystem.SimpleMotorState targetState) {
        return this.targetState == targetState && atTargetState();
    }

    public boolean atTargetState() {
        if (targetState == null)
            return false;

        if (shouldUseVoltageControl)
            return true;
        return Math.abs(getVelocityRotationsPerSecond() - targetState.getTargetUnit()) < velocityTolerance;
    }

    public double getVelocityRotationsPerSecond() {
        return motor.getSignal(TalonFXSignal.VELOCITY);
    }

    public double getVoltage() {
        return motor.getSignal(TalonFXSignal.MOTOR_VOLTAGE);
    }

    public void setTargetState(SimpleMotorSubsystem.SimpleMotorState targetState) {
        if (shouldUseVoltageControl) {
            setTargetStateWithVoltage(targetState);
            return;
        }
        setTargetStateWithVelocity(targetState);
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    public void setTargetVoltage(double targetVoltage) {
        setControl(voltageRequest.withOutput(targetVoltage));
    }

    public void setTargetVelocity(double targetVelocityRotationsPerSecond) {
        setControl(velocityRequest.withVelocity(targetVelocityRotationsPerSecond));
    }

    private void setTargetStateWithVoltage(SimpleMotorSubsystem.SimpleMotorState targetState) {
        this.targetState = targetState;
        setTargetVoltage(targetState.getTargetUnit());
    }

    private void setTargetStateWithVelocity(SimpleMotorSubsystem.SimpleMotorState targetState) {
        this.targetState = targetState;
        setTargetVelocity(targetState.getTargetUnit());
    }

    /**
     * An interface for a Simple motor state.
     * getTargetUnit represents the target Velocity or Voltage (depending on the control mode) of the state.
     */
    public interface SimpleMotorState {
        double getTargetUnit();

        SimpleMotorState getPrepareState();
    }
}
