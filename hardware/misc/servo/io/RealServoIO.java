package frc.trigon.lib.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Servo;
import frc.trigon.lib.hardware.misc.servo.ServoIO;
import frc.trigon.lib.hardware.misc.servo.ServoInputsAutoLogged;

public class RealServoIO extends ServoIO {
    private final Servo servo;
    private Rotation2d maximumServoAngle = Rotation2d.fromDegrees(180);

    public RealServoIO(int channel) {
        servo = new Servo(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = maximumServoAngle.times(servo.get());
        inputs.targetSpeed = servo.getSpeed();
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        servo.setSpeed(targetSpeed);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final Rotation2d clampedTargetAngle = clampAngleToServoRange(targetAngle);
        servo.set(calculateScaledPosition(clampedTargetAngle));
    }

    @Override
    protected void set(double value) {
        servo.set(MathUtil.clamp(value, 0, 1));
    }

    @Override
    protected void setPWMBoundaries(int maximumPulseWidthMicroseconds, int maximumDeadbandRangeMicroseconds,
                                    int centerPulseMicroseconds, int minimumDeadbandRangeMicroseconds,
                                    int minimumPulseWidthMicroseconds) {
        servo.setBoundsMicroseconds(maximumPulseWidthMicroseconds, maximumDeadbandRangeMicroseconds, centerPulseMicroseconds, minimumDeadbandRangeMicroseconds, minimumPulseWidthMicroseconds);
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