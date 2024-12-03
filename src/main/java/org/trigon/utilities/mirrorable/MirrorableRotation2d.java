package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * A class that represents a {@link Rotation2d} that can be mirrored, reversing its orientation across the center of the field when the robot is on the red alliance.
 */
public class MirrorableRotation2d extends Mirrorable<Rotation2d> {
    public MirrorableRotation2d(Rotation2d nonMirroredRotation, boolean shouldMirrorWhenRedAlliance) {
        super(nonMirroredRotation, shouldMirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorableRotation2d with the given rotation value.
     *
     * @param radians                     the value of the angle in radians
     * @param shouldMirrorWhenRedAlliance should the rotation be mirrored when the robot is on the red alliance
     */
    public MirrorableRotation2d(double radians, boolean shouldMirrorWhenRedAlliance) {
        this(new Rotation2d(radians), shouldMirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given degree value.
     *
     * @param degrees                     the value of the angle in degrees
     * @param shouldMirrorWhenRedAlliance should the rotation be mirrored when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromDegrees(double degrees, boolean shouldMirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromDegrees(degrees), shouldMirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given radian value.
     *
     * @param radians                     the value of the angle in radians
     * @param shouldMirrorWhenRedAlliance should the rotation be mirrored when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromRadians(double radians, boolean shouldMirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromRadians(radians), shouldMirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given number of rotations.
     *
     * @param rotations                   the value of the angle in rotations
     * @param shouldMirrorWhenRedAlliance should the rotation be mirrored when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromRotations(double rotations, boolean shouldMirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromRotations(rotations), shouldMirrorWhenRedAlliance);
    }

    @Override
    protected Rotation2d mirror(Rotation2d rotation) {
        return HALF_ROTATION.minus(rotation);
    }
}
