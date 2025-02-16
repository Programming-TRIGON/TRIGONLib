package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList;

public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final ArrayList<TalonFX> MOTORS = new ArrayList<>();

    public static void addMotor(TalonFX motor) {
        MOTORS.add(motor);
    }

    public static void playTrack(String trackFile, int tracks) {
        for (int i = 0; i < MOTORS.size(); i++) {
            ORCHESTRA.addInstrument(MOTORS.get(i), i % tracks + 1);
        }
        addTrack(trackFile);
        playTrack();
    }

    public static void playTrack(String trackFile, int... perTrack) throws IllegalStateException {
        int total = 0,
                index = 0;
        for (int i = 0; i < perTrack.length; i++) {
            total += perTrack[i];
            if (total > MOTORS.size())
                throw new IllegalStateException("Not enough motors");
            for (int j = 0; j < perTrack[i]; i++) {
                ORCHESTRA.addInstrument(MOTORS.get(index++), i + 1);
            }
        }

        addTrack(trackFile);
        playTrack();
    }

    public static void stopTrack() {
        ORCHESTRA.stop();
        clearAllMotors();
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
