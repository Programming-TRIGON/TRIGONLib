package org.trigon.hardware.rev.spark;

import com.revrobotics.spark.SparkBase;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.SignalUtilities;
import org.trigon.hardware.rev.sparkecnoder.SparkEncoder;

import java.util.function.Function;

/**
 * An enum that represents the different signals that can be sent from a Spark motor.
 */
public enum SparkSignal {
    POSITION(null, SparkEncoder::getPositionRotations),
    VELOCITY(null, SparkEncoder::getVelocityRotationsPerSecond),
    OUTPUT_CURRENT(SparkBase::getOutputCurrent, null),
    APPLIED_OUTPUT(SparkBase::getAppliedOutput, null),
    BUS_VOLTAGE(SparkBase::getBusVoltage, null);

    final String name;
    final Function<SparkBase, Double> motorSignalFunction;
    final Function<SparkEncoder, Double> encoderSignalFunction;

    SparkSignal(Function<SparkBase, Double> motorSignalFunction, Function<SparkEncoder, Double> encoderSignalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.motorSignalFunction = motorSignalFunction;
        this.encoderSignalFunction = encoderSignalFunction;
    }

    public SparkStatusSignal getStatusSignal(SparkBase spark, SparkEncoder encoder) {
        if (RobotHardwareStats.isReplay() || spark == null || encoder == null)
            return null;
        if (motorSignalFunction != null)
            return new SparkStatusSignal(this, () -> motorSignalFunction.apply(spark));
        else
            return new SparkStatusSignal(this, () -> encoderSignalFunction.apply(encoder));
    }
}