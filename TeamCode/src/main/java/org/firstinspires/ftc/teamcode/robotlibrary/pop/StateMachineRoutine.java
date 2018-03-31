package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class StateMachineRoutine implements Routine, StateMachine {
    int stage = 0;
    ElapsedTime time;

    @Override
    public void next() {
        stage++;
        if (time == null) time = new ElapsedTime();
        time.reset();
    }
}
