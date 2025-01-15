package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Servo;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class RealServoIO extends ServoIO {
    private final Servo servo;

    public RealServoIO(int channel) {
        servo = new Servo(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = Rotation2d.fromDegrees(servo.getAngle());
        inputs.speed = servo.getSpeed();
    }

    @Override
    protected void setSpeed(double speed) {
        servo.setSpeed(speed);
    }

    @Override
    protected void setAngle(Rotation2d angle) {
        servo.setAngle(angle.getDegrees());
    }

    @Override
    protected void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
        servo.setBoundsMicroseconds(maxPulseWidthMicroseconds, maxDeadbandRangeMicroseconds, centerPulseMicroseconds, minDeadbandRangeMicroseconds, minPulseWidthMicroseconds);
    }
}