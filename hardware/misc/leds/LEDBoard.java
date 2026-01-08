package frc.trigon.lib.hardware.misc.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.trigon.lib.utilities.RGBArrayUtils;

import java.io.IOException;

public class LEDBoard extends SubsystemBase {
    private final LEDStrip[] ledStrips;
    private String[] currentAnimationFilePaths;
    private Color movingLEDColor;
    private int
            currentAnimationFrame,
            numberOfMovingLEDs,
            currentMovingLEDIndex,
            movingLEDSpacing,
            movingLEDStripSpacing;
    private double
            animationUpdateIntervalSeconds,
            lastAnimationUpdateTimeSeconds,
            movingUpdateIntervalSeconds,
            lastLEDMovementTimeSeconds;
    private boolean shouldMoveInverted;

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
            System.out.println("Couldn't find file at " + filePath);
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

    void breathe(Color color, int numberOfBreathingLEDs, int speedLEDsPerSecond, int ledSpacing, boolean inverted) {
        movingLEDColor = color;
        this.numberOfMovingLEDs = numberOfBreathingLEDs;
        currentMovingLEDIndex = 0;
        movingUpdateIntervalSeconds = (double) 1 / speedLEDsPerSecond;
        movingLEDSpacing = ledSpacing;
        lastLEDMovementTimeSeconds = Timer.getFPGATimestamp();
        shouldMoveInverted = inverted;

        updateBreathingLEDs();
    }

    void bounce(Color color, int numberOfMovingLEDs, int speedLEDsPerSecond, int ledSpacing, int stripSpacing) {
        movingLEDColor = color;
        this.numberOfMovingLEDs = numberOfMovingLEDs;
        currentMovingLEDIndex = 0;
        movingUpdateIntervalSeconds = (double) 1 / speedLEDsPerSecond;
        movingLEDSpacing = ledStrips[0].getNumberOfLEDS() % ledSpacing == 0 ? ledSpacing : 1;
        movingLEDStripSpacing = stripSpacing + 1;
        lastLEDMovementTimeSeconds = Timer.getFPGATimestamp();
        shouldMoveInverted = false;

        updateBouncingLEDs(0, currentMovingLEDIndex, shouldMoveInverted);
    }

    void updateAnimationPeriodically() {
        if (Timer.getFPGATimestamp() - lastAnimationUpdateTimeSeconds >= animationUpdateIntervalSeconds) {
            setImage(currentAnimationFilePaths[currentAnimationFrame++ % currentAnimationFilePaths.length]);
            lastAnimationUpdateTimeSeconds = Timer.getFPGATimestamp();
        }
    }

    void updateBreathingPeriodically() {
        if (Timer.getFPGATimestamp() - lastLEDMovementTimeSeconds >= movingUpdateIntervalSeconds) {
            incrementAndWrapCurrentLEDIndex();
            updateBreathingLEDs();
            lastLEDMovementTimeSeconds = Timer.getFPGATimestamp();
        }
    }

    void updateBouncingPeriodically() {
        if (Timer.getFPGATimestamp() - lastLEDMovementTimeSeconds >= movingUpdateIntervalSeconds) {
            incrementAndBounceCurrentLEDIndex();
            updateBouncingLEDs(0, currentMovingLEDIndex, !shouldMoveInverted);
            lastLEDMovementTimeSeconds = Timer.getFPGATimestamp();
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
            updateBreathingLEDStrip((i * (shouldMoveInverted ? -movingLEDSpacing : movingLEDSpacing)) + currentMovingLEDIndex, ledStrips[i]);
    }

    private void updateBouncingLEDs(int ledStripIndex, int startIndex, boolean movingRight) {
        if (ledStripIndex >= ledStrips.length)
            return;

        ledStrips[ledStripIndex].clearLEDColors();
        for (int i = 0; i < numberOfMovingLEDs; i++)
            ledStrips[ledStripIndex].setSingleLEDColor(startIndex + i, movingLEDColor);

        if (startIndex == 0)
            movingRight = true;
        else if (startIndex + numberOfMovingLEDs >= ledStrips[ledStripIndex].getNumberOfLEDS())
            movingRight = false;

        updateBouncingLEDs(ledStripIndex + movingLEDStripSpacing, startIndex + (movingRight ? movingLEDSpacing : -movingLEDSpacing), movingRight);
    }

    private void updateBreathingLEDStrip(int ledStartIndex, LEDStrip ledStrip) {
        ledStrip.clearLEDColors();
        for (int currentLED = ledStartIndex; currentLED < ledStartIndex + numberOfMovingLEDs; currentLED++)
            ledStrip.setSingleLEDColor(((currentLED % ledStrip.getNumberOfLEDS()) + ledStrip.getNumberOfLEDS()) % ledStrip.getNumberOfLEDS(), movingLEDColor);
    }

    private void incrementAndWrapCurrentLEDIndex() {
        currentMovingLEDIndex++;
        currentMovingLEDIndex %= ledStrips[0].getNumberOfLEDS();
    }

    private void incrementAndBounceCurrentLEDIndex() {
        currentMovingLEDIndex += shouldMoveInverted ? -movingLEDSpacing : movingLEDSpacing;
        if (currentMovingLEDIndex >= ledStrips[0].getNumberOfLEDS() - numberOfMovingLEDs || currentMovingLEDIndex <= 0)
            shouldMoveInverted = !shouldMoveInverted;
    }
}
