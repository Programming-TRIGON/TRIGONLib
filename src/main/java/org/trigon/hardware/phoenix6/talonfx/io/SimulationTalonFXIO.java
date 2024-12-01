package org.trigon.hardware.phoenix6.talonfx.io;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.TalonFXSimState;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.phoenix6.talonfx.TalonFXIO;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SimulationTalonFXIO extends TalonFXIO {
    private final TalonFX talonFX;
    private final TalonFXSimState motorSimState;
    private MotorPhysicsSimulation physicsSimulation = null;

    public SimulationTalonFXIO(int id) {
        this.talonFX = new TalonFX(id);
        this.motorSimState = talonFX.getSimState();
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
        talonFX.setControl(request);
    }

    @Override
    protected void setPosition(double positionRotations) {
        talonFX.setPosition(positionRotations);
    }

    @Override
    public void applyConfiguration(TalonFXConfiguration configuration) {
        final TalonFXConfiguration adaptedConfiguration = adaptConfigurationToSimulation(configuration);
        talonFX.getConfigurator().apply(adaptedConfiguration);
    }

    @Override
    public void optimizeBusUsage() {
        talonFX.optimizeBusUtilization();
    }

    @Override
    public void stopMotor() {
        talonFX.stopMotor();
    }

    @Override
    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        this.physicsSimulation = physicsSimulation;
    }

    @Override
    public TalonFX getTalonFX() {
        return talonFX;
    }

    /**
     * Adapts the configuration for simulation. There are some setting that don't work well in simulation and need to be adapted.
     * The inverted value doesn't affect the simulation, and it can sometimes cause problems so it is always set to CounterClockwise_Positive.
     * The feedback sensor source is set to RotorSensor to avoid delays from using a CANCoder that gets its data from the motor anyway.
     * We don't use the SensorToMechanismRatio for the same reason as it is only used for remote encoders.
     * However, if someone did set a SensorToMechanismRatio, we still want to use it as the gear ratio so we multiply it by the SensorToMechanismRatio.
     *
     * @param configuration The configuration to adapt
     * @return The adapted configuration
     */
    private TalonFXConfiguration adaptConfigurationToSimulation(TalonFXConfiguration configuration) {
        configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        configuration.Feedback.SensorToMechanismRatio *= configuration.Feedback.RotorToSensorRatio;
        configuration.Feedback.RotorToSensorRatio = 1.0;
        return configuration;
    }
}