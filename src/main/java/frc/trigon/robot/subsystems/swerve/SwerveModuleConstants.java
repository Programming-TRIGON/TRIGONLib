package frc.trigon.robot.subsystems.swerve;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.*;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.simulation.SimpleMotorSimulation;

public class SwerveModuleConstants {
    private static final double
            DRIVE_GEAR_RATIO = 6.12,
            STEER_GEAR_RATIO = 12.8;
    private static final double
            DRIVE_OPEN_LOOP_RAMP_RATE = RobotConstants.IS_SIMULATION ? 0.1 : 0.1,
            DRIVE_CLOSED_LOOP_RAMP_RATE = RobotConstants.IS_SIMULATION ? 0.1 : 0.1;
    private static final InvertedValue
            DRIVE_MOTOR_INVERTED_VALUE = InvertedValue.CounterClockwise_Positive,
            STEER_MOTOR_INVERTED_VALUE = InvertedValue.CounterClockwise_Positive;
    private static final SensorDirectionValue STEER_ENCODER_DIRECTION = SensorDirectionValue.CounterClockwise_Positive;
    private static final AbsoluteSensorRangeValue STEER_ENCODER_RANGE = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
    private static final NeutralModeValue
            DRIVE_MOTOR_NEUTRAL_MODE_VALUE = NeutralModeValue.Brake,
            STEER_MOTOR_NEUTRAL_MODE_VALUE = NeutralModeValue.Brake;
    private static final double
            DRIVE_SLIP_CURRENT = RobotConstants.IS_SIMULATION ? 1000 : 100,
            STEER_CURRENT_LIMIT = RobotConstants.IS_SIMULATION ? 1000 : 50;
    private static final double
            STEER_MOTOR_P = RobotConstants.IS_SIMULATION ? 75 : 75,
            STEER_MOTOR_I = 0,
            STEER_MOTOR_D = 0;
    private static final double
            DRIVE_MOTOR_P = RobotConstants.IS_SIMULATION ? 50 : 50,
            DRIVE_MOTOR_I = 0,
            DRIVE_MOTOR_D = 0;
    static final boolean ENABLE_FOC = true;
    static final TalonFXConfiguration
            DRIVE_MOTOR_CONFIGURATION = generateDriveConfiguration(),
            STEER_MOTOR_CONFIGURATION = generateSteerConfiguration();
    static final CANcoderConfiguration STEER_ENCODER_CONFIGURATION = generateSteerEncoderConfiguration();

    private static final double
            DRIVE_MOMENT_OF_INERTIA = 0.03,
            STEER_MOMENT_OF_INERTIA = 0.003;
    private static final int
            DRIVE_MOTOR_AMOUNT = 1,
            STEER_MOTOR_AMOUNT = 1;
    private static final DCMotor
            DRIVE_MOTOR_GEARBOX = DCMotor.getKrakenX60Foc(DRIVE_MOTOR_AMOUNT),
            STEER_MOTOR_GEARBOX = DCMotor.getFalcon500Foc(STEER_MOTOR_AMOUNT);

    static final double WHEEL_DIAMETER_METERS = RobotConstants.IS_SIMULATION ? 0.1016 : 0.049149 * 2;
    static final double VOLTAGE_COMPENSATION_SATURATION = 12;

    static SimpleMotorSimulation createDriveSimulation() {
        return new SimpleMotorSimulation(DRIVE_MOTOR_GEARBOX, DRIVE_GEAR_RATIO, DRIVE_MOMENT_OF_INERTIA);
    }

    static SimpleMotorSimulation createSteerSimulation() {
        return new SimpleMotorSimulation(STEER_MOTOR_GEARBOX, STEER_GEAR_RATIO, STEER_MOMENT_OF_INERTIA);
    }

    private static TalonFXConfiguration generateDriveConfiguration() {
        final TalonFXConfiguration config = new TalonFXConfiguration();

        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;

        config.MotorOutput.Inverted = DRIVE_MOTOR_INVERTED_VALUE;
        config.MotorOutput.NeutralMode = DRIVE_MOTOR_NEUTRAL_MODE_VALUE;
        config.Feedback.SensorToMechanismRatio = DRIVE_GEAR_RATIO;

        config.TorqueCurrent.PeakForwardTorqueCurrent = DRIVE_SLIP_CURRENT;
        config.TorqueCurrent.PeakReverseTorqueCurrent = -DRIVE_SLIP_CURRENT;
        config.CurrentLimits.StatorCurrentLimit = DRIVE_SLIP_CURRENT;
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.ClosedLoopRamps.TorqueClosedLoopRampPeriod = DRIVE_CLOSED_LOOP_RAMP_RATE;
        config.OpenLoopRamps.VoltageOpenLoopRampPeriod = DRIVE_OPEN_LOOP_RAMP_RATE;

        config.Slot0.kP = DRIVE_MOTOR_P;
        config.Slot0.kI = DRIVE_MOTOR_I;
        config.Slot0.kD = DRIVE_MOTOR_D;

        return config;
    }

    private static TalonFXConfiguration generateSteerConfiguration() {
        final TalonFXConfiguration config = new TalonFXConfiguration();

        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;

        config.MotorOutput.Inverted = STEER_MOTOR_INVERTED_VALUE;
        config.MotorOutput.NeutralMode = STEER_MOTOR_NEUTRAL_MODE_VALUE;
        config.CurrentLimits.StatorCurrentLimit = STEER_CURRENT_LIMIT;
        config.CurrentLimits.StatorCurrentLimitEnable = true;

        config.Feedback.RotorToSensorRatio = STEER_GEAR_RATIO;
        config.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;

        config.Slot0.kP = STEER_MOTOR_P;
        config.Slot0.kI = STEER_MOTOR_I;
        config.Slot0.kD = STEER_MOTOR_D;
        config.ClosedLoopGeneral.ContinuousWrap = true;

        return config;
    }

    private static CANcoderConfiguration generateSteerEncoderConfiguration() {
        final CANcoderConfiguration config = new CANcoderConfiguration();

        config.MagnetSensor.AbsoluteSensorRange = STEER_ENCODER_RANGE;
        config.MagnetSensor.SensorDirection = STEER_ENCODER_DIRECTION;

        return config;
    }
}
