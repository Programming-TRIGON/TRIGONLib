package frc.trigon.lib.hardware.simulation;

/**
 * An abstract class to simulate the physics of a motor.
 */
public abstract class MotorPhysicsSimulation {
    private final double gearRatio;

    MotorPhysicsSimulation(double gearRatio) {
        this.gearRatio = gearRatio;
    }

    public double getRotorPositionRotations() {
        return getSystemPositionRotations() * gearRatio;
    }

    public double getRotorVelocityRotationsPerSecond() {
        return getSystemVelocityRotationsPerSecond() * gearRatio;
    }

    public abstract double getCurrent();

    public abstract double getSystemPositionRotations();

    public abstract double getSystemVelocityRotationsPerSecond();

    public abstract void setInputVoltage(double voltage);

    public abstract void updateMotor();
}