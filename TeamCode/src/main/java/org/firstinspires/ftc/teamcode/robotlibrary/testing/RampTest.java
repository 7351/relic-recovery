package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Autonomous;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.LiftToPosition;

/**
 * Created by Leo on 11/26/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "RampTest")
public class RampTest extends Autonomous {

    @Override
    public void start() {
        gyroUtils.calibrateGyro();
        stage = 3;
    }

    @Override
    public void loop() {

        if (stage == 3) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
            EncoderDrive.createDrive(this, 1500, 0.35);
        }

        if (stage == 4) {
            BasicGyroTurn.createTurn(this, 90);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());
        telemetry.addData("Pitch", gyroUtils.getPitch());
        telemetry.addData("Roll", gyroUtils.getRoll());
        telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown") );

    }
}
