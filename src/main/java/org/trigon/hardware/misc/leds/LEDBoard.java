package org.trigon.hardware.misc.leds;

import com.ctre.phoenix.led.LarsonAnimation;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.trigon.utilities.RGBArrayUtils;

import java.io.IOException;
import java.util.function.Supplier;

public class LEDBoard extends SubsystemBase {
    private final LEDStrip[] ledStrips;

    public LEDBoard(LEDStrip... ledStrips) {
        this.ledStrips = ledStrips;
    }

    public void clearBoard() {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.clearLEDColors();
    }

    public void setImage(String filePath) throws IOException {
        final int[][][] rgbArray = RGBArrayUtils.convertPngToRgbArray(filePath, ledStrips[0].getNumberOfLEDS(), ledStrips.length);
        for (int i = 0; i < rgbArray.length; i++) {
            for (int j = 0; j < rgbArray[0].length; j++) {
                int[] currentRawColor = rgbArray[i][j];
                Color currentColor = new Color(currentRawColor[0], currentRawColor[1], currentRawColor[2]);
                System.out.println(currentColor);
                ledStrips[i].setSingleLEDColor(j, currentColor);
            }
        }
    }

    public void staticColor(Color color) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.staticColor(color);
    }

    public void blink(Color color, double speed) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.blink(color, speed);
    }

    public void breathe(Color color, double speed, boolean inverted, LarsonAnimation.BounceMode bounceMode) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.breathe(color, ledStrip.numberOfLEDs, speed, inverted, bounceMode);
    }

    public void colorFlow(Color color, double speed, boolean inverted) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.colorFlow(color, speed, inverted);
    }

    public void alternateColor(Color firstColor, Color secondColor, boolean checkerBoard) {
        for (int i = 0; i < ledStrips.length; i++) {
            if (checkerBoard && i % 2 == 1)
                ledStrips[i].alternateColor(secondColor, firstColor);
            else
                ledStrips[i].alternateColor(firstColor, secondColor);
        }
    }

    public void sectionColor(Supplier<Color>[] colors) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.sectionColor(colors);
    }

    public void rainbow(double brightness, double speed, boolean inverted) {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.rainbow(brightness, speed, inverted);
    }
}
