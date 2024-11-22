package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Translation3d;

/**
 * A class that represents a {@link Translation3d} that can be mirrored reversing its position across the center of the field when the robot is on the red alliance.
 */
public class MirrorableTranslation3d extends Mirrorable<Translation3d> {
    /**
     * Creates a new MirrorableTranslation3d with the given translation.
     *
     * @param nonMirroredTranslation      the translation to mirror
     * @param shouldMirrorWhenRedAlliance should the position be mirrored when the robot is on the red alliance
     */
    public MirrorableTranslation3d(Translation3d nonMirroredTranslation, boolean shouldMirrorWhenRedAlliance) {
        super(nonMirroredTranslation, shouldMirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorableTranslation3d with the given x, y, and z values.
     *
     * @param x                           the x value of the translation
     * @param y                           the y value of the translation
     * @param z                           the z value of the translation
     * @param shouldMirrorWhenRedAlliance should the position be mirrored when the robot is on the red alliance
     */
    public MirrorableTranslation3d(double x, double y, double z, boolean shouldMirrorWhenRedAlliance) {
        this(new Translation3d(x, y, z), shouldMirrorWhenRedAlliance);
    }

    @Override
    protected Translation3d mirror(Translation3d translation) {
        return new Translation3d(
                FIELD_LENGTH_METERS - translation.getX(),
                translation.getY(),
                translation.getZ()
        );
    }
}