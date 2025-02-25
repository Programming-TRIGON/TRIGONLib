package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.trigon.hardware.RobotHardwareStats;

import java.util.function.Supplier;

/**
 * A wrapper class for LED strips. This class provides a set of methods for controlling LED strips.
 */
public abstract class LEDStrip extends SubsystemBase {
    public static LEDStrip[] LED_STRIPS = new LEDStrip[0];
    protected final int indexOffset;
    protected final boolean inverted;
    protected final int numberOfLEDs;
    protected Runnable currentAnimation = () -> {
    };

    /**
     * Creates a new AddressableLEDStrip.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     * @return the created AddressableLEDStrip
     */
    public static AddressableLEDStrip createAddressableLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        return new AddressableLEDStrip(inverted, numberOfLEDs, indexOffset);
    }

    /**
     * Creates a new CANdleLEDStrip. In simulation or replay mode, an AddressableLEDStrip is created instead.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     * @return the created LEDStrip
     */
    public static LEDStrip createCANdleLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        if (RobotHardwareStats.isReplay() || RobotHardwareStats.isSimulation())
            return new AddressableLEDStrip(inverted, numberOfLEDs, indexOffset);
        return new CANdleLEDStrip(inverted, numberOfLEDs, indexOffset);
    }

    /**
     * Sets the default animation for all LED strips.
     *
     * @param defaultAnimationSettings the default animation settings to be set
     */
    public static void setDefaultAnimationForAllLEDS(LEDStripAnimationSettings.LEDAnimationSettings defaultAnimationSettings) {
        for (LEDStrip ledStrip : LED_STRIPS)
            ledStrip.setDefaultCommand(LEDCommands.getAnimateCommand(defaultAnimationSettings, ledStrip));
    }

    /**
     * Applies the correct animation based on the type of LEDAnimationSettings.
     */
    static Runnable applyAnimation(LEDStrip ledStrip, LEDStripAnimationSettings.LEDAnimationSettings settings) {
        if (settings instanceof LEDStripAnimationSettings.StaticColorSettings staticColorSettings)
            return () -> ledStrip.staticColor(staticColorSettings.color());
        if (settings instanceof LEDStripAnimationSettings.BlinkSettings blinkSettings)
            return () -> ledStrip.blink(blinkSettings.color(), blinkSettings.speed());
        if (settings instanceof LEDStripAnimationSettings.BreatheSettings breatheSettings)
            return () -> ledStrip.breathe(breatheSettings.color(), breatheSettings.numberOfBreathingLEDs(), breatheSettings.speed(), breatheSettings.inverted(), breatheSettings.bounceMode());
        if (settings instanceof LEDStripAnimationSettings.ColorFlowSettings colorFlowSettings)
            return () -> ledStrip.colorFlow(colorFlowSettings.color(), colorFlowSettings.speed(), colorFlowSettings.inverted());
        if (settings instanceof LEDStripAnimationSettings.AlternateColorSettings alternateColorSettings)
            return () -> ledStrip.alternateColor(alternateColorSettings.firstColor(), alternateColorSettings.secondColor());
        if (settings instanceof LEDStripAnimationSettings.SectionColorSettings sectionColorSettings)
            return () -> ledStrip.sectionColor(sectionColorSettings.colors());
        if (settings instanceof LEDStripAnimationSettings.RainbowSettings rainbowSettings)
            return () -> ledStrip.rainbow(rainbowSettings.brightness(), rainbowSettings.speed(), rainbowSettings.inverted());
        // TODO: I really don't like this but it'll do for now
        return () -> {
        };
    }

    protected LEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        this.inverted = inverted;
        this.numberOfLEDs = numberOfLEDs;
        this.indexOffset = indexOffset;

        addLEDStripToLEDStripsArray(this);
    }

    public int getNumberOfLEDS() {
        return numberOfLEDs;
    }

    protected void setCurrentAnimation(Runnable currentAnimation) {
        this.currentAnimation = currentAnimation;
        currentAnimation.run();
    }

    protected void resetLEDSettings() {
    }

    protected abstract void clearLEDColors();

    protected abstract void staticColor(Color color);

    protected abstract void blink(Color color, double speed);

    protected abstract void breathe(Color color, int numberOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode);

    protected abstract void colorFlow(Color color, double speed, boolean inverted);

    protected abstract void alternateColor(Color firstColor, Color secondColor);

    protected abstract void sectionColor(Supplier<Color>[] colors);

    protected abstract void rainbow(double brightness, double speed, boolean inverted);

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}