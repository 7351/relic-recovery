package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

/**
 * Created by leo on 3/18/17.
 */

public abstract class StateMachineOpMode extends OpMode implements StateMachine {

    public double stage = 0;
    public ElapsedTime time = new ElapsedTime();
    public ElapsedTime matchTime = new ElapsedTime();
    public boolean telemetryEnabled = true;
    public Type classType;

    public static enum Type {
        AUTONOMOUS,
        TELEOP
    }

    public void start() {
        time.reset();
        matchTime.reset();
    }

    public void next() {
        stage++;
        time.reset();
    }

    public void waitStage(double stage, double time) {
        if (this.stage == stage) {
            if (this.time.time() > time) {
                next();
            }
        }
    }

    public void waitStage(double stage) {
        if (this.stage == stage) {
            if (this.time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
    }
}

