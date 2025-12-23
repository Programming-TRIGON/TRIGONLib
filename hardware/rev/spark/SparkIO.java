package frc.trigon.lib.hardware.rev.spark;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import frc.trigon.lib.hardware.rev.sparkencoder.SparkEncoder;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;

public class SparkIO {
    public void setReference(double value, SparkBase.ControlType ctrl) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot slot) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot slot, double arbFeedForward) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot slot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFFUnits) {
    }

    public void setPeriodicFrameTimeout(int timeoutMs) {
    }

    public void stopMotor() {
    }

    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
    }

    public void setInverted(boolean inverted) {
    }

    public void setBrake(boolean brake) {
    }

    public void updateSimulation() {
    }

    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation, boolean isUsingAbsoluteEncoder) {
    }

    public SparkBase getMotor() {
        return null;
    }

    public SparkEncoder getEncoder() {
        return null;
    }
}