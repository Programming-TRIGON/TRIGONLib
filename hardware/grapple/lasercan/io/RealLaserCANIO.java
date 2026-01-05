package frc.trigon.lib.hardware.grapple.lasercan.io;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import frc.trigon.lib.hardware.grapple.lasercan.LaserCANIO;
import frc.trigon.lib.hardware.grapple.lasercan.LaserCANInputsAutoLogged;

public class RealLaserCANIO extends LaserCANIO {
    private final LaserCan laserCan;

    public RealLaserCANIO(int id) {
        laserCan = new LaserCan(id);
    }

    public void updateInputs(LaserCANInputsAutoLogged inputs) {
        final LaserCan.Measurement measurement = laserCan.getMeasurement();
        if (measurement != null) {
            inputs.hasResult = measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT;
            if (!inputs.hasResult)
                return;

            inputs.distanceMillimeters = laserCan.getMeasurement().distance_mm;
            inputs.ambientLight = measurement.ambient;
            inputs.highNoise = measurement.status == LaserCan.LASERCAN_STATUS_NOISE_ISSUE;
            inputs.weakSignal = measurement.status == LaserCan.LASERCAN_STATUS_WEAK_SIGNAL;
            inputs.outOfBounds = measurement.status == LaserCan.LASERCAN_STATUS_OUT_OF_BOUNDS;
            inputs.wrapAround = measurement.status == LaserCan.LASERCAN_STATUS_WRAPAROUND;
        }
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