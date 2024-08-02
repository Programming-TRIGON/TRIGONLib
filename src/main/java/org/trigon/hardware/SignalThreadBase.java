package org.trigon.hardware;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SignalThreadBase extends Thread {
    public static final ReentrantLock SIGNALS_LOCK = new ReentrantLock();
    protected final Queue<Double> timestamps = new ArrayBlockingQueue<>(100);
    private final ThreadInputsAutoLogged threadInputs = new ThreadInputsAutoLogged();
    private final String name;
    protected double odometryFrequencyHertz = 50;

    public SignalThreadBase(String name) {
        this.name = name;
    }

    public void setOdometryFrequencyHertz(double odometryFrequencyHertz) {
        this.odometryFrequencyHertz = odometryFrequencyHertz;
    }

    public void updateLatestTimestamps() {
        if (!RobotHardwareStats.isReplay()) {
            threadInputs.timestamps = timestamps.stream().mapToDouble(Double::doubleValue).toArray();
            timestamps.clear();
        }
        Logger.processInputs(name, threadInputs);
    }

    public double[] getLatestTimestamps() {
        return threadInputs.timestamps;
    }

    @AutoLog
    public static class ThreadInputs {
        public double[] timestamps;
    }
}
