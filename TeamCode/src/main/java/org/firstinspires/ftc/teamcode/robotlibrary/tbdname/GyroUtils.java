package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

import java.util.Locale;

/**
 * Created by Dynamic Signals on 10/11/2016.
 */

public class GyroUtils {

    private DriveTrain driveTrain;
    private Telemetry telemetry;
    public BNO055IMU imu;
    private BNO055IMU.Parameters parameters;
    private boolean initialized = false;

    public Orientation angles;

    public GyroUtils(StateMachineOpMode opMode) {
        //driveTrain = new DriveTrain(opMode.hardwareMap);
        telemetry = opMode.telemetry;
        HardwareMap hardwareMap = opMode.hardwareMap;

        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }

    public void startGyro() {
        imu.initialize(parameters);
    }

    private void refreshData() {
        if (!initialized) {
            imu.initialize(parameters);
            initialized = true;
        }
        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    public double getHeading() {
        refreshData();
        return Double.valueOf(AutonomousUtils.df.format(-angles.firstAngle));
    }

    public double getRoll() {
        refreshData();
        return Double.valueOf(AutonomousUtils.df.format(-angles.secondAngle));
    }

    public double getPitch() {
        refreshData();
        return Double.valueOf(AutonomousUtils.df.format(-angles.thirdAngle));
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    // -180 to 180
    public static double temporaryZero(GyroUtils gyroUtils, double zeroDegree) {
        double formattedDegree = gyroUtils.getHeading();
        RobotLog.d("Degree: " + String.valueOf(formattedDegree));
        if (Math.signum(formattedDegree) == -1) formattedDegree += 360;
        double degree = formattedDegree - zeroDegree;
        if (degree > 360) {
            degree = degree - 360;
        }
        if (degree < 0) {
            degree = degree + 360;
        }
        return degree;
    }

    public static class GyroDetail {

        public double degreesOff;
        public Direction turnDirection;
        public double movedZero;
        public double targetDegree = 0;
        public double percentComplete = 0; // 0 - 100
        public Direction initialTurnDirection = null;
        private double initialDegreesOff = -1;

        private GyroUtils gyroUtils;

        public GyroDetail(GyroUtils gyroUtils, double degree) {
            this.targetDegree = degree;
            this.gyroUtils = gyroUtils;

            updateData();
        }

        public GyroDetail(GyroUtils gyroUtils) {
            this.gyroUtils = gyroUtils;
        }

        public void updateData() {
            if (initialTurnDirection == null) {
                initialTurnDirection = turnDirection;
            }
            movedZero = GyroUtils.temporaryZero(gyroUtils, Range.clip(targetDegree, -180, 180)); // This is basically spoofedZero, it is used to cross over -180 & 180

            if (movedZero > 0 && movedZero < 180) { // We need to turn counterclockwise
                degreesOff = movedZero; // Its just the straight degrees
                turnDirection = Direction.COUNTERCLOCKWISE;
            }

            if (movedZero >= 180 && movedZero < 360) { // We need to turn clockwise
                degreesOff = Math.abs(90 - (movedZero - 270)); // We need to find abs of 90 - (degree - 270) to get the degrees off
                turnDirection = Direction.CLOCKWISE;
            }

            if (initialDegreesOff == -1) {
                initialDegreesOff = degreesOff;
                RobotLog.d("Initial degrees off: " + initialDegreesOff);
            }

            percentComplete = Double.valueOf(AutonomousUtils.df.format(100 - Range.clip((degreesOff / initialDegreesOff) * 100, 0, 100)));
        }

        public void setData(double degree) {
            this.targetDegree = degree;
            updateData();
        }

        public boolean isInTolerance(double tolerance) {
            return degreesOff < tolerance;
        }
    }

    public enum Direction {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
}
