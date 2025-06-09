package org.trigon.hardware.misc.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import org.trigon.utilities.RGBArrayUtils;

import java.io.IOException;

public class LEDBoard {
    private final LEDStrip[] ledStrips;
    private String[] currentAnimationFilePaths;
    private int currentAnimationFrame;
    private double animationUpdateIntervalSeconds, lastAnimationUpdateTimeSeconds;

    public LEDBoard(LEDStrip... ledStrips) {
        this.ledStrips = ledStrips;
    }

    public LEDStrip[] getLEDStrips() {
        return ledStrips;
    }

    public void clearBoard() {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.clearLEDColors();
    }

    public void setImage(String filePath) {
        int[][][] rgbArray;
        try {
            rgbArray = RGBArrayUtils.convertPngToRgbArray(filePath, ledStrips[0].getNumberOfLEDS(), ledStrips.length);
        } catch (IOException e) {
            e.printStackTrace();
            rgbArray = new int[0][0][0];
        }

        for (int i = 0; i < rgbArray.length; i++) {
            for (int j = 0; j < rgbArray[0].length; j++) {
                int[] currentRawColor = rgbArray[i][j];
                Color currentColor = new Color(currentRawColor[0], currentRawColor[1], currentRawColor[2]);
                ledStrips[i].setSingleLEDColor(j, currentColor);
            }
        }
    }

    public void setAnimation(String[] filePaths, double framesPerSecond) {
        currentAnimationFilePaths = filePaths;
        animationUpdateIntervalSeconds = 1 / framesPerSecond;
        lastAnimationUpdateTimeSeconds = Timer.getFPGATimestamp();
        currentAnimationFrame = 0;
        setImage(filePaths[0]);
    }

    public void updateAnimationPeriodically() {
        if (Timer.getFPGATimestamp() - lastAnimationUpdateTimeSeconds >= animationUpdateIntervalSeconds) {
            setImage(currentAnimationFilePaths[currentAnimationFrame++ % currentAnimationFilePaths.length]);
            lastAnimationUpdateTimeSeconds = Timer.getFPGATimestamp();
        }
    }

    boolean hasAnimationEnded() {
        return currentAnimationFrame > currentAnimationFilePaths.length;
    }

    void resetAnimation() {
        currentAnimationFilePaths = new String[0];
        currentAnimationFrame = 0;
        animationUpdateIntervalSeconds = 0;
        lastAnimationUpdateTimeSeconds = 0;
    }
}
