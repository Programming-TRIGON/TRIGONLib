package frc.trigon.robot.subsystems.ledstrip;

import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.trigon.robot.commands.InitExecuteCommand;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LEDStripCommands {
    public static Command getStaticColorCommand(Color color, LEDStrip... ledStrips) {
        return new StartEndCommand(
                () -> {
                    getClearAnimationsRunnable(ledStrips).run();
                    runForEach((strip) -> strip.staticColor(color), ledStrips).run();
                },
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getThreeSectionColorCommand(Supplier<Color> firstSectionColor, Supplier<Color> secondSectionColor, Supplier<Color> thirdSectionColor, LEDStrip... ledStrips) {
        return new InitExecuteCommand(
                getClearAnimationsRunnable(ledStrips),
                runForEach((strip) -> strip.threeSectionColor(firstSectionColor.get(), secondSectionColor.get(), thirdSectionColor.get()), ledStrips),
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateFireCommand(double brightness, double speed, double sparking, double cooling, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateFire(brightness, speed, sparking, cooling), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateRainbowCommand(double brightness, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateRainbow(brightness, speed), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateColorFlowCommand(Color color, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateColorFlow(color, speed), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateLarsonCommand(Color color, double speed, LarsonAnimation.BounceMode mode, int size, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateLarson(color, speed, mode, size), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateRGBFadeCommand(double brightness, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateRGBFade(brightness, speed), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateSingleFadeCommand(Color color, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateSingleFade(color, speed), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateTwinkleCommand(Color color, double speed, TwinkleAnimation.TwinklePercent divider, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateTwinkle(color, speed, divider), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    public static Command getAnimateStrobeCommand(Color color, double speed, LEDStrip... ledStrips) {
        return new StartEndCommand(
                runForEach((strip) -> strip.animateStrobe(color, speed), ledStrips),
                () -> {
                },
                ledStrips
        ).ignoringDisable(true);
    }

    private static Runnable getClearAnimationsRunnable(LEDStrip... ledStrips) {
        return runForEach(LEDStrip::clearAnimation, ledStrips);
    }

    private static Runnable runForEach(Consumer<LEDStrip> toRun, LEDStrip... ledStrips) {
        return () -> Arrays.stream(ledStrips).forEach(toRun);
    }
}
