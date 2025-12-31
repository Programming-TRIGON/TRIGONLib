package frc.trigon.lib.hardware.talonfxs.io;

import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.trigon.lib.hardware.talonfxs.TalonFXSIO;

public class RealTalonFXSIO extends TalonFXSIO {
    private final TalonFXS talonFXS;

    public RealTalonFXSIO(int id, String canbus) {
        this.talonFXS = new TalonFXS(id, canbus);
    }

    @Override
    public void setControl(ControlRequest request) {
        talonFXS.setControl(request);
    }

    @Override
    protected void setPosition(double positionRotations) {
        talonFXS.setPosition(positionRotations);
    }

    @Override
    public void applyConfiguration(TalonFXSConfiguration configuration) {
        talonFXS.getConfigurator().apply(configuration);
    }

    @Override
    public void optimizeBusUsage() {
        talonFXS.optimizeBusUtilization();
    }

    @Override
    public void stopMotor() {
        talonFXS.stopMotor();
    }

    @Override
    public TalonFXS getTalonFXS() {
        return talonFXS;
    }

    public void setBrake(boolean brake) {
        talonFXS.setNeutralMode(brake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
    }
}
