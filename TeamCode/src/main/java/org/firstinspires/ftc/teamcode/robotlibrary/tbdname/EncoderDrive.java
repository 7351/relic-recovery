package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by leo on 3/25/17.
 */

public class EncoderDrive implements Routine {

    private static EncoderDrive instance;

    private LegacyEncoderDrive drive;
    private DriveTrain driveTrain;
    private StateMachineOpMode opMode;

    /*
    This class is basically a wrapper for LegacyEncoderDrive in the new StateMachine format
     */

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition, boolean wiggle) {
        return createDrive(opMode, targetPosition, 0.35, wiggle);
    }

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition) {
        return createDrive(opMode, targetPosition, 0.35, false);
    }

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition, double power) {
        return createDrive(opMode, targetPosition, power, false);
    }

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition, double power, boolean wiggle) {
        if (instance == null) {
            instance = new EncoderDrive(opMode, targetPosition, power, wiggle);
        }
        instance.isCompleted();
        return instance;
    }

    private EncoderDrive(StateMachineOpMode opMode, int targetPosition, double power, boolean wiggle) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode.hardwareMap);
        drive = new LegacyEncoderDrive(new DriveTrain(opMode.hardwareMap), targetPosition, power, wiggle);
    }

    private int lastEncoder = 0;
    private int stuckCounter = 0;

    @Override
    public void run() {
        drive.run();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = drive.isCompleted();
        if (lastEncoder == driveTrain.RightFrontMotor.getCurrentPosition()) {
            stuckCounter++;
        } else {
            stuckCounter = 0;
        }
        opMode.telemetry.addData("Counter", stuckCounter);
        if (stuckCounter > 2) {
            completed = true;
        }
        if (completed) {
            completed();
        } else {
            run();
        }
        lastEncoder = driveTrain.RightFrontMotor.getCurrentPosition();
        return completed;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        opMode.next();
        teardown();
    }

    public static void teardown() {
        instance = null;
    }
}

class LegacyEncoderDrive implements Routine {

    DriveTrain driveTrain;
    double power;
    public int targetPosition;
    private int startingPositionRight;
    private int startingPositionLeft;

    private int stuckCounter = 0;
    private int lastRightPositon = 0;
    private ElapsedTime time;
    private int stage = 0;
    private boolean wiggle = false;

    // We only want to use the FrontRightMotor encoder

    /**
     * Constructor for the LegacyEncoderDrive object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public LegacyEncoderDrive(DriveTrain driveTrain, int targetPosition, double power, boolean wiggle) {
        this.driveTrain = driveTrain;
        this.targetPosition = targetPosition;
        this.power = power;
        this.wiggle = wiggle;

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        startingPositionRight = driveTrain.RightFrontMotor.getCurrentPosition();
        startingPositionRight = driveTrain.LeftBackMotor.getCurrentPosition();

        driveTrain.RightFrontMotor.setTargetPosition(targetPosition + startingPositionRight);
        driveTrain.LeftBackMotor.setTargetPosition(targetPosition + startingPositionLeft);

        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        time = new ElapsedTime();

    }

    @Override
    public boolean isCompleted() {

        if (driveTrain.RightFrontMotor.getCurrentPosition() == lastRightPositon) {
            stuckCounter++;
        } else {
            stuckCounter = 0;
        }

        if (stuckCounter > 50) {
            return true;
        }

        if (Math.signum(targetPosition) == 1) { // If the target is positive
            if (driveTrain.RightFrontMotor.getCurrentPosition() > targetPosition + startingPositionRight) {
                return true;
            }
        } else { // If it is negative
            if (driveTrain.RightFrontMotor.getCurrentPosition() < targetPosition + startingPositionRight) {
                return true;
            }
        }

        lastRightPositon = driveTrain.RightFrontMotor.getCurrentPosition();
        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }


    @Override
    public void run() {

        if (wiggle) {
            if (stage == 0) { // Wiggle left
                if (time.time() < 0.25) {
                    driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
                    driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power * 0.5 : power * 0.5);
                } else {
                    stage++;
                    time.reset();
                }
            }
            if (stage == 1) { // Wiggle right
                if (time.time() < 0.25) {
                    driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power * 0.5 : power * 0.5);
                    driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
                } else {
                    stage = 0;
                    time.reset();
                }
            }
        } else {
            // Set the power for all 4 motors
            driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
            driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
            // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
            // If the target is negative, and you specified positive power, it will change it to negative power
        }

    }

    public void runWithDecrementPower(double subtractivePower) {
        power = Range.clip(power - subtractivePower, 0, 1);
        run();
    }
}
