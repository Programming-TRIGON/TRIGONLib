package frc.trigon.lib.hardware.rev.sparkencoder;

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

    public double getPosition() {
        return encoder.getPosition();
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }
}