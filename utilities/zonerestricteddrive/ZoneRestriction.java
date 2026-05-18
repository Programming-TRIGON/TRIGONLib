package frc.trigon.lib.utilities.zonerestricteddrive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import frc.trigon.lib.utilities.BoundingBox;

/**
 * Represents a zone that restricts the robot's movement relative to its boundary.
 */
public interface ZoneRestriction {
    BoundingBox getBoundingBox();

    /**
     * Gets the distance from the zone boundary that the robot cannot cross.
     */
    double getMinimumDistanceMeters();

    /**
     * Gets the distance from the zone boundary at which braking begins.
     */
    double getBrakingZoneDistanceMeters();

    /**
     * Applies this zone's movement restriction to the given translation.
     *
     * @param targetTranslation the target field relative translation for the robot
     * @param robotBoundingBox  the robot's current bounding box
     * @return the restricted target translation
     */
    Translation2d applyRestriction(Translation2d targetTranslation, BoundingBox robotBoundingBox);

    /**
     * Calculates braking strength based on the distance to the zone boundary.
     * Returns 1 at the outer edge of the braking zone and 0 at the minimum distance.
     *
     * @param distanceToBoundary the current distance to the zone boundary
     * @return a velocity scale factor from 0 to 1
     */
    default double calculateBrakingScale(double distanceToBoundary) {
        final double
                distanceIntoBrakingZone = distanceToBoundary - getMinimumDistanceMeters(),
                brakingZoneSize = getBrakingZoneDistanceMeters() - getMinimumDistanceMeters();

        if (brakingZoneSize <= 0)
            return distanceToBoundary > getMinimumDistanceMeters() ? 1 : 0;

        return MathUtil.clamp(distanceIntoBrakingZone / brakingZoneSize, 0, 1);
    }
}
