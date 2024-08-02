package org.trigon.hardware.misc.simplesensor.io;

import org.trigon.hardware.misc.simplesensor.SimpleSensorIO;
import org.trigon.hardware.misc.simplesensor.SimpleSensorInputsAutoLogged;

import java.util.function.DoubleSupplier;

public class SimpleSensorSimulationIO extends SimpleSensorIO {
    private DoubleSupplier valueSupplier = null;

    @Override
    protected void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        this.valueSupplier = simulationValueSupplier;
    }

    @Override
    public void updateInputs(SimpleSensorInputsAutoLogged inputs) {
        if (valueSupplier == null)
            return;
        inputs.value = valueSupplier.getAsDouble();
    }
}
