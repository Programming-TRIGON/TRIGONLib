package org.trigon.hardware.misc.servo.io;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import org.trigon.hardware.misc.servo.ServoIO;
import org.trigon.hardware.misc.servo.ServoInputsAutoLogged;

public class SimulationServoIO extends ServoIO {
    private final SimDeviceSim simulationServo;

    public SimulationServoIO(String name) {
        simulationServo = new SimDeviceSim(name);
    }

    @Override
    protected void updateInputs(ServoInputsAutoLogged inputs) {
        inputs.positionRotations = simulationServo.getDouble("Position").get();
        inputs.targetAngle = Rotation2d.fromDegrees(simulationServo.getDouble("Angle").get());
        inputs.speed = simulationServo.getDouble("Speed").get();
    }

    @Override
    protected void setSpeed(double speed) {
        final SimDouble speedSim = simulationServo.getDouble("Speed");
        speedSim.set(speed);
    }

    @Override
    protected void setAngle(Rotation2d angle) {
        final SimDouble angleSim = simulationServo.getDouble("Angle");
        angleSim.set(angle.getDegrees());
    }

    @Override
    protected void setPosition(double position) {
        final SimDouble positionSim = simulationServo.getDouble("Position");
        positionSim.set(position);
    }
}