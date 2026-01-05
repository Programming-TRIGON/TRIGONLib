package frc.trigon.lib.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

/**
 * A LED strip that is controlled by an AddressableLED.
 */
public class AddressableLEDStrip extends LEDStrip {
    private static AddressableLED LED;
    private static AddressableLEDBuffer LED_BUFFER;

    private int lastBreatheLED;
    private double lastLEDAnimationChangeTime = 0;
    private double rainbowFirstPixelHue = 0;
    private boolean isLEDAnimationChanged = false;
    private int amountOfColorFlowLEDs = 0;

    /**
     * Sets and configures the AddressableLED and AddressableLEDBuffer instances to be used for controlling the LED strip.
     * Must be set before using any LED strips. Should only be called once.
     *
     * @param port              the port of the LED strip
     * @param totalAmountOfLEDs the total amount of LEDs in all LED strips
     */
    public static void initiateAddressableLED(int port, int totalAmountOfLEDs) {
        if (LED_BUFFER == null)
            LED_BUFFER = new AddressableLEDBuffer(totalAmountOfLEDs);

        if (LED == null) {
            LED = new AddressableLED(port);
            LED.setLength(totalAmountOfLEDs);
            LED.start();
        }
    }

    /**
     * Constructs a new AddressableLEDStrip. Before any commands are sent to the LED strip, the {@link AddressableLEDStrip#initiateAddressableLED(int, int)} method must be called.
     *
     * @param inverted     whether the LED strip is inverted
     * @param numberOfLEDs the amount of LEDs in the strip
     * @param indexOffset  the offset of the first LED in the strip
     */
    AddressableLEDStrip(boolean inverted, int numberOfLEDs, int indexOffset) {
        super(inverted, numberOfLEDs, indexOffset);
        resetLEDSettings();
    }

    @Override
    public void periodic() {
        currentAnimation.run();
        LED.setData(LED_BUFFER);
    }

    @Override
    protected void clearLEDColors() {
        setStaticColor(Color.kBlack);
    }

    @Override
    protected void blink(Color color, double speed) {
        final double correctedSpeed = 1 - speed;
        final double currentTime = Timer.getTimestamp();

        if (currentTime - lastLEDAnimationChangeTime > correctedSpeed) {
            lastLEDAnimationChangeTime = currentTime;
            isLEDAnimationChanged = !isLEDAnimationChanged;
        }

        if (isLEDAnimationChanged) {
            setStaticColor(color);
            return;
        }
        clearLEDColors();
    }

    @Override
    protected void staticColor(Color color) {
        setStaticColor(color);
    }

    @Override
    protected void breathe(Color color, int numberOfBreathingLEDs, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        clearLEDColors();
        final boolean correctedInverted = this.inverted != inverted;
        final double moveLEDTimeSeconds = 1 - speed;
        final double currentTime = Timer.getTimestamp();

        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (correctedInverted)
                lastBreatheLED--;
            else
                lastBreatheLED++;
        }

        checkIfBreathingHasHitEnd(numberOfBreathingLEDs, correctedInverted, bounceMode);
        setBreathingLEDs(color, numberOfBreathingLEDs, bounceMode);
    }

    @Override
    protected void colorFlow(Color color, double speed, boolean inverted) {
        clearLEDColors();
        final boolean correctedInverted = this.inverted != inverted;
        final double moveLEDTimeSeconds = 1 - speed;
        final double currentTime = Timer.getTimestamp();

        if (currentTime - lastLEDAnimationChangeTime > moveLEDTimeSeconds) {
            lastLEDAnimationChangeTime = currentTime;
            if (isLEDAnimationChanged)
                amountOfColorFlowLEDs--;
            else
                amountOfColorFlowLEDs++;
        }

        checkIfColorFlowHasHitEnd();
        setLEDColors(color, correctedInverted ? numberOfLEDs - amountOfColorFlowLEDs - 1 : 0, correctedInverted ? numberOfLEDs - 1 : amountOfColorFlowLEDs);
    }

    @Override
    protected void alternateColor(Color firstColor, Color secondColor) {
        for (int i = 0; i < numberOfLEDs; i++)
            LED_BUFFER.setLED(i + indexOffset, i % 2 == 0 ? firstColor : secondColor);
    }

    @Override
    protected void rainbow(double brightness, double speed, boolean inverted) {
        final boolean correctedInverted = this.inverted != inverted;
        final int adjustedBrightness = (int) (brightness * 255);
        final int hueIncrement = (int) (speed * 8);

        for (int led = 0; led < numberOfLEDs; led++) {
            final int hue = (int) (rainbowFirstPixelHue + (led * 180 / numberOfLEDs) % 180);
            LED_BUFFER.setHSV(led + indexOffset, hue, 255, adjustedBrightness);
        }

        if (correctedInverted) {
            rainbowFirstPixelHue -= hueIncrement;
            if (rainbowFirstPixelHue < 0)
                rainbowFirstPixelHue += 180;
            return;
        }
        rainbowFirstPixelHue += hueIncrement;
        rainbowFirstPixelHue %= 180;
    }

    @Override
    protected void sectionColor(Supplier<Color>[] colors) {
        final int amountOfSections = colors.length;
        final int ledsPerSection = (int) Math.floor((double) numberOfLEDs / amountOfSections);

        for (int i = 0; i < amountOfSections; i++)
            setLEDColors(
                    inverted ? colors[amountOfSections - i - 1].get() : colors[i].get(),
                    ledsPerSection * i,
                    i == amountOfSections - 1 ? numberOfLEDs - 1 : ledsPerSection * (i + 1) - 1
            );
    }

    @Override
    protected void resetLEDSettings() {
        lastBreatheLED = indexOffset;
        lastLEDAnimationChangeTime = Timer.getTimestamp();
        rainbowFirstPixelHue = 0;
        isLEDAnimationChanged = false;
        amountOfColorFlowLEDs = 0;
    }

    @Override
    protected void setSingleLEDColor(int index, Color color) {
        LED_BUFFER.setLED(indexOffset + index, color);
    }

    private void setStaticColor(Color color) {
        setLEDColors(color, 0, numberOfLEDs - 1);
    }

    private void checkIfBreathingHasHitEnd(int amountOfBreathingLEDs, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        final int bounceModeAddition = switch (bounceMode) {
            case Back -> amountOfBreathingLEDs;
            case Center -> amountOfBreathingLEDs / 2;
            default -> 0;
        };

        if (inverted ? (lastBreatheLED < indexOffset + bounceModeAddition) : (lastBreatheLED >= numberOfLEDs + indexOffset + bounceModeAddition))
            lastBreatheLED = inverted ? indexOffset + numberOfLEDs : indexOffset;
    }

    private void setBreathingLEDs(Color color, int breathingLEDs, LarsonAnimation.BounceMode bounceMode) {
        for (int i = 0; i < breathingLEDs; i++) {
            if (lastBreatheLED - i >= indexOffset && lastBreatheLED - i < indexOffset + numberOfLEDs)
                LED_BUFFER.setLED(lastBreatheLED - i, color);

            else if (lastBreatheLED - i < indexOffset + numberOfLEDs) {
                if (bounceMode.equals(LarsonAnimation.BounceMode.Back) || bounceMode.equals(LarsonAnimation.BounceMode.Center) && i > breathingLEDs / 2)
                    return;
                LED_BUFFER.setLED(lastBreatheLED - i + numberOfLEDs, color);
            }
        }
    }

    private void checkIfColorFlowHasHitEnd() {
        if (amountOfColorFlowLEDs >= numberOfLEDs || amountOfColorFlowLEDs < 0) {
            amountOfColorFlowLEDs = amountOfColorFlowLEDs < 0 ? amountOfColorFlowLEDs + 1 : amountOfColorFlowLEDs - 1;
            isLEDAnimationChanged = !isLEDAnimationChanged;
        }
    }

    private void setLEDColors(Color color, int startIndex, int endIndex) {
        for (int i = 0; i <= endIndex - startIndex; i++)
            LED_BUFFER.setLED(startIndex + indexOffset + i, color);
    }
}