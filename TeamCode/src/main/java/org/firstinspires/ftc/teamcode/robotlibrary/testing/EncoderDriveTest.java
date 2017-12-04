package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/22/2017.
 */

@Autonomous(name = "EncoderDriveTest")
public class EncoderDriveTest extends StateMachineOpMode {

    DriveTrain driveTrain;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);

    }

    @Override
    public void start() {
        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {

        if (stage == 0) {
            EncoderDrive.createDrive(this, 1000);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("F",  -driveTrain.LeftFrontMotor.getCurrentPosition() + " " + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B",  driveTrain.LeftBackMotor.getCurrentPosition() + " " + driveTrain.RightBackMotor.getCurrentPosition());


    }
}
