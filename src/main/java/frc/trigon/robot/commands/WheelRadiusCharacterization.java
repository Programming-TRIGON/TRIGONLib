// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.trigon.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.trigon.robot.RobotContainer;
import frc.trigon.robot.subsystems.swerve.SwerveConstants;
import frc.trigon.robot.utilities.Conversions;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.Arrays;
import java.util.function.DoubleSupplier;

public class WheelRadiusCharacterization extends Command {
    private static final LoggedDashboardNumber characterizationSpeed =
            new LoggedDashboardNumber("WheelRadiusCharacterization/SpeedRadsPerSec", 0.1);
    private static final double driveRadius = SwerveConstants.DRIVE_RADIUS_METERS;
    private static final DoubleSupplier gyroYawRadsSupplier =
            () -> RobotContainer.SWERVE.getHeading().getRadians();

    public enum Direction {
        CLOCKWISE(-1),
        COUNTER_CLOCKWISE(1);

        Direction(int value) {
            this.value = value;
        }

        private final int value;
    }

    private final Direction omegaDirection;
    private final SlewRateLimiter omegaLimiter = new SlewRateLimiter(1.0);

    private double lastGyroYawRads = 0.0;
    private double accumGyroYawRads = 0.0;

    private double[] startWheelPositions;

    private double currentEffectiveWheelRadius = 0.0;

    public WheelRadiusCharacterization(Direction omegaDirection) {
        this.omegaDirection = omegaDirection;
        addRequirements(RobotContainer.SWERVE);
    }

    @Override
    public void initialize() {
        // Reset
        lastGyroYawRads = gyroYawRadsSupplier.getAsDouble();
        accumGyroYawRads = 0.0;

        startWheelPositions = getWheelRadiusCharacterizationPosition();

        omegaLimiter.reset(0);
    }

    @Override
    public void execute() {
        // Run drive at velocity
        RobotContainer.SWERVE.runWheelRadiusCharacterization(
                omegaLimiter.calculate(omegaDirection.value * characterizationSpeed.get()));

        // Get yaw and wheel positions
        accumGyroYawRads += MathUtil.angleModulus(gyroYawRadsSupplier.getAsDouble() - lastGyroYawRads);
        lastGyroYawRads = gyroYawRadsSupplier.getAsDouble();
        double averageWheelPosition = 0.0;
        double[] wheelPositions = getWheelRadiusCharacterizationPosition();
        for (int i = 0; i < 4; i++) {
            averageWheelPosition += Math.abs(wheelPositions[i] - startWheelPositions[i]);
        }
        averageWheelPosition /= 4.0;

        currentEffectiveWheelRadius = (accumGyroYawRads * driveRadius) / averageWheelPosition;
        Logger.recordOutput("Drive/RadiusCharacterization/DrivePosition", averageWheelPosition);
        Logger.recordOutput("Drive/RadiusCharacterization/AccumGyroYawRads", accumGyroYawRads);
        Logger.recordOutput(
                "Drive/RadiusCharacterization/CurrentWheelRadiusMeters",
                currentEffectiveWheelRadius);
    }

    @Override
    public void end(boolean interrupted) {
        if (accumGyroYawRads <= Math.PI * 2.0) {
            System.out.println("Not enough data for characterization");
        } else {
            System.out.println(
                    "Effective Wheel Radius: "
                            + currentEffectiveWheelRadius
                            + " meters");
        }
    }

    private double[] getWheelRadiusCharacterizationPosition() {
        return Arrays.stream(RobotContainer.SWERVE.getWheelPositions()).mapToDouble((pos) -> Units.rotationsToRadians(Conversions.distanceToRotations(pos.distanceMeters, 0.1016))).toArray();
    }
}