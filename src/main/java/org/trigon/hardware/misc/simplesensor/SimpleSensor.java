package org.trigon.hardware.misc.simplesensor;

import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.misc.simplesensor.io.AnalogSensorIO;
import org.trigon.hardware.misc.simplesensor.io.DigitalSensorIO;
import org.trigon.hardware.misc.simplesensor.io.DutyCycleSensorIO;
import org.trigon.hardware.misc.simplesensor.io.SimpleSensorSimulationIO;

import java.util.function.DoubleSupplier;

public class SimpleSensor {
    private final String name;
    private final SimpleSensorIO sensorIO;
    private final SimpleSensorInputsAutoLogged sensorInputs = new SimpleSensorInputsAutoLogged();
    private double
            scalingSlope = 1,
            scalingInterceptPoint = 0;

    public static SimpleSensor createAnalogSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new AnalogSensorIO(channel), name);
    }

    public static SimpleSensor createDigitalSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new DigitalSensorIO(channel), name);
    }

    public static SimpleSensor createDutyCycleSensor(int channel, String name) {
        final SimpleSensor nonRealSensor = createNonRealSensor(name);
        if (nonRealSensor != null)
            return nonRealSensor;
        return new SimpleSensor(new DutyCycleSensorIO(channel), name);
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

    public void setScalingConstants(double scalingSlope, double scalingInterceptPoint) {
        this.scalingSlope = scalingSlope;
        this.scalingInterceptPoint = scalingInterceptPoint;
    }

    public double getValue() {
        return sensorInputs.value;
    }

    public boolean getBinaryValue() {
        return sensorInputs.value > 0;
    }

    public double getScaledValue() {
        return (sensorInputs.value * scalingSlope) + scalingInterceptPoint;
    }

    public void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        sensorIO.setSimulationSupplier(simulationValueSupplier);
    }

    public void updateSensor() {
        sensorIO.updateInputs(sensorInputs);
        Logger.processInputs("SimpleSensors/" + name, sensorInputs);
    }
}
