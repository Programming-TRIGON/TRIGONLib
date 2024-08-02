package frc.trigon.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A command that runs a function with values from the NetworkTables.
 */
public class NetworkTablesCommand extends Command {
    private final Consumer<Double[]> toRun;
    private final LoggedDashboardNumber[] dashboardNumbers;
    private final boolean runPeriodically;

    /**
     * Constructs a new NetworkTablesCommand that'll read multiple values from the NetworkTables.
     *
     * @param toRun           a consumer that takes an array of values from the NetworkTables
     * @param runPeriodically whether we should run the consumer periodically or once on initialize
     * @param requirements    the subsystems required by this command
     * @param keys            the keys to read from the NetworkTables. Make sure these keys are in the same length as the array accepted for the consumer
     */
    public NetworkTablesCommand(Consumer<Double[]> toRun, boolean runPeriodically, Set<Subsystem> requirements, String... keys) {
        this.toRun = toRun;
        this.runPeriodically = runPeriodically;
        dashboardNumbers = new LoggedDashboardNumber[keys.length];
        for (int i = 0; i < keys.length; i++)
            dashboardNumbers[i] = new LoggedDashboardNumber(keys[i]);
        addRequirements(requirements.toArray(new Subsystem[0]));
    }

    /**
     * Constructs a new NetworkTablesCommand that'll read one value from the NetworkTables.
     *
     * @param toRun           a consumer that takes a single value from the NetworkTables
     * @param runPeriodically whether we should run the consumer periodically or once on initialize
     * @param requirements    the subsystems required by this command
     * @param key             the key to read from the NetworkTables
     */
    public NetworkTablesCommand(Consumer<Double> toRun, boolean runPeriodically, Set<Subsystem> requirements, String key) {
        this((Double[] values) -> toRun.accept(values[0]), runPeriodically, requirements, key);
    }

    /**
     * Constructs a new NetworkTablesCommand that'll read two values from the NetworkTables.
     *
     * @param toRun           a consumer that takes two values from the NetworkTables
     * @param runPeriodically whether we should run the consumer periodically or once on initialize
     * @param key1            the first key to read from the NetworkTables
     * @param requirements    the subsystems required by this command
     * @param key2            the second key to read from the NetworkTables
     */
    public NetworkTablesCommand(BiConsumer<Double, Double> toRun, boolean runPeriodically, Set<Subsystem> requirements, String key1, String key2) {
        this((Double[] values) -> toRun.accept(values[0], values[1]), runPeriodically, requirements, key1, key2);
    }

    /**
     * Constructs a new NetworkTablesCommand that'll read values from the NetworkTables and run a command with them.
     *
     * @param commandCreator  a function that takes an array of values from the NetworkTables and returns a command to run
     * @param runPeriodically whether we should run the command's execute method periodically or once on initialize
     * @param keys            the keys to read from the NetworkTables. Make sure these keys are in the same length as the array accepted for the function
     */
    public NetworkTablesCommand(Function<Double[], Command> commandCreator, boolean runPeriodically, String... keys) {
        this(commandCreatorAsConsumer(commandCreator, runPeriodically), runPeriodically, commandCreator.apply(new Double[keys.length]).getRequirements(), keys);
    }

    /**
     * Constructs a new NetworkTablesCommand that'll read one value from the NetworkTables and run a command with it.
     *
     * @param commandCreator  a function that takes a single value from the NetworkTables and returns a command to run
     * @param runPeriodically whether we should run the command's execute method periodically or once on initialize
     * @param key             the key to read from the NetworkTables
     */
    public NetworkTablesCommand(Function<Double, Command> commandCreator, boolean runPeriodically, String key) {
        this(commandCreatorAsConsumer((Double[] values) -> commandCreator.apply(values[0]), runPeriodically), runPeriodically, commandCreator.apply(0.0).getRequirements(), key);
    }

    /**
     * Constructs a new NetworkTablesCommand that'll read two values from the NetworkTables and run a command with them.
     *
     * @param commandCreator  a function that takes two values from the NetworkTables and returns a command to run
     * @param runPeriodically whether we should run the command's execute method periodically or once on initialize
     * @param key1            the first key to read from the NetworkTables
     * @param key2            the second key to read from the NetworkTables
     */
    public NetworkTablesCommand(BiFunction<Double, Double, Command> commandCreator, boolean runPeriodically, String key1, String key2) {
        this(commandCreatorAsConsumer((Double[] values) -> commandCreator.apply(values[0], values[1]), runPeriodically), runPeriodically, commandCreator.apply(0.0, 0.0).getRequirements(), key1, key2);
    }

    @Override
    public void initialize() {
        toRun.accept(getDashboardNumbersValues());
    }

    @Override
    public void execute() {
        if (runPeriodically)
            toRun.accept(getDashboardNumbersValues());
    }

    /**
     * Converts a function that takes an array of values from the NetworkTables and returns a command
     * to a consumer that takes an array of values from the NetworkTables and runs the command.
     * This function is static, so it can be used in the constructor inside "this()".
     *
     * @param commandCreator  a function that takes an array of values from the NetworkTables and returns a command to run
     * @param runPeriodically whether we should run the command's execute method periodically or once on initialize
     * @return a consumer that takes an array of values from the NetworkTables and runs the command
     */
    private static Consumer<Double[]> commandCreatorAsConsumer(Function<Double[], Command> commandCreator, boolean runPeriodically) {
        if (runPeriodically)
            return (Double[] values) -> commandCreator.apply(values).execute();
        return (Double[] values) -> commandCreator.apply(values).initialize();
    }

    private Double[] getDashboardNumbersValues() {
        Double[] values = new Double[dashboardNumbers.length];
        for (int i = 0; i < dashboardNumbers.length; i++)
            values[i] = dashboardNumbers[i].get();
        return values;
    }
}
