package com.igknighters.subsystems.stem;

import java.nio.ByteBuffer;

import com.igknighters.constants.ConstValues.kStem.kTelescope;
import com.igknighters.constants.ConstValues.kStem.kWrist;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.struct.Struct;
import edu.wpi.first.util.struct.StructSerializable;

public class StemPosition implements StructSerializable {
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

    public static class StemPositionStruct implements Struct<StemPosition> {

        @Override
        public Class<StemPosition> getTypeClass() {
            return StemPosition.class;
        }

        @Override
        public String getTypeString() {
            return "struct:StemPosition";
        }

        @Override
        public String getSchema() {
            return "double pivotRads; double wristRads; double telescopeMeters;";
        }

        @Override
        public int getSize() {
            return kSizeDouble * 3;
        }

        @Override
        public void pack(ByteBuffer bb, StemPosition value) {
            bb.putDouble(value.pivotRads);
            bb.putDouble(value.wristRads);
            bb.putDouble(value.telescopeMeters);
        }

        @Override
        public StemPosition unpack(ByteBuffer bb) {
            return new StemPosition(bb.getDouble(), bb.getDouble(), bb.getDouble());
        }
    }

    public static final StemPositionStruct struct = new StemPositionStruct();

    public static final StemPosition STOW = new StemPosition(Units.degreesToRadians(49.5),
            Units.degreesToRadians(104.0), kTelescope.MIN_METERS) {
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

    public static final StemPosition INTAKE = new StemPosition(Units.degreesToRadians(10.8),
            kWrist.FROZEN_WRIST_ANGLE, kTelescope.MIN_METERS + Units.inchesToMeters(4.7)) {

        @Override
        public String toString() {
            return "Intake";
        }
    };

    public static final StemPosition AMP_SAFE = new StemPosition(
            Units.degreesToRadians(90.0),
            Units.degreesToRadians(43.0),
            kTelescope.MIN_METERS + Units.inchesToMeters(2.0)) {

        @Override
        public String toString() {
            return "AmpSafe";
        }

    };

    public static final StemPosition AMP_SCORE = new StemPosition(
            Units.degreesToRadians(88.0),
            Units.degreesToRadians(43.0),
            kTelescope.MIN_METERS + Units.inchesToMeters(7.0)) {

        @Override
        public String toString() {
            return "AmpScore";
        }

    };

    public static final StemPosition CLIMB = new StemPosition(1.245, 0.783, 0.626) {

        @Override
        public String toString() {
            return "Climb";
        }
    };

    public static final StemPosition STARTING = new StemPosition(
            1.114, // 63.8
            1.93, // 110.58
            kTelescope.MIN_METERS) {

        @Override
        public String toString() {
            return "Starting";
        }
    };

    public static final StemPosition SUBWOOFER = new StemPosition(
            0.3750953350652253 + Units.degreesToRadians(0.5),
            kWrist.FROZEN_WRIST_ANGLE,
            kTelescope.MIN_METERS + 0.05) {

        @Override
        public String toString() {
            return "Subwoofer";
        }
    };
}