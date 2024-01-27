package com.igknighters.subsystems.umbrella.intake;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
import com.ctre.phoenix6.signals.ReverseLimitValue;
import com.igknighters.constants.ConstValues.kUmbrella.kIntake;

import edu.wpi.first.math.util.Units;

public class IntakeReal implements Intake {

    private final TalonFX leaderMotor = new TalonFX(kIntake.UPPER_MOTOR_ID);
    private final TalonFX followerMotor = new TalonFX(kIntake.LOWER_MOTOR_ID);
    private final StatusSignal<Double> veloSignal, voltSignal, currentSignal, tempSignal;
    private final StatusSignal<ReverseLimitValue> revLimitSignal;
    private final StatusSignal<ForwardLimitValue> fwdLimitSignal;
    private final IntakeInputs inputs = new IntakeInputs();

    public IntakeReal() {
        leaderMotor.getConfigurator().apply(new TalonFXConfiguration());
        followerMotor.getConfigurator().apply(new TalonFXConfiguration());

        veloSignal = leaderMotor.getVelocity();
        voltSignal = leaderMotor.getMotorVoltage();
        currentSignal = leaderMotor.getTorqueCurrent();
        tempSignal = leaderMotor.getDeviceTemp();
        revLimitSignal = leaderMotor.getReverseLimit();
        fwdLimitSignal = leaderMotor.getForwardLimit();

        veloSignal.setUpdateFrequency(100);
        voltSignal.setUpdateFrequency(100);
        currentSignal.setUpdateFrequency(100);
        tempSignal.setUpdateFrequency(100);
        revLimitSignal.setUpdateFrequency(100);
        fwdLimitSignal.setUpdateFrequency(100);

        leaderMotor.optimizeBusUtilization();

        followerMotor.optimizeBusUtilization();

        // followerMotor.setControl(new Follower(kIntake.UPPER_MOTOR_ID, false));
    }

    @Override
    public void setVoltageOut(double volts) {
        inputs.volts = volts;
        leaderMotor.setVoltage(volts);
        followerMotor.setVoltage(volts * 0.66);
    }

    @Override
    public void turnIntakeRads(double radians) {
        setVoltageOut(0.0);
        var ret = new PositionDutyCycle(
                leaderMotor.getRotorPosition().getValue() + Units.radiansToRotations(radians));
        leaderMotor.setControl(ret);
    }

    @Override
    public boolean isEntranceBeamBroken() {
        return inputs.entranceBeamBroken;
    }

    @Override
    public boolean isExitBeamBroken() {
        return inputs.exitBeamBroken;
    }

    @Override
    public void periodic() {
        BaseStatusSignal.refreshAll(
                veloSignal, voltSignal,
                currentSignal, tempSignal);

        inputs.entranceBeamBroken = revLimitSignal.getValue().equals(ReverseLimitValue.ClosedToGround);
        inputs.exitBeamBroken = fwdLimitSignal.getValue().equals(ForwardLimitValue.ClosedToGround);
        inputs.radiansPerSecond = Units.rotationsToRadians(veloSignal.getValue());
        inputs.volts = voltSignal.getValue();
        inputs.amps = currentSignal.getValue();
        inputs.temp = tempSignal.getValue();

        Logger.processInputs("/Umbrella/Intake", inputs);
    }
}
