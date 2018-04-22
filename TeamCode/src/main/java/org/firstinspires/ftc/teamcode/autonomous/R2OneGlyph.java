package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.LiftToPosition;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ScoreGlyph;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "R2OneGlyph", group = "A-Team")
public class R2OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.pop.Autonomous {

    boolean twoGlyph = false;

    @Override
    public void start() {
        classType = Type.AUTONOMOUS;
        setAlliance("Red");
        startingPosition = FAR;
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

        // Drive off ramp
        if (stage == 1) {
            EncoderDrive.createDrive(this, 1100, 0.35);
        }

        // Turn to -90 degrees to be parallel to the cryptobox
        if (stage == 2) {
            BasicGyroTurn.createTurn(this, -90);
        }

        /*
         * Right - 400
         * Center - 100
         * Left - 400
         */
        // Drive to appropriate positions based on read vumark
        if (stage == 3) {
            if (relicRecoveryVuMark == null) {
                relicRecoveryVuMark = RelicRecoveryVuMark.CENTER;
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, 400, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, 175, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, 400, 0.35);
            }
        }

        // Turn based on vumark
        if (stage == 4) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, -28);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, -27);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 25);
            }
        }

        if (stage == 5) {
            ScoreGlyph.scoreGlyph(this, !twoGlyph);
        }

        if (stage == 6 && twoGlyph && !relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 39);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, 39);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 44);
            }
        }

        if (stage == 7) {
            intake.setPower(Intake.Power.IN);
            intake.setPosition(Intake.ServoPosition.OUT);
            EncoderDrive.createDrive(this, -1775, 0.35);
        }

        if (stage == 8) {
            if (time.time() > 1.25) {
                next();
            }
        }

        if (stage == 9) {
            lift.setRampPosition(Lift.RampServoPosition.FLAT);
            intake.setPower(Intake.Power.STOP);
            intake.setPosition(Intake.ServoPosition.IN);
            EncoderDrive.createDrive(this, 1700, 0.35);
        }

        if (stage == 10) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, -28);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, -25);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 22);
            }
        }

        if (stage == 11) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        // Drive to the correct distance away from the cryptobox
        if (stage == 12) {
            ScoreGlyph.scoreGlyph(this, true);
        }

        if (stage == 13) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
            lift.setRampPosition(Lift.RampServoPosition.HOME);
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
