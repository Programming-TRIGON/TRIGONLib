package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;

import java.util.ArrayList;

public class Orchestra {
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final ArrayList<TalonFX> MOTORS = new ArrayList<>();

    /**
     * adds a motor to the array list MOTORS.
     *
     * @param motor the motor to be added to MOTORS
     */
    public static void addMotor(TalonFX motor) {
        MOTORS.add(motor);
    }

    /**
     * sets a track file to the Orchestra and assigns a track to each motor added to the Orchestra.
     * if the number of motors exceeds the amount of tracks then the extra motors will be assigned used tracks.
     *
     * @param trackFile the track file to be added to the Orchestra
     * @param tracks    how many tracks should be assigned to the motors
     */
    public static void playTrack(String trackFile, int tracks) {
        for (int i = 0; i < MOTORS.size(); i++)
            ORCHESTRA.addInstrument(MOTORS.get(i), i % tracks + 1);
        addTrack(trackFile);
        playTrack();
    }

    /**
     * sets a track file to the Orchestra and assigns one or multiple motors in the Orchestra a single track.
     * works with multiple tracks
     *
     * @param trackFile the track file to be added to the Orchestra
     * @param perTrack  how many motors should get assigned to a track
     * @throws IllegalStateException if the amount of motors is below the amount of tracks to be assigned
     */
    public static void playTrack(String trackFile, int... perTrack) throws IllegalStateException {
        int total = 0,
                index = 0;
        for (int i = 0; i < perTrack.length; i++) {
            total += perTrack[i];
            if (total > MOTORS.size())
                throw new IllegalStateException("Not enough motors");
            for (int j = 0; j < perTrack[i]; i++)
                ORCHESTRA.addInstrument(MOTORS.get(index++), i + 1);

        }

        addTrack(trackFile);
        playTrack();
    }

    /**
     * stops the Orchestra from playing and clears all the motors from it.
     */
    public static void stopTrack() {
        ORCHESTRA.stop();
        clearAllMotors();
    }

    /**
     * adds a track file to the Orchestra.
     *
     * @param trackFile the file to be added to the Orchestra
     */
    public static void addTrack(String trackFile) {
        ORCHESTRA.loadMusic(trackFile);
    }

    /**
     * clears all the motors from the Orchestra
     */
    public static void clearAllMotors() {
        ORCHESTRA.clearInstruments();
    }

    /**
     * plays the track in the Orchestra.
     */
    public static void playTrack() {
        ORCHESTRA.play();
    }

    /**
     * pauses the track in the Orchestra.
     */
    public static void pauseTrack() {
        ORCHESTRA.pause();
    }

    /**
     * closes the Orchestra.
     */
    public static void closeTrack() {
        ORCHESTRA.close();
    }

    /**
     * a function to determine whether the track is playing.
     *
     * @return if the track is playing
     */
    public static boolean isTrackPlaying() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * a function to get how much time the Orchestra has been playing.
     *
     * @return how long the Orchestra has been playing
     */
    public static double getCurrentTrackTime() {
        return ORCHESTRA.getCurrentTime();
    }
}
