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

package frc.trigon.robot.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import edu.wpi.first.wpilibj.Timer;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.SignalThreadBase;
import frc.trigon.robot.poseestimation.poseestimator.PoseEstimatorConstants;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class Phoenix6SignalThread extends SignalThreadBase {
    public static ReentrantLock SIGNALS_LOCK = new ReentrantLock();
    private static final boolean IS_CAN_FD = true;
    private final List<Queue<Double>> queues = new ArrayList<>();
    private BaseStatusSignal[] signals = new BaseStatusSignal[0];

    private static Phoenix6SignalThread INSTANCE = null;

    public static Phoenix6SignalThread getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Phoenix6SignalThread();
        }
        return INSTANCE;
    }

    private Phoenix6SignalThread() {
        super("Phoenix6SignalThread");
        if (RobotConstants.IS_REPLAY)
            return;
        setName("Phoenix6SignalThread");
        setDaemon(true);
        start();
    }

    public Queue<Double> registerSignal(BaseStatusSignal signal, BaseStatusSignal slopeSignal) {
        Queue<Double> queue = new ArrayBlockingQueue<>(100);
        SIGNALS_LOCK.lock();
        try {
            BaseStatusSignal[] newSignals = new BaseStatusSignal[signals.length + 2];
            System.arraycopy(signals, 0, newSignals, 0, signals.length);
            newSignals[signals.length] = signal;
            newSignals[signals.length + 1] = slopeSignal;
            signals = newSignals;
            queues.add(queue);
        } finally {
            SIGNALS_LOCK.unlock();
        }
        return queue;
    }

    @Override
    public void run() {
        Timer.delay(5);
        while (true) {
            // Wait for updates from all signals
            try {
                if (IS_CAN_FD) {
                    BaseStatusSignal.waitForAll(RobotConstants.PERIODIC_TIME_SECONDS, signals);
                } else {
                    Thread.sleep((long) (1000.0 / PoseEstimatorConstants.ODOMETRY_FREQUENCY_HERTZ));
                    if (signals.length > 0) BaseStatusSignal.refreshAll(signals);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double fpgaTimestamp = Logger.getRealTimestamp() / 1.0e6;

            // Save new data to queues
            SIGNALS_LOCK.lock();
            try {
                for (int i = 0; i < signals.length; i += 2) {
                    final StatusSignal<Double> signal = (StatusSignal<Double>) signals[i];
                    final StatusSignal<Double> slopeSignal = (StatusSignal<Double>) signals[i + 1];
                    final double latencyCompensatedValue = BaseStatusSignal.getLatencyCompensatedValue(signal, slopeSignal);
                    queues.get(i / 2).offer(latencyCompensatedValue);
                }
                timestamps.offer(fpgaTimestamp);
            } finally {
                SIGNALS_LOCK.unlock();
            }
        }
    }
}