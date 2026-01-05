package frc.trigon.lib.hardware;

/**
 * A class that contains stats about the robot's hardware.
 */
public class RobotHardwareStats {
    public static final double SUPPLY_VOLTAGE = 12;
    private static boolean IS_SIMULATION = false;
    private static boolean IS_REPLAY = false;
    private static double PERIODIC_TIME_SECONDS = 0.02;

    /**
     * Sets the current robot stats. This should be called in the robot's init method.
     * If isReal is true both simulation and replay will be false, otherwise they will be set according to the replay type.
     * We use this structure to avoid using static variables in the Robot class.
     *
     * @param isReal     whether the robot is real or a simulation. This should be taken from the Robot class
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
     * Sets how frequently the simulation is updated.
     *
     * @param periodicTimeSeconds the periodic time in seconds
     */
    public static void setPeriodicTimeSeconds(double periodicTimeSeconds) {
        PERIODIC_TIME_SECONDS = periodicTimeSeconds;
    }

    /**
     * @return the periodic time in seconds set in {@link #setPeriodicTimeSeconds(double)}
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
     */
    public enum ReplayType {
        /**
         * The robot is not in replay mode
         */
        NONE,
        /**
         * The robot is in simulation replay mode
         */
        SIMULATION_REPLAY,
        /**
         * The robot is in real replay mode
         */
        REAL_REPLAY
    }
}