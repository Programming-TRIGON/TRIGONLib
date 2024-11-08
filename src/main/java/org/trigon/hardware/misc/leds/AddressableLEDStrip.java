package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

import java.awt.*;
import java.util.function.Supplier;

public class AddressableLEDStrip extends LEDStrip {
    private static AddressableLED LED;
    private static AddressableLEDBuffer LED_BUFFER;
    private int lastBreatheLED;
    private double lastBreatheMovementTime = 0;
    private double rainbowFirstPixelHue = 0;
    private boolean areLEDsOnForBlinking = false;
    private double lastBlinkTime = 0;
    private boolean alternateColor = true;
    private double lastAlternateColorTime = 0;

    public static void setLED(AddressableLED led) {
        LED = led;
    }

    public static void setLEDBuffer(AddressableLEDBuffer ledBuffer) {
        LED_BUFFER = ledBuffer;
    }

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
        staticColor(Color.BLACK);
    }

    @Override
    void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds) {
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastBlinkTime > blinkingIntervalSeconds) {
            lastBlinkTime = currentTime;
            areLEDsOnForBlinking = !areLEDsOnForBlinking;
        }
        if (areLEDsOnForBlinking)
            staticColor(firstColor);
        else
            staticColor(secondColor);
    }

    @Override
    void staticColor(Color color) {
        setLEDColors(color, 0, numberOfLEDs - 1);
    }

    @Override
    void breath(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        clearLEDColors();
        inverted = this.inverted != inverted;
        double moveLEDTimeSeconds = cycleTimeSeconds / numberOfLEDs;
        double currentTime = Timer.getFPGATimestamp();
        if (currentTime - lastBreatheMovementTime > moveLEDTimeSeconds) {
            lastBreatheMovementTime = currentTime;
            if (inverted)
                lastBreatheLED--;
            else
                lastBreatheLED++;
        }
        if (inverted ? (lastBreatheLED < indexOffset) : (lastBreatheLED >= numberOfLEDs + indexOffset)) {
            if (!shouldLoop) {
                getDefaultCommand().schedule();
                return;
            }
            lastBreatheLED = inverted ? indexOffset + numberOfLEDs : indexOffset;
        }
        for (int i = 0; i < breathingLEDs; i++) {
            if (lastBreatheLED - i >= indexOffset && lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i, convertToColor(color));
            else if (lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i + numberOfLEDs, convertToColor(color));
        }
    }

    @Override
    void twinkle(Color firstColor, Color secondColor, double intervalSeconds, TwinkleAnimation.TwinklePercent divider) {
        if (Timer.getFPGATimestamp() - lastAlternateColorTime > intervalSeconds) {
            alternateColor = !alternateColor;
            lastAlternateColorTime = Timer.getFPGATimestamp();
        }
        if (alternateColor) {
            for (int i = 0; i < numberOfLEDs; i++)
                LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? convertToColor(firstColor) : convertToColor(secondColor));
            return;
        }
        for (int i = 0; i < numberOfLEDs; i++)
            LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? convertToColor(secondColor) : convertToColor(firstColor));
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
    void sectionColor(int amountOfSections, Supplier<Color>[] colors) {
        if (amountOfSections != colors.length)
            throw new IllegalArgumentException("Amount of sections must be equal to the amount of colors");
        final int LEDSPerSection = (int) Math.floor(numberOfLEDs / amountOfSections);
        setSectionColor(amountOfSections, LEDSPerSection, colors);
    }

    private void setSectionColor(int amountOfSections, int LEDSPerSection, Supplier<Color>[] colors) {
        if (inverted) {
            for (int i = 0; i < amountOfSections; i++)
                setLEDColors(colors[amountOfSections - i - 1].get(), LEDSPerSection * i, i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1);
            return;
        }
        for (int i = 0; i < amountOfSections; i++)
            setLEDColors(colors[i].get(), LEDSPerSection * i, i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1);
    }

    private void setLEDColors(Color color, int startIndex, int endIndex) {
        for (int i = 0; i <= endIndex - startIndex; i++)
            LED_BUFFER.setLED(startIndex + indexOffset + i, convertToColor(color));
    }

    private void resetLEDSettings() {
        lastBreatheLED = indexOffset;
        lastBreatheMovementTime = Timer.getFPGATimestamp();
        rainbowFirstPixelHue = 0;
        areLEDsOnForBlinking = false;
        lastBlinkTime = 0;
        alternateColor = true;
        lastAlternateColorTime = 0;
    }

    private edu.wpi.first.wpilibj.util.Color convertToColor(Color color) {
        return new edu.wpi.first.wpilibj.util.Color(color.getRed(), color.getGreen(), color.getBlue());
    }
}
