package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A class that contains commands to control all kinds of LEDs.
 */
public class LEDCommands {
    /**
     * Gets a command that applies animation settings to the LED strips.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to animate
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
     * Gets a command that applies animation settings to the LED strips in an LED board.
     *
     * @param settings the settings for the desired animation
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getAnimateCommand(LEDStripAnimationSettings.LEDAnimationSettings settings, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(LEDStrip.applyAnimation(ledStrip, settings)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to a static color
     *
     * @param color     the color the LED strips will be set to
     * @param ledStrips the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips in an LED board to a static color
     *
     * @param color    the color the LED strips will be set to
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getStaticColorCommand(Color color, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.staticColor(color)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to blink a single color on and off.
     *
     * @param color     the color to blink on and off
     * @param speed     the speed at which the LED strips will alternate between on and off from 0 to 1
     * @param ledStrips the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips in an LED board to blink a single color on and off.
     *
     * @param color    the color to blink on and off
     * @param speed    the speed at which the LED strips will alternate between on and off from 0 to 1
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getBlinkCommand(Color color, double speed, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.blink(color, speed)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that "breathes" LEDs along the LED strips.
     *
     * @param color                 the color of the breathing LEDs
     * @param numberOfBreathingLEDs the amount of breathing LEDs
     * @param speed                 the speed at which the color should travel throughout the strip from 0 to 1
     * @param inverted              whether the breathing should be inverted or not
     * @param bounceMode            where the breathing LEDs should restart their cycle throughout the strip
     * @param ledStrips             the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that "breathes" LEDs along the LED strips in an LED board.
     *
     * @param color                 the color of the breathing LEDs
     * @param numberOfBreathingLEDs the amount of breathing LEDs in each strip
     * @param speedLEDsPerSecond    the amount of LEDs the animation should move by per second
     * @param ledSpacing            the lateral distance in LEDs of each layer's moving LEDs from the next layer
     * @param inverted              whether the breathing should be inverted or not
     * @param ledBoard              the LED board to animate
     * @return the command
     */
    public static Command getBreatheCommand(Color color, int numberOfBreathingLEDs, int speedLEDsPerSecond, int ledSpacing, boolean inverted, LEDBoard ledBoard) {
        return new FunctionalCommand(
                () -> ledBoard.breathe(color, numberOfBreathingLEDs, speedLEDsPerSecond, ledSpacing, inverted),
                ledBoard::updateBreathingPeriodically,
                (interrupted) -> ledBoard.clearBoard(),
                () -> false,
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that flows a single color through the LED strips.
     *
     * @param color     the color to flow through the LED strips
     * @param speed     the speed at which the color flows through the LED strips from 0 to 1
     * @param inverted  whether the breathing should be inverted or not
     * @param ledStrips the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that flows a single color through the LED strips in an LED board.
     *
     * @param color    the color to flow through the LED strips
     * @param speed    the speed at which the color flows through each LED strip from 0 to 1
     * @param inverted whether the breathing should be inverted or not
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getColorFlowCommand(Color color, double speed, boolean inverted, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.colorFlow(color, speed, inverted)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that displays 2 colors in an alternating pattern on the LED strips.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     * @param ledStrips   the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that displays 2 colors in an alternating pattern on the LED strips in an LED board.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     * @param ledBoard    the LED board to animate
     * @return the command
     */
    public static Command getAlternateColorCommand(Color firstColor, Color secondColor, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.alternateColor(firstColor, secondColor)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sections the LED strips into multiple different colors.
     *
     * @param colors    an array of the colors to color the sections with. The length of the array dictates the amount of sections
     * @param ledStrips the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sections the LED strips in an LED board with different colors.
     *
     * @param colors   an array of the colors to section the strips with. The length of the array dictates the amount of sections
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getSectionColorCommand(Supplier<Color>[] colors, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.sectionColor(colors)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips to a moving rainbow pattern.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a between 0 and 1
     * @param ledStrips  the LED strips to animate
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
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets the LED strips in an LED board to a moving rainbow pattern.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a between 0 and 1
     * @param ledBoard   the LED board to animate
     * @return the command
     */
    public static Command getRainbowCommand(double brightness, double speed, boolean inverted, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> {
                    runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips());
                    runForLEDs(ledStrip -> ledStrip.setCurrentAnimation(() -> ledStrip.rainbow(brightness, speed, inverted)), ledBoard.getLEDStrips());
                },
                () -> runForLEDs(LEDStrip::clearLEDColors, ledBoard.getLEDStrips()),
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that sets an LED board to display an image
     *
     * @param filePath the path to the image to display
     * @param ledBoard the LED board to animate
     * @return the command
     */
    public static Command getSetImageCommand(String filePath, LEDBoard ledBoard) {
        return new StartEndCommand(
                () -> ledBoard.setImage(filePath),
                ledBoard::clearBoard,
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that animates an LED board with a series of images.
     *
     * @param filePaths           the path to each image in the animation in order
     * @param framesPerSecond     the amount of frames to display each second
     * @param shouldLoop          whether the animation should repeat after going through all frames or not
     * @param shouldKeepLastFrame whether the last frame should remain displayed or if it should be cleared on end
     * @param ledBoard            the LED board to animate
     * @return the command
     */
    public static Command getSetBoardAnimationCommand(String[] filePaths, int framesPerSecond, boolean shouldLoop, boolean shouldKeepLastFrame, LEDBoard ledBoard) {
        return new FunctionalCommand(
                () -> ledBoard.setAnimation(filePaths, framesPerSecond),
                ledBoard::updateAnimationPeriodically,
                (interrupted) -> {
                    ledBoard.resetAnimation();
                    if (!shouldKeepLastFrame)
                        ledBoard.clearBoard();
                },
                () -> false,
                ledBoard
        ).ignoringDisable(true).until(() -> !shouldLoop && ledBoard.hasAnimationEnded());
    }

    /**
     * Gets a command that animated an LED board so that each strip "bounces" LEDs from side to side.
     *
     * @param color              the color to bounce around the LED strips
     * @param numberOfMovingLEDs the number of LEDs to bounce in each strip
     * @param speedLEDsPerSecond the number of LEDs to move per second
     * @param ledSpacing         the distance that the LEDs should move along the strip per movement. Must fit evenly into the number of LEDs per strip
     * @param stripSpacing       the amount of LED strips to skip over in between active LED strips
     * @param ledBoard           the LED board to animate
     * @return the command
     */
    public static Command getSetBoardBounceCommand(Color color, int numberOfMovingLEDs, int speedLEDsPerSecond, int ledSpacing, int stripSpacing, LEDBoard ledBoard) {
        return new FunctionalCommand(
                () -> ledBoard.bounce(color, numberOfMovingLEDs, speedLEDsPerSecond, ledSpacing, stripSpacing),
                ledBoard::updateBouncingPeriodically,
                (interrupted) -> ledBoard.clearBoard(),
                () -> false,
                ledBoard
        ).ignoringDisable(true);
    }

    /**
     * Gets a command that applies the specified animation settings to the LED strips.
     *
     * @param settings  the settings for the desired animation
     * @param ledStrips the LED strips to animate
     * @return the command
     */
    public static Command getDefaultAnimateCommand(LEDStripAnimationSettings.LEDAnimationSettings settings, LEDStrip... ledStrips) {
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
     * Runs an action on all LED strips.
     */
    private static void runForLEDs(Consumer<LEDStrip> action, LEDStrip... ledStrips) {
        if (ledStrips.length == 0)
            ledStrips = LEDStrip.LED_STRIPS;

        for (LEDStrip ledStrip : ledStrips)
            action.accept(ledStrip);
    }
}