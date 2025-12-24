package frc.trigon.lib.hardware.phoenix6.cancoder;


import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import org.littletonrobotics.junction.Logger;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.phoenix6.Phoenix6Inputs;
import frc.trigon.lib.hardware.phoenix6.cancoder.io.RealCANcoderIO;
import frc.trigon.lib.hardware.phoenix6.cancoder.io.SimulationCANcoderIO;
import frc.trigon.lib.hardware.phoenix6.talonfx.TalonFXMotor;
import frc.trigon.lib.hardware.phoenix6.talonfx.TalonFXSignal;

import java.util.function.DoubleSupplier;

/**
 * A class that represents a CANcoder encoder.
 */
public class CANcoderEncoder {
    private final String encoderName;
    private final CANcoderIO encoderIO;
    private final Phoenix6Inputs encoderInputs;
    private final int id;

    /**
     * Creates a new CANcoder encoder.
     *
     * @param id          the ID of the CANcoder
     * @param encoderName the name of the encoder
     */
    public CANcoderEncoder(int id, String encoderName) {
        this(id, encoderName, "");
    }

    /**
     * Creates a new CANcoder encoder.
     *
     * @param id          the ID of the CANcoder
     * @param encoderName the name of the encoder
     * @param canbus      the canivore name
     */
    public CANcoderEncoder(int id, String encoderName, String canbus) {
        this.encoderName = encoderName;
        this.encoderIO = generateIO(id, canbus);
        this.encoderInputs = new Phoenix6Inputs(encoderName, !canbus.isEmpty());
        this.id = id;
        encoderIO.optimizeBusUsage();
    }

    /**
     * Updates the encoder and logs the inputs. Should be called periodically.
     */
    public void update() {
        encoderIO.updateEncoder();
        Logger.processInputs("Encoders/" + encoderName, encoderInputs);
    }

    public int getID() {
        return id;
    }

    /**
     * Sets the TalonFX motor used to supply the encoder's position and velocity values in simulation.
     *
     * @param motor the TalonFX motor to get the simulation inputs from
     */
    public void setSimulationInputsFromTalonFX(TalonFXMotor motor) {
        encoderIO.setSimulationInputSuppliers(() -> motor.getSignal(TalonFXSignal.POSITION), () -> motor.getSignal(TalonFXSignal.VELOCITY));
    }

    /**
     * Sets the simulation inputs of the encoder from suppliers.
     *
     * @param positionSupplierRotations          the supplier of the position in rotations
     * @param velocitySupplierRotationsPerSecond the supplier of the velocity in rotations per second
     */
    public void setSimulationInputsSuppliers(DoubleSupplier positionSupplierRotations, DoubleSupplier velocitySupplierRotationsPerSecond) {
        encoderIO.setSimulationInputSuppliers(positionSupplierRotations, velocitySupplierRotationsPerSecond);
    }

    /**
     * Applies both the real and simulation configurations to the encoder.
     * Having two different configurations allows for tuning encoder behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration       configuration to be used in real life
     * @param simulationConfiguration configuration to be used in simulation
     */
    public void applyConfigurations(CANcoderConfiguration realConfiguration, CANcoderConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            encoderIO.applyConfiguration(simulationConfiguration);
        else
            encoderIO.applyConfiguration(realConfiguration);
    }

    /**
     * Applies the configuration to be used both in real life and in simulation.
     *
     * @param simulationAndRealConfiguration the configuration
     */
    public void applyConfiguration(CANcoderConfiguration simulationAndRealConfiguration) {
        encoderIO.applyConfiguration(simulationAndRealConfiguration);
    }

    /**
     * Applies the configuration to be used when {@link RobotHardwareStats#isSimulation()} is false.
     * Having two different configurations allows for tuning encoder behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration the configuration
     */
    public void applyRealConfiguration(CANcoderConfiguration realConfiguration) {
        if (!RobotHardwareStats.isSimulation())
            encoderIO.applyConfiguration(realConfiguration);
    }

    /**
     * Applies the configuration to be used in simulation.
     * Having two different configurations allows for tuning encoder behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param simulationConfiguration the configuration
     */
    public void applySimulationConfiguration(CANcoderConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            encoderIO.applyConfiguration(simulationConfiguration);
    }

    /**
     * Gets the signal from the encoder.
     *
     * @param signal the type of signal to get
     * @return the signal
     */
    public double getSignal(CANcoderSignal signal) {
        return encoderInputs.getSignal(signal.name);
    }

    /**
     * Gets the threaded signal from the encoder.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the type of signal to get
     * @return the threaded signal
     */
    public double[] getThreadedSignal(CANcoderSignal signal) {
        return encoderInputs.getThreadedSignal(signal.name);
    }

    /**
     * Registers a signal to be updated at a certain frequency.
     *
     * @param signal               the signal to register
     * @param updateFrequencyHertz the frequency at which the signal will be updated
     */
    public void registerSignal(CANcoderSignal signal, double updateFrequencyHertz) {
        encoderInputs.registerSignal(encoderSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    /**
     * Registers a threaded signal to be updated at a certain frequency.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal               the signal to register
     * @param updateFrequencyHertz the frequency at which the signal will be updated
     */
    public void registerThreadedSignal(CANcoderSignal signal, double updateFrequencyHertz) {
        encoderInputs.registerThreadedSignal(encoderSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    /**
     * Sets the current position of the encoder in rotations.
     *
     * @param positionRotations the position to be set in rotations
     */
    public void setPosition(double positionRotations) {
        encoderIO.setPosition(positionRotations);
    }

    private BaseStatusSignal encoderSignalToStatusSignal(CANcoderSignal signal) {
        final CANcoder cancoder = encoderIO.getCANcoder();
        if (RobotHardwareStats.isReplay() || cancoder == null)
            return null;

        return signal.signalFunction.apply(cancoder);
    }

    private CANcoderIO generateIO(int id, String canbus) {
        if (RobotHardwareStats.isReplay())
            return new CANcoderIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationCANcoderIO(id);
        return new RealCANcoderIO(id, canbus);
    }
}
