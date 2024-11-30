package org.trigon.hardware.rev.sparkecnoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase;

public abstract class SparkEncoder {
    /**
     * Creates a new Spark encoder. If the Spark motor has an absolute encoder, an AbsoluteSparkEncoder is created. Otherwise, a RelativeSparkEncoder is created.
     *
     * @param spark the Spark motor
     * @return the Spark encoder
     */
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

    public abstract double getPosition();

    public abstract double getVelocity();
}