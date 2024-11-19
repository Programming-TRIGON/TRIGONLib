package org.trigon.hardware.rev.spark;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Timer;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.rev.spark.io.RealSparkIO;
import org.trigon.hardware.rev.spark.io.SimulationSparkIO;

import java.util.concurrent.CompletableFuture;

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
     * @param id              the motor ID
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
     * Registers a signal to be read from the motor.
     *
     * @param signal the signal to be read
     */
    public void registerSignal(SparkSignal signal) {
        this.registerSignal(signal, false);
    }

    /**
     * Registers a signal to be read from the motor.
     *
     * @param signal     the signal to be read
     * @param isThreaded whether the signal should be read in a separate thread
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
     * @param signal the signal to get
     * @return the signal
     */
    public double[] getThreadedSignal(SparkSignal signal) {
        return motorInputs.getThreadedSignal(signal.name);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value       the value to set
     * @param controlType the control type
     */
    public void setReference(double value, CANSparkBase.ControlType controlType) {
        motorIO.setReference(value, controlType);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value       the value to set
     * @param controlType the control type
     * @param pidSlot     the PID slot to use
     */
    public void setReference(double value, CANSparkBase.ControlType controlType, int pidSlot) {
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
    public void setReference(double value, CANSparkBase.ControlType controlType, int pidSlot, double arbFeedForward) {
        motorIO.setReference(value, controlType, pidSlot, arbFeedForward);
    }

    /**
     * Sends a request to the motor.
     *
     * @param value               the value to set
     * @param controlType         the control type
     * @param pidSlot             the PID slot to use
     * @param arbFeedForward      the feed forward value
     * @param arbFeedForwardUnits the units of the feed forward value
     */
    public void setReference(double value, CANSparkBase.ControlType controlType, int pidSlot, double arbFeedForward, SparkPIDController.ArbFFUnits arbFeedForwardUnits) {
        motorIO.setReference(value, controlType, pidSlot, arbFeedForward, arbFeedForwardUnits);
    }

    /**
     * Set the rate of transmission for periodic frames from the SPARK motor.
     *
     * @param frame    the frame to set the period for
     * @param periodMs the period in milliseconds
     */
    public void setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame frame, int periodMs) {
        motorIO.setPeriodicFramePeriod(frame, periodMs);
    }

    /**
     * Stops the motor.
     */
    public void stopMotor() {
        motorIO.stopMotor();
    }

    /**
     * Sets the motors neutral value.
     *
     * @param brake should the motor brake
     */
    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    /**
     * Sets the motors inverted value.
     *
     * @param inverted should the motor be inverted
     */
    public void setInverted(boolean inverted) {
        motorIO.setInverted(inverted);
    }

    /**
     * Enables and sets the voltage compensation.
     *
     * @param voltage the voltage to set
     */
    public void enableVoltageCompensation(double voltage) {
        motorIO.enableVoltageCompensation(voltage);
    }

    /**
     * Sets the ramp rate for closed loop control.
     *
     * @param rampRate the ramp rate to set
     */
    public void setClosedLoopRampRate(double rampRate) {
        motorIO.setClosedLoopRampRate(rampRate);
    }

    /**
     * Sets the smart current limitAmperes in amperes.
     *
     * @param limitAmperes the limitAmperes to set
     */
    public void setSmartCurrentLimit(int limitAmperes) {
        motorIO.setSmartCurrentLimit(limitAmperes);
    }

    /**
     * Sets the ramp rate for open loop control.
     *
     * @param rampRate the ramp rate to set
     */
    public void setOpenLoopRampRate(double rampRate) {
        motorIO.setOpenLoopRampRate(rampRate);
    }

    /**
     * Sets the PID values for the motor.
     *
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    public void setPID(double p, double i, double d) {
        motorIO.setPID(p, i, d);
    }

    /**
     * Sets the conversion factor for values received the motor.
     *
     * @param conversionsFactor the conversion factor to set
     */
    public void setConversionsFactor(double conversionsFactor) {
        motorIO.setConversionsFactor(conversionsFactor);
    }

    /**
     * Restores the motor settings to factory defaults.
     */
    public void restoreFactoryDefaults() {
        motorIO.restoreFactoryDefaults();
    }

    /**
     * Enables PID wrapping.
     *
     * @param minimumInput the minimum input
     * @param maximumInput the maximum input
     */
    public void enablePIDWrapping(double minimumInput, double maximumInput) {
        motorIO.enablePIDWrapping(minimumInput, maximumInput);
    }

    /**
     * Writes all settings to flash
     */
    public void burnFlash() {
        CompletableFuture.runAsync(() -> {
            Timer.delay(5);
            motorIO.burnFlash();
        });
    }

    private SparkIO createSparkIO(int id, SparkType sparkType, DCMotor simulationMotor) {
        if (RobotHardwareStats.isReplay())
            return new SparkIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationSparkIO(id, simulationMotor);
        return new RealSparkIO(id, sparkType);
    }
}
