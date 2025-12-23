package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.util.Color;
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
        CANDLE.clearAnimation(animationSlot);
    }

    @Override
    protected void blink(Color color, double speed) {
        shouldRunPeriodically = false;
        CANDLE.animate(
                new SingleFadeAnimation(
                        (int) (color.red * 255),
                        (int) (color.green * 255),
                        (int) (color.blue * 255),
                        0,
                        speed,
                        this.numberOfLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    protected void staticColor(Color color) {
        shouldRunPeriodically = false;
        CANDLE.setLEDs(
                ((int) color.red * 255),
                ((int) color.green * 255),
                ((int) color.blue * 255),
                0,
                indexOffset,
                numberOfLEDs
        );
    }

    @Override
    protected void breathe(Color color, int numberOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        shouldRunPeriodically = false;
        CANDLE.animate(
                new LarsonAnimation(
                        (int) (color.red * 255),
                        (int) (color.green * 255),
                        (int) (color.blue * 255),
                        0,
                        speed,
                        this.numberOfLEDs,
                        bounceMode,
                        numberOfBreathingLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    protected void alternateColor(Color firstColor, Color secondColor) {
        shouldRunPeriodically = false;
        for (int i = 0; i < numberOfLEDs; i++)
            CANDLE.setLEDs(
                    (int) ((isEven(i) ? firstColor.red : secondColor.red) * 255),
                    (int) ((isEven(i) ? firstColor.green : secondColor.green) * 255),
                    (int) ((isEven(i) ? firstColor.blue : secondColor.blue) * 255),
                    0,
                    i + indexOffset,
                    1
            );
    }

    @Override
    protected void colorFlow(Color color, double speed, boolean inverted) {
        shouldRunPeriodically = false;
        final boolean correctedInverted = this.inverted != inverted;
        CANDLE.animate(
                new ColorFlowAnimation(
                        (int) (color.red * 255),
                        (int) (color.green * 255),
                        (int) (color.blue * 255),
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
    protected void rainbow(double brightness, double speed, boolean inverted) {
        shouldRunPeriodically = false;
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
    protected void sectionColor(Supplier<Color>[] colors) {
        shouldRunPeriodically = true;
        final int ledsPerSection = (int) Math.floor((double) numberOfLEDs / colors.length);
        setSectionColor(colors.length, ledsPerSection, colors);
    }

    @Override
    protected void setSingleLEDColor(int index, Color color) {
        CANDLE.setLEDs((int) color.red, (int) color.green, (int) color.blue, 0, index, 1);
    }

    private void setSectionColor(int amountOfSections, int ledsPerSection, Supplier<Color>[] colors) {
        for (int i = 0; i < amountOfSections; i++) {
            CANDLE.setLEDs(
                    (int) ((inverted ? colors[amountOfSections - i - 1].get().red : colors[i].get().red) * 255),
                    (int) ((inverted ? colors[amountOfSections - i - 1].get().green : colors[i].get().green) * 255),
                    (int) ((inverted ? colors[amountOfSections - i - 1].get().blue : colors[i].get().blue) * 255),
                    0,
                    ledsPerSection * i + indexOffset,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : ledsPerSection * (i + 1) - 1
            );
        }
    }

    private boolean isEven(int number) {
        return number % 2 == 0;
    }
}