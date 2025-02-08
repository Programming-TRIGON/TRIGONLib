package org.trigon.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import org.littletonrobotics.junction.LogTable;
import org.trigon.hardware.InputsBase;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.SignalThreadBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Phoenix6Inputs extends InputsBase {
    private static BaseStatusSignal[] ALL_SIGNALS = new BaseStatusSignal[0];

    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final Phoenix6SignalThread signalThread = Phoenix6SignalThread.getInstance();
    private int firstInputIndex = -1;
    private int numberOfInputs = 0;

    /**
     * Creates a new Phoenix6Inputs instance.
     *
     * @param name the name of the instance
     */
    public Phoenix6Inputs(String name) {
        super(name);
    }

    public static void refreshAllInputs() {
        if (RobotHardwareStats.isReplay())
            return;

        BaseStatusSignal.refreshAll(ALL_SIGNALS);
    }

    @Override
    public void toLog(LogTable table) {
        if (numberOfInputs == 0)
            return;

        updateThreadedSignalsToTable(table);
        updateSignalsToTable(table);

        latestTable = table;
    }

    /**
     * Registers a threaded signal.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param statusSignal         the threaded signal to register
     * @param updateFrequencyHertz the frequency at which the threaded signal will be updated
     */
    public void registerThreadedSignal(BaseStatusSignal statusSignal, double updateFrequencyHertz) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;

        registerSignal(statusSignal, updateFrequencyHertz);
        if (RobotHardwareStats.isSimulation()) // You can't run signals at a high frequency in simulation. A fast thread slows down the simulation.
            statusSignal.setUpdateFrequency(50);
        signalToThreadedQueue.put(statusSignal.getName() + "_Threaded", signalThread.registerSignal(statusSignal));
    }

    /**
     * Registers a signal.
     *
     * @param statusSignal         the signal to register
     * @param updateFrequencyHertz the frequency at which the signal will be updated
     */
    public void registerSignal(BaseStatusSignal statusSignal, double updateFrequencyHertz) {
        if (statusSignal == null || RobotHardwareStats.isReplay())
            return;
        if (RobotHardwareStats.isSimulation())
            updateFrequencyHertz = 100; // For some reason, simulation sometimes malfunctions if a status signal isn't updated frequently enough.

        statusSignal.setUpdateFrequency(updateFrequencyHertz);
        addSignalToSignalsArray(statusSignal);
    }

    private void updateThreadedSignalsToTable(LogTable table) {
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet())
            table.put(entry.getKey(), SignalThreadBase.queueToDoubleArray(entry.getValue()));
    }

    private void updateSignalsToTable(LogTable table) {
        for (int i = firstInputIndex; i < ALL_SIGNALS.length; i++) {
            final BaseStatusSignal signal = ALL_SIGNALS[i];
            if (signal.getName().equals("ClosedLoopReference")) // This signal isn't updated correctly by `BaseStatusSignal.updateAll` for some reason.
                ((StatusSignal<Double>) signal).refresh();

            table.put(signal.getName(), signal.getValueAsDouble());
        }
    }

    private void addSignalToSignalsArray(BaseStatusSignal statusSignal) {
        if (firstInputIndex == -1)
            firstInputIndex = ALL_SIGNALS.length;
        numberOfInputs++;

        final BaseStatusSignal[] newSignals = new BaseStatusSignal[ALL_SIGNALS.length + 1];
        System.arraycopy(ALL_SIGNALS, 0, newSignals, 0, ALL_SIGNALS.length);
        newSignals[ALL_SIGNALS.length] = statusSignal;
        ALL_SIGNALS = newSignals;
    }
}