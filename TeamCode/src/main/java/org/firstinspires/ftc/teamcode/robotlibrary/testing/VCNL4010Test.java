package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.VCNL4010;

/**
 * Created by Leo on 10/11/2017.
 */

@Autonomous(name = "VCNL4010Test")
public class VCNL4010Test extends StateMachineOpMode {

    VCNL4010 prox;

    @Override
    public void init() {

        prox = hardwareMap.get(VCNL4010.class, "prox");

    }

    @Override
    public void loop() {

        telemetry.addData("Led", prox.getLedCurrent());
        telemetry.addData("Proximity", prox.getProximity());
        telemetry.addData("Ambient", prox.getAmbient());

    }
}
