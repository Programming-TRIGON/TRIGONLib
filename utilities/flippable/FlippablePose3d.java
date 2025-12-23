package frc.trigon.lib.utilities.flippable;

import com.pathplanner.lib.util.FlippingUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

/**
 * A class that represents a {@link Pose3d} that can be flipped when the robot is on the red alliance.
 */
public class FlippablePose3d extends Flippable<Pose3d> {
    /**
     * Creates a new FlippablePose3d with the given x, y, z and rotation.
     *
     * @param x                         the x value of the pose
     * @param y                         the y value of the pose
     * @param z                         the z value of the pose
     * @param rotation                  the rotation of the pose
     * @param shouldFlipWhenRedAlliance should the pose be flipped when the robot is on the red alliance
     */
    public FlippablePose3d(double x, double y, double z, Rotation3d rotation, boolean shouldFlipWhenRedAlliance) {
        this(new Pose3d(x, y, z, rotation), shouldFlipWhenRedAlliance);
    }

    /**
     * Creates a new FlippablePose3d with the given translation and rotation.
     *
     * @param translation2d             the translation of the pose
     * @param rotation                  the rotation of the pose
     * @param shouldFlipWhenRedAlliance should the pose be flipped when the robot is on the red alliance
     */
    public FlippablePose3d(Translation3d translation2d, Rotation3d rotation, boolean shouldFlipWhenRedAlliance) {
        this(new Pose3d(translation2d, rotation), shouldFlipWhenRedAlliance);
    }

    /**
     * Creates a new FlippablePose3d with the given pose.
     *
     * @param nonFlippedPose            the pose when the robot is on the blue alliance
     * @param shouldFlipWhenRedAlliance should the pose be flipped when the robot is on the red alliance
     */
    public FlippablePose3d(Pose3d nonFlippedPose, boolean shouldFlipWhenRedAlliance) {
        super(nonFlippedPose, shouldFlipWhenRedAlliance);
    }

    @Override
    protected Pose3d flip(Pose3d pose) {
        final Pose2d flippedPose2d = FlippingUtil.flipFieldPose(pose.toPose2d());
        return new Pose3d(
                flippedPose2d.getTranslation().getX(),
                flippedPose2d.getTranslation().getY(),
                pose.getZ(),
                new Rotation3d(
                        pose.getRotation().getX(),
                        pose.getRotation().getY(),
                        flippedPose2d.getRotation().getRadians()
                )
        );
    }
}