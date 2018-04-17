package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.stormbots.MiniPID;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class BasicGyroTurn implements Routine {

    private static BasicGyroTurn instance;
    private final double TOLERANCE_DEGREES = 3; // The degrees positive and negative that you want to get to
    private double TIMEOUT;

    public GyroUtils.GyroDetail detail; // Used for getting useful data and stats from a turn
    public DriveTrain driveTrain; // DriveTrain instance, it's public so you can grab the object outside the class
    private MiniPID controller;
    private GyroUtils gyroUtils;

    private double targetDegree = 0;
    private int completedCounter = 0;

    private ElapsedTime creationTime = new ElapsedTime(); // Used for timeout failsafe

    private StateMachineOpMode opMode;
    private double MinMotor = 0.1, MaxMotor = 1;

    /**
     * Static constructor for a BasicGyroTurn if you want to specify the PID for
     *
     * @param pid The PID instance of the class that has the P, I, and D coefficients.
     * @return the BasicGyroTurn instance
     */
    public static BasicGyroTurn createTurn(StateMachineOpMode opMode, double targetDegree, PIDCoefficients pid) {
        if (instance == null) {
            instance = new BasicGyroTurn(opMode, targetDegree, pid);
        }
        instance.isCompleted();
        return instance;
    }

    /**
     * Static constructor for an OpMode style class with just the degree and opmode
     *
     * @param opModeArg       the OpMode that implements StateMachineOpMode, usually just type "this"
     * @param targetDegreeArg the degree that you want to turn to (0-360)
     * @return the instance of the BasicGyroTurn class. You can check the progress using the GyroDetail detail for percentage complete.
     */
    public static BasicGyroTurn createTurn(StateMachineOpMode opModeArg, double targetDegreeArg) {
        return createTurn(opModeArg, targetDegreeArg, null);
    }

    private BasicGyroTurn(StateMachineOpMode opMode, double targetDegree, @Nullable PIDCoefficients pid) {
        this.opMode = opMode;
        this.gyroUtils = GyroUtils.getInstance(opMode);
        this.targetDegree = targetDegree;
        opMode.telemetryEnabled = false;

        driveTrain = new DriveTrain(opMode.hardwareMap);

        driveTrain.controller.resetDeviceConfigurationForOpMode();

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (pid == null) {
            pid = new PIDCoefficients(0.02825, 0, 0.05);
        }

        controller = new MiniPID(pid.p, pid.i, pid.d);
        controller.setOutputLimits(-1, 1);
        controller.setDirection(true);

        creationTime.reset();

        /* Start tracking data */
        detail = new GyroUtils.GyroDetail(gyroUtils, targetDegree);

        detail.updateData();

        TIMEOUT = detail.degreesOff * 0.03;

    }

    @Override
    public void run() {

        detail.updateData();

        double output = -controller.getOutput(detail.degreesOffAndDirection, 0);
        double power = 0; // Temp value
        if (Math.signum(output) == 1) {
            power = Range.clip(output, MinMotor, MaxMotor);
        }
        if (Math.signum(output) == -1) {
            power = Range.clip(output, -MaxMotor, -MinMotor);
        }
        opMode.telemetry.addData("Power", power);
        driveTrain.powerLeft(-power);
        driveTrain.powerRight(power);


    }

    @Override
    public boolean isCompleted() {
        boolean onTarget = detail.degreesOff < TOLERANCE_DEGREES;
        if (onTarget) {
            completedCounter++;
        } else {
            completedCounter = 0;
        }
        if (completedCounter > 25 || (TIMEOUT > 0 && creationTime.time() > TIMEOUT)) {
            completed();
        } else {
            run();
        }
        return (completedCounter > 25);
    }

    @Override
    public void completed() {
        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        driveTrain.stopRobot();
        opMode.next(); // Go to next stage
        opMode.telemetryEnabled = true;
        teardown();
    }

    public static void teardown() {
        instance = null;
    }

}