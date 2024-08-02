package frc.trigon.robot.utilities.mirrorable;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.trigon.robot.constants.FieldConstants;

public class MirrorablePose2d extends Mirrorable<Pose2d> {
    public MirrorablePose2d(Pose2d nonMirroredPose, boolean mirrorWhenRedAlliance) {
        super(nonMirroredPose, mirrorWhenRedAlliance);
    }

    public MirrorablePose2d(double x, double y, Rotation2d rotation, boolean mirrorWhenRedAlliance) {
        this(new Pose2d(x, y, rotation), mirrorWhenRedAlliance);
    }

    public MirrorablePose2d(Translation2d translation2d, double rotation, boolean mirrorWhenRedAlliance) {
        this(new Pose2d(translation2d, new Rotation2d(rotation)), mirrorWhenRedAlliance);
    }

    public MirrorableRotation2d getRotation() {
        return new MirrorableRotation2d(nonMirroredObject.getRotation(), mirrorWhenRedAlliance);
    }

    @Override
    protected Pose2d mirror(Pose2d pose) {
        return new Pose2d(
                FieldConstants.FIELD_LENGTH_METERS - pose.getX(),
                pose.getY(),
                HALF_ROTATION.minus(pose.getRotation())
        );
    }
}
