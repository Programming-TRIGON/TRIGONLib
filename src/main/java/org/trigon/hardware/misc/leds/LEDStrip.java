package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.Supplier;

public abstract class LEDStrip extends SubsystemBase {
    public static LEDStrip[] LED_STRIPS = new LEDStrip[0];
    final int indexOffset;
    final boolean inverted;
    final int numberOfLEDs;

    public LEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        this.inverted = inverted;
        this.numberOfLEDs = numberOfLEDs;
        this.indexOffset = indexOffset;

        addLEDStripToLEDStripsArray(this);
    }

    public static void setDefaultCommandForAllLEDS(Command command) {
        for (LEDStrip ledStrip : LED_STRIPS)
            ledStrip.setDefaultCommand(command);
    }

    public int getNumberOfLEDS() {
        return numberOfLEDs;
    }

    abstract void resetLEDSettings();

    /**
     * Sets the color of the LED strip to the given color.
     *
     * @param color the color to set the LED strip to
     */
    abstract void staticColor(Color color);

    /**
     * Blinks the LED strip between two colors.
     *
     * @param firstColor              the first color to blink
     * @param secondColor             the second color to blink
     * @param blinkingIntervalSeconds the interval in seconds to blink between color changes
     */
    abstract void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds);

    /**
     * "Breathes" a bunch of LEDs with a given color.
     *
     * @param color            the color of the breathing LEDs
     * @param breathingLEDs    the amount of breathing LEDs
     * @param cycleTimeSeconds the time it takes for a full breathing cycle
     * @param inverted         whether the breathing should be inverted
     * @param bounceMode       when the breathing LEDs should bounce back to the start of the strip
     */
    abstract void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode);

    /**
     * Flows a color through the LED strip.
     *
     * @param color            the color to flow through the LED strip
     * @param cycleTimeSeconds the time it takes for the color to flow through the LED strip
     * @param inverted         whether the color flow should be inverted
     */
    abstract void colorFlow(Color color, double cycleTimeSeconds, boolean inverted);

    /**
     * Displays two colors in an alternating pattern on the LED strip.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     *                    =
     */
    abstract void alternateColor(Color firstColor, Color secondColor);

    /**
     * Colors the LED strip in sections.
     *
     * @param colors an array of the colors to color the sections with. The length of the array dictates the amount of sections.
     */
    abstract void sectionColor(Supplier<Color>[] colors);

    /**
     * Displays a rainbow pattern on the LED strip.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement
     */
    abstract void rainbow(double brightness, double speed);

    abstract void clearLEDColors();

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}