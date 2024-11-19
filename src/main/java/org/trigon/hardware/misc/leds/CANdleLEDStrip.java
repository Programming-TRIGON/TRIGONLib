package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.*;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
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
    private final AddressableLEDStrip simulationLEDStrip;

    /**
     * Sets the CANdle instance to be used for controlling the LED strips. Must be set before using any LED strips.
     *
     * @param candle the CANdle instance to be used
     */
    public static void setCANdle(CANdle candle) {
        if (CANDLE == null)
            CANDLE = candle;
    }

    /**
     * Sets the simulation AddressableLED instance to be used for testing in simulation. Must be set before using any LED strips in simulation.
     *
     * @param simulationLEDStrip the AddressableLED instance to be used in simulation
     */
    public static void setSimulationLED(AddressableLED simulationLEDStrip) {
        AddressableLEDStrip.setAddressableLED(simulationLEDStrip);
    }

    /**
     * Sets the simulation AddressableLEDBuffer instance to be used for testing in simulation. Must be set before using any LED strips in simulation.
     *
     * @param simulationLEDBuffer the AddressableLED buffer instance to be used in simulation
     */
    public static void setSimulationLEDBuffer(AddressableLEDBuffer simulationLEDBuffer) {
        AddressableLEDStrip.setAddressableLEDBuffer(simulationLEDBuffer);
    }

    /**
     * Constructs a new CANdleLEDStrip. Before any commands are sent to the LED strip, the setCANdle method must be called.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     */
    public CANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        super(inverted, numberOfLEDs, indexOffset);
        animationSlot = LAST_CREATED_LED_STRIP_ANIMATION_SLOT;
        LAST_CREATED_LED_STRIP_ANIMATION_SLOT++;
        this.simulationLEDStrip = new AddressableLEDStrip(inverted, numberOfLEDs, indexOffset);
    }

    @Override
    public void simulationPeriodic() {
        simulationLEDStrip.periodic();
    }

    @Override
    void clearLEDColors() {
        if (runIfInSimulation(simulationLEDStrip::clearLEDColors))
            return;

        CANDLE.clearAnimation(animationSlot);
    }

    @Override
    void blink(Color firstColor, double speed) {
        if (runIfInSimulation(() -> simulationLEDStrip.blink(firstColor, speed)))
            return;

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
        if (runIfInSimulation(() -> simulationLEDStrip.staticColor(color)))
            return;

        CANDLE.setLEDs((int) color.red, (int) color.green, (int) color.blue, 0, indexOffset, numberOfLEDs);
    }

    @Override
    void breathe(Color color, int amountOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        if (runIfInSimulation(() -> simulationLEDStrip.breathe(color, amountOfBreathingLEDs, speed, inverted, bounceMode)))
            return;

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
        if (runIfInSimulation(() -> simulationLEDStrip.alternateColor(firstColor, secondColor)))
            return;

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
        if (runIfInSimulation(() -> simulationLEDStrip.colorFlow(color, speed, inverted)))
            return;

        boolean correctedInverted = this.inverted != inverted;
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
    void rainbow(double brightness, double speed) {
        if (runIfInSimulation(() -> simulationLEDStrip.rainbow(brightness, speed)))
            return;

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
        if (runIfInSimulation(() -> simulationLEDStrip.sectionColor(colors)))
            return;

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

    private boolean runIfInSimulation(Runnable simulationAction) {
        if (RobotHardwareStats.isSimulation()) {
            setCurrentAnimation(simulationAction);
            return true;
        }
        return false;
    }
}