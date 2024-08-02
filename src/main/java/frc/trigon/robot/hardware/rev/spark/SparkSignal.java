package frc.trigon.robot.hardware.rev.spark;

import com.revrobotics.CANSparkBase;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.SignalUtilities;
import frc.trigon.robot.hardware.rev.sparkecnoder.SparkEncoder;

import java.util.function.Function;

public enum SparkSignal {
    POSITION(null, SparkEncoder::getPositionRotations),
    VELOCITY(null, SparkEncoder::getVelocityRotationsPerSecond),
    OUTPUT_CURRENT(CANSparkBase::getOutputCurrent, null),
    APPLIED_OUTPUT(CANSparkBase::getAppliedOutput, null),
    BUS_VOLTAGE(CANSparkBase::getBusVoltage, null);

    final String name;
    final Function<CANSparkBase, Double> motorSignalFunction;
    final Function<SparkEncoder, Double> encoderSignalFunction;

    SparkSignal(Function<CANSparkBase, Double> motorSignalFunction, Function<SparkEncoder, Double> encoderSignalFunction) {
        this.name = SignalUtilities.enumNameToSignalName(name());
        this.motorSignalFunction = motorSignalFunction;
        this.encoderSignalFunction = encoderSignalFunction;
    }

    public SparkStatusSignal getStatusSignal(CANSparkBase spark, SparkEncoder encoder) {
        if (RobotConstants.IS_REPLAY || spark == null || encoder == null)
            return null;
        if (motorSignalFunction != null)
            return new SparkStatusSignal(this, () -> motorSignalFunction.apply(spark));
        else
            return new SparkStatusSignal(this, () -> encoderSignalFunction.apply(encoder));
    }
}