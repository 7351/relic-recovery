package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

/**
 * Created by Leo on 11/9/2017.
 */

@Autonomous(name = "EncoderDriveTurnClass")
public class SquareTest extends StateMachineOpMode {

    DriveTrain driveTrain;
    GyroUtils gyroUtils;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);

        gyroUtils = GyroUtils.getInstance(this);
        gyroUtils.calibrateGyro();

    }

    @Override
    public void init_loop() {

        if (gamepad1.a) {
            stage = 0;
        }
        if (gamepad1.y) {
            stage = 1;
        }
        telemetry.addData("Stage", stage);
    }

    @Override
    public void loop() {

        if (stage == 0) {
            EncoderDrive.createDrive(this, 700, 0.35);
        }
        if (stage == 1) {
            BasicGyroTurn.createTurn(this, 90);
        }
        if (stage == 2) {
            EncoderDrive.createDrive(this, 700, 0.35);
        }
        if (stage == 3) {
            BasicGyroTurn.createTurn(this, 180);
        }
        if (stage == 4) {
            EncoderDrive.createDrive(this, 700, 0.35);
        }
        if (stage == 5) {
            BasicGyroTurn.createTurn(this, -90);
        }
        if (stage == 6) {
            EncoderDrive.createDrive(this, 700, 0.35);
        }
        if (stage == 7) {
            BasicGyroTurn.createTurn(this, 0);
        }
        if (stage == 8) {
            stage = 0;
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());

    }
}
