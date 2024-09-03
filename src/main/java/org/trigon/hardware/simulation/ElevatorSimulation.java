package org.trigon.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.utilities.Conversions;

public class ElevatorSimulation extends MotorPhysicsSimulation {
    private final ElevatorSim elevatorSimulation;
    private final double retractedHeightMeters;
    private final double diameterMeters;

    public ElevatorSimulation(DCMotor gearbox, double gearRatio, double carriageMassKilograms, double drumRadiusMeters, double retractedHeightMeters, double maximumHeightMeters, boolean simulateGravity) {
        super(gearRatio);
        diameterMeters = drumRadiusMeters + drumRadiusMeters;
        this.retractedHeightMeters = retractedHeightMeters;
        elevatorSimulation = new ElevatorSim(
                gearbox,
                gearRatio,
                carriageMassKilograms,
                drumRadiusMeters,
                retractedHeightMeters,
                maximumHeightMeters,
                simulateGravity,
                retractedHeightMeters
        );
    }

    @Override
    public double getCurrent() {
        return elevatorSimulation.getCurrentDrawAmps();
    }

    @Override
    public double getSystemPositionRotations() {
        return Conversions.distanceToRotations(elevatorSimulation.getPositionMeters() - retractedHeightMeters, diameterMeters);
    }

    @Override
    public double getSystemVelocityRotationsPerSecond() {
        return Conversions.distanceToRotations(elevatorSimulation.getVelocityMetersPerSecond(), diameterMeters);
    }

    @Override
    public void setInputVoltage(double voltage) {
        elevatorSimulation.setInputVoltage(voltage);
    }

    @Override
    public void updateMotor() {
        elevatorSimulation.update(RobotHardwareStats.getPeriodicTimeSeconds());
    }
}
