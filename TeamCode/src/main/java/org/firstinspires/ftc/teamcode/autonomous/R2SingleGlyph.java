package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.VuforiaSystem;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

public class R2SingleGlyph extends StateMachineOpMode {

    String alliance = "Red";
    VuforiaSystem vuforiaSystem;
    RangeUtils rangeUtils;
    ColorUtils colorUtils;
    RelicRecoveryVuMark relicRecoveryVuMark;
    ColorUtils.Color jewelColor;
    int amountOfColumns;

    @Override
    public void init() {

        vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
        rangeUtils = new RangeUtils(hardwareMap);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            // Calibrate sensor
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
        }

        if (stage == 4) {
            // Manipulate servo to knock off opposing alliance jewel.
            if (time.time() > 0.5) {
                next();
            }
        }

        if (stage == 5) {
            // Drive forward until designated distance
        }

        if (stage == 6) {
            BasicGyroTurn.createTurn(this, 90);
        }

        if (stage == 7) {
            // Drive forward while checking proximity sensor
            // Do code to count how many columns we have passed
        }

        if (stage == 8) {
            BasicGyroTurn.createTurn(this, 0); // 90 or -90 undecided
        }

        if (stage == 9) {
            //Drive forward to score glyph using encoders or sensors
        }

        if (stage == 10) {
            if (colorUtils.getColorSensorColor(colorUtils.lineColorSensor).equals((alliance.equals("Red") ? ColorUtils.Color.RED : ColorUtils.Color.BLUE))) {
                next();
            }
        }

        if (stage == 11) {
            // Stop driving
        }

    }
}
