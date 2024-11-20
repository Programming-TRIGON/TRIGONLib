package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * A class that represents a {@link Rotation2d} that can be mirrored. Reversing its orientation.
 */
public class MirrorableRotation2d extends Mirrorable<Rotation2d> {
    public MirrorableRotation2d(Rotation2d nonMirroredRotation, boolean mirrorWhenRedAlliance) {
        super(nonMirroredRotation, mirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorableRotation2d with the given rotation value.
     *
     * @param radians               the value of the angle in radians
     * @param mirrorWhenRedAlliance a boolean indicating whether to mirror the object when the robot is on the red alliance
     */
    public MirrorableRotation2d(double radians, boolean mirrorWhenRedAlliance) {
        this(new Rotation2d(radians), mirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given degree value.
     *
     * @param degrees               the value of the angle in degrees
     * @param mirrorWhenRedAlliance a boolean indicating whether to mirror the object when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromDegrees(double degrees, boolean mirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromDegrees(degrees), mirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given radian value.
     *
     * @param radians               the value of the angle in radians
     * @param mirrorWhenRedAlliance a boolean indicating whether to mirror the object when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromRadians(double radians, boolean mirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromRadians(radians), mirrorWhenRedAlliance);
    }

    /**
     * Constructs and returns a MirrorableRotation2d with the given number of rotations.
     *
     * @param rotations             the value of the angle in rotations
     * @param mirrorWhenRedAlliance a boolean indicating whether to mirror the object when the robot is on the red alliance
     * @return the rotation object with the desired angle value
     */
    public static MirrorableRotation2d fromRotations(double rotations, boolean mirrorWhenRedAlliance) {
        return new MirrorableRotation2d(Rotation2d.fromRotations(rotations), mirrorWhenRedAlliance);
    }

    @Override
    protected Rotation2d mirror(Rotation2d rotation) {
        return HALF_ROTATION.minus(rotation);
    }
}
