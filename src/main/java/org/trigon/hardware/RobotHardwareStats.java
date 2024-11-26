package org.trigon.hardware;

/**
 * A class that contains stats about the robot's hardware.
 */
public class RobotHardwareStats {
    private static boolean IS_SIMULATION = false;
    private static boolean IS_REPLAY = false;
    private static double PERIODIC_TIME_SECONDS = 0.02;

    /**
     * Sets the current robot stats. This should be called in the robot's init method.
     * We use this structure to avoid using static variables in the Robot class.
     *
     * @param isReal     whether the robot is real or a simulation. This should be taken from the Robot class.
     * @param replayType the type of replay
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
     * Sets how frequently the periodic method is called in seconds.
     *
     * @param periodicTimeSeconds the periodic time in seconds
     */
    public static void setPeriodicTimeSeconds(double periodicTimeSeconds) {
        PERIODIC_TIME_SECONDS = periodicTimeSeconds;
    }

    /**
     * @return the periodic time in seconds
     */
    public static double getPeriodicTimeSeconds() {
        return PERIODIC_TIME_SECONDS;
    }

    /**
     * @return whether the robot is in replay mode or not
     */
    public static boolean isReplay() {
        return IS_REPLAY;
    }

    /**
     * @return whether the robot is running in simulation or not
     */
    public static boolean isSimulation() {
        return IS_SIMULATION;
    }

    /**
     * An enum that represents the type of replay.
     * NONE - the robot is not in replay mode
     * SIMULATION_REPLAY - the robot is in simulation replay mode
     * REAL_REPLAY - the robot is in real replay mode
     */
    public enum ReplayType {
        NONE,
        SIMULATION_REPLAY,
        REAL_REPLAY
    }
}