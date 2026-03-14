package frc.trigon.lib.subsystems;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;
import frc.trigon.lib.hardware.RobotHardwareStats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * A class that represents a subsystem that has motors (rather than something like LEDs).
 * This class will automatically stop all the motors when the robot is disabled, and set the motors to brake when the robot is enabled.
 * If a subsystem doesn't need to ever brake (i.e. shooter, flywheel, etc.), then it should override the {@link #setBrake(boolean)} method and do nothing.
 */
public abstract class MotorSubsystem extends edu.wpi.first.wpilibj2.command.SubsystemBase {
    public static boolean IS_BRAKING = true;
    private static final List<MotorSubsystem> REGISTERED_SUBSYSTEMS = new ArrayList<>();
    private static final Trigger DISABLED_TRIGGER = new Trigger(DriverStation::isDisabled);
    private static final Executor BRAKE_MODE_EXECUTOR = Executors.newFixedThreadPool(8);
    private static final LoggedNetworkBoolean ENABLE_EXTENSIVE_LOGGING = new LoggedNetworkBoolean("/SmartDashboard/EnableExtensiveLogging", RobotHardwareStats.isSimulation());

    static {
        DISABLED_TRIGGER.onTrue(new InstantCommand(() -> forEach(MotorSubsystem::stop)).ignoringDisable(true));
        DISABLED_TRIGGER.onFalse(new InstantCommand(() -> {
            setAllSubsystemsBrakeAsync(true);
            IS_BRAKING = true;
        }).ignoringDisable(true));
    }

    private final SysIdRoutine sysIDRoutine = createSysIDRoutine();

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
        BRAKE_MODE_EXECUTOR.execute(() -> forEach((subsystem) -> subsystem.setBrake(brake)));
    }

    public static boolean isExtensiveLoggingEnabled() {
        return ENABLE_EXTENSIVE_LOGGING.get() || RobotHardwareStats.isReplay();
    }

    /**
     * Runs periodically, to update the subsystem, and update the mechanism of the subsystem (if there is one).
     * This only updates the mechanism if the robot is in replay mode or extensive logging is enabled.
     * This function cannot be overridden. Use {@linkplain MotorSubsystem#updatePeriodically} or {@linkplain MotorSubsystem#updateMechanism} (depending on the usage) instead.
     */
    @Override
    public final void periodic() {
        updatePeriodically();
        if (isExtensiveLoggingEnabled())
            updateMechanism();
    }

    /**
     * Creates a quasistatic (ramp up) command for characterizing the subsystem's mechanism.
     *
     * @param direction the direction in which to run the test
     * @return the command
     * @throws IllegalStateException if the {@link MotorSubsystem#getSysIDConfig()} function wasn't overridden or returns null
     */
    public final Command getQuasistaticCharacterizationCommand(SysIdRoutine.Direction direction) throws IllegalStateException {
        if (sysIDRoutine == null)
            throw new IllegalStateException("Subsystem " + getName() + " doesn't have a SysID routine!");
        return sysIDRoutine.quasistatic(direction);
    }

    /**
     * Creates a dynamic (constant "step up") command for characterizing the subsystem's mechanism.
     *
     * @param direction the direction in which to run the test
     * @return the command
     * @throws IllegalStateException if the {@link MotorSubsystem#getSysIDConfig()} function wasn't overridden or returns null
     */
    public final Command getDynamicCharacterizationCommand(SysIdRoutine.Direction direction) throws IllegalStateException {
        if (sysIDRoutine == null)
            throw new IllegalStateException("Subsystem " + getName() + " doesn't have a SysID routine!");
        return sysIDRoutine.dynamic(direction);
    }

    /**
     * Drives the motor with the given voltage for characterizing.
     *
     * @param targetDrivePower the target drive power, unitless. This can be amps, volts, etc. Depending on the characterization type
     */
    public void sysIDDrive(double targetDrivePower) {
    }

    /**
     * Updates the SysId log of the motor states for characterizing.
     *
     * @param log the log to update
     */
    public void updateLog(SysIdRoutineLog log) {
    }

    public SysIdRoutine.Config getSysIDConfig() {
        return null;
    }

    /**
     * Sets whether the subsystem's motors should brake or coast.
     * If a subsystem doesn't need to ever brake (i.e. shooter, flywheel, etc.), don't implement this method.
     *
     * @param brake whether the motors should brake or coast
     */
    public void setBrake(boolean brake) {
    }

    /**
     * Updates periodically. Anything that should be updated periodically but isn't related to the mechanism (or shouldn't always be logged, in order to save resources) should be put here.
     */
    public void updatePeriodically() {
    }

    /**
     * Updates the mechanism of the subsystem periodically if the robot is in replay mode, or if {@linkplain MotorSubsystem#ENABLE_EXTENSIVE_LOGGING) is true.
     * This doesn't always run in order to save resources.
     */
    public void updateMechanism() {
    }

    public void changeDefaultCommand(Command newDefaultCommand) {
        final Command currentDefaultCommand = getDefaultCommand();
        if (currentDefaultCommand != null)
            currentDefaultCommand.cancel();
        setDefaultCommand(newDefaultCommand);
    }

    public abstract void stop();

    private SysIdRoutine createSysIDRoutine() {
        if (getSysIDConfig() == null)
            return null;

        return new SysIdRoutine(
                getSysIDConfig(),
                new SysIdRoutine.Mechanism(
                        (voltage) -> sysIDDrive(voltage.in(Units.Volts)),
                        this::updateLog,
                        this,
                        getName()
                )
        );
    }
}