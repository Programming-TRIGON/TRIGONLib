package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Translation3d;


public class MirrorableTranslation3d extends Mirrorable<Translation3d> {
    public MirrorableTranslation3d(Translation3d nonMirroredTranslation, boolean mirrorWhenRedAlliance) {
        super(nonMirroredTranslation, mirrorWhenRedAlliance);
    }

    public MirrorableTranslation3d(double x, double y, double z, boolean mirrorWhenRedAlliance) {
        this(new Translation3d(x, y, z), mirrorWhenRedAlliance);
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
