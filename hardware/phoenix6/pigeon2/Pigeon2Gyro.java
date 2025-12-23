package frc.trigon.lib.hardware.phoenix6.pigeon2;


import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.Logger;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.phoenix6.Phoenix6Inputs;
import frc.trigon.lib.hardware.phoenix6.pigeon2.io.RealPigeon2IO;
import frc.trigon.lib.hardware.phoenix6.pigeon2.io.SimulationPigeon2IO;

import java.util.function.DoubleSupplier;

/**
 * A class that represents a Pigeon2 gyro.
 */
public class Pigeon2Gyro {
    private final String gyroName;
    private final Pigeon2IO gyroIO;
    private final Phoenix6Inputs gyroInputs;
    private final int id;

    /**
     * Creates a new Pigeon2 gyro.
     *
     * @param id       the gyro's ID
     * @param gyroName the name of the gyro
     */
    public Pigeon2Gyro(int id, String gyroName) {
        this(id, gyroName, "");
    }

    /**
     * Creates a new Pigeon2 gyro.
     *
     * @param id       the gyro's ID
     * @param gyroName the name of the gyro
     * @param canbus   the canivore's name
     */
    public Pigeon2Gyro(int id, String gyroName, String canbus) {
        this.gyroName = gyroName;
        this.gyroIO = generateIO(id, canbus);
        this.gyroInputs = new Phoenix6Inputs(gyroName, !canbus.isEmpty());
        this.id = id;
        gyroIO.optimizeBusUsage();
    }

    /**
     * Updates the gyro and logs its inputs. Should be called periodically.
     */
    public void update() {
        gyroIO.updateGyro();
        Logger.processInputs("Gyros/" + gyroName, gyroInputs);
    }

    public int getID() {
        return id;
    }

    /**
     * Sets the yaw velocity of the robot supplier used in simulation.
     * This is used to calculate the gyro's yaw in simulation by multiplying it by the time since the last update, and adding it to the total yaw.
     *
     * @param yawVelocitySupplierDegreesPerSecond the yaw velocity supplier in degrees per second
     */
    public void setSimulationYawVelocitySupplier(DoubleSupplier yawVelocitySupplierDegreesPerSecond) {
        gyroIO.setSimulationYawVelocitySupplier(yawVelocitySupplierDegreesPerSecond);
    }

    public void applyConfigurations(Pigeon2Configuration realConfiguration, Pigeon2Configuration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            gyroIO.applyConfiguration(simulationConfiguration);
        else
            gyroIO.applyConfiguration(realConfiguration);
    }

    public void applyConfiguration(Pigeon2Configuration simulationAndRealConfiguration) {
        gyroIO.applyConfiguration(simulationAndRealConfiguration);
    }

    public void applyRealConfiguration(Pigeon2Configuration realConfiguration) {
        if (!RobotHardwareStats.isSimulation())
            gyroIO.applyConfiguration(realConfiguration);
    }

    public void applySimulationConfiguration(Pigeon2Configuration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            gyroIO.applyConfiguration(simulationConfiguration);
    }

    public void setYaw(Rotation2d currentYaw) {
        gyroIO.setYaw(currentYaw);
    }

    public double getSignal(Pigeon2Signal signal) {
        return gyroInputs.getSignal(signal.name);
    }

    public double[] getThreadedSignal(Pigeon2Signal signal) {
        return gyroInputs.getThreadedSignal(signal.name);
    }

    public void registerSignal(Pigeon2Signal signal, double updateFrequencyHertz) {
        gyroInputs.registerSignal(pigeon2SignalToStatusSignal(signal), updateFrequencyHertz);
    }

    public void registerThreadedSignal(Pigeon2Signal signal, double updateFrequencyHertz) {
        gyroInputs.registerThreadedSignal(pigeon2SignalToStatusSignal(signal), updateFrequencyHertz);
    }

    private BaseStatusSignal pigeon2SignalToStatusSignal(Pigeon2Signal signal) {
        final Pigeon2 pigeon2 = gyroIO.getPigeon2();
        if (RobotHardwareStats.isReplay() || pigeon2 == null)
            return null;

        return signal.signalFunction.apply(pigeon2);
    }

    private Pigeon2IO generateIO(int id, String canbus) {
        if (RobotHardwareStats.isReplay())
            return new Pigeon2IO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationPigeon2IO(id);
        return new RealPigeon2IO(id, canbus);
    }
}
