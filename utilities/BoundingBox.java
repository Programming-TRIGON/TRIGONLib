package frc.trigon.lib.utilities;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rectangle2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Distance;
import org.littletonrobotics.junction.Logger;

/**
 * A wrapper class for the WPILib {@link Rectangle2d}.
 */
public class BoundingBox extends Rectangle2d {
    public BoundingBox(Pose2d center, double xWidth, double yWidth) {
        super(center, xWidth, yWidth);
    }

    public BoundingBox(Pose2d center, Distance xWidth, Distance yWidth) {
        super(center, xWidth, yWidth);
    }

    public BoundingBox(Translation2d cornerA, Translation2d cornerB) {
        super(cornerA, cornerB);
    }

    /**
     * Logs this bounding box for visualization in AdvantageScope.
     *
     * @param key the logging key
     */
    public void log(String key) {
        final Translation2d[] corners = getRectangleCorners();
        Logger.recordOutput(key, new Translation2d[]{
                corners[0],
                corners[1],
                corners[3],
                corners[2],
                corners[0]
        });
    }

    /**
     * Checks whether this bounding box overlaps another bounding box.
     *
     * <p>Uses the <a href="https://dyn4j.org/2010/01/sat/">Separating Axis Theorem (SAT)</a>: when both
     * rectangles are projected onto a one dimensional axis, if there exists such an axis in which the rectangles'
     * projections do not intersect they are fully separate.
     * Because rectangles contain two sets of parallel sides, only two axes need to be checked per rectangle.</p>
     *
     * @param other the bounding box to check for overlaps
     * @return whether the bounding boxes overlap
     */
    public boolean overlaps(BoundingBox other) {
        final Rotation2d
                thisRectangleRotation = this.getRotation(),
                otherRectangleRotation = other.getRotation();
        final Translation2d
                thisRectangleCenterPosition = this.getCenter().getTranslation(),
                otherRectangleCenterPosition = other.getCenter().getTranslation();

        final Translation2d centerDistance = otherRectangleCenterPosition.minus(thisRectangleCenterPosition);
        final Translation2d
                firstBoxXAxis = getRectangleXAxis(thisRectangleRotation),
                firstBoxYAxis = getRectangleYAxis(thisRectangleRotation),
                secondBoxXAxis = getRectangleXAxis(otherRectangleRotation),
                secondBoxYAxis = getRectangleYAxis(otherRectangleRotation);

        return !hasGapOnAxis(centerDistance, firstBoxXAxis, other)
                && !hasGapOnAxis(centerDistance, firstBoxYAxis, other)
                && !hasGapOnAxis(centerDistance, secondBoxXAxis, other)
                && !hasGapOnAxis(centerDistance, secondBoxYAxis, other);
    }

    /**
     * Checks whether this bounding box fully contains another bounding box.
     * A bounding box is fully contained if all four of its corners lie within this bounding box.
     *
     * @param other the bounding box to check
     * @return whether this bounding box fully contains the other bounding box
     */
    public boolean contains(BoundingBox other) {
        for (final Translation2d corner : other.getRectangleCorners())
            if (!this.contains(corner))
                return false;
        return true;
    }

    /**
     * Returns the shortest distance between two bounding boxes.
     * Returns 0 if the bounding boxes overlap.
     *
     * @param other the bounding box to measure the distance to
     * @return the shortest distance between the two bounding boxes
     */
    public double distanceTo(BoundingBox other) {
        return this.overlaps(other) ? 0 : getMinimumCornerDistance(other);
    }

    /**
     * Returns the shortest distance from the perimeter of this bounding box to the perimeter of another bounding box.
     * Unlike {@link #distanceTo(BoundingBox)}, this returns a non-zero value even if the bounding boxes overlap.
     *
     * @param other the bounding box to measure the distance to
     * @return the shortest distance from the perimeter of this bounding box to the perimeter of the other bounding box
     */
    public double getMinimumDistanceToPerimeter(BoundingBox other) {
        final Translation2d[] otherCorners = other.getRectangleCorners();
        double minimumDistance = Double.POSITIVE_INFINITY;

        for (Translation2d otherCorner : otherCorners)
            minimumDistance = Math.min(minimumDistance, this.distanceToPerimeter(otherCorner));

        return minimumDistance;
    }

    /**
     * Returns the distance from a point to the nearest point on the perimeter of this bounding box.
     * Unlike {@link #getDistance(Translation2d)}, this returns a non-zero value even if the point is inside.
     *
     * @param point the point to measure the distance from
     * @return the distance from the point to the nearest perimeter point
     */
    public double distanceToPerimeter(Translation2d point) {
        return point.getDistance(nearestPerimeterPoint(point));
    }

    /**
     * Returns the nearest point on the perimeter of this bounding box to the given point.
     * Unlike {@link #nearest(Translation2d)}, this always returns a point on the perimeter,
     * even if the given point is inside the bounding box.
     *
     * @param point the point to find the nearest perimeter point to
     * @return the nearest point on the perimeter of this bounding box
     */
    public Translation2d nearestPerimeterPoint(Translation2d point) {
        final Pose2d center = this.getCenter();
        final Translation2d localPoint = point.minus(center.getTranslation())
                .rotateBy(center.getRotation().unaryMinus());

        final double halfXWidth = this.getXWidth() / 2.0;
        final double halfYWidth = this.getYWidth() / 2.0;

        final boolean isInsideX = Math.abs(localPoint.getX()) < halfXWidth;
        final boolean isInsideY = Math.abs(localPoint.getY()) < halfYWidth;

        final Translation2d localPerimeterPoint = isInsideX && isInsideY
                ? calculateNearestEdgePoint(localPoint, halfXWidth, halfYWidth)
                : new Translation2d(
                MathUtil.clamp(localPoint.getX(), -halfXWidth, halfXWidth),
                MathUtil.clamp(localPoint.getY(), -halfYWidth, halfYWidth)
        );

        return localPerimeterPoint.rotateBy(center.getRotation()).plus(center.getTranslation());
    }

    /**
     * Returns a new bounding box expanded outward on all sides by the given amount.
     * The center and rotation remain unchanged.
     *
     * @param expansionAmount the amount in meters to expand each side outward
     * @return the expanded bounding box
     */
    public BoundingBox expandedBy(double expansionAmount) {
        return new BoundingBox(
                this.getCenter(),
                this.getXWidth() + expansionAmount * 2,
                this.getYWidth() + expansionAmount * 2
        );
    }

    /**
     * Checks whether there is a gap between two rectangles when projected onto an axis.
     * If the combined span of both rectangles along that axis is less than the center distance,
     * there exists a gap, and the rectangles cannot be overlapping.
     *
     * @param centerDistance the vector from the center of the first rectangle to the center of the second
     * @param axis           the axis to check for a gap along
     * @param other          the second bounding box
     * @return whether there is a gap between the rectangles projected on this axis
     */
    private boolean hasGapOnAxis(Translation2d centerDistance, Translation2d axis, BoundingBox other) {
        final double projectedCenterDistance = Math.abs(projectToAxis(centerDistance, axis));
        final double
                thisRectangleHalfProjectedLength = calculateProjectedRectangleSpanOnAxis(this, axis) / 2,
                otherRectangleHalfProjectedLength = calculateProjectedRectangleSpanOnAxis(other, axis) / 2;

        return projectedCenterDistance > thisRectangleHalfProjectedLength + otherRectangleHalfProjectedLength;
    }

    /**
     * Calculates the span of a rectangle when projected onto an axis.
     * This accounts for the rectangle's rotation, so a rotated rectangle may span further
     * than its raw width or height.
     *
     * @param rectangle the rectangle to measure
     * @param axis      the axis to measure the span on
     * @return the span of the rectangle along the given axis
     */
    private double calculateProjectedRectangleSpanOnAxis(BoundingBox rectangle, Translation2d axis) {
        final Translation2d
                rectangleXAxis = getRectangleXAxis(rectangle.getRotation()),
                rectangleYAxis = getRectangleYAxis(rectangle.getRotation());
        final double
                xAxisScalar = Math.abs(projectToAxis(rectangleXAxis, axis)),
                yAxisScalar = Math.abs(projectToAxis(rectangleYAxis, axis));

        return rectangle.getXWidth() * xAxisScalar + rectangle.getYWidth() * yAxisScalar;
    }

    /**
     * Returns the shortest distance between the perimeters of this and another bounding box.
     *
     * @param other the other bounding box
     * @return the shortest distance between the perimeters
     */
    public double getMinimumCornerDistance(BoundingBox other) {
        final Translation2d[]
                thisBoundingBoxCorners = this.getRectangleCorners(),
                otherBoundingBoxCorners = other.getRectangleCorners();

        double minimumDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < thisBoundingBoxCorners.length; i++) {
            minimumDistance = Math.min(minimumDistance, other.getDistance(thisBoundingBoxCorners[i]));
            minimumDistance = Math.min(minimumDistance, this.getDistance(otherBoundingBoxCorners[i]));
        }

        return minimumDistance;
    }

    /**
     * Returns the four corners of this bounding box.
     * Corners are ordered as: front-left, front-right, back-left, back-right,
     * relative to the rectangle's own rotation.
     *
     * @return an array of the four corner positions
     */
    private Translation2d[] getRectangleCorners() {
        final double halfXWidth = this.getXWidth() / 2.0;
        final double halfYWidth = this.getYWidth() / 2.0;
        final Pose2d center = this.getCenter();

        return new Translation2d[]{
                new Translation2d(halfXWidth, halfYWidth).rotateBy(center.getRotation()).plus(center.getTranslation()),
                new Translation2d(halfXWidth, -halfYWidth).rotateBy(center.getRotation()).plus(center.getTranslation()),
                new Translation2d(-halfXWidth, halfYWidth).rotateBy(center.getRotation()).plus(center.getTranslation()),
                new Translation2d(-halfXWidth, -halfYWidth).rotateBy(center.getRotation()).plus(center.getTranslation())
        };
    }

    /**
     * Returns the nearest point on the edge of a rectangle to a point that is inside it.
     * The point and result are both in the rectangle's local coordinate frame.
     *
     * @param localPoint the point in local frame, must be strictly inside the rectangle
     * @param halfXWidth half the rectangle's width along the X axis
     * @param halfYWidth half the rectangle's width along the Y axis
     * @return the nearest edge point in local frame
     */
    private static Translation2d calculateNearestEdgePoint(Translation2d localPoint, double halfXWidth, double halfYWidth) {
        final double
                distanceToLeftEdge = localPoint.getX() + halfXWidth,
                distanceToRightEdge = halfXWidth - localPoint.getX(),
                distanceToBottomEdge = localPoint.getY() + halfYWidth,
                distanceToTopEdge = halfYWidth - localPoint.getY();

        final double minimumEdgeDistance = Math.min(
                Math.min(distanceToLeftEdge, distanceToRightEdge),
                Math.min(distanceToBottomEdge, distanceToTopEdge)
        );

        if (minimumEdgeDistance == distanceToLeftEdge)
            return new Translation2d(-halfXWidth, localPoint.getY());
        if (minimumEdgeDistance == distanceToRightEdge)
            return new Translation2d(halfXWidth, localPoint.getY());
        if (minimumEdgeDistance == distanceToBottomEdge)
            return new Translation2d(localPoint.getX(), -halfYWidth);
        return new Translation2d(localPoint.getX(), halfYWidth);
    }

    /**
     * Measures how much of a vector lies along a given axis.
     *
     * @param vector the vector to measure
     * @param axis   the axis to measure along
     * @return the length of the component of the vector that lies along the given axis
     */
    private static double projectToAxis(Translation2d vector, Translation2d axis) {
        return vector.getX() * axis.getX() + vector.getY() * axis.getY();
    }

    /**
     * Returns a unit vector along the rectangle's X axis with the given rotation.
     *
     * @param rotation the rotation of the rectangle
     * @return a unit vector along the X axis of a rectangle with the given rotation
     */
    private static Translation2d getRectangleXAxis(Rotation2d rotation) {
        return new Translation2d(rotation.getCos(), rotation.getSin());
    }

    /**
     * Returns a unit vector along the rectangle's Y axis with the given rotation.
     * This direction is perpendicular to the X axis.
     *
     * @param rotation the rotation of the rectangle
     * @return a unit vector along the Y axis of a rectangle with the given rotation
     */
    private static Translation2d getRectangleYAxis(Rotation2d rotation) {
        return new Translation2d(-rotation.getSin(), rotation.getCos());
    }
}