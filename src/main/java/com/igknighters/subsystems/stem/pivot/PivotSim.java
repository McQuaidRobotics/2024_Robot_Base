package com.igknighters.subsystems.stem.pivot;

import edu.wpi.first.hal.SimBoolean;
import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;

import org.littletonrobotics.junction.Logger;

import com.igknighters.GlobalState;
import com.igknighters.constants.ConstValues.kStem.kPivot;
import com.igknighters.util.BootupLogger;

public class PivotSim implements Pivot {
    private final PivotInputs inputs;
    private final SingleJointedArmSim sim;
    private final PIDController pidController = new PIDController(
            kPivot.MOTOR_kP, kPivot.MOTOR_kI, kPivot.MOTOR_kD, 0.2);
    private final SimBoolean limitSwitch;

    public PivotSim() {
        sim = new SingleJointedArmSim(
                DCMotor.getFalcon500(2),
                kPivot.MOTOR_TO_MECHANISM_RATIO,
                0.07, // TODO: get real values
                0.55,
                kPivot.PIVOT_MIN_RADIANS,
                kPivot.PIVOT_MAX_RADIANS,
                false,
                kPivot.PIVOT_MIN_RADIANS);
        sim.setState(0.0, 0);
        inputs = new PivotInputs(0.0);

        if (RobotBase.isReal()) {
            // In the event we use this to "disable" the pivot on the real robot,
            // we don't want to crash the robot by trying to create a SimDevice
            limitSwitch = null;
            NetworkTableInstance.getDefault()
                    .getTable("SimDevices")
                    .getSubTable("PivotLimitSwitch")
                    .getEntry("tripped")
                    .setBoolean(false);
        } else if (GlobalState.isUnitTest()) {
            // HAL requires unique allocations for each SimDevice,
            // in unit tests we don't care what this is actually called so just make it
            // random
            limitSwitch = SimDevice.create("" + Math.random() + Math.random()).createBoolean("", Direction.kInput,
                    false);
        } else {
            limitSwitch = SimDevice.create("PivotLimitSwitch").createBoolean("tripped", Direction.kInput, false);
        }

        BootupLogger.bootupLog("    Pivot initialized (sim)");
    }

    @Override
    public void setPivotRadians(double radians) {
        inputs.targetRadians = radians;
        double pivotVoltageFeedback = pidController.calculate(
                inputs.radians, radians);
        sim.setInputVoltage(pivotVoltageFeedback);
        inputs.volts = pivotVoltageFeedback;
    }

    @Override
    public double getPivotRadians() {
        return inputs.radians;
    }

    @Override
    public void setVoltageOut(double volts) {
        sim.setInputVoltage(volts);
        inputs.volts = volts;
        inputs.targetRadians = 0;
    }

    @Override
    public void periodic() {
        if (DriverStation.isDisabled()) {
            sim.setInputVoltage(0);
        }

        sim.update(0.2);

        inputs.radians = Units.radiansToDegrees(sim.getAngleRads());
        inputs.radiansPerSecond = Units.radiansToDegrees(sim.getVelocityRadPerSec());
        inputs.leftAmps = sim.getCurrentDrawAmps() / 2.0;
        inputs.rightAmps = sim.getCurrentDrawAmps() / 2.0;
        inputs.leftTemp = 0.0;
        inputs.rightTemp = 0.0;
        if (RobotBase.isReal()) {
            inputs.isLimitSwitchHit = NetworkTableInstance.getDefault()
                    .getTable("SimDevices")
                    .getSubTable("PivotLimitSwitch")
                    .getEntry("tripped")
                    .getBoolean(false);
        } else {
            inputs.isLimitSwitchHit = limitSwitch.get();
        }

        Logger.processInputs("Stem/Pivot", inputs);
    }

}