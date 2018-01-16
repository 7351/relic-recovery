package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeV2 {

    public CRServo LeftInOut1, LeftInOut2, RightInOut1, RightInOut2, LeftUpDown1, RightUpDown1, LeftUpDown2, RightUpDown2;
    boolean eightMotorMode = true;

    public IntakeV2(StateMachineOpMode opMode, boolean eightMotorMode) {
        this.eightMotorMode = eightMotorMode;
        LeftInOut1 = opMode.hardwareMap.crservo.get("LeftInOut1");
        LeftInOut1.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftInOut2 = opMode.hardwareMap.crservo.get("LeftInOut2");
        LeftInOut2.setDirection(DcMotorSimple.Direction.REVERSE);
        RightInOut1 = opMode.hardwareMap.crservo.get("RightInOut1");
        RightInOut2 = opMode.hardwareMap.crservo.get("RightInOut2");
        LeftUpDown1 = opMode.hardwareMap.crservo.get("LeftUpDown1");
        LeftUpDown1.setDirection(DcMotorSimple.Direction.REVERSE);
        RightUpDown1 = opMode.hardwareMap.crservo.get("RightUpDown1");
        if (eightMotorMode) {
            LeftUpDown2 = opMode.hardwareMap.crservo.get("LeftUpDown2");
            LeftUpDown2.setDirection(DcMotorSimple.Direction.REVERSE);
            RightUpDown2 = opMode.hardwareMap.crservo.get("RightUpDown2");
        }

    }

    public enum IntakeMode {
        IN,
        OUT,
        REST,
        UP,
        DOWN
    }

    double[] restingPowers = {0, 0, 0, 0, 0, 0, 0, 0};

    public void setIntakeMode(IntakeMode intakeMode) {
        switch (intakeMode) {
            case IN:
                LeftInOut1.setPower(1);
                RightInOut1.setPower(1);
                LeftInOut2.setPower(1);
                RightInOut2.setPower(1);
                break;
            case OUT:
                LeftInOut1.setPower(-1);
                RightInOut1.setPower(-1);
                LeftInOut2.setPower(-1);
                RightInOut2.setPower(-1);
                break;
            case REST:
                LeftInOut1.setPower(restingPowers[0]);
                RightInOut1.setPower(restingPowers[1]);
                LeftInOut2.setPower(restingPowers[2]);
                RightInOut2.setPower(restingPowers[3]);
                LeftUpDown1.setPower(restingPowers[4]);
                RightUpDown1.setPower(restingPowers[5]);
                if (eightMotorMode) {
                    LeftUpDown2.setPower(restingPowers[6]);
                    RightUpDown2.setPower(restingPowers[7]);
                }
                break;
            case UP:
                LeftUpDown1.setPower(1);
                RightUpDown1.setPower(1);
                if (eightMotorMode) {
                    LeftUpDown2.setPower(1);
                    RightUpDown2.setPower(1);
                }
                break;
            case DOWN:
                LeftUpDown1.setPower(-1);
                RightUpDown1.setPower(-1);
                if (eightMotorMode) {
                    LeftUpDown2.setPower(-1);
                    RightUpDown2.setPower(-1);
                }
                break;
        }
    }

}
