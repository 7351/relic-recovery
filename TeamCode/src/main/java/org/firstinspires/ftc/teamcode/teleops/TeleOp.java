package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.suitbots.util.Controller;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends StateMachineOpMode {

    DriveTrain driveTrain;
    TeleOpUtils teleOpUtils;
    private boolean slowMode = false;
    private double slowPower = 0.5;
    private DcMotor.ZeroPowerBehavior currentBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    @Override
    public void init() {

        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
        driveTrain = new DriveTrain(this);
        driveTrain.setZeroPowerBehavior(currentBehavior);

    }

    @Override
    public void loop() {

        teleOpUtils.updateControllers();

        if (teleOpUtils.gamepad1Controller.rightBumperOnce()) {
            slowMode = !slowMode;
        }

        if (teleOpUtils.gamepad1Controller.AOnce()) {
            if (currentBehavior == DcMotor.ZeroPowerBehavior.BRAKE) {
                currentBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
            } else {
                currentBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
            }
            driveTrain.setZeroPowerBehavior(currentBehavior);
        }
        float left = 0;
        float right = 0;
        // throttle: right_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: right_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = gamepad1.right_stick_y;
        float direction = gamepad1.right_stick_x;

        right = throttle - direction;
        left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) teleOpUtils.scaleInput(right);
        left = (float) teleOpUtils.scaleInput(left);

        driveTrain.powerLeft((slowMode ? left * slowPower : left));
        driveTrain.powerRight((slowMode ? right * slowPower : right));

        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
        telemetry.addData("Drive mode", currentBehavior.toString().toLowerCase() + " " + (slowMode ? "slow" : "fast"));
    }
}
