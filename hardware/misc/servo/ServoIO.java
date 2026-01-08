package frc.trigon.lib.hardware.misc.servo;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;
import frc.trigon.lib.hardware.RobotHardwareStats;
import frc.trigon.lib.hardware.misc.servo.io.RealServoIO;
import frc.trigon.lib.hardware.misc.servo.io.SimulationServoIO;

public class ServoIO {
    static ServoIO generateServoIO(int channel) {
        if (RobotHardwareStats.isReplay())
            return new ServoIO();
        if (RobotHardwareStats.isSimulation())
            return new SimulationServoIO();
        return new RealServoIO(channel);
    }

    protected void updateInputs(ServoInputsAutoLogged inputs) {
    }

    protected void setTargetSpeed(double targetSpeed) {
    }

    protected void setTargetAngle(Rotation2d targetAngle) {
    }

    protected void set(double value) {
    }

    protected void setPWMBoundaries(int maximumPulseWidthMicroseconds, int maximumDeadbandRangeMicroseconds,
                                    int centerPulseMicroseconds, int minimumDeadbandRangeMicroseconds,
                                    int minimumPulseWidthMicroseconds) {
    }

    protected void setMaximumAngle(Rotation2d maximumAngle) {
    }

    @AutoLog
    protected static class ServoInputs {
        public Rotation2d targetAngle = Rotation2d.fromDegrees(0);
        public double targetSpeed = 0;
    }
}