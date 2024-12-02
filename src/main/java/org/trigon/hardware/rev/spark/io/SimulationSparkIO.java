package org.trigon.hardware.rev.spark.io;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;
import org.trigon.utilities.Conversions;

public class SimulationSparkIO extends SparkIO {
    private final SparkMax motor;
    private final SparkSim motorSimulation;
    private final SparkClosedLoopController pidController;
    private final SparkAbsoluteEncoderSim absoluteEncoderSimulation;
    private final SparkEncoder encoder;
    private MotorPhysicsSimulation physicsSimulation = null;
    private boolean isUsingAbsoluteEncoder = false;

    public SimulationSparkIO(int id) {
        motor = new SparkMax(id, SparkMax.MotorType.kBrushless);
        encoder = SparkEncoder.createAbsoluteEncoder(motor);
        pidController = motor.getClosedLoopController();
        motorSimulation = new SparkSim(motor, DCMotor.getBag(1)); /** DCMotor.getBag(1) is a placeholder, we don't actually care about this since we always do {@link com.revrobotics.sim.SparkMaxSim#setMotorCurrent} */
        absoluteEncoderSimulation = motorSimulation.getAbsoluteEncoderSim();
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType) {
        pidController.setReference(value, controlType);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot) {
        pidController.setReference(value, controlType, pidSlot);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbitraryFeedForward) {
        pidController.setReference(value, controlType, pidSlot, arbitraryFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbitraryFeedForward, SparkClosedLoopController.ArbFFUnits arbitraryFeedForwardUnits) {
        pidController.setReference(value, controlType, pidSlot, arbitraryFeedForward, arbitraryFeedForwardUnits);
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

        updatePhysicsSimulation();
        updateMotorSimulation();
        updateEncoderSimulation();
    }

    @Override
    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation, boolean isUsingAbsoluteEncoder) {
        this.physicsSimulation = physicsSimulation;
        this.isUsingAbsoluteEncoder = isUsingAbsoluteEncoder;
    }

    private void updatePhysicsSimulation() {
        physicsSimulation.setInputVoltage(motorSimulation.getBusVoltage() * motorSimulation.getAppliedOutput());
        physicsSimulation.updateMotor();
    }

    private void updateMotorSimulation() {
        motorSimulation.iterate(getVelocity(), RobotHardwareStats.SUPPLY_VOLTAGE, RobotHardwareStats.getPeriodicTimeSeconds());
        motorSimulation.setMotorCurrent(physicsSimulation.getCurrent());
    }

    private void updateEncoderSimulation() {
        absoluteEncoderSimulation.iterate(getVelocity(), RobotHardwareStats.getPeriodicTimeSeconds());
    }

    private double getVelocity() {
        if (isUsingAbsoluteEncoder)
            return getConversionFactor() * Conversions.perMinuteToPerSecond(physicsSimulation.getSystemVelocityRotationsPerSecond());
        return getConversionFactor() * Conversions.perMinuteToPerSecond(physicsSimulation.getRotorVelocityRotationsPerSecond());
    }

    private double getConversionFactor() {
        return motor.configAccessor.encoder.getPositionConversionFactor();
    }
}