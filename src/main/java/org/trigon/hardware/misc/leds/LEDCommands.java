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
    public static Command getStaticColorCommand(Color color, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.staticColor(color)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getBlinkingCommand(Color firstColor, double blinkingIntervalSeconds, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.blink(firstColor, blinkingIntervalSeconds)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getBreatheCommand(Color color, int amountOfBreathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getColorFlowCommand(Color color, double cycleTimeSeconds, boolean inverted, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.colorFlow(color, cycleTimeSeconds, inverted)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.alternateColor(firstColor, secondColor)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.sectionColor(colors)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    public static Command getRainbowCommand(double brightness, double speed, LEDStrip... LEDStrips) {
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs(LEDStrip -> LEDStrip.setCurrentAnimation(() -> LEDStrip.rainbow(brightness, speed)), LEDStrips);
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                LEDStrips
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... LEDStrips) {
        for (LEDStrip LEDStrip : LEDStrips)
            action.accept(LEDStrip);
    }
}