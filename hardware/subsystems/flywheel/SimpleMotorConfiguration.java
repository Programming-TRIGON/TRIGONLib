package frc.trigon.lib.hardware.subsystems.flywheel;

import edu.wpi.first.math.system.plant.DCMotor;

/**
 * The config for the SimpleMotor Subsystem.
 */
public class SimpleMotorConfiguration {
    public String name = "";
    public double
            gearRatio = 1,
    /**
     * The moment of inertia of the motor
     */
    momentOfInertia = 0.003,
    /**
     * Maximum velocity of the motor.
     */
    maximumVelocity = 1,
    /**
     * Maximum acceleration of the motor.
     */
    maximumAcceleration = 1,
    /**
     * Maximum jerk of the motor.
     */
    maximumJerk = 1,
    /**
     * Maximum velocity displayed by the mechanism 2D
     */
    maximumDisplayableVelocity = 1,
    /**
     * The acceptable tolerance between the target velocity and current velocity.
     */
    velocityTolerance = 1,
    /**
     * Ramp rate used in system identification.
     * See <a href="https://docs.wpilib.org/en/stable/docs/software/advanced-controls/system-identification/introduction.html">
     * WPILib System Identification Introduction</a>.
     */
    sysIDRampRate = 1,
    /**
     * Step voltage used in system identification.
     * See <a href="https://docs.wpilib.org/en/stable/docs/software/advanced-controls/system-identification/introduction.html">
     * WPILib System Identification Introduction</a>.
     */
    sysIDStepVoltage = 1;
    public boolean focEnabled = true;
    /**
     * should the motor control mode be Voltage as opposed to Velocity.
     */
    public boolean shouldUseVoltageControl = false;
    /**
     * The type and amount of motors to be used in the simulation.
     */
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
