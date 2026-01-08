package frc.trigon.lib.hardware.grapple.lasercan;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import org.littletonrobotics.junction.Logger;

import java.util.function.DoubleSupplier;

/**
 * A LaserCAN is a sensor that measures the distance of an object in millimeters.
 * A LaserCAN also has the capabilities to scan from only a certain region of the scanner's area.
 */
public class LaserCAN {
    private final String name;
    private final LaserCANIO laserCANIO;
    private final LaserCANInputsAutoLogged inputs = new LaserCANInputsAutoLogged();

    /**
     * Creates a new LaserCAN with the given CAN ID and name.
     *
     * @param id   the ID of the LaserCAN on the CANBUS
     * @param name the name of the LaserCAN
     */
    public LaserCAN(int id, String name) {
        this.name = name;
        laserCANIO = LaserCANIO.generateIO(id);
    }

    /**
     * Updates the sensor and logs its inputs.
     */
    public void update() {
        laserCANIO.updateInputs(inputs);
        Logger.processInputs("LaserCANs/" + name, inputs);
    }

    /**
     * Sets the simulation supplier for the sensor.
     * This supplier is used to get the value of the sensor in simulation.
     *
     * @param simulationValueSupplier the simulation supplier
     */
    public void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        laserCANIO.setSimulationSupplier(simulationValueSupplier);
    }

    /**
     * Sets the distance that the LaserCAN senses at.
     * Long range is 4m, Short range is 1.3m.
     *
     * @param rangingMode the ranging mode to set the LaserCAN to
     * @throws ConfigurationFailedException if the ranging mode could not be set
     */
    public void setRangingMode(LaserCan.RangingMode rangingMode) throws ConfigurationFailedException {
        laserCANIO.setRangingMode(rangingMode);
    }

    /**
     * Sets the area that the LaserCAN will scan from.
     * The area is a 16x16 grid originating from the bottom left (?).
     *
     * @param startX the starting x coordinate
     * @param startY the starting y coordinate
     * @param endX   the ending x coordinate
     * @param endY   the ending y coordinate
     * @throws ConfigurationFailedException if the region of interest could not be set
     */
    public void setRegionOfInterest(int startX, int startY, int endX, int endY) throws ConfigurationFailedException {
        laserCANIO.setRegionOfInterest(startX, startY, endX, endY);
    }

    /**
     * Sets the time it takes for the sensor to take a measurement.
     *
     * @param loopTime the desired time for the sensor to take a measurement
     * @throws ConfigurationFailedException if the loop time could not be set
     */
    public void setLoopTime(LaserCan.TimingBudget loopTime) throws ConfigurationFailedException {
        laserCANIO.setLoopTime(loopTime);
    }

    /**
     * @return the distance measured by the sensor in millimeters
     */
    public double getDistanceMillimeters() {
        return inputs.distanceMillimeters;
    }

    /**
     * @return the ambient light measured by the sensor
     */
    public int getAmbientLight() {
        return inputs.ambientLight;
    }

    /**
     * @return whether the sensor has a result
     */
    public boolean hasResult() {
        return inputs.hasResult;
    }

    /**
     * @return whether the result of the sensor has high noise or not
     */
    public boolean hasHighNoise() {
        return inputs.highNoise;
    }

    /**
     * @return whether the sensor has a weak signal
     */
    public boolean hasWeakSignal() {
        return inputs.weakSignal;
    }

    /**
     * @return whether the result of the sensor is out of bounds
     */
    public boolean isOutOfBounds() {
        return inputs.outOfBounds;
    }

    /**
     * @return whether the result of the sensor is a wraparound
     */
    public boolean isWrapAround() {
        return inputs.wrapAround;
    }
}
