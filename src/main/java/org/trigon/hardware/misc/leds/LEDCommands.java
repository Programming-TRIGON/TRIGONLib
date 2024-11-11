package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import org.trigon.commands.ExecuteEndCommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LEDCommands {
    public static Command getStaticColorCommand(Color color, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs((LEDStrip -> LEDStrip.staticColor(color)), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips)
        ).ignoringDisable(true);
    }

    public static Command getBlinkingCommand(Color firstColor, Color secondColor, double blinkingIntervalSeconds, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs((LEDStrip -> LEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds)), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips)
        ).ignoringDisable(true);
    }

    public static Command getBreatheCommand(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... ledStrips) {
        return new FunctionalCommand(
                () -> {
                    if (!shouldLoop)
                        runForLEDs(LEDStrip::resetLEDSettings, ledStrips);
                },
                () -> runForLEDs((LEDStrip) -> LEDStrip.breathe(color, breathingLEDs, cycleTimeSeconds, shouldLoop, inverted, bounceMode), ledStrips),
                (interrupted) -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                () -> false,
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getColorFlowCommand(Color color, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LEDStrip... ledStrips) {
        return new FunctionalCommand(
                () -> {
                    if (!shouldLoop)
                        runForLEDs(LEDStrip::resetLEDSettings, ledStrips);
                },
                () -> runForLEDs((LEDStrip) -> LEDStrip.colorFlow(color, cycleTimeSeconds, shouldLoop, inverted), ledStrips),
                (interrupted) -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                () -> false,
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, double intervalSeconds, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs(LEDStrip -> LEDStrip.alternateColor(firstColor, secondColor, intervalSeconds), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrip.LED_STRIPS)
        ).ignoringDisable(true);
    }

    public static Command getSectionColorCommand(int amountOfSections, Supplier<Color>[] colors, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs((LEDStrip) -> LEDStrip.sectionColor(amountOfSections, colors), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips)
        ).ignoringDisable(true);
    }

    public static Command getRainbowCommand(double brightness, double speed, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs(LEDStrip -> LEDStrip.rainbow(brightness, speed), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips)
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip LEDStrip : ledStrips)
            action.accept(LEDStrip);
    }
}