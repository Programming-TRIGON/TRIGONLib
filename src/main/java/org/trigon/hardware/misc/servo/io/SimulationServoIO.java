package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class SimulationServoIO extends ServoIO {
    private static final double MAX_SERVO_ANGLE = 180;
    private static final double MIN_SERVO_ANGLE = 0;
    private final PWMSim servoSim;

    public SimulationServoIO(int channel) {
        servoSim = new PWMSim(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.positionRotations = servoSim.getPosition();
        inputs.targetAngle = Rotation2d.fromDegrees(servoSim.getPosition() * getServoAngleRange() + MIN_SERVO_ANGLE);
        inputs.speed = servoSim.getSpeed();
    }

    @Override
    protected void setSpeed(double speed) {
        servoSim.setSpeed(speed);
    }

    @Override
    protected void setAngle(Rotation2d angle) {
        final double targetAngleDegrees = clampAngleToRange(angle);
        setPosition((targetAngleDegrees - MIN_SERVO_ANGLE) / getServoAngleRange());
    }

    @Override
    protected void setPosition(double position) {
        servoSim.setPosition(position);
    }

    private double clampAngleToRange(Rotation2d angle) {
        double degrees = angle.getDegrees();
        if (degrees < MIN_SERVO_ANGLE)
            degrees = MIN_SERVO_ANGLE;
        else if (degrees > MAX_SERVO_ANGLE)
            degrees = MAX_SERVO_ANGLE;
        return degrees;
    }

    private double getServoAngleRange() {
        return MAX_SERVO_ANGLE - MIN_SERVO_ANGLE;
    }
}