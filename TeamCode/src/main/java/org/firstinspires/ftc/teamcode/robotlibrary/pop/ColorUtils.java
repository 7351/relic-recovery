package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class ColorUtils {

    public ColorSensor lineColorSensor;

    public ColorUtils(StateMachineOpMode opMode) {
        //jewelColorSensor = hardwareMap.colorSensor.get("jewelColorSensor");
        //lineColorSensor = hardwareMap.colorSensor.get("lineColorSensor");
        //TODO: Implement I2c address changing
    }

    public static Color getColorSensorColor(ColorSensor sensor) {
        Color returnColor = Color.NONE;
        if ((sensor.red() > sensor.green() + 4) && (sensor.red() > sensor.blue() + 4)) {
            returnColor = Color.RED;
        }
        if ((sensor.blue() > sensor.red() + 6) && (sensor.blue() > sensor.green() + 6)) {
            returnColor = Color.BLUE;
        }
        if ((sensor.red() >= 100) && (sensor.green() >= 100) && (sensor.blue() >= 100)) {
            returnColor = Color.WHITE;
        }
        if ((sensor.red() > sensor.green() + 10) && (sensor.green() > sensor.blue() + 5)) {
            returnColor = Color.BROWN;
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
        BROWN,
        GREY,
        NONE
    }

    public String colorData(ColorSensor sensor) {
        return String.valueOf("R: " + sensor.red() + " G: " + sensor.green() + " B: " + sensor.blue());
    }


}
