package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/1/2017.
 */

@Autonomous(name = "GyroTurnTest")
public class GyroTurnTest extends StateMachineOpMode {

    GyroUtils gyroUtils;

    GyroUtils.GyroDetail detail;
    DriveTrain driveTrain;
    double completedTime;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);
        GyroUtils.getInstance(this).calibrateGyro();

    }

    @Override
    public void loop() {

        if (stage == 0) {
            BasicGyroTurn turn = BasicGyroTurn.createTurn(this, 90);
            if (turn != null) {
                detail = turn.detail;
                telemetry.addData("Degrees left", turn.detail.degreesOffAndDirection);
            } else {
                completedTime = time.time();
            }
        } if (stage == 1) {
            telemetry.addData("Finished degrees left", detail.degreesOff);
            telemetry.addData("Completed", completedTime);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("LF", driveTrain.LeftFrontMotor.getDirection());
        telemetry.addData("LB", driveTrain.LeftBackMotor.getDirection());
        telemetry.addData("RF", driveTrain.RightFrontMotor.getDirection());
        telemetry.addData("RB", driveTrain.RightBackMotor.getDirection());

    }

    @Override
    public void next() {
        stage++;
    }

}
