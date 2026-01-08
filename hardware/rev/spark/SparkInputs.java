package frc.trigon.lib.hardware.rev.spark;

import org.littletonrobotics.junction.LogTable;
import frc.trigon.lib.hardware.InputsBase;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.SignalThreadBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class SparkInputs extends InputsBase {
    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final SparkSignalThread signalThread = SparkSignalThread.getInstance();
    private SparkStatusSignal[] signals = new SparkStatusSignal[0];

    /**
     * Creates a new SparkInputs instance.
     *
     * @param name the name of the instance
     */
    public SparkInputs(String name) {
        super(name);
    }

    @Override
    public void toLog(LogTable table) {
        if (signals.length == 0)
            return;

        updateSignalsToTable(table);
        updateThreadedSignalsToTable(table);

        latestTable = table;
    }

    /**
     * Registers a signal.
     *
     * @param statusSignal the signal to register
     */
    public void registerSignal(SparkStatusSignal statusSignal) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;

        addSignalToSignalsArray(statusSignal);
    }

    /**
     * Registers a threaded signal.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param statusSignal the threaded signal to register
     */
    public void registerThreadedSignal(SparkStatusSignal statusSignal) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;

        registerSignal(statusSignal);
        signalToThreadedQueue.put(statusSignal.getName() + "_Threaded", signalThread.registerThreadedSignal(statusSignal.getValueSupplier()));
    }

    private void updateThreadedSignalsToTable(LogTable table) {
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet())
            table.put(entry.getKey(), SignalThreadBase.queueToDoubleArray(entry.getValue()));
    }

    private void updateSignalsToTable(LogTable table) {
        for (SparkStatusSignal signal : signals)
            table.put(signal.getName(), signal.getValue());
    }

    private void addSignalToSignalsArray(SparkStatusSignal statusSignal) {
        final SparkStatusSignal[] newSignals = new SparkStatusSignal[signals.length + 1];
        System.arraycopy(signals, 0, newSignals, 0, signals.length);
        newSignals[signals.length] = statusSignal;
        signals = newSignals;
    }
}
