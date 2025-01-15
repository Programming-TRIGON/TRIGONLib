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
     * @param speed the speed of the servo between -1.0 and 1.0.
     */
    public void setSpeed(double speed) {
        servoIO.setSpeed(speed);
    }

    /**
     * Set the angle of the servo.
     * Servo angles that are out of the supported range of the servo, the servo will go to the closest angle in the supported range.
     *
     * @param angle the angle of the servo
     */
    public void setAngle(Rotation2d angle) {
        servoIO.setAngle(angle);
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
     * @return the speed of the servo
     */
    public double getSpeed() {
        return inputs.speed;
    }
}