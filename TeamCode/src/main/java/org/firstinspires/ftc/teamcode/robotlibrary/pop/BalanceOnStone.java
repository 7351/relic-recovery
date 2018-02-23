package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.Range;
import com.stormbots.MiniPID;

public class BalanceOnStone implements Routine {

    StateMachineOpMode opMode;
    GyroUtils gyroUtils;
    DriveTrain driveTrain;
    private static BalanceOnStone instance;
    MiniPID controller;
    PIDCoefficients pid;
    int stage = 0;
    private double MinMotor = 0.1, MaxMotor = 1;
    double initialPitch = 0;
    double pitch = 200;
    int completedCounter = 0;


    // Look at pitch
    public static BalanceOnStone balance(StateMachineOpMode opMode) {
        if (instance == null) {
            instance = new BalanceOnStone(opMode);
        }
        instance.isCompleted();
        return instance;
    }

    private BalanceOnStone(StateMachineOpMode opMode) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode);
        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gyroUtils = GyroUtils.getInstance(opMode);

        if (pid == null) {
            pid = new PIDCoefficients(0.029, 0, 0.008);
        }

        controller = new MiniPID(pid.p, pid.i, pid.d);
        controller.setOutputLimits(-1, 1);
        controller.setDirection(true);

        initialPitch = gyroUtils.getPitch();

    }

    @Override
    public void run() {
        pitch = gyroUtils.getPitch();
        if (stage == 0) {
            if (Math.abs(pitch) < 4) {
                driveTrain.powerLeft(0.8);
                driveTrain.powerRight(0.8);
            } else {
                stage++;
            }
        }
        if (stage == 1) {
            double output = controller.getOutput(pitch, -1);
            double power = 0; // Temp value
            if (Math.signum(output) == 1) {
                power = Range.clip(output, MinMotor, MaxMotor);
            }
            if (Math.signum(output) == -1) {
                power = Range.clip(output, -MaxMotor, -(MinMotor + 0.12));
            }
            opMode.telemetry.addData("Power", power);
            driveTrain.powerLeft(power);
            driveTrain.powerRight(power);
       }
    }

    @Override
    public boolean isCompleted() {
        boolean completed = (Math.abs(pitch) < 4 && stage == 1);
        if (completed) {
            completedCounter++;
        } else {
            completedCounter = 0;
        }
        if (completedCounter > 10) {
            completed();
        } else {
            run();
        }
        return (completedCounter > 10);
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        opMode.stage = 2;
        teardown();
    }

    public static void teardown() {
        instance = null;
    }
}
