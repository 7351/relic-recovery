package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.LiftToPosition;
import org.firstinspires.ftc.teamcode.teleops.TeleOp;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "B2OneGlyph", group = "A-Team")
public class B2OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Autonomous {

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
        }

        // Lift up lift to second position
        if (stage == 1) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        // Drive off platform
        if (stage == 2) {
            EncoderDrive.createDrive(this, -1100, 0.35);
        }

        // Turn to be parallel with cryptobox
        if (stage == 3) {
            BasicGyroTurn.createTurn(this, 90);
        }

        /*
         * Left - -200
         * Center - -400
         * Right - -775
         */
        // Drive to distance depending on read vumark
        if (stage == 4) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, -200, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, -400, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, -775, 0.35);
            }
        }

        // Turn to face cryptobox
        if (stage == 5) {
            BasicGyroTurn.createTurn(this, -180);
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
            lift.setGlyphGrabberPosition(Lift.ServoPosition.OPEN);
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
            telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown") );

        }
    }

}
