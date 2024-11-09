package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.awt.*;
import java.util.function.Supplier;

public abstract class LEDStrip extends SubsystemBase {
    public static LEDStrip[] LED_STRIPS = new LEDStrip[0];
    final int indexOffset;
    final boolean inverted;
    final int numberOfLEDs;

    public LEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        this.indexOffset = indexOffset;
        this.inverted = inverted;
        this.numberOfLEDs = numberOfLEDs;

        addLEDStripToLEDStripsArray(this);
    }

    public static void setDefaultCommandForAllLEDS(Command command) {
        for (LEDStrip ledStrip : LED_STRIPS)
            ledStrip.setDefaultCommand(command);
    }

    public int getNumberOfLEDS() {
        return numberOfLEDs;
    }

    void resetLEDSettings() {
    }

    abstract void staticColor(Color color);

    abstract void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds);

    abstract void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode);

    abstract void colorFlow(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted);

    abstract void alternateColor(Color firstColor, Color secondColor, double intervalSeconds, TwinkleAnimation.TwinklePercent divider);

    abstract void sectionColor(int amountOfSections, Supplier<Color>[] colors);

    abstract void rainbow(double brightness, double speed);

    abstract void clearLEDColors();

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}
