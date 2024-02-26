package com.igknighters.controllers;

import com.igknighters.SubsystemResources.Subsystems;
import com.igknighters.commands.stem.StemCommands;
import com.igknighters.subsystems.stem.StemPosition;

@SuppressWarnings("unused")

public class OperatorController extends ControllerParent {

    public OperatorController(int port) {
        super(port, true, ControllerType.Operator);
        // disregard null safety as it is checked on assignment

        /// FACE BUTTONS
        this.A.binding = new Binding((trig, allss) -> {
            trig.onTrue(StemCommands.holdAt(allss.stem.get(), StemPosition.CLIMB));
        });

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
        // this.LT.binding = 

        // this.RT.binding =

        /// DPAD
        // this.DPR.binding =

        // this.DPD.binding =

        // this.DPL.binding =

        // this.DPU.binding =
    }
}
