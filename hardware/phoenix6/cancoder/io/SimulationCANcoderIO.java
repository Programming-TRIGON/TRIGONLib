package frc.trigon.lib.hardware.phoenix6.cancoder.io;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.sim.CANcoderSimState;
import frc.trigon.lib.hardware.phoenix6.cancoder.CANcoderIO;

import java.util.function.DoubleSupplier;

public class SimulationCANcoderIO extends CANcoderIO {
    private final CANcoder cancoder;
    private final CANcoderSimState simState;
    private DoubleSupplier
            positionSupplier = null,
            velocitySupplier = null;

    public SimulationCANcoderIO(int id) {
        this.cancoder = new CANcoder(id);
        cancoder.setPosition(0);
        this.simState = cancoder.getSimState();
    }

    @Override
    public void updateEncoder() {
        simState.setRawPosition(positionSupplier == null ? 0 : positionSupplier.getAsDouble());
        simState.setVelocity(velocitySupplier == null ? 0 : velocitySupplier.getAsDouble());
    }

    @Override
    public void setSimulationInputSuppliers(DoubleSupplier positionSupplierRotations, DoubleSupplier velocitySupplierRotationsPerSecond) {
        this.positionSupplier = positionSupplierRotations;
        this.velocitySupplier = velocitySupplierRotationsPerSecond;
    }

    @Override
    public void applyConfiguration(CANcoderConfiguration configuration) {
        final CANcoderConfiguration adaptedConfiguration = adaptConfigurationToSimulation(configuration);
        cancoder.getConfigurator().apply(adaptedConfiguration);
    }

    @Override
    public void optimizeBusUsage() {
        cancoder.optimizeBusUtilization();
    }

    @Override
    protected void setPosition(double positionRotations) {
        cancoder.setPosition(positionRotations);
    }

    @Override
    public CANcoder getCANcoder() {
        return cancoder;
    }

    /**
     * Adapts the configuration for simulation. There are some setting that don't work well in simulation and need to be adapted.
     * There's no reason to use the sensor direction in simulation, and it can sometimes cause problems so it is always set to CounterClockwise_Positive.
     * The magnet offset is set to 0 because the magnet offset is not used in simulation.
     *
     * @param configuration the configuration to adapt
     * @return the adapted configuration
     */
    private CANcoderConfiguration adaptConfigurationToSimulation(CANcoderConfiguration configuration) {
        configuration.MagnetSensor.MagnetOffset = 0;
        configuration.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        return configuration;
    }
}
