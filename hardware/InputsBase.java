package frc.trigon.lib.hardware;

import edu.wpi.first.wpilibj.Timer;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.NoSuchElementException;

public abstract class InputsBase implements LoggableInputs {
    protected LogTable latestTable = null;
    private final String name;
    private double lastErrorTimestamp = 0;

    /**
     * Creates a new BaseInputs instance.
     *
     * @param name the name of the instance. Used for error messages
     */
    public InputsBase(String name) {
        this.name = name;
    }

    @Override
    public void fromLog(LogTable table) {
        latestTable = table;
    }

    /**
     * Gets a signal from the inputs.
     *
     * @param signalName the name of the signal
     * @return the signal
     */
    public double getSignal(String signalName) {
        if (latestTable == null) {
            if (shouldPrintError())
                new NullPointerException("The device \"" + name + "\" is trying to retrieve signal \"" + signalName + "\". Though, the latest table is null. This is likely due to the device not being logged.").printStackTrace();
            return 0;
        }

        final LogTable.LogValue value = latestTable.get(signalName);
        if (value == null) {
            if (shouldPrintError())
                new NoSuchElementException("The device \"" + name + "\" is trying to retrieve signal \"" + signalName + "\" which doesn't exist.").printStackTrace();
            return 0;
        }

        return value.getDouble();
    }

    /**
     * Gets a threaded signal.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signalName the name of the threaded signal
     * @return the threaded signal
     */
    public double[] getThreadedSignal(String signalName) {
        if (latestTable == null) {
            if (shouldPrintError())
                new NullPointerException("The device \"" + name + "\" is trying to retrieve threaded signal \"" + signalName + "\". Though, the latest table is null. This is likely due to the device not being logged.").printStackTrace();
            return new double[0];
        }

        final LogTable.LogValue value = latestTable.get(signalName + "_Threaded");
        if (value == null) {
            if (shouldPrintError())
                new NoSuchElementException("The device \"" + name + "\" is trying to retrieve threaded signal \"" + signalName + "\" which doesn't exist.").printStackTrace();
            return new double[0];
        }

        return value.getDoubleArray();
    }

    private boolean shouldPrintError() {
        final double currentTime = Timer.getTimestamp();
        final boolean shouldPrint = currentTime - lastErrorTimestamp > 5;
        if (shouldPrint)
            lastErrorTimestamp = currentTime;
        return shouldPrint;
    }
}
