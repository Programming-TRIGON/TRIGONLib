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
     * The LED instance be configured before being set, however it does not need to be started.
     *
     * @param led the LED instance to be used
     */
    public static void setLED(AddressableLED led) {
        LED = led;
        LED.start();
    }

    /**
     * Sets the AddressableLEDBuffer instance to be used for controlling the LED strip. Must be set before using any LED strips.
     * The LED buffer instance must be configured before being set.
     *
     * @param ledBuffer the LED buffer instance to be used
     */
    public static void setLEDBuffer(AddressableLEDBuffer ledBuffer) {
        LED_BUFFER = ledBuffer;
    }

    /**
     * Constructs a new AddressableLEDStrip. Before any commands are sent to the LED strip, the setLED method must be called.
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
        currentAnimation.run();
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
        currentAnimation = () -> staticColor(color);
        setLEDColors(color, 0, numberOfLEDs - 1);
    }

    @Override
    void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        inverted = this.inverted != inverted;
        clearLEDColors();
        double moveLEDTimeSeconds = cycleTimeSeconds / numberOfLEDs;
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (inverted)
                lastBreatheLED--;
            else
                lastBreatheLED++;
        }
        checkIfBreathingHasHitEnd(breathingLEDs, inverted, bounceMode);
        setBreathingLEDs(color, breathingLEDs, bounceMode);
    }

    @Override
    void colorFlow(Color color, double cycleTimeSeconds, boolean inverted) {
        clearLEDColors();
        inverted = this.inverted != inverted;
        double moveLEDTimeSeconds = cycleTimeSeconds / numberOfLEDs;
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (isLEDAnimationChanged)
                amountOfColorFlowLEDs--;
            else
                amountOfColorFlowLEDs++;
        }
        checkIfColorFlowHasHitEnd();
        setLEDColors(color, inverted ? numberOfLEDs - amountOfColorFlowLEDs - 1 : 0, inverted ? numberOfLEDs - 1 : amountOfColorFlowLEDs);
    }

    @Override
    void alternateColor(Color firstColor, Color secondColor) {
        for (int i = 0; i < numberOfLEDs; i++)
            LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? firstColor : secondColor);
    }

    @Override
    void rainbow(double brightness, double speed) {
        int adjustedBrightness = (int) (brightness * 255);
        int hueIncrement = (int) (speed * 3);

        for (int led = 0; led < numberOfLEDs; led++) {
            final int hue = (int) (rainbowFirstPixelHue + (led * 180 / numberOfLEDs) % 180);
            LED_BUFFER.setHSV(led + indexOffset, hue, 255, adjustedBrightness);
        }

        if (inverted) {
            rainbowFirstPixelHue -= hueIncrement;
            if (rainbowFirstPixelHue < 0)
                rainbowFirstPixelHue += 180;
        } else {
            rainbowFirstPixelHue += hueIncrement;
            rainbowFirstPixelHue %= 180;
        }
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

    private void checkIfBreathingHasHitEnd(int amountOfBreathingLEDs, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        int bounceModeAddition = switch (bounceMode) {
            case Back -> amountOfBreathingLEDs;
            case Center -> amountOfBreathingLEDs / 2;
            default -> 0;
        };
        if (inverted ? (lastBreatheLED < indexOffset + bounceModeAddition) : (lastBreatheLED >= numberOfLEDs + indexOffset + bounceModeAddition))
            lastBreatheLED = inverted ? indexOffset + numberOfLEDs : indexOffset;
    }

    private void setBreathingLEDs(Color color, int breathingLEDs, LarsonAnimation.BounceMode bounceMode) {
        for (int i = 0; i < breathingLEDs; i++) {
            if (lastBreatheLED - i >= indexOffset && lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i, color);
            else if (lastBreatheLED - i < indexOffset + numberOfLEDs) {
                if (bounceMode.equals(LarsonAnimation.BounceMode.Back) || bounceMode.equals(LarsonAnimation.BounceMode.Center) && i > breathingLEDs / 2)
                    return;
                LED_BUFFER.setLED(lastBreatheLED - i + numberOfLEDs, color);
            }
        }
    }

    private void checkIfColorFlowHasHitEnd() {
        if (amountOfColorFlowLEDs >= numberOfLEDs || amountOfColorFlowLEDs < 0) {
            amountOfColorFlowLEDs = amountOfColorFlowLEDs < 0 ? amountOfColorFlowLEDs + 1 : amountOfColorFlowLEDs - 1;
            isLEDAnimationChanged = !isLEDAnimationChanged;
        }
    }

    private void setLEDColors(Color color, int startIndex, int endIndex) {
        for (int i = 0; i <= endIndex - startIndex; i++)
            LED_BUFFER.setLED(startIndex + indexOffset + i, color);
    }
}