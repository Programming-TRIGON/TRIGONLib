package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A class that contains static functions for getting LED commands. These commands work with both types of LEDStrips.
 */
public class LEDCommands {
    /**
     * Gets a command that sets the color of the LED strip to the given color.
     *
     * @param color     the color to set the LED strip to
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getStaticColorCommand(Color color, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.staticColor(color)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that blinks the LED strip with a specific color.
     *
     * @param firstColor the color to blink
     * @param speed      how fast the LED strip should blink on a scale between 0 and 1
     * @param ledStrips  the LED strips to be used
     * @return the command
     */
    public static Command getBlinkingCommand(Color firstColor, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.blink(firstColor, speed)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that "breathes" a pocket of light across the LED strip.
     *
     * @param color                 the color to breathe
     * @param amountOfBreathingLEDs the amount of breathing LEDs between 1 and 7
     * @param speed                 the speed of the breathing on a scale between 0 and 1
     * @param inverted              whether the breathing should be inverted
     * @param bounceMode            when the pocket of LEDs should restart to the start of the strip
     * @param ledStrips             the LED strips to be used
     * @return the command
     */
    public static Command getBreatheCommand(Color color, int amountOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.breathe(color, amountOfBreathingLEDs, speed, inverted, bounceMode)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that flows a color through the LED strip.
     *
     * @param color     the color to flow through the LED strip
     * @param speed     how fast should the color travel the strip on a scale between 0 and 1
     * @param inverted  whether the flow should be inverted
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getColorFlowCommand(Color color, double speed, boolean inverted, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.colorFlow(color, speed, inverted)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that displays two colors in an alternating pattern on the LED strips.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     * @param ledStrips   the LED strips to be used
     * @return the command
     */
    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.alternateColor(firstColor, secondColor)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that colors the LED strips in sections.
     *
     * @param colors    an array of colors to set the sections to. The length of the array dictates the amount of sections
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.sectionColor(colors)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that displays a rainbow pattern on the LED strips.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a scale from 0 to 1
     * @param inverted   whether the rainbow should be inverted
     * @param ledStrips  the LED strips to be used
     * @return the command
     */
    public static Command getRainbowCommand(double brightness, double speed, boolean inverted, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.rainbow(brightness, speed, inverted)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip LEDStrip : ledStrips)
            action.accept(LEDStrip);
    }
}