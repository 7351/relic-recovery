package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Leo on 11/11/2017.
 */

public class Intake {

    StateMachineOpMode opMode;
    public Servo LeftPositionServo, RightPositionServo;
    public DcMotor LeftIntakeMotor, RightIntakeMotor;

    public Intake(StateMachineOpMode opMode) {
        this.opMode = opMode;

        LeftPositionServo = opMode.hardwareMap.servo.get("LeftIntakePositionServo");
        RightPositionServo = opMode.hardwareMap.servo.get("RightIntakePositionServo");
        LeftIntakeMotor = opMode.hardwareMap.dcMotor.get("LeftIntakeMotor");
        RightIntakeMotor = opMode.hardwareMap.dcMotor.get("RightIntakeMotor");
        LeftIntakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        setPosition(ServoPosition.IN);
    }

    public enum ServoPosition {
        IN(0.015, 1),
        OUT(0.89, 0.088);

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
            LeftIntakeMotor.setPower(0.7);
            RightIntakeMotor.setPower(0.7);
        } else {
            LeftIntakeMotor.setPower(0);
            RightIntakeMotor.setPower(0);
        }
    }

    public void setPosition(ServoPosition position) {
        LeftPositionServo.setPosition(position.getPosition()[0]);
        RightPositionServo.setPosition(position.getPosition()[1]);
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
