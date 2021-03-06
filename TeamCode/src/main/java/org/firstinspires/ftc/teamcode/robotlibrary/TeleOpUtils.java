package org.firstinspires.ftc.teamcode.robotlibrary;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.suitbots.util.Controller;

import java.text.DecimalFormat;

/**
 * Created by Dynamic Signals on 10/31/2016.
 */

public class TeleOpUtils {

    private Gamepad gamepad1, gamepad2;
    public Controller gamepad1Controller, gamepad2Controller;
    public static final DecimalFormat df = new DecimalFormat("#.##");

    public TeleOpUtils(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        gamepad1Controller = new Controller(gamepad1);
        gamepad2Controller = new Controller(gamepad2);
    }

    public void updateControllers() {
        gamepad1Controller.update();
        gamepad2Controller.update();
    }

    public double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};
        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        return dScale;
    }

}