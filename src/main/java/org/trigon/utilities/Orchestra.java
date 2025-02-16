package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList;

public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final ArrayList<TalonFX> TALON_FXES = new ArrayList<>();

    /**
     * Adds a motor to the array list TALON_FXES to then be added to the Orchestra.
     *
     * @param motor the motor to be added to MOTORS
     */
    public static void addMotor(TalonFX motor) {
        TALON_FXES.add(motor);
    }

    /**
     * sets a music file to the Orchestra and assigns a track to each motor added to the Orchestra.
     * if the number of motors exceeds the amount of tracks then the extra motors will be assigned used tracks.
     * can only assign one track to each motor
     *
     * @param musicFile the music file to be added to the Orchestra
     * @param tracks    number of tracks to be assigned to the motors
     */
    public static void playTrack(String musicFile, int tracks) {
        for (int i = 0; i < TALON_FXES.size(); i++)
            ORCHESTRA.addInstrument(TALON_FXES.get(i), i % tracks + 1);
        addAndPlayTrack(musicFile);
    }

    /**
     * Sets a music file to the Orchestra and assigns one or multiple motors in the Orchestra a single track.
     * Works with multiple tracks.
     * can assign a single track to more than one motor.
     *
     * @param musicFile the music file to be added to the Orchestra
     * @param perTrack  how many motors should get assigned to a track
     */
    public static void playTrack(String musicFile, int... perTrack) throws IllegalStateException {
        int totalTracks = 0;
        int motorsAssignedTracks = 0;
        for (int i = 0; i < perTrack.length; i++) {
            totalTracks += perTrack[i];
            if (totalTracks > TALON_FXES.size())
                throw new IllegalStateException("Not enough motors");
            for (int j = 0; j < perTrack[i]; i++)
                ORCHESTRA.addInstrument(TALON_FXES.get(motorsAssignedTracks++), i + 1);

        }
        addAndPlayTrack(musicFile);
    }

    /**
     * Adds a music file to the Orchestra and then plays it.
     *
     * @param musicFile the music file to be added to the Orchestra
     */
    public static void addAndPlayTrack(String musicFile) {
        addTrack(musicFile);
        playTrack();
    }

    /**
     * Stops the Orchestra from playing and resets the music file to the start
     * removes all the motors from the Orchestra.
     */
    public static void stopTrack() {
        ORCHESTRA.stop();
        clearAllMotors();
    }

    /**
     * Removes all the motors from the Orchestra meaning there is no hardware to play the music.
     */
    public static void clearAllMotors() {
        ORCHESTRA.clearInstruments();
    }

    /**
     * Plays the music file in the Orchestra.
     */
    public static void playTrack() {
        ORCHESTRA.play();
    }

    /**
     * add a music file to the Orchestra.
     *
     * @param musicFile the music file to be added to the Orchestra
     */
    public static void addTrack(String musicFile) {
        ORCHESTRA.loadMusic(musicFile);
    }

    /**
     * Pauses the music file in the Orchestra.
     */
    public static void pauseTrack() {
        ORCHESTRA.pause();
    }

    /**
     * Closes the current Orchestra instance.
     */
    public static void closeTrack() {
        ORCHESTRA.close();
    }

    /**
     * Determines whether or not the music file is playing.
     *
     * @return if the track is playing
     */
    public static boolean isTrackPlaying() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * Gets the current time stamp of the music file.
     *
     * @return the current time stamp of the music file in seconds
     */
    public static double getCurrentTrackTime() {
        return ORCHESTRA.getCurrentTime();
    }
}
