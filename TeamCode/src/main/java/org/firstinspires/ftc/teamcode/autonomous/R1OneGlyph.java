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

@Autonomous(name = "R1OneGlyph", group = "A-Team")
public class R1OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Autonomous {

    @Override
    public void start() {
        setAlliance("Red");
        gyroUtils.calibrateGyro();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            ActUponJewelKicker.doAction(this, kicker, alliance);
            relicRecoveryVuMark = vuforiaSystem.getVuMark();
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.UNKNOWN)) {
                relicRecoveryVuMark = RelicRecoveryVuMark.RIGHT;
            }
        }

        if (stage == 1) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        /*
         * Left - 1900
         * Center - 1575
         * Right - 1225
         */
        if (stage == 2) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, 1900, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, 1575, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, 1225, 0.35);
            }
        }

        if (stage == 3) {
            BasicGyroTurn.createTurn(this, 90);
        }

        if (stage == 4) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        if (stage == 5) {
            EncoderDrive.createDrive(this, 350, true);
        }

        if (stage == 6) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
        }

        if (stage == 7) {
            lift.setGlyphGrabberPosition(Lift.ServoPosition.OPEN);
            next();
        }

        if (stage == 8) {
            EncoderDrive.createDrive(this, -100);
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
