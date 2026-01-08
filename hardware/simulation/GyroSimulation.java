package frc.trigon.lib.hardware.simulation;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * A class that represents a simulation of a gyro sensor.
 */
public class GyroSimulation {
    private double simulationYawRadians = 0;

    /**
     * @return the yaw in degrees
     */
    public double getGyroYawDegrees() {
        return Math.toDegrees(simulationYawRadians);
    }

    /**
     * Updates the simulation's current angle in radians based on the angular velocity.
     *
     * @param omegaRadiansPerSecond the angular velocity of the robot in radians per second
     * @param timeSeconds           the time elapsed in seconds since the last update
     */
    public void update(double omegaRadiansPerSecond, double timeSeconds) {
        simulationYawRadians += omegaRadiansPerSecond * timeSeconds;
    }

    /**
     * Sets the yaw of the gyro.
     *
     * @param heading the yaw to set
     */
    public void setYaw(Rotation2d heading) {
        simulationYawRadians = heading.getRadians();
    }
}