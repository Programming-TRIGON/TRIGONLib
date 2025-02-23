package org.trigon.hardware.misc.leds;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

import java.util.function.Consumer;

/**
 * A class that contains static functions for getting LED commands. These commands work with both types of LEDStrips.
 */
public class LEDCommands {
    /**
     * Gets a command that sets the LED strips to a single color.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.StaticColorSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to blink a single color on and off.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.BlinkSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that "breathes" LEDs along the LED strips.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.BreatheSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that flows a single color through the LED strips.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.ColorFlowSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to alternate between two colors.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.AlternateColorSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sections the LED strips into multiple different colors.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.SectionColorSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to a moving rainbow pattern.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to be used
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.RainbowSettings settings, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.animate(settings)), ledStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip ledStrip : ledStrips)
            action.accept(ledStrip);
    }
}