package org.trigon.utilities.mirrorable;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * A class that represents a {@link Pose2d} that can be mirrored, reversing its position and orientation across the center of the field when the robot is on the red alliance.
 */
public class MirrorablePose2d extends Mirrorable<Pose2d> {
    public MirrorablePose2d(Pose2d nonMirroredPose, boolean shouldMirrorWhenRedAlliance) {
        super(nonMirroredPose, shouldMirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorablePose2d with the given x, y, and rotation.
     *
     * @param x                           the x value of the pose.
     * @param y                           the y value of the pose.
     * @param rotation                    the rotation of the pose.
     * @param shouldMirrorWhenRedAlliance should the pose be mirrored when the robot is on the red alliance
     */
    public MirrorablePose2d(double x, double y, Rotation2d rotation, boolean shouldMirrorWhenRedAlliance) {
        this(new Pose2d(x, y, rotation), shouldMirrorWhenRedAlliance);
    }

    /**
     * Creates a new MirrorablePose2d with the given translation and rotation.
     *
     * @param translation2d               the translation of the pose.
     * @param rotation                    the rotation of the pose.
     * @param shouldMirrorWhenRedAlliance should the pose be mirrored when the robot is on the red alliance
     */
    public MirrorablePose2d(Translation2d translation2d, double rotation, boolean shouldMirrorWhenRedAlliance) {
        this(new Pose2d(translation2d, new Rotation2d(rotation)), shouldMirrorWhenRedAlliance);
    }

    /**
     * Gets the rotation value of the pose. The pose will be mirrored if the robot is on the red alliance and {@link #shouldMirrorWhenRedAlliance} is true.
     *
     * @return the rotation value of the pose.
     */
    public MirrorableRotation2d getRotation() {
        return new MirrorableRotation2d(nonMirroredObject.getRotation(), shouldMirrorWhenRedAlliance);
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