package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.trigon.hardware.RobotHardwareStats;

import java.util.function.Supplier;

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

    public LEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        this.inverted = inverted;
        this.numberOfLEDs = numberOfLEDs;
        this.indexOffset = indexOffset;

        addLEDStripToLEDStripsArray(this);
    }

    public int getNumberOfLEDS() {
        return numberOfLEDs;
    }

    void setCurrentAnimation(Runnable currentAnimation) {
        this.currentAnimation = currentAnimation;
        currentAnimation.run();
    }

    void resetLEDSettings() {
    }

    /**
     * Sets the color of the LED strip to the given color.
     *
     * @param color the color to set the LED strip to
     */
    abstract void staticColor(Color color);

    /**
     * Blinks the LED strip with a specific color.
     *
     * @param color the color to blink
     * @param speed how fast the LED strip should blink on a scale between 0 and 1
     */
    abstract void blink(Color color, double speed);

    /**
     * "Breathes" a pocket of LEDs with a given color.
     *
     * @param color         the color of the breathing LEDs
     * @param breathingLEDs the amount of breathing LEDs
     * @param speed         how fast should the color travel the strip on a scale between 0 and 1
     * @param inverted      whether the breathing should be inverted
     * @param bounceMode    when the breathing LEDs should restart at the start of the strip
     */
    abstract void breathe(Color color, int breathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode);

    /**
     * Flows a color through the LED strip.
     *
     * @param color    the color to flow through the LED strip
     * @param speed    how fast should the color travel the strip on a scale between 0 and 1
     * @param inverted whether the color flow should be inverted
     */
    abstract void colorFlow(Color color, double speed, boolean inverted);

    /**
     * Displays two colors in an alternating pattern on the LED strip.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     */
    abstract void alternateColor(Color firstColor, Color secondColor);

    /**
     * Splits the LED strip into different sections.
     *
     * @param colors an array of the colors to color the sections with. The length of the array dictates the amount of sections
     */
    abstract void sectionColor(Supplier<Color>[] colors);

    /**
     * Displays a rainbow pattern on the LED strip.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a scale from 0 to 1
     * @param inverted   whether the rainbow should be inverted
     */
    abstract void rainbow(double brightness, double speed, boolean inverted);

    abstract void clearLEDColors();

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}