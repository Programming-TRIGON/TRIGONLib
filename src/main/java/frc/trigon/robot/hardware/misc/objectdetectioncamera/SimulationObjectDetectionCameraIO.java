package frc.trigon.robot.hardware.misc.objectdetectioncamera;

import edu.wpi.first.math.geometry.*;
import frc.trigon.robot.RobotContainer;
import frc.trigon.robot.commands.Commands;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationObjectDetectionCameraIO extends ObjectDetectionCameraIO {
    public static boolean HAS_OBJECTS = true;
    private static final Rotation2d HORIZONTAL_FOV = Rotation2d.fromDegrees(75);
    private static final double
            MAXIMUM_DISTANCE_METERS = 5,
            MINIMUM_DISTANCE_METERS = 0.05;
    private static final double PICKING_UP_TOLERANCE_METERS = 0.3;

    private final ArrayList<Translation2d> objectsOnField = new ArrayList<>(Arrays.asList(
            new Translation2d(2.9, 7),
            new Translation2d(2.9, 5.5),
            new Translation2d(2.9, 4.1),
            new Translation2d(8.3, 7.45),
            new Translation2d(8.3, 5.75),
            new Translation2d(8.3, 4.1),
            new Translation2d(8.3, 2.45),
            new Translation2d(8.3, 0.75),
            new Translation2d(13.65, 7),
            new Translation2d(13.65, 5.5),
            new Translation2d(13.65, 4.1)
    ));
    private final String hostname;
    private Pose3d[] heldObject = {new Pose3d()};
    private boolean isDelayingEjection = false;

    protected SimulationObjectDetectionCameraIO(String hostname) {
        this.hostname = hostname;
    }

    @Override
    protected void updateInputs(ObjectDetectionCameraInputsAutoLogged inputs) {
        final Rotation2d closestObjectYaw = getClosestVisibleObjectYaw(RobotContainer.POSE_ESTIMATOR.getCurrentPose());
        if (closestObjectYaw == null) {
            inputs.hasTargets = false;
        } else {
            inputs.hasTargets = true;
            inputs.bestObjectYaw = closestObjectYaw.getDegrees();
            inputs.visibleObjectsYaw = new double[]{inputs.bestObjectYaw};
        }

        updateObjectCollection();
        updateObjectEjection();
        updateHeldObjectPose();
        HAS_OBJECTS = heldObject.length != 0;
        Logger.recordOutput("Poses/GamePieces/HeldGamePiece", heldObject);
        Logger.recordOutput("Poses/GamePieces/ObjectsOnField", toPosesArray(objectsOnField));
    }

    private void updateHeldObjectPose() {
        if (heldObject.length == 0)
            return;
        heldObject[0] = getHeldObjectPose(RobotContainer.POSE_ESTIMATOR.getCurrentPose());
    }

    private void updateObjectEjection() {
        if (heldObject.length == 0 || !isEjecting() || isDelayingEjection)
            return;
        isDelayingEjection = true;
        Commands.getDelayedCommand(0.04, () -> {
            heldObject = new Pose3d[0];
            isDelayingEjection = false;
        }).schedule();
    }

    private void updateObjectCollection() {
        if (heldObject.length == 1 || !isCollecting())
            return;
        final Pose2d robotPose = RobotContainer.POSE_ESTIMATOR.getCurrentPose();
        final Translation2d robotTranslation = robotPose.getTranslation();
        for (Translation2d objectPlacement : objectsOnField) {
            if (objectPlacement.getDistance(robotTranslation) <= PICKING_UP_TOLERANCE_METERS) {
                heldObject = new Pose3d[]{getHeldObjectPose(robotPose)};
                objectsOnField.remove(objectPlacement);
                Commands.getDelayedCommand(10, () -> objectsOnField.add(objectPlacement)).schedule();
                break;
            }
        }
    }

    private Pose3d getHeldObjectPose(Pose2d robotPose) {
        //todo: get the pose of the object held by the robot
        return new Pose3d();
    }

    private Transform3d toTransform(Pose3d pose) {
        return new Transform3d(pose.getTranslation(), pose.getRotation());
    }

    private Pose3d[] toPosesArray(List<Translation2d> translationsList) {
        final Pose3d[] posesArray = new Pose3d[translationsList.size()];
        for (int i = 0; i < translationsList.size(); i++) {
            final Translation2d translation = translationsList.get(i);
            posesArray[i] = new Pose3d(translation.getX(), translation.getY(), 0.1, new Rotation3d());
        }
        return posesArray;
    }

    private boolean isEjecting() {
        //todo: check if the robot is ejecting
        return false;
    }

    private boolean isCollecting() {
        //todo: check if the robot is collecting
        return false;
    }

    private Rotation2d getClosestVisibleObjectYaw(Pose2d robotPose) {
        Translation2d closestObject = null;
        Rotation2d closestObjectYaw = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        for (Translation2d objectPlacement : objectsOnField) {
            final Rotation2d angleToObject = getAngleToObject(objectPlacement, robotPose);
            if (!isWithinHorizontalFOV(angleToObject, robotPose) || !isWithinDistance(objectPlacement, robotPose))
                continue;

            final double distance = getObjectDistance(objectPlacement, robotPose);
            if (distance < closestDistance) {
                closestObject = objectPlacement;
                closestObjectYaw = angleToObject.minus(robotPose.getRotation());
                closestDistance = distance;
            }
        }

        logObjectPlacement(closestObject);
        return closestObjectYaw;
    }

    private void logObjectPlacement(Translation2d objectPlacement) {
        if (objectPlacement != null)
            Logger.recordOutput(hostname + "/ClosestObject", objectPlacement);
        else
            Logger.recordOutput(hostname + "/ClosestObject", new Translation2d[0]);
    }

    private boolean isWithinHorizontalFOV(Rotation2d objectYaw, Pose2d robotPose) {
        return Math.abs(objectYaw.minus(robotPose.getRotation()).getRadians()) <= HORIZONTAL_FOV.getRadians() / 2;
    }

    private boolean isWithinDistance(Translation2d objectPlacement, Pose2d robotPose) {
        final double distance = getObjectDistance(objectPlacement, robotPose);
        return distance <= MAXIMUM_DISTANCE_METERS && distance >= MINIMUM_DISTANCE_METERS;
    }

    private double getObjectDistance(Translation2d objectPlacement, Pose2d robotPose) {
        final Translation2d difference = objectPlacement.minus(robotPose.getTranslation());
        return difference.getNorm();
    }

    private Rotation2d getAngleToObject(Translation2d objectPlacement, Pose2d robotPose) {
        final Translation2d difference = objectPlacement.minus(robotPose.getTranslation());
        return Rotation2d.fromRadians(Math.atan2(difference.getY(), difference.getX()));
    }
}
