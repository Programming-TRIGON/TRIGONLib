package frc.trigon.lib.hardware.rev.spark;

import com.revrobotics.spark.SparkBase;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.rev.sparkencoder.SparkEncoder;
import frc.trigon.lib.utilities.Conversions;

import java.util.function.Function;

/**
 * An enum that represents the different signals that can be sent from a Spark motor.
 */
public enum SparkSignal {
    POSITION(null, SparkEncoder::getPosition),
    VELOCITY(null, SparkEncoder::getVelocity),
    OUTPUT_CURRENT(SparkBase::getOutputCurrent, null),
    APPLIED_OUTPUT(SparkBase::getAppliedOutput, null),
    BUS_VOLTAGE(SparkBase::getBusVoltage, null);

    final String name;
    final Function<SparkBase, Double> motorSignalFunction;
    final Function<SparkEncoder, Double> encoderSignalFunction;

    SparkSignal(Function<SparkBase, Double> motorSignalFunction, Function<SparkEncoder, Double> encoderSignalFunction) {
        this.name = Conversions.snakeCaseToCamelCase(name());
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