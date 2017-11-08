package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.JewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.VuforiaSystem;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "B1SingleGlyph", group = "Main")
public class B1SingleGlyph extends StateMachineOpMode {

    String alliance = "Blue";
    VuforiaSystem vuforiaSystem;
    //RangeUtils rangeUtils;
    ColorUtils colorUtils;
    GyroUtils gyroUtils;
    JewelKicker kicker;
    RelicRecoveryVuMark relicRecoveryVuMark;
    ColorUtils.Color jewelColor;
    int amountOfColumns;

    @Override
    public void init() {

        vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
        kicker = new JewelKicker(this);
        gyroUtils = GyroUtils.getInstance(this);
        //rangeUtils = new RangeUtils(hardwareMap);
        gyroUtils.calibrateGyro();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            // Calibrate sensor
            gyroUtils.calibrateGyro();
            next();
        }

        if (stage == 1) {
            // Read VuMark
            if (time.time() > 0.5) { // Wait 0.5 seconds before we take our VuMark reading
                relicRecoveryVuMark = vuforiaSystem.getVuMark(); // Store the value
                next();
            }
        }

        if (stage == 2) {
            ActUponJewelKicker.doAction(this, kicker, alliance);
        }

        if (stage == 3) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
            EncoderDrive.createDrive(this, 1500, 0.35);
        }

        waitStage(4);

        if (stage == 5) {
            BasicGyroTurn turn = BasicGyroTurn.createTurn(this, -90);
            if (turn != null) {
                telemetry.addData("Degrees left", turn.detail.degreesOffAndDirection);
            }
        }

        waitStage(6);

        if (stage == 7) {
            EncoderDrive.createDrive(this, 400, 0.35);
        }

        /*
        if (stage == 8) {
            if (colorUtils.getColorSensorColor(colorUtils.lineColorSensor).equals((alliance.equals("Red") ? ColorUtils.Color.RED : ColorUtils.Color.BLUE))) {
                next();
            }
        }*/

        if (stage == 8) {
            // Stop driving
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());
        telemetry.addData("Pitch", gyroUtils.getPitch());
        telemetry.addData("Roll", gyroUtils.getRoll());
        telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown") );

    }
}
