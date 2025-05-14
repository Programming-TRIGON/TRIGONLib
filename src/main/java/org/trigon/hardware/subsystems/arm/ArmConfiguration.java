package org.trigon.hardware.subsystems.arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.util.Color;

public class ArmConfiguration {
    public String name = "";
    public double
            lengthMeters = 1,
            maximumVelocity = 1,
            maximumAcceleration = 1,
            maximumJerk = 1,
            sysIDRampRate = 1,
            sysIDStepVoltage = 1,
            gearRatio = 1,
            massKilograms = 1;
    public Rotation2d
            maximumAngle = Rotation2d.kZero,
            minimumAngle = Rotation2d.kZero,
            angleTolerance = Rotation2d.kZero;
    public boolean
            focEnabled = true,
            shouldSimulateGravity = true;
    public Color mechanismColor = Color.kBlue;
    public DCMotor gearbox = DCMotor.getKrakenX60Foc(1);
}
