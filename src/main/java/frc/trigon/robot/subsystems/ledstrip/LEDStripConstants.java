package frc.trigon.robot.subsystems.ledstrip;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import frc.trigon.robot.constants.RobotConstants;

public class LEDStripConstants {
    private static final int CANDLE_ID = 0;
    private static final CANdle.LEDStripType STRIP_TYPE = CANdle.LEDStripType.RGB;
    private static final double BRIGHTNESS_SCALAR = 0.5;
    static final double
            MINIMUM_BATTERY_VOLTAGE = 10.5,
            LOW_BATTERY_FLASHING_SPEED = 1;
    static final CANdle CANDLE = new CANdle(CANDLE_ID, RobotConstants.CANIVORE_NAME);

    public static final LEDStrip
            REAR_LEFT_STRIP = new LEDStrip(24, false),
            FRONT_LEFT_STRIP = new LEDStrip(17, true),
            REAR_RIGHT_STRIP = new LEDStrip(24, false),
            FRONT_RIGHT_STRIP = new LEDStrip(17, true);
    public static final LEDStrip[] LED_STRIPS = {
            REAR_LEFT_STRIP,
            FRONT_LEFT_STRIP,
            REAR_RIGHT_STRIP,
            FRONT_RIGHT_STRIP
    };

    static {
        final CANdleConfiguration config = new CANdleConfiguration();
        config.stripType = STRIP_TYPE;
        config.brightnessScalar = BRIGHTNESS_SCALAR;
        config.disableWhenLOS = true;
        config.enableOptimizations = true;
        CANDLE.configAllSettings(config);
    }
}
