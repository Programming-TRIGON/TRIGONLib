package org.trigon.hardware.misc.simplesensor.io;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycle;
import org.trigon.hardware.misc.simplesensor.SimpleSensorIO;
import org.trigon.hardware.misc.simplesensor.SimpleSensorInputsAutoLogged;

public class DutyCycleSensorIO extends SimpleSensorIO {
    private final DutyCycle dutyCycle;

    public DutyCycleSensorIO(int channel) {
        dutyCycle = new DutyCycle(new DigitalInput(channel));
    }

    public void updateInputs(SimpleSensorInputsAutoLogged inputs) {
        inputs.value = dutyCycle.getOutput();
    }
}

