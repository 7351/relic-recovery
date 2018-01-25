package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.pop.ActUponJewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.JewelKicker;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.CM;

/**
 * Created by Leo on 11/5/2017.
 */

@Autonomous(name = "JewelKickerTest")
public class JewelKickerTest extends StateMachineOpMode {

    JewelKicker kicker;
    String alliance = "Blue";

    @Override
    public void init() {

        kicker = new JewelKicker(this);

    }

    @Override
    public void init_loop() {
        if (gamepad1.x) {
            alliance = "Blue";
        } else if(gamepad1.b) {
            alliance = "Red";
        }

        telemetry.addData("Alliance", "Current: " + alliance + " change with X & B");
    }

    @Override
    public void loop() {

        if (stage == 0) {
            ActUponJewelKicker doAction = ActUponJewelKicker.doAction(this, kicker, alliance);
            if (doAction != null) {
                telemetry.addData("ActionStage", doAction.stage);
                telemetry.addData("Computed Color", doAction.computerColor);
            }

        }

        telemetry.addData("Color", ColorUtils.getColorSensorColor(kicker.colorSensor));
        telemetry.addData("Distance", kicker.colorRangeSensor.getDistance(CM));
        telemetry.addData("Stage", stage);

    }
}
