package frc.trigon.lib.hardware.grapple.lasercan.io;

import frc.trigon.lib.hardware.grapple.lasercan.LaserCANIO;
import frc.trigon.lib.hardware.grapple.lasercan.LaserCANInputsAutoLogged;

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
        inputs.hasResult = true;
    }
}
