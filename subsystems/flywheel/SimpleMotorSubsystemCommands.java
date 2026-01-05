package frc.trigon.lib.subsystems.flywheel;

import edu.wpi.first.wpilibj2.command.Command;
import lib.commands.ExecuteEndCommand;
import lib.commands.NetworkTablesCommand;

import java.util.Set;

public class SimpleMotorSubsystemCommands {
    public static Command getDebuggingCommand(SimpleMotorSubsystem subsystem) {
        return new NetworkTablesCommand(
                subsystem::setTargetVelocity,
                false,
                Set.of(subsystem),
                subsystem.getName() + "TargetVelocityRotationsPerSecond"
        );
    }

    public static Command getSetTargetStateCommand(SimpleMotorSubsystem.SimpleMotorState targetState, SimpleMotorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetState(targetState),
                subsystem::stop,
                subsystem
        );
    }

    public static Command getSetTargetStateCommand(double targetVelocityRotationsPerSecond, SimpleMotorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetVelocity(targetVelocityRotationsPerSecond),
                subsystem::stop,
                subsystem
        );
    }

    public static Command getPrepareTargetStateCommand(SimpleMotorSubsystem.SimpleMotorState targetState, SimpleMotorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetState(targetState.getPrepareState()),
                subsystem::stop,
                subsystem
        );
    }
}
