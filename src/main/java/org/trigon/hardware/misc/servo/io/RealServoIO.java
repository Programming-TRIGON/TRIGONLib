package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Servo;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class RealServoIO extends ServoIO {
    private static final Rotation2d MIN_SERVO_ANGLE = Rotation2d.fromDegrees(0);
    private final Servo servo;
    private Rotation2d maxServoAngle = Rotation2d.fromDegrees(180);

    public RealServoIO(int channel) {
        servo = new Servo(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetScaledPosition = servo.get();
        inputs.targetAngle = Rotation2d.fromDegrees(servo.get() * getServoAngleRange().getDegrees() + MIN_SERVO_ANGLE.getDegrees());
        inputs.targetSpeed = servo.getSpeed();
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        servo.setSpeed(targetSpeed);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final Rotation2d clampedTargetAngle = clampAngleToRange(targetAngle);
        servo.set(calculateScaledPosition(clampedTargetAngle));
    }

    @Override
    protected void set(double value) {
        servo.set(value);
    }

    @Override
    protected void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
        servo.setBoundsMicroseconds(maxPulseWidthMicroseconds, maxDeadbandRangeMicroseconds, centerPulseMicroseconds, minDeadbandRangeMicroseconds, minPulseWidthMicroseconds);
    }

    @Override
    protected void setMaxAngle(Rotation2d maxAngle) {
        maxServoAngle = maxAngle;
    }

    private double calculateScaledPosition(Rotation2d angle) {
        return angle.minus(MIN_SERVO_ANGLE).getDegrees() / getServoAngleRange().getDegrees();
    }

    private Rotation2d clampAngleToRange(Rotation2d angle) {
        return Rotation2d.fromDegrees(MathUtil.clamp(angle.getDegrees(), MIN_SERVO_ANGLE.getDegrees(), maxServoAngle.getDegrees()));
    }

    private Rotation2d getServoAngleRange() {
        return maxServoAngle.minus(MIN_SERVO_ANGLE);
    }
}