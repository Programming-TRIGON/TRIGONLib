package frc.trigon.robot.utilities.mechanisms;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class MechanismConstants {
    static final Color8Bit
            GRAY = new Color8Bit(Color.kGray),
            BLUE = new Color8Bit(Color.kBlue),
            GREEN = new Color8Bit(Color.kGreen),
            RED = new Color8Bit(Color.kRed);
    static final double
            MECHANISM_LINE_WIDTH = 5,
            MECHANISM_LINE_LENGTH = 5,
            TARGET_ELEVATOR_POSITION_LIGAMENT_WIDTH = 10;
    static final double
            NEGATIVE_TOP_ANGLE = 45,
            NEGATIVE_BOTTOM_ANGLE = 315,
            POSITIVE_TOP_ANGLE = 210,
            POSITIVE_BOTTOM_ANGLE = 145,
            ZERO_TOP_ANGLE = 90,
            ZERO_BOTTOM_ANGLE = 270;
    static final double ELEVATOR_MECHANISM_STARTING_ANGLE = 90;
    static final double ARROW_LENGTH_SCALE = 0.2;
    static final double LIGAMENT_END_TO_EDGE_RATIO = 1.1;
}
