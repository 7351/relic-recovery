package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ScoreGlyph extends StateMachineRoutine {

    private static ScoreGlyph instance;
    private Autonomous autonomous;
    private boolean secondPush = true;


    public static ScoreGlyph scoreGlyph(Autonomous autonomous, boolean secondPush) {
        if (instance == null) {
            instance = new ScoreGlyph(autonomous, secondPush);
        }
        instance.isCompleted();
        return instance;
    }

    public static ScoreGlyph scoreGlyph(Autonomous autonomous) {
        return scoreGlyph(autonomous, true);
    }

    private ScoreGlyph(Autonomous autonomous, boolean secondPush) {
        this.autonomous = autonomous;
        this.secondPush = secondPush;
        time = new ElapsedTime();
    }

    @Override
    public void run() {
        // Drive to the correct distance away from the cyrptobox
        if (stage == 0) {
            RoutineEncoderDrive.createDrive(autonomous, this, 150);
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
            RoutineEncoderDrive.createDrive(autonomous, this, 75);
        }

        if (stage == 3) {
            RoutineEncoderDrive.createDrive(autonomous, this, -150);
        }

        if (stage == 4 && secondPush) {
            stage++;
        } else if (stage == 4 && !secondPush) {
            stage = 7;
        }

        // Push back in
        if (stage == 5) {
            RoutineEncoderDrive.createDrive(autonomous, this, 220);
        }

        // Drive back out
        if (stage == 6) {
            RoutineEncoderDrive.createDrive(autonomous, this, -200);
        }

        if (stage == 7) {
            autonomous.lift.setRampPosition(Lift.RampServoPosition.HOME);
            next();
        }
    }

    @Override
    public boolean isCompleted() {
        boolean completed = stage > 6;
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