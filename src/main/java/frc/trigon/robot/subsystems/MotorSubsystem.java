package frc.trigon.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A class that represents a subsystem that has motors (rather than something like LEDs).
 * This class will automatically stop all the motors when the robot is disabled, and set the motors to brake when the robot is enabled.
 * If a subsystem doesn't need to ever brake (i.e. shooter, flywheel, etc.), then it should override the {@link #setBrake(boolean)} method and do nothing.
 */
public abstract class MotorSubsystem extends edu.wpi.first.wpilibj2.command.SubsystemBase {
    private static final List<MotorSubsystem> REGISTERED_SUBSYSTEMS = new ArrayList<>();
    private static final Trigger DISABLED_TRIGGER = new Trigger(DriverStation::isDisabled);

    static {
        DISABLED_TRIGGER.onTrue(new InstantCommand(() -> forEach(MotorSubsystem::stop)).ignoringDisable(true));
        DISABLED_TRIGGER.onFalse(new InstantCommand(() -> setAllSubsystemsBrakeAsync(true)).ignoringDisable(true));
    }

    public MotorSubsystem() {
        REGISTERED_SUBSYSTEMS.add(this);
    }

    /**
     * Runs the given consumer on all the subsystem instances.
     *
     * @param toRun the consumer to run on each registered subsystem
     */
    public static void forEach(Consumer<MotorSubsystem> toRun) {
        REGISTERED_SUBSYSTEMS.forEach(toRun);
    }

    /**
     * Sets whether the all the subsystems should brake or coast their motors.
     * This command will run asynchronously, since the TalonFX's setting of brake/coast is blocking.
     *
     * @param brake whether the motors should brake or coast
     */
    public static void setAllSubsystemsBrakeAsync(boolean brake) {
        CompletableFuture.runAsync(() -> forEach((subsystem) -> subsystem.setBrake(brake)));
    }

    /**
     * Sets whether the subsystem's motors should brake or coast.
     * If a subsystem doesn't need to ever brake (i.e. shooter, flywheel, etc.), then it should override this method and do nothing.
     *
     * @param brake whether the motors should brake or coast
     */
    public abstract void setBrake(boolean brake);

    public abstract void stop();
}
