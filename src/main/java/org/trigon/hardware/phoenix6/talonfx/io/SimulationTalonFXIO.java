package org.trigon.hardware.phoenix6.talonfx.io;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.TalonFXSimState;
import org.trigon.hardware.phoenix6.talonfx.TalonFXIO;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class SimulationTalonFXIO extends TalonFXIO {
    private final TalonFX talonFX;
    private final TalonFXSimState motorSimState;
    private MotorPhysicsSimulation physicsSimulation = null;

    public SimulationTalonFXIO(int id) {
        this.talonFX = new TalonFX(id);
        this.motorSimState = talonFX.getSimState();
        motorSimState.setSupplyVoltage(12);
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
        configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        if (configuration.Feedback.RotorToSensorRatio != 1.0) {
            configuration.Feedback.SensorToMechanismRatio *= configuration.Feedback.RotorToSensorRatio;
            configuration.Feedback.RotorToSensorRatio = 1.0;
        }
        talonFX.getConfigurator().apply(configuration);
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
}
