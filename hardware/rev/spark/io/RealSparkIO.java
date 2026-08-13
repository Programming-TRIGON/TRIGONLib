package frc.trigon.lib.hardware.rev.spark.io;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.trigon.lib.hardware.rev.spark.SparkIO;
import frc.trigon.lib.hardware.rev.spark.SparkType;
import frc.trigon.lib.hardware.rev.sparkencoder.SparkEncoder;

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
    public void setReference(double value, SparkBase.ControlType controlType) {
        pidController.setSetpoint(value, controlType);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot) {
        pidController.setSetpoint(value, controlType, slot);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward) {
        pidController.setSetpoint(value, controlType, slot, arbitraryFeedForward);
    }

    @Override
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward, SparkClosedLoopController.ArbFFUnits arbitraryFeedForwardUnits) {
        pidController.setSetpoint(value, controlType, slot, arbitraryFeedForward, arbitraryFeedForwardUnits);
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
    public void configure(SparkBaseConfig configuration, ResetMode resetMode, PersistMode persistMode) {
        motor.configure(configuration, resetMode, persistMode);
    }

    @Override
    public void setInverted(boolean inverted) {
        final SparkMaxConfig configuration = new SparkMaxConfig();
        configuration.inverted(inverted);
        motor.configure(configuration, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    @Override
    public void setBrake(boolean brake) {
        final SparkMaxConfig configuration = new SparkMaxConfig();
        configuration.idleMode(brake ? SparkMaxConfig.IdleMode.kBrake : SparkMaxConfig.IdleMode.kCoast);
        motor.configure(configuration, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }
}