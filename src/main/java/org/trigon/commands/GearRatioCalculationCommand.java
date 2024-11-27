package org.trigon.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * A command that calculates and logs the gear ratio of a subsystem by comparing the distance traveled by a rotor and an encoder.
 */
public class GearRatioCalculationCommand extends Command {
    private final DoubleSupplier rotorPositionSupplier;
    private final DoubleSupplier encoderPositionSupplier;
    private final DoubleConsumer runGearRatioCalculation;
    private final String subsystemName;
    private final double backlashAccountabilityTimeSeconds;

    private final LoggedDashboardNumber movementVoltage;

    private double startingRotorPosition;
    private double startingEncoderPosition;
    private double gearRatio;
    private double startTime;
    private boolean hasSetStartingPositions = false;

    /**
     * Creates a new GearRatioCalculationCommand.
     *
     * @param rotorPositionSupplier             a supplier that returns the current position of the rotor
     * @param encoderPositionSupplier           a supplier that returns the current position of the encoder
     * @param runGearRatioCalculation           a consumer that runs the gear ratio calculation with a given voltage
     * @param backlashAccountabilityTimeSeconds the time to wait before setting the starting positions in order to account for backlash
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
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        runGearRatioCalculation();
        if (Timer.getFPGATimestamp() - startTime > backlashAccountabilityTimeSeconds && !hasSetStartingPositions)
            setStartingPositions();
        log();
    }

    @Override
    public void end(boolean interrupted) {
        gearRatio = calculateGearRatio();
        log();
        printResult();
    }

    private void setStartingPositions() {
        startingRotorPosition = rotorPositionSupplier.getAsDouble();
        startingEncoderPosition = encoderPositionSupplier.getAsDouble();
        hasSetStartingPositions = true;
    }

    private void runGearRatioCalculation() {
        runGearRatioCalculation.accept(movementVoltage.get());
    }

    private void log() {
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