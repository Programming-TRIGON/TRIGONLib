package org.trigon.hardware.grapple.lasercan;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.grapple.lasercan.io.RealLaserCANIO;
import org.trigon.hardware.grapple.lasercan.io.SimulationLaserCANIO;

import java.util.function.DoubleSupplier;

public class LaserCANIO {
    static LaserCANIO generateIO(int canID) {
        if (RobotHardwareStats.isReplay())
            return new LaserCANIO();
        else if (RobotHardwareStats.isSimulation())
            return new SimulationLaserCANIO();
        return new RealLaserCANIO(canID);
    }

    protected void updateInputs(LaserCANInputsAutoLogged inputs) {
    }

    protected void setRangingMode(LaserCan.RangingMode rangingMode) throws ConfigurationFailedException {
    }

    protected void setRegionOfInterest(int startX, int startY, int endX, int endY) throws ConfigurationFailedException {
    }

    protected void setSimulationSupplier(DoubleSupplier simulationValueSupplier) {
    }

    protected void setLoopTime(LaserCan.TimingBudget loopTime) throws ConfigurationFailedException {
    }

    protected static class LaserCANInputs {
        public double distanceMillimeters;
    }
}
