package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/1/2017.
 */

@Autonomous(name = "GyroTurnTest")
public class GyroTurnTest extends StateMachineOpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {

        if (stage == 0) {
            BasicGyroTurn turn = BasicGyroTurn.createTurn(this, 90);
            if (turn != null) {
                telemetry.addData("Degrees left", turn.detail.degreesOff);
            }
        }

        telemetry.addData("Stage", stage);

    }

}
