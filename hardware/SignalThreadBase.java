package frc.trigon.lib.hardware;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A class that represents a base for a signal thread.
 * Signal threads are specialized threads that run at a specific frequency to handle updating signals.
 */
public class SignalThreadBase extends Thread {
    public static final ReentrantLock SIGNALS_LOCK = new ReentrantLock();
    protected final Queue<Double> timestamps = new ArrayBlockingQueue<>(100);
    private final ThreadInputsAutoLogged threadInputs = new ThreadInputsAutoLogged();
    private final String name;
    protected double threadFrequencyHertz = 50;

    @SuppressWarnings("ConstantConditions")
    public static double[] queueToDoubleArray(Queue<Double> queue) {
        final double[] array = new double[queue.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = queue.poll();

        return array;
    }

    /**
     * Creates a new SignalThreadBase.
     *
     * @param name the name of the thread
     */
    public SignalThreadBase(String name) {
        this.name = name;
    }

    /**
     * Sets the thread frequency in hertz.
     * The thread frequency determines how often the robot's position and motion data are updated.
     * A higher frequency will result in more frequent updates, but may also demand more processing power.
     * Only used for Spark motors.
     *
     * @param threadFrequencyHertz the odometry frequency in hertz
     */
    public void setThreadFrequencyHertz(double threadFrequencyHertz) {
        this.threadFrequencyHertz = threadFrequencyHertz;
    }

    /**
     * Updates the latest timestamps, and processes the inputs.
     */
    public void updateLatestTimestamps() {
        if (!RobotHardwareStats.isReplay())
            threadInputs.timestamps = queueToDoubleArray(timestamps);

        Logger.processInputs(name, threadInputs);
    }

    /**
     * Gets the latest timestamps when signals were updated.
     *
     * @return the latest timestamps
     */
    public double[] getLatestTimestamps() {
        return threadInputs.timestamps;
    }

    @AutoLog
    public static class ThreadInputs {
        public double[] timestamps;
    }
}