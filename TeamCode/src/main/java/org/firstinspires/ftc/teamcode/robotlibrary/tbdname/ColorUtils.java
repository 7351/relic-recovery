package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class ColorUtils {

    public ColorSensor jewelColorSensor, lineColorSensor;

    public ColorUtils(StateMachineOpMode opMode) {
        //jewelColorSensor = hardwareMap.colorSensor.get("jewelColorSensor");
        //lineColorSensor = hardwareMap.colorSensor.get("lineColorSensor");
        //TODO: Implement I2c address changing
    }

    public Color getColorSensorColor(ColorSensor sensor) {
        Color returnColor = Color.NONE;
        if ((sensor.red() > sensor.green() + 50) && (sensor.red() > sensor.blue() + 50)) {
            returnColor = Color.RED;
        }

        if ((sensor.blue() > sensor.red() + 10) && (sensor.blue() > sensor.green() + 10)) {
            returnColor = Color.BLUE;
        }
        if ((sensor.red() >= 100) && (sensor.green() >= 100) && (sensor.blue() >= 100)) {
            returnColor = Color.WHITE;
        }
        return returnColor;
    }

    public HSV getSensorHSV(ColorSensor sensor) {
        HSV returnHSV = new HSV();

        ModernRoboticsI2cColorSensor i2cColorSensor = (ModernRoboticsI2cColorSensor) sensor;

        NormalizedRGBA colors = i2cColorSensor.getNormalizedColors();

        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(Math.round(colors.red * 255), Math.round(colors.green * 255), Math.round(colors.blue * 255), hsv);

        returnHSV.hsv = hsv;
        returnHSV.hue = Double.valueOf(AutonomousUtils.df.format(hsv[0]));
        returnHSV.saturation = Double.valueOf(AutonomousUtils.df.format(hsv[1]));
        returnHSV.value = Double.valueOf(AutonomousUtils.df.format(hsv[2]));

        double sumRGB = (colors.red * 255) + (colors.blue * 255) + (colors.green * 255);
        returnHSV.intensity = Double.valueOf(AutonomousUtils.df.format(sumRGB / 3));

        return returnHSV;
    }

    /**
     * @param sensor
     * @return int color
     * @see ://developer.android.com/reference/android/graphics/Color.html#BLACK
     */
    public int getSensorColorFromHSV(ColorSensor sensor) {
        return android.graphics.Color.HSVToColor(getSensorHSV(sensor).hsv);
    }

    public class HSV {
        float[] hsv;
        double hue;
        double saturation;
        double value;
        double intensity;

        @Override
        public String toString() {
            return "H: " + hue + ", S: " + saturation + ", I: " + intensity;
        }
    }

    public enum Color {
        BLUE,
        RED,
        WHITE,
        NONE
    }

    public enum GlyphColors {
        BROWN, GREY
    }

    public String colorData(ColorSensor sensor) {
        return String.valueOf("R: " + sensor.red() + " G: " + sensor.green() + " B: " + sensor.blue());
    }


}
