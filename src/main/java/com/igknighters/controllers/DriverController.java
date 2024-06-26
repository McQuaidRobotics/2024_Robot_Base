package com.igknighters.controllers;

import com.igknighters.SubsystemResources.Subsystems;

import edu.wpi.first.wpilibj2.command.Commands;

public class DriverController extends ControllerParent {

    public DriverController(int port) {
        super(port, true, ControllerType.Driver);
        // disregard null safety for subsystems as it is checked on assignment

        /// FACE BUTTONS
        // this.A.binding = 

        // this.B.binding =

        // this.X.binding =

        // this.Y.binding =

        /// BUMPER
        // this.LB.binding =

        // this.RB.binding =

        /// CENTER BUTTONS
        // this.Back.binding =

        this.Start.binding = new SingleDepBinding(Subsystems.Swerve, (trig, allss) -> {
            trig.onTrue(Commands.runOnce(() -> allss.swerve.get().setYaw(0.0)));
        });

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
