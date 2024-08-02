package frc.trigon.robot.utilities.mirrorable;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.Optional;

/**
 * A class that allows for objects to be mirrored across the center of the field when the robot is on the red alliance.
 * This is useful for placing field elements and other objects that are mirrored across the field, or for mirroring the target heading to face a field element.
 *
 * @param <T> the type of object to mirror
 */
public abstract class Mirrorable<T> {
    protected static final Rotation2d HALF_ROTATION = new Rotation2d(Math.PI);

    private static final Timer UPDATE_ALLIANCE_TIMER = new Timer();
    private static boolean IS_RED_ALLIANCE = isRedAlliance();

    protected final T nonMirroredObject, mirroredObject;
    protected final boolean mirrorWhenRedAlliance;

    static {
        UPDATE_ALLIANCE_TIMER.start();

        new Trigger(() -> UPDATE_ALLIANCE_TIMER.advanceIfElapsed(0.5)).onTrue(
                new InstantCommand(() -> IS_RED_ALLIANCE = notCachedIsRedAlliance())
        );
    }

    /**
     * Creates a new mirrorable object.
     *
     * @param nonMirroredObject     the object when the robot is on the blue alliance, or the non-mirrored object
     * @param mirrorWhenRedAlliance whether to mirror the object when the robot is on the red alliance
     */
    protected Mirrorable(T nonMirroredObject, boolean mirrorWhenRedAlliance) {
        this.nonMirroredObject = nonMirroredObject;
        this.mirroredObject = mirror(nonMirroredObject);
        this.mirrorWhenRedAlliance = mirrorWhenRedAlliance;
    }

    /**
     * @return whether the robot is on the red alliance. This is cached every 0.5 seconds
     */
    public static boolean isRedAlliance() {
        return IS_RED_ALLIANCE;
    }

    /**
     * @return whether the robot is on the red alliance. This is not cached
     */
    private static boolean notCachedIsRedAlliance() {
        final Optional<DriverStation.Alliance> optionalAlliance = DriverStation.getAlliance();
        return optionalAlliance.orElse(DriverStation.Alliance.Red).equals(DriverStation.Alliance.Red);
    }

    /**
     * @return the current object.
     * If the robot is on the red alliance and the object should be mirrored, the mirrored object is returned.
     * Otherwise, the non-mirrored object is returned.
     */
    public T get() {
        return isRedAlliance() && mirrorWhenRedAlliance ? mirroredObject : nonMirroredObject;
    }

    /**
     * Mirrors the object across the center of the field.
     *
     * @param object the object to mirror
     * @return the mirrored object
     */
    protected abstract T mirror(T object);
}
