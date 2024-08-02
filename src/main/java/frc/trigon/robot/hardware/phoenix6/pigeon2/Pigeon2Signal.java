package frc.trigon.robot.hardware.phoenix6.pigeon2;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.core.CorePigeon2;
import frc.trigon.robot.hardware.SignalUtilities;

import java.util.function.Function;

public enum Pigeon2Signal {
    YAW(CorePigeon2::getYaw),
    PITCH(CorePigeon2::getPitch),
    ROLL(CorePigeon2::getRoll),
    ANGULAR_VELOCITY_Z_WORLD(CorePigeon2::getAngularVelocityZWorld),
    ANGULAR_VELOCITY_Y_WORLD(CorePigeon2::getAngularVelocityYWorld),
    ANGULAR_VELOCITY_X_WORLD(CorePigeon2::getAngularVelocityXWorld);

    final String name;
    final Function<Pigeon2, BaseStatusSignal> signalFunction;

    Pigeon2Signal(Function<Pigeon2, BaseStatusSignal> signalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.signalFunction = signalFunction;
    }
}
