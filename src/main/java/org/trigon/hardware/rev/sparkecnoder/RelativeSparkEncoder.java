package org.trigon.hardware.rev.sparkecnoder;

import com.revrobotics.RelativeEncoder;

/**
 * A class that represents a relative encoder on a Spark MAX motor controller.
 */
public class RelativeSparkEncoder extends SparkEncoder {
    private final RelativeEncoder encoder;

    /**
     * Creates a new RelativeSparkEncoder.
     *
     * @param encoder the RelativeEncoder to use
     */
    public RelativeSparkEncoder(RelativeEncoder encoder) {
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