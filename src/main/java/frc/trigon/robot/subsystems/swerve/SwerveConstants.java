package frc.trigon.robot.subsystems.swerve;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.trigon.robot.RobotContainer;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2Gyro;
import frc.trigon.robot.hardware.phoenix6.pigeon2.Pigeon2Signal;
import frc.trigon.robot.poseestimation.poseestimator.PoseEstimatorConstants;
import frc.trigon.robot.utilities.Conversions;

import java.util.function.DoubleSupplier;

public abstract class SwerveConstants {
    private static final int PIGEON_ID = 0;
    static final Pigeon2Gyro GYRO = new Pigeon2Gyro(SwerveConstants.PIGEON_ID, "SwerveGyro", RobotConstants.CANIVORE_NAME);
    private static final double
            GYRO_MOUNT_POSITION_YAW = 0,
            GYRO_MOUNT_POSITION_PITCH = 0,
            GYRO_MOUNT_POSITION_ROLL = 0;
    private static final double
            FRONT_LEFT_STEER_ENCODER_OFFSET = -Conversions.degreesToRotations(225.263672 - 360),
            FRONT_RIGHT_STEER_ENCODER_OFFSET = -Conversions.degreesToRotations(-256.904297 + 360),
            REAR_LEFT_STEER_ENCODER_OFFSET = -Conversions.degreesToRotations(108.369141),
            REAR_RIGHT_STEER_ENCODER_OFFSET = -Conversions.degreesToRotations(-36.035156);
    private static final int
            FRONT_LEFT_ID = 0,
            FRONT_RIGHT_ID = 1,
            REAR_LEFT_ID = 2,
            REAR_RIGHT_ID = 3;
    static final SwerveModule[] SWERVE_MODULES = {
            new SwerveModule(FRONT_LEFT_ID, FRONT_LEFT_STEER_ENCODER_OFFSET),
            new SwerveModule(FRONT_RIGHT_ID, FRONT_RIGHT_STEER_ENCODER_OFFSET),
            new SwerveModule(REAR_LEFT_ID, REAR_LEFT_STEER_ENCODER_OFFSET),
            new SwerveModule(REAR_RIGHT_ID, REAR_RIGHT_STEER_ENCODER_OFFSET)
    };

    private static final DoubleSupplier SIMULATION_YAW_VELOCITY_SUPPLIER = () -> RobotContainer.SWERVE.getSelfRelativeVelocity().omegaRadiansPerSecond;

    private static final double
            MODULE_X_DISTANCE_FROM_CENTER = 0.6457 / 2,
            MODULE_Y_DISTANCE_FROM_CENTER = 0.5357 / 2;
    private static final Translation2d[] LOCATIONS = {
            new Translation2d(MODULE_X_DISTANCE_FROM_CENTER, MODULE_Y_DISTANCE_FROM_CENTER),
            new Translation2d(MODULE_X_DISTANCE_FROM_CENTER, -MODULE_Y_DISTANCE_FROM_CENTER),
            new Translation2d(-MODULE_X_DISTANCE_FROM_CENTER, MODULE_Y_DISTANCE_FROM_CENTER),
            new Translation2d(-MODULE_X_DISTANCE_FROM_CENTER, -MODULE_Y_DISTANCE_FROM_CENTER)
    };
    public static final SwerveDriveKinematics KINEMATICS = new SwerveDriveKinematics(LOCATIONS);
    public static final double DRIVE_RADIUS_METERS = Math.hypot(
            MODULE_X_DISTANCE_FROM_CENTER, MODULE_Y_DISTANCE_FROM_CENTER
    );

    static final double
            TRANSLATION_TOLERANCE_METERS = 0.05,
            ROTATION_TOLERANCE_DEGREES = 2,
            TRANSLATION_VELOCITY_TOLERANCE = 0.05,
            ROTATION_VELOCITY_TOLERANCE = 0.3;
    static final double
            DRIVE_NEUTRAL_DEADBAND = 0.2,
            ROTATION_NEUTRAL_DEADBAND = 0.2;
    static final double
            MAX_SPEED_METERS_PER_SECOND = RobotConstants.IS_SIMULATION ? 4.9 : 4.04502,
            MAX_ROTATIONAL_SPEED_RADIANS_PER_SECOND = RobotConstants.IS_SIMULATION ? 12.03 : 12.03;

    private static final PIDConstants
            TRANSLATION_PID_CONSTANTS = RobotConstants.IS_SIMULATION ?
            new PIDConstants(5, 0, 0) :
            new PIDConstants(5, 0, 0),
            PROFILED_ROTATION_PID_CONSTANTS = RobotConstants.IS_SIMULATION ?
                    new PIDConstants(8, 0, 0) :
                    new PIDConstants(5, 0, 0),
            AUTO_TRANSLATION_PID_CONSTANTS = RobotConstants.IS_SIMULATION ?
                    new PIDConstants(9, 0, 0) :
                    new PIDConstants(6.5, 0, 0),
            AUTO_ROTATION_PID_CONSTANTS = RobotConstants.IS_SIMULATION ?
                    new PIDConstants(8.9, 0, 0) :
                    new PIDConstants(3, 0, 0);
    private static final double
            MAX_ROTATION_VELOCITY = RobotConstants.IS_SIMULATION ? 720 : 720,
            MAX_ROTATION_ACCELERATION = RobotConstants.IS_SIMULATION ? 720 : 720;
    private static final TrapezoidProfile.Constraints ROTATION_CONSTRAINTS = new TrapezoidProfile.Constraints(
            MAX_ROTATION_VELOCITY,
            MAX_ROTATION_ACCELERATION
    );
    static final ProfiledPIDController PROFILED_ROTATION_PID_CONTROLLER = new ProfiledPIDController(
            PROFILED_ROTATION_PID_CONSTANTS.kP,
            PROFILED_ROTATION_PID_CONSTANTS.kI,
            PROFILED_ROTATION_PID_CONSTANTS.kD,
            ROTATION_CONSTRAINTS
    );
    static final PIDController TRANSLATION_PID_CONTROLLER = new PIDController(
            TRANSLATION_PID_CONSTANTS.kP,
            TRANSLATION_PID_CONSTANTS.kI,
            TRANSLATION_PID_CONSTANTS.kD
    );

    private static final ReplanningConfig REPLANNING_CONFIG = new ReplanningConfig(true, false);
    static final HolonomicPathFollowerConfig HOLONOMIC_PATH_FOLLOWER_CONFIG = new HolonomicPathFollowerConfig(
            AUTO_TRANSLATION_PID_CONSTANTS,
            AUTO_ROTATION_PID_CONSTANTS,
            MAX_ROTATIONAL_SPEED_RADIANS_PER_SECOND,
            SwerveConstants.DRIVE_RADIUS_METERS,
            REPLANNING_CONFIG
    );

    static {
        final Pigeon2Configuration config = new Pigeon2Configuration();
        config.MountPose.MountPoseYaw = GYRO_MOUNT_POSITION_YAW;
        config.MountPose.MountPosePitch = GYRO_MOUNT_POSITION_PITCH;
        config.MountPose.MountPoseRoll = GYRO_MOUNT_POSITION_ROLL;
        GYRO.applyConfiguration(config);
        GYRO.setSimulationYawVelocitySupplier(SIMULATION_YAW_VELOCITY_SUPPLIER);

        GYRO.registerThreadedSignal(Pigeon2Signal.YAW, Pigeon2Signal.ANGULAR_VELOCITY_Z_WORLD, PoseEstimatorConstants.ODOMETRY_FREQUENCY_HERTZ);
    }
}
