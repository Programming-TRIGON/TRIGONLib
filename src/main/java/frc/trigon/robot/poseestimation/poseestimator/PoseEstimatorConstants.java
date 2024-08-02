package frc.trigon.robot.poseestimation.poseestimator;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;

public class PoseEstimatorConstants {
    public static final double ODOMETRY_FREQUENCY_HERTZ = 50;

    /**
     * The vector represents how ambiguous each value of the odometry is.
     * The first value represents how ambiguous is the x,
     * the second one for the y, and the third one is for the theta (rotation).
     * Increase these numbers to trust the estimate less.
     */
    static final Vector<N3> ODOMETRY_AMBIGUITY = VecBuilder.fill(0.003, 0.003, 0.0002);

    static final double
            TRANSLATIONS_STD_EXPONENT = 0.005,
            THETA_STD_EXPONENT = 0.01;
}

