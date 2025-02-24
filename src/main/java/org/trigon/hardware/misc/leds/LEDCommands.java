package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LEDCommands {
    /**
     * Gets a command that applies the specified animation settings to the LED strips.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.LEDAnimationSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(LEDStrip.applyAnimation(ledStrip, settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets LED strips to a specific color
     *
     * @param color     the color the LED strips will be set to
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getStaticColorCommand(Color color, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.staticColor(color)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that sets the LED strips to blink a single color on and off.
     *
     * @param color     the color to blink on and off
     * @param speed     the speed at which the LED strips will alternate between being on and off on a scale between 0 and 1
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getBlinkCommand(Color color, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.blink(color, speed)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that "breathes" LEDs along the LED strips.
     *
     * @param color                 the color of the breathing LEDs
     * @param numberOfBreathingLEDs the amount of breathing LEDs
     * @param speed                 the speed at which the color should travel throughout the strip on a scale between 0 and 1
     * @param inverted              whether the breathing should be inverted
     * @param bounceMode            when the breathing LEDs should restart their cycle throughout the strip     * @param ledStrips             the LED strips to be used
     * @return the command
     */
    public static Command getBreatheCommand(Color color, int numberOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.breathe(color, numberOfBreathingLEDs, speed, inverted, bounceMode)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that flows a single color through the LED strips.
     *
     * @param color     the color that flows through the LED strips
     * @param speed     the speed at which the color flows through the LED strips on a scale between 0 and 1
     * @param inverted  whether the breathing should be inverted
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getColorFlowCommand(Color color, double speed, boolean inverted, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.colorFlow(color, speed, inverted)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that displays 2 colors in an alternating pattern on the LED strips.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     * @param ledStrips   the LED strips to be used
     * @return the command
     */
    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.alternateColor(firstColor, secondColor)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that sections the LED strips into multiple different colors.
     *
     * @param colors    an array of the colors to color the sections with. The length of the array dictates the amount of sections
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.sectionColor(colors)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Gets a command that sets the LED strips to a moving rainbow pattern.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a between 0 and 1
     * @param ledStrips  the LED strips to be used
     * @return the command
     */
    public static Command getRainbowCommand(double brightness, double speed, boolean inverted, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.rainbow(brightness, speed, inverted)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        );
    }

    /**
     * Runs an action on all LED strips.
     */
    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip ledStrip : ledStrips) {
            action.accept(ledStrip);
        }
    }
}
