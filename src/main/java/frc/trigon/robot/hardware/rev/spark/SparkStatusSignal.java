package frc.trigon.robot.hardware.rev.spark;

import java.util.function.DoubleSupplier;

public class SparkStatusSignal {
    private final DoubleSupplier valueSupplier;
    private final String name;

    public SparkStatusSignal(SparkSignal signal, DoubleSupplier valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.name = signal.name;
    }

    public double getValue() {
        return valueSupplier.getAsDouble();
    }

    public DoubleSupplier getValueSupplier() {
        return valueSupplier;
    }

    public String getName() {
        return name;
    }
}
