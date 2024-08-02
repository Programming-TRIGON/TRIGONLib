package frc.trigon.robot.poseestimation.robotposesources;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.trigon.robot.poseestimation.photonposeestimator.EstimatedRobotPose;
import frc.trigon.robot.poseestimation.photonposeestimator.PhotonPoseEstimator;
import org.littletonrobotics.junction.Logger;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.util.List;
import java.util.Optional;

public class AprilTagPhotonCameraIO extends RobotPoseSourceIO {
    private final PhotonCamera photonCamera;
    private final PhotonPoseEstimator photonPoseEstimator;

    protected AprilTagPhotonCameraIO(String cameraName, Transform3d robotCenterToCamera) {
        photonCamera = new PhotonCamera(cameraName);

        photonPoseEstimator = new PhotonPoseEstimator(
                RobotPoseSourceConstants.APRIL_TAG_FIELD_LAYOUT,
                RobotPoseSourceConstants.PRIMARY_POSE_STRATEGY,
                photonCamera,
                robotCenterToCamera
        );

        photonPoseEstimator.setMultiTagFallbackStrategy(RobotPoseSourceConstants.SECONDARY_POSE_STRATEGY);
    }

    @Override
    protected void updateInputs(RobotPoseSourceInputsAutoLogged inputs) {
        final PhotonPipelineResult latestResult = photonCamera.getLatestResult();
        Optional<EstimatedRobotPose> optionalEstimatedRobotPose = photonPoseEstimator.update(latestResult);

        inputs.hasResult = hasResult(optionalEstimatedRobotPose);
        if (inputs.hasResult) {
            final EstimatedRobotPose estimatedRobotPose = optionalEstimatedRobotPose.get();
            inputs.cameraPose = RobotPoseSource.pose3dToDoubleArray(estimatedRobotPose.estimatedPose);
            inputs.lastResultTimestamp = estimatedRobotPose.timestampSeconds;
            inputs.visibleTags = estimatedRobotPose.targetsUsed.size();
            inputs.averageDistanceFromTags = getAverageDistanceFromTags(latestResult);
        } else {
            inputs.visibleTags = 0;
            inputs.cameraPose = new double[0];
        }

        logVisibleTags(inputs.hasResult, optionalEstimatedRobotPose);
    }

    private void logVisibleTags(boolean hasResult, Optional<EstimatedRobotPose> optionalEstimatedRobotPose) {
        if (!hasResult) {
            Logger.recordOutput("VisibleTags/" + photonCamera.getName(), new Pose2d[0]);
            return;
        }

        final EstimatedRobotPose estimatedRobotPose = optionalEstimatedRobotPose.get();
        final Pose2d[] visibleTagPoses = new Pose2d[estimatedRobotPose.targetsUsed.size()];
        for (int i = 0; i < visibleTagPoses.length; i++) {
            final int currentId = estimatedRobotPose.targetsUsed.get(i).getFiducialId();
            final Pose2d currentPose = RobotPoseSourceConstants.TAG_ID_TO_POSE.get(currentId).toPose2d();
            visibleTagPoses[i] = currentPose;
        }
        Logger.recordOutput("VisibleTags/" + photonCamera.getName(), visibleTagPoses);
    }

    private boolean hasResult(Optional<EstimatedRobotPose> optionalEstimatedRobotPose) {
        final boolean isEmpty = optionalEstimatedRobotPose.isEmpty();
        if (isEmpty)
            return false;
        final EstimatedRobotPose estimatedRobotPose = optionalEstimatedRobotPose.get();
        if (estimatedRobotPose.strategy == PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR)
            return true;
        return estimatedRobotPose.targetsUsed.get(0).getPoseAmbiguity() < RobotPoseSourceConstants.MAXIMUM_AMBIGUITY;
    }

    private double getAverageDistanceFromTags(PhotonPipelineResult result) {
        final List<PhotonTrackedTarget> targets = result.targets;
        double distanceSum = 0;

        for (PhotonTrackedTarget currentTarget : targets) {
            final Translation2d distanceTranslation = currentTarget.getBestCameraToTarget().getTranslation().toTranslation2d();
            distanceSum += distanceTranslation.getNorm();
        }

        return distanceSum / targets.size();
    }
}
