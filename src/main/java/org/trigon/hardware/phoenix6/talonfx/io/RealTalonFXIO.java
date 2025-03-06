package org.trigon.hardware.phoenix6.talonfx.io;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import org.trigon.hardware.phoenix6.talonfx.TalonFXIO;

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
        configuration.Audio.BeepOnBoot = false;
        configuration.Audio.BeepOnConfig = false;
        configuration.Audio.AllowMusicDurDisable = true;

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
