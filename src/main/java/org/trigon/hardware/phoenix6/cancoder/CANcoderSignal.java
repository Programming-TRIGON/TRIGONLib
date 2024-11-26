package org.trigon.hardware.phoenix6.cancoder;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import org.trigon.utilities.StringUtilities;

import java.util.function.Function;

public enum CANcoderSignal {
    POSITION(CANcoder::getPosition),
    VELOCITY(CANcoder::getVelocity);

    final String name;
    final Function<CANcoder, BaseStatusSignal> signalFunction;

    CANcoderSignal(Function<CANcoder, BaseStatusSignal> signalFunction) {
        this.name = StringUtilities.toCamelCase(name());
        this.signalFunction = signalFunction;
    }
}
