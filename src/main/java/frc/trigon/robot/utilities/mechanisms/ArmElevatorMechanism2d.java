package frc.trigon.robot.utilities.mechanisms;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;
import org.littletonrobotics.junction.Logger;

/**
 * A Mechanism2d object to display the current angle and target angle, and the current position and target position of an arm elevator.
 */
public class ArmElevatorMechanism2d {
    private final String key;
    private final Mechanism2d mechanism;
    private final MechanismLigament2d
            currentPositionLigament,
            targetPositionLigament;
    private final double minimumLength;

    /**
     * Constructs an ArmElevatorMechanism2d object.
     *
     * @param name           the name of the mechanism
     * @param maximumLength  the maximum length of the elevator
     * @param minimumLength  the minimum length of the elevator
     * @param mechanismColor the color of the mechanism
     */
    public ArmElevatorMechanism2d(String name, double maximumLength, double minimumLength, Color8Bit mechanismColor) {
        this.key = "Mechanisms/" + name;
        this.minimumLength = minimumLength;
        this.mechanism = new Mechanism2d(2 * maximumLength, 2 * maximumLength);
        final MechanismRoot2d root = mechanism.getRoot("Root", maximumLength, maximumLength);
        this.currentPositionLigament = root.append(new MechanismLigament2d("ZCurrentPositionLigament", 0, 0, MechanismConstants.MECHANISM_LINE_WIDTH, mechanismColor));
        this.targetPositionLigament = root.append(new MechanismLigament2d("TargetPositionLigament", 0, 0, MechanismConstants.TARGET_ELEVATOR_POSITION_LIGAMENT_WIDTH, MechanismConstants.GRAY));
    }

    /**
     * Updates the mechanism's position and target position as well as the mechanism's angle and target angle, then logs the Mechanism2d object.
     *
     * @param currentPosition the current position of the elevator
     * @param targetPosition  the target position of the elevator
     * @param currentAngle    the current angle of the arm
     * @param targetAngle     the target angle of the arm
     */
    public void updateMechanism(double currentPosition, double targetPosition, Rotation2d currentAngle, Rotation2d targetAngle) {
        setTargetPosition(targetPosition);
        setTargetAngle(targetAngle);
        updateMechanism(currentPosition, currentAngle);
    }

    /**
     * updates the mechanism's position and angle, then logs the Mechanism2d object.
     *
     * @param currentPosition the current position of the elevator
     * @param currentAngle    the current angle of the arm
     */
    public void updateMechanism(double currentPosition, Rotation2d currentAngle) {
        setCurrentPosition(currentPosition);
        setCurrentAngle(currentAngle);
        update();
    }

    /**
     * Updates the mechanism's position then logs the Mechanism2d object.
     *
     * @param currentPosition the current position of the elevator
     */
    public void updateCurrentPosition(double currentPosition) {
        setCurrentPosition(currentPosition);
        update();
    }

    /**
     * Updates the mechanism's angle then logs the Mechanism2d object.
     *
     * @param currentAngle the current angle of the arm
     */
    public void updateCurrentAngle(Rotation2d currentAngle) {
        setCurrentAngle(currentAngle);
        update();
    }

    /**
     * Logs the Mechanism2d object.
     */
    public void update() {
        Logger.recordOutput(key, mechanism);
    }

    /**
     * Updates the mechanism's position but doesn't log the Mechanism2d object.
     *
     * @param currentPosition the current position of the elevator
     */
    public void setCurrentPosition(double currentPosition) {
        currentPositionLigament.setLength(currentPosition + minimumLength);
    }

    /**
     * Updates the mechanism's angle but doesn't log the Mechanism2d object.
     *
     * @param currentAngle the current angle of the arm
     */
    public void setCurrentAngle(Rotation2d currentAngle) {
        currentPositionLigament.setAngle(currentAngle);
    }

    /**
     * Sets the target position of the mechanism but doesn't log the Mechanism2d object.
     *
     * @param targetPosition the target position of the elevator
     */
    public void setTargetPosition(double targetPosition) {
        targetPositionLigament.setLength(targetPosition + minimumLength);
    }

    /**
     * Sets the target angle of the mechanism but doesn't log the Mechanism2d object.
     *
     * @param targetAngle the target angle of the arm
     */
    public void setTargetAngle(Rotation2d targetAngle) {
        targetPositionLigament.setAngle(targetAngle);
    }
}
