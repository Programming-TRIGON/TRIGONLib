package frc.trigon.lib.hardware.misc;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * A class that represents an Xbox controller. Used to get the values of the sticks and buttons on a controller, with the option of a deadband and exponentiation.
 */
public class XboxController extends CommandXboxController {
    private int
            rightStickExponent = 1,
            leftStickExponent = 1;
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
     * @param port               the port index on the Driver Station that the controller is plugged into
     * @param rightStickExponent how much to exponentiate the raw values of the right stick by
     * @param leftStickExponent  how much to exponentiate the raw values of the right stick by
     * @param deadband           the deadband for the controller
     */
    public XboxController(int port, int rightStickExponent, int leftStickExponent, double deadband) {
        this(port);
        this.rightStickExponent = rightStickExponent;
        this.leftStickExponent = leftStickExponent;
        this.deadband = deadband;
    }

    @Override
    public double getRightX() {
        return calculateRightStickValue(super.getRightX());
    }

    @Override
    public double getRightY() {
        return calculateRightStickValue(super.getRightY());
    }

    @Override
    public double getLeftX() {
        return calculateLeftStickValue(super.getLeftX());
    }

    @Override
    public double getLeftY() {
        return calculateLeftStickValue(super.getLeftY());
    }

    /**
     * Sets the exponent for the controller, which will exponentiate the raw values of the stick by the exponent.
     *
     * @param exponent the exponent
     */
    public void setExponent(int exponent) {
        this.rightStickExponent = exponent;
        this.leftStickExponent = exponent;
    }

    /**
     * Sets the exponent for the right stick on the controller, which will exponentiate the raw values of the stick by the exponent.
     *
     * @param exponent the exponent
     */
    public void setRightStickExponent(int exponent) {
        this.rightStickExponent = exponent;
    }

    /**
     * Sets the exponent for the left stick on the controller, which will exponentiate the raw values of the stick by the exponent.
     *
     * @param exponent the exponent
     */
    public void setLeftStickExponent(int exponent) {
        this.leftStickExponent = exponent;
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

        stopRumbleCommand = new WaitCommand(durationSeconds).andThen(() -> getHID().setRumble(GenericHID.RumbleType.kBothRumble, 0)).ignoringDisable(true);
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

    private double calculateRightStickValue(double value) {
        return calculateStickValue(value, rightStickExponent);
    }

    private double calculateLeftStickValue(double value) {
        return calculateStickValue(value, leftStickExponent);
    }

    private double calculateStickValue(double value, double exponent) {
        if (Math.abs(value) < deadband)
            return 0;

        final double exponentiatedValue = Math.pow(value, exponent);
        return Math.abs(exponentiatedValue) * -Math.signum(value);
    }
}