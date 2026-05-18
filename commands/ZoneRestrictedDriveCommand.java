package frc.trigon.lib.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.utilities.BoundingBox;
import frc.trigon.lib.utilities.zonerestricteddrive.ContainmentZone;
import frc.trigon.lib.utilities.zonerestricteddrive.ZoneRestriction;
import frc.trigon.robot.RobotContainer;
import frc.trigon.robot.commands.CommandConstants;
import frc.trigon.robot.constants.FieldConstants;
import frc.trigon.robot.constants.OperatorConstants;
import frc.trigon.robot.subsystems.swerve.SwerveCommands;

/**
 * Drives the robot while restricting movement relative to defined zones on the field.
 * Restricted zones slow and block movement into them.
 * Containment zones slow and block movement out of them.
 * All zone restrictions are applied sequentially, each further restricting the previous result.
 * Movement parallel to or away from a zone boundary is never restricted.
 */
public class ZoneRestrictedDriveCommand extends ParallelCommandGroup {
    private static final double
            ROBOT_X_WIDTH_METERS = 1,
            ROBOT_Y_WIDTH_METERS = 1;
    private static final double
            FIELD_BOUNDARY_MINIMUM_DISTANCE_METERS = 0.1,
            FIELD_BOUNDARY_BRAKING_ZONE_DISTANCE_METERS = 0.3;
    private static final ContainmentZone FIELD_BOUNDARY_ZONE = new ContainmentZone(
            FieldConstants.FIELD_BOUNDING_BOX,
            FIELD_BOUNDARY_MINIMUM_DISTANCE_METERS,
            FIELD_BOUNDARY_BRAKING_ZONE_DISTANCE_METERS
    );

    private final ZoneRestriction[] zoneRestrictions;
    private Translation2d cachedRestrictedTranslation;

    /**
     * Creates a new ZoneRestrictedDriveCommand.
     *
     * @param shouldRestrictToField whether to restrict the robot from leaving the field boundary
     * @param zoneRestrictions      the zones to restrict movement relative to
     */
    public ZoneRestrictedDriveCommand(boolean shouldRestrictToField, ZoneRestriction... zoneRestrictions) {
        this.zoneRestrictions = shouldRestrictToField ? getZoneRestrictionsWithFieldRestriction(zoneRestrictions) : zoneRestrictions;

        if (RobotHardwareStats.isSimulation())
            logAllZoneBoundaries();

        addCommands(
                getUpdateCachedTranslationCommand(),
                getDriveCommand()
        );
    }

    private ZoneRestriction[] getZoneRestrictionsWithFieldRestriction(ZoneRestriction[] zoneRestrictions) {
        final ZoneRestriction[] allZones = new ZoneRestriction[zoneRestrictions.length + 1];

        allZones[0] = FIELD_BOUNDARY_ZONE;
        System.arraycopy(zoneRestrictions, 0, allZones, 1, zoneRestrictions.length);

        return allZones;
    }

    private void logAllZoneBoundaries() {
        for (int i = 0; i < zoneRestrictions.length; i++)
            zoneRestrictions[i].getBoundingBox().log("ZoneRestrictions/Zone" + i);
    }

    private Command getUpdateCachedTranslationCommand() {
        return new RunCommand(() -> cachedRestrictedTranslation = calculateRestrictedTranslation());
    }

    private Command getDriveCommand() {
        return SwerveCommands.getClosedLoopFieldRelativeDriveCommand(
                () -> cachedRestrictedTranslation.getX(),
                () -> cachedRestrictedTranslation.getY(),
                () -> CommandConstants.calculateRotationStickAxisValue(OperatorConstants.DRIVER_CONTROLLER.getRightX())
        ).onlyIf(() -> cachedRestrictedTranslation != null).repeatedly().asProxy();
    }

    /**
     * Calculates the target translation after applying all zone restrictions sequentially.
     * Each zone further restricts the translation produced by the previous zone.
     *
     * @return the restricted target translation
     */
    private Translation2d calculateRestrictedTranslation() {
        final BoundingBox robotBoundingBox = getRobotBoundingBox();
        Translation2d targetFieldRelativeTranslation = calculateTargetJoystickTranslation().unaryMinus();

        for (ZoneRestriction zone : zoneRestrictions)
            targetFieldRelativeTranslation = zone.applyRestriction(targetFieldRelativeTranslation, robotBoundingBox);

        return targetFieldRelativeTranslation.unaryMinus();
    }

    /**
     * Calculates the target translation based on driver input.
     *
     * @return the target translation
     */
    private Translation2d calculateTargetJoystickTranslation() {
        final double
                rawXValue = OperatorConstants.DRIVER_CONTROLLER.getLeftY(),
                rawYValue = OperatorConstants.DRIVER_CONTROLLER.getLeftX();

        return new Translation2d(
                CommandConstants.calculateDriveStickAxisValue(rawXValue),
                CommandConstants.calculateDriveStickAxisValue(rawYValue)
        );
    }

    /**
     * Returns a bounding box representing the robot's current position and size on the field.
     *
     * @return the robot's current bounding box
     */
    private BoundingBox getRobotBoundingBox() {
        final Pose2d robotPose = RobotContainer.ROBOT_POSE_ESTIMATOR.getEstimatedRobotPose();
        return new BoundingBox(robotPose, ROBOT_X_WIDTH_METERS, ROBOT_Y_WIDTH_METERS);
    }
}