package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class SimulationServoIO extends ServoIO {
    private static final Rotation2d MIN_SERVO_ANGLE = Rotation2d.fromDegrees(0);
    private Rotation2d maxServoAngle = Rotation2d.fromDegrees(180);
    private double
            targetSpeed = 0,
            targetScaledPosition = 0;

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = Rotation2d.fromDegrees(targetScaledPosition * getServoAngleRange().getDegrees() + MIN_SERVO_ANGLE.getDegrees());
        inputs.targetSpeed = targetSpeed;
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    @Override
    protected void set(double value) {
        this.targetScaledPosition = MathUtil.clamp(value, 0, 1);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final Rotation2d clampedAngle = clampAngleToRange(targetAngle);
        targetScaledPosition = calculateScaledPosition(clampedAngle);
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