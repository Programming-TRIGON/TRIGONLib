package org.trigon.hardware.rev.spark;

import org.littletonrobotics.junction.LogTable;
import org.trigon.hardware.BaseInputs;
import org.trigon.hardware.RobotHardwareStats;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class SparkInputs extends BaseInputs {
    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final SparkSignalThread signalThread = SparkSignalThread.getInstance();
    private SparkStatusSignal[] signals = new SparkStatusSignal[0];

    public SparkInputs(String name) {
        super(name);
    }

    @Override
    public void toLog(LogTable table) {
        if (signals.length == 0)
            return;
        for (SparkStatusSignal signal : signals)
            table.put(signal.getName(), signal.getValue());
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet()) {
            table.put(entry.getKey(), entry.getValue().stream().mapToDouble(Double::doubleValue).toArray());
            entry.getValue().clear();
        }
        latestTable = table;
    }

    public void registerSignal(SparkStatusSignal statusSignal) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;

        final SparkStatusSignal[] newSignals = new SparkStatusSignal[signals.length + 1];
        System.arraycopy(signals, 0, newSignals, 0, signals.length);
        newSignals[signals.length] = statusSignal;
        signals = newSignals;
    }

    public void registerThreadedSignal(SparkStatusSignal statusSignal) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;

        registerSignal(statusSignal);
        signalToThreadedQueue.put(statusSignal.getName() + "_Threaded", signalThread.registerSignal(statusSignal.getValueSupplier()));
    }
}
