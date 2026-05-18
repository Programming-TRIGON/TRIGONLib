package frc.trigon.lib.utilities.flippable;

import com.pathplanner.lib.util.FlippingUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rectangle2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * A class that represents a {@link Rectangle2d} who's center pose can be flipped when the robot is on the red alliance.
 */
public class FlippableRectangle2d extends Flippable<Rectangle2d> {
    /**
     * Creates a new FlippableRectangle2d with the given pose, x width, and y width.
     *
     * @param pose                      the {@link Pose2d} of the center of the rectangle
     * @param xWidth                    the width of the rectangle in the x direction
     * @param yWidth                    the width of the rectangle in the y direction
     * @param shouldFlipWhenRedAlliance should the center pose be flipped when the robot is on the red alliance
     */
    public FlippableRectangle2d(Pose2d pose, double xWidth, double yWidth, boolean shouldFlipWhenRedAlliance) {
        this(new Rectangle2d(pose, xWidth, yWidth), shouldFlipWhenRedAlliance);
    }

    /**
     * Creates a new FlippableRectangle2d from the given corners.
     *
     * @param cornerA                   the position of one corner of the rectangle
     * @param cornerB                   the position of the opposite corner of the rectangle
     * @param shouldFlipWhenRedAlliance should the center pose be flipped when the robot is on the red alliance
     */
    public FlippableRectangle2d(Translation2d cornerA, Translation2d cornerB, boolean shouldFlipWhenRedAlliance) {
        this(new Rectangle2d(cornerA, cornerB), shouldFlipWhenRedAlliance);
    }

    /**
     * Creates a new FlippableRectangle2d from a {@link Rectangle2d}.
     *
     * @param nonFlippedRectangle       the rectangle when the robot is on the blue alliance
     * @param shouldFlipWhenRedAlliance should the center pose be flipped when the robot is on the red alliance
     */
    public FlippableRectangle2d(Rectangle2d nonFlippedRectangle, boolean shouldFlipWhenRedAlliance) {
        super(nonFlippedRectangle, shouldFlipWhenRedAlliance);
    }

    /**
     * Gets the center pose of the rectangle. The pose will be flipped if the robot is on the red alliance and {@link #shouldFlipWhenRedAlliance} is true.
     *
     * @return the center pose of the rectangle
     */
    public FlippablePose2d getCenterPose() {
        return new FlippablePose2d(nonFlippedObject.getCenter(), shouldFlipWhenRedAlliance);
    }

    /**
     * Gets the rotation value of the pose. The pose will be flipped if the robot is on the red alliance and {@link #shouldFlipWhenRedAlliance} is true.
     *
     * @return the rotation value of the pose
     */
    public FlippableRotation2d getRotation() {
        return new FlippableRotation2d(nonFlippedObject.getRotation(), shouldFlipWhenRedAlliance);
    }

    /**
     * Gets the width of the rectangle in the x direction. The width will not be flipped because flipping only affects the pose, not the dimensions of the rectangle.
     *
     * @return the width of the rectangle in the x direction
     */
    public double getXWidth() {
        return nonFlippedObject.getXWidth();
    }

    /**
     * Gets the width of the rectangle in the y direction. The width will not be flipped because flipping only affects the pose, not the dimensions of the rectangle.
     *
     * @return the width of the rectangle in the y direction
     */
    public double getYWidth() {
        return nonFlippedObject.getYWidth();
    }

    @Override
    protected Rectangle2d flip(Rectangle2d rectangle) {
        return new Rectangle2d(
                FlippingUtil.flipFieldPose(rectangle.getCenter()),
                rectangle.getXWidth(),
                rectangle.getYWidth()
        );
    }
}