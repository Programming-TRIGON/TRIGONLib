package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class SimulationServoIO extends ServoIO {
    private Rotation2d
            maxServoAngle = Rotation2d.fromDegrees(180),
            minServoAngle = Rotation2d.fromDegrees(0);
    private double
            targetSpeed = 0,
            targetScaledPosition = 0;

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = Rotation2d.fromDegrees(targetScaledPosition * getServoAngleRange().getDegrees() + minServoAngle.getDegrees());
        inputs.targetScaledPosition = targetScaledPosition;
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
        final double clampedAngleDegrees = MathUtil.clamp(targetAngle.getDegrees(), minServoAngle.getDegrees(), maxServoAngle.getDegrees());
        targetScaledPosition = clampedAngleDegrees - (minServoAngle.getDegrees() / getServoAngleRange().getDegrees());
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