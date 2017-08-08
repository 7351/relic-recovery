package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Dynamic Signals on 10/21/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpREVBoard")
public class TeleOpREVBoard extends OpMode {


    public DcMotor motor_1;
    public Servo servo_1;
    public ColorSensor color;
    public DigitalChannel push;
    public BNO055IMU imu;
    double servoPostion1 = 0.0;
    double servoPostion2 = 1;
    double motorPower = 0;

    @Override
    public void init() {

        motor_1 = hardwareMap.dcMotor.get("motor");
        motor_1.setDirection(DcMotor.Direction.REVERSE);
        servo_1 = hardwareMap.servo.get("servo");
        color = hardwareMap.colorSensor.get("color");
        push = hardwareMap.digitalChannel.get("push");
        push.setMode(DigitalChannel.Mode.INPUT);
        imu = hardwareMap.
    }

    @Override
    public void loop() {

        /*
         * Controller 1 Controls --------------------------------------------------
         */
        motor_1.setPower(-gamepad1.right_stick_y);

        if (gamepad1.a) {
            servo_1.setPosition(servoPostion1);
        }
        if (gamepad1.y) {
            servo_1.setPosition(servoPostion2);
        }

        //telemetry.addData("Beacon Servo", beaconUtils.BeaconServo.getPosition());
        telemetry.addData("Servo:", servo_1.getPosition());
        telemetry.addData("Motor:", motor_1.getPower());
        telemetry.addData("Color Red:", color.red());
        telemetry.addData("Color Green:", color.green());
        telemetry.addData("Color Blue:", color.blue());
        telemetry.addData("Push", (push.getState() ? "Not pushed" : "Pushed"));
    }
}