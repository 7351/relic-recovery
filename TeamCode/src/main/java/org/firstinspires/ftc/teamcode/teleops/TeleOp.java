package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robotlibrary.TeleOpUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.IntakeV2;
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
    IntakeV2 intakeV2;
    private boolean slowMode = false;
    private double slowPower = 0.5;
    private double delta = 0.01;
    private boolean runLiftToPosition = false;
    private LiftToPosition.LiftPosition targetPosition;
    private DcMotor.ZeroPowerBehavior currentBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
    private JewelKicker.ServoPosition currentKickerPosition = JewelKicker.ServoPosition.TELEOP;
    public boolean intakev2 = false;
    public boolean eightMotorMode = false;

    @Override
    public void init() {
        teleOpUtils = new TeleOpUtils(gamepad1, gamepad2);
    }

    @Override
    public void init_loop() {
        teleOpUtils.updateControllers();
        if (intakev2) {
            if (teleOpUtils.gamepad1Controller.XOnce()) {
                eightMotorMode = !eightMotorMode;
            }
            telemetry.addData("How to", "Use X to toggle between 8 or 4");
            telemetry.addData("Current", (eightMotorMode ? "8" : "6"));
        }

    }

    @Override
    public void start() {


        driveTrain = new DriveTrain(this);
        driveTrain.setZeroPowerBehavior(currentBehavior);
        lift = new Lift(this);
        kicker = new JewelKicker(this);
        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.TELEOP);
        relicGrabber = new RelicGrabber(this);
        if (!intakev2) {
            intake = new Intake(this);
        } else {
            intakeV2 = new IntakeV2(this, eightMotorMode);
        }


    }

    @Override
    public void loop() {

        teleOpUtils.updateControllers();

        /*- Controller 1 Controls -*/

        /*
         * Kicker controls
         * X - toggle between INROBOT and TELEOP
         */

        if (teleOpUtils.gamepad1Controller.XOnce()) {
            if (currentKickerPosition.equals(JewelKicker.ServoPosition.TELEOP)) {
                currentKickerPosition = JewelKicker.ServoPosition.INROBOT;
            } else if (currentKickerPosition.equals(JewelKicker.ServoPosition.INROBOT)) {
                currentKickerPosition = JewelKicker.ServoPosition.TELEOP;
            }
            kicker.setJewelKickerPosition(currentKickerPosition);
        }

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

        if (gamepad1.left_trigger == 0) {
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
                if (!gamepad1.b && !gamepad1.x) {
                    if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                        lift.stepOpen();
                    }

                    if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                        lift.stepClosed();
                    }
                }
                if (gamepad1.b) {
                    if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                        lift.stepOpenRight();
                    }

                    if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                        lift.stepClosedRight();
                    }
                } else if (gamepad1.x) {
                    if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                        lift.stepOpenLeft();
                    }

                    if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                        lift.stepClosedLeft();
                    }
                }

            }
        }

        telemetry.addData("Grabber", "L: " + lift.GlyphGrabberLeft.getPosition() + ", R: " + lift.GlyphGrabberRight.getPosition());


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
        if (leftStickValueY < -0.15) {
            lift.setPower(leftStickValueY * 0.3);
        }
        if (leftStickValueY > 0.15) {
            lift.setPower(leftStickValueY);
        }

        if (gamepad1.left_trigger != 0) {
            if (teleOpUtils.gamepad1Controller.dpadUpOnce()) {
                runLiftToPosition = true;
                targetPosition = lift.getPositionAbove();
            }
            if (teleOpUtils.gamepad1Controller.dpadDownOnce()) {
                runLiftToPosition = true;
                targetPosition = LiftToPosition.LiftPosition.GROUND;
            }
            if (teleOpUtils.gamepad1Controller.dpadLeftOnce()) {
                runLiftToPosition = true;
                targetPosition = LiftToPosition.LiftPosition.FIRST;
            }
        }

        if (runLiftToPosition) {
            LiftToPosition.movePosition(this, lift, targetPosition);
        }

        if (stage == 1) {
            runLiftToPosition = false;
            stage = 0;
        }

        telemetry.addData("Lift Position", lift.getClosestPosition().toString());
        telemetry.addData("Lift Encoder", "Avg: " + lift.getAveragePosition() + " 1: " + -lift.getCurrentPositions()[0] + " 2: " + lift.getCurrentPositions()[1]);


        /*
         * Intake Controls
         * When Right trigger is pressed - put intake out and set power to be on
         */

        if (!intakev2) {
            if (gamepad1.right_trigger != 0) {
                intake.setPosition(Intake.ServoPosition.OUT);
            } else {
                intake.setPosition(Intake.ServoPosition.IN);
            }
        } else {
            if (gamepad1.right_trigger != 0) {
                intakeV2.setIntakeMode(IntakeV2.IntakeMode.IN);
            } else if (gamepad1.left_trigger != 0) {
                intakeV2.setIntakeMode(IntakeV2.IntakeMode.OUT);
            } else if (gamepad1.right_bumper) {
                intakeV2.setIntakeMode(IntakeV2.IntakeMode.UP);
            } else if (gamepad1.left_bumper) {
                intakeV2.setIntakeMode(IntakeV2.IntakeMode.DOWN);
            } else {
                intakeV2.setIntakeMode(IntakeV2.IntakeMode.REST);
            }
        }

        /*- Controller 2 Controls -*/

        /*
         * Relic Scorer
         * Left Joystick - move lift out and in
         * A - Put grabber down
         * Y - Put grabber up
         * X - Toggle for gripping
         */

        double relicLiftJoystick = -teleOpUtils.scaleInput(gamepad2.left_stick_y);
        relicGrabber.setPower(relicLiftJoystick);

        if (teleOpUtils.gamepad2Controller.AOnce()) {
            relicGrabber.setTopPosition(RelicGrabber.TopGrabberPosition.SQUARE);
        }

        if (teleOpUtils.gamepad2Controller.YOnce()) {
            relicGrabber.setTopPosition(RelicGrabber.TopGrabberPosition.UP);
        }

        if (teleOpUtils.gamepad2Controller.XOnce()) {
            switch (relicGrabber.BottomCurrentPosition) {
                case GRIP:
                    if (!relicGrabber.TopCurrentPosition.equals(RelicGrabber.TopGrabberPosition.UP)) {
                        relicGrabber.setBottomPosition(RelicGrabber.BottomGrabberPosition.OPEN);
                    }
                    break;
                case OPEN:
                    relicGrabber.setBottomPosition(RelicGrabber.BottomGrabberPosition.GRIP);
                    break;
            }
        }


    }

    @Override
    public void stop() {

    }
}
