package com.igknighters;

import com.igknighters.constants.ConstValues;
import com.igknighters.constants.RobotSetup;
import com.igknighters.constants.ConstValues.kAuto;
import com.igknighters.constants.ConstValues.kSwerve;
import com.igknighters.controllers.DriverController;
// import com.igknighters.controllers.InspectorController;
import com.igknighters.controllers.OperatorController;
import com.igknighters.controllers.TestingController;
import com.igknighters.subsystems.swerve.Swerve;
import com.igknighters.util.geom.AllianceFlip;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.igknighters.SubsystemResources.AllSubsystems;
import com.igknighters.commands.autos.AutosCmdRegister;
import com.igknighters.commands.swerve.teleop.TeleopSwerveBase;
import edu.wpi.first.wpilibj.DriverStation;

public class RobotContainer {

    private final DriverController driverController;
    private final OperatorController operatorController;
    private final TestingController testingController;
    // private final InspectorController inspectorController;

    private final AllSubsystems allSubsystems;

    public RobotContainer() {
        DriverStation.silenceJoystickConnectionWarning(ConstValues.DEBUG);

        driverController = new DriverController(0);
        operatorController = new OperatorController(1);
        testingController = new TestingController(3);
        // inspectorController = new InspectorController(4);

        allSubsystems = new AllSubsystems(RobotSetup.getRobotID().subsystems);

        driverController.assignButtons(allSubsystems);
        operatorController.assignButtons(allSubsystems);
        testingController.assignButtons(allSubsystems);
        // inspectorController.assignButtons(allSubsystems);

        if (allSubsystems.swerve.isPresent()) {
            var swerve = allSubsystems.swerve.get();

            swerve.setDefaultCommand(new TeleopSwerveBase.TeleopSwerveOmni(swerve, driverController));

            setupAutos(swerve);
        }
        
        // if (allSubsystems.stem.isPresent()) {
        //     var stem = allSubsystems.stem.get();
        //     stem.setDefaultCommand(stem.run(() -> {
        //         stem.setStemVolts(
        //                 testingController.leftStickY(0.1).getAsDouble() * 12.0,
        //                 (testingController.rightTrigger(true).getAsDouble()
        //                         - testingController.leftTrigger(true).getAsDouble()) * 6.0,
        //                 testingController.rightStickY(0.1).getAsDouble() * 12.0);
        //     }).withName("StemDefaultCommand"));
        // }

        // if (allSubsystems.umbrella.isPresent()) {
        //     var umbrella = allSubsystems.umbrella.get();
        //     Rectangle2d friendlyArea = new Rectangle2d(
        //             0, 0,
        //             FieldConstants.FIELD_LENGTH * 0.65,
        //             FieldConstants.FIELD_WIDTH);
        //     PolyTrigger trigger = new PolyTrigger(
        //         (AllianceFlip.isBlue()
        //             ? friendlyArea
        //             : AllianceFlip.flipRectangle(friendlyArea)
        //         ).asPolygon2d()
        //     );
        //     umbrella.setDefaultCommand(umbrella.run(() -> {
        //         if (trigger.getAsBoolean()) {
        //             umbrella.spinupShooterToRPM(400);
        //             umbrella.runIntakeAt(0);
        //         }
        //     }).withName("UmbrellaDefaultCommand"));
        // }

        // if (allSubsystems.umbrella.isPresent()) {
        //     var umbrella = allSubsystems.umbrella.get();
        //     umbrella.setDefaultCommand(
        //         UmbrellaCommands.spinUmbrellaBoth(umbrella)
        //             .withName("UmbrellaDefaultCommand")
        //     );
        // }
    }

    private void setupAutos(Swerve swerve) {

        if (AutoBuilder.isConfigured() && GlobalState.isUnitTest()) {
            // this code can be run multiple times during unit tests,
            // because of AutoBuilder once paradigm this causes a crash
            return;
        }

        AutosCmdRegister.registerCommands(allSubsystems);

        AutoBuilder.configureHolonomic(
                swerve::getPose,
                swerve::resetOdometry,
                swerve::getChassisSpeed,
                chassisSpeeds -> swerve.drive(
                        chassisSpeeds, false),
                new HolonomicPathFollowerConfig(
                        kAuto.AUTO_TRANSLATION_PID,
                        kAuto.AUTO_ANGULAR_PID,
                        kSwerve.MAX_DRIVE_VELOCITY,
                        kSwerve.DRIVEBASE_RADIUS,
                        kAuto.DYNAMIC_REPLANNING_CONFIG),
                AllianceFlip::isRed,
                swerve);

        GlobalState.onceInitAutoChooser(swerve);
    }

    AllSubsystems getAllSubsystemsForTest() {
        if (!GlobalState.isUnitTest()) {
            throw new RuntimeException("This method should only be called in unit tests");
        }
        return allSubsystems;
    }
}
