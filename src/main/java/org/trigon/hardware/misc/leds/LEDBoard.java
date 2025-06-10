package org.trigon.hardware.misc.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.trigon.utilities.RGBArrayUtils;

import java.io.IOException;

public class LEDBoard extends SubsystemBase {
    private final LEDStrip[] ledStrips;
    private String[] currentAnimationFilePaths;
    private Color breatheColor;
    private int currentAnimationFrame, numberOfBreathingLEDs, currentBreatheLEDIndex;
    private double animationUpdateIntervalSeconds,
            lastAnimationUpdateTimeSeconds,
            breathingUpdateIntervalSeconds,
            lastBreatheMovementTimeSeconds;
    private boolean shouldBreatheInverted;

    public LEDBoard(LEDStrip... ledStrips) {
        this.ledStrips = ledStrips;
    }

    public LEDStrip[] getLEDStrips() {
        return ledStrips;
    }

    boolean hasAnimationEnded() {
        return currentAnimationFrame > currentAnimationFilePaths.length;
    }

    void clearBoard() {
        for (LEDStrip ledStrip : ledStrips)
            ledStrip.clearLEDColors();
    }

    void setImage(String filePath) {
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

    void setAnimation(String[] filePaths, int framesPerSecond) {
        currentAnimationFilePaths = filePaths;
        animationUpdateIntervalSeconds = (double) 1 / framesPerSecond;
        lastAnimationUpdateTimeSeconds = Timer.getFPGATimestamp();
        currentAnimationFrame = 0;
        setImage(filePaths[0]);
    }

    void breathe(Color color, int numberOfBreathingLEDs, int speedLEDsPerSecond, boolean inverted) {
        breatheColor = color;
        this.numberOfBreathingLEDs = numberOfBreathingLEDs;
        currentBreatheLEDIndex = 0;
        breathingUpdateIntervalSeconds = (double) 1 / speedLEDsPerSecond;
        lastBreatheMovementTimeSeconds = Timer.getFPGATimestamp();
        shouldBreatheInverted = inverted;

        updateBreathingLEDs();
    }

    void updateAnimationPeriodically() {
        if (Timer.getFPGATimestamp() - lastAnimationUpdateTimeSeconds >= animationUpdateIntervalSeconds) {
            setImage(currentAnimationFilePaths[currentAnimationFrame++ % currentAnimationFilePaths.length]);
            lastAnimationUpdateTimeSeconds = Timer.getFPGATimestamp();
        }
    }

    void updateBreathingPeriodically() {
        if (Timer.getFPGATimestamp() - lastBreatheMovementTimeSeconds >= breathingUpdateIntervalSeconds) {
            currentBreatheLEDIndex += (shouldBreatheInverted ? -1 : 1);
            updateBreathingLEDs();
            lastBreatheMovementTimeSeconds = Timer.getFPGATimestamp();
        }
    }

    void resetAnimation() {
        currentAnimationFilePaths = new String[0];
        currentAnimationFrame = 0;
        animationUpdateIntervalSeconds = 0;
        lastAnimationUpdateTimeSeconds = 0;
    }

    private void updateBreathingLEDs() {
        for (int i = 0; i < ledStrips.length; i++)
            updateLEDStrip(i + currentBreatheLEDIndex, ledStrips[i]);
    }

    private void updateLEDStrip(int ledStartIndex, LEDStrip ledStrip) {
        for (int currentLED = ledStartIndex; currentLED < numberOfBreathingLEDs + ledStartIndex; currentLED += (shouldBreatheInverted ? -1 : 1))
            ledStrip.setSingleLEDColor(currentLED % ledStrip.getNumberOfLEDS(), breatheColor);
    }
}
