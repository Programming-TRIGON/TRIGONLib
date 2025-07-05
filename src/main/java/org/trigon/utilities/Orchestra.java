package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.HashMap;

/**
 * A class that uses the {@link com.ctre.phoenix6.Orchestra} library in Phoenix 6 to play a .chrp file.
 */
public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final HashMap<Integer, TalonFX> MOTORS = new HashMap<>();

    /**
     * Adds a motor to the Orchestra.
     * The Orchestra can then play the music from those motors.
     *
     * @param motor the motor to be added to the Orchestra
     */
    public static void addMotor(TalonFX motor, int id) {
        MOTORS.put(id, motor);
    }

    /**
     * Plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned linearly and loop back if there are extra motors.
     *
     * @param filePath    the path of the .chrp file to be played by the Orchestra
     * @param totalTracks the number of tracks in the .chrp file
     * @param skippedIDs  the IDs of motors that should not be assigned a track
     */
    public static void playFile(String filePath, int totalTracks, int... skippedIDs) {
        for (int i = 0; i < MOTORS.size(); i++) {
            if (!shouldSkipMotor(i, skippedIDs))
                ORCHESTRA.addInstrument(MOTORS.get(i), i % totalTracks);
        }

        addAndPlayFile(filePath);
    }

    /**
     * plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned by the {@code motorsPerTrack}.
     * Each slot represents a track.
     * Each value in the slot represents the number of motors that will be assigned that track.
     *
     * @param filePath       the path of the .chrp file to be added to the Orchestra
     * @param motorsPerTrack number of motors that should be assigned to each track
     * @param skippedIDs     the IDs of motors that should not be assigned a track
     */
    public static void playFile(String filePath, int[] motorsPerTrack, int... skippedIDs) {
        int motorIndex = 0;

        for (int trackIndex = 0; trackIndex < motorsPerTrack.length; trackIndex++) {
            for (int motorsInCurrentTrack = 0; motorsInCurrentTrack < motorsPerTrack[trackIndex]; motorsInCurrentTrack++) {
                if (motorIndex >= MOTORS.size()) {
                    System.out.println("Orchestra: Not enough motors");
                    return;
                }
                if (!shouldSkipMotor(motorIndex, skippedIDs))
                    ORCHESTRA.addInstrument(MOTORS.get(motorIndex), trackIndex);
                motorIndex++;
            }
        }

        addAndPlayFile(filePath);
    }

    /**
     * Stops the music and removes all tracks assigned to motors.
     */
    public static void stop() {
        ORCHESTRA.stop();
        ORCHESTRA.clearInstruments();
    }

    /**
     * Plays the stored .chrp file.
     */
    public static void play() {
        ORCHESTRA.play();
    }

    /**
     * Pauses the music.
     */
    public static void pause() {
        ORCHESTRA.pause();
    }

    /**
     * @return whether music is playing or not
     */
    public static boolean isOrchestraCurrentlyPlayingMusic() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * @return the play time of the .chrp file in seconds
     */
    public static double getPlayTimeSeconds() {
        return ORCHESTRA.getCurrentTime();
    }

    /**
     * Adds a .chrp file to the Orchestra and plays it.
     *
     * @param filePath the path of the .chrp file to be added to the Orchestra
     */
    private static void addAndPlayFile(String filePath) {
        ORCHESTRA.loadMusic(filePath);
        play();
    }

    private static boolean shouldSkipMotor(int id, int[] skippedIDs) {
        for (int skippedID : skippedIDs) {
            if (id == skippedID) {
                return true;
            }
        }
        return false;
    }
}
