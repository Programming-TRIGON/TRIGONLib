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
    public static void setCandle(CANdle candle) {
        CANDLE = candle;
    }

    /**
     * Sets the simulation AddressableLED instance to be used for testing in simulation. Must be set before using any LED strips in simulation.
     *
     * @param simulationLEDStrip the AddressableLED instance to be used in simulation
     */
    public static void setSimulationLED(AddressableLED simulationLEDStrip) {
        AddressableLEDStrip.setLED(simulationLEDStrip);
    }

    /**
     * Sets the simulation AddressableLEDBuffer instance to be used for testing in simulation. Must be set before using any LED strips in simulation.
     *
     * @param simulationLEDBuffer the AddressableLED buffer instance to be used in simulation
     */
    public static void setSimulationLEDBuffer(AddressableLEDBuffer simulationLEDBuffer) {
        AddressableLEDStrip.setLEDBuffer(simulationLEDBuffer);
    }

    /**
     * Constructs a new CANdleLEDStrip. Before any commands are sent to the LED strip, the setLED method must be called.
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
    public void periodic() {
        if (RobotHardwareStats.isSimulation())
            simulationLEDStrip.periodic();
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
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds));
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
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.staticColor(color));
            return;
        }
        CANDLE.setLEDs((int) color.red, (int) color.green, (int) color.blue, 0, indexOffset, numberOfLEDs);
    }

    @Override
    void breathe(Color color, int amountOfBreathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode);
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode));
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
                        amountOfBreathingLEDs,
                        indexOffset
                ),
                animationSlot
        );
    }

    @Override
    void alternateColor(Color firstColor, Color secondColor) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.alternateColor(firstColor, secondColor);
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.alternateColor(firstColor, secondColor));
            return;
        }
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
    void colorFlow(Color color, double cycleTimeSeconds, boolean inverted) {
        if (RobotHardwareStats.isSimulation()) {
            simulationLEDStrip.colorFlow(color, cycleTimeSeconds, inverted);
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.colorFlow(color, cycleTimeSeconds, inverted));
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
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.rainbow(brightness, speed));
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
            simulationLEDStrip.setCurrentAnimation(() -> simulationLEDStrip.sectionColor(colors));
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