package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class that uses the {@link com.ctre.phoenix6.Orchestra} library in Phoenix 6 to play a .chrp file.
 */
public class Orchestra extends SubsystemBase {
    private static final Orchestra INSTANCE = new Orchestra();
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final HashMap<Integer, TalonFX> MOTORS = new HashMap<>();
    private static final AtomicReference<ArrayList<Integer>> SKIPPED_IDS = new AtomicReference<>(new ArrayList<>());
    private static boolean SHOULD_UPDATE_SKIPPED_MOTORS = false;

    /**
     * Adds a motor to the Orchestra.
     * The Orchestra can then play the music from those motors.
     *
     * @param motor the motor to be added to the Orchestra
     */
    public static void addMotor(TalonFX motor, int id) {
        MOTORS.put(id, motor);
    }

    public static Command getPlayFileCommand(String filePath, int totalTracks) {
        return new FunctionalCommand(
                () -> playFile(filePath, totalTracks),
                () -> updateMotors(totalTracks),
                (interrupted) -> stop(),
                () -> false,
                INSTANCE
        ).ignoringDisable(true);
    }

    public static Command getPlayFileCommand(String filePath, int[] motorsPerTrack) {
        return new FunctionalCommand(
                () -> playFile(filePath, motorsPerTrack),
                () -> updateMotors(motorsPerTrack),
                (interrupted) -> stop(),
                () -> false,
                INSTANCE
        ).ignoringDisable(true);
    }

    public static Command getStopCommand() {
        return new InstantCommand(
                Orchestra::stop,
                INSTANCE
        ).ignoringDisable(true);
    }

    public static Command getPauseCommand() {
        return new InstantCommand(
                Orchestra::pause,
                INSTANCE
        ).ignoringDisable(true);
    }

    public static Command getPlayCommand() {
        return new InstantCommand(
                Orchestra::play,
                INSTANCE
        ).ignoringDisable(true);
    }

    /**
     * @return whether orchestra is currently playing a file
     */
    public static boolean isOrchestraCurrentlyPlaying() {
        return ORCHESTRA.isPlaying();
    }

    /**
     * @return the play time of the current file in seconds
     */
    public static double getPlayTimeSeconds() {
        return ORCHESTRA.getCurrentTime();
    }

    /**
     * Adds IDs of motors that should be skipped when playing a file.
     *
     * @param newIDs the IDs of motors that should be skipped
     */
    public static void addSkippedIDs(Integer... newIDs) {
        ArrayList<Integer> currentIDs = SKIPPED_IDS.get();
        if (currentIDs.containsAll(Arrays.asList(newIDs)))
            return;

        currentIDs.addAll(Arrays.asList(newIDs));
        setSkippedIDs(currentIDs);
    }

    /**
     * Removes IDs of motors that should no longer be skipped when playing a file.
     *
     * @param idsToRemove the IDs of motors that should no longer be skipped
     */
    public static void removeSkippedIDs(Integer... idsToRemove) {
        ArrayList<Integer> currentIDs = SKIPPED_IDS.get();
        if (!currentIDs.containsAll(Arrays.asList(idsToRemove)))
            return;

        currentIDs.removeAll(Arrays.asList(idsToRemove));
        setSkippedIDs(currentIDs);
    }

    /**
     * Clears all skipped motor IDs.
     */
    public static void clearSkippedIDs() {
        setSkippedIDs(new ArrayList<>());
    }

    /**
     * Sets the skipped motor IDs to a specific list.
     *
     * @param skippedIDs the list of motor IDs to be skipped
     */
    public static void setSkippedIDs(ArrayList<Integer> skippedIDs) {
        SKIPPED_IDS.set(skippedIDs);
        SHOULD_UPDATE_SKIPPED_MOTORS = true;
    }

    /**
     * Plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned linearly and loop back if there are extra motors.
     *
     * @param filePath    the path of the .chrp file to be played by the Orchestra
     * @param totalTracks the number of tracks in the .chrp file
     */
    private static void playFile(String filePath, int totalTracks) {
        applyTracks(totalTracks);
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
     */
    private static void playFile(String filePath, int[] motorsPerTrack) {
        applyTracks(motorsPerTrack);
        addAndPlayFile(filePath);
    }

    private static void updateMotors(int totalTracks) {
        if (!SHOULD_UPDATE_SKIPPED_MOTORS)
            return;
        ORCHESTRA.clearInstruments();

        applyTracks(totalTracks);
        SHOULD_UPDATE_SKIPPED_MOTORS = false;
    }

    private static void updateMotors(int[] motorsPerTrack) {
        if (!SHOULD_UPDATE_SKIPPED_MOTORS)
            return;
        ORCHESTRA.clearInstruments();

        applyTracks(motorsPerTrack);
        SHOULD_UPDATE_SKIPPED_MOTORS = false;
    }

    private static void applyTracks(int totalTracks) {
        for (int i = 1; i < MOTORS.size() + 1; i++)
            if (shouldUseMotor(i) && MOTORS.containsKey(i))
                ORCHESTRA.addInstrument(MOTORS.get(i), i % totalTracks);
    }

    private static void applyTracks(int[] motorsPerTrack) {
        int motorIndex = 1;

        for (int trackIndex = 0; trackIndex < motorsPerTrack.length; trackIndex++) {
            for (int motorsInCurrentTrack = 0; motorsInCurrentTrack < motorsPerTrack[trackIndex]; motorsInCurrentTrack++) {
                if (motorIndex > MOTORS.size()) {
                    System.out.println("Orchestra: Not enough motors");
                    return;
                }
                if (shouldUseMotor(motorIndex) && MOTORS.containsKey(motorIndex))
                    ORCHESTRA.addInstrument(MOTORS.get(motorIndex), trackIndex);
                motorIndex++;
            }
        }
    }

    /**
     * Stops the music and removes all tracks assigned to motors.
     */
    private static void stop() {
        ORCHESTRA.stop();
        ORCHESTRA.clearInstruments();
    }

    /**
     * Pauses the music.
     */
    private static void pause() {
        ORCHESTRA.pause();
    }

    /**
     * Plays the stored .chrp file.
     */
    private static void play() {
        ORCHESTRA.play();
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

    private static boolean shouldUseMotor(Integer id) {
        return !SKIPPED_IDS.get().contains(id);
    }
}
