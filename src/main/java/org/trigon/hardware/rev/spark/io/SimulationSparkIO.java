package org.trigon.hardware.rev.spark.io;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SimulationSparkIO extends SparkIO {
    private final SparkMax motor;
    private final SparkSim motorSimulation;
    private final SparkClosedLoopController pidController;
    private SparkEncoder encoder = null;
    private SparkAbsoluteEncoderSim absoluteEncoderSimulation = null;
    private SparkRelativeEncoderSim relativeEncoderSimulation = null;
    private MotorPhysicsSimulation physicsSimulation = null;

    public SimulationSparkIO(int id) {
        motor = new SparkMax(id, SparkMax.MotorType.kBrushless);
        motorSimulation = new SparkSim(motor, DCMotor.getBag(1)); // DCMotor.getBag(1) is a placeholder, we don't actually care about this since we always do link to setCurrent
        pidController = motor.getClosedLoopController();
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType) {
        motor.setVoltage(value);
        System.out.println(value + "set reference");
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot) {
//        pidController.setReference(value, controlType, pidSlot);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward) {
//        pidController.setReference(value, controlType, pidSlot, arbFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFeedForwardUnits) {
//        pidController.setReference(value, controlType, pidSlot, arbFeedForward, arbFeedForwardUnits);
    }

    @Override
    public SparkEncoder getEncoder() {
        return encoder;
    }

    @Override
    public SparkBase getMotor() {
        return motor;
    }

    @Override
    public void stopMotor() {
        motor.stopMotor();
    }

    @Override
    public void setPeriodicFrameTimeout(int timeoutMs) {
        motor.setPeriodicFrameTimeout(timeoutMs);
    }

    @Override
    public void setInverted(boolean inverted) {
        motor.setInverted(inverted);
    }

    @Override
    public void setBrake(boolean brake) {
        final SparkMaxConfig configuration = new SparkMaxConfig();
        configuration.idleMode(brake ? SparkMaxConfig.IdleMode.kBrake : SparkMaxConfig.IdleMode.kCoast);
        motor.configure(configuration, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
    }

    @Override
    public void updateSimulation() {
        if (physicsSimulation == null)
            return;

        physicsSimulation.setInputVoltage(motorSimulation.getBusVoltage() * motorSimulation.getAppliedOutput());
        physicsSimulation.updateMotor();

        Logger.recordOutput("motor simulation applied output" + motor.getDeviceId(), motorSimulation.getAppliedOutput());
        Logger.recordOutput("velocity" + motor.getDeviceId(), physicsSimulation.getRotorVelocityRotationsPerSecond());

        motorSimulation.iterate(physicsSimulation.getRotorVelocityRotationsPerSecond() * 60 * motor.configAccessor.encoder.getVelocityConversionFactor(), RobotHardwareStats.SUPPLY_VOLTAGE, RobotHardwareStats.getPeriodicTimeSeconds());
        motorSimulation.setMotorCurrent(physicsSimulation.getCurrent());
        if (isUsingAbsoluteEncoder()) {
            System.out.println("absolute encoder simulation iterate");
            absoluteEncoderSimulation.iterate(physicsSimulation.getSystemVelocityRotationsPerSecond() * 60 * motor.configAccessor.encoder.getVelocityConversionFactor(), RobotHardwareStats.getPeriodicTimeSeconds());
            return;
        }
        relativeEncoderSimulation.iterate(physicsSimulation.getRotorVelocityRotationsPerSecond() * 60 * motor.configAccessor.encoder.getVelocityConversionFactor(), RobotHardwareStats.getPeriodicTimeSeconds());
    }

    @Override
    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation, boolean isUsingAbsoluteEncoder) {
        this.physicsSimulation = physicsSimulation;

        if (isUsingAbsoluteEncoder && absoluteEncoderSimulation == null) {
            System.out.println("create absolute encoder simulation");
            createAbsoluteEncoderSimulation();
            return;
        }
        if (relativeEncoderSimulation == null)
            createRelativeEncoderSimulation();
    }

    private void createAbsoluteEncoderSimulation() {
        absoluteEncoderSimulation = motorSimulation.getAbsoluteEncoderSim();
        System.out.println(absoluteEncoderSimulation.toString());
        encoder = SparkEncoder.createAbsoluteEncoder(motor);
        relativeEncoderSimulation = null;
    }

    private void createRelativeEncoderSimulation() {
        relativeEncoderSimulation = motorSimulation.getRelativeEncoderSim();
        encoder = SparkEncoder.createRelativeEncoder(motor);
        absoluteEncoderSimulation = null;
    }

    private boolean isUsingAbsoluteEncoder() {
        return absoluteEncoderSimulation != null;
    }
}