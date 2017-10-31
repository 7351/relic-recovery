package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GP2Y0E02A;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/29/2017.
 */

@Autonomous(name = "GP2Y0E02ATest")
public class GP2Y0E02ATest extends StateMachineOpMode {

    GP2Y0E02A distanceSensor;

    @Override
    public void init() {

        distanceSensor = hardwareMap.get(GP2Y0E02A.class, "distance");

    }

    @Override
    public void loop() {

        telemetry.addData("Distance (cm)", distanceSensor.getDistance(DistanceUnit.CM));

    }
}
