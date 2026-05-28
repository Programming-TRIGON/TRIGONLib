package frc.trigon.lib.hardware.phoenix6.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.trigon.lib.utilities.Conversions;

import java.util.function.Function;

/**
 * An enum that represents the different signals that can be sent from a TalonFX motor.
 */
public enum TalonFXSignal {
    POSITION(TalonFX::getPosition),
    VELOCITY(TalonFX::getVelocity),
    ROTOR_VELOCITY(TalonFX::getRotorVelocity),
    ROTOR_POSITION(TalonFX::getRotorPosition),
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
        this.name = Conversions.snakeCaseToCamelCase(name());
        this.signalFunction = signalFunction;
    }
}