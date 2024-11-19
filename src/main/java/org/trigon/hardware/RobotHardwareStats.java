package org.trigon.hardware;

/**
 * A class that contains stats about the robot hardware.
 */
public class RobotHardwareStats {
    private static boolean IS_SIMULATION = false;
    private static boolean IS_REPLAY = false;
    private static double PERIODIC_TIME_SECONDS = 0.02;

    /**
     * Sets the current robot stats.
     *
     * @param isReal     whether the robot is real
     * @param replayType the replay type
     */
    public static void setCurrentRobotStats(boolean isReal, ReplayType replayType) {
        if (isReal || replayType.equals(ReplayType.NONE)) {
            IS_SIMULATION = !isReal;
            IS_REPLAY = false;
            return;
        }

        IS_SIMULATION = replayType.equals(ReplayType.SIMULATION_REPLAY);
        IS_REPLAY = true;
    }

    /**
     * Sets the periodic time in seconds.
     *
     * @param periodicTimeSeconds the periodic time in seconds
     */
    public static void setPeriodicTimeSeconds(double periodicTimeSeconds) {
        PERIODIC_TIME_SECONDS = periodicTimeSeconds;
    }

    /**
     * Gets the periodic time in seconds.
     *
     * @return the periodic time in seconds
     */
    public static double getPeriodicTimeSeconds() {
        return PERIODIC_TIME_SECONDS;
    }

    /**
     * Gets whether the robot is in replay mode.
     *
     * @return whether the robot is in replay mode
     */
    public static boolean isReplay() {
        return IS_REPLAY;
    }

    /**
     * Gets whether the robot is in simulation mode.
     *
     * @return whether the robot is in simulation mode
     */
    public static boolean isSimulation() {
        return IS_SIMULATION;
    }

    /**
     * An enum that represents the replay type.
     */
    public enum ReplayType {
        NONE,
        SIMULATION_REPLAY,
        REAL_REPLAY
    }
}