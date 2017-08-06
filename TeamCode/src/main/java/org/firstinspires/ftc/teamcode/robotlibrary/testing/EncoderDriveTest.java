package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderDriveTest", group = "Testing")
public class EncoderDriveTest extends OpMode {

    DriveTrain driveTrain;

    int stage = 0;

    EncoderDrive drive;

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);
    }

    /* Encoder drive sequence
     * 1. Reset
     * 2. Set target
     * 3. Set mode to RUN_TO_POSITION
     * 4. Set motor power
     * 5. Wait until motor isn't busy
     */

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1000, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
            }
        }


        telemetry.addData("Left Front Position: ", driveTrain.LeftFrontMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());

        DbgLog.msg(String.valueOf(": " + driveTrain.LeftBackMotor.getCurrentPosition() + " - " + driveTrain.LeftBackMotor.getTargetPosition()));

    }
}