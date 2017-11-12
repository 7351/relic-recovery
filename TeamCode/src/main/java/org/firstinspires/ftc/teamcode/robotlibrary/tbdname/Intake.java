package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Leo on 11/11/2017.
 */

public class Intake {

    StateMachineOpMode opMode;
    Servo LeftPositionServo, RightPostiionServo;
    CRServo LeftCRServo, RightCRServo;
    double[] stopPower = {-0.09, 0.04};

    public Intake(StateMachineOpMode opMode) {
        this.opMode = opMode;

        LeftPositionServo = opMode.hardwareMap.servo.get("LeftIntakePositionServo");
        RightPostiionServo = opMode.hardwareMap.servo.get("RightIntakePositionServo");
        LeftCRServo = opMode.hardwareMap.crservo.get("LeftIntakeCRServo");
        RightCRServo = opMode.hardwareMap.crservo.get("RightIntakeCRServo");
        RightCRServo.setDirection(DcMotorSimple.Direction.REVERSE);

        setPosition(ServoPosition.IN);
    }

    public enum ServoPosition {
        IN(0.015, 1),
        OUT(0.93, 0.089);

        private double[] position; // Array containing data

        ServoPosition(double positionLeft, double positionRight) { // Constructor
            this.position = new double[]{positionLeft, positionRight}; // Create array
        }

        public double[] getPosition() { // Return data
            return position;
        }

    }

    public void setPower(boolean on) {
        if (on) {
            LeftCRServo.setPower(1);
            RightCRServo.setPower(1);
        } else {
            LeftCRServo.setPower(stopPower[0]);
            RightCRServo.setPower(stopPower[1]);
        }
    }

    public void setPosition(ServoPosition position) {
        LeftPositionServo.setPosition(position.getPosition()[0]);
        RightPostiionServo.setPosition(position.getPosition()[1]);
        switch (position) {
            case IN:
                setPower(false);
                break;
            case OUT:
                setPower(true);
                break;
        }
    }



}
