package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class DriveTrain {

    public DcMotor LeftFrontMotor, RightFrontMotor, LeftBackMotor, RightBackMotor;
    public double LeftPower = 0, RightPower = 0;
    boolean spoofMotors = false;

    public DriveTrain(HardwareMap hardwareMap) {
        if (!spoofMotors) {
            LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
            RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
            LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
            RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");
        }

    }

    public DriveTrain(OpMode opMode) {
        this(opMode.hardwareMap);
    }


    public void powerLeft(double power) {
        if (!spoofMotors) {
            double clippedPower = Range.clip(power, -1, 1);
            LeftFrontMotor.setPower(clippedPower);
            LeftBackMotor.setPower(clippedPower);
        }
        LeftPower = power;
    }

    public void powerRight(double power) {
        if (!spoofMotors) {
            double clippedPower = Range.clip(power, -1, 1);
            RightFrontMotor.setPower(clippedPower);
            RightBackMotor.setPower(clippedPower);
        }
        RightPower = power;
    }

    public void stopRobot() {
        powerLeft(0);
        powerRight(0);
    }

    public void setMode(DcMotor.RunMode mode) {
        if (!spoofMotors) {
            LeftFrontMotor.setMode(mode);
            LeftBackMotor.setMode(mode);
            RightFrontMotor.setMode(mode);
            RightBackMotor.setMode(mode);
        }

    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        if (!spoofMotors) {
            LeftFrontMotor.setZeroPowerBehavior(behavior);
            LeftBackMotor.setZeroPowerBehavior(behavior);
            RightFrontMotor.setZeroPowerBehavior(behavior);
            RightBackMotor.setZeroPowerBehavior(behavior);
        }
    }

}


