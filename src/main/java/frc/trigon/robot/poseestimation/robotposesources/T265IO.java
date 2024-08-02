package frc.trigon.robot.poseestimation.robotposesources;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.trigon.robot.utilities.JsonHandler;

public class T265IO extends RobotPoseSourceIO {
    private static final NetworkTable NETWORK_TABLE = NetworkTableInstance.getDefault().getTable("T265");
    private static final short CONFIDENCE_THRESHOLD = 2;
    private final NetworkTableEntry jsonDump;

    protected T265IO(String name) {
        jsonDump = NETWORK_TABLE.getEntry(name + "/jsonDump");
    }

    @Override
    protected void updateInputs(RobotPoseSourceInputsAutoLogged inputs) {
        inputs.hasResult = canUseJsonDump();
        if (inputs.hasResult)
            inputs.cameraPose = RobotPoseSource.pose3dToDoubleArray(getCameraPose());
        inputs.lastResultTimestamp = (double) jsonDump.getLastChange() / 1000000;
    }

    private Pose3d getCameraPose() {
        if (!canUseJsonDump())
            return null;

        return getRobotPoseFromJsonDump();
    }

    private Pose3d getRobotPoseFromJsonDump() {
        final T265JsonDump jsonDump = getJsonDump();
        final Translation3d translation = getTranslationFromDoubleArray(jsonDump.translation);
        final Rotation3d rotation = getRotationFromDoubleArray(jsonDump.rotation);

        return t265PoseToWPIPose(new Pose3d(translation, rotation));
    }

    private Pose3d t265PoseToWPIPose(Pose3d t265Pose) {
        final CoordinateSystem eusCoordinateSystem = new CoordinateSystem(CoordinateAxis.E(), CoordinateAxis.U(), CoordinateAxis.S());
        final Pose3d convertedPose = CoordinateSystem.convert(t265Pose, eusCoordinateSystem, CoordinateSystem.NWU());
        final Rotation3d convertedRotation = convertedPose.getRotation().plus(new Rotation3d(0, 0, Math.toRadians(90)));

        return new Pose3d(convertedPose.getTranslation(), convertedRotation);
    }

    private Translation3d getTranslationFromDoubleArray(double[] xyz) {
        return new Translation3d(xyz[0], xyz[1], xyz[2]);
    }

    private Rotation3d getRotationFromDoubleArray(double[] wxyz) {
        return new Rotation3d(new Quaternion(wxyz[0], wxyz[1], wxyz[2], wxyz[3]));
    }

    private boolean canUseJsonDump() {
        final T265JsonDump jsonDump = getJsonDump();

        try {
            return jsonDump.confidence >= CONFIDENCE_THRESHOLD && jsonDump.translation.length == 3 && jsonDump.rotation.length == 4;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private T265JsonDump getJsonDump() {
        return JsonHandler.parseJsonStringToObject(jsonDump.getString(""), T265JsonDump.class);
    }

    private static class T265JsonDump {
        private double[] translation;
        private double[] rotation;
        private int confidence;
    }
}