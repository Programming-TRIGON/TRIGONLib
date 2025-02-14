package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();

    public static void addMotor(TalonFX motor, int trackNumber) {
        ORCHESTRA.addInstrument(motor, trackNumber);
    }

    public static void addTrack(String trackFile) {
        ORCHESTRA.loadMusic(trackFile);
    }

    public static void clearAllMotors() {
        ORCHESTRA.clearInstruments();
    }

    public static void playTrack() {
        ORCHESTRA.play();
    }

    public static void pauseTrack() {
        ORCHESTRA.pause();
    }

    public static void stopTrack() {
        ORCHESTRA.stop();
    }

    public static void closeTrack() {
        ORCHESTRA.close();
    }

    public static boolean isTrackPlaying() {
        return ORCHESTRA.isPlaying();
    }

    public static double getCurrentTrackTime() {
        return ORCHESTRA.getCurrentTime();
    }
}
