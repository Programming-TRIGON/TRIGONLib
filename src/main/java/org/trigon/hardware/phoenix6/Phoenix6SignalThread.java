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

package org.trigon.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import edu.wpi.first.wpilibj.Timer;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.SignalThreadBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides an interface for asynchronously reading high-frequency measurements to a set of queues for Phoenix 6.
 */
public class Phoenix6SignalThread extends SignalThreadBase {
    public static ReentrantLock SIGNALS_LOCK = new ReentrantLock();
    private final List<Queue<Double>> queues = new ArrayList<>();
    private BaseStatusSignal[] signals = new BaseStatusSignal[0];

    private static Phoenix6SignalThread INSTANCE = null;

    public static Phoenix6SignalThread getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Phoenix6SignalThread();
        return INSTANCE;
    }

    private Phoenix6SignalThread() {
        super("Phoenix6SignalThread");
        if (RobotHardwareStats.isReplay())
            return;
        setName("Phoenix6SignalThread");
        setDaemon(true);
        start();
    }

    /**
     * Registers a signal to be read asynchronously.
     *
     * @param signal the signal to register
     * @return the queue that the signal's values will be written to
     */
    public Queue<Double> registerSignal(BaseStatusSignal signal) {
        Queue<Double> queue = new ArrayBlockingQueue<>(100);
        SIGNALS_LOCK.lock();
        try {
            addSignalToSignalsArray(signal);
            queues.add(queue);
        } finally {
            SIGNALS_LOCK.unlock();
        }
        return queue;
    }

    @Override
    public void run() {
        Timer.delay(5);

        while (true)
            updateValues();
    }

    private void updateValues() {
        if (BaseStatusSignal.waitForAll(RobotHardwareStats.getPeriodicTimeSeconds(), signals) != StatusCode.OK)
            return;
        final double updateTimestamp = (Logger.getRealTimestamp() / 1e6) - signals[0].getTimestamp().getLatency();

        SIGNALS_LOCK.lock();
        try {
            updateQueues(updateTimestamp);
        } finally {
            SIGNALS_LOCK.unlock();
        }
    }

    private void updateQueues(double updateTimestamp) {
        for (int i = 0; i < signals.length; i += 1) {
            final BaseStatusSignal signal = signals[i];
            queues.get(i).offer(signal.getValueAsDouble());
        }

        timestamps.offer(updateTimestamp);
    }

    private void addSignalToSignalsArray(BaseStatusSignal statusSignal) {
        final BaseStatusSignal[] newSignals = new BaseStatusSignal[signals.length + 1];
        System.arraycopy(signals, 0, newSignals, 0, signals.length);
        newSignals[signals.length] = statusSignal;
        signals = newSignals;
    }
}