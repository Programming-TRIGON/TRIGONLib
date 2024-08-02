package frc.trigon.robot.hardware.simulation;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import frc.trigon.robot.constants.RobotConstants;
import frc.trigon.robot.utilities.Conversions;

public class ElevatorSimulation extends MotorPhysicsSimulation {
    private final ElevatorSim elevatorSimulation;
    private final double retractedHeightMeters;
    private final double diameterMeters;

    public ElevatorSimulation(DCMotor gearbox, double gearRatio, double carriageMassKilograms, double drumRadiusMeters, double retractedHeightMeters, double maximumHeightMeters, boolean simulateGravity) {
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
    public double getPositionRotations() {
        return Conversions.distanceToRotations(elevatorSimulation.getPositionMeters() - retractedHeightMeters, diameterMeters);
    }

    @Override
    public double getVelocityRotationsPerSecond() {
        return Conversions.distanceToRotations(elevatorSimulation.getVelocityMetersPerSecond(), diameterMeters);
    }

    @Override
    public void setInputVoltage(double voltage) {
        elevatorSimulation.setInputVoltage(voltage);
    }

    @Override
    public void updateMotor() {
        elevatorSimulation.update(RobotConstants.PERIODIC_TIME_SECONDS);
    }
}
