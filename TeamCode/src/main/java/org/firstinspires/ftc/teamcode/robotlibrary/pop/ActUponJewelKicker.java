package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.CM;

/**
 * Created by Leo on 11/5/2017.
 */

public class ActUponJewelKicker implements Routine {

    private static ActUponJewelKicker instance;

    StateMachineOpMode opMode;
    public ColorUtils.Color computerColor;
    ElapsedTime time, servoStepperTime;
    double deltaPosition = 0.025, deltaTime = 0.0005;
    JewelKicker kicker;
    String alliance;
    public int stage = 0;
    int ballDistance = 16 - 1;

    public static ActUponJewelKicker doAction(StateMachineOpMode opMode, JewelKicker kicker, String alliance) {
        if (instance == null) {
            instance = new ActUponJewelKicker(opMode, kicker, alliance);
        }
        instance.isCompleted();
        return instance;
    }


    private ActUponJewelKicker(StateMachineOpMode opMode, JewelKicker kicker, String alliance) {
        this.opMode = opMode;
        this.kicker = kicker;
        this.alliance = alliance;

        time = new ElapsedTime();
        servoStepperTime = new ElapsedTime();
        time.reset();

    }

    @Override
    public void run() {
        if (stage == 0) {
            servoStepperTime.reset();
            stage++;
            time.reset();
        }
        if (stage == 1) {
            kicker.setJewelKickerPositionY(JewelKicker.ServoPosition.MIDDLEJEWEL, servoStepperTime, deltaPosition, deltaTime);
            double distance = kicker.colorRangeSensor.getDistance(CM);
            if (time.time() > 1.5) {
                if (distance <= ballDistance) {
                    stage++;
                    time.reset();
                } else {
                    if (time.time() > 6) {
                        stage = 4;
                        time.reset();
                    }
                    kicker.stepKickerDown();
                }
            }
        }
        if (stage == 2) {
            ColorUtils.Color readColor = getColorSensorColor(kicker.colorSensor);
            double distance = kicker.colorRangeSensor.getDistance(CM);
            if (distance <= ballDistance) {
                if (readColor.equals(ColorUtils.Color.NONE)) {
                    computerColor = ColorUtils.Color.BLUE;
                } else {
                    computerColor = readColor;
                }
                if (alliance.equals("Red")) {
                    if (computerColor.equals(ColorUtils.Color.RED)) {
                        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.KNOCKLEFT);
                        stage++;
                        time.reset();
                    }
                    if (computerColor.equals(ColorUtils.Color.BLUE)) {
                        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.KNOCKRIGHT);
                        stage++;
                        time.reset();
                    }
                }
                if (alliance.equals("Blue")) {
                    if (computerColor.equals(ColorUtils.Color.RED)) {
                        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.KNOCKRIGHT);
                        stage++;
                        time.reset();
                    }
                    if (computerColor.equals(ColorUtils.Color.BLUE)) {
                        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.KNOCKLEFT);
                        stage++;
                        time.reset();
                    }
                }
            }
            if (time.time() > 3.5) {
                if (time.time() > 0.75) {
                    stage++;
                    time.reset();
                }
            }
        }
        if (stage == 3) {
            stage++;
            time.reset();
        }
        if (stage == 4) {
            kicker.setJewelKickerPositionX(JewelKicker.ServoPosition.MIDDLEJEWEL, servoStepperTime, deltaPosition, deltaTime);
            if (time.time() > 1) {
                time.reset();
                stage++;
            }
        }

        if (stage == 5) {
            kicker.setJewelKickerPosition(JewelKicker.ServoPosition.TELEOP);
            stage++;
        }
    }

    @Override
    public boolean isCompleted() {
        if (stage >= 6) {
            completed();
        } else {
            run();
        }
        return stage >= 6;
    }

    @Override
    public void completed() {
        opMode.next(); // Go to next stage
        teardown();
    }

    public static void teardown() {
        instance = null;
    }

    private ColorUtils.Color getColorSensorColor(ColorSensor sensor) {
        ColorUtils.Color returnColor = ColorUtils.Color.NONE;
        if ((sensor.red() > sensor.green() + 4) && (sensor.red() > sensor.blue() + 4)) {
            returnColor = ColorUtils.Color.RED;
        }
        if ((sensor.blue() > sensor.red() + 6) && (sensor.blue() > sensor.green() + 6)) {
            returnColor = ColorUtils.Color.BLUE;
        }
        return returnColor;
    }

}
