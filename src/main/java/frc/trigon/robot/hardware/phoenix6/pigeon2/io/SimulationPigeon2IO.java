package frc.trigon.robot.hardware.phoenix6.pigeon2.io;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.sim.Pigeon2SimState;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2IO;
import frc.trigon.robot.hardware.simulation.GyroSimulation;

import java.util.function.DoubleSupplier;

public class SimulationPigeon2IO extends Pigeon2IO {
    private final Pigeon2 pigeon2;
    private final Pigeon2SimState simState;
    private final GyroSimulation gyroSimulation;
    private DoubleSupplier yawVelocitySupplier = null;

    public SimulationPigeon2IO(int id) {
        this.pigeon2 = new Pigeon2(id);
        this.simState = pigeon2.getSimState();
        this.gyroSimulation = new GyroSimulation();
    }

    @Override
    public void updateGyro() {
        if (yawVelocitySupplier == null)
            return;
        gyroSimulation.update(yawVelocitySupplier.getAsDouble(), RobotConstants.PERIODIC_TIME_SECONDS);
        simState.setRawYaw(gyroSimulation.getGyroYawDegrees());
    }

    @Override
    public void setSimulationYawVelocitySupplier(DoubleSupplier yawVelocitySupplierDegreesPerSecond) {
        this.yawVelocitySupplier = yawVelocitySupplierDegreesPerSecond;
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
