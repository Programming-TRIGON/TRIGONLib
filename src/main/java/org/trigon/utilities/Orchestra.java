package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList;

/**
 * A class that uses the {@link com.ctre.phoenix6.Orchestra} library in Phoenix 6 to play a .chrp file.
 */
public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final ArrayList<TalonFX> MOTORS = new ArrayList<>();

    /**
     * Adds a motor to the Orchestra.
     * The Orchestra can then play the music from those motors.
     *
     * @param motor the motor to be added to the Orchestra
     */
    public static void addMotor(TalonFX motor) {
        MOTORS.add(motor);
    }

    /**
     * Plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned linearly and loop back if there are extra motors.
     *
     * @param fileName    the .chrp file to be played by the Orchestra
     * @param totalTracks the number of tracks in the .chrp file.
     */
    public static void playFile(String fileName, int totalTracks) {
        for (int i = 0; i < MOTORS.size(); i++)
            ORCHESTRA.addInstrument(MOTORS.get(i), i % totalTracks + 1);

        addAndPlayFile(fileName);
    }

    /**
     * plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned by the {@code motorsPerTrack}.
     * Each slot represents a track.
     * Each value in the slot represents the number of motors that will be assigned that track.
     *
     * @param fileName       the .chrp file to be added to the Orchestra
     * @param motorsPerTrack number of motors that should be assigned to each track
     */
    public static void playFile(String fileName, int... motorsPerTrack) throws IllegalStateException {
        int totalUsedMotors = 0;
        int motorsAssignedTracks = 0;
        for (int i = 0; i < motorsPerTrack.length; i++) {
            totalUsedMotors += motorsPerTrack[i];
            if (totalUsedMotors > MOTORS.size())
                throw new IllegalStateException("Not enough motors added to the Orchestra.");
            for (int j = 0; j < motorsPerTrack[i]; i++)
                ORCHESTRA.addInstrument(MOTORS.get(motorsAssignedTracks++), i + 1);

        }
        addAndPlayFile(fileName);
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
     * Returns whether the Orchestra is playing music.
     *
     * @return if music is playing
     */
    public static boolean isOrchestraCurrentlyPlayingMusic() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * Gets the play time of the .chrp file being played in seconds.
     *
     * @return the play time
     */
    public static double getPlayTimeSeconds() {
        return ORCHESTRA.getCurrentTime();
    }

    /**
     * Adds a .chrp file to the Orchestra and plays it.
     *
     * @param fileName the .chrp file to be added to the Orchestra
     */
    private static void addAndPlayFile(String fileName) {
        ORCHESTRA.loadMusic(fileName);
        play();
    }
}
