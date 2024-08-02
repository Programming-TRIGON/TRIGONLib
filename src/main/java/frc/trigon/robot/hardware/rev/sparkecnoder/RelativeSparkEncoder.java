package frc.trigon.robot.hardware.rev.sparkecnoder;

import com.revrobotics.RelativeEncoder;

public class RelativeSparkEncoder extends SparkEncoder {
    private final RelativeEncoder encoder;

    public RelativeSparkEncoder(RelativeEncoder encoder) {
        this.encoder = encoder;
        setConversionsFactor(1);
    }

    public double getPositionRotations() {
        return encoder.getPosition();
    }

    public double getVelocityRotationsPerSecond() {
        return encoder.getVelocity();
    }

    @Override
    public void setConversionsFactor(double conversionsFactor) {
        encoder.setPositionConversionFactor(conversionsFactor);
        encoder.setVelocityConversionFactor(conversionsFactor / 60);
    }
}
