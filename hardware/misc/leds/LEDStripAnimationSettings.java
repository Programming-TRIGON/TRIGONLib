package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

/**
 * A class that contains settings for the LED commands.
 */
public class LEDStripAnimationSettings {
    public interface LEDAnimationSettings {
        void apply(LEDStrip ledStrip);
    }

    /**
     * The settings for a command that sets an LED strip to a single color.
     *
     * @param color the color to set the LED strip to
     */
    public record StaticColorSettings(Color color) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.staticColor(color);
        }
    }

    /**
     * The settings for a command that blinks a single color on and off on an LED strip.
     *
     * @param color the color to blink
     * @param speed the speed at which the LED strip should blink on a scale between 0 and 1
     */
    public record BlinkSettings(Color color, double speed) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.blink(color, speed);
        }
    }

    /**
     * The settings for a command that "breathes" LEDs along an LED strip.
     *
     * @param color                 the color of the breathing LEDs
     * @param numberOfBreathingLEDs the amount of breathing LEDs
     * @param speed                 the speed at which the color should travel throughout the strip on a scale between 0 and 1
     * @param inverted              whether the breathing should be inverted
     * @param bounceMode            when the breathing LEDs should restart their cycle throughout the strip
     */
    public record BreatheSettings(Color color, int numberOfBreathingLEDs, double speed, boolean inverted,
                                  LarsonAnimation.BounceMode bounceMode) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.breathe(color, numberOfBreathingLEDs, speed, inverted, bounceMode);
        }
    }

    /**
     * The settings for a command that flows a color throughout a LED strip.
     *
     * @param color    the color to flow throughout the LED strip
     * @param speed    the speed at which the color should travel throughout the strip on a scale between 0 and 1
     * @param inverted whether the color flow should be inverted
     */
    public record ColorFlowSettings(Color color, double speed, boolean inverted) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.colorFlow(color, speed, inverted);
        }
    }

    /**
     * The settings for a command that displays 2 colors in an alternating pattern.
     *
     * @param firstColor  the first color
     * @param secondColor the second color
     */
    public record AlternateColorSettings(Color firstColor, Color secondColor) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.alternateColor(firstColor, secondColor);
        }
    }

    /**
     * The settings for a command that splits the LED strip into different colored sections.
     *
     * @param colors an array of the colors to color the sections with. The length of the array dictates the amount of sections
     */
    public record SectionColorSettings(Supplier<Color>[] colors) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.sectionColor(colors);
        }
    }

    /**
     * The settings for a command that animates an LED strip as a rainbow.
     *
     * @param brightness the brightness of the rainbow on a scale from 0 to 1
     * @param speed      the speed of the rainbow's movement on a scale between 0 and 1
     * @param inverted   whether the rainbow should be inverted
     */
    public record RainbowSettings(double brightness, double speed, boolean inverted) implements LEDAnimationSettings {
        @Override
        public void apply(LEDStrip ledStrip) {
            ledStrip.rainbow(brightness, speed, inverted);
        }
    }
}