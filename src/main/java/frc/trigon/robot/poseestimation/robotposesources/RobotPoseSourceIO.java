package frc.trigon.robot.poseestimation.robotposesources;

import org.littletonrobotics.junction.AutoLog;

public class RobotPoseSourceIO {
    protected void updateInputs(RobotPoseSourceInputsAutoLogged inputs) {
    }

    @AutoLog
    public static class RobotPoseSourceInputs {
        public boolean hasResult = false;
        public double lastResultTimestamp = 0;
        public double[] cameraPose = new double[6];
        public double averageDistanceFromTags = 0;
        public int visibleTags = 0;
    }
}
