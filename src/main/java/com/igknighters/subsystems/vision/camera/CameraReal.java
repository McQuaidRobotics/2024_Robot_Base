package com.igknighters.subsystems.vision.camera;

import java.util.List;
import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.estimation.TargetModel;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.igknighters.constants.FieldConstants;
import com.igknighters.util.BootupLogger;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;

/**
 * An abstraction for a photon camera.
 */
public class CameraReal implements Camera {
    private final PhotonCamera camera;
    private final Integer id;
    private final Transform3d cameraPose;
    private final PhotonPoseEstimator poseEstimator;

    private final CameraInput cameraInput;

    /**
     * Creates an abstraction for a photon camera.
     * 
     * @param cameraName The name of the camera
     * @param id         The ID of the camera
     * @param cameraPose The pose of the camera relative to the robot
     */
    public CameraReal(String cameraName, Integer id, Pose3d cameraPose) {
        this.camera = new PhotonCamera(cameraName);
        this.id = id;
        this.cameraPose = new Transform3d(cameraPose.getTranslation(), cameraPose.getRotation());

        poseEstimator = new PhotonPoseEstimator(
                FieldConstants.APRIL_TAG_FIELD,
                PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
                this.camera,
                this.cameraPose);
        poseEstimator.setTagModel(TargetModel.kAprilTag36h11);
        poseEstimator.setMultiTagFallbackStrategy(PoseStrategy.CLOSEST_TO_CAMERA_HEIGHT);

        cameraInput = new CameraInput(new VisionPoseEst(
                id,
                new Pose3d(),
                0,
                List.of(),
                0.0
        ));

        BootupLogger.BootupLog(cameraName + " camera initialized");
    }

    private Optional<VisionPoseEst> realEvaluatePose() {
        return poseEstimator.update()
                .map(estRoboPose -> new VisionPoseEst(
                        this.id,
                        estRoboPose.estimatedPose,
                        estRoboPose.timestampSeconds,
                        estRoboPose.targetsUsed
                            .stream()
                            .map(PhotonTrackedTarget::getFiducialId)
                            .toList(),
                        estRoboPose.targetsUsed
                            .stream()
                            .map(PhotonTrackedTarget::getPoseAmbiguity)
                            .reduce(0.0, Double::sum)
                            / estRoboPose.targetsUsed.size()
                    )
                );
    }

    @Override
    public Optional<VisionPoseEst> evalPose() {
        return cameraInput.getLatestPoseEst();
    }

    @Override
    public Transform3d getRobotToCameraTransform3d() {
        return cameraPose;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return camera.getName();
    }

    @Override
    public void periodic() {
        cameraInput.update(realEvaluatePose());

        Logger.processInputs("Vision/Camera[" + getName() + "]", cameraInput);
    }
}
