package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.util.Color;
import org.trigon.hardware.RobotHardwareStats;

import java.util.function.Supplier;

/**
 * A LED strip that is controlled by a CANdle, and uses AddressableLED for simulation.
 */
public class CANdleLEDStrip extends LEDStrip {
    private static CANdle CANDLE;
    private static int LAST_CREATED_LED_STRIP_ANIMATION_SLOT = 0;
    private final int animationSlot;

    /**
     * Sets the CANdle instance to be used for controlling the LED strips. Must be set before using any LED strips. Should only be called once
     *
     * @param candle the CANdle instance to be used
     */
    public static void setCANdle(CANdle candle) {
        if (CANDLE == null)
            CANDLE = candle;
    }

    public static void setTotalAmountOfLEDs(int totalAmountOfLEDs) {
        if (RobotHardwareStats.isSimulation() || RobotHardwareStats.isReplay())
            AddressableLEDStrip.initiateAddressableLED(0, totalAmountOfLEDs);
    }

    /**
     * Constructs a new CANdleLEDStrip. Before any commands are sent to the LED strip, the setCANdle method must be called.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     */
    CANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        super(inverted, numberOfLEDs, indexOffset);
        animationSlot = LAST_CREATED_LED_STRIP_ANIMATION_SLOT;
        LAST_CREATED_LED_STRIP_ANIMATION_SLOT++;
    }

    @Override
    void clearLEDColors() {
        CANDLE.clearAnimation(animationSlot);
    }

    @Override
    void blink(Color firstColor, double speed) {
        CANDLE.animate(
                new SingleFadeAnimation(
                        (int) firstColor.red,
                        (int) firstColor.green,
                        (int) firstColor.blue,
                        0,
                        speed,
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
    void breathe(Color color, int amountOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        CANDLE.animate(
                new LarsonAnimation(
                        (int) color.red,
                        (int) color.green,
                        (int) color.blue,
                        0,
                        speed,
                        this.numberOfLEDs,
                        bounceMode,
                        amountOfBreathingLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void alternateColor(Color firstColor, Color secondColor) {
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

    @Override
    void colorFlow(Color color, double speed, boolean inverted) {
        final boolean correctedInverted = this.inverted != inverted;
        CANDLE.animate(
                new ColorFlowAnimation(
                        (int) color.red,
                        (int) color.green,
                        (int) color.blue,
                        0,
                        speed,
                        this.numberOfLEDs,
                        correctedInverted ? ColorFlowAnimation.Direction.Backward : ColorFlowAnimation.Direction.Forward,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void rainbow(double brightness, double speed, boolean inverted) {
        final boolean correctedInverted = this.inverted != inverted;
        CANDLE.animate(
                new RainbowAnimation(
                        brightness,
                        speed,
                        this.numberOfLEDs,
                        correctedInverted,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void sectionColor(Supplier<Color>[] colors) {
        final int LEDSPerSection = (int) Math.floor(numberOfLEDs / colors.length);
        setSectionColor(colors.length, LEDSPerSection, colors);
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