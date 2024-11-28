package org.trigon.hardware.rev.spark;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;

public class SparkIO {
    public void setReference(double value, SparkBase.ControlType ctrl) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot, double arbFeedForward) {
    }

    public void setReference(double value, SparkBase.ControlType ctrl, int pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFFUnits) {
    }

    public void setPeriodicFrameTimeout(int periodMs) {
    }

    public void stopMotor() {
    }

    public void configure(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
    }

    public void setInverted(boolean inverted) {
    }

    public void updateSimulation() {
    }

    public void setSimulationGearbox(DCMotor gearbox) {
    }

    public SparkBase getMotor() {
        return null;
    }

    public SparkEncoder getEncoder() {
        return null;
    }
}