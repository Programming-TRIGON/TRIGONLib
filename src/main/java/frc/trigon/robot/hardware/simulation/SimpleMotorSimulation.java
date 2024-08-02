package frc.trigon.robot.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.trigon.robot.constants.RobotConstants;

public class SimpleMotorSimulation extends MotorPhysicsSimulation {
    private final DCMotorSim motorSimulation;

    public SimpleMotorSimulation(DCMotor gearbox, double gearRatio, double momentOfInertia) {
        motorSimulation = new DCMotorSim(gearbox, gearRatio, momentOfInertia);
    }

    @Override
    public double getCurrent() {
        return motorSimulation.getCurrentDrawAmps();
    }

    @Override
    public double getPositionRotations() {
        return Units.radiansToRotations(motorSimulation.getAngularPositionRad());
    }

    @Override
    public double getVelocityRotationsPerSecond() {
        return Units.radiansToRotations(motorSimulation.getAngularVelocityRadPerSec());
    }

    @Override
    public void setInputVoltage(double voltage) {
        motorSimulation.setInputVoltage(voltage);
    }

    @Override
    public void updateMotor() {
        motorSimulation.update(RobotConstants.PERIODIC_TIME_SECONDS);
    }
}
