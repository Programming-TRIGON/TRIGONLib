package frc.trigon.robot.hardware.misc.simplesensor;

import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.misc.simplesensor.io.AnalogSensorIO;
import frc.trigon.robot.hardware.misc.simplesensor.io.DigitalSensorIO;
import frc.trigon.robot.hardware.misc.simplesensor.io.DutyCycleSensorIO;
import frc.trigon.robot.hardware.misc.simplesensor.io.SimpleSensorSimulationIO;
import org.littletonrobotics.junction.Logger;

import java.util.function.DoubleSupplier;

public class SimpleSensor {
    private final String name;
    private final SimpleSensorIO sensorIO;
    private final SimpleSensorInputsAutoLogged sensorInputs = new SimpleSensorInputsAutoLogged();

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
        if (RobotConstants.IS_REPLAY)
            return new SimpleSensor(new SimpleSensorIO(), name);
        if (RobotConstants.IS_SIMULATION)
            return new SimpleSensor(new SimpleSensorSimulationIO(), name);
        return null;
    }

    private SimpleSensor(SimpleSensorIO sensorIO, String name) {
        this.sensorIO = sensorIO;
        this.name = name;
    }

    public double getValue() {
        return sensorInputs.value;
    }

    public boolean getBinaryValue() {
        return sensorInputs.value > 0;
    }

    public double getDutyCycleValue(double maximumValue) {
        return (sensorInputs.value - 0.5) * maximumValue;
    }

    public void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        sensorIO.setSimulationSupplier(simulationValueSupplier);
    }

    public void updateSensor() {
        sensorIO.updateInputs(sensorInputs);
        Logger.processInputs("AnalogSensors/" + name, sensorInputs);
    }
}
