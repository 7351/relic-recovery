package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class RelicGrabber {

    private DcMotor LiftMotor1, LiftMotor2;
    private ElapsedTime time;
    double deltaPosition = 0.025, deltaTime = 0.0005;
    public Servo TopGrabberServo, BottomGrabberServo;
    public BottomGrabberPosition BottomCurrentPosition = BottomGrabberPosition.OPEN;
    public TopGrabberPosition TopCurrentPosition = TopGrabberPosition.SQUARECLOSE;

    public RelicGrabber(StateMachineOpMode opMode) {
        TopGrabberServo = opMode.hardwareMap.servo.get("TopGrabberServo");
        BottomGrabberServo = opMode.hardwareMap.servo.get("BottomGrabberServo");

        LiftMotor1 = opMode.hardwareMap.dcMotor.get("RelicLiftMotor1");
        LiftMotor2 = opMode.hardwareMap.dcMotor.get("RelicLiftMotor2");

        setBottomPosition(BottomCurrentPosition);
        setTopPosition(TopGrabberPosition.UP);
        time = new ElapsedTime();
    }

    public enum TopGrabberPosition {
        HOME(0),
        SQUARECLOSE(0.97),
        SQUAREFAR(0.79),
        UP(0.4);

        private double position; // Array containing data

        TopGrabberPosition(double position) { // Constructor
            this.position = position;
        }

        public double getPosition() { // Return data
            return position;
        }
    }

    public enum BottomGrabberPosition {
        OPEN(0.67),
        GRIP(0.27);

        private double position; // Array containing data

        BottomGrabberPosition(double position) { // Constructor
            this.position = position;
        }

        public double getPosition() { // Return data
            return position;
        }
    }

    public void setBottomPosition(BottomGrabberPosition position) {
        BottomCurrentPosition = position;
        BottomGrabberServo.setPosition(position.getPosition());
    }

    public void setTopPosition(TopGrabberPosition position) {
        TopCurrentPosition = position;
        if (!position.equals(TopGrabberPosition.SQUARECLOSE)) {
            TopGrabberServo.setPosition(position.getPosition());
        } else {
            if (time.time() > deltaTime) {
                TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() + deltaPosition, 0, TopGrabberPosition.SQUARECLOSE.position));
                time.reset();
            }
        }
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
        return new int[]{LiftMotor1.getCurrentPosition(), LiftMotor2.getCurrentPosition()};
    }

    public int getAveragePosition() {
        int[] positions = getCurrentPositions();
        return (positions[0] + positions[1]) / 2;
    }

    public LiftToPosition.LiftPosition getClosestPosition() {
        int currentPosition = getAveragePosition();
        LiftToPosition.LiftPosition lowestOne = LiftToPosition.LiftPosition.FOURTH;
        for (LiftToPosition.LiftPosition position : LiftToPosition.LiftPosition.values()) {
            if (position.getPosition() < currentPosition) {
                lowestOne = position;
            }
        }
        if (currentPosition < LiftToPosition.LiftPosition.GROUND.getPosition()) {
            lowestOne = LiftToPosition.LiftPosition.GROUND;
        }
        return lowestOne;
    }

    public LiftToPosition.LiftPosition getPositionAbove() {
        LiftToPosition.LiftPosition currentPosition = getClosestPosition();
        return LiftToPosition.LiftPosition.values()[Range.clip(currentPosition.ordinal() + 1, 0, LiftToPosition.LiftPosition.values().length - 1)];
    }

    public LiftToPosition.LiftPosition getPositionBelow() {
        LiftToPosition.LiftPosition currentPosition = getClosestPosition();
        return LiftToPosition.LiftPosition.values()[Range.clip(currentPosition.ordinal() - 1, 0, LiftToPosition.LiftPosition.values().length - 1)];

    }

    public void stepTopUp() {
        TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() - 0.01, 0, 1));
    }

    public void stepTopDown() {
        TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() + 0.01, 0, 1));
    }

}
