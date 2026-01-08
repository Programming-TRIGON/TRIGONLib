package frc.trigon.lib.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.trigon.lib.hardware.RobotHardwareStats;

/**
 * A class that represents a simulation of a simple motor mechanism, such as flywheel and turret mechanisms.
 */
public class SimpleMotorSimulation extends MotorPhysicsSimulation {
    private final DCMotorSim motorSimulation;

    /**
     * Creates a new SimpleMotorSimulation.
     *
     * @param gearbox         the gearbox of the motor(s)
     * @param gearRatio       the gearbox's gear ratio
     * @param momentOfInertia the moment of inertia of the motor(s)
     */
    public SimpleMotorSimulation(DCMotor gearbox, double gearRatio, double momentOfInertia) {
        super(gearRatio);
        motorSimulation = new DCMotorSim(LinearSystemId.createDCMotorSystem(gearbox, momentOfInertia, gearRatio), gearbox);
    }

    /**
     * Creates a new SimpleMotorSimulation.
     *
     * @param gearbox   The gearbox of the motor(s)
     * @param gearRatio The gearbox's gear ratio
     * @param kv        voltage needed to maintain constant velocity
     * @param ka        voltage needed to induce a specific acceleration
     */
    public SimpleMotorSimulation(DCMotor gearbox, double gearRatio, double kv, double ka) {
        super(gearRatio);
        motorSimulation = new DCMotorSim(LinearSystemId.createDCMotorSystem(kv, ka), gearbox);
    }

    /**
     * @return the current in amperes
     */
    @Override
    public double getCurrent() {
        return motorSimulation.getCurrentDrawAmps();
    }

    /**
     * @return the position in rotations
     */
    @Override
    public double getSystemPositionRotations() {
        return Units.radiansToRotations(motorSimulation.getAngularPositionRad());
    }

    /**
     * @return the velocity in rotations per second
     */
    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Units.radiansToRotations(motorSimulation.getAngularVelocityRadPerSec());
    }

    /**
     * Sets the input voltage of the motor.
     *
     * @param voltage the voltage to set
     */
    @Override
    public void setInputVoltage(double voltage) {
        motorSimulation.setInputVoltage(voltage);
    }

    /**
     * Updates the motor simulation.
     */
    @Override
    public void updateMotor() {
        motorSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
    }
}