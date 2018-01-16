package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

/**
 * Created by Leo on 10/31/2017.
 */

@Autonomous(name = "ServoTest")
public class ServoTest extends StateMachineOpMode {
    
    private Servo servo1, servo2;
    Intake intake;
    TeleOpUtils utils;

    double delta = 0.01;

    @Override
    public void init() {

        utils = new TeleOpUtils(gamepad1, gamepad2);
        intake = new Intake(this);
        servo1 = intake.LeftPositionServo;
        servo2 = intake.RightPositionServo;

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

        intake.setPower(gamepad1.right_trigger != 0);

        if (utils.gamepad1Controller.dpadLeftOnce()) {
            intake.setPosition(Intake.ServoPosition.OUT);
        }
        if (utils.gamepad1Controller.dpadRightOnce()) {
            intake.setPosition(Intake.ServoPosition.IN);
        }

        telemetry.addData("1", servo1.getPosition());
        telemetry.addData("2", servo2.getPosition());

    }
}
