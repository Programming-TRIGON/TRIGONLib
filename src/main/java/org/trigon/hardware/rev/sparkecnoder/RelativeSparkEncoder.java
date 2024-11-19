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
     * @param encoder The RelativeEncoder to use.
     */
    public RelativeSparkEncoder(RelativeEncoder encoder) {
        this.encoder = encoder;
        setConversionsFactor(1);
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

    /**
     * Sets the conversion factor for values received the encoder.
     *
     * @param conversionsFactor The conversion factor to use.
     */
    @Override
    public void setConversionsFactor(double conversionsFactor) {
        encoder.setPositionConversionFactor(conversionsFactor);
        encoder.setVelocityConversionFactor(conversionsFactor / 60);
    }
}
