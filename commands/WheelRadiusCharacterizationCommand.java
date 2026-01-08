// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.trigon.lib.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import java.util.Arrays;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * A command for characterizing the wheel radius of each module of a swerve drive system by calculating the radius of each drive wheel based on rotational data and robot yaw during operation.
 */
public class WheelRadiusCharacterizationCommand extends Command {
    private static final LoggedNetworkNumber CHARACTERIZATION_SPEED = new LoggedNetworkNumber("/SmartDashboard/WheelRadiusCharacterization/SpeedRadiansPerSecond", 1);
    private static final LoggedNetworkNumber ROTATION_RATE_LIMIT = new LoggedNetworkNumber("/SmartDashboard/WheelRadiusCharacterization/RotationRateLimit", 1);

    private final double[]
            wheelDistancesFromCenterMeters,
            driveWheelRadii;
    private final Supplier<double[]> wheelPositionsRadiansSupplier;
    private final DoubleSupplier gyroYawRadiansSupplier;
    private final DoubleConsumer runWheelRadiusCharacterization;

    private SlewRateLimiter rotationSlewRateLimiter = new SlewRateLimiter(ROTATION_RATE_LIMIT.get());
    private double startingGyroYawRadians;
    private double accumulatedGyroYawRadians;
    private double[] startingWheelPositions;

    /**
     * Creates a new WheelRadiusCharacterizationCommand.
     *
     * @param wheelDistanceFromCenterMeters  an array of the distances of each wheel from the center of the robot in meters
     * @param wheelPositionsRadiansSupplier  a supplier that returns the current position of each wheel in radians
     * @param gyroYawRadiansSupplier         a supplier that returns the current yaw of the robot in radians
     * @param runWheelRadiusCharacterization a consumer that runs the swerve drive with a given speed
     * @param requirement                    the subsystem that this command requires
     */
    public WheelRadiusCharacterizationCommand(Translation2d wheelDistanceFromCenterMeters,
                                              Supplier<double[]> wheelPositionsRadiansSupplier,
                                              DoubleSupplier gyroYawRadiansSupplier,
                                              DoubleConsumer runWheelRadiusCharacterization,
                                              SubsystemBase requirement) {
        this(
                new Translation2d[]{wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters},
                wheelPositionsRadiansSupplier,
                gyroYawRadiansSupplier,
                runWheelRadiusCharacterization,
                requirement
        );
    }

    /**
     * Creates a new WheelRadiusCharacterizationCommand.
     *
     * @param wheelDistancesFromCenterMeters an array of the distances of each wheel from the center of the robot in meters
     * @param wheelPositionsRadiansSupplier  a supplier that returns the current position of each wheel in radians
     * @param gyroYawRadiansSupplier         a supplier that returns the current yaw of the robot in radians
     * @param runWheelRadiusCharacterization a consumer that runs the swerve drive with a given speed
     * @param requirement                    the subsystem that this command requires
     */
    public WheelRadiusCharacterizationCommand(
            Translation2d[] wheelDistancesFromCenterMeters,
            Supplier<double[]> wheelPositionsRadiansSupplier,
            DoubleSupplier gyroYawRadiansSupplier,
            DoubleConsumer runWheelRadiusCharacterization,
            SubsystemBase requirement) {
        this(
                Arrays.stream(wheelDistancesFromCenterMeters).mapToDouble(Translation2d::getNorm).toArray(),
                wheelPositionsRadiansSupplier,
                gyroYawRadiansSupplier,
                runWheelRadiusCharacterization,
                requirement
        );
    }

    /**
     * Creates a new WheelRadiusCharacterizationCommand.
     *
     * @param wheelDistanceFromCenterMeters  the distance of the wheels from the center of the robot in meters
     * @param wheelPositionsRadiansSupplier  a supplier that returns the current position of each wheel in radians
     * @param gyroYawRadiansSupplier         a supplier that returns the current yaw of the robot in radians
     * @param runWheelRadiusCharacterization a consumer that runs the swerve drive with a given speed
     * @param requirement                    the subsystem that this command requires
     */
    public WheelRadiusCharacterizationCommand(
            double wheelDistanceFromCenterMeters,
            Supplier<double[]> wheelPositionsRadiansSupplier,
            DoubleSupplier gyroYawRadiansSupplier,
            DoubleConsumer runWheelRadiusCharacterization,
            SubsystemBase requirement) {
        this(
                new double[]{wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters, wheelDistanceFromCenterMeters},
                wheelPositionsRadiansSupplier,
                gyroYawRadiansSupplier,
                runWheelRadiusCharacterization,
                requirement
        );
    }

    /**
     * Creates a new WheelRadiusCharacterizationCommand.
     *
     * @param wheelDistancesFromCenterMeters an array of the distances of each wheel from the center of the robot in meters
     * @param wheelPositionsRadiansSupplier  a supplier that returns the current position of each wheel in radians
     * @param gyroYawRadiansSupplier         a supplier that returns the current yaw of the robot in radians
     * @param runWheelRadiusCharacterization a consumer that runs the swerve drive with a given speed
     * @param requirement                    the subsystem that this command requires
     */
    public WheelRadiusCharacterizationCommand(double[] wheelDistancesFromCenterMeters,
                                              Supplier<double[]> wheelPositionsRadiansSupplier,
                                              DoubleSupplier gyroYawRadiansSupplier,
                                              DoubleConsumer runWheelRadiusCharacterization,
                                              SubsystemBase requirement) {
        this.wheelDistancesFromCenterMeters = wheelDistancesFromCenterMeters;
        this.wheelPositionsRadiansSupplier = wheelPositionsRadiansSupplier;
        this.gyroYawRadiansSupplier = gyroYawRadiansSupplier;
        this.runWheelRadiusCharacterization = runWheelRadiusCharacterization;
        this.driveWheelRadii = new double[wheelDistancesFromCenterMeters.length];
        addRequirements(requirement);
    }

    @Override
    public void initialize() {
        configureStartingStats();
    }

    @Override
    public void execute() {
        driveMotors();

        accumulatedGyroYawRadians = getAccumulatedGyroYaw();
        calculateDriveWheelRadius();

        Logger.recordOutput("WheelRadiusCharacterization/AccumulatedGyroYawRadians", accumulatedGyroYawRadians);
        logWheelRadii();
    }

    @Override
    public void end(boolean interrupted) {
        printResults();
    }

    private void configureStartingStats() {
        startingGyroYawRadians = gyroYawRadiansSupplier.getAsDouble();
        startingWheelPositions = wheelPositionsRadiansSupplier.get();
        accumulatedGyroYawRadians = 0.0;

        rotationSlewRateLimiter = new SlewRateLimiter(ROTATION_RATE_LIMIT.get());
        rotationSlewRateLimiter.reset(0);
    }

    private void driveMotors() {
        runWheelRadiusCharacterization.accept(rotationSlewRateLimiter.calculate(CHARACTERIZATION_SPEED.get()));
    }

    private double getAccumulatedGyroYaw() {
        return Math.abs(startingGyroYawRadians - gyroYawRadiansSupplier.getAsDouble());
    }

    private void calculateDriveWheelRadius() {
        final double[] wheelPositionsRadians = wheelPositionsRadiansSupplier.get();

        for (int i = 0; i < 4; i++) {
            final double accumulatedWheelRadians = Math.abs(wheelPositionsRadians[i] - startingWheelPositions[i]);
            driveWheelRadii[i] = (accumulatedGyroYawRadians * wheelDistancesFromCenterMeters[i]) / accumulatedWheelRadians;
            Logger.recordOutput("RadiusCharacterization/AccumulatedWheelRadians" + i, accumulatedWheelRadians);
        }
    }

    private void printResults() {
        if (accumulatedGyroYawRadians <= Math.PI * 2.0) {
            System.out.println("Not enough data for characterization");
            return;
        }
        for (int i = 0; i < driveWheelRadii.length; i++)
            System.out.println("Drive Wheel Radius for Module " + i + ": " + driveWheelRadii[i] + " meters");
        System.out.println("Average Drive Wheel Radius: " + Arrays.stream(driveWheelRadii).average().getAsDouble() + " meters");
    }

    private void logWheelRadii() {
        for (int i = 0; i < driveWheelRadii.length; i++)
            Logger.recordOutput("RadiusCharacterization/DriveWheelRadiusModule" + i, driveWheelRadii[i]);
        Logger.recordOutput("RadiusCharacterization/AverageDriveWheelRadius", Arrays.stream(driveWheelRadii).average().getAsDouble());
    }
}