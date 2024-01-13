package com.igknighters.autos;

import java.util.function.Supplier;

import com.igknighters.constants.ConstValues.kAuto;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class DynamicPath {
    
    static enum DynPath {
        SPEAKER(new DynamicPath(
                FieldPositions.SPEAKER, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        AMP(new DynamicPath(
                FieldPositions.AMP, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        STAGE_CENTER(new DynamicPath(
                FieldPositions.STAGE_CENTER, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        STAGE_LEFT(new DynamicPath(
                FieldPositions.STAGE_LEFT, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        STAGE_RIGHT(new DynamicPath(
                FieldPositions.STAGE_RIGHT, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        NOTE_LEFT(new DynamicPath(
                FieldPositions.NOTE_LEFT, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        NOTE_CENTER(new DynamicPath(
                FieldPositions.NOTE_CENTER, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0)),
        NOTE_RIGHT(new DynamicPath(
                FieldPositions.NOTE_RIGHT, 
                kAuto.DYNAMIC_PATH_CONSTRAINTS, 
                kAuto.DYN_END_VELO, 
                0.0));

        private DynamicPath dynBlock;
        private DynPath(DynamicPath dynPath) {
            this.dynBlock = dynPath.withName(this.name());
        }

        public Command getCmd() {
            return dynBlock.getCmd().withName(this.name());
        }
    }

    private Command loggedCommad(Command cmd) {
        return new FunctionalCommand(
            () -> {
                SmartDashboard.putString("DynamicCmd", cmd.getName());
                cmd.initialize();
            },
            cmd::execute,
            (interupted) -> {
                SmartDashboard.putString("DynamicCmd", "");
                cmd.end(interupted);
            },
            cmd::isFinished,
            cmd.getRequirements().toArray(new Subsystem[0])
        );
    }

    private Command cmd;
    private String name = "Dynamic Path Command";

    public DynamicPath(double x, double y, double rotationDegrees) {
        cmd = AutoBuilder.pathfindToPose(
            new Pose2d(
                new Translation2d(x, y), 
                Rotation2d.fromDegrees(rotationDegrees)), 
            kAuto.DYNAMIC_PATH_CONSTRAINTS, 
            kAuto.DYN_END_VELO, 
            0.0);
    }
    public DynamicPath(Pose2d target, PathConstraints contraints, double endVelo, double rotDelay) {
        cmd = AutoBuilder.pathfindToPose(target, contraints, endVelo, rotDelay);
    }
    public DynamicPath(PathPlannerPath path, PathConstraints contraints, double rotDelay) {
        cmd = AutoBuilder.pathfindThenFollowPath(path, contraints, rotDelay);
    }

    public DynamicPath withName(String name) {
        this.name = name;
        return this;
    }

    public Command getCmd() {
        return loggedCommad(cmd.withName(name));
    }
}