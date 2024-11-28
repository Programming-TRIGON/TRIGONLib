package org.trigon.hardware.rev.spark.io;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;
import com.revrobotics.spark.config.SparkBaseConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Timer;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;

public class SimulationSparkIO extends SparkIO {
    private final SparkMax motor;
    private final SparkClosedLoopController pidController;
    private final SparkEncoder encoder;
    private SparkSim simulation;

    public SimulationSparkIO(int id) {
        motor = new SparkMax(id, SparkMax.MotorType.kBrushless);
        pidController = motor.getClosedLoopController();
        encoder = SparkEncoder.createRelativeEncoder(motor);
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
    public void setPeriodicFrameTimeout(int periodMs) {
        motor.setPeriodicFrameTimeout(periodMs);
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
    public void setInverted(boolean inverted) {
        motor.setInverted(inverted);
    }

    @Override
    public void updateSimulation() {
        if (simulation == null)
            return;
        simulation.iterate(motor.getAbsoluteEncoder().getVelocity(), motor.getBusVoltage(), Timer.getFPGATimestamp());
        simulation.setAppliedOutput(motor.getAppliedOutput());
        simulation.setMotorCurrent(motor.getOutputCurrent());
    }

    @Override
    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setSimulationGearbox(DCMotor gearbox) {
        simulation = new SparkSim(motor, gearbox);
    }
}