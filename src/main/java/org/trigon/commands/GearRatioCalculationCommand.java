package org.trigon.commands;

import com.ctre.phoenix6.controls.VoltageOut;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;
import org.trigon.hardware.phoenix6.cancoder.CANcoderEncoder;
import org.trigon.hardware.phoenix6.cancoder.CANcoderSignal;
import org.trigon.hardware.phoenix6.talonfx.TalonFXMotor;
import org.trigon.hardware.phoenix6.talonfx.TalonFXSignal;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * A command that calculates and logs the gear ratio of a mechanism by comparing the distance traveled by a rotor and an encoder.
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
     * This constructor takes a motor to run the gear ratio calculation on, and to measure the distance the rotor travels.
     * It also takes an encoder to measure the distance traveled.
     *
     * @param motor                             the motor that drives the rotor
     * @param encoder                           the encoder that measures the distance traveled
     * @param backlashAccountabilityTimeSeconds the time to wait before setting the starting positions in order to account for backlash
     * @param requirement                       the subsystem that this command requires
     */
    public GearRatioCalculationCommand(TalonFXMotor motor, CANcoderEncoder encoder, double backlashAccountabilityTimeSeconds, SubsystemBase requirement) {
        this(
                () -> motor.getSignal(TalonFXSignal.ROTOR_POSITION),
                () -> encoder.getSignal(CANcoderSignal.POSITION),
                (voltage) -> motor.setControl(new VoltageOut(voltage)),
                backlashAccountabilityTimeSeconds,
                requirement
        );
    }

    /**
     * Creates a new GearRatioCalculationCommand.
     *
     * @param rotorPositionSupplier             a supplier that returns the current position of the rotor in degrees
     * @param encoderPositionSupplier           a supplier that returns the current position of the encoder in degrees
     * @param runGearRatioCalculation           a consumer that drives the motor with a given voltage
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
        gearRatio = 0;
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/RotorDistance", 0);
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/EncoderDistance", 0);
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/GearRatio", gearRatio);
    }

    @Override
    public void execute() {
        runGearRatioCalculation();

        if (Timer.getFPGATimestamp() - startTime > backlashAccountabilityTimeSeconds && !hasSetStartingPositions)
            setStartingPositions();

        if (hasSetStartingPositions) {
            gearRatio = calculateGearRatio();
            logGearRatioAndDistance();
        }
    }

    @Override
    public void end(boolean interrupted) {
        gearRatio = calculateGearRatio();
        logGearRatioAndDistance();
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

    private void logGearRatioAndDistance() {
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/RotorDistance", getRotorDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/EncoderDistance", getEncoderDistance());
        Logger.recordOutput("GearRatioCalculation/" + subsystemName + "/GearRatio", gearRatio);
    }

    private double calculateGearRatio() {
        final double currentRotorDistance = getRotorDistance();
        final double currentEncoderDistance = getEncoderDistance();
        if (currentEncoderDistance == 0)
            return 0;
        return currentRotorDistance / currentEncoderDistance;
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