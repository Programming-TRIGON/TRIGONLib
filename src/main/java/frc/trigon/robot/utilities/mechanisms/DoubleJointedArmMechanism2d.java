package frc.trigon.robot.utilities.mechanisms;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color8Bit;
import org.littletonrobotics.junction.Logger;

/**
 * A Mechanism2d object to display the current angle and the target angle of a double-jointed arm.
 */
public class DoubleJointedArmMechanism2d {
    private final String key;
    private final Mechanism2d mechanism;
    private final MechanismLigament2d
            currentPositionFirstLigament,
            currentPositionSecondLigament,
            targetPositionFirstLigament,
            targetPositionSecondLigament;

    /**
     * Constructs a SingleJointedArmMechanism2d object.
     *
     * @param key            the key of the mechanism
     * @param mechanismColor the color of the mechanism
     */
    public DoubleJointedArmMechanism2d(String key, Color8Bit mechanismColor) {
        this(key, MechanismConstants.MECHANISM_LINE_LENGTH, MechanismConstants.MECHANISM_LINE_LENGTH, mechanismColor);
    }

    /**
     * Constructs a SingleJointedArmMechanism2d object.
     *
     * @param name              the name of the mechanism
     * @param firstJointLength  the length of the first joint
     * @param secondJointLength the length of the second joint
     * @param mechanismColor    the color of the mechanism
     */
    public DoubleJointedArmMechanism2d(String name, double firstJointLength, double secondJointLength, Color8Bit mechanismColor) {
        this.key = "Mechanisms/" + name;
        final double mechanismMiddle = MechanismConstants.LIGAMENT_END_TO_EDGE_RATIO * (firstJointLength + secondJointLength);
        this.mechanism = new Mechanism2d(2 * mechanismMiddle, 2 * mechanismMiddle);
        final MechanismRoot2d root = mechanism.getRoot("Root", mechanismMiddle, mechanismMiddle);

        this.currentPositionFirstLigament = root.append(new MechanismLigament2d("ZCurrentPositionFirstLigament", firstJointLength, 0, MechanismConstants.MECHANISM_LINE_WIDTH, mechanismColor));
        this.currentPositionSecondLigament = currentPositionFirstLigament.append(new MechanismLigament2d("ZCurrentPositionSecondLigament", secondJointLength, 0, MechanismConstants.MECHANISM_LINE_WIDTH, mechanismColor));
        this.targetPositionFirstLigament = root.append(new MechanismLigament2d("TargetPositionFirstLigament", firstJointLength, 0, MechanismConstants.MECHANISM_LINE_WIDTH, MechanismConstants.GRAY));
        this.targetPositionSecondLigament = targetPositionFirstLigament.append(new MechanismLigament2d("TargetPositionSecondLigament", secondJointLength, 0, MechanismConstants.MECHANISM_LINE_WIDTH, MechanismConstants.GRAY));
    }

    /**
     * Updates the angle and target angle of both joints, then logs the Mechanism2d object.
     *
     * @param firstJointCurrentAngle  the current angle of the first joint
     * @param firstJointTargetAngle   the target angle of the first joint
     * @param secondJointCurrentAngle the current angle of the second joint
     * @param secondJointTargetAngle  the target angle of the second joint
     */
    public void update(Rotation2d firstJointCurrentAngle, Rotation2d firstJointTargetAngle, Rotation2d secondJointCurrentAngle, Rotation2d secondJointTargetAngle) {
        setTargetAngles(firstJointTargetAngle, secondJointTargetAngle);
        update(firstJointCurrentAngle, secondJointCurrentAngle);
    }

    /**
     * Updates the angle of both joints, then logs the Mechanism2d object.
     *
     * @param firstJointCurrentAngle  the current angle of the first joint
     * @param secondJointCurrentAngle the current angle of the second joint
     */
    public void update(Rotation2d firstJointCurrentAngle, Rotation2d secondJointCurrentAngle) {
        setCurrentAngles(firstJointCurrentAngle, secondJointCurrentAngle);
        update();
    }

    /**
     * Updates the angle and target angle of the first joint, then logs the Mechanism2d object.
     *
     * @param firstJointCurrentAngle the current angle of the first joint
     * @param firstJointTargetAngle  the target angle of the first joint
     */
    public void updateFirstJoint(Rotation2d firstJointCurrentAngle, Rotation2d firstJointTargetAngle) {
        setFirstJointCurrentAngle(firstJointCurrentAngle);
        setFirstJointTargetAngle(firstJointTargetAngle);
        update();
    }

    /**
     * Updates the angle and target angle of the second joint, then logs the Mechanism2d object.
     *
     * @param secondJointCurrentAngle the current angle of the second joint
     * @param secondJointTargetAngle  the target angle of the second joint
     */
    public void updateSecondJoint(Rotation2d secondJointCurrentAngle, Rotation2d secondJointTargetAngle) {
        setSecondJointCurrentAngle(secondJointCurrentAngle);
        setSecondJointTargetAngle(secondJointTargetAngle);
        update();
    }

    /**
     * Logs the Mechanism2d object.
     */
    public void update() {
        Logger.recordOutput(key, mechanism);
    }

    /**
     * Updates the angle of both joints, but doesn't log the Mechanism2d object.
     *
     * @param firstJointCurrentAngle  the current angle of the first joint
     * @param secondJointCurrentAngle the current angle of the second joint
     */
    public void setCurrentAngles(Rotation2d firstJointCurrentAngle, Rotation2d secondJointCurrentAngle) {
        setFirstJointCurrentAngle(firstJointCurrentAngle);
        setSecondJointCurrentAngle(secondJointCurrentAngle);
    }

    /**
     * Sets the target angle of both joints, but doesn't log the mechanism.
     *
     * @param firstJointTargetAngle  the target angle of the first joint
     * @param secondJointTargetAngle the target angle of the second joint
     */
    public void setTargetAngles(Rotation2d firstJointTargetAngle, Rotation2d secondJointTargetAngle) {
        setFirstJointTargetAngle(firstJointTargetAngle);
        setSecondJointTargetAngle(secondJointTargetAngle);
    }

    /**
     * Updates the angle of the first joint, but doesn't log the Mechanism2d object.
     *
     * @param firstJointCurrentAngle the current angle of the first joint
     */
    public void setFirstJointCurrentAngle(Rotation2d firstJointCurrentAngle) {
        currentPositionFirstLigament.setAngle(firstJointCurrentAngle);
    }

    /**
     * Sets the target angle of the first joint.
     *
     * @param firstJointTargetAngle the target angle of the first joint
     */
    public void setFirstJointTargetAngle(Rotation2d firstJointTargetAngle) {
        targetPositionFirstLigament.setAngle(firstJointTargetAngle);
    }

    /**
     * Updates the angle of the second joint but doesn't log the Mechanism2d object.
     *
     * @param secondJointCurrentAngle the current angle of the second joint
     */
    public void setSecondJointCurrentAngle(Rotation2d secondJointCurrentAngle) {
        currentPositionSecondLigament.setAngle(secondJointCurrentAngle);
    }

    /**
     * Sets the target angle of the second joint.
     *
     * @param secondJointTargetAngle the target angle of the second joint
     */
    public void setSecondJointTargetAngle(Rotation2d secondJointTargetAngle) {
        targetPositionSecondLigament.setAngle(secondJointTargetAngle);
    }
}
