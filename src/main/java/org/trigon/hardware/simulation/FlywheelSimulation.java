package org.trigon.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import org.trigon.hardware.RobotHardwareStats;


public class FlywheelSimulation extends MotorPhysicsSimulation {
    private final FlywheelSim flywheelSimulation;
    private double lastPositionRadians = 0;

    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double momentOfInertia) {
        flywheelSimulation = new FlywheelSim(gearbox, gearRatio, momentOfInertia);
    }

    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double kv, double ka) {
        flywheelSimulation = new FlywheelSim(LinearSystemId.identifyVelocitySystem(kv, ka), gearbox, gearRatio);
    }

    @Override
    public double getCurrent() {
        return flywheelSimulation.getCurrentDrawAmps();
    }

    @Override
    public double getPositionRotations() {
        return Units.radiansToRotations(lastPositionRadians);
    }

    @Override
    public double getVelocityRotationsPerSecond() {
        return Units.radiansToRotations(flywheelSimulation.getAngularVelocityRadPerSec());
    }

    @Override
    public void setInputVoltage(double voltage) {
        flywheelSimulation.setInputVoltage(voltage);
    }

    @Override
    public void updateMotor() {
        flywheelSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
        lastPositionRadians = lastPositionRadians + flywheelSimulation.getAngularVelocityRadPerSec() * RobotHardwareStats.getPeriodicTimeSeconds();
    }
}