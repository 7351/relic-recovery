package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;

/**
 * Created by Leo on 10/3/2017.
 */

@Autonomous(name = "RangeSensorTest")
public class RangeSensorTest extends StateMachineOpMode {

    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void init() {

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

    }

    @Override
    public void loop() {

        telemetry.addData("Range (in)", rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Range (cm)", rangeSensor.getDistance(DistanceUnit.CM));

    }
}
