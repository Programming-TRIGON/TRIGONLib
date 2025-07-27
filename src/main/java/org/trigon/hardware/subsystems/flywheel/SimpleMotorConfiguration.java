package org.trigon.hardware.subsystems.flywheel;

import edu.wpi.first.math.system.plant.DCMotor;

public class SimpleMotorConfiguration {
    public String name = "";
    public double
            gearRatio = 1,
            momentOfInertia = 0.003,
            maximumVelocity = 1,
            maximumAcceleration = 1,
            maximumJerk = 1,
            maximumDisplayableVelocity = 1,
            velocityTolerance = 1,
            voltageTolerance = 1,
            sysIDRampRate = 1,
            sysIDStepVoltage = 1;
    public boolean focEnabled = true;
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
