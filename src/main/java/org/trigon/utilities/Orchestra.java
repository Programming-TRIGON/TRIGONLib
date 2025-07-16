package org.trigon.utilities;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A class that uses the {@link com.ctre.phoenix6.Orchestra} library in Phoenix 6 to play a .chrp file.
 */
public class Orchestra extends SubsystemBase {
    private static final Orchestra INSTANCE = new Orchestra();
    private static final com.ctre.phoenix6.Orchestra ORCHESTRA = new com.ctre.phoenix6.Orchestra();
    private static final HashMap<Integer, TalonFX> MOTORS = new HashMap<>();
    private static int[] SKIPPED_IDS = new int[0];

    /**
     * Adds a motor to the Orchestra.
     * The Orchestra can then play the music from those motors.
     *
     * @param motor the motor to be added to the Orchestra
     */
    public static void addMotor(TalonFX motor, int id) {
        MOTORS.put(id, motor);
    }

    public static Command getPlayFileCommand(String filePath, int totalTracks, Supplier<int[]> skippedIDs) {
        return new FunctionalCommand(
                () -> playFile(filePath, totalTracks, skippedIDs.get()),
                () -> {
                    updateMotors(totalTracks, skippedIDs);
                    System.out.println(Arrays.toString(skippedIDs.get()));
                },
                (interrupted) -> stop(),
                () -> false,
                INSTANCE
        ).ignoringDisable(true);
    }

    public static Command getPlayFileCommand(String filePath, int[] motorsPerTrack, Supplier<int[]> skippedIDs) {
        return new FunctionalCommand(
                () -> playFile(filePath, motorsPerTrack, skippedIDs.get()),
                () -> updateMotors(motorsPerTrack, skippedIDs),
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
     * Plays a .chrp file and assigns a track to each motor.
     * A .chrp file stores music as tracks that can be played separately.
     * The tracks are assigned linearly and loop back if there are extra motors.
     *
     * @param filePath    the path of the .chrp file to be played by the Orchestra
     * @param totalTracks the number of tracks in the .chrp file
     * @param skippedIDs  the IDs of motors that should not be assigned a track
     */
    private static void playFile(String filePath, int totalTracks, int[] skippedIDs) {
        for (int i = 1; i < MOTORS.size() + 1; i++) {
            if (shouldUseMotor(i, skippedIDs) && MOTORS.containsKey(i))
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
    private static void playFile(String filePath, int[] motorsPerTrack, int... skippedIDs) {
        int motorIndex = 1;

        for (int trackIndex = 0; trackIndex < motorsPerTrack.length; trackIndex++) {
            for (int motorsInCurrentTrack = 0; motorsInCurrentTrack < motorsPerTrack[trackIndex]; motorsInCurrentTrack++) {
                if (motorIndex > MOTORS.size()) {
                    System.out.println("Orchestra: Not enough motors");
                    return;
                }
                if (shouldUseMotor(motorIndex, skippedIDs) && MOTORS.containsKey(motorIndex))
                    ORCHESTRA.addInstrument(MOTORS.get(motorIndex), trackIndex);
                motorIndex++;
            }
        }

        addAndPlayFile(filePath);
    }

    private static void updateMotors(int totalTracks, Supplier<int[]> skippedIDs) {
        int[] currentSkippedIDs = skippedIDs.get();
        if (Arrays.equals(currentSkippedIDs, SKIPPED_IDS))
            return;

        SKIPPED_IDS = currentSkippedIDs;
        ORCHESTRA.clearInstruments();

        for (int i = 1; i < MOTORS.size() + 1; i++)
            if (shouldUseMotor(i, SKIPPED_IDS) && MOTORS.containsKey(i))
                ORCHESTRA.addInstrument(MOTORS.get(i), i % totalTracks);
    }

    private static void updateMotors(int[] motorsPerTrack, Supplier<int[]> skippedIDs) {
        int[] currentSkippedIDs = skippedIDs.get();
        if (Arrays.equals(currentSkippedIDs, SKIPPED_IDS))
            return;

        SKIPPED_IDS = currentSkippedIDs;
        ORCHESTRA.clearInstruments();

        int motorIndex = 1;
        for (int trackIndex = 0; trackIndex < motorsPerTrack.length; trackIndex++) {
            for (int motorsInCurrentTrack = 0; motorsInCurrentTrack < motorsPerTrack[trackIndex]; motorsInCurrentTrack++) {
                if (motorIndex > MOTORS.size()) {
                    System.out.println("Orchestra: Not enough motors");
                    return;
                }
                if (shouldUseMotor(motorIndex, SKIPPED_IDS) && MOTORS.containsKey(motorIndex))
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

    private static boolean shouldUseMotor(int id, int[] skippedIDs) {
        for (int skippedID : skippedIDs) {
            if (id == skippedID) {
                return false;
            }
        }
        return true;
    }
}
