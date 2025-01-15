package org.trigon.hardware.misc.servo;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.misc.servo.io.RealServoIO;
import org.trigon.hardware.misc.servo.io.SimulationServoIO;
import org.trigon.hardware.simulation.MotorPhysicsSimulation;

public class ServoIO {
    static ServoIO generateServoIO(int channel) {
        if (RobotHardwareStats.isReplay())
            return new ServoIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationServoIO(channel);
        return new RealServoIO(channel);
    }

    protected void updateInputs(ServoInputsAutoLogged inputs) {
    }

    protected void updateSimulation() {
    }

    protected void setTargetSpeed(double targetSpeed) {
    }

    protected void setTargetAngle(Rotation2d targetAngle) {
    }

    protected void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
    }

    protected void setPhysicsSimulation(MotorPhysicsSimulation physicsSimulation) {
    }

    @AutoLog
    protected static class ServoInputs {
        public Rotation2d targetAngle = Rotation2d.fromDegrees(0);
        public double speed = 0;
    }
}