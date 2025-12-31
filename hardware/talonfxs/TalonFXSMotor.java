package frc.trigon.lib.hardware.talonfxs;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFXS;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;
import frc.trigon.lib.hardware.talonfxs.io.RealTalonFXSIO;
import frc.trigon.lib.hardware.talonfxs.io.SimulationTalonFXSIO;
import frc.trigon.lib.hardware.talonfxs.phoenix6.Phoenix6Inputs;
import org.littletonrobotics.junction.Logger;

/**
 * A class that represents a TalonFXS motor controller.
 * This class provides a structured interface to control and monitor TalonFXS motors both in real-world deployment, and physics-based simulation.
 * It incorporates features for signal management, configuration handling, and threaded processing to ensure efficient and robust motor control
 * It's also fully integrated with AdvantageKit logging.
 */
public class TalonFXSMotor {
    private final String motorName;
    private final TalonFXSIO motorIO;
    private final Phoenix6Inputs motorInputs;
    private final int id;

    /**
     * Creates a new TalonFXS motor.
     *
     * @param id        the motor's ID
     * @param motorName the name of the motor
     */
    public TalonFXSMotor(int id, String motorName) {
        this(id, motorName, "");
    }

    /**
     * Creates a new TalonFXS motor.
     *
     * @param id        the motor's ID
     * @param motorName the name of the motor
     * @param canbus    the canbus' name
     */
    public TalonFXSMotor(int id, String motorName, String canbus) {
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
    public void applyConfigurations(TalonFXSConfiguration realConfiguration, TalonFXSConfiguration simulationConfiguration) {
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
    public void applyConfiguration(TalonFXSConfiguration simulationAndRealConfiguration) {
        motorIO.applyConfiguration(simulationAndRealConfiguration);
    }

    /**
     * Applies the configuration to be used when {@link RobotHardwareStats#isSimulation()} is false.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration the configuration
     */
    public void applyRealConfiguration(TalonFXSConfiguration realConfiguration) {
        if (!RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(realConfiguration);
    }

    /**
     * Applies the configuration to be used in simulation.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param simulationConfiguration the configuration
     */
    public void applySimulationConfiguration(TalonFXSConfiguration simulationConfiguration) {
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
    public double getSignal(TalonFXSSignal signal) {
        return motorInputs.getSignal(signal.name);
    }

    /**
     * Gets a threaded signal from the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the type of threaded signal to get
     * @return the signal
     */
    public double[] getThreadedSignal(TalonFXSSignal signal) {
        return motorInputs.getThreadedSignal(signal.name);
    }

    /**
     * Registers a signal to the motor.
     *
     * @param signal               the signal to register
     * @param updateFrequencyHertz the frequency at which the signal will be updated
     */
    public void registerSignal(TalonFXSSignal signal, double updateFrequencyHertz) {
        motorInputs.registerSignal(motorSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    /**
     * Registers a threaded signal to the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal               the threaded signal to register
     * @param updateFrequencyHertz the frequency at which the threaded signal will be updated
     */
    public void registerThreadedSignal(TalonFXSSignal signal, double updateFrequencyHertz) {
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
     * Sets the motor's neutral mode.
     *
     * @param brake true if the motor should brake, false if it should coast
     */
    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    private BaseStatusSignal motorSignalToStatusSignal(TalonFXSSignal signal) {
        final TalonFXS talonFXS = motorIO.getTalonFXS();
        if (RobotHardwareStats.isReplay() || talonFXS == null)
            return null;

        return signal.signalFunction.apply(talonFXS);
    }

    private TalonFXSIO generateIO(int id, String canbus) {
        if (RobotHardwareStats.isReplay())
            return new TalonFXSIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationTalonFXSIO(id);
        return new RealTalonFXSIO(id, canbus);
    }
}
