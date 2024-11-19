package org.trigon.hardware;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A class that represents a base for a signal thread.
 */
public class SignalThreadBase extends Thread {
    public static final ReentrantLock SIGNALS_LOCK = new ReentrantLock();
    protected final Queue<Double> timestamps = new ArrayBlockingQueue<>(100);
    private final ThreadInputsAutoLogged threadInputs = new ThreadInputsAutoLogged();
    private final String name;
    protected double odometryFrequencyHertz = 50;

    /**
     * Creates a new SignalThreadBase.
     *
     * @param name The name of the thread
     */
    public SignalThreadBase(String name) {
        this.name = name;
    }

    /**
     * Sets the odometry frequency in hertz.
     *
     * @param odometryFrequencyHertz The odometry frequency in hertz
     */
    public void setOdometryFrequencyHertz(double odometryFrequencyHertz) {
        this.odometryFrequencyHertz = odometryFrequencyHertz;
    }

    /**
     * Updates the latest timestamps, and processes the inputs.
     */
    public void updateLatestTimestamps() {
        if (!RobotHardwareStats.isReplay()) {
            threadInputs.timestamps = timestamps.stream().mapToDouble(Double::doubleValue).toArray();
            timestamps.clear();
        }
        Logger.processInputs(name, threadInputs);
    }

    /**
     * Gets the latest timestamps.
     *
     * @return The latest timestamps
     */
    public double[] getLatestTimestamps() {
        return threadInputs.timestamps;
    }

    @AutoLog
    public static class ThreadInputs {
        public double[] timestamps;
    }
}
