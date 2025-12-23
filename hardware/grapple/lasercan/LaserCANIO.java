package frc.trigon.lib.hardware.grapple.lasercan;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.grapple.lasercan.io.RealLaserCANIO;
import frc.trigon.lib.hardware.grapple.lasercan.io.SimulationLaserCANIO;
import org.littletonrobotics.junction.AutoLog;

import java.util.function.DoubleSupplier;

public class LaserCANIO {
    static LaserCANIO generateIO(int id) {
        if (RobotHardwareStats.isReplay())
            return new LaserCANIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationLaserCANIO();
        return new RealLaserCANIO(id);
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

    @AutoLog
    protected static class LaserCANInputs {
        /**
         * The distance in millimeters of the scanned object from the sensor.
         */
        public double distanceMillimeters = 0;
        /**
         * The ambient light in the area of the sensor.
         */
        public int ambientLight = 0;
        /**
         * Whether the sensor has a result or not.
         */
        public boolean hasResult = false;
        /**
         * Whether the sensor has high noise or not.
         */
        public boolean highNoise = false;
        /**
         * Whether the sensor has a weak signal or not.
         * It is recommended to lower the loop time or decrease the region of interest if this occurs.
         */
        public boolean weakSignal = false;
        /**
         * Whether the result is out of bounds or not.
         * This usually only happens if the sensed object is bright.
         */
        public boolean outOfBounds = false;
        /**
         * Whether the result is a wrapped around or not.
         * This usually only happens for highly reflective objects outside the sensor's theoretical range.
         * If this happens, the result is actually the distance to the object plus the sensor's maximum range.
         */
        public boolean wrapAround = false;
    }
}
