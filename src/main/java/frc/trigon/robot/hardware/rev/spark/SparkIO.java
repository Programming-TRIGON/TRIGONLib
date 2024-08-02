package frc.trigon.robot.hardware.rev.spark;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.SparkPIDController;
import frc.trigon.robot.hardware.rev.sparkecnoder.SparkEncoder;

public class SparkIO {
    public void setReference(double value, CANSparkBase.ControlType ctrl) {
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot) {
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward) {
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward, SparkPIDController.ArbFFUnits arbFFUnits) {
    }

    public void setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame frame, int periodMs) {
    }

    public void stopMotor() {
    }

    public void setBrake(boolean brake) {
    }

    public void setInverted(boolean inverted) {
    }

    public void enableVoltageCompensation(double voltage) {
    }

    public void setClosedLoopRampRate(double rampRate) {
    }

    public void setOpenLoopRampRate(double rampRate) {
    }

    public void setSmartCurrentLimit(int limit) {
    }

    public void setConversionsFactor(double conversionsFactor) {
    }

    public void setPID(double p, double i, double d) {
    }

    public void restoreFactoryDefaults() {
    }

    public void burnFlash() {
    }

    public void enablePIDWrapping(double minInput, double maxInput) {
    }

    public CANSparkBase getMotor() {
        return null;
    }

    public SparkEncoder getEncoder() {
        return null;
    }
}
