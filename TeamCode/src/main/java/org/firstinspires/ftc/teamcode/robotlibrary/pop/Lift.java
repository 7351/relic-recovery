package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Leo on 10/24/2017.
 */

public class Lift {

    private StateMachineOpMode opMode;

    public DcMotor LiftMotor;
    public Servo RampUpDown1, RampUpDown2;

    private RampServoPosition currentRampPosition;

    private double delta = 0.015;

    public Lift(StateMachineOpMode opMode) {
        this.opMode = opMode;
        LiftMotor = opMode.hardwareMap.dcMotor.get("LiftMotor2"); // Grab from hardwaremap

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        LiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // We want the motor to brake when we give it 0 power

        RampUpDown1 = opMode.hardwareMap.servo.get("RampUpDown1");
        RampUpDown2 = opMode.hardwareMap.servo.get("RampUpDown2");

        setRampPosition(RampServoPosition.HOME);
    }

    public void setPower(double power) {
        LiftMotor.setPower(power); // Give power to both motors
    }

    public void setMode(DcMotor.RunMode mode) {
        LiftMotor.setMode(mode);
    }

    public LiftToPosition.LiftPosition getClosestPosition() {
        int currentPosition = LiftMotor.getCurrentPosition();
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

    public enum RampServoPosition {
        HOME(0.26),
        SCORE(0.83),
        FLAT(0.4),
        INBETWEEN(0.5);

        private double position; // Array containing data

        RampServoPosition(double positionLeft) { // Constructor
            this.position = positionLeft; // Create array
        }

        public double getPosition() { // Return data
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

    public void rampPositionUp() {
        switch (currentRampPosition) {
            case HOME:
                setRampPosition(RampServoPosition.INBETWEEN);
                break;
            case INBETWEEN:
                setRampPosition(RampServoPosition.SCORE);
                break;
        }
    }

    public void rampPositionDown() {
        switch (currentRampPosition) {
            case SCORE:
                setRampPosition(RampServoPosition.INBETWEEN);
                break;
            case INBETWEEN:
                setRampPosition(RampServoPosition.HOME);
                break;
        }
    }

    public void setRampPosition(RampServoPosition position) {
        RampUpDown1.setPosition(position.position);
        RampUpDown2.setPosition(position.position);
        currentRampPosition = position;
    }

}

