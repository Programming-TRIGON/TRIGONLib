package org.trigon.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;

/**
 * An abstract class to simulate the physics of a motor.
 */
public abstract class MotorPhysicsSimulation {
    private final double gearRatio;
    private final DCMotor gearbox;

    MotorPhysicsSimulation(DCMotor gearbox, double gearRatio) {
        this.gearRatio = gearRatio;
        this.gearbox = gearbox;
    }

    public double getRotorPositionRotations() {
        return getSystemPositionRotations() * gearRatio;
    }

    public double getRotorVelocityRotationsPerSecond() {
        return getSystemVelocityRotationsPerSecond() * gearRatio;
    }

    public DCMotor getGearbox() {
        return gearbox;
    }

    public abstract double getCurrent();

    public abstract double getSystemPositionRotations();

    public abstract double getSystemVelocityRotationsPerSecond();

    public abstract void setInputVoltage(double voltage);

    public abstract void updateMotor();
}