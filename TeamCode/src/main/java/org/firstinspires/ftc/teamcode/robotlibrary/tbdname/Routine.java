package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

/**
 * Created by Dynamic Signals on 12/29/2016.
 */

public interface Routine {
    double GearRatio = 40;
    double SprocketRatio = 1.5;

    void run();
    boolean isCompleted();
    void completed();
}
