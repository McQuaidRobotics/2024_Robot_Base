package com.igknighters.subsystems.stem.telescope;

import com.igknighters.constants.ConstValues.kStem.kTelescope;

import edu.wpi.first.math.MathUtil;

public class TelescopeDisabled implements Telescope {
    double targetMeters = kTelescope.MIN_METERS;
    final double slewRate = 0.5 / 50.0;

    @Override
    public double getTelescopeMeters() {
        return targetMeters;
    }

    @Override
    public void setTelescopeMeters(double meters) {
        var clampedTarget = MathUtil.clamp(meters, kTelescope.MIN_METERS, kTelescope.MAX_METERS);
        targetMeters = targetMeters + MathUtil.clamp(clampedTarget - targetMeters, -slewRate, slewRate);
    }

    @Override
    public void setVoltageOut(double volts) {}

    @Override
    public boolean isFwdLimitSwitchHit() {
        return targetMeters >= kTelescope.MAX_METERS * 0.98;
    }

    @Override
    public boolean isRevLimitSwitchHit() {
        return targetMeters <= kTelescope.MIN_METERS * 0.98;
    }

    @Override
    public boolean hasHomed() {
        return true;
    }
}
