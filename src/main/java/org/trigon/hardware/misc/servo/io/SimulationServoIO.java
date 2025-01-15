package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;
import org.trigon.utilities.Conversions;

public class SimulationServoIO extends ServoIO {
    private static final double MAX_SERVO_ANGLE_DEGREES = 180;
    private static final double MIN_SERVO_ANGLE_DEGREES = 0;
    private final PWMSim servoSim;

    public SimulationServoIO(int channel) {
        servoSim = new PWMSim(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = Rotation2d.fromDegrees(servoSim.getPosition() * getServoAngleRange() + MIN_SERVO_ANGLE_DEGREES);
        inputs.speed = servoSim.getSpeed();
    }

    @Override
    protected void setSpeed(double speed) {
        servoSim.setSpeed(speed);
    }

    @Override
    protected void setAngle(Rotation2d angle) {
        final double clampedAngleDegrees = Conversions.clampToRange(angle.getDegrees(), MIN_SERVO_ANGLE_DEGREES, MAX_SERVO_ANGLE_DEGREES);
        servoSim.setPosition((clampedAngleDegrees - MIN_SERVO_ANGLE_DEGREES) / getServoAngleRange());
    }

    private double getServoAngleRange() {
        return MAX_SERVO_ANGLE_DEGREES - MIN_SERVO_ANGLE_DEGREES;
    }
}