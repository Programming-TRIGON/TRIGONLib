package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix6.controls.*;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.AnimationDirectionValue;
import com.ctre.phoenix6.signals.LarsonBounceValue;
import com.ctre.phoenix6.signals.RGBWColor;
import frc.trigon.lib.hardware.RobotHardwareStats;

import java.util.function.Supplier;

/**
 * A LED strip that is controlled by a CANdle, and uses AddressableLED for simulation.
 */
public class CANdleLEDStrip extends LEDStrip {
    private static CANdle CANDLE;
    private static int LAST_CREATED_LED_STRIP_ANIMATION_SLOT = 0;
    private final int animationSlot;
    private boolean shouldRunPeriodically = false;

    /**
     * Sets the CANdle instance to be used for controlling the LED strips. Must be set before using any LED strips. Should only be called once.
     * Must be configured before being set.
     *
     * @param candle the CANdle instance to be used
     */
    public static void setCANdle(CANdle candle) {
        if (CANDLE == null && !RobotHardwareStats.isSimulation())
            CANDLE = candle;
    }

    /**
     * Sets the total amount of LEDs in all LED strips for simulation.
     * Must be set before using any LED strips in simulation. Should only be called once.
     *
     * @param totalAmountOfLEDs the total amount of LEDs in all LED strips
     */
    public static void setTotalAmountOfLEDs(int totalAmountOfLEDs) {
        if (RobotHardwareStats.isSimulation() || RobotHardwareStats.isReplay())
            AddressableLEDStrip.initiateAddressableLED(0, totalAmountOfLEDs);
    }

    /**
     * Constructs a new CANdleLEDStrip. Before any commands are sent to the LED strip, the {@link CANdleLEDStrip#setCANdle(CANdle)} method must be called.
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
    public void periodic() {
        if (shouldRunPeriodically)
            currentAnimation.run();
    }

    @Override
    protected void clearLEDColors() {
        CANDLE.setControl(new EmptyAnimation(animationSlot));
    }

    @Override
    protected void blink(RGBWColor color, double speed) {
        shouldRunPeriodically = false;
        CANDLE.setControl(
                new SingleFadeAnimation(
                        indexOffset,
                        this.numberOfLEDs
                )
        );
    }

    @Override
    protected void staticColor(RGBWColor color) {
        shouldRunPeriodically = false;
        CANDLE.setControl(
                new SolidColor(
                        indexOffset,
                        this.numberOfLEDs
                )
                        .withColor(color)
        );
    }

    @Override
    protected void breathe(RGBWColor color, int numberOfBreathingLEDs, double speed, boolean inverted, LarsonBounceValue bounceMode) {
        shouldRunPeriodically = false;
        CANDLE.setControl(
                new LarsonAnimation(
                        indexOffset,
                        this.numberOfLEDs
                )
                        .withColor(color)
                        .withSize(numberOfBreathingLEDs)
                        .withFrameRate(speed)
                        .withBounceMode(bounceMode)
                        .withSlot(animationSlot)
        );
    }

    @Override
    protected void colorFlow(RGBWColor color, double speed, boolean inverted) {
        shouldRunPeriodically = false;
        final boolean correctedInverted = this.inverted != inverted;
        CANDLE.setControl(
                new ColorFlowAnimation(
                        indexOffset,
                        this.numberOfLEDs
                )
                        .withColor(color)
                        .withFrameRate(speed)
                        .withDirection(correctedInverted ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
                        .withSlot(animationSlot)
        );
    }

    @Override
    protected void rainbow(double brightness, double speed, boolean inverted) {
        shouldRunPeriodically = false;
        final boolean correctedInverted = this.inverted != inverted;
        CANDLE.setControl(
                new RainbowAnimation(
                        indexOffset,
                        this.numberOfLEDs
                )
                        .withBrightness(brightness)
                        .withFrameRate(speed)
                        .withDirection(correctedInverted ? AnimationDirectionValue.Backward : AnimationDirectionValue.Forward)
                        .withSlot(animationSlot)
        );
    }

    @Override
    protected void sectionColor(Supplier<RGBWColor>[] colors) {
        shouldRunPeriodically = true;
        final int ledsPerSection = (int) Math.floor((double) numberOfLEDs / colors.length);
        setSectionColor(colors.length, ledsPerSection, colors);
    }

    @Override
    protected void setSingleLEDColor(int index, RGBWColor color) {
        CANDLE.setControl(
                new SolidColor(
                        indexOffset + index,
                        indexOffset + index + 1
                )
                        .withColor(color)
        );
    }

    private void setSectionColor(int amountOfSections, int ledsPerSection, Supplier<RGBWColor>[] colors) {
        for (int i = 0; i < amountOfSections; i++) {
            CANDLE.setControl(
                    new SolidColor(
                            ledsPerSection * i + indexOffset,
                            i == amountOfSections - 1 ? numberOfLEDs + indexOffset : ledsPerSection * (i + 1) - 1
                    )
                            .withColor(inverted ? colors[amountOfSections - i - 1].get() : colors[i].get())
            );
        }
    }
}