package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import android.os.Environment;

import com.google.common.io.Files;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.GP2Y0E02A;
import org.firstinspires.ftc.teamcode.robotlibrary.pop.StateMachineOpMode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 10/29/2017.
 */

@Autonomous(name = "GP2Y0E02ATest")
public class GP2Y0E02ATest extends StateMachineOpMode {

    GP2Y0E02A distanceSensor;
    DriveTrain driveTrain;

    private File chartLocation = new File(Environment.getExternalStorageDirectory() + File.separator + "FIRST" + File.separator + "IRSensorData.html");
    private List<String> rangeData;


    @Override
    public void init() {

        distanceSensor = hardwareMap.get(GP2Y0E02A.class, "distance");
        driveTrain = new DriveTrain(this);

        rangeData = new ArrayList<>();

    }

    @Override
    public void loop() {

        if (stage == 0) {
            EncoderDrive.createDrive(this, 2000, 0.2);
            double timestamp = (double) Math.round(time.time() * 100) / 100;
            rangeData.add("\n[" + timestamp + ", " + distanceSensor.getDistance(DistanceUnit.CM) + "],");
        }

        telemetry.addData("Distance (cm)", distanceSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Distance (in)", distanceSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Stage", stage);

    }

    @Override
    public void stop() {
        writeData();
    }

    public void writeData() {
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
