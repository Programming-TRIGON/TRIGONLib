package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import org.trigon.hardware.RobotHardwareStats;

import java.util.function.Supplier;

public class CANdleLEDStrip extends LEDStrip {
    private static CANdle CANDLE;
    private static int LAST_CREATED_LED_STRIP_ANIMATION_SLOT = 0;
    private final int animationSlot;
    private final AddressableLEDStrip simulationLEDStrip;
    private boolean isAlternateColorAlternated = true;
    private double lastAlternateColorTime = 0;

    public static void setCandle(CANdle candle) {
        CANDLE = candle;
    }

    public CANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset, AddressableLEDStrip simulationLEDStrip) {
        super(inverted, numberOfLEDs, indexOffset);
        animationSlot = LAST_CREATED_LED_STRIP_ANIMATION_SLOT;
        LAST_CREATED_LED_STRIP_ANIMATION_SLOT++;
        this.simulationLEDStrip = simulationLEDStrip;
    }

    @Override
    void clearLEDColors() {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.clearLEDColors();
            return;
        }
        CANDLE.clearAnimation(animationSlot);
    }

    @Override
    void blink(Color firstColor, Color secondColor, double blinkingIntervalSeconds) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds);
            return;
        }
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
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.staticColor(color);
            return;
        }
        CANDLE.setLEDs((int) color.red, (int) color.green, (int) color.blue, 0, indexOffset, numberOfLEDs);
    }

    @Override
    void breathe(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.breathe(color, breathingLEDs, cycleTimeSeconds, shouldLoop, inverted, bounceMode);
            return;
        }
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
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.alternateColor(firstColor, secondColor, intervalSeconds);
            return;
        }
        if (Timer.getFPGATimestamp() - lastAlternateColorTime > intervalSeconds) {
            isAlternateColorAlternated = !isAlternateColorAlternated;
            lastAlternateColorTime = Timer.getFPGATimestamp();
        }
        if (isAlternateColorAlternated) {
            for (int i = 0; i < numberOfLEDs; i++)
                CANDLE.setLEDs(
                        (int) (i % 2 == 0 ? firstColor.red : secondColor.red),
                        (int) (i % 2 == 0 ? firstColor.green : secondColor.green),
                        (int) (i % 2 == 0 ? firstColor.blue : secondColor.blue),
                        0,
                        i + indexOffset,
                        1
                );
        }
    }

    @Override
    void colorFlow(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.colorFlow(color, cycleTimeSeconds, shouldLoop, inverted);
            return;
        }
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
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.rainbow(brightness, speed);
            return;
        }
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
    void sectionColor(Supplier<Color>[] colors) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.sectionColor(colors);
            return;
        }
        final int LEDSPerSection = (int) Math.floor(numberOfLEDs / colors.length);
        setSectionColor(colors.length, LEDSPerSection, colors);
    }

    @Override
    void resetLEDSettings() {
        if (RobotHardwareStats.isSimulation())
            simulationLEDStrip.resetLEDSettings();
    }

    private void setSectionColor(int amountOfSections, int LEDSPerSection, Supplier<Color>[] colors) {
        for (int i = 0; i < amountOfSections; i++) {
            CANDLE.setLEDs(
                    (int) (inverted ? colors[amountOfSections - i - 1].get().red : colors[i].get().red),
                    (int) (inverted ? colors[amountOfSections - i - 1].get().green : colors[i].get().green),
                    (int) (inverted ? colors[amountOfSections - i - 1].get().blue : colors[i].get().blue),
                    0,
                    LEDSPerSection * i + indexOffset,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : LEDSPerSection * (i + 1) - 1
            );
        }
    }
}