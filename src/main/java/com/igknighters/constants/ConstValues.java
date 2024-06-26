package com.igknighters.constants;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.igknighters.subsystems.vision.camera.Camera;
import com.igknighters.subsystems.vision.camera.Camera.CameraConfig;
import com.igknighters.util.LerpTable;
import com.igknighters.util.SwerveModuleConstants;
import com.igknighters.util.LerpTable.LerpTableEntry;
import com.igknighters.util.SwerveModuleConstants.ModuleId;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

public final class ConstValues {
    private final static double TAU = 2 * Math.PI;

    // all measurements are in meters unless otherwise specified
    // all angles are in radians unless otherwise specified
    @SuppressWarnings("unused")
    private static final class Conv {
        public static final double FEET_TO_METERS = 0.3048;
        public static final double INCHES_TO_METERS = 0.0254;
        public static final double DEGREES_TO_RADIANS = Math.PI / 180.0;
        public static final double ROTATIONS_TO_RADIANTS = TAU;
    }

    @SuppressWarnings("unused")
    private static final class Motors {
        private static final class Falcon500 {
            public static final double FREE_SPEED = 668.1;
            public static final double FREE_CURRENT = 1.5;
            public static final double STALL_TORQUE = 4.69;
            public static final double STALL_CURRENT = 257.0;
        }

        private static final class Falcon500Foc {
            public static final double FREE_SPEED = 636.69;
            public static final double FREE_CURRENT = 1.5;
            public static final double STALL_TORQUE = 5.84;
            public static final double STALL_CURRENT = 304.0;
        }
    }

    public static final boolean DEBUG = true; // this should be false for competition
    public static final double PERIODIC_TIME = 0.02; // 20ms

    public static final class kDimensions {
        public static final double ROBOT_WIDTH = Units.inchesToMeters(26);
        public static final double ROBOT_LENGTH = Units.inchesToMeters(26);
        public static final double BUMPER_THICKNESS = Units.inchesToMeters(2.7);
        public static final double BELLYPAN_HEIGHT = Units.inchesToMeters(2);
    }

    public static final class kVision {
        /** The least trustworthy std dev */
        public static final Vector<N3> visionStdDevs = VecBuilder.fill(0.9, 0.9, 0.9);
        /** The middle trustworthy std dev */
        public static final Vector<N3> visionStdDevsTrust = VecBuilder.fill(0.4, 0.4, 0.4);
        /** The most trustworthy std dev */
        public static final Vector<N3> visionStdDevsReal = VecBuilder.fill(0.15, 0.15, 0.15);

        public static final double AMBIGUITY_CUTOFF = 0.5;

        public static final double MAX_Z_DELTA = 100.0;

        /**
         * The cameras used for vision.
         */
        public static final CameraConfig[] CAMERA_CONFIGS = new CameraConfig[] {
            Camera.createConfig(
                "photonvision-15",
                0,
                new Pose3d(
                    new Translation3d(Units.inchesToMeters(10.0), Units.inchesToMeters(10.0), Units.inchesToMeters(3.0)),
                    new Rotation3d(
                        0.0,
                        Units.degreesToRadians(15.0),
                        Units.degreesToRadians(45.0))
                )
            ),
            Camera.createConfig(
                "photonvision-16",
                1,
                new Pose3d(
                    new Translation3d(Units.inchesToMeters(10.0), Units.inchesToMeters(-10.0), Units.inchesToMeters(3.0)),
                    new Rotation3d(
                        0.0,
                        Units.degreesToRadians(15.0),
                        Units.degreesToRadians(-45.0))
                )
            )
        };
    }

    public static final class kSwerve {
        /**
         * The gear ratios for the swerve modules for easier constant definition.
         */
        @SuppressWarnings("unused")
        private static final class SwerveGearRatios {
            static final double L1_DRIVE = 1.0 / 8.14;
            static final double L2_DRIVE = 1.0 / 6.75;
            static final double L3_DRIVE = 1.0 / 6.12;
            static final double L4_DRIVE = 1.0 / 5.14;

            static final double ANGLE = 7.0 / 150.0;
        }

        public static final int PIGEON_ID = 33;
        public static final boolean INVERT_GYRO = false;
        public static final String CANBUS = "DriveBus";

        /* Drivetrain Constants */
        public static final double TRACK_WIDTH = 0.551942;
        public static final double WHEEL_DIAMETER = 0.1016;
        public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
        // public static final double DRIVEBASE_RADIUS = Math.sqrt(Math.pow(TRACK_WIDTH / 2.0, 2) + Math.pow(WHEEL_BASE / 2.0, 2));
        public static final double DRIVEBASE_RADIUS = 0.39;
        public static final double DRIVEBASE_CIRCUMFERENCE = DRIVEBASE_RADIUS * TAU;

        public static final double ANGLE_GEAR_RATIO = SwerveGearRatios.ANGLE;

        public static final double DRIVE_GEAR_RATIO = SwerveGearRatios.L3_DRIVE;

        /**Not every motor can output the max speed at all times, add a buffer to make closed loop more accurate */
        public static final double MOTOR_OUTPUT_SCALAR = 0.95;

        /**User defined acceleration time in seconds */
        public static final double ACCELERATION_TIME = 1.0;

        public static final double MAX_DRIVE_VELOCITY = 
            (Motors.Falcon500Foc.FREE_SPEED / TAU) * DRIVE_GEAR_RATIO * WHEEL_CIRCUMFERENCE * MOTOR_OUTPUT_SCALAR;
        public static final double MAX_DRIVE_ACCELERATION = MAX_DRIVE_VELOCITY / ACCELERATION_TIME;

        public static final double MAX_ANGULAR_VELOCITY = MAX_DRIVE_VELOCITY / DRIVEBASE_RADIUS;
        public static final double MAX_ANGULAR_ACCELERATION = MAX_ANGULAR_VELOCITY / ACCELERATION_TIME;

        /* Inverts */
        public static final InvertedValue ANGLE_MOTOR_INVERT = InvertedValue.Clockwise_Positive;
        public static final InvertedValue DRIVE_MOTOR_INVERT = InvertedValue.CounterClockwise_Positive;
        public static final SensorDirectionValue CANCODER_INVERT = SensorDirectionValue.CounterClockwise_Positive;

        /* Neutral Modes */
        public static final NeutralModeValue ANGLE_NEUTRAL_MODE = NeutralModeValue.Coast;
        public static final NeutralModeValue DRIVE_NEUTRAL_MODE = NeutralModeValue.Brake;

        public static final class DriveMotorConstants {
            public static final double kP = 0.4;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
        }

        public static final class AngleMotorConstants {
            public static final double kP = 11.0;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
        }

        public static final double ANGLE_CONTROLLER_KP = 4.0;

        public static final boolean ORIENT_TELEOP_FOR_SIM = true;

        public static final LerpTable TELEOP_TRANSLATION_AXIS_CURVE = new LerpTable(
            new LerpTableEntry(0.0, 0.0),
            new LerpTableEntry(0.1, 0.0), //deadzone
            new LerpTableEntry(0.7, 0.4),
            new LerpTableEntry(1.0, 1.0)
        );

        public static final LerpTable TELEOP_ROTATION_AXIS_CURVE = new LerpTable(
            new LerpTableEntry(0.0, 0.0),
            new LerpTableEntry(0.1, 0.0), //deadzone
            new LerpTableEntry(0.7, 0.4),
            new LerpTableEntry(1.0, 1.0)
        );

        public static final class Mod0 {
            public static final ModuleId MODULE = ModuleId.m0;
            public static final int DRIVE_MOTOR_ID = 1;
            public static final int ANGLE_MOTOR_ID = 2;
            public static final int CANCODER_ID = 21;
            public static final double ROTATION_OFFSET = 0.21875;
            public static final Translation2d CHASSIS_OFFSET = new Translation2d(-TRACK_WIDTH / 2.0, TRACK_WIDTH / 2.0);
            public static final SwerveModuleConstants CONSTANTS = new SwerveModuleConstants(MODULE, DRIVE_MOTOR_ID,
                    ANGLE_MOTOR_ID, CANCODER_ID, CHASSIS_OFFSET, ROTATION_OFFSET);
        }

        public static final class Mod1 {
            public static final ModuleId MODULE = ModuleId.m1;
            public static final int DRIVE_MOTOR_ID = 3;
            public static final int ANLGE_MOTOR_ID = 4;
            public static final int CANCODER_ID = 22;
            public static final double ROTATION_OFFSET = 0.3278805;
            public static final Translation2d CHASSIS_OFFSET = new Translation2d(TRACK_WIDTH / 2.0, TRACK_WIDTH / 2.0);
            public static final SwerveModuleConstants CONSTANTS = new SwerveModuleConstants(MODULE, DRIVE_MOTOR_ID,
                    ANLGE_MOTOR_ID, CANCODER_ID, CHASSIS_OFFSET, ROTATION_OFFSET);
        }

        public static final class Mod2 {
            public static final ModuleId MODULE = ModuleId.m2;
            public static final int DRIVE_MOTOR_ID = 5;
            public static final int ANGLE_MOTOR_ID = 6;
            public static final int CANCODER_ID = 23;
            public static final double ROTATION_OFFSET = 0.65;
            public static final Translation2d CHASSIS_OFFSET = new Translation2d(TRACK_WIDTH / 2.0, -TRACK_WIDTH / 2.0);
            public static final SwerveModuleConstants CONSTANTS = new SwerveModuleConstants(MODULE, DRIVE_MOTOR_ID,
                    ANGLE_MOTOR_ID, CANCODER_ID, CHASSIS_OFFSET, ROTATION_OFFSET);
        }

        public static final class Mod3 {
            public static final ModuleId MODULE = ModuleId.m3;
            public static final int DRIVE_MOTOR_ID = 7;
            public static final int ANGLE_MOTOR_ID = 8;
            public static final int CANCODER_ID = 24;
            public static final double ROTATION_OFFSET = 0.5776361;
            public static final Translation2d CHASSIS_OFFSET = new Translation2d(-TRACK_WIDTH / 2.0, -TRACK_WIDTH / 2.0);
            public static final SwerveModuleConstants CONSTANTS = new SwerveModuleConstants(MODULE, DRIVE_MOTOR_ID,
                    ANGLE_MOTOR_ID, CANCODER_ID, CHASSIS_OFFSET, ROTATION_OFFSET);
        }

        public static final Translation2d[] MODULE_CHASSIS_OFFSETS = new Translation2d[] {
            Mod0.CHASSIS_OFFSET,
            Mod1.CHASSIS_OFFSET,
            Mod2.CHASSIS_OFFSET,
            Mod3.CHASSIS_OFFSET
        };

        public static final SwerveDriveKinematics SWERVE_KINEMATICS = new SwerveDriveKinematics(
            MODULE_CHASSIS_OFFSETS
        );
    }

    public static final class kAuto {
        public static final PIDConstants AUTO_TRANSLATION_PID = new PIDConstants(3.4, 0, 0.0);
        public static final PIDConstants AUTO_ANGULAR_PID = new PIDConstants(3.0, 0.0, 0.0);
        public static final PathConstraints DYNAMIC_PATH_CONSTRAINTS = new PathConstraints(
            kSwerve.MAX_DRIVE_VELOCITY,
            kSwerve.MAX_DRIVE_ACCELERATION,
            kSwerve.MAX_ANGULAR_VELOCITY,
            kSwerve.MAX_ANGULAR_ACCELERATION
        );
        public static final ReplanningConfig DYNAMIC_REPLANNING_CONFIG = new ReplanningConfig(
            true,
            false
        );
    }
}
