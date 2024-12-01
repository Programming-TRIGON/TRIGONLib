package org.trigon.hardware.rev.spark.io;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SimulationSparkIO extends SparkIO {
    private final SparkMax motor;
    private final SparkClosedLoopController pidController;
    private final SparkEncoder encoder;
    private SparkSim motorSimulation = null;
    private SparkAbsoluteEncoderSim absoluteEncoderSimulation = null;
    private SparkRelativeEncoderSim relativeEncoderSimulation = null;
    private MotorPhysicsSimulation physicsSimulation = null;

    public SimulationSparkIO(int id) {
        motor = new SparkMax(id, SparkMax.MotorType.kBrushless);
        pidController = motor.getClosedLoopController();
        encoder = SparkEncoder.createEncoder(motor);
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
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward) {
        pidController.setReference(value, controlType, pidSlot, arbFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFFUnits) {
        pidController.setReference(value, controlType, pidSlot, arbFeedForward, arbFFUnits);
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
        SparkMaxConfig configuration = new SparkMaxConfig();
        configuration.idleMode(brake ? SparkMaxConfig.IdleMode.kBrake : SparkMaxConfig.IdleMode.kCoast);
        motor.configure(configuration, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);
    }

    @Override
    public void updateSimulation() {
        if (physicsSimulation == null)
            return;

//        physicsSimulation.setInputVoltage(motorSimulation.getAppliedOutput());
        physicsSimulation.setInputVoltage(motor.getBusVoltage());
        physicsSimulation.updateMotor();
        System.out.println(motor.getBusVoltage() + " motor bus voltage");
        System.out.println(motor.getAppliedOutput() + " motor applied output");

        motorSimulation.iterate(physicsSimulation.getRotorVelocityRotationsPerSecond(), RobotHardwareStats.SUPPLY_VOLTAGE, RobotHardwareStats.getPeriodicTimeSeconds());
        if (isUsingAbsoluteEncoder()) {
            absoluteEncoderSimulation.iterate(physicsSimulation.getSystemVelocityRotationsPerSecond(), RobotHardwareStats.getPeriodicTimeSeconds());
            return;
        }
        relativeEncoderSimulation.iterate(physicsSimulation.getRotorVelocityRotationsPerSecond(), RobotHardwareStats.getPeriodicTimeSeconds());
    }

    @Override
    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation, boolean isUsingAbsoluteEncoder) {
        this.physicsSimulation = physicsSimulation;
        if (motorSimulation == null)
            motorSimulation = new SparkSim(motor, physicsSimulation.getGearbox());
        if (isUsingAbsoluteEncoder && absoluteEncoderSimulation == null) {
            absoluteEncoderSimulation = new SparkAbsoluteEncoderSim(motor);
            return;
        }
        if (relativeEncoderSimulation == null)
            relativeEncoderSimulation = new SparkRelativeEncoderSim(motor);
    }

    private boolean isUsingAbsoluteEncoder() {
        return absoluteEncoderSimulation != null;
    }
}