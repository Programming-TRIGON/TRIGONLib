package frc.trigon.lib.hardware.rev.sparkencoder;

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

    public double getPosition() {
        return encoder.getPosition();
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }
}