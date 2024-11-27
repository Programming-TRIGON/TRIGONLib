package org.trigon.hardware.rev.spark;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.rev.spark.io.RealSparkIO;
import org.trigon.hardware.rev.spark.io.SimulationSparkIO;

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
     * @param id              the motor's ID
     * @param sparkType       the type of Spark motor
     * @param motorName       the name of the motor
     * @param simulationMotor the motor to be used in simulation
     */
    public SparkMotor(int id, SparkType sparkType, String motorName, DCMotor simulationMotor) {
        this.id = id;
        this.motorName = motorName;
        motorInputs = new SparkInputs(motorName);
        motorIO = createSparkIO(id, sparkType, simulationMotor);
    }

    /**
     * Processes the inputs of the motor.
     */
    public void update() {
        Logger.processInputs("Motors/" + motorName, motorInputs);
    }

    /**
     * Gets the ID of the motor.
     *
     * @return the ID
     */
    public int getID() {
        return id;
    }

    /**
     * Registers a signal to be logged from the motor.
     *
     * @param signal the signal to be registered
     */
    public void registerSignal(SparkSignal signal) {
        this.registerSignal(signal, false);
    }

    /**
     * Registers a signal to be read from the motor.
     *
     * @param signal     the signal to be read
     * @param isThreaded whether the signal should be read in a separate thread or not
     */
    public void registerSignal(SparkSignal signal, boolean isThreaded) {
        final SparkStatusSignal statusSignal = signal.getStatusSignal(motorIO.getMotor(), motorIO.getEncoder());
        if (isThreaded) {
            motorInputs.registerThreadedSignal(statusSignal);
            return;
        }
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
     * @param pidSlot     the PID slot to use
     */
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot) {
        motorIO.setReference(value, controlType, pidSlot);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value          the value to set
     * @param controlType    the control type
     * @param pidSlot        the PID slot to use
     * @param arbFeedForward the feed forward value
     */
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward) {
        motorIO.setReference(value, controlType, pidSlot, arbFeedForward);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value               the value to set depending on the control type
     * @param controlType         the control type
     * @param pidSlot             the PID slot to use
     * @param arbFeedForward      the feed forward value
     * @param arbFeedForwardUnits the units of the feed forward value
     */
    public void setReference(double value, SparkBase.ControlType controlType, int pidSlot, double arbFeedForward, SparkClosedLoopController.ArbFFUnits arbFeedForwardUnits) {
        motorIO.setReference(value, controlType, pidSlot, arbFeedForward, arbFeedForwardUnits);
    }

    /**
     * Sets the transmission period for a specific periodic frame on the motor controller.
     * This method adjusts the rate at which the controller sends the frame, but the change is not saved permanently and will reset on powerup.
     *
     * @param periodMs the new transmission period in milliseconds
     */
    public void setPeriodicFramePeriod(int periodMs) {
        motorIO.setPeriodicFrameTimeout(periodMs);
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
     * Applies the configuration to the motor.
     *
     * @param configuration the configuration to apply
     * @param resetMode     whether to reset safe parameters before setting the configuration or not
     * @param persistMode   whether to persist the parameters after setting the configuration or not
     */
    public void configure(SparkMaxConfig configuration, SparkBase.ResetMode resetMode, SparkBase.PersistMode persistMode) {
        motorIO.configure(configuration, resetMode, persistMode);
    }

    /**
     * Updates the motor simulation. Only used in simulation. Should be called periodically.
     */
    public void updateSimulation() {
        motorIO.updateSimulation();
    }

    private SparkIO createSparkIO(int id, SparkType sparkType, DCMotor simulationMotor) {
        if (RobotHardwareStats.isReplay())
            return new SparkIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationSparkIO(id, simulationMotor);
        return new RealSparkIO(id, sparkType);
    }
}