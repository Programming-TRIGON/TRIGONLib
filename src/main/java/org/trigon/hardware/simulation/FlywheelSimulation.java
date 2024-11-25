package org.trigon.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import org.trigon.hardware.RobotHardwareStats;

/**
 * A class the represents a simulation of a flywheel mechanism.
 */
public class FlywheelSimulation extends MotorPhysicsSimulation {
    private final FlywheelSim flywheelSimulation;
    private double lastPositionRadians = 0;

    /**
     * Creates a new FlywheelSimulation.
     *
     * @param gearbox   the motor(s) used to control the flywheel
     * @param gearRatio the gearbox's gear ratio
     * @param kv        voltage needed to maintain constant velocity
     * @param ka        voltage needed to induce a specific acceleration
     */
    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double kv, double ka) {
        super(gearRatio);
        flywheelSimulation = new FlywheelSim(LinearSystemId.identifyVelocitySystem(kv, ka), gearbox, gearRatio);
    }

    /**
     * Creates a new FlywheelSimulation.
     *
     * @param gearbox         the motor used to control the flywheel
     * @param gearRatio       the gearbox's gear ratio
     * @param momentOfInertia the flywheel's moment of inertia
     */
    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double momentOfInertia) {
        super(gearRatio);
        flywheelSimulation = new FlywheelSim(LinearSystemId.createFlywheelSystem(gearbox, momentOfInertia, gearRatio), gearbox, gearRatio);
    }

    /**
     * Gets the current draw of the flywheel in amperes.
     *
     * @return the current in amperes
     */
    @Override
    public double getCurrent() {
        return flywheelSimulation.getCurrentDrawAmps();
    }

    /**
     * Gets the position of the flywheel in rotations.
     *
     * @return The position in rotations
     */
    @Override
    public double getSystemPositionRotations() {
        return Units.radiansToRotations(lastPositionRadians);
    }

    /**
     * Gets the velocity of the flywheel in rotations per second.
     *
     * @return The velocity in rotations per second
     */
    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Units.radiansToRotations(flywheelSimulation.getAngularVelocityRadPerSec());
    }

    /**
     * Sets the input voltage of the flywheel.
     *
     * @param voltage The input voltage
     */
    @Override
    public void setInputVoltage(double voltage) {
        flywheelSimulation.setInputVoltage(voltage);
    }

    /**
     * Updates the flywheel simulation.
     */
    @Override
    public void updateMotor() {
        flywheelSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
        lastPositionRadians = lastPositionRadians + flywheelSimulation.getAngularVelocityRadPerSec() * RobotHardwareStats.getPeriodicTimeSeconds();
    }
}
