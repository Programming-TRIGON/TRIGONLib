package org.trigon.hardware.rev.sparkecnoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase;

public abstract class SparkEncoder {
    public static SparkEncoder createEncoder(SparkBase spark) {
        final SparkAbsoluteEncoder absoluteEncoder = spark.getAbsoluteEncoder();
        if (absoluteEncoder != null)
            return new AbsoluteSparkEncoder(absoluteEncoder);
        else
            return new RelativeSparkEncoder(spark.getEncoder());
    }

    public static SparkEncoder createRelativeEncoder(SparkBase spark) {
        return new RelativeSparkEncoder(spark.getEncoder());
    }

    public abstract double getPositionRotations();

    public abstract double getVelocityRotationsPerSecond();
}