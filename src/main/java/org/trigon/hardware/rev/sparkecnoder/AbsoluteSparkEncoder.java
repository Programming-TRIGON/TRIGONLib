package org.trigon.hardware.rev.sparkecnoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;

/**
 * A class that represents an absolute encoder on a Spark MAX motor controller.
 */
public class AbsoluteSparkEncoder extends SparkEncoder {
    private final SparkAbsoluteEncoder encoder;

    /**
     * Creates a new AbsoluteSparkEncoder.
     *
     * @param encoder the SparkAbsoluteEncoder to use.
     */
    public AbsoluteSparkEncoder(SparkAbsoluteEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Gets the position of the encoder in rotations.
     *
     * @return the position of the encoder in rotations.
     */
    public double getPositionRotations() {
        return encoder.getPosition();
    }

    /**
     * Gets the velocity of the encoder in rotations per second.
     *
     * @return the velocity of the encoder in rotations per second.
     */
    public double getVelocityRotationsPerSecond() {
        return encoder.getVelocity();
    }
}