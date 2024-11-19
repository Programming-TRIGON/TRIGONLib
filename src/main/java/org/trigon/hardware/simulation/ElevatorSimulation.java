package org.trigon.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.utilities.Conversions;

/**
 * A class that represents a simulation of an elevator mechanism.
 */
public class ElevatorSimulation extends MotorPhysicsSimulation {
    private final ElevatorSim elevatorSimulation;
    private final double retractedHeightMeters;
    private final double diameterMeters;

    /**
     * Creates a new ElevatorSimulation.
     *
     * @param gearbox               The motor used to move the elevator
     * @param gearRatio             The gear ratio
     * @param carriageMassKilograms The mass of the elevator carriage in kilograms
     * @param drumRadiusMeters      The radius of the drum in meters
     * @param retractedHeightMeters The height of the elevator when retracted in meters
     * @param maximumHeightMeters   The maximum height of the elevator in meters
     * @param simulateGravity       Whether to simulate gravity
     */
    public ElevatorSimulation(DCMotor gearbox, double gearRatio, double carriageMassKilograms, double drumRadiusMeters, double retractedHeightMeters, double maximumHeightMeters, boolean simulateGravity) {
        super(gearRatio);
        diameterMeters = drumRadiusMeters + drumRadiusMeters;
        this.retractedHeightMeters = retractedHeightMeters;
        elevatorSimulation = new ElevatorSim(
                gearbox,
                gearRatio,
                carriageMassKilograms,
                drumRadiusMeters,
                retractedHeightMeters,
                maximumHeightMeters,
                simulateGravity,
                retractedHeightMeters
        );
    }

    /**
     * Gets the current draw of the elevator in amperes.
     *
     * @return The current in amperes
     */
    @Override
    public double getCurrent() {
        return elevatorSimulation.getCurrentDrawAmps();
    }

    /**
     * Gets the position of the elevator in meters.
     *
     * @return The position in meters
     */
    @Override
    public double getSystemPositionRotations() {
        return Conversions.distanceToRotations(elevatorSimulation.getPositionMeters() - retractedHeightMeters, diameterMeters);
    }

    /**
     * Gets the velocity of the elevator in meters per second.
     *
     * @return The velocity in meters per second
     */
    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Conversions.distanceToRotations(elevatorSimulation.getVelocityMetersPerSecond(), diameterMeters);
    }

    /**
     * Sets the input voltage of the elevator.
     *
     * @param voltage The voltage to set
     */
    @Override
    public void setInputVoltage(double voltage) {
        elevatorSimulation.setInputVoltage(voltage);
    }

    /**
     * Updates the elevator simulation.
     */
    @Override
    public void updateMotor() {
        elevatorSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
    }
}