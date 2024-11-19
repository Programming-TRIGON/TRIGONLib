package org.trigon.hardware.simulation;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import org.trigon.hardware.RobotHardwareStats;

/**
 * A class that represents a simulation of a single jointed arm mechanism.
 */
public class SingleJointedArmSimulation extends MotorPhysicsSimulation {
    private final SingleJointedArmSim armSimulation;

    /**
     * Creates a new SingleJointedArmSimulation.
     *
     * @param gearbox          The motor used to control the arm
     * @param gearRatio        The gear ratio
     * @param armLengthMeters  The length of the arm in meters
     * @param armMassKilograms The mass of the arm in kilograms
     * @param minimumAngle     The minimum angle of the arm
     * @param maximumAngle     The maximum angle of the arm
     * @param simulateGravity  Whether to simulate gravity
     */
    public SingleJointedArmSimulation(DCMotor gearbox, double gearRatio, double armLengthMeters, double armMassKilograms, Rotation2d minimumAngle, Rotation2d maximumAngle, boolean simulateGravity) {
        super(gearRatio);
        armSimulation = new SingleJointedArmSim(
                gearbox,
                gearRatio,
                SingleJointedArmSim.estimateMOI(armLengthMeters, armMassKilograms),
                armLengthMeters,
                minimumAngle.getRadians(),
                maximumAngle.getRadians(),
                simulateGravity,
                minimumAngle.getRadians()
        );
    }

    /**
     * Gets the current draw of the arm in amperes.
     *
     * @return The current in amperes
     */
    @Override
    public double getCurrent() {
        return armSimulation.getCurrentDrawAmps();
    }

    /**
     * Gets the position of the arm in rotations.
     *
     * @return The position in rotations
     */
    @Override
    public double getSystemPositionRotations() {
        return Units.radiansToRotations(armSimulation.getAngleRads());
    }

    /**
     * Gets the velocity of the arm in rotations per second.
     *
     * @return The velocity in rotations per second
     */
    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Units.radiansToRotations(armSimulation.getVelocityRadPerSec());
    }

    /**
     * Sets the input voltage of the arm.
     *
     * @param voltage The voltage to set
     */
    @Override
    public void setInputVoltage(double voltage) {
        armSimulation.setInputVoltage(voltage);
    }

    /**
     * Updates the arm simulation.
     */
    @Override
    public void updateMotor() {
        armSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
    }
}