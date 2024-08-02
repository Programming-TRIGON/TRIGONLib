package frc.trigon.robot.hardware.phoenix6.pigeon2.io;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2IO;

public class RealPigeon2IO extends Pigeon2IO {
    private final Pigeon2 pigeon2;

    public RealPigeon2IO(int id, String canbus) {
        this.pigeon2 = new Pigeon2(id, canbus);
    }

    @Override
    public void applyConfiguration(Pigeon2Configuration configuration) {
        pigeon2.getConfigurator().apply(configuration);
    }

    @Override
    public void setYaw(Rotation2d currentYaw) {
        pigeon2.setYaw(currentYaw.getDegrees());
    }

    @Override
    public void optimizeBusUsage() {
        pigeon2.optimizeBusUtilization();
    }

    @Override
    public Pigeon2 getPigeon2() {
        return pigeon2;
    }
}
