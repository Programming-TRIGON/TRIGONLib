package frc.trigon.lib.hardware.misc.simplesensor.io;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.trigon.lib.hardware.misc.simplesensor.SimpleSensorIO;
import frc.trigon.lib.hardware.misc.simplesensor.SimpleSensorInputsAutoLogged;

public class AnalogSensorIO extends SimpleSensorIO {
    private final AnalogInput analogInput;

    public AnalogSensorIO(int channel) {
        analogInput = new AnalogInput(channel);
    }

    public void updateInputs(SimpleSensorInputsAutoLogged inputs) {
        inputs.value = analogInput.getValue();
    }
}
