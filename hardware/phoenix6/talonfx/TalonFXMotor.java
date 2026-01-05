package frc.trigon.lib.hardware.phoenix6.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import org.littletonrobotics.junction.Logger;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.phoenix6.Phoenix6Inputs;
import frc.trigon.lib.hardware.phoenix6.talonfx.io.RealTalonFXIO;
import frc.trigon.lib.hardware.phoenix6.talonfx.io.SimulationTalonFXIO;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;

/**
 * A class that represents a TalonFX motor controller.
 * This class provides a structured interface to control and monitor TalonFX motors both in real-world deployment, and physics-based simulation.
 * It incorporates features for signal management, configuration handling, and threaded processing to ensure efficient and robust motor control
 * It's also fully integrated with AdvantageKit logging.
 */
public class TalonFXMotor {
    private final String motorName;
    private final TalonFXIO motorIO;
    private final Phoenix6Inputs motorInputs;
    private final int id;

    /**
     * Creates a new TalonFX motor.
     *
     * @param id        the motor's ID
     * @param motorName the name of the motor
     */
    public TalonFXMotor(int id, String motorName) {
        this(id, motorName, "");
    }

    /**
     * Creates a new TalonFX motor.
     *
     * @param id        the motor's ID
     * @param motorName the name of the motor
     * @param canbus    the canivore's name
     */
    public TalonFXMotor(int id, String motorName, String canbus) {
        this.motorName = motorName;
        this.motorIO = generateIO(id, canbus);
        this.motorInputs = new Phoenix6Inputs(motorName, !canbus.isEmpty());
        this.id = id;
        motorIO.optimizeBusUsage();
    }

    /**
     * Updates the motor and logs its inputs.
     */
    public void update() {
        if (RobotHardwareStats.isSimulation())
            motorIO.updateMotor();
        Logger.processInputs("Motors/" + motorName, motorInputs);
    }

    public int getID() {
        return id;
    }

    /**
     * Sets the physics simulation of the motor. Needed for the motor to be used in simulation with accurate physics.
     *
     * @param physicsSimulation the simulation
     */
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        motorIO.setPhysicsSimulation(physicsSimulation);
    }

    /**
     * Applies both the real and simulation configurations to the motor.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration       configuration to be used in real life
     * @param simulationConfiguration configuration to be used in simulation
     */
    public void applyConfigurations(TalonFXConfiguration realConfiguration, TalonFXConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(simulationConfiguration);
        else
            motorIO.applyConfiguration(realConfiguration);
    }

    /**
     * Applies the configuration to be used both in real life and in simulation.
     *
     * @param simulationAndRealConfiguration the configuration
     */
    public void applyConfiguration(TalonFXConfiguration simulationAndRealConfiguration) {
        motorIO.applyConfiguration(simulationAndRealConfiguration);
    }

    /**
     * Applies the configuration to be used when {@link RobotHardwareStats#isSimulation()} is false.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration the configuration
     */
    public void applyRealConfiguration(TalonFXConfiguration realConfiguration) {
        if (!RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(realConfiguration);
    }

    /**
     * Applies the configuration to be used in simulation.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param simulationConfiguration the configuration
     */
    public void applySimulationConfiguration(TalonFXConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(simulationConfiguration);
    }

    /**
     * Stops the motor.
     */
    public void stopMotor() {
        motorIO.stopMotor();
    }

    /**
     * Gets a signal from the motor.
     *
     * @param signal the type of signal to get
     * @return the signal
     */
    public double getSignal(TalonFXSignal signal) {
        return motorInputs.getSignal(signal.name);
    }

    /**
     * Gets a threaded signal from the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the type of threaded signal to get
     * @return the signal
     */
    public double[] getThreadedSignal(TalonFXSignal signal) {
        return motorInputs.getThreadedSignal(signal.name);
    }

    /**
     * Registers a signal to the motor.
     *
     * @param signal               the signal to register
     * @param updateFrequencyHertz the frequency at which the signal will be updated
     */
    public void registerSignal(TalonFXSignal signal, double updateFrequencyHertz) {
        motorInputs.registerSignal(motorSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    /**
     * Registers a threaded signal to the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal               the threaded signal to register
     * @param updateFrequencyHertz the frequency at which the threaded signal will be updated
     */
    public void registerThreadedSignal(TalonFXSignal signal, double updateFrequencyHertz) {
        motorInputs.registerThreadedSignal(motorSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    /**
     * Sends a control request to the motor.
     *
     * @param request the request to send
     */
    public void setControl(ControlRequest request) {
        motorIO.setControl(request);
    }

    /**
     * Sets the motor's current position in rotations.
     *
     * @param positionRotations the position to set
     */
    public void setPosition(double positionRotations) {
        motorIO.setPosition(positionRotations);
    }

    /**
     * Sets the motors neutral mode.
     *
     * @param brake true if the motor should brake, false if it should coast
     */
    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    private BaseStatusSignal motorSignalToStatusSignal(TalonFXSignal signal) {
        final TalonFX talonFX = motorIO.getTalonFX();
        if (RobotHardwareStats.isReplay() || talonFX == null)
            return null;

        return signal.signalFunction.apply(talonFX);
    }

    private TalonFXIO generateIO(int id, String canbus) {
        if (RobotHardwareStats.isReplay())
            return new TalonFXIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationTalonFXIO(id);
        return new RealTalonFXIO(id, canbus);
    }
}