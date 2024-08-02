package frc.trigon.robot.hardware.phoenix6.cancoder.io;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.trigon.robot.hardware.phoenix6.cancoder.CANcoderIO;

public class RealCANcoderIO extends CANcoderIO {
    private final CANcoder cancoder;

    public RealCANcoderIO(int id, String canbus) {
        this.cancoder = new CANcoder(id, canbus);
    }

    @Override
    public void applyConfiguration(CANcoderConfiguration configuration) {
        cancoder.getConfigurator().apply(configuration);
    }

    @Override
    public void optimizeBusUsage() {
        cancoder.optimizeBusUtilization();
    }

    @Override
    public CANcoder getCANcoder() {
        return cancoder;
    }
}
