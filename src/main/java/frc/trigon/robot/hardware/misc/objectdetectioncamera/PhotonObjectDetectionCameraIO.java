package frc.trigon.robot.hardware.misc.objectdetectioncamera;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class PhotonObjectDetectionCameraIO extends ObjectDetectionCameraIO {
    private final PhotonCamera photonCamera;

    protected PhotonObjectDetectionCameraIO(String hostname) {
        PhotonCamera.setVersionCheckEnabled(false);
        photonCamera = new PhotonCamera(hostname);
    }

    @Override
    protected void updateInputs(ObjectDetectionCameraInputsAutoLogged inputs) {
        if (!photonCamera.isConnected())
            return;
        final PhotonPipelineResult result = photonCamera.getLatestResult();

        inputs.hasTargets = result.hasTargets();
        if (inputs.hasTargets) {
            inputs.bestObjectYaw = getBestTargetYaw(result);
            inputs.visibleObjectsYaw = result.getTargets().stream().mapToDouble((it) -> -it.getYaw()).toArray();
        }
    }

    private double getBestTargetYaw(PhotonPipelineResult result) {
        double lowestSum = 100000;
        double chosenOne = 0;
        for (PhotonTrackedTarget target : result.getTargets()) {
            final double current = Math.abs(target.getYaw()) + Math.abs(target.getPitch());
            if (lowestSum > current) {
                lowestSum = current;
                chosenOne = -target.getYaw();
            }
        }
        return chosenOne;
    }
}
