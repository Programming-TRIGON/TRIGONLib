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
     * @param encoder The SparkAbsoluteEncoder to use.
     */
    public AbsoluteSparkEncoder(SparkAbsoluteEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Gets the position of the encoder in rotations.
     *
     * @return The position of the encoder in rotations.
     */
    public double getPositionRotations() {
        return encoder.getPosition();
    }

    /**
     * Gets the velocity of the encoder in rotations per second.
     *
     * @return The velocity of the encoder in rotations per second.
     */
    public double getVelocityRotationsPerSecond() {
        return encoder.getVelocity();
    }
}