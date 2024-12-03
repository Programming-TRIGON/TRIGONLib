package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
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
    protected static final double FIELD_LENGTH_METERS = 16.54175;
    private static final Timer UPDATE_ALLIANCE_TIMER = new Timer();
    private static boolean IS_RED_ALLIANCE = notCachedIsRedAlliance();
    protected final T nonMirroredObject, mirroredObject;

    protected final boolean shouldMirrorWhenRedAlliance;

    /**
     * Initializes the mirrorable class. This should be called once in RobotContainer.
     */
    public static void init() {
        UPDATE_ALLIANCE_TIMER.start();
        new Trigger(() -> UPDATE_ALLIANCE_TIMER.advanceIfElapsed(0.5)).onTrue(getUpdateAllianceCommand());
    }

    /**
     * @return whether the robot is on the red alliance. This is cached every 0.5 seconds
     */
    public static boolean isRedAlliance() {
        return IS_RED_ALLIANCE;
    }

    /**
     * Gets a command that updates the current alliance. This is used to cache the alliance every 0.5 seconds. Ignoring disable is used to update the current alliance when the robot is disabled.
     *
     * @return the command
     */
    private static Command getUpdateAllianceCommand() {
        return new InstantCommand(() -> IS_RED_ALLIANCE = notCachedIsRedAlliance()).ignoringDisable(true);
    }

    /**
     * @return whether the robot is on the red alliance. This is not cached
     */
    private static boolean notCachedIsRedAlliance() {
        final Optional<DriverStation.Alliance> optionalAlliance = DriverStation.getAlliance();
        return optionalAlliance.orElse(DriverStation.Alliance.Red).equals(DriverStation.Alliance.Red);
    }

    /**
     * Creates a new mirrorable object.
     *
     * @param nonMirroredObject           the object when the robot is on the blue alliance, or the non-mirrored object
     * @param shouldMirrorWhenRedAlliance should the object should be mirrored when the robot is on the red alliance
     */
    protected Mirrorable(T nonMirroredObject, boolean shouldMirrorWhenRedAlliance) {
        this.nonMirroredObject = nonMirroredObject;
        this.mirroredObject = mirror(nonMirroredObject);
        this.shouldMirrorWhenRedAlliance = shouldMirrorWhenRedAlliance;
    }

    /**
     * If the robot is on the red alliance and the object should be mirrored, the mirrored object is returned.
     * Otherwise, the non-mirrored object is returned.
     *
     * @return the current object
     */
    public T get() {
        return isRedAlliance() && shouldMirrorWhenRedAlliance ? mirroredObject : nonMirroredObject;
    }

    /**
     * Mirrors the object across the center of the field.
     *
     * @param object the object to mirror
     * @return the mirrored object
     */
    protected abstract T mirror(T object);
}