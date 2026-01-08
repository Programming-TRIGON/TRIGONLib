package frc.trigon.lib.hardware.rev.spark;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;

import java.util.function.Function;

/**
 * An enum that represents the different types of Spark motors.
 */
public enum SparkType {
    SPARK_MAX((id -> new SparkMax(id, SparkMax.MotorType.kBrushless))),
    SPARK_FLEW((id -> new SparkFlex(id, SparkMax.MotorType.kBrushless)));

    public final Function<Integer, SparkBase> sparkCreator;

    SparkType(Function<Integer, SparkBase> sparkCreator) {
        this.sparkCreator = sparkCreator;
    }
}
