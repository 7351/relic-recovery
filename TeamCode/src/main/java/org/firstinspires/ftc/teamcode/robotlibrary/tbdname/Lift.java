package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        LiftMotor2 = opMode.hardwareMap.dcMotor.get("LiftMotor2"); // Grab from hardwaremap\

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        LiftMotor1.setDirection(DcMotorSimple.Direction.REVERSE); // Reverse direction of both motors
        LiftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        LiftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // We want the motor to brake when we give it 0 power
        LiftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        GlyphGrabberLeft = opMode.hardwareMap.servo.get("GlyphGrabberLeft"); // Grab both servos from hardwaremap
        GlyphGrabberRight = opMode.hardwareMap.servo.get("GlyphGrabberRight");
        GlyphGrabberLeft.setDirection(Servo.Direction.REVERSE); // Reverse left so that the zero is facing inward

        setGlyphGrabberPosition(ServoPosition.OPEN); // By default, set position to open
    }

    public void setPower(double power) {
        LiftMotor1.setPower(power); // Give power to both motors
        LiftMotor2.setPower(power);
    }

    public void setMode(DcMotor.RunMode mode) {
        LiftMotor1.setMode(mode);
        LiftMotor2.setMode(mode);
    }

    public void setTargetPosition(int position) {
        LiftMotor1.setTargetPosition(position);
        LiftMotor2.setTargetPosition(position);
    }

    public int[] getCurrentPositions() {
        return new int[]{LiftMotor1.getCurrentPosition(), LiftMotor2.getCurrentPosition()};
    }

    public int getAveragePosition() {
        int[] positions = getCurrentPositions();
        return (positions[0] + positions[1]) / 2;
    }

    public LiftToPosition.LiftPosition getClosestPosition() {
        int currentPosition = getAveragePosition();
        LiftToPosition.LiftPosition lowestOne = LiftToPosition.LiftPosition.FOURTH;
        for (LiftToPosition.LiftPosition position: LiftToPosition.LiftPosition.values()) {
            if (position.getPosition() < currentPosition) {
                lowestOne = position;
            }
        }
        if (currentPosition < LiftToPosition.LiftPosition.GROUND.getPosition()) {
            lowestOne = LiftToPosition.LiftPosition.GROUND;
        }
        return lowestOne;
    }

    public boolean isBusy() {
        return LiftMotor1.isBusy();
    }

    public enum ServoPosition {
        OPEN(0.748, 0.722), // Each position and their corresponding positions for both the Left and Right servos
        SEMIOPEN(0.662, 0.65),
        CLOSED(0.565, 0.573),
        PUSH(0.45, 0.55);

        private double[] position; // Array containing data

        ServoPosition(double positionLeft, double positionRight) { // Constructor
            this.position = new double[]{positionLeft, positionRight}; // Create array
        }

        public double[] getPosition() { // Return data
            return position;
        }

    }

    public LiftToPosition.LiftPosition getPositionAbove() {
        LiftToPosition.LiftPosition currentPosition = getClosestPosition();
        return LiftToPosition.LiftPosition.values()[Range.clip(currentPosition.ordinal() + 1, 0, LiftToPosition.LiftPosition.values().length - 1)];
    }

    public LiftToPosition.LiftPosition getPositionBelow() {
        LiftToPosition.LiftPosition currentPosition = getClosestPosition();
        return LiftToPosition.LiftPosition.values()[Range.clip(currentPosition.ordinal() - 1, 0, LiftToPosition.LiftPosition.values().length - 1)];

    }

    public void stepClosed() {
        GlyphGrabberLeft.setPosition(Range.clip(GlyphGrabberLeft.getPosition() - delta, 0, 1)); // Stepping closed aka subtracting delta
        GlyphGrabberRight.setPosition(Range.clip(GlyphGrabberRight.getPosition() - delta, 0, 1));
    }

    public void stepOpen() {
        GlyphGrabberLeft.setPosition(Range.clip(GlyphGrabberLeft.getPosition() + delta, 0, 1)); // Stepping open aka adding delta
        GlyphGrabberRight.setPosition(Range.clip(GlyphGrabberRight.getPosition() + delta, 0, 1));
    }

    public void setGlyphGrabberPosition(ServoPosition position) { // Function to operate the glyph grabber position
        GlyphGrabberLeft.setPosition(position.getPosition()[0]);
        GlyphGrabberRight.setPosition(position.getPosition()[1]);
    }

}

