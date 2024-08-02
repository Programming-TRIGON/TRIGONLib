package frc.trigon.robot.hardware.phoenix6.talonfx.io;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.trigon.robot.hardware.phoenix6.talonfx.TalonFXIO;

public class RealTalonFXIO extends TalonFXIO {
    private final TalonFX talonFX;

    public RealTalonFXIO(int id, String canbus) {
        this.talonFX = new TalonFX(id, canbus);
    }

    @Override
    public void setControl(ControlRequest request) {
        talonFX.setControl(request);
    }

    @Override
    protected void setPosition(double positionRotations) {
        talonFX.setPosition(positionRotations);
    }

    @Override
    public void applyConfiguration(TalonFXConfiguration configuration) {
        talonFX.getConfigurator().apply(configuration);
    }

    @Override
    public void optimizeBusUsage() {
        talonFX.optimizeBusUtilization();
    }

    @Override
    public void stopMotor() {
        talonFX.stopMotor();
    }

    @Override
    public TalonFX getTalonFX() {
        return talonFX;
    }

    public void setBrake(boolean brake) {
        talonFX.setNeutralMode(brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
    }
}
