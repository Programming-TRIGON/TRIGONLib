package frc.trigon.lib.hardware.phoenix6.pigeon2.io;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.sim.Pigeon2SimState;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.phoenix6.pigeon2.Pigeon2IO;
import frc.trigon.lib.hardware.simulation.GyroSimulation;

import java.util.function.DoubleSupplier;

public class SimulationPigeon2IO extends Pigeon2IO {
    private final Pigeon2 pigeon2;
    private final Pigeon2SimState simState;
    private final GyroSimulation gyroSimulation;
    private DoubleSupplier yawVelocitySupplierDegreesPerSecond = null;

    public SimulationPigeon2IO(int id) {
        this.pigeon2 = new Pigeon2(id);
        this.simState = pigeon2.getSimState();
        this.gyroSimulation = new GyroSimulation();
    }

    @Override
    public void updateGyro() {
        if (yawVelocitySupplierDegreesPerSecond == null)
            return;
        gyroSimulation.update(yawVelocitySupplierDegreesPerSecond.getAsDouble(), RobotHardwareStats.getPeriodicTimeSeconds());
        simState.setRawYaw(gyroSimulation.getGyroYawDegrees());
    }

    @Override
    public void setSimulationYawVelocitySupplier(DoubleSupplier yawVelocitySupplierDegreesPerSecond) {
        this.yawVelocitySupplierDegreesPerSecond = yawVelocitySupplierDegreesPerSecond;
    }

    @Override
    public void setYaw(Rotation2d currentYaw) {
        gyroSimulation.setYaw(currentYaw);
    }

    @Override
    public void applyConfiguration(Pigeon2Configuration configuration) {
        pigeon2.getConfigurator().apply(configuration);
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
