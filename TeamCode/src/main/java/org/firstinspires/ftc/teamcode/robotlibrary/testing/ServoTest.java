package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/31/2017.
 */

@Autonomous(name = "ServoTest")
public class ServoTest extends StateMachineOpMode {
    
    private Servo servo1, servo2;
    TeleOpUtils utils;

    double delta = 0.01;

    @Override
    public void init() {

        utils = new TeleOpUtils(gamepad1, gamepad2);
        servo1 = hardwareMap.servo.get("JewelKickerX");
        servo2 = hardwareMap.servo.get("JewelKickerY");
        
        servo1.setPosition(0.6);
        servo2.setPosition(0);

    }

    @Override
    public void loop() {

        utils.updateControllers();

        if (utils.gamepad1Controller.YOnce()) {
            servo2.setPosition(Range.clip(servo2.getPosition() + delta, 0, 1));
        }

        if (utils.gamepad1Controller.AOnce()) {
            servo2.setPosition(Range.clip(servo2.getPosition() - delta, 0, 1));
        }

        if (utils.gamepad1Controller.dpadUpOnce()) {
            servo1.setPosition(Range.clip(servo1.getPosition() + delta, 0, 1));
        }

        if (utils.gamepad1Controller.dpadDownOnce()) {
            servo1.setPosition(Range.clip(servo1.getPosition() - delta, 0, 1));
        }

        telemetry.addData("1", servo1.getPosition());
        telemetry.addData("2", servo2.getPosition());

    }
}
