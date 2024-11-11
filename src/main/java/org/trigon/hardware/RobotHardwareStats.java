package org.trigon.hardware;

public class RobotHardwareStats {
    private static boolean IS_SIMULATION = false;
    private static boolean IS_REPLAY = false;
    private static double PERIODIC_TIME_SECONDS = 0.02;

    public static void setCurrentRobotStats(boolean isReal, ReplayType replayType) {
        if (isReal && replayType.equals(ReplayType.NONE)) {
            IS_SIMULATION = !isReal;
            IS_REPLAY = false;
            return;
        }
        IS_SIMULATION = replayType.equals(ReplayType.SIMULATION_REPLAY);
        IS_REPLAY = true;
    }

    public static void setPeriodicTimeSeconds(double periodicTimeSeconds) {
        PERIODIC_TIME_SECONDS = periodicTimeSeconds;
    }

    public static double getPeriodicTimeSeconds() {
        return PERIODIC_TIME_SECONDS;
    }

    public static boolean isReplay() {
        return IS_REPLAY;
    }

    public static boolean isSimulation() {
        return IS_SIMULATION;
    }

    public enum ReplayType {
        NONE,
        SIMULATION_REPLAY,
        REAL_REPLAY
    }
}