package org.trigon.hardware.misc.servo.io;

import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

import java.util.function.DoubleSupplier;

public class SimulationServoIO extends ServoIO {
    private DoubleSupplier valueSupplier = null;

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        if (valueSupplier == null)
            return;
        inputs.positionRotations = valueSupplier.getAsDouble();
        inputs.speed = 0;
    }

    @Override
    protected void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        valueSupplier = simulationValueSupplier;
    }
}