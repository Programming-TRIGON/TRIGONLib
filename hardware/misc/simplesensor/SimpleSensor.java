package frc.trigon.lib.hardware.misc.simplesensor;

import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.misc.simplesensor.io.AnalogSensorIO;
import frc.trigon.lib.hardware.misc.simplesensor.io.DigitalSensorIO;
import frc.trigon.lib.hardware.misc.simplesensor.io.DutyCycleSensorIO;
import frc.trigon.lib.hardware.misc.simplesensor.io.SimpleSensorSimulationIO;
import org.littletonrobotics.junction.Logger;

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
     * @param channel the channel of the sensor
     * @param name    the name of the sensor
     * @return the new sensor
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
     * @param channel the channel of the sensor
     * @param name    the name of the sensor
     * @return the new sensor
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
     * @param channel the channel of the sensor
     * @param name    the name of the sensor
     * @return the new sensor
     */
    public static SimpleSensor createDutyCycleSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new DutyCycleSensorIO(channel), name);
    }

    /**
     * Creates a non-real sensor to be used if the robot is in replay or simulation. Returns null if the robot is real.
     * This is used in the create methods to avoid creating a real sensor when the robot is in replay or simulation.
     *
     * @param name the name of the sensor
     * @return the non-real sensor
     */
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

    /**
     * Sets the scaling constants for the sensor. Used in {@link SimpleSensor#getScaledValue()} to convert the raw sensor value to a more useful unit.
     *
     * @param scalingSlope          the slope of the scaling line
     * @param scalingInterceptPoint the y-intercept of the scaling line
     */
    public void setScalingConstants(double scalingSlope, double scalingInterceptPoint) {
        this.scalingSlope = scalingSlope;
        this.scalingInterceptPoint = scalingInterceptPoint;
    }

    /**
     * @return the value from the sensor
     */
    public double getValue() {
        return sensorInputs.value;
    }

    /**
     * Gets the binary value of the sensor. A binary value is a boolean value that is true if the sensor has a value greater than 0.
     *
     * @return the binary value of the sensor
     */
    public boolean getBinaryValue() {
        return sensorInputs.value > 0;
    }

    /**
     * Gets the scaled value from the sensor using the scaling constants.
     * In simulation, just gets the value from the simulation supplier.
     *
     * @return the scaled value
     */
    public double getScaledValue() {
        return RobotHardwareStats.isSimulation() ? sensorInputs.value : (sensorInputs.value * scalingSlope) + scalingInterceptPoint;
    }

    /**
     * Sets the simulation supplier for the sensor. This supplier is used to get the value of the sensor in simulation.
     *
     * @param simulationValueSupplier the simulation supplier
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
}