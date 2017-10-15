package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 10/16/2016.
 */

@Autonomous(name = "AutonomousREV", group = "AWorking")
public class AutonomousREV extends StateMachineOpMode {

    public DcMotor motor_1;
    public Servo servo_1;
    public ColorSensor color;
    double servoPostion1 = 0.0;
    double servoPostion2 = 1;
    double servoPostion3 = 0.5;
    double motorPower = 0;
    ColorUtils utils;
    int stage = 0;
    ElapsedTime time;

    @Override
    public void init() {

        motor_1 = hardwareMap.dcMotor.get("motor");
        motor_1.setDirection(DcMotor.Direction.REVERSE);
        servo_1 = hardwareMap.servo.get("servo");
        color = hardwareMap.colorSensor.get("color");

        utils = new ColorUtils(this);

    }

    @Override
    public void start() {
        time = new ElapsedTime();
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            if(time.time() > 1) {
                stage++;
                time.reset();
            }
        }

        if (stage == 1) {
            if (time.time() < 1){
                servo_1.setPosition(servoPostion1);
            }
            else {
                stage++;
                time.reset();
            }
        }

        if (stage == 2){
            if (time.time() < 5)
                motor_1.setPower(1);
            else {
                motor_1.setPower(0);
                stage++;
                time.reset();
            }
        }

        if (stage == 3){
            if (time.time() < 1){
                servo_1.setPosition(servoPostion2);
            }
            else {
                stage++;
                time.reset();
            }
        }

        if (stage == 4){
            if (time.time() < 5)
                motor_1.setPower(-.5);
            else {
                stage++;
                time.reset();
            }
        }

        if (stage == 5) {
            if (time.time() < 1){
                servo_1.setPosition(servoPostion3);
            }
            else{
                stage++;
                time.reset();
            }
        }

        if (stage == 6){
            if (color.red() > 2)
            {
                servo_1.setPosition(servoPostion1);
            }
        }

        telemetry.addData("Color", utils.getColorSensorColor(color).toString().toUpperCase());
        telemetry.addData("Motor Power: ", motor_1.getPower());
        telemetry.addData("Servo Position: ", servo_1.getPosition());
        telemetry.addData("Stage: ", stage);
        telemetry.addData("Time: ", time);


    }
}

