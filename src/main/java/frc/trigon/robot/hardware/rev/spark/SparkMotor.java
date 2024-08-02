package frc.trigon.robot.hardware.rev.spark;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Timer;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.hardware.rev.spark.io.RealSparkIO;
import frc.trigon.robot.hardware.rev.spark.io.SimulationSparkIO;
import org.littletonrobotics.junction.Logger;

import java.util.concurrent.CompletableFuture;

public class SparkMotor {
    private final String motorName;
    private final SparkIO motorIO;
    private final SparkInputs motorInputs;
    private final int id;

    public SparkMotor(int id, SparkType sparkType, String motorName, DCMotor simulationMotor) {
        this.id = id;
        this.motorName = motorName;
        motorInputs = new SparkInputs(motorName);
        motorIO = createSparkIO(id, sparkType, simulationMotor);
    }

    public void update() {
        Logger.processInputs("Motors/" + motorName, motorInputs);
    }

    public int getID() {
        return id;
    }

    public void registerSignal(SparkSignal signal) {
        this.registerSignal(signal, false);
    }

    public void registerSignal(SparkSignal signal, boolean isThreaded) {
        final SparkStatusSignal statusSignal = signal.getStatusSignal(motorIO.getMotor(), motorIO.getEncoder());
        if (isThreaded)
            motorInputs.registerThreadedSignal(statusSignal);
        else
            motorInputs.registerSignal(statusSignal);
    }

    public double getSignal(SparkSignal signal) {
        return motorInputs.getSignal(signal.name);
    }

    public void getThreadedSignal(SparkSignal signal) {
        motorInputs.getThreadedSignal(signal.name);
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl) {
        motorIO.setReference(value, ctrl);
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot) {
        motorIO.setReference(value, ctrl, pidSlot);
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward) {
        motorIO.setReference(value, ctrl, pidSlot, arbFeedForward);
    }

    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward, SparkPIDController.ArbFFUnits arbFFUnits) {
        motorIO.setReference(value, ctrl, pidSlot, arbFeedForward, arbFFUnits);
    }

    public void setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame frame, int periodMs) {
        motorIO.setPeriodicFramePeriod(frame, periodMs);
    }

    public void stopMotor() {
        motorIO.stopMotor();
    }

    public void setBrake(boolean brake) {
        motorIO.setBrake(brake);
    }

    public void setInverted(boolean inverted) {
        motorIO.setInverted(inverted);
    }

    public void enableVoltageCompensation(double voltage) {
        motorIO.enableVoltageCompensation(voltage);
    }

    public void setClosedLoopRampRate(double rampRate) {
        motorIO.setClosedLoopRampRate(rampRate);
    }

    public void setSmartCurrentLimit(int limit) {
        motorIO.setSmartCurrentLimit(limit);
    }

    public void setOpenLoopRampRate(double rampRate) {
        motorIO.setOpenLoopRampRate(rampRate);
    }

    public void setPID(double p, double i, double d) {
        motorIO.setPID(p, i, d);
    }

    public void setConversionsFactor(double conversionsFactor) {
        motorIO.setConversionsFactor(conversionsFactor);
    }

    public void restoreFactoryDefaults() {
        motorIO.restoreFactoryDefaults();
    }

    public void enablePIDWrapping(double minInput, double maxInput) {
        motorIO.enablePIDWrapping(minInput, maxInput);
    }

    public void burnFlash() {
        CompletableFuture.runAsync(() -> {
            Timer.delay(5);
            motorIO.burnFlash();
        });
    }

    private SparkIO createSparkIO(int id, SparkType sparkType, DCMotor simulationMotor) {
        if (RobotConstants.IS_REPLAY)
            return new SparkIO();
        if (RobotConstants.IS_SIMULATION)
            return new SimulationSparkIO(id, simulationMotor);
        return new RealSparkIO(id, sparkType);
    }
}
