package org.trigon.hardware.phoenix6.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import org.trigon.hardware.SignalUtilities;

import java.util.function.Function;

public enum TalonFXSignal {
    POSITION((motor) -> motor.getPosition().clone()),
    VELOCITY((motor) -> motor.getVelocity().clone()),
    TORQUE_CURRENT((motor) -> motor.getTorqueCurrent().clone()),
    STATOR_CURRENT((motor) -> motor.getStatorCurrent().clone()),
    SUPPLY_CURRENT((motor) -> motor.getSupplyCurrent().clone()),
    CLOSED_LOOP_REFERENCE((motor) -> motor.getClosedLoopReference().clone()),
    MOTOR_VOLTAGE((motor) -> motor.getMotorVoltage().clone()),
    FORWARD_LIMIT((motor) -> motor.getForwardLimit().clone()),
    REVERSE_LIMIT((motor) -> motor.getReverseLimit().clone());

    final String name;
    final Function<TalonFX, BaseStatusSignal> signalFunction;

    TalonFXSignal(Function<TalonFX, BaseStatusSignal> signalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.signalFunction = signalFunction;
    }
}