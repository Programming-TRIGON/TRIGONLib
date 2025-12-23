package frc.trigon.lib.commands;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

/**
 * A wait command that ends when a given value changes.
 */
public class WaitUntilChangeCommand<T> extends Command {
    private final Supplier<T> valueSupplier;
    private T lastValue;

    /**
     * Creates a new WaitUntilChangeCommand.
     * Will run until the given value supplier returns a different value than it did when the command was initialized.
     *
     * @param valueSupplier the supplier of the value to monitor for changes
     */
    public WaitUntilChangeCommand(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Override
    public void initialize() {
        lastValue = valueSupplier.get();
    }

    @Override
    public boolean isFinished() {
        final T currentValue = valueSupplier.get();

        if (currentValue.equals(lastValue)) {
            lastValue = currentValue;
            return false;
        }

        return true;
    }
}
