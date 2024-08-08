package org.trigon.hardware.phoenix6.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import org.trigon.hardware.SignalUtilities;

import java.util.function.Function;

public enum TalonFXSignal {
    POSITION(TalonFX::getPosition),
    VELOCITY(TalonFX::getVelocity),
    TORQUE_CURRENT(TalonFX::getTorqueCurrent),
    STATOR_CURRENT(TalonFX::getStatorCurrent),
    SUPPLY_CURRENT(TalonFX::getSupplyCurrent),
    CLOSED_LOOP_REFERENCE(TalonFX::getClosedLoopReference),
    MOTOR_VOLTAGE(TalonFX::getMotorVoltage),
    FORWARD_LIMIT(TalonFX::getForwardLimit),
    REVERSE_LIMIT(TalonFX::getReverseLimit);

    final String name;
    final Function<TalonFX, BaseStatusSignal> signalFunction;

    TalonFXSignal(Function<TalonFX, BaseStatusSignal> signalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.signalFunction = signalFunction;
    }
}