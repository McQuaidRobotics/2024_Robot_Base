package com.igknighters.controllers;

import com.igknighters.SubsystemResources.Subsystems;
import com.igknighters.commands.stem.StemCommands;
import com.igknighters.subsystems.stem.StemPosition;
import com.igknighters.subsystems.stem.wrist.WristRealSuicidal;

import edu.wpi.first.wpilibj2.command.Commands;

public class OperatorController extends ControllerParent {

    public OperatorController(int port) {
        super(port, true, ControllerType.Operator);
        // disregard null safety as it is checked on assignment

        /// FACE BUTTONS
        this.A.binding = new Binding((trig, allss) -> {
            trig.onTrue(
                Commands.sequence(
                    Commands.runOnce(
                        () -> WristRealSuicidal.sweetReleaseOfDeath = false
                    ),
                    StemCommands.holdAt(allss.stem.get(), StemPosition.CLIMB)
                )
            );
        }, Subsystems.Stem);

        // this.B.binding =

        // this.X.binding =

        // this.Y.binding =

        /// BUMPER
        // this.LB.binding =

        // this.RB.binding =

        /// CENTER BUTTONS
        // this.Back.binding =

        // this.Start.binding =

        /// STICKS
        // this.LS.binding =

        // this.RS.binding =

        /// TRIGGERS
        this.LT.binding = new Binding((trig, allss) -> {
            trig.and(this.RT.trigger).whileTrue(
                    StemCommands.LimitedManualControl(
                            allss.stem.get(),
                            this.leftStickY(),
                            this.rightStickY(),
                            0.225));
        }, Subsystems.Stem);

        // this.RT.binding = DON'T USE!!! OTHER TRIGGERS COMMANDS USES BOTH TRIGGERS!!!

        /// DPAD
        // this.DPR.binding =

        // this.DPD.binding =

        // this.DPL.binding =

        // this.DPU.binding =
    }
}
