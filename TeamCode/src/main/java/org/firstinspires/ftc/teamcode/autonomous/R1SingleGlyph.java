package org.firstinspires.ftc.teamcode.autonomous;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.VuforiaSystem;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@Autonomous(name = "R1SingleGlyph", group = "Main")
public class R1SingleGlyph extends StateMachineOpMode {

    String alliance = "Red";
    VuforiaSystem vuforiaSystem;
    //RangeUtils rangeUtils;
    ColorUtils colorUtils;
    GyroUtils gyroUtils;
    RelicRecoveryVuMark relicRecoveryVuMark;
    ColorUtils.Color jewelColor;
    int amountOfColumns;

    @Override
    public void init() {

        vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
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
            // Extend mechanism to reach out for jewels (probably servo)
            if (time.time() > 0.5) {
                next();
            }
        }
        if (stage == 3) {
            // Read color of jewels (to be determined)
            //jewelColor = colorUtils.getColorSensorColor(colorUtils.jewelColorSensor);
            next();
        }

        if (stage == 4) {
            // Manipulate servo to knock off opposing alliance jewel.
            if (time.time() > 0.5) {
                next();
            }
        }

        if (stage == 5) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
            EncoderDrive.createDrive(this, 1500, 0.35);
        }

        waitStage(6);

        if (stage == 7) {
            BasicGyroTurn turn = BasicGyroTurn.createTurn(this, 90, new PIDCoefficients(0.015, 0, 0.01));
            if (turn != null) {
                telemetry.addData("Degrees left", turn.detail.degreesOffAndDirection);
            }
        }

        waitStage(8);

        if (stage == 9) {
            EncoderDrive.createDrive(this, 400, 0.35);
        }

        /*
        if (stage == 8) {
            if (colorUtils.getColorSensorColor(colorUtils.lineColorSensor).equals((alliance.equals("Red") ? ColorUtils.Color.RED : ColorUtils.Color.BLUE))) {
                next();
            }
        }*/

        if (stage == 9) {
            // Stop driving
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Heading", gyroUtils.getHeading());
        telemetry.addData("Pitch", gyroUtils.getPitch());
        telemetry.addData("Roll", gyroUtils.getRoll());
        telemetry.addData("VuMark", (relicRecoveryVuMark != null ? relicRecoveryVuMark.toString().toLowerCase() : "Unknown") );

    }
}
