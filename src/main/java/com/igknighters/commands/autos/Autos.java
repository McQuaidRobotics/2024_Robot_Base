package com.igknighters.commands.autos;

import com.igknighters.subsystems.swerve.Swerve;
import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Autos {
    private static SendableChooser<Command> autoChooser;

    public static void createSendableChooser(Swerve swerve) {
        autoChooser = AutoBuilder.buildAutoChooser();
        for (Command dynamicAutoCmd : DynamicRoutines.choosableDynamicRoutines(swerve)) {
            autoChooser.addOption("(Dynamic) " + dynamicAutoCmd.getName(), dynamicAutoCmd);
        }
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

public static Command getAutonomousCommand() {
        if (autoChooser == null) return new InstantCommand().withName("Nothing -> Auto Chooser Not Created!");
        return autoChooser.getSelected();
    }

    public static String getSelectedAutoName() {
        if (autoChooser == null) return "Nothing -> Auto Chooser Not Created!";
        return autoChooser.getSelected().getName();
    }
}