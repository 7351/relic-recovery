package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 9/24/2017.
 */

@Autonomous(name = "GyroTest")
public class GyroTest extends StateMachineOpMode {

    GyroUtils gyroUtils;
    GyroUtils.GyroDetail detail;

    @Override
    public void init() {

        //GyroUtils.teardown();
        gyroUtils = GyroUtils.getInstance(this);
        gyroUtils.calibrateGyro();

    }

    @Override
    public void loop() {

        if (stage == 0) {
            detail = new GyroUtils.GyroDetail(gyroUtils, 90);
        }

        telemetry.addData("heading", gyroUtils.getHeading());
        telemetry.addData("roll", gyroUtils.getRoll());
        telemetry.addData("pitch", gyroUtils.getPitch());
        telemetry.addData("degrees off", detail.degreesOff);
        telemetry.addData("direction", detail.turnDirection);

    }

}
