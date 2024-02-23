package com.igknighters.subsystems.stem;

import com.igknighters.constants.ConstValues.kStem.kTelescope;

import edu.wpi.first.math.util.Units;

public class StemPosition {
    public double pivotRads, wristRads, telescopeMeters;

    private StemPosition(double pivotPosRads, double wristPosRads, double telescopePosMeters) {
        this.pivotRads = pivotPosRads;
        this.wristRads = wristPosRads;
        this.telescopeMeters = telescopePosMeters;
    }

    public static StemPosition fromDegrees(double pivotPosDeg, double wristPosDeg, double telescopePosMeters) {
        return new StemPosition(Math.toRadians(pivotPosDeg), Math.toRadians(wristPosDeg), telescopePosMeters);
    }

    public static StemPosition fromRadians(double pivotPosRads, double wristPosRads, double telescopePosMeters) {
        return new StemPosition(pivotPosRads, wristPosRads, telescopePosMeters);
    }

    public static StemPosition fromRotations(double pivotPosRot, double wristPosRot, double telescopePosMeters) {
        return new StemPosition(Math.toRadians(pivotPosRot * 360.0), Math.toRadians(wristPosRot * 360.0),
                telescopePosMeters);
    }

    public double getPivotRads() {
        return pivotRads;
    }

    public double getWristRads() {
        return wristRads;
    }

    public double getTelescopeMeters() {
        return telescopeMeters;
    }

    public boolean isValid() {
        return StemValidator.validatePosition(this).isValid();
    }

    public boolean isStow() {
        return false;
    }

    @Override
    public String toString() {
        return "StemPosition(" + Units.radiansToDegrees(pivotRads) + ", " + Units.radiansToDegrees(wristRads) + ", "
                + telescopeMeters + ")";
    }

    public static StemPosition STOW = new StemPosition(
            Units.degreesToRadians(41.0),
            Units.degreesToRadians(108.0),
            kTelescope.MIN_METERS) {
        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public String toString() {
            return "Stow";
        }

        @Override
        public boolean isStow() {
            return true;
        }
    };

    public static StemPosition INTAKE = new StemPosition(
            Units.degreesToRadians(11.0),
            kTelescope.MIN_METERS + Units.inchesToMeters(4.7),
            Units.degreesToRadians(72.0)) {

        @Override
        public String toString() {
            return "Intake";
        }
    };

    public static StemPosition AMP = new StemPosition(
            Units.degreesToRadians(94.0),
            Units.degreesToRadians(58.0),
            kTelescope.MIN_METERS + Units.inchesToMeters(5.5)) {

        @Override
        public String toString() {
            return "Amp";
        }

    };
}