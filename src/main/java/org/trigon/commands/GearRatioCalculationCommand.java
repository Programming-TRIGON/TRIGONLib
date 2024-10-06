package org.trigon.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardBoolean;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class GearRatioCalculationCommand extends Command {
    private static final LoggedDashboardNumber MOVEMENT_VOLTAGE = new LoggedDashboardNumber("GearRatioCalculation/Voltage", 1);
    private static final LoggedDashboardBoolean SHOULD_MOVE_CLOCKWISE = new LoggedDashboardBoolean("GearRatioCalculation/ShouldMoveClockwise", false);

    private final DoubleSupplier rotorPositionSupplier;
    private final DoubleSupplier encoderPositionSupplier;
    private final DoubleConsumer runGearRatioCalculation;
    private final String subsystemName;

    private double startingRotorPosition;
    private double startingEncoderPosition;
    private double gearRatio;

    public GearRatioCalculationCommand(DoubleSupplier rotorPositionSupplier, DoubleSupplier encoderPositionSupplier, DoubleConsumer runGearRatioCalculation, SubsystemBase requirement) {
        this.rotorPositionSupplier = rotorPositionSupplier;
        this.encoderPositionSupplier = encoderPositionSupplier;
        this.runGearRatioCalculation = runGearRatioCalculation;
        this.subsystemName = repeatedly().getName();
      
        addRequirements(requirement);
    }

    @Override
    public void initialize() {
        startingRotorPosition = rotorPositionSupplier.getAsDouble();
        startingEncoderPosition = encoderPositionSupplier.getAsDouble();
    }

    @Override
    public void execute() {
        runGearRatioCalculation.accept(MOVEMENT_VOLTAGE.get() * getRotationDirection());
        gearRatio = calculateGearRatio();

        Logger.recordOutput(subsystemName + "GearRatioCalculation/RotorDistance", getRotorDistance());
        Logger.recordOutput(subsystemName + "GearRatioCalculation/EncoderDistance", getEncoderDistance());
        Logger.recordOutput(subsystemName + "GearRatioCalculation/GearRatio", gearRatio);
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
        return SHOULD_MOVE_CLOCKWISE.get() ? -1 : 1;
    }

    private void printResult() {
        System.out.println(subsystemName + " Gear Ratio: " + gearRatio);
    }
}
