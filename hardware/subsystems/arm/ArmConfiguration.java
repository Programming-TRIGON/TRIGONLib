package frc.trigon.lib.hardware.subsystems.arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.util.Color;

public class ArmConfiguration {
    public String name = "";

    /**
     * Length of the arm in meters.
     */
    public double lengthMeters = 1;

    /**
     * Maximum velocity of the motor.
     */
    public double maximumVelocity = 1;

    /**
     * Maximum acceleration of the motor.
     */
    public double maximumAcceleration = 1;

    /**
     * Maximum jerk of the motor.
     */
    public double maximumJerk = 1;

    /**
     * Ramp rate used in system identification.
     * See <a href="https://docs.wpilib.org/en/stable/docs/software/advanced-controls/system-identification/introduction.html">
     * WPILib System Identification Introduction</a>.
     */
    public double sysIDRampRate = 1;

    /**
     * Step voltage used in system identification.
     * See <a href="https://docs.wpilib.org/en/stable/docs/software/advanced-controls/system-identification/introduction.html">
     * WPILib System Identification Introduction</a>.
     */
    public double sysIDStepVoltage = 1;

    public double gearRatio = 1;

    /**
     * Mass of the arm in kilograms.
     */
    public double massKilograms = 1;

    /**
     * Visual offset for display in Mechanism2D.
     */
    public double visualizationOffset = 1;

    /**
     * Maximum angle of the arm.
     */
    public Rotation2d maximumAngle = Rotation2d.kZero;

    /**
     * Minimum angle of the arm.
     */
    public Rotation2d minimumAngle = Rotation2d.kZero;

    /**
     * Acceptable angular tolerance for reaching the target.
     */
    public Rotation2d angleTolerance = Rotation2d.kZero;

    public boolean focEnabled = true;

    public boolean shouldSimulateGravity = true;

    public Color mechanismColor = Color.kBlue;

    /**
     * The type and number of motors used in the simulation.
     */
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
