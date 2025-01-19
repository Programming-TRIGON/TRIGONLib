package org.trigon.hardware.misc.servo;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.Logger;

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
     * For speed servos this will set the speed of the servo.
     * For angle servos this will set the angle of the servo depending on the range given in {@link Servo#setMaxAngle(Rotation2d)}.
     *
     * @param value the target position/speed of the servo on a scale between 0 and 1
     */
    public void set(double value) {
        servoIO.set(value);
    }

    /**
     * Set the servo position by specifying the angle.
     * Any values passed to this method outside the range set in {@link Servo#setMaxAngle(Rotation2d)} will be coerced to the boundary.
     *
     * @param targetAngle the angle of the servo
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
     * Set the range of the servo.
     * This depends on the type of servo being used, and should be called once before being used.
     * The range is 0 to 180 degrees by default.
     *
     * @param maxAngle the maximum angle of the servo
     */
    public void setMaxAngle(Rotation2d maxAngle) {
        servoIO.setMaxAngle(maxAngle);
    }

    /**
     * @return the target angle of the servo
     */
    public Rotation2d getTargetAngle() {
        return inputs.targetAngle;
    }

    /**
     * @return the target scaled position of the servo
     */
    public double getTargetScaledPosition() {
        return inputs.targetScaledPosition;
    }

    /**
     * @return the targetSpeed of the servo
     */
    public double getSpeed() {
        return inputs.targetSpeed;
    }
}