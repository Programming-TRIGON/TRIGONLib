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
     * @param encoder the RelativeEncoder to use.
     */
    public RelativeSparkEncoder(RelativeEncoder encoder) {
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