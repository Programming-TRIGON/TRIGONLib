package frc.trigon.lib.hardware.misc.servo;

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
     * Constructs a new Servo object.
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
     * Sets the target speed of the servo.
     * This method is used for speed control servos, where the PWM pulse's width dictates the speed of the servo.
     * The PWM boundaries ({@link #setPWMBoundaries(int, int, int, int, int)}) must be called before speed control can be used.
     *
     * @param targetSpeed the target speed of the servo, from -1.0 to 1.0.
     */
    public void setTargetSpeed(double targetSpeed) {
        servoIO.setTargetSpeed(targetSpeed);
    }

    /**
     * Sets the servo position using a scaled 0 to 1.0 value. 0 corresponds to one extreme of the servo and 1.0 corresponds to the other
     * This method works regardless of the types of servo being used.
     * For speed servos this will set the speed of the servo.
     * For angle servos this will set the angle of the servo, dependent on the range given in {@link Servo#setMaximumAngle(Rotation2d)}.
     * In simulation, this will act as though it was setting the target speed of the servo from 0 to 1.
     *
     * @param value the target position/speed of the servo on a scale from 0 to 1
     */
    public void set(double value) {
        servoIO.set(value);
    }

    /**
     * Set the target angle of the servo.
     * This method is used for angle control servos, where the PWM pulse's width dictates the angle of the servo.
     * Any angles outside the servo's range will be clamped down to a legitimate position.
     *
     * @param targetAngle the target angle of the servo
     */
    public void setTargetAngle(Rotation2d targetAngle) {
        servoIO.setTargetAngle(targetAngle);
    }

    /**
     * Set the boundaries of the PWM pulse widths.
     * The values determine the upper and lower speeds as well as the deadband bracket.
     *
     * @param maximumPulseWidthMicroseconds    the maximum PWM pulse width in microseconds
     * @param maximumDeadbandRangeMicroseconds the high end of the deadband range pulse width in microseconds
     * @param centerPulseMicroseconds          the center (off) pulse width in microseconds
     * @param minimumDeadbandRangeMicroseconds the low end of the deadband pulse width in microseconds
     * @param minimumPulseWidthMicroseconds    the minimum PWM pulse width in microseconds
     */
    public void setPWMBoundaries(int maximumPulseWidthMicroseconds, int maximumDeadbandRangeMicroseconds,
                                 int centerPulseMicroseconds, int minimumDeadbandRangeMicroseconds,
                                 int minimumPulseWidthMicroseconds) {
        servoIO.setPWMBoundaries(maximumPulseWidthMicroseconds, maximumDeadbandRangeMicroseconds, centerPulseMicroseconds, minimumDeadbandRangeMicroseconds, minimumPulseWidthMicroseconds);
    }

    /**
     * Set the range of the servo.
     * This depends on the type of servo being used, and should be called once before being used.
     * The range is 0 to 180 degrees by default.
     *
     * @param maximumAngle the maximum angle of the servo
     */
    public void setMaximumAngle(Rotation2d maximumAngle) {
        servoIO.setMaximumAngle(maximumAngle);
    }

    /**
     * @return the target angle of the servo
     */
    public Rotation2d getTargetAngle() {
        return inputs.targetAngle;
    }

    /**
     * @return the target speed of the servo
     */
    public double getTargetSpeed() {
        return inputs.targetSpeed;
    }
}