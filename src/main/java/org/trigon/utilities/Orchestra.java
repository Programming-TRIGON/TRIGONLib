package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList;

/**
 * Software that plays music through motors.
 */
public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final ArrayList<TalonFX> INSTRUMENTS = new ArrayList<>();

    /**
     * Adds a motor to the array list INSTRUMENTS to then be added to the Orchestra.
     *
     * @param motor the motor to be added to the Orchestra through INSTRUMENTS
     */
    public static void addMotor(TalonFX motor) {
        INSTRUMENTS.add(motor);
    }

    /**
     * Gives a music file to the Orchestra and assigns a track to each motor added to the Orchestra.
     * Each track plays a different part of the music file; bass, harmonys, etc....
     * If the number of motors exceeds the amount of total tracks then the extra motors will be assigned used tracks.
     * The extra motors will receive the track number equivalent to by how much exceeded the total amount of tracks.
     * Only one track can be assigned to each motor.
     *
     * @param musicFile   the music file to be added to the Orchestra
     * @param totalTracks number of totalTracks to be assigned to the motors
     */
    public static void playTracks(String musicFile, int totalTracks) {
        for (int i = 0; i < INSTRUMENTS.size(); i++)
            ORCHESTRA.addInstrument(INSTRUMENTS.get(i), i % totalTracks + 1);
        addAndPlayTracks(musicFile);
    }

    /**
     * Sets a music file to the Orchestra and assigns each track to one or more motors.
     * Each track plays a different part of the music file; bass, harmonys, etc....
     * Works with multiple tracks.
     * Can assign a single track to more than one motor.
     *
     * @param musicFile      the music file to be added to the Orchestra
     * @param motorsPerTrack number of motors that get assigned to a track
     */
    public static void playTracks(String musicFile, int... motorsPerTrack) throws IllegalStateException {
        int totalTracks = 0;
        int motorsAssignedTracks = 0;
        for (int i = 0; i < motorsPerTrack.length; i++) {
            totalTracks += motorsPerTrack[i];
            if (totalTracks > INSTRUMENTS.size())
                throw new IllegalStateException("Not enough motors");
            for (int j = 0; j < motorsPerTrack[i]; i++)
                ORCHESTRA.addInstrument(INSTRUMENTS.get(motorsAssignedTracks++), i + 1);

        }
        addAndPlayTracks(musicFile);
    }

    /**
     * Adds a music file to the Orchestra and then plays it.
     *
     * @param musicFile the music file to be added to the Orchestra
     */
    public static void addAndPlayTracks(String musicFile) {
        addTracks(musicFile);
        playTracks();
    }

    /**
     * Resets the Orchestra.
     */
    public static void stopTracks() {
        ORCHESTRA.stop();
        clearAllMotors();
    }

    /**
     * Removes all the motors from the Orchestra and returns the status code of clearing all the motors.
     */
    public static void clearAllMotors() {
        ORCHESTRA.clearInstruments();
    }

    /**
     * Plays the music file in the Orchestra.
     */
    public static void playTracks() {
        ORCHESTRA.play();
    }

    /**
     * Assigns a music file to the Orchestra.
     *
     * @param musicFile the music file to be added to the Orchestra
     */
    public static void addTracks(String musicFile) {
        ORCHESTRA.loadMusic(musicFile);
    }

    /**
     * Stops the music file in the Orchestra until resumed.
     */
    public static void pauseTracks() {
        ORCHESTRA.pause();
    }

    /**
     * Stops playing the music file and resets the Orchestra.
     */
    public static void closeTracks() {
        ORCHESTRA.close();
    }

    /**
     * returns whether or not the music file is playing.
     *
     * @return if the track is playing
     */
    public static boolean areTracksPlaying() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * Gets the current time location of the music file.
     *
     * @return the current time location of the music file in seconds
     */
    public static double getCurrentMusicFileTimeLocation() {
        return ORCHESTRA.getCurrentTime();
    }
}
