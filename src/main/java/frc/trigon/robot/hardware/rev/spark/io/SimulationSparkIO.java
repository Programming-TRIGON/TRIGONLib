package frc.trigon.robot.hardware.rev.spark.io;

import com.revrobotics.*;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.trigon.robot.hardware.rev.spark.SparkIO;
import frc.trigon.robot.hardware.rev.sparkecnoder.SparkEncoder;

public class SimulationSparkIO extends SparkIO {
    private final CANSparkMax motor;
    private final SparkPIDController pidController;
    private final SparkEncoder encoder;

    public SimulationSparkIO(int id, DCMotor gearbox) {
        motor = new CANSparkMax(id, CANSparkMax.MotorType.kBrushless);
        REVPhysicsSim.getInstance().addSparkMax(motor, gearbox);
        pidController = motor.getPIDController();
        encoder = SparkEncoder.createRelativeEncoder(motor);
    }

    @Override
    public void setReference(double value, CANSparkBase.ControlType ctrl) {
        pidController.setReference(value, ctrl);
    }

    @Override
    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot) {
        pidController.setReference(value, ctrl, pidSlot);
    }

    @Override
    public SparkEncoder getEncoder() {
        return encoder;
    }

    @Override
    public CANSparkBase getMotor() {
        return motor;
    }

    @Override
    public void stopMotor() {
        motor.stopMotor();
    }

    @Override
    public void setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame frame, int periodMs) {
        motor.setPeriodicFramePeriod(frame, periodMs);
    }

    @Override
    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward) {
        pidController.setReference(value, ctrl, pidSlot, arbFeedForward);
    }

    @Override
    public void setReference(double value, CANSparkBase.ControlType ctrl, int pidSlot, double arbFeedForward, SparkPIDController.ArbFFUnits arbFFUnits) {
        pidController.setReference(value, ctrl, pidSlot, arbFeedForward, arbFFUnits);
    }

    @Override
    public void setInverted(boolean inverted) {
        motor.setInverted(inverted);
    }

    @Override
    public void enableVoltageCompensation(double voltage) {
        motor.enableVoltageCompensation(voltage);
    }

    @Override
    public void setClosedLoopRampRate(double rampRate) {
        motor.setClosedLoopRampRate(rampRate);
    }

    @Override
    public void setSmartCurrentLimit(int limit) {
        motor.setSmartCurrentLimit(limit);
    }

    @Override
    public void setOpenLoopRampRate(double rampRate) {
        motor.setOpenLoopRampRate(rampRate);
    }

    @Override
    public void setPID(double p, double i, double d) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
    }

    @Override
    public void setConversionsFactor(double conversionsFactor) {
        encoder.setConversionsFactor(conversionsFactor);
    }

    @Override
    public void restoreFactoryDefaults() {
        motor.restoreFactoryDefaults();
    }

    @Override
    public void burnFlash() {
        motor.burnFlash();
    }

    @Override
    public void enablePIDWrapping(double minInput, double maxInput) {
        pidController.setPositionPIDWrappingEnabled(true);
        pidController.setPositionPIDWrappingMinInput(minInput);
        pidController.setPositionPIDWrappingMaxInput(maxInput);
    }
}
