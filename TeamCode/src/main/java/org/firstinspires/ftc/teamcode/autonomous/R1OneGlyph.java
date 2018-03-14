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

@Autonomous(name = "R1OneGlyph", group = "A-Team")
public class R1OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.pop.Autonomous {

    @Override
    public void start() {
        setAlliance("Red");
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
            EncoderDrive.createDrive(this, 200);
        }

        if (stage == 4) {
            lift.setRampPosition(Lift.RampServoPosition.SCORE);
            if (time.time() > 1) {
                next();
            }
        }

        if (stage == 5) {
            EncoderDrive.createDrive(this, -100);
        }

        if (stage == 6) {
            EncoderDrive.createDrive(this, 220);
        }

        if (stage == 7) {
            EncoderDrive.createDrive(this, -200);
        }

        if (stage == 8) {
            lift.setRampPosition(Lift.RampServoPosition.HOME);
            next();
        }

        if (stage == 9) {
            BasicGyroTurn.createTurn(this, 90);
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
