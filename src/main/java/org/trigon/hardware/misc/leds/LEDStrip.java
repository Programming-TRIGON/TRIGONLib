package org.trigon.hardware.misc.leds;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.trigon.hardware.RobotHardwareStats;

/**
 * A wrapper class for LED strips. This class provides a set of methods for controlling LED strips.
 */
public abstract class LEDStrip extends SubsystemBase {
    public static LEDStrip[] LED_STRIPS = new LEDStrip[0];
    protected final int indexOffset;
    protected final boolean inverted;
    protected final int numberOfLEDs;
    protected Runnable currentAnimation = () -> {
    };

    /**
     * Creates a new AddressableLEDStrip.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     * @return the created AddressableLEDStrip
     */
    public static AddressableLEDStrip createAddressableLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        return new AddressableLEDStrip(inverted, numberOfLEDs, indexOffset);
    }

    /**
     * Creates a new CANdleLEDStrip. In simulation or replay mode, an AddressableLEDStrip is created instead.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     * @return the created LEDStrip
     */
    public static LEDStrip createCANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        if (RobotHardwareStats.isReplay() || RobotHardwareStats.isSimulation())
            return new AddressableLEDStrip(inverted, numberOfLEDs, indexOffset);
        return new CANdleLEDStrip(inverted, numberOfLEDs, indexOffset);
    }

    /**
     * Sets the default command for all LED strips.
     *
     * @param command the default command to be set
     */
    public static void setDefaultCommandForAllLEDS(Command command) {
        for (LEDStrip ledStrip : LED_STRIPS)
            ledStrip.setDefaultCommand(command);
    }

    protected LEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        this.inverted = inverted;
        this.numberOfLEDs = numberOfLEDs;
        this.indexOffset = indexOffset;

        addLEDStripToLEDStripsArray(this);
    }

    public int getNumberOfLEDS() {
        return numberOfLEDs;
    }

    protected void setCurrentAnimation(Runnable currentAnimation) {
        this.currentAnimation = currentAnimation;
        currentAnimation.run();
    }

    protected void resetLEDSettings() {
    }

    protected abstract void clearLEDColors();

    protected abstract void animate(LEDStripAnimationSettings.StaticColorSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.BlinkSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.BreatheSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.ColorFlowSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.AlternateColorSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.SectionColorSettings settings);

    protected abstract void animate(LEDStripAnimationSettings.RainbowSettings settings);

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}