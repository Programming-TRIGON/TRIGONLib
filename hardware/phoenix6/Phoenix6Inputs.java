package frc.trigon.lib.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import org.littletonrobotics.junction.LogTable;
import frc.trigon.lib.hardware.InputsBase;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.SignalThreadBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Phoenix6Inputs extends InputsBase {
    private static BaseStatusSignal[]
            RIO_SIGNALS = new BaseStatusSignal[0],
            CANIVORE_SIGNALS = new BaseStatusSignal[0];

    private final boolean isCanivore;
    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final Phoenix6SignalThread signalThread = Phoenix6SignalThread.getInstance();
    private int firstInputIndex = -1;
    private int numberOfInputs = 0;

    /**
     * Creates a new Phoenix6Inputs instance.
     *
     * @param name       the name of the instance
     * @param isCanivore whether the instance is running on a canivore network (CAN FD)
     */
    public Phoenix6Inputs(String name, boolean isCanivore) {
        super(name);
        this.isCanivore = isCanivore;
    }

    public static void refreshAllInputs() {
        if (RobotHardwareStats.isReplay())
            return;

        BaseStatusSignal.refreshAll(CANIVORE_SIGNALS);
        BaseStatusSignal.refreshAll(RIO_SIGNALS);
    }

    @Override
    public void toLog(LogTable table) {
        if (numberOfInputs == 0 && signalToThreadedQueue.isEmpty())
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

        if (RobotHardwareStats.isSimulation()) // You can't run signals at a high frequency in simulation. A fast thread slows down the simulation.
            updateFrequencyHertz = 50;
        statusSignal.setUpdateFrequency(updateFrequencyHertz);

        signalToThreadedQueue.put(statusSignal.getName(), signalThread.registerSignal(statusSignal));
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
        if (isCanivore)
            addSignalToCANivoreSignalsArray(statusSignal);
        else
            addSignalToRIOSignalsArray(statusSignal);
    }

    private void updateThreadedSignalsToTable(LogTable table) {
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet()) {
            final double[] queueAsArray = SignalThreadBase.queueToDoubleArray(entry.getValue());
            table.put(entry.getKey() + "_Threaded", queueAsArray);
            if (queueAsArray.length == 0)
                continue;
            table.put(entry.getKey(), queueAsArray[queueAsArray.length - 1]);
        }
    }

    private void updateSignalsToTable(LogTable table) {
        if (firstInputIndex == -1 || numberOfInputs == 0)
            return;

        for (int i = firstInputIndex; i < firstInputIndex + numberOfInputs; i++) {
            final BaseStatusSignal signal = isCanivore ? CANIVORE_SIGNALS[i] : RIO_SIGNALS[i];
            table.put(signal.getName(), signal.getValueAsDouble());
        }
    }

    private void addSignalToRIOSignalsArray(BaseStatusSignal statusSignal) {
        if (firstInputIndex == -1)
            firstInputIndex = RIO_SIGNALS.length;
        numberOfInputs++;

        final BaseStatusSignal[] newSignals = new BaseStatusSignal[RIO_SIGNALS.length + 1];
        System.arraycopy(RIO_SIGNALS, 0, newSignals, 0, RIO_SIGNALS.length);
        newSignals[RIO_SIGNALS.length] = statusSignal;
        RIO_SIGNALS = newSignals;
    }

    private void addSignalToCANivoreSignalsArray(BaseStatusSignal statusSignal) {
        if (firstInputIndex == -1)
            firstInputIndex = CANIVORE_SIGNALS.length;
        numberOfInputs++;

        final BaseStatusSignal[] newSignals = new BaseStatusSignal[CANIVORE_SIGNALS.length + 1];
        System.arraycopy(CANIVORE_SIGNALS, 0, newSignals, 0, CANIVORE_SIGNALS.length);
        newSignals[CANIVORE_SIGNALS.length] = statusSignal;
        CANIVORE_SIGNALS = newSignals;
    }
}