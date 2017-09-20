package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

/**
 * Created by Dynamic Signals on 10/11/2016.
 */

public class GyroUtils {

    public GyroSensor gyro;
    public ModernRoboticsI2cGyro i2cGyro;
    public double dividerNumber = 17;
    Telemetry telemetry;
    private int TOLERANCE = 1;
    private DriveTrain driveTrain;
    private int gyroDistance = 0;

    @Deprecated
    public GyroUtils(HardwareMap hardwareMap, DriveTrain driveTrain, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.driveTrain = driveTrain;
        gyro = hardwareMap.gyroSensor.get("gyro");
        i2cGyro = (ModernRoboticsI2cGyro) gyro;
    }

    /*
     * Borrowed from team 5942
     * @See https://www.reddit.com/r/FTC/comments/44zhfu/help_programming_a_turning_method_using_the_gyro/czuc6uf/
     */
    public int gyroDelta() {
        return gyroDistance - i2cGyro.getIntegratedZValue();
    }

    public void resetDelta() {
        gyroDistance = i2cGyro.getIntegratedZValue();
    }

    public void gTurn(int degrees, double power) {
        float direction = Math.signum(degrees); //get +/- sign of target

        if (Math.abs(gyroDelta()) < Math.abs(degrees)) {
            driveTrain.powerLeft(-direction * power);
            driveTrain.powerRight(direction * power);
        } else {
            driveTrain.stopRobot();
        }
    }

    public void setTolerance(int tolerance) {
        this.TOLERANCE = tolerance;
    }

    public boolean isGyroInTolerance(int degree, int tolerance) {
        boolean returnValue = false;
        if ((gyro.getHeading() <= degree + tolerance) && (gyro.getHeading() >= degree - tolerance)) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean isGyroInTolerance(int degree) {
        return isGyroInTolerance(degree, TOLERANCE);
    }

    public int spoofedZero(int zeroDegree) {
        int ActualDegree = gyro.getHeading();
        int degree = ActualDegree - zeroDegree;
        if (degree > 360) {
            degree = degree - 360;
        }
        if (degree < 0) {
            degree = degree + 360;
        }
        return degree;
    }

    // -180 to 180
    public static double temporaryZero(AHRS navx, double zeroDegree) {
        double formattedDegree = navx.getYaw();
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

        private AHRS navx;

        /**
         * Constructor for GyroDetail which gives useful data about about getting to a certain degree for gyro.
         * This was made for the navX mxp for micro
         * @param navx navX device
         * @param degree the degree you want to get data about (-180 - 180)
         */
        public GyroDetail(AHRS navx, double degree) {
            this.targetDegree = degree;
            this.navx = navx;

            updateData();
        }

        public GyroDetail(AHRS navx) {
            this.navx = navx;
        }

        public void updateData() {
            if (initialTurnDirection == null) {
                initialTurnDirection = turnDirection;
            }
            movedZero = GyroUtils.temporaryZero(navx, Range.clip(targetDegree, -180, 180)); // This is basically spoofedZero, it is used to cross over -180 & 180

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
    }

    public void rotateUsingSpoofed(int ZeroDegree, int TargetDegree, double DivisionNumber, Direction direction) {
        int CurrentSpoofedDegree = spoofedZero(ZeroDegree); //An expected 39 gyro value from fake zero
        if (!isGyroInTolerance(TargetDegree)) {
            double DegreesOff = Math.abs(TargetDegree - CurrentSpoofedDegree);
            double RawPower = Range.clip(DegreesOff / DivisionNumber, 0, 1);
            switch (direction) {
                case CLOCKWISE:
                    driveTrain.powerLeft(RawPower);
                    driveTrain.powerRight(-RawPower);
                    break;
                case COUNTERCLOCKWISE:
                    driveTrain.powerLeft(-RawPower);
                    driveTrain.powerRight(RawPower);
                    break;
            }

        }
    }

    //This function turns a number of degrees compared to where the robot is. Positive numbers trn left.
    public void turn(int target) throws InterruptedException {
        turnAbsolute(target + i2cGyro.getIntegratedZValue());
    }

    //This function turns a number of degrees compared to where the robot was when the program started. Positive numbers trn left.
    public void turnAbsolute(int target) {
        int zAccumulated = i2cGyro.getIntegratedZValue();  //Set variables to gyro readings
        double turnSpeed;

        if (Math.abs(zAccumulated - target) > 15) {
            turnSpeed = 0.4;
        } else {
            turnSpeed = 0.23;
        }

        if (Math.abs(zAccumulated - target) > 3) {  //Continue while the robot direction is further than three degrees from the target
            if (zAccumulated > target) {  //if gyro is positive, we will turn right
                driveTrain.powerLeft(turnSpeed);
                driveTrain.powerRight(-turnSpeed);
            }

            if (zAccumulated < target) {  //if gyro is positive, we will turn left
                driveTrain.powerLeft(-turnSpeed);
                driveTrain.powerRight(turnSpeed);
            }

            zAccumulated = i2cGyro.getIntegratedZValue();  //Set variables to gyro readings
            telemetry.addData("Accumulated", String.format("%03d", zAccumulated));
        } else {
            driveTrain.stopRobot();
        }
    }

    public void rotateUsingGyro(int DesiredDegree, int DivisionNumber, Direction direction) {
        int CurrentSpoofedDegree = spoofedZero(DesiredDegree); //An expected 39 gyro value from fake zero
        if (!isGyroInTolerance(0)) {
            double DegreesOff = Math.abs(0 - CurrentSpoofedDegree);
            double ProportionalPower = Range.clip(DegreesOff / DivisionNumber, 0, 1);
            switch (direction) {
                case CLOCKWISE:
                    driveTrain.powerLeft(ProportionalPower);
                    driveTrain.powerRight(-ProportionalPower);
                    break;
                case COUNTERCLOCKWISE:
                    driveTrain.powerLeft(-ProportionalPower);
                    driveTrain.powerRight(ProportionalPower);
                    break;
            }

        } else {

        }
    }

    public enum Direction {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
}