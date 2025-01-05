package org.trigon.hardware.rev.spark;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import org.trigon.hardware.rev.sparkencoder.SparkEncoder;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SparkIO {
    public void setReference(double value, SparkBase.ControlType ctrl) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot pidSlot) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot pidSlot, double arbFeedForward) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, ClosedLoopSlot pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFFUnits) {
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