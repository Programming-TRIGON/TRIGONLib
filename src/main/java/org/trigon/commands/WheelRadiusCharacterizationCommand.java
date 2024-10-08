// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package org.trigon.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardBoolean;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.Arrays;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class WheelRadiusCharacterizationCommand extends Command {
    private static final LoggedDashboardNumber CHARACTERIZATION_SPEED = new LoggedDashboardNumber("WheelRadiusCharacterization/SpeedRadiansPerSecond", 1);
    private static final LoggedDashboardNumber ROTATION_RATE_LIMIT = new LoggedDashboardNumber("WheelRadiusCharacterization/RotationRateLimit", 1);
    private static final LoggedDashboardBoolean SHOULD_MOVE_CLOCKWISE = new LoggedDashboardBoolean("WheelRadiusCharacterization/ShouldMoveClockwise", false);

    private final double[] wheelDistancesFromCenterMeters;
    private final Supplier<double[]> wheelPositionsRadiansSupplier;
    private final DoubleSupplier gyroYawRadiansSupplier;
    private final DoubleConsumer runWheelRadiusCharacterization;

    private SlewRateLimiter rotationSlewRateLimiter = new SlewRateLimiter(ROTATION_RATE_LIMIT.get());
    private double startingGyroYawRadians;
    private double accumulatedGyroYawRadians;
    private double[] startingWheelPositions;
    private double driveWheelsRadius;

    public WheelRadiusCharacterizationCommand(Translation2d[] wheelDistancesFromCenterMeters, Supplier<double[]> wheelPositionsRadiansSupplier,
                                              DoubleSupplier gyroYawRadiansSupplier, DoubleConsumer runWheelRadiusCharacterization, SubsystemBase requirement
    ) {
        this.wheelDistancesFromCenterMeters = Arrays.stream(wheelDistancesFromCenterMeters).mapToDouble(Translation2d::getNorm).toArray();
        this.wheelPositionsRadiansSupplier = wheelPositionsRadiansSupplier;
        this.gyroYawRadiansSupplier = gyroYawRadiansSupplier;
        this.runWheelRadiusCharacterization = runWheelRadiusCharacterization;
        addRequirements(requirement);
    }

    public WheelRadiusCharacterizationCommand(double[] wheelDistancesFromCenterMeters, Supplier<double[]> wheelPositionsRadiansSupplier,
                                              DoubleSupplier gyroYawRadiansSupplier, DoubleConsumer runWheelRadiusCharacterization, SubsystemBase requirement
    ) {
        this.wheelDistancesFromCenterMeters = wheelDistancesFromCenterMeters;
        this.wheelPositionsRadiansSupplier = wheelPositionsRadiansSupplier;
        this.gyroYawRadiansSupplier = gyroYawRadiansSupplier;
        this.runWheelRadiusCharacterization = runWheelRadiusCharacterization;
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
        driveWheelsRadius = calculateDriveWheelRadius();

        Logger.recordOutput("WheelRadiusCharacterization/AccumulatedGyroYawRadians", accumulatedGyroYawRadians);
        Logger.recordOutput("RadiusCharacterization/DriveWheelRadius", driveWheelsRadius);
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
        runWheelRadiusCharacterization.accept(rotationSlewRateLimiter.calculate(getRotationDirection() * CHARACTERIZATION_SPEED.get()));
    }

    private double getAccumulatedGyroYaw() {
        return Math.abs(startingGyroYawRadians - gyroYawRadiansSupplier.getAsDouble());
    }

    private double calculateDriveWheelRadius() {
        driveWheelsRadius = 0;
        final double[] wheelPositionsRadians = wheelPositionsRadiansSupplier.get();

        for (int i = 0; i < 4; i++) {
            final double accumulatedWheelRadians = Math.abs(wheelPositionsRadians[i] - startingWheelPositions[i]);
            driveWheelsRadius += (accumulatedGyroYawRadians * wheelDistancesFromCenterMeters[i]) / accumulatedWheelRadians;
            Logger.recordOutput("RadiusCharacterization/AccumulatedWheelRadians" + i, accumulatedWheelRadians);
        }

        return driveWheelsRadius /= 4;
    }

    private void printResults() {
        if (accumulatedGyroYawRadians <= Math.PI * 2.0)
            System.out.println("Not enough data for characterization");
        else
            System.out.println("Drive Wheel Radius: " + driveWheelsRadius + " meters");
    }

    private int getRotationDirection() {
        return SHOULD_MOVE_CLOCKWISE.get() ? -1 : 1;
    }
}