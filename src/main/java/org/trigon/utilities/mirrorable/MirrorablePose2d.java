package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * A class that represents a {@link Pose2d} that can be mirrored.
 */
public class MirrorablePose2d extends Mirrorable<Pose2d> {
    public MirrorablePose2d(Pose2d nonMirroredPose, boolean mirrorWhenRedAlliance) {
        super(nonMirroredPose, mirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorablePose2d with the given x, y, and rotation.
     *
     * @param x                     The x value of the pose.
     * @param y                     The y value of the pose.
     * @param rotation              The rotation of the pose.
     * @param mirrorWhenRedAlliance Whether to mirror the pose when the robot is on the red alliance.
     */
    public MirrorablePose2d(double x, double y, Rotation2d rotation, boolean mirrorWhenRedAlliance) {
        this(new Pose2d(x, y, rotation), mirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorablePose2d with the given translation and rotation.
     *
     * @param translation2d         The translation of the pose.
     * @param rotation              The rotation of the pose.
     * @param mirrorWhenRedAlliance Whether to mirror the pose when the robot is on the red alliance.
     */
    public MirrorablePose2d(Translation2d translation2d, double rotation, boolean mirrorWhenRedAlliance) {
        this(new Pose2d(translation2d, new Rotation2d(rotation)), mirrorWhenRedAlliance);
    }

    /**
     * Gets the rotation value of the pose.
     *
     * @return the rotation value of the pose.
     */
    public MirrorableRotation2d getRotation() {
        return new MirrorableRotation2d(nonMirroredObject.getRotation(), mirrorWhenRedAlliance);
    }

    @Override
    protected Pose2d mirror(Pose2d pose) {
        return new Pose2d(
                FIELD_LENGTH_METERS - pose.getX(),
                pose.getY(),
                HALF_ROTATION.minus(pose.getRotation())
        );
    }
}
