package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

/**
 * A class that contains settings for the LED commands.
 */
public class AnimationSettings {
    /**
     * The settings for a command that sets an LED strip to a color.
     *
     * @param color the color to set the LED strip to
     */
    public record StaticColorSettings(Color color) {
    }

    /**
     * The settings for a command that blinks a color on and off on an LED strip.
     *
     * @param color the color to blink
     * @param speed the speed at which the LED strip should blink on a scale from 0 and 1
     */
    public record BlinkSettings(Color color, double speed) {
    }

    /**
     * The settings for a command that "breathes" LEDs along an LED strip.
     *
     * @param color                 the color of the breathing LEDs
     * @param numberOfBreathingLEDs the amount of breathing LEDs
     * @param speed                 the speed at which the color should travel throughout the strip on a scale from 0 and 1
     * @param inverted              whether the breathing should be inverted
     * @param bounceMode            when the breathing LEDs should restart their cycle throughout the strip
     */
    public record BreatheSettings(Color color, int numberOfBreathingLEDs, double speed, boolean inverted,
                                  LarsonAnimation.BounceMode bounceMode) {
    }

    /**
     * The settings for a command that flows a color throughout a LED strip.
     *
     * @param color    the color to flow throughout the LED strip
     * @param speed    the speed at which the color should travel throughout the strip on a scale from 0 and 1
     * @param inverted whether the color flow should be inverted
     */
    public record ColorFlowSettings(Color color, double speed, boolean inverted) {
    }

    /**
     * The settings for a command that flashes the LED strip between two different colors.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     */
    public record AlternateColorSettings(Color firstColor, Color secondColor) {
    }

    /**
     * The settings for a command that splits the LED strip into different colored sections.
     *
     * @param colors an array of the colors to color the sections with. The length of the array dictates the amount of sections
     */
    public record SectionColorSettings(Supplier<Color>[] colors) {
    }

    /**
     * The settings for a command that animates an LED strip as a rainbow.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a scale from 0 to 1
     * @param inverted   whether the rainbow should be inverted
     */
    public record RainbowSettings(double brightness, double speed, boolean inverted) {
    }
}
