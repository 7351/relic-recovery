package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ScoreGlyph extends StateMachineRoutine {

    private static ScoreGlyph instance;
    private Autonomous autonomous;

    public static ScoreGlyph scoreGlyph(Autonomous autonomous) {
        if (instance == null) {
            instance = new ScoreGlyph(autonomous);
        }
        instance.isCompleted();
        return instance;
    }

    private ScoreGlyph(Autonomous autonomous) {
        this.autonomous = autonomous;
        time = new ElapsedTime();
    }

    @Override
    public void run() {
        // Drive to the correct distance away from the cyrptobox
        if (stage == 0) {
            RoutineEncoderDrive.createDrive(autonomous, this, 200);
        }

        // Put the ramp up
        if (stage == 1) {
            autonomous.lift.setRampPosition(Lift.RampServoPosition.SCORE);
            if (time.time() > 1) {
                next();
            }
        }

        // Drive back to get ready to push back in
        if (stage == 2) {
            RoutineEncoderDrive.createDrive(autonomous, this, -100);
        }

        // Push back in
        if (stage == 3) {
            RoutineEncoderDrive.createDrive(autonomous, this, 220);
        }

        // Drive back out
        if (stage == 4) {
            RoutineEncoderDrive.createDrive(autonomous, this, -200);
        }

        if (stage == 5) {
            autonomous.lift.setRampPosition(Lift.RampServoPosition.HOME);
            next();
        }
    }

    @Override
    public boolean isCompleted() {
        boolean completed = stage > 5;
        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        autonomous.next();
        teardown();
    }

    private void teardown() {
        instance = null;
    }


}