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
            ActUponJewelKicker.doAction(this, kicker, alliance);
            relicRecoveryVuMark = vuforiaSystem.getVuMark();
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.UNKNOWN)) {
                relicRecoveryVuMark = RelicRecoveryVuMark.LEFT;
            }
        }

        // Lift up lift to second position
        if (stage == 1) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        // Drive off platform
        if (stage == 2) {
            EncoderDrive.createDrive(this, -980, 0.35);
        }

        // Turn to be parallel with cryptobox
        if (stage == 3) {
            BasicGyroTurn.createTurn(this, 90);
        }

        /*
         * Left - -500
         * Center - None
         * Right - -300
         */
        // Drive to distance depending on read vumark
        if (stage == 4) {
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
        if (stage == 5) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 144);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, -150);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, -153);
            }
        }

        // Bring lift down to prepare to insert
        if (stage == 6) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        // Insert glyph into rails
        if (stage == 7) {
            EncoderDrive.createDrive(this, 350, 0.35);
        }

        // Bring lift down to ground
        if (stage == 8) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
        }

        // Release glyph
        if (stage == 9) {
            lift.setGlyphGrabberPosition(Lift.GripperServoPosition.OPEN);
            next();
        }

        // Back up and park
        if (stage == 10) {
            EncoderDrive.createDrive(this, -100, 0.35);
        }

        if (telemetryEnabled) {
            telemetry.addData("Stage", stage);
            telemetry.addData("Heading", gyroUtils.getHeading());
            telemetry.addData("Pitch", gyroUtils.getPitch());
            telemetry.addData("Roll", gyroUtils.getRoll());
            telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown"));

        }

        if (stage != 1 && stage != 6 && stage != 8 && stage <= 10) {
            lift.setPower(0.01);
        }
    }

}
