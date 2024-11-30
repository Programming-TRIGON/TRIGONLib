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
     * Gets the position of the encoder in the unit set in the conversion factor. Rotations by default.
     *
     * @return the position of the encoder
     */
    public double getPosition() {
        return encoder.getPosition();
    }

    /**
     * Gets the position of the encoder in the unit set in the conversion factor. Rotations by default.
     *
     * @return the position of the encoder
     */
    public double getVelocity() {
        return encoder.getVelocity();
    }
}