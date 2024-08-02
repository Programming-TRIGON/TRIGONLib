package frc.trigon.robot.subsystems.swerve;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathfindingCommand;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import frc.trigon.robot.RobotContainer;
import frc.trigon.robot.hardware.phoenix6.Phoenix6SignalThread;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2Gyro;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2Signal;
import frc.trigon.robot.subsystems.MotorSubsystem;
import frc.trigon.robot.utilities.mirrorable.Mirrorable;
import frc.trigon.robot.utilities.mirrorable.MirrorablePose2d;
import frc.trigon.robot.utilities.mirrorable.MirrorableRotation2d;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Swerve extends MotorSubsystem {
    private final Pigeon2Gyro gyro = SwerveConstants.GYRO;
    private final SwerveModule[] swerveModules = SwerveConstants.SWERVE_MODULES;
    private final Phoenix6SignalThread phoenix6SignalThread = Phoenix6SignalThread.getInstance();
    private double lastTimestamp = Timer.getFPGATimestamp();

    public Swerve() {
        setName("Swerve");
        configurePathPlanner();
        SwerveConstants.PROFILED_ROTATION_PID_CONTROLLER.enableContinuousInput(-180, 180);
    }

    @Override
    public void periodic() {
        Phoenix6SignalThread.SIGNALS_LOCK.lock();
        updateHardware();
        Phoenix6SignalThread.SIGNALS_LOCK.unlock();

        updatePoseEstimatorStates();
        RobotContainer.POSE_ESTIMATOR.periodic();
        updateNetworkTables();
    }

    @Override
    public void stop() {
        for (SwerveModule currentModule : swerveModules)
            currentModule.stop();
    }

    @Override
    public void setBrake(boolean brake) {
        for (SwerveModule currentModule : swerveModules)
            currentModule.setBrake(brake);
    }

    public Rotation2d getHeading() {
        final double inputtedHeading = MathUtil.inputModulus(gyro.getSignal(Pigeon2Signal.YAW), -0.5, 0.5);
        return Rotation2d.fromRotations(inputtedHeading);
    }

    public void setHeading(Rotation2d heading) {
        gyro.setYaw(heading);
    }

    public ChassisSpeeds getSelfRelativeVelocity() {
        return SwerveConstants.KINEMATICS.toChassisSpeeds(getModuleStates());
    }

    public ChassisSpeeds getFieldRelativeVelocity() {
        return ChassisSpeeds.fromRobotRelativeSpeeds(getSelfRelativeVelocity(), RobotContainer.POSE_ESTIMATOR.getCurrentPose().getRotation());
    }

    /**
     * Checks if the robot is at a pose.
     *
     * @param pose2d a pose, relative to the blue alliance driver station's right corner
     * @return whether the robot is at the pose
     */
    public boolean atPose(MirrorablePose2d pose2d) {
        final Pose2d mirroredPose = pose2d.get();
        return atXAxisPosition(mirroredPose.getX()) && atYAxisPosition(mirroredPose.getY()) && atAngle(pose2d.getRotation());
    }

    public boolean atXAxisPosition(double xAxisPosition) {
        final double currentXAxisVelocity = getFieldRelativeVelocity().vxMetersPerSecond;
        return atTranslationPosition(RobotContainer.POSE_ESTIMATOR.getCurrentPose().getX(), xAxisPosition, currentXAxisVelocity);
    }

    public boolean atYAxisPosition(double yAxisPosition) {
        final double currentYAxisVelocity = getFieldRelativeVelocity().vyMetersPerSecond;
        return atTranslationPosition(RobotContainer.POSE_ESTIMATOR.getCurrentPose().getY(), yAxisPosition, currentYAxisVelocity);
    }

    public boolean atAngle(MirrorableRotation2d angle) {
        final boolean atTargetAngle = Math.abs(angle.get().minus(RobotContainer.POSE_ESTIMATOR.getCurrentPose().getRotation()).getDegrees()) < SwerveConstants.ROTATION_TOLERANCE_DEGREES;
        final boolean isAngleStill = Math.abs(getSelfRelativeVelocity().omegaRadiansPerSecond) < SwerveConstants.ROTATION_VELOCITY_TOLERANCE;
        Logger.recordOutput("Swerve/AtTargetAngle/isStill", isAngleStill);
        Logger.recordOutput("Swerve/AtTargetAngle/atTargetAngle", atTargetAngle);
        return atTargetAngle/* && isAngleStill*/;
    }

    public SwerveModulePosition[] getWheelPositions() {
        final SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[swerveModules.length];
        for (int i = 0; i < swerveModules.length; i++)
            swerveModulePositions[i] = swerveModules[i].getOdometryPosition(swerveModules[i].getLastOdometryUpdateIndex());
        return swerveModulePositions;
    }

    public void runWheelRadiusCharacterization(double omegaRadsPerSec) {
        selfRelativeDrive(new ChassisSpeeds(0, 0, omegaRadsPerSec));
    }

    /**
     * Locks the swerve, so it'll be hard to move it. This will make the modules look in the middle of a robot in an "x" shape.
     */
    void lockSwerve() {
        final SwerveModuleState
                right = new SwerveModuleState(0, Rotation2d.fromDegrees(-45)),
                left = new SwerveModuleState(0, Rotation2d.fromDegrees(45));

        swerveModules[0].setTargetState(left);
        swerveModules[1].setTargetState(right);
        swerveModules[2].setTargetState(right);
        swerveModules[3].setTargetState(left);
    }

    /**
     * Moves the robot to a certain pose using PID.
     *
     * @param targetPose the target pose, relative to the blue alliance driver station's right corner
     */
    void pidToPose(MirrorablePose2d targetPose) {
        final Pose2d currentPose = RobotContainer.POSE_ESTIMATOR.getCurrentPose();
        final Pose2d mirroredTargetPose = targetPose.get();
        final double xSpeed = SwerveConstants.TRANSLATION_PID_CONTROLLER.calculate(currentPose.getX(), mirroredTargetPose.getX());
        final double ySpeed = SwerveConstants.TRANSLATION_PID_CONTROLLER.calculate(currentPose.getY(), mirroredTargetPose.getY());
        final int direction = Mirrorable.isRedAlliance() ? -1 : 1;
        final ChassisSpeeds targetFieldRelativeSpeeds = new ChassisSpeeds(
                xSpeed * direction,
                ySpeed * direction,
                calculateProfiledAngleSpeedToTargetAngle(targetPose.getRotation())
        );
        selfRelativeDrive(fieldRelativeSpeedsToSelfRelativeSpeeds(targetFieldRelativeSpeeds));
    }

    void initializeDrive(boolean closedLoop) {
        setClosedLoop(closedLoop);
        resetRotationController();
    }

    void resetRotationController() {
        SwerveConstants.PROFILED_ROTATION_PID_CONTROLLER.reset(RobotContainer.POSE_ESTIMATOR.getCurrentPose().getRotation().getDegrees());
    }

    void setClosedLoop(boolean closedLoop) {
        for (SwerveModule currentModule : swerveModules)
            currentModule.setDriveMotorClosedLoop(closedLoop);
    }

    /**
     * Drives the swerve with the given powers and a target angle, relative to the field's frame of reference.
     *
     * @param xPower      the x power
     * @param yPower      the y power
     * @param targetAngle the target angle, relative to the blue alliance's forward position
     */
    void fieldRelativeDrive(double xPower, double yPower, MirrorableRotation2d targetAngle) {
        final ChassisSpeeds speeds = selfRelativeSpeedsFromFieldRelativePowers(xPower, yPower, 0);
        speeds.omegaRadiansPerSecond = calculateProfiledAngleSpeedToTargetAngle(targetAngle);
        selfRelativeDrive(speeds);
    }

    /**
     * Drives the swerve with the given powers, relative to the field's frame of reference.
     *
     * @param xPower     the x power
     * @param yPower     the y power
     * @param thetaPower the theta power
     */
    void fieldRelativeDrive(double xPower, double yPower, double thetaPower) {
        final ChassisSpeeds speeds = selfRelativeSpeedsFromFieldRelativePowers(xPower, yPower, thetaPower);
        selfRelativeDrive(speeds);
    }

    /**
     * Drives the swerve with the given powers, relative to the robot's frame of reference.
     *
     * @param xPower     the x power
     * @param yPower     the y power
     * @param thetaPower the theta power
     */
    void selfRelativeDrive(double xPower, double yPower, double thetaPower) {
        final ChassisSpeeds speeds = powersToSpeeds(xPower, yPower, thetaPower);
        selfRelativeDrive(speeds);
    }

    /**
     * Drives the swerve with the given powers and a target angle, relative to the robot's frame of reference.
     *
     * @param xPower      the x power
     * @param yPower      the y power
     * @param targetAngle the target angle
     */
    void selfRelativeDrive(double xPower, double yPower, MirrorableRotation2d targetAngle) {
        final ChassisSpeeds speeds = powersToSpeeds(xPower, yPower, 0);
        speeds.omegaRadiansPerSecond = calculateProfiledAngleSpeedToTargetAngle(targetAngle);
        selfRelativeDrive(speeds);
    }

    private void selfRelativeDrive(ChassisSpeeds chassisSpeeds) {
        chassisSpeeds = discretize(chassisSpeeds);
        if (isStill(chassisSpeeds)) {
            stop();
            return;
        }

        final SwerveModuleState[] swerveModuleStates = SwerveConstants.KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        setTargetModuleStates(swerveModuleStates);
    }

    private void setTargetModuleStates(SwerveModuleState[] swerveModuleStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveConstants.MAX_SPEED_METERS_PER_SECOND);
        for (int i = 0; i < swerveModules.length; i++)
            swerveModules[i].setTargetState(swerveModuleStates[i]);
    }

    /**
     * When the robot drives while rotating it skews a bit to the side.
     * This should fix the chassis speeds, so they won't make the robot skew while rotating.
     *
     * @param chassisSpeeds the chassis speeds to fix skewing for
     * @return the fixed speeds
     */
    private ChassisSpeeds discretize(ChassisSpeeds chassisSpeeds) {
        final double currentTimestamp = Timer.getFPGATimestamp();
        final double difference = currentTimestamp - lastTimestamp;
        lastTimestamp = currentTimestamp;
        return ChassisSpeeds.discretize(chassisSpeeds, difference);
    }

    private void updatePoseEstimatorStates() {
        final double[] odometryUpdatesYawDegrees = gyro.getThreadedSignal(Pigeon2Signal.YAW);
        final int odometryUpdates = odometryUpdatesYawDegrees.length;
        final SwerveDriveWheelPositions[] swerveWheelPositions = new SwerveDriveWheelPositions[odometryUpdates];
        final Rotation2d[] gyroRotations = new Rotation2d[odometryUpdates];

        for (int i = 0; i < odometryUpdates; i++) {
            swerveWheelPositions[i] = getSwerveWheelPositions(i);
            gyroRotations[i] = Rotation2d.fromDegrees(odometryUpdatesYawDegrees[i]);
        }

        RobotContainer.POSE_ESTIMATOR.updatePoseEstimatorStates(swerveWheelPositions, gyroRotations, phoenix6SignalThread.getLatestTimestamps());
    }

    private SwerveDriveWheelPositions getSwerveWheelPositions(int odometryUpdateIndex) {
        final SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[swerveModules.length];
        for (int i = 0; i < swerveModules.length; i++)
            swerveModulePositions[i] = swerveModules[i].getOdometryPosition(odometryUpdateIndex);
        return new SwerveDriveWheelPositions(swerveModulePositions);
    }

    private void updateHardware() {
        gyro.update();

        for (SwerveModule currentModule : swerveModules)
            currentModule.update();

        phoenix6SignalThread.updateLatestTimestamps();
    }

    private void configurePathPlanner() {
        AutoBuilder.configureHolonomic(
                () -> RobotContainer.POSE_ESTIMATOR.getCurrentPose(),
                (pose) -> {
                },
                this::getSelfRelativeVelocity,
                this::selfRelativeDrive,
                SwerveConstants.HOLONOMIC_PATH_FOLLOWER_CONFIG,
                Mirrorable::isRedAlliance,
                this
        );
        PathfindingCommand.warmupCommand().schedule();
    }

    private boolean atTranslationPosition(double currentTranslationPosition, double targetTranslationPosition, double currentTranslationVelocity) {
        return Math.abs(currentTranslationPosition - targetTranslationPosition) < SwerveConstants.TRANSLATION_TOLERANCE_METERS &&
                Math.abs(currentTranslationVelocity) < SwerveConstants.TRANSLATION_VELOCITY_TOLERANCE;
    }

    private double calculateProfiledAngleSpeedToTargetAngle(MirrorableRotation2d targetAngle) {
        final Rotation2d currentAngle = RobotContainer.POSE_ESTIMATOR.getCurrentPose().getRotation();
        return Units.degreesToRadians(SwerveConstants.PROFILED_ROTATION_PID_CONTROLLER.calculate(currentAngle.getDegrees(), targetAngle.get().getDegrees()));
    }

    private ChassisSpeeds selfRelativeSpeedsFromFieldRelativePowers(double xPower, double yPower, double thetaPower) {
        final ChassisSpeeds fieldRelativeSpeeds = powersToSpeeds(xPower, yPower, thetaPower);
        return fieldRelativeSpeedsToSelfRelativeSpeeds(fieldRelativeSpeeds);
    }

    private ChassisSpeeds fieldRelativeSpeedsToSelfRelativeSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
        return ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeSpeeds, getDriveRelativeAngle());
    }

    private Rotation2d getDriveRelativeAngle() {
        final Rotation2d currentAngle = RobotContainer.POSE_ESTIMATOR.getCurrentPose().getRotation();
        return Mirrorable.isRedAlliance() ? currentAngle.rotateBy(Rotation2d.fromDegrees(180)) : currentAngle;
    }

    private ChassisSpeeds powersToSpeeds(double xPower, double yPower, double thetaPower) {
        return new ChassisSpeeds(
                xPower * SwerveConstants.MAX_SPEED_METERS_PER_SECOND,
                yPower * SwerveConstants.MAX_SPEED_METERS_PER_SECOND,
                Math.pow(thetaPower, 2) * Math.signum(thetaPower) * SwerveConstants.MAX_ROTATIONAL_SPEED_RADIANS_PER_SECOND
        );
    }

    private void updateNetworkTables() {
        Logger.recordOutput("Swerve/Velocity/Rot", getSelfRelativeVelocity().omegaRadiansPerSecond);
        Logger.recordOutput("Swerve/Velocity/X", getSelfRelativeVelocity().vxMetersPerSecond);
        Logger.recordOutput("Swerve/Velocity/Y", getSelfRelativeVelocity().vyMetersPerSecond);
    }

    @AutoLogOutput(key = "Swerve/CurrentStates")
    private SwerveModuleState[] getModuleStates() {
        final SwerveModuleState[] states = new SwerveModuleState[swerveModules.length];

        for (int i = 0; i < swerveModules.length; i++)
            states[i] = swerveModules[i].getCurrentState();

        return states;
    }

    @AutoLogOutput(key = "Swerve/TargetStates")
    @SuppressWarnings("unused")
    private SwerveModuleState[] getTargetStates() {
        final SwerveModuleState[] states = new SwerveModuleState[swerveModules.length];

        for (int i = 0; i < swerveModules.length; i++)
            states[i] = swerveModules[i].getTargetState();

        return states;
    }

    /**
     * Returns whether the given chassis speeds are considered to be "still" by the swerve neutral deadband.
     *
     * @param chassisSpeeds the chassis speeds to check
     * @return true if the chassis speeds are considered to be "still"
     */
    private boolean isStill(ChassisSpeeds chassisSpeeds) {
        return Math.abs(chassisSpeeds.vxMetersPerSecond) <= SwerveConstants.DRIVE_NEUTRAL_DEADBAND &&
                Math.abs(chassisSpeeds.vyMetersPerSecond) <= SwerveConstants.DRIVE_NEUTRAL_DEADBAND &&
                Math.abs(chassisSpeeds.omegaRadiansPerSecond) <= SwerveConstants.ROTATION_NEUTRAL_DEADBAND;
    }
}
