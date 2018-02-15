package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Leo on 11/5/2017.
 */

public class JewelKicker {

    Servo JewelKickerX, JewelKickerY;
    public LynxI2cColorRangeSensor colorRangeSensor;
    public ColorSensor colorSensor;

    double delta = 0.01;
    double minPosition = 0.09;

    public JewelKicker(StateMachineOpMode opMode) {
        JewelKickerX = opMode.hardwareMap.servo.get("JewelKickerX"); // Left and right
        JewelKickerY = opMode.hardwareMap.servo.get("JewelKickerY"); // Up and out

        colorSensor = opMode.hardwareMap.colorSensor.get("JewelColorSensor");
        colorSensor.enableLed(true);
        colorRangeSensor = (LynxI2cColorRangeSensor) colorSensor;

        setJewelKickerPosition(ServoPosition.INROBOT);
    }

    public enum ServoPosition {
        INROBOT(0.65, 0.06),
        MIDDLEJEWEL(INROBOT.getPosition()[0], 0.11),
        KNOCKRIGHT(0.74, MIDDLEJEWEL.getPosition()[1]),
        KNOCKLEFT(0.4, MIDDLEJEWEL.getPosition()[1]),
        TELEOP(INROBOT.getPosition()[0], 0.38);

        private double[] position; // Array containing data

        ServoPosition(double positionX, double positionY) { // Constructor
            this.position = new double[]{positionX, positionY}; // Create array
        }

        public double[] getPosition() { // Return data
            return position;
        }


    }

    public void setJewelKickerPosition(ServoPosition position) {
        JewelKickerX.setPosition(position.getPosition()[0]);
        JewelKickerY.setPosition(position.getPosition()[1]);
    }

    public void setJewelKickerPositionY(ServoPosition servoPosition, ElapsedTime servoStepperTime, double deltaPosition, double deltaTime) {
        JewelKickerX.setPosition(servoPosition.getPosition()[0]);
        double position = servoPosition.getPosition()[1];
        double currentServoPosition = JewelKickerY.getPosition();
        if (servoStepperTime.time() > deltaTime) {
            if (currentServoPosition < position) { // We need to increment up
                JewelKickerY.setPosition(Range.clip(currentServoPosition + deltaPosition, 0, position));
            } else if (currentServoPosition > position) {
                JewelKickerY.setPosition(Range.clip(currentServoPosition - deltaPosition, position, 1));
            }
            servoStepperTime.reset();
        }
    }

    public void setJewelKickerPositionX(ServoPosition servoPosition, ElapsedTime servoStepperTime, double deltaPosition, double deltaTime) {
        JewelKickerY.setPosition(servoPosition.getPosition()[0]);
        double position = servoPosition.getPosition()[0];
        double currentServoPosition = JewelKickerX.getPosition();
        if (servoStepperTime.time() > deltaTime) {
            if (currentServoPosition < position) { // We need to increment up
                JewelKickerX.setPosition(Range.clip(currentServoPosition + deltaPosition, 0, position));
            } else if (currentServoPosition > position) {
                JewelKickerX.setPosition(Range.clip(currentServoPosition - deltaPosition, position, 1));
            }
            servoStepperTime.reset();
        }
    }

    public void stepKickerUp() {
        JewelKickerY.setPosition(Range.clip(JewelKickerY.getPosition() + delta, minPosition, 1));
    }

    public void stepKickerDown() {
        JewelKickerY.setPosition(Range.clip(JewelKickerY.getPosition() - delta, minPosition, 1));
    }

    @Override
    public String toString() {
        return "X: " + JewelKickerX.getPosition() + " Y: " + JewelKickerY.getPosition();
    }
}
