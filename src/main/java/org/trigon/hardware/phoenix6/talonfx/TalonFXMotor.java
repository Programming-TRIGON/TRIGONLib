package org.trigon.hardware.phoenix6.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import org.littletonrobotics.junction.Logger;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.phoenix6.Phoenix6Inputs;
import org.trigon.hardware.phoenix6.talonfx.io.RealTalonFXIO;
import org.trigon.hardware.phoenix6.talonfx.io.SimulationTalonFXIO;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class TalonFXMotor {
    private final String motorName;
    private final TalonFXIO motorIO;
    private final Phoenix6Inputs motorInputs;
    private final int id;

    public TalonFXMotor(int id, String motorName) {
        this(id, motorName, "");
    }

    public TalonFXMotor(int id, String motorName, String canbus) {
        this.motorName = motorName;
        this.motorIO = generateIO(id, canbus);
        this.motorInputs = new Phoenix6Inputs(motorName);
        this.id = id;
        motorIO.optimizeBusUsage();
    }

    public void update() {
        motorIO.updateMotor();
        Logger.processInputs("Motors/" + motorName, motorInputs);
    }

    public int getID() {
        return id;
    }

    public void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
        motorIO.setPhysicsSimulation(physicsSimulation);
    }

    public void applyConfigurations(TalonFXConfiguration realConfiguration, TalonFXConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(simulationConfiguration);
        else
            motorIO.applyConfiguration(realConfiguration);
    }

    public void applyConfiguration(TalonFXConfiguration simulationAndRealConfiguration) {
        motorIO.applyConfiguration(simulationAndRealConfiguration);
    }

    public void applyRealConfiguration(TalonFXConfiguration realConfiguration) {
        if (!RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(realConfiguration);
    }

    public void applySimulationConfiguration(TalonFXConfiguration simulationConfiguration) {
        if (RobotHardwareStats.isSimulation())
            motorIO.applyConfiguration(simulationConfiguration);
    }

    public void stopMotor() {
        motorIO.stopMotor();
    }

    public double getSignal(TalonFXSignal signal) {
        return motorInputs.getSignal(signal.name);
    }

    public double[] getThreadedSignal(TalonFXSignal signal) {
        return motorInputs.getThreadedSignal(signal.name);
    }

    public void registerSignal(TalonFXSignal signal, double updateFrequencyHertz) {
        motorInputs.registerSignal(motorSignalToStatusSignal(signal), updateFrequencyHertz);
    }

    public void registerThreadedSignal(TalonFXSignal signal, TalonFXSignal slopeSignal, double updateFrequencyHertz) {
        motorInputs.registerThreadedSignal(motorSignalToStatusSignal(signal), motorSignalToStatusSignal(slopeSignal), updateFrequencyHertz);
    }

    public void setControl(ControlRequest request) {
        motorIO.setControl(request);
    }

    public void setPosition(double positionRotations) {
        motorIO.setPosition(positionRotations);
    }

    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    private BaseStatusSignal motorSignalToStatusSignal(TalonFXSignal signal) {
        final TalonFX talonFX = motorIO.getTalonFX();
        if (RobotHardwareStats.isReplay() || talonFX == null)
            return null;

        return signal.signalFunction.apply(talonFX);
    }

    private TalonFXIO generateIO(int id, String canbus) {
        if (RobotHardwareStats.isReplay())
            return new TalonFXIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationTalonFXIO(id);
        return new RealTalonFXIO(id, canbus);
    }
}
