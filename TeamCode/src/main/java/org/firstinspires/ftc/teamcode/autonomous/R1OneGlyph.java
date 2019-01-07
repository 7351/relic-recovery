package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.JewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.LiftToPosition;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ScoreGlyph;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "R1OneGlyph", group = "A-Team")
public class R1OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.pop.Autonomous {

    boolean twoGlyph = false;

    @Override
    public void start() {
        classType = Type.AUTONOMOUS;
        setAlliance("Red");
        startingPosition = CLOSE;
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

        /*
         * Left - 1350
         * Center - 1100
         * Right - 1850
         */
        // Drive to distance depending on read vumark
        if (stage == 1) {
            if (relicRecoveryVuMark == null) {
                relicRecoveryVuMark = RelicRecoveryVuMark.LEFT;
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, 1370, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, 1100, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, 1750, 0.35);
            }
        }

        // Turn based on vumark
        if (stage == 2) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 51);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, 58);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 120);
            }
        }

        if (stage == 3) {
            ScoreGlyph.scoreGlyph(this, !twoGlyph);
        }

        // Turn to face the pit of glyphs
        if (stage == 4) {
            if (twoGlyph) {
                BasicGyroTurn.createTurn(this, 90);
            } else {
                if (time.time() > 3) {
                    kicker.setJewelKickerPosition(JewelKicker.ServoPosition.INROBOT);
                }
            }
        }

        if (stage == 5) {
            intake.setPower(Intake.Power.IN);
            intake.setPosition(Intake.ServoPosition.OUT);
            EncoderDrive.createDrive(this, -1300, 0.35);
        }

        if (stage == 6) {
            if (time.time() > 0.75) {
                next();
            }
        }

        if (stage == 7) {
            BasicGyroTurn.createTurn(this, 90);
        }

        if (stage == 8) {
            lift.setRampPosition(Lift.RampServoPosition.FLAT);
            intake.setPower(Intake.Power.STOP);
            intake.setPosition(Intake.ServoPosition.IN);
            EncoderDrive.createDrive(this, 1150, 0.35);
        }

        // Turn based on vumark
        if (stage == 9) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                BasicGyroTurn.createTurn(this, 51);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                BasicGyroTurn.createTurn(this, 58);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                BasicGyroTurn.createTurn(this, 120);
            }
        }

        if (stage == 10) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        // Drive to the correct distance away from the cyrptobox
        if (stage == 11) {
            ScoreGlyph.scoreGlyph(this, true);
        }

        if (stage == 12) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
            lift.setRampPosition(Lift.RampServoPosition.HOME);
        }

        if (stage == 13) {
            if (time.time() > 3) {
                kicker.setJewelKickerPosition(JewelKicker.ServoPosition.INROBOT);
            }
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
