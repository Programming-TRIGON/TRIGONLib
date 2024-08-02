package frc.trigon.robot.hardware.phoenix6.cancoder;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.trigon.robot.hardware.SignalUtilities;

import java.util.function.Function;

public enum CANcoderSignal {
    POSITION(CANcoder::getPosition),
    VELOCITY(CANcoder::getVelocity);

    final String name;
    final Function<CANcoder, BaseStatusSignal> signalFunction;

    CANcoderSignal(Function<CANcoder, BaseStatusSignal> signalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.signalFunction = signalFunction;
    }
}
