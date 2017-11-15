package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.JewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.LiftToPosition;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.VuforiaSystem;
import org.firstinspires.ftc.teamcode.teleops.TeleOp;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "R2NoGlyph", group = "A-Team")
public class R2NoGlyph extends StateMachineOpMode {

    String alliance = "Red";
    //VuforiaSystem vuforiaSystem;
    //RangeUtils rangeUtils;
    Intake intake;
    Lift lift;
    ColorUtils colorUtils;
    GyroUtils gyroUtils;
    JewelKicker kicker;
    RelicRecoveryVuMark relicRecoveryVuMark;
    ColorUtils.Color jewelColor;
    int amountOfColumns;

    @Override
    public void init() {

        //vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
        kicker = new JewelKicker(this);
        gyroUtils = GyroUtils.getInstance(this);
        intake = new Intake(this);
        //rangeUtils = new RangeUtils(hardwareMap);
        gyroUtils.calibrateGyro();
        lift = new Lift(this);
        lift.setGlyphGrabberPosition(Lift.ServoPosition.CLOSED);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            // Calibrate sensor
            gyroUtils.calibrateGyro();
            next();
        }

        if (stage == 1) {
            LiftToPosition.movePosition(this, lift, LiftToPosition.LiftPosition.SECOND);
        }

        if (stage == 2) {
            ActUponJewelKicker.doAction(this, kicker, alliance);
        }

        if (stage == 3) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
            EncoderDrive.createDrive(this, 1300, 0.35);
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
