package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

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
                        (int) firstColor.red,
                        (int) firstColor.green,
                        (int) firstColor.blue,
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
        CANDLE.setLEDs((int) color.red, (int) color.green, (int) color.blue, 0, indexOffset, numberOfLEDs);
    }

    @Override
    void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        CANDLE.animate(
                new LarsonAnimation(
                        (int) color.red,
                        (int) color.green,
                        (int) color.blue,
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
    void alternateColor(Color firstColor, Color secondColor, double intervalSeconds) {
        if (Timer.getFPGATimestamp() - lastAlternateColorTime > intervalSeconds) {
            alternateColor = !alternateColor;
            lastAlternateColorTime = Timer.getFPGATimestamp();
        }
        if (alternateColor) {
            for (int i = 0; i < numberOfLEDs; i++)
                CANDLE.setLEDs(
                        (int) firstColor.red,
                        (int) firstColor.green,
                        (int) firstColor.blue,
                        0,
                        i + indexOffset,
                        1
                );
            return;
        }
        for (int i = 0; i < numberOfLEDs; i++)
            CANDLE.setLEDs(
                    (int) firstColor.red,
                    (int) firstColor.green,
                    (int) firstColor.blue,
                    0,
                    i + indexOffset,
                    1
            );
    }

    @Override
    void colorFlow(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted) {
        inverted = this.inverted != inverted;
        CANDLE.animate(
                new ColorFlowAnimation(
                        (int) color.red,
                        (int) color.green,
                        (int) color.blue,
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
                        (int) colors[amountOfSections - i - 1].get().red,
                        (int) colors[amountOfSections - i - 1].get().green,
                        (int) colors[amountOfSections - i - 1].get().blue,
                        0,
                        LEDSPerSection * i + indexOffset,
                        i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1
                );
            }
            return;
        }
        for (int i = 0; i < amountOfSections; i++) {
            CANDLE.setLEDs(
                    (int) colors[i].get().red,
                    (int) colors[i].get().green,
                    (int) colors[i].get().blue,
                    0,
                    LEDSPerSection * i + indexOffset,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1
            );
        }
    }
}