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

@Autonomous(name = "R2OneGlyph", group = "A-Team")
public class R2OneGlyph extends org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Autonomous {

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

        if (stage == 2) {
            EncoderDrive.createDrive(this, 1100, 0.35);
        }

        if (stage == 3) {
            BasicGyroTurn.createTurn(this, -90);
        }

        /*
         * Right - 100
         * Center - 425
         * Left - 825
         */

        if (stage == 4) {
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.RIGHT)) {
                EncoderDrive.createDrive(this, 100, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.CENTER)) {
                EncoderDrive.createDrive(this, 425, 0.35);
            }
            if (relicRecoveryVuMark.equals(RelicRecoveryVuMark.LEFT)) {
                EncoderDrive.createDrive(this, 825, 0.35);
            }
        }

        if (stage == 5) {
            BasicGyroTurn.createTurn(this, 0);
        }

        if (stage == 6) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.FIRST);
        }

        if (stage == 7) {
            EncoderDrive.createDrive(this, 350, 0.35);
        }

        if (stage == 8) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.GROUND);
        }

        if (stage == 9) {
            lift.setGlyphGrabberPosition(Lift.ServoPosition.OPEN);
            next();
        }

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
