package frc.trigon.lib.hardware.phoenix6.talonfxs.io;

import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.TalonFXSSimState;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.phoenix6.talonfxs.TalonFXSIO;
import frc.trigon.lib.hardware.simulation.MotorPhysicsSimulation;

public class SimulationTalonFXSIO extends TalonFXSIO {
    private final TalonFXS talonFXS;
    private final TalonFXSSimState motorSimState;
    private MotorPhysicsSimulation physicsSimulation = null;

    public SimulationTalonFXSIO(int id) {
        this.talonFXS = new TalonFXS(id);
        this.motorSimState = talonFXS.getSimState();
        motorSimState.setSupplyVoltage(RobotHardwareStats.SUPPLY_VOLTAGE);
    }

    @Override
    public void updateMotor() {
        if (physicsSimulation == null)
            return;

        physicsSimulation.setInputVoltage(motorSimState.getMotorVoltage());
        physicsSimulation.updateMotor();

        motorSimState.setRawRotorPosition(physicsSimulation.getRotorPositionRotations());
        motorSimState.setRotorVelocity(physicsSimulation.getRotorVelocityRotationsPerSecond());
    }

    @Override
    public void setControl(ControlRequest request) {
        talonFXS.setControl(request);
    }

    @Override
    protected void setPosition(double positionRotations) {
        talonFXS.setPosition(positionRotations);
    }

    @Override
    public void applyConfiguration(TalonFXSConfiguration configuration) {
        final TalonFXSConfiguration adaptedConfiguration = adaptConfigurationToSimulation(configuration);
        talonFXS.getConfigurator().apply(adaptedConfiguration);
    }

    @Override
    public void optimizeBusUsage() {
        talonFXS.optimizeBusUtilization();
    }

    @Override
    public void stopMotor() {
        talonFXS.stopMotor();
    }

    @Override
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        this.physicsSimulation = physicsSimulation;
    }

    @Override
    public TalonFXS getTalonFXS() {
        return talonFXS;
    }

    /**
     * Adapts the configuration for simulation. There are some settings that don't work well in simulation and need to be adapted.
     * There's no reason to use the inverted value in simulation, and it can sometimes cause problems so it is always set to CounterClockwise_Positive.
     *
     * @param configuration the configuration to adapt
     * @return the adapted configuration
     */
    private TalonFXSConfiguration adaptConfigurationToSimulation(TalonFXSConfiguration configuration) {
        configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        return configuration;
    }
}
