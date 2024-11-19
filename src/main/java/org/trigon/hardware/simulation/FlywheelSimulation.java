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
     * @param gearbox         The motor used to control the flywheel
     * @param gearRatio       The gear ratio
     * @param momentOfInertia The moment of inertia of the flywheel
     */
    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double momentOfInertia) {
        super(gearRatio);
        flywheelSimulation = new FlywheelSim(gearbox, gearRatio, momentOfInertia);
    }

    /**
     * Creates a new FlywheelSimulation.
     *
     * @param gearbox   The motor used to control the flywheel
     * @param gearRatio The gear ratio
     * @param kv        The velocity gain of the flywheel
     * @param ka        The acceleration gain of the flywheel
     */
    public FlywheelSimulation(DCMotor gearbox, double gearRatio, double kv, double ka) {
        super(gearRatio);
        flywheelSimulation = new FlywheelSim(LinearSystemId.identifyVelocitySystem(kv, ka), gearbox, gearRatio);
    }

    /**
     * Gets the current draw of the flywheel in amperes.
     *
     * @return The current in amperes
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
