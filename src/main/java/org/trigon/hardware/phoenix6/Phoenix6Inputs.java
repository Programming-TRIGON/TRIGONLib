package org.trigon.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import org.littletonrobotics.junction.LogTable;
import org.trigon.hardware.BaseInputs;
import org.trigon.hardware.RobotHardwareStats;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Phoenix6Inputs extends BaseInputs {
    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final Phoenix6SignalThread signalThread = Phoenix6SignalThread.getInstance();
    private BaseStatusSignal[] signals = new BaseStatusSignal[0];

    /**
     * Creates a new Phoenix6Inputs instance.
     *
     * @param name the name of the instance
     */
    public Phoenix6Inputs(String name) {
        super(name);
    }

    @Override
    public void toLog(LogTable table) {
        if (signals.length == 0)
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
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet()) {
            table.put(entry.getKey(), entry.getValue().stream().mapToDouble(Double::doubleValue).toArray());
            entry.getValue().clear();
        }
    }

    private void updateSignalsToTable(LogTable table) {
        BaseStatusSignal.refreshAll(signals);

        for (BaseStatusSignal signal : signals) {
            if (signal.getName().equals("ClosedLoopReference")) // This signal isn't updated correctly by `BaseStatusSignal.updateAll` for some reason.
                ((StatusSignal<Double>) signal).refresh();
            table.put(signal.getName(), signal.getValueAsDouble());
        }
    }

    private void addSignalToSignalsArray(BaseStatusSignal statusSignal) {
        final BaseStatusSignal[] newSignals = new BaseStatusSignal[signals.length + 1];
        System.arraycopy(signals, 0, newSignals, 0, signals.length);
        newSignals[signals.length] = statusSignal;
        signals = newSignals;
    }
}