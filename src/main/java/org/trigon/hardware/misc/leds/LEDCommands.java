package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import edu.wpi.first.wpilibj2.command.Command;
import org.trigon.commands.ExecuteEndCommand;

import java.awt.*;
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

    public static Command getBreathingCommand(Color color, int breathingLEDs, double cycleTimeSeconds, boolean shouldLoop, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... ledStrips) {
        return new ExecuteEndCommand(
                () -> runForLEDs((LEDStrip -> LEDStrip.breath(color, breathingLEDs, cycleTimeSeconds, shouldLoop, inverted, bounceMode)), ledStrips),
                () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips)
        ).ignoringDisable(true);
    }

    public static Command getTwinkleCommand(Color firstColor, Color secondColor, double intervalSeconds, TwinkleAnimation.TwinklePercent divider) {
        return new ExecuteEndCommand(
                () -> runForLEDs(LEDStrip -> LEDStrip.twinkle(firstColor, secondColor, intervalSeconds, divider), LEDStrip.LED_STRIPS),
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

    public static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip LEDStrip : ledStrips)
            action.accept(LEDStrip);
    }
}
