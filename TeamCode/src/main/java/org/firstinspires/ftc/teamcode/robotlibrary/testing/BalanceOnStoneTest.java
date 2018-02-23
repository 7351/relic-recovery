package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.BalanceOnStone;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

@Autonomous(name = "BalanceOnStoneTest")
public class BalanceOnStoneTest extends StateMachineOpMode {

    GyroUtils gyroUtils;

    @Override
    public void init() {
        gyroUtils = GyroUtils.getInstance(this);

    }

    @Override
    public void start() {
        gyroUtils.calibrateGyro();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            BalanceOnStone.balance(this);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());
        telemetry.addData("Pitch", gyroUtils.getPitch());
        telemetry.addData("Roll", gyroUtils.getRoll());

    }
}
