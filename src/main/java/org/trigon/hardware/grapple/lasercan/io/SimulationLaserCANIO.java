package org.trigon.hardware.grapple.lasercan.io;

import org.trigon.hardware.grapple.lasercan.LaserCANIO;
import org.trigon.hardware.grapple.lasercan.LaserCANInputsAutoLogged;

import java.util.function.DoubleSupplier;

public class SimulationLaserCANIO extends LaserCANIO {
    private DoubleSupplier valueSupplier = null;

    @Override
    protected void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
        this.valueSupplier = simulationValueSupplier;
    }

    @Override
    public void updateInputs(LaserCANInputsAutoLogged inputs) {
        if (valueSupplier == null)
            return;
        inputs.distanceMillimeters = valueSupplier.getAsDouble();
        inputs.ambientLight = 0;
        inputs.hasResult = false;
    }
}
