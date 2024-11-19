package org.trigon.hardware.simulation;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * A class that represents a simulation of a gyro sensor.
 */
public class GyroSimulation {
    private double simulationRadians = 0;

    /**
     * Gets the yaw of the gyro in degrees.
     *
     * @return The yaw in degrees
     */
    public double getGyroYawDegrees() {
        return Math.toDegrees(simulationRadians);
    }

    /**
     * Updates the gyro simulation.
     *
     * @param omegaRadiansPerSecond The angular velocity of the gyro in radians per second
     * @param timeSeconds           The time in seconds
     */
    public void update(double omegaRadiansPerSecond, double timeSeconds) {
        simulationRadians += omegaRadiansPerSecond * timeSeconds;
    }

    /**
     * Sets the yaw of the gyro.
     *
     * @param heading The yaw to set
     */
    public void setYaw(Rotation2d heading) {
        simulationRadians = heading.getRadians();
    }
}