package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/1/2017.
 */

@Autonomous(name = "GyroTurnTest")
public class GyroTurnTest extends StateMachineOpMode {

    GyroUtils gyroUtils;

    GyroUtils.GyroDetail detail;
    double completedTime;

    @Override
    public void init() {

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

    }

    @Override
    public void next() {
        stage++;
    }

}
