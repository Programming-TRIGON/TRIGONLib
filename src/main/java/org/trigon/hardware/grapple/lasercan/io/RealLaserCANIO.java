package org.trigon.hardware.grapple.lasercan.io;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import org.trigon.hardware.grapple.lasercan.LaserCANIO;
import org.trigon.hardware.grapple.lasercan.LaserCANInputsAutoLogged;

public class RealLaserCANIO extends LaserCANIO {
    private final LaserCan laserCan;

    public RealLaserCANIO(int canID) {
        laserCan = new LaserCan(canID);
    }

    public void updateInputs(LaserCANInputsAutoLogged inputs) {
        if (laserCan.getMeasurement() != null)
            inputs.distanceMillimeters = laserCan.getMeasurement().distance_mm;
    }

    @Override
    protected void setRangingMode(LaserCan.RangingMode rangingMode) throws ConfigurationFailedException {
        laserCan.setRangingMode(rangingMode);
    }


    @Override
    protected void setRegionOfInterest(int startX, int startY, int endX, int endY) throws ConfigurationFailedException {
        laserCan.setRegionOfInterest(new LaserCan.RegionOfInterest(startX, startY, endX - startX, endY - startY));
    }

    @Override
    protected void setLoopTime(LaserCan.TimingBudget loopTime) throws ConfigurationFailedException {
        laserCan.setTimingBudget(loopTime);
    }
}
