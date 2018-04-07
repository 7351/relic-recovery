package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BalanceOnStone;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.JewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.LiftToPosition;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.RelicGrabber;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 1/15/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends StateMachineOpMode {

    DriveTrain driveTrain;
    Lift lift;
    TeleOpUtils teleOpUtils;
    JewelKicker kicker;
    Intake intake;
    RelicGrabber relicGrabber;
    private boolean slowMode = false;
    private double slowPower = 0.5;
    private double delta = 0.01;
    private boolean runLiftToPosition = false;
    private boolean balacing = false;
    private LiftToPosition.LiftPosition targetPosition;
    private DcMotor.ZeroPowerBehavior currentBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
    private JewelKicker.ServoPosition currentKickerPosition = JewelKicker.ServoPosition.TELEOP;
    private boolean intakeRunning = false;
    private boolean rightButtonClicked = false;

    @Override
    public void init() {
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
    }

    @Override
    public void init_loop() {
        teleOpUtils.updateControllers();
    }

    @Override
    public void start() {

        driveTrain = new DriveTrain(this);
        driveTrain.setZeroPowerBehavior(currentBehavior);
        lift = new Lift(this);
        kicker = new JewelKicker(this);
        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.TELEOP);
        relicGrabber = new RelicGrabber(this);
        intake = new Intake(this);

    }

    @Override
    public void loop() {

        teleOpUtils.updateControllers();

        /*- Controller 1 Controls -*/

        /*
         * Kicker controls
         * X - toggle between INROBOT and TELEOP
         */

        if (teleOpUtils.gamepad1Controller.YOnce()) {
            if (currentKickerPosition.equals(JewelKicker.ServoPosition.TELEOP)) {
                currentKickerPosition = JewelKicker.ServoPosition.INROBOT;
            } else if (currentKickerPosition.equals(JewelKicker.ServoPosition.INROBOT)) {
                currentKickerPosition = JewelKicker.ServoPosition.TELEOP;
            }
            kicker.setJewelKickerPosition(currentKickerPosition);
        }

        telemetry.addData("Kicker", kicker.toString());

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
        float throttle = gamepad1.right_stick_y;
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

        if (!balacing) {
            driveTrain.powerLeft((slowMode ? left * slowPower : left));
            driveTrain.powerRight((slowMode ? right * slowPower : right));
        }

        if (teleOpUtils.gamepad1Controller.BOnce()) {
            balacing = !balacing;
        }

        if (balacing) {
            BalanceOnStone.balance(this);
        }

        if (!balacing) {
            BalanceOnStone.teardown();
        }

        if (stage == 2) {
            stage = 0;
            balacing = false;
        }

        /* Controller 1 telemetry data */
        telemetry.addData("Drive power", "L: " + String.valueOf(left) + ", R: " + String.valueOf(right));
        telemetry.addData("Drive mode", currentBehavior.toString().toLowerCase() + " " + (slowMode ? "slow" : "fast"));


        /*
         * Ramp controls
         * When left trigger isn't pressed
         * Dpad up - go to next ramp position
         * Dpad down - go to lower ramp position
         */

        if (gamepad1.left_trigger == 0) {
            if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                lift.setRampPosition(Lift.RampServoPosition.INBETWEEN);
            }
            if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                lift.setRampPosition(Lift.RampServoPosition.HOME);
            }
            if (teleOpUtils.gamepad1Controller.dpadRightOnce()) {
                lift.setRampPosition(Lift.RampServoPosition.FLAT);
            }

        }

        if (gamepad1.right_stick_button) {
            if (!rightButtonClicked) {
                if (gamepad1.left_trigger == 0) {
                    if (lift.currentRampPosition.equals(Lift.RampServoPosition.HOME)
                            || lift.currentRampPosition.equals(Lift.RampServoPosition.INBETWEEN)
                            || lift.currentRampPosition.equals(Lift.RampServoPosition.FLAT)) {
                        lift.setRampPosition(Lift.RampServoPosition.SCORE);
                    } else if (lift.currentRampPosition.equals(Lift.RampServoPosition.SCORE)) {
                        lift.setRampPosition(Lift.RampServoPosition.HOME);
                    }
                    rightButtonClicked = true;
                } else {
                    runLiftToPosition = true;
                    targetPosition = LiftToPosition.LiftPosition.GROUND;
                }
            }
        } else {
            rightButtonClicked = false;
        }



        /*
         * Lift Controls
         * Left Joystick - Move up or down
         * Position buttons (To be found and assigned)
         * Automatic positions (all with left trigger)
         * D-pad left - low position right off ground
         * D-pad up - level up
         * D-pad down - ground level
         */

        double leftStickValueY = -teleOpUtils.scaleInput(gamepad1.left_stick_y);
        if (leftStickValueY <= 0.15 && leftStickValueY >= -0.15) {
            lift.setPower(0);
        }
        if (leftStickValueY < -0.15) { // Down
            lift.setPower(leftStickValueY * 0.8);
        }
        if (leftStickValueY > 0.15) { // Up
            if (lift.LiftMotor.getCurrentPosition() < LiftToPosition.LiftPosition.TOP.getPosition()) {
                lift.setPower(leftStickValueY);
                if (lift.LiftMotor.getCurrentPosition() < LiftToPosition.LiftPosition.FIRST.getPosition()) {
                    lift.setRampPosition(Lift.RampServoPosition.INBETWEEN);
                }
            }
        }

        if (lift.LiftMotor.getCurrentPosition() > LiftToPosition.LiftPosition.TOP.getPosition() + 60) {
            lift.setPower(-0.1);
        }

        if (gamepad1.left_trigger != 0) {
            if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                runLiftToPosition = true;
                targetPosition = LiftToPosition.LiftPosition.GROUND;
            }
        }

        if (runLiftToPosition) {
            LiftToPosition.movePosition(this, lift, targetPosition);
            if (targetPosition.equals(LiftToPosition.LiftPosition.GROUND))
                lift.setRampPosition(Lift.RampServoPosition.HOME);
        }

        if (stage == 1) {
            runLiftToPosition = false;
            stage = 0;
        }

        telemetry.addData("Lift Position", lift.getClosestPosition().toString());
        telemetry.addData("Lift Encoder", lift.LiftMotor.getCurrentPosition());


        /*
         * Intake Controls
         * Right bumper - toggle intake out or in
         */

        if (teleOpUtils.gamepad1Controller.rightBumperOnce()) {
            intakeRunning = !intakeRunning;
            intake.setPower(Intake.Power.IN);
        }

        if (gamepad1.right_trigger != 0) {
            intake.setPosition(Intake.ServoPosition.OUT);
            if (!intakeRunning) {
                if (gamepad1.left_trigger == 0) {
                    intake.setPower(Intake.Power.IN);
                } else {
                    intake.setPower(Intake.Power.OUT);
                }

            }
        } else {
            intake.setPosition(Intake.ServoPosition.IN);
            if (!intakeRunning) {
                intake.setPower(Intake.Power.STOP);
            }
        }


        /*- Controller 2 Controls -*/

        /*
         * Relic Scorer
         * Left Joystick - move lift out and in
         *
         */

        double relicLiftJoystick = teleOpUtils.scaleInput(gamepad2.left_stick_y);
        relicGrabber.setPower(relicLiftJoystick);

        if (teleOpUtils.gamepad2Controller.A()) {
            if (!gamepad2.left_bumper) {
                if (gamepad2.right_trigger != 0 && gamepad2.left_trigger != 0) {
                    relicGrabber.stepTopDown();
                } else if (gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0) {
                    relicGrabber.setTopPosition(RelicGrabber.TopGrabberPosition.SQUARE);
                }
            }

        }

        if (teleOpUtils.gamepad2Controller.leftBumper()) {
            if (teleOpUtils.gamepad2Controller.A()) { // Bring down
                relicGrabber.bringUpRack();
            }

            if (teleOpUtils.gamepad2Controller.Y()) { // Bring up
                relicGrabber.bringDownRack();
            }
        }

        if (teleOpUtils.gamepad2Controller.left_trigger != 0 && teleOpUtils.gamepad2Controller.right_trigger == 0) {
            if (teleOpUtils.gamepad2Controller.A()) { // Bring down
                relicGrabber.stepRackDown();
            }

            if (teleOpUtils.gamepad2Controller.Y()) { // Bring up
                relicGrabber.stepRackUp();
            }
        }

        if (teleOpUtils.gamepad2Controller.YOnce()) {
            if (!gamepad2.left_bumper) {
                if (gamepad2.right_trigger != 0 && gamepad2.left_trigger != 0) {
                    relicGrabber.stepTopUp();
                } else if (gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0) {
                    relicGrabber.setTopPosition(RelicGrabber.TopGrabberPosition.UP);
                }
            }

        }

        if (teleOpUtils.gamepad2Controller.BOnce()) {
            relicGrabber.setTopPosition(RelicGrabber.TopGrabberPosition.HOME);
        }

        if (teleOpUtils.gamepad2Controller.XOnce()) {
            switch (relicGrabber.BottomCurrentPosition) {
                case GRIP:
                    if (relicGrabber.TopCurrentPosition.equals(RelicGrabber.TopGrabberPosition.SQUARE)) {
                        relicGrabber.setBottomPosition(RelicGrabber.BottomGrabberPosition.OPEN);
                    }
                    break;
                case OPEN:
                    relicGrabber.setBottomPosition(RelicGrabber.BottomGrabberPosition.GRIP);
                    break;
            }
        }

        telemetry.addData("Top Relic Servo", relicGrabber.TopGrabberServo.getPosition());

    }

    @Override
    public void stop() {
        if (intake != null) {
            intake.setPower(Intake.Power.STOP);
        }
    }
}
