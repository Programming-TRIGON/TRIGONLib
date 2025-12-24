package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.trigon.lib.hardware.RobotHardwareStats;

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
            ledStrip.setDefaultCommand(LEDCommands.getDefaultAnimateCommand(defaultAnimationSettings, ledStrip));
    }

    /**
     * Applies the correct animation based on the type of LEDAnimationSettings.
     */
    static Runnable applyAnimation(LEDStrip ledStrip, LEDStripAnimationSettings.LEDAnimationSettings settings) {
        return () -> settings.apply(ledStrip);
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

    protected abstract void setSingleLEDColor(int index, Color color);

    private void addLEDStripToLEDStripsArray(LEDStrip ledStrip) {
        final LEDStrip[] newLEDStrips = new LEDStrip[LED_STRIPS.length + 1];
        System.arraycopy(LED_STRIPS, 0, newLEDStrips, 0, LED_STRIPS.length);
        newLEDStrips[LED_STRIPS.length] = ledStrip;
        LED_STRIPS = newLEDStrips;
    }
}