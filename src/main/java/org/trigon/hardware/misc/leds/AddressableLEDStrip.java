package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

public class AddressableLEDStrip extends LEDStrip {
    private static AddressableLED LED;
    private static AddressableLEDBuffer LED_BUFFER;
    private int lastBreatheLED;
    private double lastLEDAnimationChangeTime = 0;
    private double rainbowFirstPixelHue = 0;
    private boolean isLEDAnimationChanged = false;
    private int amountOfColorFlowLEDs = 0;

    /**
     * Sets the AddressableLED instance to be used for controlling the LED strip. Must be set before using any LED strips.
     *
     * @param led the LED instance to be used
     */
    public static void setLED(AddressableLED led) {
        LED = led;
    }

    /**
     * Sets the AddressableLEDBuffer instance to be used for controlling the LED strip. Must be set before using any LED strips.
     *
     * @param ledBuffer the LED buffer instance to be used
     */
    public static void setLEDBuffer(AddressableLEDBuffer ledBuffer) {
        LED_BUFFER = ledBuffer;
    }

    /**
     * Constructs a new AddressableLEDStrip.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     */
    public AddressableLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        super(inverted, numberOfLEDs, indexOffset);
        resetLEDSettings();
    }

    @Override
    public void periodic() {
        LED.setData(LED_BUFFER);
    }

    @Override
    void clearLEDColors() {
        staticColor(Color.kBlack);
    }

    @Override
    void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds) {
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastLEDAnimationChangeTime > blinkingIntervalSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            isLEDAnimationChanged = !isLEDAnimationChanged;
        }
        if (isLEDAnimationChanged)
            staticColor(firstColor);
        else
            staticColor(secondColor);
    }

    @Override
    void staticColor(Color color) {
        setLEDColors(color, 0, numberOfLEDs - 1);
    }

    @Override
    void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        clearLEDColors();
        inverted = this.inverted != inverted;
        double moveLEDTimeSeconds = cycleTimeSeconds / numberOfLEDs;
        double currentTime = Timer.getFPGATimestamp();

        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (inverted)
                lastBreatheLED--;
            else
                lastBreatheLED++;
        }
        setBreathingLEDs(color, breathingLEDs, shouldLoop);
    }

    @Override
    void colorFlow(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted) {
        clearLEDColors();
        inverted = this.inverted != inverted;
        double moveLEDTimeSeconds = cycleTimeSeconds / numberOfLEDs;
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (inverted)
                amountOfColorFlowLEDs--;
            else
                amountOfColorFlowLEDs++;
        }
        if (inverted ? amountOfColorFlowLEDs < 0 : amountOfColorFlowLEDs >= numberOfLEDs) {
            if (!shouldLoop) {
                getDefaultCommand().schedule();
                return;
            }
            amountOfColorFlowLEDs = inverted ? numberOfLEDs : 0;
        }
        if (inverted) {
            setLEDColors(color, numberOfLEDs - amountOfColorFlowLEDs, numberOfLEDs - 1);
            return;
        }
        setLEDColors(color, 0, amountOfColorFlowLEDs);
    }

    @Override
    void alternateColor(Color firstColor, Color secondColor, double intervalSeconds) {
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastLEDAnimationChangeTime > intervalSeconds) {
            isLEDAnimationChanged = !isLEDAnimationChanged;
            lastLEDAnimationChangeTime = currentTime;
        }
        if (isLEDAnimationChanged) {
            for (int i = 0; i < numberOfLEDs; i++)
                LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? firstColor : secondColor);
            return;
        }
        for (int i = 0; i < numberOfLEDs; i++)
            LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? secondColor : firstColor);
    }


    @Override
    void rainbow(double brightness, double speed) {
        for (int led = 0; led < numberOfLEDs; led++) {
            final int hue = (int) (rainbowFirstPixelHue + (led * 180 / numberOfLEDs) % 180);
            LED_BUFFER.setHSV(led + indexOffset, hue, 255, 128);
        }
        if (inverted) {
            rainbowFirstPixelHue -= 3;
            if (rainbowFirstPixelHue < 0)
                rainbowFirstPixelHue += 180;
            return;
        }
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
    }

    @Override
    void sectionColor(Supplier<Color>[] colors) {
        final int amountOfSections = colors.length;
        final int LEDsPerSection = (int) Math.floor(numberOfLEDs / amountOfSections);

        for (int i = 0; i < amountOfSections; i++)
            setLEDColors(
                    inverted ? colors[amountOfSections - i - 1].get() : colors[i].get(),
                    LEDsPerSection * i,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDsPerSection * (i + 1) - 1
            );
    }

    @Override
    void resetLEDSettings() {
        lastBreatheLED = indexOffset;
        lastLEDAnimationChangeTime = Timer.getFPGATimestamp();
        rainbowFirstPixelHue = 0;
        isLEDAnimationChanged = false;
        amountOfColorFlowLEDs = 0;
    }

    private void setBreathingLEDs(Color color, int breathingLEDs, boolean shouldLoop) {
        if (inverted ? (lastBreatheLED < indexOffset) : (lastBreatheLED >= numberOfLEDs + indexOffset)) {
            if (!shouldLoop) {
                getDefaultCommand().schedule();
                return;
            }
            lastBreatheLED = inverted ? indexOffset + numberOfLEDs : indexOffset;
        }

        for (int i = 0; i < breathingLEDs; i++) {
            if (lastBreatheLED - i >= indexOffset && lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i, color);
            else if (lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i + numberOfLEDs, color);
        }
    }

    private void setLEDColors(Color color, int startIndex, int endIndex) {
        for (int i = 0; i <= endIndex - startIndex; i++)
            LED_BUFFER.setLED(startIndex + indexOffset + i, color);
    }
}