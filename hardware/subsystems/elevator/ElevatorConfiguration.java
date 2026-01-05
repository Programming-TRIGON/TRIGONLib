package psubsystems.elevator;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.util.Color;

public class ElevatorConfiguration {
    public String name = "";
    /**
     * Acceptable position error in meters.
     */
    public double positionToleranceMeters = 1;

    /**
     * Radius of the elevator drum in meters.
     */
    public double drumRadiusMeters = 1;

    /**
     * Minimum elevator height in meters.
     */
    public double minimumHeight = 1;

    /**
     * Maximum elevator height in meters.
     */
    public double maximumHeight = 1;

    /**
     * Maximum velocity of the elevator in meters per second.
     */
    public double maximumVelocity = 1;

    /**
     * Maximum acceleration of the elevator in meters per second squared.
     */
    public double maximumAcceleration = 1;

    /**
     * Maximum jerk of the elevator in meters per second cubed.
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
     * Mass of the Elevator in kg.
     */
    public double massKilograms = 1;

    public boolean focEnabled = true;

    public boolean shouldSimulateGravity = true;

    public Color mechanismColor = Color.kBlue;

    /**
     * The type and number of motors used in the simulation.
     */
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
