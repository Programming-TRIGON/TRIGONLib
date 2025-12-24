package frc.trigon.lib.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.utilities.Conversions;

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
     * @param gearbox               the motor(s) used to move the elevator
     * @param gearRatio             the gearbox's gear ratio
     * @param carriageMassKilograms the mass of the elevator carriage in kilograms
     * @param drumRadiusMeters      the radius of the drum in meters
     * @param retractedHeightMeters the height of the elevator when retracted in meters
     * @param maximumHeightMeters   the maximum height of the elevator in meters
     * @param simulateGravity       a boolean indicating whether to simulate gravity or not
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
     * @return the current in amperes
     */
    @Override
    public double getCurrent() {
        return elevatorSimulation.getCurrentDrawAmps();
    }

    /**
     * @return the position in meters
     */
    @Override
    public double getSystemPositionRotations() {
        return Conversions.distanceToRotations(elevatorSimulation.getPositionMeters() - retractedHeightMeters, diameterMeters);
    }

    /**
     * @return the velocity in meters per second
     */
    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Conversions.distanceToRotations(elevatorSimulation.getVelocityMetersPerSecond(), diameterMeters);
    }

    /**
     * Sets the input voltage of the elevator.
     *
     * @param voltage the voltage to set
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