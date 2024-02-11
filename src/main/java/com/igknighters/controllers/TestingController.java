package com.igknighters.controllers;

import com.igknighters.constants.ConstValues;
import com.igknighters.subsystems.stem.StemPosition;

import edu.wpi.first.wpilibj2.command.Commands;

import com.igknighters.SubsystemResources.Subsystems;

/** If debug is false this controller does not initialize */
public class TestingController extends ControllerParent {
    public TestingController(int port) {
        super(port, ConstValues.DEBUG, ControllerType.Testing);

        // disregard null safety as it is checked on assignment

        /// FACE BUTTONS
        this.A.binding = new Binding((trig, allss) -> {
            trig.onTrue(Commands.runOnce(() -> {
                allss.stem.get().setStemPosition(StemPosition.fromDegrees(80.0, 0.0, 0.0));
            }));
        }, Subsystems.Stem);

        this.B.binding = new Binding((trig, allss) -> {
            trig.onTrue(Commands.runOnce(() -> {
                allss.stem.get().setStemPosition(StemPosition.fromDegrees(20.0, 0.0, 0.0));
            }));
        }, Subsystems.Stem);

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
