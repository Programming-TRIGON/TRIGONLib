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
    public static Command getStaticColorCommand(Color color, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.staticColor(color)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.staticColor(color)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getBlinkingCommand(Color firstColor, Color secondColor, double blinkingIntervalSeconds, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.blink(firstColor, secondColor, blinkingIntervalSeconds)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getBreatheCommand(Color color, int amountOfBreathingLEDs, double cycleTimeSeconds, boolean inverted, LarsonAnimation.BounceMode bounceMode, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.breathe(color, amountOfBreathingLEDs, cycleTimeSeconds, inverted, bounceMode)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getColorFlowCommand(Color color, double cycleTimeSeconds, boolean inverted, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.colorFlow(color, cycleTimeSeconds, inverted)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.colorFlow(color, cycleTimeSeconds, inverted)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, double intervalSeconds, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.alternateColor(firstColor, secondColor, intervalSeconds)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.alternateColor(firstColor, secondColor, intervalSeconds)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.sectionColor(colors)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.sectionColor(colors)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    public static Command getRainbowCommand(double brightness, double speed, LEDStrip... LEDStrips) {
        if (isAddressableLEDStrip(LEDStrips[0]))
            return new ExecuteEndCommand(
                    () -> runForLEDs((LEDStrip -> LEDStrip.rainbow(brightness, speed)), LEDStrips),
                    () -> runForLEDs(LEDStrip::clearLEDColors, LEDStrips),
                    LEDStrips
            ).ignoringDisable(true);
        return new StartEndCommand(
                () -> {
                    runForLEDs((LEDStrip::clearLEDColors), LEDStrips);
                    runForLEDs((LEDStrip -> LEDStrip.rainbow(brightness, speed)), LEDStrips);
                },
                () -> {
                }
        ).ignoringDisable(true);
    }

    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... LEDStrips) {
        for (LEDStrip LEDStrip : LEDStrips)
            action.accept(LEDStrip);
    }

    public static boolean isAddressableLEDStrip(LEDStrip LEDStrip) {
        return LEDStrip instanceof AddressableLEDStrip || RobotHardwareStats.isSimulation();
    }
}