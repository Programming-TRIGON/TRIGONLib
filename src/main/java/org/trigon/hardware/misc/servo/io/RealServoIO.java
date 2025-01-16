package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Servo;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class RealServoIO extends ServoIO {
    private Rotation2d
            maxServoAngle = Rotation2d.fromDegrees(180),
            minServoAngle = Rotation2d.fromDegrees(0);
    private final Servo servo;

    public RealServoIO(int channel) {
        servo = new Servo(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetScaledPosition = servo.getPosition();
        inputs.targetAngle = Rotation2d.fromDegrees(inputs.targetScaledPosition * getServoAngleRange().getDegrees() + minServoAngle.getDegrees());
        inputs.targetSpeed = servo.getSpeed();
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        servo.setSpeed(targetSpeed);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final Rotation2d clampedTargetAngle = Rotation2d.fromDegrees(MathUtil.clamp(targetAngle.getDegrees(), minServoAngle.getDegrees(), maxServoAngle.getDegrees()));
        servo.setPosition(clampedTargetAngle.minus(minServoAngle).getDegrees() / getServoAngleRange().getDegrees());
    }

    @Override
    protected void setTargetScaledPosition(double targetScaledPosition) {
        servo.setPosition(targetScaledPosition);
    }

    @Override
    protected void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
        servo.setBoundsMicroseconds(maxPulseWidthMicroseconds, maxDeadbandRangeMicroseconds, centerPulseMicroseconds, minDeadbandRangeMicroseconds, minPulseWidthMicroseconds);
    }

    @Override
    protected void setRange(Rotation2d minAngle, Rotation2d maxAngle) {
        minServoAngle = minAngle;
        maxServoAngle = maxAngle;
    }

    private Rotation2d getServoAngleRange() {
        return maxServoAngle.minus(minServoAngle);
    }
}