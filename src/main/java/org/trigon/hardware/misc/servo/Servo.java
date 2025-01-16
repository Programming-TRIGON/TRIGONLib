package org.trigon.hardware.misc.servo;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

/**
 * A wrapper class representing a servo motor.
 */
public class Servo {
    private final String name;
    private final ServoIO servoIO;
    private final ServoInputsAutoLogged inputs = new ServoInputsAutoLogged();

    /**
     * Constructs a Servo object.
     *
     * @param channel the PWM channel the servo is connected to
     * @param name    the name of the servo
     */
    public Servo(int channel, String name) {
        this.name = name;
        servoIO = ServoIO.generateServoIO(channel);
    }

    /**
     * Updates the servo and logs its inputs.
     */
    public void update() {
        servoIO.updateInputs(inputs);
        Logger.processInputs("Servos/" + name, inputs);
    }

    /**
     * Set the PWM value based on a speed.
     * Should be called before {@link #setBoundsMicroseconds(int, int, int, int, int)} is called.
     *
     * @param targetSpeed the speed of the servo between -1.0 and 1.0.
     */
    public void setTargetSpeed(double targetSpeed) {
        servoIO.setTargetSpeed(targetSpeed);
    }

    /**
     * Sets the servo position using a scaled 0 to 1.0 value. 0 corresponds to one extreme of the servo and 1.0 corresponds to the other
     * This method works regardless of the types of servo being used.
     *
     * @param targetScaledPosition the target position of the servo on a scale between 0 and 1
     */
    public void setTargetScaledPosition(double targetScaledPosition) {
        servoIO.setTargetScaledPosition(targetScaledPosition);
    }

    /**
     * Set the servo position by specifying the angle.
     * This method will work for servos with the range of 0 to 180 degrees.
     * Any values passed to this method outside the specified range will be coerced to the boundary.
     *
     * @param targetAngle the angle of the servo between 0 and 180 degrees
     */
    public void setTargetAngle(Rotation2d targetAngle) {
        servoIO.setTargetAngle(targetAngle);
    }

    /**
     * Set the bounds on the PWM pulse widths.
     * The values determine the upper and lower speeds as well as the deadband bracket.
     *
     * @param maxPulseWidthMicroseconds    the maximum PWM pulse width in microseconds
     * @param maxDeadbandRangeMicroseconds the high end of the deadband range pulse width in microseconds
     * @param centerPulseMicroseconds      the center (off) pulse width in microseconds
     * @param minDeadbandRangeMicroseconds the low end of the deadband pulse width in microseconds
     * @param minPulseWidthMicroseconds    the minimum pulse width in microseconds
     */
    public void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
        servoIO.setBoundsMicroseconds(maxPulseWidthMicroseconds, maxDeadbandRangeMicroseconds, centerPulseMicroseconds, minDeadbandRangeMicroseconds, minPulseWidthMicroseconds);
    }

    /**
     * @return the target angle of the servo
     */
    public Rotation2d getTargetAngle() {
        return inputs.targetAngle;
    }

    /**
     * @return the targetSpeed of the servo
     */
    public double getSpeed() {
        return inputs.targetSpeed;
    }

    /**
     * Set the physics simulation for the servo. Must be called for the servo to work in simulation.
     *
     * @param physicsSimulation the physics simulation to be used
     */
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        servoIO.setPhysicsSimulation(physicsSimulation);
    }
}