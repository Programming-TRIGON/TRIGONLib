package frc.trigon.lib.hardware.rev.spark.io;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.rev.spark.SparkIO;
import frc.trigon.lib.hardware.rev.sparkencoder.SparkEncoder;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;
import frc.trigon.lib.utilities.Conversions;

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
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot) {
        pidController.setReference(value, controlType, slot);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward) {
        pidController.setReference(value, controlType, slot, arbitraryFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward, SparkClosedLoopController.ArbFFUnits arbitraryFeedForwardUnits) {
        pidController.setReference(value, controlType, slot, arbitraryFeedForward, arbitraryFeedForwardUnits);
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
        final SparkMaxConfig configuration = new SparkMaxConfig();
        configuration.inverted(inverted);
        motor.configure(configuration, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
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
        final double physicsSimulationVelocity = getPhysicsSimulationVelocity();
        updateMotorSimulation(physicsSimulationVelocity);
        updateEncoderSimulation(physicsSimulationVelocity);
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

    private void updateMotorSimulation(double physicsSimulationVelocityForSimulation) {
        motorSimulation.iterate(physicsSimulationVelocityForSimulation, RobotHardwareStats.SUPPLY_VOLTAGE, RobotHardwareStats.getPeriodicTimeSeconds());
        motorSimulation.setMotorCurrent(physicsSimulation.getCurrent());
    }

    private void updateEncoderSimulation(double physicsSimulationVelocityForSimulation) {
        absoluteEncoderSimulation.iterate(physicsSimulationVelocityForSimulation, RobotHardwareStats.getPeriodicTimeSeconds());
    }

    /**
     * Gets the velocity from the physics simulation in the units set in the conversion factor. This is used in the iterate method to update the motor simulation.
     *
     * @return the velocity from the physics simulation
     */
    private double getPhysicsSimulationVelocity() {
        if (isUsingAbsoluteEncoder)
            return getVelocityConversionFactor() * Conversions.perMinuteToPerSecond(physicsSimulation.getSystemVelocityRotationsPerSecond());
        return getVelocityConversionFactor() * Conversions.perMinuteToPerSecond(physicsSimulation.getRotorVelocityRotationsPerSecond());
    }

    private double getVelocityConversionFactor() {
        return motor.configAccessor.encoder.getVelocityConversionFactor();
    }
}