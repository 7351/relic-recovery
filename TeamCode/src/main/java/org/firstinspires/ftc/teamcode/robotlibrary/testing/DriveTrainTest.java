package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/22/2017.
 */

@Autonomous(name = "DriveTrainTest")
public class DriveTrainTest extends StateMachineOpMode {

    DriveTrain driveTrain;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);

    }

    @Override
    public void start() {

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            driveTrain.LeftFrontMotor.setPower(1);
            if (time.time() > 2) {
                driveTrain.stopRobot();
                next();
            }
        }
        if (stage == 1) {
            driveTrain.RightFrontMotor.setPower(1);
            if (time.time() > 2) {
                driveTrain.stopRobot();
                next();
            }
        }
        if (stage == 2) {
            driveTrain.LeftBackMotor.setPower(1);
            if (time.time() > 2) {
                driveTrain.stopRobot();
                next();
            }
        }
        if (stage == 3) {
            driveTrain.RightBackMotor.setPower(1);
            if (time.time() > 2) {
                driveTrain.stopRobot();
                next();
            }
        }
        if (stage == 4) {
            driveTrain.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (time.time() > 1) {
                driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                next();
            }
        }
        if (stage == 5) {
            driveTrain.setTargetPosition(4000);
            next();
        }
        if (stage == 6) {
            if (!driveTrain.isBusy()) {
                driveTrain.stopRobot();
                next();
            }
        }

        telemetry.addData("Busy", driveTrain.isBusy());
        telemetry.addData("Stage", stage);
        telemetry.addData("F:",  driveTrain.LeftFrontMotor.getCurrentPosition() + " " + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B:",  driveTrain.LeftBackMotor.getCurrentPosition() + " " + driveTrain.RightBackMotor.getCurrentPosition());

    }
}
