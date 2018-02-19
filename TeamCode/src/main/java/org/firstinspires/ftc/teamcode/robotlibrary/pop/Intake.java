package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Leo on 11/11/2017.
 */

public class Intake {

    StateMachineOpMode opMode;
    public Servo LeftPositionServo, RightPositionServo;
    public CRServo LeftIntakeCRServo, RightIntakeCRServo;

    public Intake(StateMachineOpMode opMode) {
        this.opMode = opMode;

        LeftPositionServo = opMode.hardwareMap.servo.get("LeftIntakePositionServo");
        RightPositionServo = opMode.hardwareMap.servo.get("RightIntakePositionServo");
        LeftIntakeCRServo = opMode.hardwareMap.crservo.get("LeftIntakeCRServo");
        RightIntakeCRServo = opMode.hardwareMap.crservo.get("RightIntakeCRServo");
        RightIntakeCRServo.setDirection(DcMotorSimple.Direction.REVERSE);

        setPosition(ServoPosition.IN);
    }

    public enum ServoPosition {
        IN(0.25, 0.75),
        OUT(0.92, 0.07);

        private double[] position; // Array containing data

        ServoPosition(double positionLeft, double positionRight) { // Constructor
            this.position = new double[]{positionLeft, positionRight}; // Create array
        }

        public double[] getPosition() { // Return data
            return position;
        }

    }

    public enum Power {
        OUT(-0.7),
        STOP(0),
        IN(0.7);

        private double power;

        Power(double power) {
            this.power = power;
        }

        public double getPower() {return power;}

    }

    @Deprecated
    public void setPower(boolean on) {
        /*
        if (on) {
            LeftIntakeCRServo.setPower(0.7);
            RightIntakeCRServo.setPower(0.7);
        } else {
            LeftIntakeCRServo.setPower(0);
            RightIntakeCRServo.setPower(0);
        }*/
    }

    public void setPower(Power power) {
        LeftIntakeCRServo.setPower(power.getPower());
        RightIntakeCRServo.setPower(power.getPower());
    }

    public void setPosition(ServoPosition position) {
        LeftPositionServo.setPosition(position.getPosition()[0]);
        RightPositionServo.setPosition(position.getPosition()[1]);
    }

}
