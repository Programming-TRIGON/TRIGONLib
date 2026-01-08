package frc.trigon.lib.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.trigon.lib.hardware.misc.servo.ServoIO;
import frc.trigon.lib.hardware.misc.servo.ServoInputsAutoLogged;

public class SimulationServoIO extends ServoIO {
    private Rotation2d maximumServoAngle = Rotation2d.fromDegrees(180);
    private double
            targetSpeed = 0,
            targetScaledPosition = 0;

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = maximumServoAngle.times(targetScaledPosition);
        inputs.targetSpeed = targetSpeed;
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    @Override
    protected void set(double value) {
        this.targetSpeed = MathUtil.clamp(value, 0, 1);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final Rotation2d clampedAngle = clampAngleToServoRange(targetAngle);
        targetScaledPosition = calculateScaledPosition(clampedAngle);
    }

    @Override
    protected void setMaximumAngle(Rotation2d maximumAngle) {
        maximumServoAngle = maximumAngle;
    }

    private Rotation2d clampAngleToServoRange(Rotation2d angle) {
        return Rotation2d.fromDegrees(MathUtil.clamp(angle.getDegrees(), 0, maximumServoAngle.getDegrees()));
    }

    private double calculateScaledPosition(Rotation2d angle) {
        return angle.getDegrees() / maximumServoAngle.getDegrees();
    }
}