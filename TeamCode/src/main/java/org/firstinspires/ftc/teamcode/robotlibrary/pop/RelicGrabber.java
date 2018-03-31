package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class RelicGrabber {

    private DcMotor LiftMotor;
    private ElapsedTime time;
    double deltaPosition = 0.025, deltaTime = 0.0005;
    public Servo TopGrabberServo, BottomGrabberServo;
    public Servo RelicRackCRServo;
    public BottomGrabberPosition BottomCurrentPosition = BottomGrabberPosition.OPEN;
    public TopGrabberPosition TopCurrentPosition = TopGrabberPosition.UP;

    public RelicGrabber(StateMachineOpMode opMode) {
        TopGrabberServo = opMode.hardwareMap.servo.get("TopGrabberServo");
        BottomGrabberServo = opMode.hardwareMap.servo.get("BottomGrabberServo");
        RelicRackCRServo = opMode.hardwareMap.servo.get("RelicRackCRServo");

        LiftMotor = opMode.hardwareMap.dcMotor.get("RelicLiftMotor");

        setBottomPosition(BottomCurrentPosition);
        setTopPosition(TopGrabberPosition.HOME);
        time = new ElapsedTime();
        bringDownRack();
    }

    public enum TopGrabberPosition {
        HOME(0.1),
        SQUARE(0.9),
        UP(0.3);

        private double position; // Array containing data

        TopGrabberPosition(double position) { // Constructor
            this.position = position;
        }

        public double getPosition() { // Return data
            return position;
        }
    }

    public enum BottomGrabberPosition {
        OPEN(0.31),
        GRIP(0.83);

        private double position; // Array containing data

        BottomGrabberPosition(double position) { // Constructor
            this.position = position;
        }

        public double getPosition() { // Return data
            return position;
        }
    }

    public void bringDownRack() {
        RelicRackCRServo.setPosition(0.97);
    }

    public void bringUpRack() {
        RelicRackCRServo.setPosition(0.615);
    }

    public void rackMiddle() {
        RelicRackCRServo.setPosition(0.8);
    }

    public void stepRackDown() {
        RelicRackCRServo.setPosition(Range.clip(RelicRackCRServo.getPosition() + 0.01, 0.615, 0.97));
    }

    public void stepRackUp() {
        RelicRackCRServo.setPosition(Range.clip(RelicRackCRServo.getPosition() - 0.01, 0.615, 0.97));
    }

    public void setBottomPosition(BottomGrabberPosition position) {
        BottomCurrentPosition = position;
        BottomGrabberServo.setPosition(position.getPosition());
    }

    public void setTopPosition(TopGrabberPosition position) {
        if (!position.equals(TopGrabberPosition.SQUARE)) {
            TopGrabberServo.setPosition(position.getPosition());
            TopCurrentPosition = position;
        } else {
            if (time.time() > deltaTime) {
                TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() + deltaPosition, 0, TopGrabberPosition.SQUARE.position));
                time.reset();
            }
            if (TopGrabberServo.getPosition() == TopGrabberPosition.SQUARE.position) TopCurrentPosition = position;
        }
    }

    public void setPower(double power) {
        LiftMotor.setPower(power); // Give power to both motors
    }

    public void stepTopUp() {
        TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() - 0.01, 0, 1));
    }

    public void stepTopDown() {
        TopGrabberServo.setPosition(Range.clip(TopGrabberServo.getPosition() + 0.01, 0, 1));
    }

}
