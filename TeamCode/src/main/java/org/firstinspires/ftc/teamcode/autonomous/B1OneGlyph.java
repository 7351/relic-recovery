package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.serenegiant.media.Encoder;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "B1OneGlyph", group = "A-Team")
public class B1OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.pop.Autonomous {

    boolean twoGlyph = false;

    @Override
    public void start() {
        setAlliance("Blue");
        gyroUtils.calibrateGyro();
    }

    @Override
    public void loop() {

        // Use Jewel Kicker with color and alliance and read vumark
        if (stage == 0) {
            ActUponJewelKicker.doAction(this, kicker, alliance);
            relicRecoveryVuMark = vuforiaSystem.getVuMark();
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.UNKNOWN)) {
                relicRecoveryVuMark = RelicRecoveryVuMark.RIGHT;
            }
            if (time.time() > 1) {
                lift.setRampPosition(Lift.RampServoPosition.FLAT); // Set ramp position to flat
            }
        }

        /*
         * Left - -1700
         * Center - -1000
         * Right - -1520
         */
        // Drive to distance depending on read vumark
        if (stage == 1) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, -1700, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, -1100, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, -1250, 0.35);
            }
        }

        // Turn based on vumark
        if (stage == 2) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 55);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, 115);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 125);
            }
        }

        // Drive to the correct distance away from the cyrptobox
        if (stage == 3) {
            EncoderDrive.createDrive(this, 200);
        }

        // Put the ramp up
        if (stage == 4) {
            lift.setRampPosition(Lift.RampServoPosition.SCORE);
            if (time.time() > 1) {
                next();
            }
        }

        // Drive back to get ready to push back in
        if (stage == 5) {
            EncoderDrive.createDrive(this, -100);
        }

        // Push back in
        if (stage == 6) {
            EncoderDrive.createDrive(this, 220);
        }

        // Drive back out
        if (stage == 7) {
            EncoderDrive.createDrive(this, -200);
        }

        // Put ramp down
        if (stage == 8) {
            lift.setRampPosition(Lift.RampServoPosition.HOME);
            if (twoGlyph) next();
        }

        // Turn to face the pit of glyphs
        if (stage == 9) {
            BasicGyroTurn.createTurn(this, 90);
        }

        if (stage == 10) {
            intake.setPower(Intake.Power.IN);
            intake.setPosition(Intake.ServoPosition.OUT);
            EncoderDrive.createDrive(this, -1300);
        }

        if (stage == 11) {
            EncoderDrive.createDrive(this, 100, 0.25);
        }

        if (stage == 12) {
            EncoderDrive.createDrive(this, -100, 0.25);
        }

        if (stage == 13) {
            BasicGyroTurn.createTurn(this, 90);
        }

        if (stage == 14) {
            EncoderDrive.createDrive(this, 1300);
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
