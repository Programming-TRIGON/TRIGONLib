package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SimulationServoIO extends ServoIO {
    private static final double MAX_SERVO_ANGLE_DEGREES = 180;
    private static final double MIN_SERVO_ANGLE_DEGREES = 0;
    private final PWMSim servoSim;
    private MotorPhysicsSimulation motorPhysicsSimulation = null;

    public SimulationServoIO(int channel) {
        servoSim = new PWMSim(channel);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.targetAngle = Rotation2d.fromDegrees(servoSim.getPosition() * getServoAngleRange() + MIN_SERVO_ANGLE_DEGREES);
        inputs.speed = servoSim.getSpeed();
    }

    @Override
    protected void updateSimulation() {
        if (motorPhysicsSimulation == null)
            return;

        updatePhysicsSimulation();
        updateServoSimulation();
    }

    @Override
    protected void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        this.motorPhysicsSimulation = physicsSimulation;
    }

    @Override
    protected void setTargetSpeed(double targetSpeed) {
        servoSim.setSpeed(targetSpeed);
    }

    @Override
    protected void setTargetAngle(Rotation2d targetAngle) {
        final double clampedAngleDegrees = MathUtil.clamp(targetAngle.getDegrees(), MIN_SERVO_ANGLE_DEGREES, MAX_SERVO_ANGLE_DEGREES);
        servoSim.setPosition((clampedAngleDegrees - MIN_SERVO_ANGLE_DEGREES) / getServoAngleRange());
    }

    private double getServoAngleRange() {
        return MAX_SERVO_ANGLE_DEGREES - MIN_SERVO_ANGLE_DEGREES;
    }

    private void updatePhysicsSimulation() {
        motorPhysicsSimulation.setInputVoltage(RobotHardwareStats.SUPPLY_VOLTAGE);
        motorPhysicsSimulation.updateMotor();
    }

    private void updateServoSimulation() {
        servoSim.setSpeed(motorPhysicsSimulation.getSystemVelocityRotationsPerSecond());
        servoSim.setPosition(motorPhysicsSimulation.getSystemPositionRotations());
    }
}