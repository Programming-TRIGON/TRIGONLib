package org.trigon.hardware.rev.spark.io;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;

public class SimulationSparkIO extends SparkIO {
    private final SparkMax motor;
    private final SparkClosedLoopController pidController;
    private final SparkEncoder encoder;
    private final SparkMaxSim simulation;

    public SimulationSparkIO(int id, DCMotor gearbox) {
        motor = new SparkMax(id, SparkMax.MotorType.kBrushless);
        simulation = new SparkMaxSim(motor, gearbox);
        pidController = motor.getClosedLoopController();
        encoder = SparkEncoder.createRelativeEncoder(motor);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType ctrl) {
        pidController.setReference(value, ctrl);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot) {
        pidController.setReference(value, ctrl, pidSlot);
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
        simulation.setAppliedOutput(0);
    }

    @Override
    public void setPeriodicFrameTimeout(int periodMs) {
        motor.setPeriodicFrameTimeout(periodMs);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot, double arbFeedForward) {
        pidController.setReference(value, ctrl, pidSlot, arbFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFFUnits) {
        pidController.setReference(value, ctrl, pidSlot, arbFeedForward, arbFFUnits);
    }

    @Override
    public void setInverted(boolean inverted) {
        motor.setInverted(inverted);
    }

    @Override
    public void configure(SparkMaxConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }
}