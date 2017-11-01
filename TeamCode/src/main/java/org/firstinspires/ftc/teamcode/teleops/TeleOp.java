package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.suitbots.util.Controller;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends StateMachineOpMode {

    DriveTrain driveTrain;
    Lift lift;
    TeleOpUtils teleOpUtils;
    private boolean slowMode = false;
    private double slowPower = 0.5;
    private double delta = 0.01;
    private DcMotor.ZeroPowerBehavior currentBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    @Override
    public void init() {

        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
        driveTrain = new DriveTrain(this);
        driveTrain.setZeroPowerBehavior(currentBehavior);
        lift = new Lift(this);

    }

    @Override
    public void loop() {

        teleOpUtils.updateControllers();

        /*- Controller 1 Controls -*/

        /*
         * Driving controls
         * Left bumper - Change from slow to fast mode
         * A - Toggle from brake to coast mode
         * Right Joystick - Arcade drive
         */

        if (teleOpUtils.gamepad1Controller.leftBumperOnce()) {
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
        float throttle = -gamepad1.right_stick_y;
        float direction = -gamepad1.right_stick_x;

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

        /*
         * Glyph Controls
         * D-Pad Down - Open
         * D-Pad Left - Semi-Open
         * D-Pad Right - Close
         * D-Pad Up - Push
         * Right Bumper - Manual override
         * D-Pad Up (Manual) - Step open by stepper delta
         * D-Pad Down (Manual) - Step closed by stepper delta
         */

        if (!gamepad1.right_bumper) {
            if (teleOpUtils.gamepad1Controller.dpadLeftOnce()) {
                lift.setGlyphGrabberPosition(Lift.ServoPosition.SEMIOPEN);
            }

            if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                lift.setGlyphGrabberPosition(Lift.ServoPosition.PUSH);
            }

            if (teleOpUtils.gamepad1Controller.dpadRightOnce()) {
                lift.setGlyphGrabberPosition(Lift.ServoPosition.CLOSED);
            }

            if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                lift.setGlyphGrabberPosition(Lift.ServoPosition.OPEN);
            }
        } else {
            if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                lift.stepOpen();
            }

            if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                lift.stepClosed();
            }
        }

        telemetry.addData("Grabber", "L: " + lift.GlyphGrabberLeft.getPosition() + ", R: " + lift.GlyphGrabberRight.getPosition());


        /*
         * Lift Controls
         * Left Joystick - Move up or down
         * Position buttons (To be found and assigned)
         */

        lift.setPower(-gamepad1.left_stick_y);


        /*- Controller 2 Controls -*/

    }
}
