package frc.trigon.lib.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.trigon.lib.commands.ExecuteEndCommand;
import frc.trigon.lib.commands.NetworkTablesCommand;

import java.util.Set;

public class ElevatorSubsystemCommands {
    public static Command getDebuggingCommand(ElevatorSubsystem subsystem) {
        return new NetworkTablesCommand(
                subsystem::setTargetState,
                false,
                Set.of(subsystem),
                subsystem.getName() + "TargetPositionMeters",
                subsystem.getName() + "SpeedScalar"
        );
    }

    public static Command getSetTargetStateCommand(ElevatorSubsystem.ElevatorState targetState, ElevatorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetState(targetState),
                subsystem::stop,
                subsystem
        );
    }

    public static Command getSetTargetStateCommand(double targetPositionMeters, double speedScalar, ElevatorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetState(targetPositionMeters, speedScalar),
                subsystem::stop,
                subsystem
        );
    }

    public static Command getPrepareTargetStateCommand(ElevatorSubsystem.ElevatorState targetState, ElevatorSubsystem subsystem) {
        return new ExecuteEndCommand(
                () -> subsystem.setTargetState(targetState.getPrepareState()),
                subsystem::stop,
                subsystem
        );
    }
}