package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DcMotor;
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

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition) {
        return createDrive(opMode, targetPosition, 0.5);
    }

    public static EncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition, double power) {
        if (instance == null) {
            instance = new EncoderDrive(opMode, targetPosition, power);
        }
        instance.isCompleted();
        return instance;
    }

    private EncoderDrive(StateMachineOpMode opMode, int targetPosition, double power) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode.hardwareMap);
        drive = new LegacyEncoderDrive(new DriveTrain(opMode.hardwareMap), targetPosition, power);
    }

    @Override
    public void run() {
        drive.run();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = drive.isCompleted();
        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        opMode.next();
        instance = null;
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

    private int stuckCounter = 0;
    private int lastRightPositon = 0;

    // We only want to use the FrontRightMotor encoder

    /**
     * Constructor for the LegacyEncoderDrive object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public LegacyEncoderDrive(DriveTrain driveTrain, int targetPosition, double power) {
        this.driveTrain = driveTrain;
        this.targetPosition = targetPosition;
        this.power = power;

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        startingPositionRight = driveTrain.RightFrontMotor.getCurrentPosition();

        driveTrain.RightFrontMotor.setTargetPosition(targetPosition + startingPositionRight);

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

        // Set the power for all 4 motors
        driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
        driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
        // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
        // If the target is negative, and you specified positive power, it will change it to negative power
    }

    public void runWithDecrementPower(double subtractivePower) {
        power = Range.clip(power - subtractivePower, 0, 1);
        run();
    }
}
