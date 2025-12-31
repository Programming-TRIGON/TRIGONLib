package frc.trigon.lib.hardware.talonfxs;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFXS;
import lib.utilities.Conversions;

import java.util.function.Function;

/**
 * An enum that represents the different signals that can be sent from a TalonFXS motor.
 */
public enum TalonFXSSignal {
    POSITION(TalonFXS::getPosition),
    VELOCITY(TalonFXS::getVelocity),
    ROTOR_VELOCITY(TalonFXS::getRotorVelocity),
    ROTOR_POSITION(TalonFXS::getRotorPosition),
    TORQUE_CURRENT(TalonFXS::getTorqueCurrent),
    STATOR_CURRENT(TalonFXS::getStatorCurrent),
    SUPPLY_CURRENT(TalonFXS::getSupplyCurrent),
    CLOSED_LOOP_REFERENCE(TalonFXS::getClosedLoopReference),
    MOTOR_VOLTAGE(TalonFXS::getMotorVoltage),
    FORWARD_LIMIT(TalonFXS::getForwardLimit),
    REVERSE_LIMIT(TalonFXS::getReverseLimit);

    final String name;
    final Function<TalonFXS, BaseStatusSignal> signalFunction;

    TalonFXSSignal(Function<TalonFXS, BaseStatusSignal> signalFunction) {
        this.name = Conversions.snakeCaseToCamelCase(name());
        this.signalFunction = signalFunction;
    }
}