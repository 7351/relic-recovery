package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GP2Y0A21YK0F;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 10/29/2017.
 */

@Autonomous(name = "GP2Y0A21YK0FTest")
public class GP2Y0A21YK0FTest extends StateMachineOpMode {

    GP2Y0A21YK0F analogDistance;

    @Override
    public void init() {

        analogDistance = new GP2Y0A21YK0F(this);

    }

    @Override
    public void loop() {

        telemetry.addData("Distance", analogDistance.getDistance(DistanceUnit.CM));

    }
}
