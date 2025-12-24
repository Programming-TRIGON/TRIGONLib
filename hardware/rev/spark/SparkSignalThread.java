// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.trigon.lib.hardware.rev.spark;

import edu.wpi.first.wpilibj.Notifier;
import org.littletonrobotics.junction.Logger;
import frc.trigon.lib.hardware.SignalThreadBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.DoubleSupplier;

/**
 * Provides an interface for asynchronously reading high-frequency measurements to a set of queues.
 *
 * <p>This version is intended for devices like the SparkMax that require polling rather than a
 * blocking thread. A Notifier thread is used to gather samples with consistent timing.
 */
public class SparkSignalThread extends SignalThreadBase {
    public static final boolean ACTIVE = false;
    private final List<DoubleSupplier> signals = new ArrayList<>();
    private final List<Queue<Double>> queues = new ArrayList<>();
    private static SparkSignalThread instance = null;

    public static SparkSignalThread getInstance() {
        if (instance == null)
            instance = new SparkSignalThread();
        return instance;
    }

    private SparkSignalThread() {
        super("SparkSignalThread");
        if (ACTIVE) {
            Notifier notifier = new Notifier(this::periodic);
            notifier.setName("SparkSignalThread");
            notifier.startPeriodic(1.0 / super.threadFrequencyHertz);
        }
    }

    /**
     * Registers a threaded signal to be read asynchronously.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the signal to register
     * @return the queue that the signal's values will be written to
     */
    public Queue<Double> registerThreadedSignal(DoubleSupplier signal) {
        Queue<Double> queue = new ArrayBlockingQueue<>(100);
        SIGNALS_LOCK.lock();
        try {
            signals.add(signal);
            queues.add(queue);
        } finally {
            SIGNALS_LOCK.unlock();
        }
        return queue;
    }

    private void periodic() {
        SIGNALS_LOCK.lock();
        timestamps.offer(Logger.getRealTimestamp() / 1.0e6);
        try {
            for (int i = 0; i < signals.size(); i++)
                queues.get(i).offer(signals.get(i).getAsDouble());
        } finally {
            SIGNALS_LOCK.unlock();
        }
    }
}