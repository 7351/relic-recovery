package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.LiftToPosition;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

/**
 * Created by Leo on 11/12/2017.
 */

@Autonomous(name = "LiftTest")
public class LiftTest extends StateMachineOpMode {

    Lift lift;

    @Override
    public void init() {

        lift = new Lift(this);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        if (stage == 1) {
            waitStage(1, 1);
        }

        if (stage == 2) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FOURTH);
        }

        if (stage == 3) {
            waitStage(3, 1);
        }

        if (stage == 4) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        if (stage == 5) {
            waitStage(5, 1);
        }

        if (stage == 6) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Counts", lift.getAveragePosition());

    }
}
