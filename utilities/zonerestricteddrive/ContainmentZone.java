package frc.trigon.lib.utilities.zonerestricteddrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.trigon.lib.utilities.BoundingBox;

/**
 * A class that represents a zone that the robot is not allowed to leave.
 * Movement toward the zone boundary is slowed and eventually blocked as the robot approaches the edge.
 */
public class ContainmentZone implements ZoneRestriction {
    private final BoundingBox boundingBox;
    private final double minimumDistanceMeters, brakingZoneDistanceMeters;

    /**
     * Constructs a new ContainmentZone object.
     *
     * @param boundingBox               the bounding box of the containment zone
     * @param minimumDistanceMeters     the distance from the zone boundary that the robot cannot cross
     * @param brakingZoneDistanceMeters the distance from the zone boundary at which braking begins
     */
    public ContainmentZone(BoundingBox boundingBox, double minimumDistanceMeters, double brakingZoneDistanceMeters) {
        this.boundingBox = boundingBox;
        this.minimumDistanceMeters = minimumDistanceMeters;
        this.brakingZoneDistanceMeters = brakingZoneDistanceMeters;

        if (minimumDistanceMeters < 0 || brakingZoneDistanceMeters < 0)
            DriverStation.reportWarning("ContainmentZone distances must be non-negative", false);
        if (minimumDistanceMeters > brakingZoneDistanceMeters)
            DriverStation.reportWarning("ContainmentZone minimum distance cannot be greater than braking zone distance", false);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public double getMinimumDistanceMeters() {
        return minimumDistanceMeters;
    }

    @Override
    public double getBrakingZoneDistanceMeters() {
        return brakingZoneDistanceMeters;
    }

    @Override
    public Translation2d applyRestriction(Translation2d targetTranslation, BoundingBox robotBoundingBox) {
        if (!isWithinBrakingZone(robotBoundingBox))
            return targetTranslation;
        return calculateRestrictedTranslation(targetTranslation, robotBoundingBox);
    }

    /**
     * Returns whether the robot is close enough to the boundary for braking to apply.
     *
     * @param robotBoundingBox the robot's current bounding box
     * @return whether the robot is within the braking zone
     */
    private boolean isWithinBrakingZone(BoundingBox robotBoundingBox) {
        return calculateDistanceToBoundary(robotBoundingBox) <= brakingZoneDistanceMeters;
    }

    /**
     * Scales down the components of the translation that point toward the zone boundaries.
     *
     * @param targetTranslation the target translation to restrict
     * @param robotBoundingBox  the robot's current bounding box
     * @return the restricted translation
     */
    private Translation2d calculateRestrictedTranslation(Translation2d targetTranslation, BoundingBox robotBoundingBox) {
        final Pose2d zoneCenter = boundingBox.getCenter();
        final Translation2d
                zoneRelativeTargetTranslation = toZoneRelativeDirection(targetTranslation, zoneCenter),
                zoneRelativeRobotCenter = toZoneRelativePosition(robotBoundingBox.getCenter().getTranslation(), zoneCenter);

        final Translation2d zoneRelativeRestrictedTranslation = applyZoneRelativeAxisBraking(zoneRelativeTargetTranslation, zoneRelativeRobotCenter, robotBoundingBox, zoneCenter);
        return fromZoneRelativeDirection(zoneRelativeRestrictedTranslation, zoneCenter);
    }

    /**
     * Calculates the shortest distance from the robot's bounding box to the nearest point on the containment zone's perimeter.
     * Returns 0 if the robot is not fully contained within the zone.
     *
     * @param robotBoundingBox the robot's current bounding box
     * @return the shortest distance to the nearest point on the perimeter of the containment zone
     */
    private double calculateDistanceToBoundary(BoundingBox robotBoundingBox) {
        if (!boundingBox.contains(robotBoundingBox))
            return 0;
        return boundingBox.getMinimumDistanceToPerimeter(robotBoundingBox);
    }

    /**
     * Applies braking independently to each axis of the given zone-relative translation.
     *
     * @param zoneRelativeTargetTranslation   the target robot translation in the zone's coordinate frame
     * @param zoneRelativeRobotCenterPosition the robot's center position in the zone's coordinate frame
     * @param robotBoundingBox                the robot's bounding box
     * @param zoneCenter                      the center pose of the containment zone
     * @return the braked translation in the zone's coordinate frame
     */
    private Translation2d applyZoneRelativeAxisBraking(Translation2d zoneRelativeTargetTranslation, Translation2d zoneRelativeRobotCenterPosition, BoundingBox robotBoundingBox, Pose2d zoneCenter) {
        final double
                restrictedXVelocity = applyXAxisBraking(zoneRelativeTargetTranslation.getX(), zoneRelativeRobotCenterPosition.getX(), robotBoundingBox, zoneCenter),
                restrictedYVelocity = applyYAxisBraking(zoneRelativeTargetTranslation.getY(), zoneRelativeRobotCenterPosition.getY(), robotBoundingBox, zoneCenter);
        return new Translation2d(restrictedXVelocity, restrictedYVelocity);
    }

    /**
     * Applies braking to the X velocity component based on the robot's proximity to the zone's X walls.
     *
     * @param xVelocity                        the X velocity component in the zone's coordinate frame
     * @param zoneRelativeRobotCenterPositionX the robot's center X coordinate in the zone's coordinate frame
     * @param robotBoundingBox                 the robot's bounding box
     * @param zoneCenter                       the center pose of the containment zone
     * @return the braked X velocity component
     */
    private double applyXAxisBraking(double xVelocity, double zoneRelativeRobotCenterPositionX, BoundingBox robotBoundingBox, Pose2d zoneCenter) {
        final double
                zoneHalfXWidth = boundingBox.getXWidth() / 2,
                robotHalfXProjection = calculateRobotXProjection(robotBoundingBox, zoneCenter) / 2;
        return applyAxisBraking(xVelocity, zoneRelativeRobotCenterPositionX, zoneHalfXWidth, robotHalfXProjection);
    }

    /**
     * Applies braking to the Y velocity component based on the robot's proximity to the zone's Y walls.
     *
     * @param yVelocity                        the Y velocity component in the zone's coordinate frame
     * @param zoneRelativeRobotCenterPositionY the robot's center Y coordinate in the zone's coordinate frame
     * @param robotBoundingBox                 the robot's bounding box
     * @param zoneCenter                       the center pose of the containment zone
     * @return the braked Y velocity component
     */
    private double applyYAxisBraking(double yVelocity, double zoneRelativeRobotCenterPositionY, BoundingBox robotBoundingBox, Pose2d zoneCenter) {
        final double
                zoneHalfYWidth = boundingBox.getYWidth() / 2,
                robotHalfYProjection = calculateRobotYProjection(robotBoundingBox, zoneCenter) / 2;
        return applyAxisBraking(yVelocity, zoneRelativeRobotCenterPositionY, zoneHalfYWidth, robotHalfYProjection);
    }

    /**
     * Applies braking to a single velocity component based on the robot's position along an axis.
     * Only restricts movement toward a wall that is within the braking zone.
     *
     * @param velocity                        the velocity component to restrict
     * @param zoneRelativeRobotCenterPosition the robot's center position along the axis in the zone's coordinate frame
     * @param zoneHalfWidth                   the half-width of the zone along the axis
     * @param robotHalfProjection             the robot's projected half-length along the axis
     * @return the braked velocity component
     */
    private double applyAxisBraking(double velocity, double zoneRelativeRobotCenterPosition, double zoneHalfWidth, double robotHalfProjection) {
        final double
                distanceToUpperWallMeters = calculateDistanceToUpperWall(zoneRelativeRobotCenterPosition, zoneHalfWidth, robotHalfProjection),
                distanceToLowerWallMeters = calculateDistanceToLowerWall(zoneRelativeRobotCenterPosition, zoneHalfWidth, robotHalfProjection);

        if (velocity > 0 && distanceToUpperWallMeters < brakingZoneDistanceMeters)
            return velocity * calculateBrakingScale(distanceToUpperWallMeters);
        if (velocity < 0 && distanceToLowerWallMeters < brakingZoneDistanceMeters)
            return velocity * calculateBrakingScale(distanceToLowerWallMeters);
        return velocity;
    }

    /**
     * Calculates the robot's projected length along the zone's X axis,
     * accounting for the relative rotation between the robot and the zone.
     *
     * @param robotBoundingBox the robot's bounding box
     * @param zoneCenter       the center pose of the containment zone
     * @return the robot's projected length along the zone's X axis
     */
    private static double calculateRobotXProjection(BoundingBox robotBoundingBox, Pose2d zoneCenter) {
        return calculateRobotProjectionAlongAxis(robotBoundingBox, zoneCenter, true);
    }

    /**
     * Returns the robot's projected length along the zone's Y axis,
     * accounting for the relative rotation between the robot and the zone.
     *
     * @param robotBoundingBox the robot's bounding box
     * @param zoneCenter       the center pose of the containment zone
     * @return the robot's projected length along the zone's Y axis
     */
    private static double calculateRobotYProjection(BoundingBox robotBoundingBox, Pose2d zoneCenter) {
        return calculateRobotProjectionAlongAxis(robotBoundingBox, zoneCenter, false);
    }

    /**
     * Returns the robot's projected length along a given zone axis,
     * accounting for the relative rotation between the robot and the zone.
     *
     * @param robotBoundingBox the robot's bounding box
     * @param zoneCenter       the center pose of the containment zone
     * @param isXAxis          whether to project along the zone's X axis (true) or Y axis (false)
     * @return the robot's projected length along the specified axis of the zone
     */
    private static double calculateRobotProjectionAlongAxis(BoundingBox robotBoundingBox, Pose2d zoneCenter, boolean isXAxis) {
        final Rotation2d zoneRelativeRobotRotation = robotBoundingBox.getRotation().minus(zoneCenter.getRotation());
        final double
                robotXWidthProjection = robotBoundingBox.getXWidth() * (isXAxis ? Math.abs(zoneRelativeRobotRotation.getCos()) : Math.abs(zoneRelativeRobotRotation.getSin())),
                robotYWidthProjection = robotBoundingBox.getYWidth() * (isXAxis ? Math.abs(zoneRelativeRobotRotation.getSin()) : Math.abs(zoneRelativeRobotRotation.getCos()));
        return robotXWidthProjection + robotYWidthProjection;
    }

    /**
     * Returns the distance from the robot's edge to the zone wall at the upper extent of the axis.
     *
     * @param zoneRelativeRobotCenterPosition the robot's center position along the axis in the zone's coordinate frame
     * @param zoneHalfWidth                   the half-width of the zone along the axis
     * @param robotHalfProjection             the robot's projected half-length along the axis
     * @return the distance to the upper wall
     */
    private static double calculateDistanceToUpperWall(double zoneRelativeRobotCenterPosition, double zoneHalfWidth, double robotHalfProjection) {
        final double distanceCenterToUpperWall = zoneHalfWidth - zoneRelativeRobotCenterPosition;
        return distanceCenterToUpperWall - robotHalfProjection;
    }

    /**
     * Returns the distance from the robot's edge to the zone wall at the lower extent of the axis.
     *
     * @param zoneRelativeRobotCenterPosition the robot's center position along the axis in the zone's coordinate frame
     * @param zoneHalfWidth                   the half-width of the zone along the axis
     * @param robotHalfProjection             the robot's projected half-length along the axis
     * @return the distance to the lower wall
     */
    private static double calculateDistanceToLowerWall(double zoneRelativeRobotCenterPosition, double zoneHalfWidth, double robotHalfProjection) {
        final double distanceCenterToLowerWall = zoneRelativeRobotCenterPosition + zoneHalfWidth;
        return distanceCenterToLowerWall - robotHalfProjection;
    }

    /**
     * Converts a field relative position into the zone's coordinate frame.
     *
     * @param fieldRelativePosition the field relative position to convert
     * @param zoneCenter            the center pose of the containment zone
     * @return the position in the zone's coordinate frame
     */
    private static Translation2d toZoneRelativePosition(Translation2d fieldRelativePosition, Pose2d zoneCenter) {
        final Translation2d offsetFromZoneCenter = fieldRelativePosition.minus(zoneCenter.getTranslation());
        return offsetFromZoneCenter.rotateBy(zoneCenter.getRotation().unaryMinus());
    }

    /**
     * Converts a field relative translation into the zone's coordinate frame.
     *
     * @param translation the field relative translation to convert
     * @param zoneCenter  the center pose of the containment zone
     * @return the translation in the zone's coordinate frame
     */
    private static Translation2d toZoneRelativeDirection(Translation2d translation, Pose2d zoneCenter) {
        final Rotation2d inverseZoneRotation = zoneCenter.getRotation().unaryMinus();
        return translation.rotateBy(inverseZoneRotation);
    }

    /**
     * Converts a translation from the zone's coordinate frame into field relative coordinates.
     *
     * @param zoneRelativeDirection the zone-relative translation to convert
     * @param zoneCenter            the center pose of the containment zone
     * @return the field relative translation
     */
    private static Translation2d fromZoneRelativeDirection(Translation2d zoneRelativeDirection, Pose2d zoneCenter) {
        final Rotation2d zoneRotation = zoneCenter.getRotation();
        return zoneRelativeDirection.rotateBy(zoneRotation);
    }
}