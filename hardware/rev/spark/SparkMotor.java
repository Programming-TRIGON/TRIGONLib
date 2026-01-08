package frc.trigon.lib.hardware.rev.spark;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import org.littletonrobotics.junction.Logger;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.rev.spark.io.RealSparkIO;
import frc.trigon.lib.hardware.rev.spark.io.SimulationSparkIO;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;

/**
 * A class the represents a Spark motor. Used to control and read data from a Spark motor.
 */
public class SparkMotor {
    private final String motorName;
    private final SparkIO motorIO;
    private final SparkInputs motorInputs;
    private final int id;

    /**
     * Creates a new Spark motor.
     *
     * @param id        the motor's ID
     * @param sparkType the type of Spark motor
     * @param motorName the name of the motor
     */
    public SparkMotor(int id, SparkType sparkType, String motorName) {
        this.id = id;
        this.motorName = motorName;
        motorInputs = new SparkInputs(motorName);
        motorIO = createSparkIO(id, sparkType);
    }

    /**
     * Processes the inputs of the motor.
     */
    public void update() {
        Logger.processInputs("Motors/" + motorName, motorInputs);
        motorIO.updateSimulation();
    }

    public int getID() {
        return id;
    }

    /**
     * Registers a threaded signal to be logged from the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the signal to be registered
     */
    public void registerThreadedSignal(SparkSignal signal) {
        final SparkStatusSignal statusSignal = signal.getStatusSignal(motorIO.getMotor(), motorIO.getEncoder());
        motorInputs.registerThreadedSignal(statusSignal);
    }

    /**
     * Registers a signal to be read from the motor.
     *
     * @param signal the signal to be read
     */
    public void registerSignal(SparkSignal signal) {
        final SparkStatusSignal statusSignal = signal.getStatusSignal(motorIO.getMotor(), motorIO.getEncoder());
        motorInputs.registerSignal(statusSignal);
    }

    /**
     * Gets a signal from the motor.
     *
     * @param signal the signal to get
     * @return the signal
     */
    public double getSignal(SparkSignal signal) {
        return motorInputs.getSignal(signal.name);
    }

    /**
     * Gets a threaded signal from the motor.
     * Threaded signals use threading to process certain signals separately at a faster rate.
     *
     * @param signal the threaded signal to get
     * @return the threaded signal
     */
    public double[] getThreadedSignal(SparkSignal signal) {
        return motorInputs.getThreadedSignal(signal.name);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value       the value to set depending on the control type
     * @param controlType the control type
     */
    public void setReference(double value, SparkBase.ControlType controlType) {
        motorIO.setReference(value, controlType);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value       the value to set depending on the control type
     * @param controlType the control type
     * @param slot        the PID slot to use
     */
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot) {
        motorIO.setReference(value, controlType, slot);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value                the value to set
     * @param controlType          the control type
     * @param slot                 the PID slot to use
     * @param arbitraryFeedForward the feed forward value
     */
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward) {
        motorIO.setReference(value, controlType, slot, arbitraryFeedForward);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value                     the value to set depending on the control type
     * @param controlType               the control type
     * @param slot                      the PID slot to use
     * @param arbitraryFeedForward      the feed forward value
     * @param arbitraryFeedForwardUnits the units of the feed forward value
     */
    public void setReference(double value, SparkBase.ControlType controlType, ClosedLoopSlot slot, double arbitraryFeedForward, SparkClosedLoopController.ArbFFUnits arbitraryFeedForwardUnits) {
        motorIO.setReference(value, controlType, slot, arbitraryFeedForward, arbitraryFeedForwardUnits);
    }

    /**
     * Set the amount of time to wait for a periodic status frame before returning a timeout error.
     * This timeout will apply to all periodic status frames for the SPARK motor controller.
     *
     * @param timeoutMs the new transmission period in milliseconds
     */
    public void setPeriodicFrameTimeout(int timeoutMs) {
        motorIO.setPeriodicFrameTimeout(timeoutMs);
    }

    /**
     * Stops the motor.
     */
    public void stopMotor() {
        motorIO.stopMotor();
    }

    /**
     * Sets the motor's inverted value.
     *
     * @param inverted should the motor be inverted
     */
    public void setInverted(boolean inverted) {
        motorIO.setInverted(inverted);
    }

    /**
     * Sets the motors neutral mode.
     *
     * @param brake true if the motor should brake, false if it should coast
     */
    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    /**
     * Applies both the real and simulation configurations to the motor.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration       configuration to be used in real life
     * @param simulationConfiguration configuration to be used in simulation
     * @param resetMode               whether to reset safe parameters before setting the configuration or not
     * @param persistMode             whether to persist the parameters after setting the configuration or not
     */
    public void applyConfigurations(SparkBaseConfig realConfiguration, SparkBaseConfig simulationConfiguration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        if (RobotHardwareStats.isSimulation())
            motorIO.configure(simulationConfiguration, resetMode, persistMode);
        else
            motorIO.configure(realConfiguration, resetMode, persistMode);
    }

    /**
     * Applies the configuration to be used both in real life and in simulation.
     *
     * @param configuration the configuration to apply
     * @param resetMode     whether to reset safe parameters before setting the configuration or not
     * @param persistMode   whether to persist the parameters after setting the configuration or not
     */
    public void applyConfiguration(SparkBaseConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motorIO.configure(configuration, resetMode, persistMode);
    }

    /**
     * Applies the configuration to be used when {@link RobotHardwareStats#isSimulation()} is false.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param realConfiguration the configuration to apply
     * @param resetMode         whether to reset safe parameters before setting the configuration or not
     * @param persistMode       whether to persist the parameters after setting the configuration or not
     */
    public void applyRealConfiguration(SparkBaseConfig realConfiguration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        if (!RobotHardwareStats.isSimulation())
            motorIO.configure(realConfiguration, resetMode, persistMode);
    }

    /**
     * Applies the configuration to be used in simulation.
     * Having two different configurations allows for tuning motor behavior in simulation which might not perfectly mimic real life performance.
     *
     * @param simulationConfiguration the configuration to apply
     * @param resetMode               whether to reset safe parameters before setting the configuration or not
     * @param persistMode             whether to persist the parameters after setting the configuration or not
     */
    public void applySimulationConfiguration(SparkBaseConfig simulationConfiguration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        if (RobotHardwareStats.isSimulation())
            motorIO.configure(simulationConfiguration, resetMode, persistMode);
    }

    /**
     * Sets the physics simulation to be used by the motor. Must be called for the motor to work in simulation.
     *
     * @param physicsSimulation      the physics simulation to be used
     * @param isUsingAbsoluteEncoder whether the motor is using a relative encoder or an absolute encoder
     */
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation, boolean isUsingAbsoluteEncoder) {
        motorIO.setPhysicsSimulation(physicsSimulation, isUsingAbsoluteEncoder);
    }

    private SparkIO createSparkIO(int id, SparkType sparkType) {
        if (RobotHardwareStats.isReplay())
            return new SparkIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationSparkIO(id);
        return new RealSparkIO(id, sparkType);
    }
}