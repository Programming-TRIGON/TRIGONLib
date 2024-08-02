package frc.trigon.robot.hardware.misc.simplesensor.io;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.trigon.robot.hardware.misc.simplesensor.SimpleSensorIO;
import frc.trigon.robot.hardware.misc.simplesensor.SimpleSensorInputsAutoLogged;

public class DigitalSensorIO extends SimpleSensorIO {
    private final DigitalInput digitalInput;

    public DigitalSensorIO(int channel) {
        digitalInput = new DigitalInput(channel);
    }

    public void updateInputs(SimpleSensorInputsAutoLogged inputs) {
        inputs.value = digitalInput.get() ? 1 : 0;
    }
}
