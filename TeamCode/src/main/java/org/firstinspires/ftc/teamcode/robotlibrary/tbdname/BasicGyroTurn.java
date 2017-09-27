package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public class BasicGyroTurn implements Routine {

    private static BasicGyroTurn instance;
    private final double TOLERANCE_DEGREES = 2; // The degrees positive and negative that you want to get to
    private final double TIMEOUT = 0; // In seconds, 0 if you don't want a timeout

    public GyroUtils.GyroDetail detail; // Used for getting useful data and stats from a turn
    public GyroUtils gyroUtils;
    public DriveTrain driveTrain; // DriveTrain instance, it's public so you can grab the object outside th class

    private double targetDegree = 0;
    private int completedCounter = 0;

    private ElapsedTime creationTime = new ElapsedTime(); // Used for timeout failsafe

    private StateMachineOpMode opMode;
    private double MinMotor = 0.0925, MaxMotor = 1;

    private BasicGyroTurn(StateMachineOpMode opMode, double targetDegree) {
        this.opMode = opMode;
        this.gyroUtils = new GyroUtils(opMode);
        this.targetDegree = targetDegree;

        detail = new GyroUtils.GyroDetail(gyroUtils, targetDegree);
    }

    @Override
    public void run() {
        detail.updateData();

        double power = 0;

        if (detail.turnDirection.equals(GyroUtils.Direction.CLOCKWISE)) {
            power = 1;
        } else {
            power = -1;
        }

        driveTrain.powerLeft(power);
        driveTrain.powerRight(-power);

    }

    @Override
    public boolean isCompleted() {
        boolean onTarget = detail.isInTolerance(TOLERANCE_DEGREES);
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
        opMode.next(); // Go to next stage
        driveTrain.stopRobot();
        teardown();
    }

    public static void teardown() {
        instance = null;
    }


}
