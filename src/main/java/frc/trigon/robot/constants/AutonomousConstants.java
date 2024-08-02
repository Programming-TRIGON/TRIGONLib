package frc.trigon.robot.constants;

import com.pathplanner.lib.path.PathConstraints;

public class AutonomousConstants {
    public static final PathConstraints REAL_TIME_CONSTRAINTS = new PathConstraints(2.5, 2.5, 4, 4);

    static {
        registerCommands();
    }

    private static void registerCommands() {
//        NamedCommands.registerCommand(name, command);
    }
}
