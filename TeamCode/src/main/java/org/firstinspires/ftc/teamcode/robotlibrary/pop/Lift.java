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

    public DcMotor LiftMotor1, LiftMotor2;
    public Servo RampUpDown1, RampUpDown2;
    public Servo Gripper1, Gripper2;

    private RampServoPosition currentRampPosition;
    private GripperServoPosition gripperServoPosition;

    private double delta = 0.015;

    public Lift(StateMachineOpMode opMode) {
        this.opMode = opMode;
        LiftMotor1 = opMode.hardwareMap.dcMotor.get("LiftMotor1");
        LiftMotor2 = opMode.hardwareMap.dcMotor.get("LiftMotor2"); // Grab from hardwaremap

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        LiftMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        LiftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // We want the motor to brake when we give it 0 power
        LiftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        RampUpDown1 = opMode.hardwareMap.servo.get("RampUpDown1");
        RampUpDown2 = opMode.hardwareMap.servo.get("RampUpDown2");

        Gripper1 = opMode.hardwareMap.servo.get("Gripper1");
        Gripper2 = opMode.hardwareMap.servo.get("Gripper2");

        setGlyphGrabberPosition(GripperServoPosition.OPEN); // By default, set position to open
        setRampPosition(RampServoPosition.HOME);
    }

    public void setPower(double power) {
        LiftMotor1.setPower(power); // Give power to both motors
        LiftMotor2.setPower(power);
    }

    public void setMode(DcMotor.RunMode mode) {
        LiftMotor1.setMode(mode);
        LiftMotor2.setMode(mode);
    }

    public int[] getCurrentPositions() {
        return new int[]{LiftMotor1.getCurrentPosition(), -LiftMotor2.getCurrentPosition()};
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

    public enum GripperServoPosition {
        OPEN(0.47, 0.66),
        GRIP(0.52, 0.6);

        private double[] position; // Array containing data

        GripperServoPosition(double positionLeft, double positionRight) { // Constructor
            this.position = new double[]{positionLeft, positionRight}; // Create array
        }

        public double[] getPosition() { // Return data
            return position;
        }

    }

    public enum RampServoPosition {
        HOME(0.26),
        SCORE(0.83),
        FLAT(0.35),
        INBETWEEN(0.45);

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

    public void toggleGrip() {
        switch (gripperServoPosition) {
            case GRIP:
                setGlyphGrabberPosition(GripperServoPosition.OPEN);
                break;
            case OPEN:
                setGlyphGrabberPosition(GripperServoPosition.GRIP);
                break;
        }
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

    public void setGlyphGrabberPosition(GripperServoPosition position) { // Function to operate the glyph grabber position
        Gripper1.setPosition(position.getPosition()[0]);
        Gripper2.setPosition(position.getPosition()[1]);
        gripperServoPosition = position;
    }

    public void setRampPosition(RampServoPosition position) {
        RampUpDown1.setPosition(position.position);
        RampUpDown2.setPosition(position.position);
        currentRampPosition = position;
    }

}

