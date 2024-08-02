package frc.trigon.robot.hardware.misc;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.trigon.robot.commands.Commands;

public class XboxController extends CommandXboxController {
    private int exponent = 1;
    private double deadband = 0;
    private Command stopRumbleCommand = null;
    
    /**
     * Constructs an instance of a controller.
     *
     * @param port The port index on the Driver Station that the controller is plugged into.
     */
    public XboxController(int port) {
        super(port);
    }

    /**
     * Constructs an instance of a controller.
     *
     * @param port     the port index on the Driver Station that the controller is plugged into
     * @param exponent how much to exponentiate the raw values of the sticks by
     * @param deadband the deadband for the controller
     */
    public XboxController(int port, int exponent, double deadband) {
        this(port);
        this.exponent = exponent;
        this.deadband = deadband;
    }

    @Override
    public double getLeftX() {
        return calculateValue(super.getLeftX());
    }

    @Override
    public double getRightX() {
        return calculateValue(super.getRightX());
    }

    @Override
    public double getLeftY() {
        return calculateValue(super.getLeftY());
    }

    @Override
    public double getRightY() {
        return calculateValue(super.getRightY());
    }

    /**
     * Sets the exponent for the controller, which will exponentiate the raw values of the stick by the exponent.
     *
     * @param exponent the exponent
     */
    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    /**
     * Sets the deadband for the controller, which will ignore any values within the deadband.
     *
     * @param deadband the deadband, between 0 and 1
     */
    public void setDeadband(double deadband) {
        this.deadband = deadband;
    }

    public void rumble(double durationSeconds, double power) {
        if (stopRumbleCommand != null)
            stopRumbleCommand.cancel();

        stopRumbleCommand = Commands.getDelayedCommand(durationSeconds, () -> getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0));
        stopRumbleCommand.schedule();

        getHID().setRumble(GenericHID.RumbleType.kBothRumble, power);
    }

    /**
     * Get the angle in degrees of the default POV (index 0) on the HID.
     * The POV angles start at 0 in the up direction, and increase clockwise (e.g. right is 90, upper-left is 315).
     *
     * @return the angle of the POV in degrees, or -1 if the POV is not pressed
     */
    public double getPov() {
        return super.getHID().getPOV();
    }

    private double calculateValue(double value) {
        if (Math.abs(value) < deadband)
            return 0;

        final double exponentiatedValue = Math.pow(value, exponent);
        return Math.abs(exponentiatedValue) * -Math.signum(value);
    }
}