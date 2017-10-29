package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.hardware.lynx.LynxDcMotorController;
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
    public LynxDcMotorController controller;
    public double LeftPower = 0, RightPower = 0;
    boolean spoofMotors = false;

    public DriveTrain(HardwareMap hardwareMap) {
        if (!spoofMotors) {
            LeftFrontMotor = hardwareMap.dcMotor.get("LeftFrontMotor");
            RightFrontMotor = hardwareMap.dcMotor.get("RightFrontMotor");
            LeftBackMotor = hardwareMap.dcMotor.get("LeftBackMotor");
            RightBackMotor = hardwareMap.dcMotor.get("RightBackMotor");
            LeftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            LeftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            RightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            RightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            controller = (LynxDcMotorController) LeftFrontMotor.getController();
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

    public boolean isBusy() {
        boolean[] isBusyArray = new boolean[4];
        isBusyArray[0] = LeftFrontMotor.isBusy();
        isBusyArray[1] = RightFrontMotor.isBusy();
        isBusyArray[2] = LeftBackMotor.isBusy();
        isBusyArray[3] = RightBackMotor.isBusy();
        boolean endResult = false;
        for (boolean busy : isBusyArray) {
            if (busy) endResult = true;
        }
        return endResult;
    }

    public void reset() {
        controller.resetDeviceConfigurationForOpMode();
    }

    public void setTargetPosition(int position) {
        LeftFrontMotor.setTargetPosition(position);
        RightFrontMotor.setTargetPosition(position);
        LeftBackMotor.setTargetPosition(position);
        RightBackMotor.setTargetPosition(position);
    }

}


