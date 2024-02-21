package com.igknighters.controllers;

import com.igknighters.constants.ConstValues;
import com.igknighters.subsystems.stem.StemPosition;

import edu.wpi.first.wpilibj2.command.Commands;

import com.igknighters.SubsystemResources.Subsystems;
import com.igknighters.commands.stem.StemCommands;
import com.igknighters.commands.umbrella.UmbrellaCommands;

@SuppressWarnings("unused")

/** If debug is false this controller does not initialize */
public class TestingController extends ControllerParent {
    public TestingController(int port) {
        super(port, ConstValues.DEBUG, ControllerType.Testing);

        // disregard null safety as it is checked on assignment

        /// FACE BUTTONS
        this.A.binding = new SingleDepBinding(Subsystems.Stem, (trig, allss) -> {
            trig.onTrue(
                StemCommands.holdAt(
                    allss.stem.get(), StemPosition.fromDegrees(
                        70.0,
                        80.0,
                        0.53
                    ))
            );
        });

        this.B.binding = new SingleDepBinding(Subsystems.Stem, (trig, allss) -> {
            trig.onTrue(Commands.runOnce(() -> {
                allss.stem.get().setStemPosition(StemPosition.fromDegrees(20.0, 0.0, 0.0));
            }));
        });

        this.X.binding = new SingleDepBinding(Subsystems.Umbrella, (trig, allss) -> {
            trig.onTrue(UmbrellaCommands.stopShooter(allss.umbrella.get()));
        });

        this.Y.binding = this.X.binding = new SingleDepBinding(Subsystems.Umbrella, (trig, allss) -> {
            trig.onTrue(UmbrellaCommands.waitUntilSpunUp(allss.umbrella.get(), 3800));
        });

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
