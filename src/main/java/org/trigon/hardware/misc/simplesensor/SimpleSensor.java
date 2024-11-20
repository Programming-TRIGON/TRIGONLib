package org.trigon.hardware.misc.simplesensor;

import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.misc.simplesensor.io.AnalogSensorIO;
import org.trigon.hardware.misc.simplesensor.io.DigitalSensorIO;
import org.trigon.hardware.misc.simplesensor.io.DutyCycleSensorIO;
import org.trigon.hardware.misc.simplesensor.io.SimpleSensorSimulationIO;

import java.util.function.DoubleSupplier;

/**
 * A class the represents a sensor, with support for analog, digital, and duty cycle sensors.
 */
public class SimpleSensor {
    private final String name;
    private final SimpleSensorIO sensorIO;
    private final SimpleSensorInputsAutoLogged sensorInputs = new SimpleSensorInputsAutoLogged();
    private double
            scalingSlope = 1,
            scalingInterceptPoint = 0;

    /**
     * Creates a new analog sensor.
     *
     * @param channel The channel of the sensor.
     * @param name    The name of the sensor.
     * @return The new sensor.
     */
    public static SimpleSensor createAnalogSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new AnalogSensorIO(channel), name);
    }

    /**
     * Creates a new digital sensor.
     *
     * @param channel The channel of the sensor.
     * @param name    The name of the sensor.
     * @return The new sensor.
     */
    public static SimpleSensor createDigitalSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new DigitalSensorIO(channel), name);
    }

    /**
     * Creates a new duty cycle sensor.
     *
     * @param channel The channel of the sensor.
     * @param name    The name of the sensor.
     * @return The new sensor.
     */
    public static SimpleSensor createDutyCycleSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new DutyCycleSensorIO(channel), name);
    }

    /**
     * Sets the scaling constants for the sensor. Used to convert the raw sensor value to a more useful unit.
     *
     * @param scalingSlope          The slope of the scaling line.
     * @param scalingInterceptPoint The y-intercept of the scaling line.
     */
    public void setScalingConstants(double scalingSlope, double scalingInterceptPoint) {
        this.scalingSlope = scalingSlope;
        this.scalingInterceptPoint = scalingInterceptPoint;
    }

    /**
     * Gets the value of the sensor.
     *
     * @return The value of the sensor.
     */
    public double getValue() {
        return sensorInputs.value;
    }

    /**
     * Gets the binary value of the sensor. A binary value is a boolean value that is true if the sensor has a value greater than 0.
     *
     * @return The binary value of the sensor.
     */
    public boolean getBinaryValue() {
        return sensorInputs.value > 0;
    }

    /**
     * Gets the scaled value using the scaling constants.
     *
     * @return The scaled value.
     */
    public double getScaledValue() {
        return (sensorInputs.value * scalingSlope) + scalingInterceptPoint;
    }

    /**
     * Sets the simulation supplier for the sensor.
     *
     * @param simulationValueSupplier The simulation supplier.
     */
    public void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        sensorIO.setSimulationSupplier(simulationValueSupplier);
    }

    /**
     * Updates and logs the sensor's inputs.
     */
    public void updateSensor() {
        sensorIO.updateInputs(sensorInputs);
        Logger.processInputs("SimpleSensors/" + name, sensorInputs);
    }

    private static SimpleSensor createNonRealSensor(String name) {
        if (RobotHardwareStats.isReplay())
            return new SimpleSensor(new SimpleSensorIO(), name);
        if (RobotHardwareStats.isSimulation())
            return new SimpleSensor(new SimpleSensorSimulationIO(), name);
        return null;
    }

    private SimpleSensor(SimpleSensorIO sensorIO, String name) {
        this.sensorIO = sensorIO;
        this.name = name;
    }
}
