package frc.trigon.lib.utilities;

import edu.wpi.first.math.geometry.*;

import java.util.function.Function;

/**
 * Handles transformations between camera and robot coordinate frames.
 * Supports both static and time-dependent camera positions, useful for cameras on moving mechanisms.
 */
public class DynamicCameraTransform {
    private final Function<Double, Transform3d> robotCenterToCameraFunction;

    /**
     * Constructs a DynamicCameraTransform with a static camera position.
     *
     * @param robotCenterToCamera the fixed transform from robot center to camera
     */
    public DynamicCameraTransform(Transform3d robotCenterToCamera) {
        this(timestamp -> robotCenterToCamera);
    }

    /**
     * Constructs a DynamicCameraTransform with a time-dependent camera position.
     *
     * @param robotCenterToCameraFunction function that returns the transform from robot center to camera at a given timestamp
     */
    public DynamicCameraTransform(Function<Double, Transform3d> robotCenterToCameraFunction) {
        this.robotCenterToCameraFunction = robotCenterToCameraFunction;
    }

    /**
     * Transforms a camera pose to a robot pose in 2D.
     *
     * @param cameraPose       the camera's pose on the field
     * @param timestampSeconds the timestamp for the camera transform
     * @return the robot's pose on the field
     */
    public Pose2d calculate2dRobotPose(Pose2d cameraPose, double timestampSeconds) {
        final Transform2d cameraToRobotCenter = get2dCameraToRobotCenter(timestampSeconds);
        return cameraPose.transformBy(cameraToRobotCenter);
    }

    /**
     * Transforms a camera pose to a robot pose in 3D.
     *
     * @param cameraPose       the camera's pose in 3D space
     * @param timestampSeconds the timestamp for the camera transform
     * @return the robot's pose in 3D space
     */
    public Pose3d calculate3dRobotPose(Pose3d cameraPose, double timestampSeconds) {
        final Transform3d cameraToRobotCenter = get3dCameraToRobotCenter(timestampSeconds);
        return cameraPose.transformBy(cameraToRobotCenter);
    }

    /**
     * Gets the 2D transform from camera to robot center.
     * Only x, y, and yaw components are preserved to avoid pitch and roll inaccuracies.
     *
     * @param timestampSeconds the timestamp for the transform
     * @return the 2D transform from camera to robot center
     */
    public Transform2d get2dCameraToRobotCenter(double timestampSeconds) {
        return get2dRobotCenterToCamera(timestampSeconds).inverse();
    }

    /**
     * Gets the 2D transform from robot center to camera.
     * Only x, y, and yaw components are preserved to avoid pitch and roll inaccuracies.
     *
     * @param timestampSeconds the timestamp for the transform
     * @return the 2D transform from robot center to camera
     */
    public Transform2d get2dRobotCenterToCamera(double timestampSeconds) {
        return toTransform2d(get3dRobotCenterToCamera(timestampSeconds));
    }

    /**
     * Gets the 3D transform from camera to robot center.
     *
     * @param timestampSeconds the timestamp for the transform
     * @return the 3D transform from camera to robot center
     */
    public Transform3d get3dCameraToRobotCenter(double timestampSeconds) {
        return get3dRobotCenterToCamera(timestampSeconds).inverse();
    }

    /**
     * Gets the 3D transform from robot center to camera.
     *
     * @param timestampSeconds the timestamp for the transform
     * @return the 3D transform from robot center to camera
     */
    public Transform3d get3dRobotCenterToCamera(double timestampSeconds) {
        return robotCenterToCameraFunction.apply(timestampSeconds);
    }

    /**
     * Converts a 3D transform to a 2D transform using only x, y, and yaw components.
     *
     * @param transform3d the 3D transform to convert
     * @return the 2D transform
     */
    private Transform2d toTransform2d(Transform3d transform3d) {
        final Translation2d robotCenterToCameraTranslation = transform3d.getTranslation().toTranslation2d();
        final Rotation2d robotCenterToCameraRotation = transform3d.getRotation().toRotation2d();

        return new Transform2d(robotCenterToCameraTranslation, robotCenterToCameraRotation);
    }
}