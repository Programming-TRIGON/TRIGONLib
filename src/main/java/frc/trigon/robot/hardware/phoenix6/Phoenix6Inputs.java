package frc.trigon.robot.hardware.phoenix6;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.BaseInputs;
import org.littletonrobotics.junction.LogTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Phoenix6Inputs extends BaseInputs {
    private final HashMap<String, Queue<Double>> signalToThreadedQueue = new HashMap<>();
    private final Phoenix6SignalThread signalThread = Phoenix6SignalThread.getInstance();
    private BaseStatusSignal[] signals = new BaseStatusSignal[0];

    public Phoenix6Inputs(String name) {
        super(name);
    }

    @Override
    public void toLog(LogTable table) {
        if (signals.length == 0)
            return;
        BaseStatusSignal.refreshAll(signals);
        for (BaseStatusSignal signal : signals) {
            if (signal.getName().equals("ClosedLoopReference"))
                ((StatusSignal<Double>) signal).refresh();
            table.put(signal.getName(), signal.getValueAsDouble());
        }
        for (Map.Entry<String, Queue<Double>> entry : signalToThreadedQueue.entrySet()) {
            table.put(entry.getKey(), entry.getValue().stream().mapToDouble(Double::doubleValue).toArray());
            entry.getValue().clear();
        }
        latestTable = table;
    }

    public void registerSignal(BaseStatusSignal statusSignal, double updateFrequencyHertz) {
        if (statusSignal == null || RobotConstants.IS_REPLAY)
            return;

        statusSignal.setUpdateFrequency(updateFrequencyHertz);
        final BaseStatusSignal[] newSignals = new BaseStatusSignal[signals.length + 1];
        System.arraycopy(signals, 0, newSignals, 0, signals.length);
        newSignals[signals.length] = statusSignal;
        signals = newSignals;
    }

    public void registerThreadedSignal(BaseStatusSignal targetStatusSignal, BaseStatusSignal slopeStatusSignal, double updateFrequencyHertz) {
        if (targetStatusSignal == null || slopeStatusSignal == null || RobotConstants.IS_REPLAY)
            return;

        registerSignal(targetStatusSignal, updateFrequencyHertz);
        registerSignal(slopeStatusSignal, updateFrequencyHertz);
        signalToThreadedQueue.put(targetStatusSignal.getName() + "_Threaded", signalThread.registerSignal(targetStatusSignal, slopeStatusSignal));
    }
}
