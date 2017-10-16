package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/15/2017.
 */

@Autonomous(name = "ColorSensorTest")
public class ColorSensorTest extends StateMachineOpMode {

    ColorSensor sensor;
    LynxI2cColorRangeSensor distanceSensor;

    ColorUtils colorUtils;

    @Override
    public void init() {

        // 18 for blue is the last most visible color for red with the rev color sensor

        sensor = hardwareMap.colorSensor.get("colorSensor");
        distanceSensor = hardwareMap.get(LynxI2cColorRangeSensor.class, "colorSensor");
        colorUtils = new ColorUtils(this);
    }

    @Override
    public void loop() {

        telemetry.addData("Color", colorUtils.getColorSensorColor(sensor));
        telemetry.addData("RGB", colorUtils.colorData(sensor));
        telemetry.addData("Dist", distanceSensor.getDistance(DistanceUnit.CM));

    }
}
