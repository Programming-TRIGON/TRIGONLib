package frc.trigon.robot.constants;

import frc.trigon.robot.Robot;
import frc.trigon.robot.utilities.FilesHandler;

public class RobotConstants {
    private static final boolean
            UNFILTERED_IS_SIMULATION = true,
            UNFILTERED_IS_REPLAY = false;
    public static final boolean
            IS_SIMULATION = UNFILTERED_IS_SIMULATION && !Robot.IS_REAL,
            IS_REPLAY = UNFILTERED_IS_REPLAY && !Robot.IS_REAL;
    public static final double PERIODIC_TIME_SECONDS = 0.02;
    public static final String CANIVORE_NAME = "CANivore";
    public static final String LOGGING_PATH = IS_SIMULATION ? FilesHandler.DEPLOY_PATH + "logs/" : "/media/sda1/akitlogs/";
}
