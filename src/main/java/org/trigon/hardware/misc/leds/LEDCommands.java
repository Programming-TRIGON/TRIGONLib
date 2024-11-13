package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import org.trigon.commands.ExecuteEndCommand;
import org.trigon.hardware.RobotHardwareStats;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LEDCommands {
    public static Command getStaticColorCommand(Color color, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.staticColor(color)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.staticColor(color)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getBlinkingCommand(Color firstColor, Color secondColor, double blinkingIntervalSeconds, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getBreatheCommand(Color color, int amountOfBreathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getColorFlowCommand(Color color, double cycleTimeSeconds, boolean inverted, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.colorFlow(color, cycleTimeSeconds, inverted)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.colorFlow(color, cycleTimeSeconds, inverted)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, double intervalSeconds, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.alternateColor(firstColor, secondColor, intervalSeconds)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.alternateColor(firstColor, secondColor, intervalSeconds)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.sectionColor(colors)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.sectionColor(colors)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getRainbowCommand(double brightness, double speed, LEDStrip... ledStrips) {
        if (isAddressableLED(ledStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.rainbow(brightness, speed)), ledStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, ledStrips),
                    ledStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), ledStrips);
                    runForLEDs((LEDStrip -> LEDStrip.rainbow(brightness, speed)), ledStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        for (LEDStrip LEDStrip : ledStrips)
            action.accept(LEDStrip);
    }

    public static boolean isAddressableLED(LEDStrip ledStrip) {
        return ledStrip instanceof AddressableLEDStrip || RobotHardwareStats.isSimulation();
    }
}