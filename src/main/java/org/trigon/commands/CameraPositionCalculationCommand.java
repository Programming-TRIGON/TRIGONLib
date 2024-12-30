package org.trigon.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import java.util.Optional;
import java.util.function.DoubleConsumer;
import java.util.function.Supplier;

public class CameraPositionCalculationCommand extends Command {
    private static final LoggedNetworkNumber ROTATION_SPEED = new LoggedNetworkNumber("CameraPositionCalculationCommand/RotationSpeed", 1);
    private static final LoggedNetworkNumber ROTATION_RATE_LIMIT = new LoggedNetworkNumber("CameraPositionCalculationCommand/RotationRateLimit", 1);

    private final Supplier<Optional<Pose2d>> cameraPose;
    private final Rotation2d minimumRotationDistance;
    private final DoubleConsumer rotateRobot;
    private final Supplier<Rotation2d> robotHeading;

    private SlewRateLimiter rotationSlewRateLimiter;
    private boolean hasSetFirstCameraPose = false;
    private Pose2d startingCameraPose = null;
    private Rotation2d StartingCameraPoseHeading;
    private Transform2d robotToCamera = null;

    public CameraPositionCalculationCommand(Supplier<Optional<Pose2d>> cameraPose, Rotation2d minimumRotationDistance, DoubleConsumer rotateRobot, Supplier<Rotation2d> robotHeading, SubsystemBase requirement) {
        this.cameraPose = cameraPose;
        this.minimumRotationDistance = minimumRotationDistance;
        this.rotateRobot = rotateRobot;
        this.robotHeading = robotHeading;
    }

    @Override
    public void initialize() {
        rotationSlewRateLimiter = new SlewRateLimiter(ROTATION_RATE_LIMIT.get());
        rotationSlewRateLimiter.reset(0);
    }

    @Override
    public void execute() {
        rotateRobot.accept(rotationSlewRateLimiter.calculate(ROTATION_SPEED.get()));

        if (!hasSetFirstCameraPose)
            if (cameraPose.get().isPresent()) {
                startingCameraPose = cameraPose.get().get();
                hasSetFirstCameraPose = true;
                StartingCameraPoseHeading = robotHeading.get();
            } else if (Math.abs(robotHeading.get().minus(StartingCameraPoseHeading).getRadians()) > minimumRotationDistance.getRadians())
                if (cameraPose.get().isPresent()) {
                    robotToCamera = solveRobotToCamera(startingCameraPose, cameraPose.get().get());
                    this.end(false);
                }
    }

    @Override
    public void end(boolean interrupted) {
        if (robotToCamera != null)
            Logger.recordOutput("CameraPositionCalculationCommand/RobotToCamera", robotToCamera);
    }

    private Transform2d solveRobotToCamera(Pose2d firstCameraPose, Pose2d secondCameraPose) {
        double firstCameraPoseX = firstCameraPose.getTranslation().getX();
        double firstCameraPoseY = firstCameraPose.getTranslation().getY();
        double firstCameraPoseCosine = firstCameraPose.getRotation().getCos();
        double firstCameraPoseSine = firstCameraPose.getRotation().getSin();

        double secondCameraPoseX = secondCameraPose.getTranslation().getX();
        double secondCameraPoseY = secondCameraPose.getTranslation().getY();
        double secondCameraPoseCosine = secondCameraPose.getRotation().getCos();
        double secondCameraPoseSine = secondCameraPose.getRotation().getSin();

        double denominator = calculateCameraPositionCalculationDenominator(firstCameraPoseCosine, secondCameraPoseCosine, firstCameraPoseSine, secondCameraPoseSine);
        double xNumerator = calculateXNumerator(firstCameraPoseX, secondCameraPoseX, firstCameraPoseY, secondCameraPoseY, firstCameraPoseCosine, secondCameraPoseCosine, firstCameraPoseSine, secondCameraPoseSine);
        double yNumerator = calculateYNumerator(firstCameraPoseX, secondCameraPoseX, firstCameraPoseY, secondCameraPoseY, firstCameraPoseCosine, secondCameraPoseCosine, firstCameraPoseSine, secondCameraPoseSine);

        double xCameraToRobotDistanceMeters = xNumerator / denominator;
        double yCameraToRobotDistanceMeters = yNumerator / denominator;

        return new Transform2d(new Translation2d(xCameraToRobotDistanceMeters, yCameraToRobotDistanceMeters), new Rotation2d(0));
    }

    private double calculateCameraPositionCalculationDenominator(double firstPoseCos, double secondPoseCos, double firstPoseSin, double secondPoseSin) {
        return (firstPoseCos - secondPoseCos) * (firstPoseCos - secondPoseCos) + (firstPoseSin - secondPoseSin) * (firstPoseSin - secondPoseSin);
    }

    private double calculateXNumerator(double firstPoseX, double secondPoseX, double firstPoseY, double secondPoseY, double firstPoseCos, double secondPoseCos, double firstPoseSin, double secondPoseSin) {
        return -calculateCameraToRobotAxisDistance(firstPoseX, secondPoseX, firstPoseY, secondPoseY, firstPoseCos - secondPoseCos, firstPoseSin - secondPoseSin);
    }

    private double calculateYNumerator(double firstPoseX, double secondPoseX, double firstPoseY, double secondPoseY, double firstPoseCos, double secondPoseCos, double firstPoseSin, double secondPoseSin) {
        return calculateCameraToRobotAxisDistance(firstPoseX, secondPoseX, firstPoseY, secondPoseY, firstPoseSin - secondPoseSin, secondPoseCos - firstPoseCos);
    }

    /**
     * Calculates the distance between the camera and the robot on the given axis.
     * The formula for the calculation is the same for both the x and y axis, other than the custom parameters.
     *
     * @param firstPoseX  the x translation of the first camera pose
     * @param secondPoseX the x translation of the second camera pose
     * @param firstPoseY  the y translation of the first camera pose
     * @param secondPoseY the y translation of the second camera pose
     * @param firstParam  the custom parameter for the first half of the calculation
     * @param secondParam the custom parameter for the second half of the calculation
     * @return the distance between the camera and the robot on the given axis
     */
    private double calculateCameraToRobotAxisDistance(double firstPoseX, double secondPoseX, double firstPoseY, double secondPoseY, double firstParam, double secondParam) {
        return ((secondPoseX - firstPoseX) * (firstParam) + (secondPoseY - firstPoseY) * (secondParam));
    }
}
