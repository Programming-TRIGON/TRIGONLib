package frc.trigon.robot.utilities.mechanisms;

import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;
import org.littletonrobotics.junction.Logger;

/**
 * A Mechanism2d object to display the current position and target position of an elevator.
 */
public class ElevatorMechanism2d {
    private final String key;
    private final Mechanism2d mechanism;
    private final MechanismLigament2d
            currentPositionLigament,
            targetPositionLigament;
    private final double minimumLength;

    /**
     * Constructs an ElevatorMechanism2d object.
     *
     * @param name           the name of the mechanism
     * @param maximumLength  the maximum length of the elevator
     * @param minimumLength  the minimum length of the elevator
     * @param mechanismColor the color of the mechanism
     */
    public ElevatorMechanism2d(String name, double maximumLength, double minimumLength, Color8Bit mechanismColor) {
        this.key = "Mechanisms/" + name;
        this.minimumLength = minimumLength;
        this.mechanism = new Mechanism2d(maximumLength, maximumLength);

        final MechanismRoot2d currentPositionRoot = mechanism.getRoot("Root", 0.5 * maximumLength, 0);
        this.currentPositionLigament = currentPositionRoot.append(new MechanismLigament2d("ZCurrentPositionLigament", minimumLength, MechanismConstants.ELEVATOR_MECHANISM_STARTING_ANGLE, MechanismConstants.MECHANISM_LINE_WIDTH, mechanismColor));
        this.targetPositionLigament = currentPositionRoot.append(new MechanismLigament2d("TargetPositionLigament", maximumLength, MechanismConstants.ELEVATOR_MECHANISM_STARTING_ANGLE, MechanismConstants.TARGET_ELEVATOR_POSITION_LIGAMENT_WIDTH, MechanismConstants.GRAY));
    }

    /**
     * Updates the mechanism's position and target position, then logs the Mechanism2d object.
     *
     * @param currentPosition the current position
     * @param targetPosition  the target position
     */
    public void update(double currentPosition, double targetPosition) {
        setTargetPosition(targetPosition);
        update(currentPosition);
    }

    /**
     * Updates the mechanism's position, then logs the Mechanism2d object.
     *
     * @param currentPosition the current position
     */
    public void update(double currentPosition) {
        setCurrentPosition(currentPosition);
        update();
    }

    /**
     * Logs the Mechanism2d object.
     */
    public void update() {
        Logger.recordOutput(key, mechanism);
    }

    /**
     * Sets the current position of the mechanism but doesn't log the Mechanism2d object.
     *
     * @param currentPosition the current position
     */
    public void setCurrentPosition(double currentPosition) {
        currentPositionLigament.setLength(currentPosition + minimumLength);
    }

    /**
     * Sets the target position of the mechanism but doesn't log the Mechanism2d object.
     *
     * @param targetPosition the target position
     */
    public void setTargetPosition(double targetPosition) {
        targetPositionLigament.setLength(targetPosition + minimumLength);
    }
}