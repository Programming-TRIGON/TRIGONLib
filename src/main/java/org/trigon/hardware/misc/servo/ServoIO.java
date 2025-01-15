package org.trigon.hardware.misc.servo;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;
import org.trigon.hardware.RobotHardwareStats;
import org.trigon.hardware.misc.servo.io.RealServoIO;
import org.trigon.hardware.misc.servo.io.SimulationServoIO;

public class ServoIO {
    static ServoIO generateServoIO(int channel, String name) {
        if (RobotHardwareStats.isReplay())
            return new ServoIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationServoIO(channel, name);
        return new RealServoIO(channel);
    }

    protected void updateInputs(ServoInputsAutoLogged inputs) {
    }

    protected void setSpeed(double speed) {
    }

    protected void setAngle(Rotation2d angle) {
    }

    protected void setPosition(double position) {
    }

    protected void setBoundsMicroseconds(int maxPulseWidthMicroseconds, int maxDeadbandRangeMicroseconds, int centerPulseMicroseconds, int minDeadbandRangeMicroseconds, int minPulseWidthMicroseconds) {
    }

    @AutoLog
    protected static class ServoInputs {
        public double positionRotations = 0;
        public Rotation2d targetAngle = Rotation2d.fromDegrees(0);
        public double speed = 0;
    }
}