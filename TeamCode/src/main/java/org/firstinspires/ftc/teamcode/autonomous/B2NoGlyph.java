package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.LiftToPosition;
import org.firstinspires.ftc.teamcode.teleops.TeleOp;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "B2NoGlyph", group = "A-Team")
public class B2NoGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Autonomous {

    @Override
    public void start() {
        setAlliance("Blue");
    }

    @Override
    public void loop() {

        if (stage == 0) {
            // Calibrate sensor
            gyroUtils.calibrateGyro();
            next();
        }

        if (stage == 1) {
            ActUponJewelKicker.doAction(this, kicker, alliance);
        }

        if (stage == 2) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        if (stage == 3) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
            EncoderDrive.createDrive(this, -1300, 0.35);
        }

        if (stage == 4) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());
        telemetry.addData("Pitch", gyroUtils.getPitch());
        telemetry.addData("Roll", gyroUtils.getRoll());
        telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown") );

    }

    @Override
    public void stop() {
        internalOpModeServices.requestOpModeStop(new TeleOp());
    }
}
