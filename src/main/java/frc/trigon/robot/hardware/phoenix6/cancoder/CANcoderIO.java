package frc.trigon.robot.hardware.phoenix6.cancoder;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;

import java.util.function.DoubleSupplier;

public class CANcoderIO {
    protected void updateEncoder() {
    }

    protected void applyConfiguration(CANcoderConfiguration configuration) {
    }

    protected void optimizeBusUsage() {
    }

    protected void setSimulationInputSuppliers(DoubleSupplier positionSupplierRotations, DoubleSupplier velocitySupplierRotationsPerSecond) {
    }

    protected CANcoder getCANcoder() {
        return null;
    }
}
