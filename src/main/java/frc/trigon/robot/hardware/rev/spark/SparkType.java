package frc.trigon.robot.hardware.rev.spark;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkMax;

import java.util.function.Function;

public enum SparkType {
    SPARK_MAX((id -> new CANSparkMax(id, CANSparkMax.MotorType.kBrushless))),
    SPARK_FLEW((id -> new CANSparkFlex(id, CANSparkMax.MotorType.kBrushless)));

    public final Function<Integer, CANSparkBase> sparkCreator;

    SparkType(Function<Integer, CANSparkBase> sparkCreator) {
        this.sparkCreator = sparkCreator;
    }
}
