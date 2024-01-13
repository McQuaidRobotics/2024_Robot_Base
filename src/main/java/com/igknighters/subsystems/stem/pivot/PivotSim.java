package com.igknighters.subsystems.stem.pivot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;

import org.littletonrobotics.junction.Logger;

import com.igknighters.constants.ConstValues.kStem.kPivot;
import com.igknighters.subsystems.stem.pivot.Pivot.PivotInputs;
import com.igknighters.subsystems.stem.wrist.Wrist.WristInputs;

public class PivotSim implements Pivot {
    private final PivotInputs inputs;
    private final Double maxVelo = 112.0 * 360.0 * kPivot.MOTOR_TO_MECHANISM_RATIO;
    private final SingleJointedArmSim sim;
        private final PIDController pidController = new PIDController(
        kPivot.MOTOR_kP, kPivot.MOTOR_kI, kPivot.MOTOR_kD, 0.2
    );
    private Double setRadians = Units.degreesToRadians(55.0), AppliedVolts = 0.0;
    
    
    public PivotSim(Double startingRadians) {
        sim = new SingleJointedArmSim(
            DCMotor.getFalcon500(2),
            1.0 / kPivot.MOTOR_TO_MECHANISM_RATIO, 
            0.07, //TODO: get real values
            0.3,
            kPivot.PIVOT_MIN_RADIANS,
            kPivot.PIVOT_MAX_RADIANS,
            false,
            kPivot.PIVOT_MIN_RADIANS
        );
        sim.setState(startingRadians, 0);
        inputs = new PivotInputs(startingRadians);
    }

    @Override
    public boolean setPivotRadians(Double radians) {
        setRadians = radians;
        Double pivotVoltageFeedback = pidController.calculate(
            sim.getAngleRads(), radians);
        sim.setInputVoltage(pivotVoltageFeedback);
        AppliedVolts = pivotVoltageFeedback;
        return Math.abs(radians - getPivotRadians()) < kPivot.TOLERANCE;
    }

    @Override
    public Double getPivotRadians() {
        return inputs.radians;
    }

    @Override
    public void setVoltageOut(double volts) {
        sim.setInputVoltage(volts);
    }

    @Override
    public void periodic() {
        
        if (DriverStation.isDisabled()) {
            sim.setInputVoltage(0);
            AppliedVolts = 0.0;
        }

        sim.update(0.2);

        inputs.radians = Units.radiansToDegrees(sim.getAngleRads());
        inputs.radiansPerSecond = Units.radiansToDegrees(sim.getVelocityRadPerSec());
        inputs.targetRadians = setRadians; //no idea 
        inputs.volts = AppliedVolts;
        inputs.leftAmps = sim.getCurrentDrawAmps()/2;
        inputs.rightAmps = sim.getCurrentDrawAmps()/2;
        inputs.leftTemp = 0.0;
        inputs.rightTemp = 0.0;
        inputs.isLimitSwitchHit = false; //DIO or NT boolean

        Logger.processInputs("SuperStructure/Pivot", inputs);

       
    }

}
