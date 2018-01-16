package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import android.os.Environment;

import com.google.common.io.Files;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.BasicGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 10/3/2017.
 */

@Autonomous(name = "RangeSensorTest")
public class RangeSensorTest extends StateMachineOpMode {

    ModernRoboticsI2cRangeSensor rangeSensor;
    DriveTrain driveTrain;

    private File chartLocation = new File(Environment.getExternalStorageDirectory() + File.separator + "FIRST" + File.separator + "RangeSensorData.html");
    private List<String> rangeData;

    @Override
    public void init() {

        driveTrain = new DriveTrain(this);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
        rangeData = new ArrayList<>();

        GyroUtils.getInstance(this).calibrateGyro();
    }

    @Override
    public void loop() {
        if (stage == 0) {
            if (rangeSensor.getDistance(DistanceUnit.INCH) <= 25) {
                driveTrain.powerLeft(0.25);
                driveTrain.powerLeft(0.25);
            } else {
                driveTrain.stopRobot();
                next();
            }
            double timestamp = (double) Math.round(time.time() * 100) / 100;
            rangeData.add("\n[" + timestamp + ", " + rangeSensor.getDistance(DistanceUnit.CM) + "],");
        }

        if (stage == 1) {
            BasicGyroTurn.createTurn(this, 90);
        }

        telemetry.addData("Range Optical(cm)", rangeSensor.cmOptical());
        telemetry.addData("Range Ultrasonic(cm)", rangeSensor.cmUltrasonic());
        telemetry.addData("Range Total(in)", rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Range Total(cm)", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Time", time.time());

    }

    @Override
    public void stop() {
        writeGyroData();
    }

    public void writeGyroData() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script><script type=\"text/javascript\">google.charts.load('current', {packages: ['corechart', 'line']});google.charts.setOnLoadCallback(drawBackgroundColor);function drawBackgroundColor() {var data = new google.visualization.DataTable();data.addColumn('number', 'Time (seconds)');data.addColumn('number', 'Distance (cm)');data.addRows([");
        for (String arrayString : rangeData) sb.append(arrayString);
        sb.append("\n]);var options = {hAxis: {title: 'Time'},vAxis: {title: 'Cm from wall (or anything blocking)'}};var chart = new google.visualization.LineChart(document.getElementById('chart_div'));chart.draw(data, options);}</script><div id=\"chart_div\"></div>");
        try {
            Files.write(sb.toString().getBytes(Charset.defaultCharset()), chartLocation); // Write to the html location
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
