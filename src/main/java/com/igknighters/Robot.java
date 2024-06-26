package com.igknighters;

import java.util.HashMap;
import java.util.function.BiConsumer;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import com.igknighters.constants.ConstValues;
import com.igknighters.util.ShuffleboardApi;
import com.igknighters.util.Tracer;
import com.igknighters.util.UnitTestableRobot;
import com.igknighters.util.pathfinders.LocalADStarAK;
import com.pathplanner.lib.pathfinding.Pathfinding;

public class Robot extends UnitTestableRobot {

    private Command autoCmd;
    private final CommandScheduler scheduler = CommandScheduler.getInstance();

    @Override
    public void robotInit() {
        Pathfinding.setPathfinder(new LocalADStarAK());
        setupAkit();

        com.igknighters.ConstantHelper.applyRoboConst(ConstValues.class);

        GlobalState.publishField();

        new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        Tracer.traceFunc("Shuffleboard", ShuffleboardApi::run);
        Tracer.traceFunc("CommandScheduler", () -> scheduler.run());
        Tracer.traceFunc("LEDUpdate", () -> LED.getInstance().run());
        GlobalState.log();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
        autoCmd = GlobalState.getAutoCommand();
        Logger.recordOutput("SelectedAutoCommand", autoCmd.getName());
    }

    @Override
    public void autonomousInit() {
        if (GlobalState.isUnitTest()) {
            autoCmd = GlobalState.getAutoCommand();
        }
        if (autoCmd != null) {
            Logger.recordOutput("CurrentAutoCommand", autoCmd.getName());
            scheduler.schedule(autoCmd);
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void autonomousExit() {
        if (autoCmd != null) {
            Logger.recordOutput("CurrentAutoCommand", "");
            autoCmd.cancel();
        }
    }

    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
    }

    @Override
    public void driverStationConnected() {
    }

    private void setupAkit() {
        if (GlobalState.isUnitTest()) {
            return;
        }

        Logger.recordMetadata("RuntimeType", getRuntimeType().toString());
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        switch (BuildConstants.DIRTY) {
            case 0:
                Logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                Logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                Logger.recordMetadata("GitDirty", "Unknown");
                break;
        }

        if (Robot.isReal()) {
            Logger.addDataReceiver(new WPILOGWriter("/media/sda1/robotlogs/"));
        }
        Logger.addDataReceiver(new NT4Publisher());
        Logger.start();

        HashMap<String, Integer> commandCounts = new HashMap<>();
        BiConsumer<Command, Boolean> logCommandFunction = (Command command, Boolean active) -> {
            String name = command.getName();
            int count = commandCounts.getOrDefault(name, 0) + (active ? 1 : -1);
            commandCounts.put(name, count);
            Logger.recordOutput(
                    "CommandsUnique/" + name + "_" + Integer.toHexString(command.hashCode()), active.booleanValue());
            Logger.recordOutput("CommandsAll/" + name, count > 0);
        };
        CommandScheduler.getInstance()
                .onCommandInitialize(
                        (Command command) -> {
                            logCommandFunction.accept(command, true);
                        });
        CommandScheduler.getInstance()
                .onCommandFinish(
                        (Command command) -> {
                            logCommandFunction.accept(command, false);
                        });
        CommandScheduler.getInstance()
                .onCommandInterrupt(
                        (Command command) -> {
                            logCommandFunction.accept(command, false);
                        });
    }
}
