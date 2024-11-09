package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.Timer;

import java.awt.*;
import java.util.function.Supplier;

public class CANdleLEDStrip extends LEDStrip {
    private static CANdle CANDLE;
    private static int LAST_CREATED_LED_STRIP_ANIMATION_SLOT = 0;
    private final int animationSlot;
    private boolean alternateColor = true;
    private double lastAlternateColorTime = 0;

    public static void setCandle(CANdle candle) {
        CANDLE = candle;
    }

    public CANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        super(inverted, numberOfLEDs, indexOffset);
        LAST_CREATED_LED_STRIP_ANIMATION_SLOT++;
        animationSlot = LAST_CREATED_LED_STRIP_ANIMATION_SLOT;
    }

    @Override
    void clearLEDColors() {
        CANDLE.clearAnimation(animationSlot);
    }

    @Override
    void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds) {
        CANDLE.animate(
                new SingleFadeAnimation(
                        firstColor.getRed(),
                        firstColor.getGreen(),
                        firstColor.getBlue(),
                        0,
                        blinkingIntervalSeconds,
                        this.numberOfLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void staticColor(Color color) {
        CANDLE.setLEDs(color.getRed(), color.getGreen(), color.getBlue(), 0, indexOffset, numberOfLEDs);
    }

    @Override
    void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        CANDLE.animate(
                new LarsonAnimation(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        0,
                        cycleTimeSeconds,
                        this.numberOfLEDs,
                        bounceMode,
                        breathingLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void alternateColor(Color firstColor, Color secondColor, double intervalSeconds, TwinkleAnimation.TwinklePercent divider) {
        if (Timer.getFPGATimestamp() - lastAlternateColorTime > intervalSeconds) {
            alternateColor = !alternateColor;
            lastAlternateColorTime = Timer.getFPGATimestamp();
        }
        if (alternateColor) {
            for (int i = 0; i < numberOfLEDs; i++)
                CANDLE.setLEDs(
                        firstColor.getRed(),
                        firstColor.getGreen(),
                        firstColor.getBlue(),
                        0,
                        i + indexOffset,
                        1
                );
            return;
        }
        for (int i = 0; i < numberOfLEDs; i++)
            CANDLE.setLEDs(
                    firstColor.getRed(),
                    firstColor.getGreen(),
                    firstColor.getBlue(),
                    0,
                    i + indexOffset,
                    1
            );
    }

    @Override
    void colorFlow(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted) {
        CANDLE.animate(
                new ColorFlowAnimation(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        0,
                        cycleTimeSeconds,
                        this.numberOfLEDs,
                        inverted ? ColorFlowAnimation.Direction.Backward : ColorFlowAnimation.Direction.Forward,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void rainbow(double brightness, double speed) {
        CANDLE.animate(
                new RainbowAnimation(
                        brightness,
                        speed,
                        this.numberOfLEDs,
                        inverted,
                        indexOffset
                ),
                animationSlot
        );
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
            for (int i = 0; i < amountOfSections; i++) {
                CANDLE.setLEDs(
                        colors[amountOfSections - i - 1].get().getRed(),
                        colors[amountOfSections - i - 1].get().getGreen(),
                        colors[amountOfSections - i - 1].get().getBlue(),
                        0,
                        LEDSPerSection * i + indexOffset,
                        i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1
                );
            }
            return;
        }
        for (int i = 0; i < amountOfSections; i++) {
            CANDLE.setLEDs(
                    colors[i].get().getRed(),
                    colors[i].get().getGreen(),
                    colors[i].get().getBlue(),
                    0,
                    LEDSPerSection * i + indexOffset,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1
            );
        }
    }
}