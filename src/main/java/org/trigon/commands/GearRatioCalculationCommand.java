package org.trigon.commands;

import edu.wpi.first.wpilibj2.command.*;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * A command that calculates and logs the gear ratio of a subsystem by comparing the positions of a rotor and an encoder..
 */
public class GearRatioCalculationCommand extends SequentialCommandGroup {
    private final DoubleSupplier rotorPositionSupplier;
    private final DoubleSupplier encoderPositionSupplier;
    private final DoubleConsumer runGearRatioCalculation;
    private final String subsystemName;
    private final double backlashAccountabilityTimeSeconds;

    private final LoggedDashboardNumber movementVoltage;

    private double startingRotorPosition;
    private double startingEncoderPosition;
    private double gearRatio;

    /**
     * Creates a new GearRatioCalculationCommand.
     *
     * @param rotorPositionSupplier             a supplier that returns the current position of the rotor
     * @param encoderPositionSupplier           a supplier that returns the current position of the encoder
     * @param runGearRatioCalculation           a consumer that runs the gear ratio calculation with a given voltage
     * @param backlashAccountabilityTimeSeconds the time to wait before logging the gear ratio in order to account for backlash
     * @param requirement                       the subsystem that this command requires
     */
    public GearRatioCalculationCommand(
            DoubleSupplier rotorPositionSupplier,
            DoubleSupplier encoderPositionSupplier,
            DoubleConsumer runGearRatioCalculation,
            double backlashAccountabilityTimeSeconds,
            SubsystemBase requirement) {
        this.rotorPositionSupplier = rotorPositionSupplier;
        this.encoderPositionSupplier = encoderPositionSupplier;
        this.runGearRatioCalculation = runGearRatioCalculation;
        this.subsystemName = requirement.getName();
        this.backlashAccountabilityTimeSeconds = backlashAccountabilityTimeSeconds;

        this.movementVoltage = new LoggedDashboardNumber("GearRatioCalculation/" + this.subsystemName + "/Voltage", 1);

        addRequirements(requirement);
        addCommands(
                getGearRatioCalculationCommand(),
                getBacklashAccountabilityCommand(),
                getLogGearRatioCommand()
        );
    }

    private Command getBacklashAccountabilityCommand() {
        return new WaitCommand(backlashAccountabilityTimeSeconds);
    }

    private Command getGearRatioCalculationCommand() {
        return new InitExecuteCommand(
                this::getStartingPositions,
                this::runGearRatioCalculation
        );
    }

    private Command getLogGearRatioCommand() {
        return new InstantCommand(
                () -> {
                    gearRatio = calculateGearRatio();
                    logGearRatio();
                    printResult();
                }
        );
    }

    private void getStartingPositions() {
        startingRotorPosition = rotorPositionSupplier.getAsDouble();
        startingEncoderPosition = encoderPositionSupplier.getAsDouble();
    }

    private void runGearRatioCalculation() {
        runGearRatioCalculation.accept(movementVoltage.get());
    }

    private void logGearRatio() {
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/RotorDistance", getRotorDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/EncoderDistance", getEncoderDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/GearRatio", gearRatio);
    }

    private double calculateGearRatio() {
        final double currentRotorPosition = getRotorDistance();
        final double currentEncoderPosition = getEncoderDistance();
        return currentRotorPosition / currentEncoderPosition;
    }

    private double getRotorDistance() {
        return startingRotorPosition - rotorPositionSupplier.getAsDouble();
    }

    private double getEncoderDistance() {
        return startingEncoderPosition - encoderPositionSupplier.getAsDouble();
    }

    private void printResult() {
        System.out.println(subsystemName + " Gear Ratio: " + gearRatio);
    }
}