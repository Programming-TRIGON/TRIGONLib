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
    protected double threadFrequencyHertz = 50;

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
     * The odometry frequency determines how often the robot's position and motion data are updated.
     * A higher frequency will result in more frequent updates, but may also demand more processing power.
     * Only used for Spark motors.
     *
     * @param odometryFrequencyHertz the odometry frequency in hertz
     */
    public void setThreadFrequencyHertz(double odometryFrequencyHertz) {
        this.threadFrequencyHertz = odometryFrequencyHertz;
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
     * Gets the latest timestamps when events occurred within the thread's execution.
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