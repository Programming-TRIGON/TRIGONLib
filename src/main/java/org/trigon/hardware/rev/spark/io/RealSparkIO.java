package org.trigon.hardware.rev.spark.io;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;
import org.trigon.hardware.rev.spark.SparkIO;
import org.trigon.hardware.rev.spark.SparkType;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;

public class RealSparkIO extends SparkIO {
    private final SparkBase motor;
    private final SparkClosedLoopController pidController;
    private final SparkEncoder encoder;

    public RealSparkIO(int id, SparkType sparkType) {
        motor = sparkType.sparkCreator.apply(id);
        pidController = motor.getClosedLoopController();
        encoder = SparkEncoder.createEncoder(motor);
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
    public void configure(SparkMaxConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setInverted(boolean inverted) {
        motor.setInverted(inverted);
    }
}