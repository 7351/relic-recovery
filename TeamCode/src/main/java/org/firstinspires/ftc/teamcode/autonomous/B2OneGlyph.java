package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.LiftToPosition;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "B2OneGlyph", group = "A-Team")
public class B2OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.pop.Autonomous {

    @Override
    public void start() {
        setAlliance("Blue");
        gyroUtils.calibrateGyro();
    }

    @Override
    public void loop() {

        // Use Jewel Kicker with color and alliance and read vumark
        if (stage == 0) {
            getVuMark();
            ActUponJewelKicker.doAction(this, kicker, alliance);
            if (time.time() > 1) {
                lift.setRampPosition(Lift.RampServoPosition.FLAT); // Set ramp position to flat
            }
        }

        // Drive off platform
        if (stage == 1) {
            EncoderDrive.createDrive(this, -1000, 0.35);
        }

        // Turn to be parallel with cryptobox
        if (stage == 2) {
            BasicGyroTurn.createTurn(this, 90);
        }

        /*
         * Left - -500
         * Center - None
         * Right - -300
         */
        // Drive to distance depending on read vumark
        if (stage == 3) {
            if (relicRecoveryVuMark == null) {
                relicRecoveryVuMark = RelicRecoveryVuMark.LEFT;
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, -500, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, -200, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, -600, 0.35);
            }
        }

        // Turn based on vumark
        if (stage == 4) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 152);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, -158);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, -163);
            }
        }

        if (stage == 5) {
            EncoderDrive.createDrive(this, 90);
        }

        if (stage == 6) {
            lift.setRampPosition(Lift.RampServoPosition.SCORE);
            if (time.time() > 1) {
                next();
            }
        }

        if (stage == 7) {
            EncoderDrive.createDrive(this, -175);
        }

        if (stage == 8) {
            EncoderDrive.createDrive(this, 220);
        }

        if (stage == 9) {
            EncoderDrive.createDrive(this, -200);
        }

        if (stage == 10) {
            lift.setRampPosition(Lift.RampServoPosition.HOME);
            next();
        }

        if (telemetryEnabled) {
            telemetry.addData("Stage", stage);
            telemetry.addData("Heading", gyroUtils.getHeading());
            telemetry.addData("Pitch", gyroUtils.getPitch());
            telemetry.addData("Roll", gyroUtils.getRoll());
            telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown"));

        }

    }

}
