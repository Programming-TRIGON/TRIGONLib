package org.trigon.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardBoolean;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class GearRatioCalculationCommand extends Command {
    private final DoubleSupplier rotorPositionSupplier;
    private final DoubleSupplier encoderPositionSupplier;
    private final DoubleConsumer runGearRatioCalculation;
    private final String subsystemName;

    private final LoggedDashboardNumber movementVoltage;
    private final LoggedDashboardBoolean shouldMoveClockwise;

    private double startingRotorPosition;
    private double startingEncoderPosition;
    private double gearRatio;

    public GearRatioCalculationCommand(DoubleSupplier rotorPositionSupplier, DoubleSupplier encoderPositionSupplier, DoubleConsumer runGearRatioCalculation, SubsystemBase requirement) {
        this.rotorPositionSupplier = rotorPositionSupplier;
        this.encoderPositionSupplier = encoderPositionSupplier;
        this.runGearRatioCalculation = runGearRatioCalculation;
        this.subsystemName = requirement.getName();

        this.movementVoltage = new LoggedDashboardNumber("GearRatioCalculation/" + this.subsystemName + "/Voltage", 1);
        this.shouldMoveClockwise = new LoggedDashboardBoolean("GearRatioCalculation/" + this.subsystemName + "/ShouldMoveClockwise", false);

        addRequirements(requirement);
    }

    @Override
    public void initialize() {
        startingRotorPosition = rotorPositionSupplier.getAsDouble();
        startingEncoderPosition = encoderPositionSupplier.getAsDouble();
    }

    @Override
    public void execute() {
        runGearRatioCalculation.accept(movementVoltage.get() * getRotationDirection());
        gearRatio = calculateGearRatio();

        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/RotorDistance", getRotorDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/EncoderDistance", getEncoderDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/GearRatio", gearRatio);
    }

    @Override
    public void end(boolean interrupted) {
        printResult();
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

    private int getRotationDirection() {
        return shouldMoveClockwise.get() ? -1 : 1;
    }

    private void printResult() {
        System.out.println(subsystemName + " Gear Ratio: " + gearRatio);
    }
}
