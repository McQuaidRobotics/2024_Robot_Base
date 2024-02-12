package com.igknighters.commands;

import com.igknighters.commands.stem.StemCommands;
import com.igknighters.commands.swerve.SwerveCommands;
import com.igknighters.commands.swerve.teleop.TeleopSwerveTarget;
import com.igknighters.commands.umbrella.UmbrellaCommands;
import com.igknighters.constants.FieldConstants;
import com.igknighters.controllers.ControllerParent;
import com.igknighters.subsystems.stem.Stem;
import com.igknighters.subsystems.stem.StemPosition;
import com.igknighters.subsystems.swerve.Swerve;
import com.igknighters.subsystems.umbrella.Umbrella;
import com.igknighters.util.AllianceFlip;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class HigherOrderCommands {

    public static Command intakeGamepiece(Stem stem, Umbrella umbrella) {
        return Commands.race(
                StemCommands.holdAt(stem, StemPosition.INTAKE),
                UmbrellaCommands.intake(umbrella)).andThen(
                        StemCommands.moveTo(stem, StemPosition.STOW));
    }

    public static Command scoreAmp(Swerve swerve, Stem stem, Umbrella umbrella) {
        return Commands.parallel(
            StemCommands.moveTo(stem, StemPosition.AMP),
            SwerveCommands.driveToAmp(swerve),
            UmbrellaCommands.spinupShooter(umbrella, 1500)
        ).andThen(
            UmbrellaCommands.shoot(umbrella)
        );
    }

    public static Command aim(
        Swerve swerve,
        Stem stem,
        Umbrella umbrella,
        ControllerParent controller
    ) {
        final var speakerTranslation = AllianceFlip.flipTranslation(FieldConstants.Speaker.SPEAKER_CENTER);
        final double rpm = 3780;
        return Commands.parallel(
            new TeleopSwerveTarget(swerve, controller)
                .withTarget(speakerTranslation.toTranslation2d())
                .withSpeedMultiplier(0.1),
            StemCommands.aimAt(stem, speakerTranslation, rpm),
            UmbrellaCommands.spinupShooter(umbrella, rpm)
        );
    }
}