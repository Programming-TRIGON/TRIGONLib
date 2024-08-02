package frc.trigon.robot.hardware.misc.objectdetectioncamera;

import org.littletonrobotics.junction.AutoLog;

public class ObjectDetectionCameraIO {
    protected ObjectDetectionCameraIO() {
    }

    protected void updateInputs(ObjectDetectionCameraInputsAutoLogged inputs) {
    }

    @AutoLog
    public static class ObjectDetectionCameraInputs {
        public boolean hasTargets = false;
        public double bestObjectYaw = 0;
        public double[] visibleObjectsYaw = new double[0];
    }
}
