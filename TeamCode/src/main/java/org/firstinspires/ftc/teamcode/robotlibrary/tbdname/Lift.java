package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Leo on 10/24/2017.
 */

public class Lift {

    private StateMachineOpMode opMode;

    private DcMotor LiftMotor1, LiftMotor2;
    public Servo GlyphGrabberLeft, GlyphGrabberRight;

    private double delta = 0.015;

    public Lift(StateMachineOpMode opMode) {
        this.opMode = opMode;
        LiftMotor1 = opMode.hardwareMap.dcMotor.get("LiftMotor1");
        LiftMotor2 = opMode.hardwareMap.dcMotor.get("LiftMotor2");
        LiftMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        LiftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        GlyphGrabberLeft = opMode.hardwareMap.servo.get("GlyphGrabberLeft");
        GlyphGrabberLeft.setDirection(Servo.Direction.REVERSE);
        GlyphGrabberRight = opMode.hardwareMap.servo.get("GlyphGrabberRight");

        setGlyphGrabberPosition(ServoPosition.OPEN);
    }

    public void setPower(double power) {
        LiftMotor1.setPower(power);
        LiftMotor2.setPower(power);
    }

    public enum ServoPosition {
        OPEN(0.771, 0.75),
        SEMIOPEN(0.662, 0.65),
        CLOSED(0.58, 0.589),
        PUSH(0.45, 0.55);

        private double[] position;

        ServoPosition(double positionLeft, double positionRight){
            this.position = new double[] {positionLeft, positionRight};
        }

        public double[] getPosition(){
            return position;
        }


    }

    public void stepClosed() {
        GlyphGrabberLeft.setPosition(Range.clip(GlyphGrabberLeft.getPosition() - delta, 0, 1));
        GlyphGrabberRight.setPosition(Range.clip(GlyphGrabberRight.getPosition() - delta, 0, 1));
    }

    public void stepOpen() {
        GlyphGrabberLeft.setPosition(Range.clip(GlyphGrabberLeft.getPosition() + delta, 0, 1));
        GlyphGrabberRight.setPosition(Range.clip(GlyphGrabberRight.getPosition() + delta, 0, 1));
    }

    public void setGlyphGrabberPosition(ServoPosition position) {
        GlyphGrabberLeft.setPosition(position.getPosition()[0]);
        GlyphGrabberRight.setPosition(position.getPosition()[1]);
    }



}
