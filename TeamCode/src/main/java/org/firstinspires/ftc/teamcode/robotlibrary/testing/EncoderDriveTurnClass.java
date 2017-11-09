package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 11/9/2017.
 */

@Autonomous(name = "EncoderDriveTurnClass")
public class EncoderDriveTurnClass extends StateMachineOpMode {

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

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());

    }
}
