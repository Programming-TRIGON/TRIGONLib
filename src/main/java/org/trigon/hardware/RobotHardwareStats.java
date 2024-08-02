package org.trigon.hardware;

public class RobotHardwareStats {
    private static boolean IS_SIMULATION = false;
    private static boolean IS_REPLAY = false;
    private static double PERIODIC_TIME_SECONDS = 0.02;

    public static void setCurrentRobotStats(boolean isReal, boolean isSimulation, boolean isReplay) {
        IS_SIMULATION = isSimulation && !isReal;
        IS_REPLAY = isReplay && !isReal;
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
}
