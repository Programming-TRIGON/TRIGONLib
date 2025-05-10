package org.trigon.hardware.subsystems.elevator;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.util.Color;

public class ElevatorConfiguration {
    public String name = "";
    public double
            positionToleranceMeters = 1,
            drumRadiusMeters = 1,
            minimumHeight = 1,
            maximumHeight = 1,
            maximumVelocity = 1,
            maximumAcceleration = 1,
            maximumJerk = 1,
            sysIDRampRate = 1,
            sysIDStepVoltage = 1,
            gearRatio = 1,
            massKilograms = 1;
    public boolean
            focEnabled = true,
            shouldSimulateGravity = true;
    public Color mechanismColor = Color.kBlue;
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
