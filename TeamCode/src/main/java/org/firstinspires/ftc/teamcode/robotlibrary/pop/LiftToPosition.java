package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by leo on 3/25/17.
 */

public class LiftToPosition implements Routine {

    private static LiftToPosition instance;

    private Lift lift;
    private StateMachineOpMode opMode;
    static int maxCounts = 1588;
    private LiftPosition targetPosition;
    ElapsedTime time;

    int difference;

    double power = 0.5;

    // Controls all w trigger
    // D-pad left - low position right off ground
    // D-pad up - level up
    // D-pad down - ground level

    public enum LiftPosition {
        GROUND(5),
        FIRST(216),
        SECOND(GROUND.position),
        FOURTH(GROUND.position),
        TOP(1150);

        private int position; // Array containing data

        LiftPosition(int counts) { // Constructor
            this.position = counts;
        }

        public int getPosition() { // Return data
            return position;
        }

    }

    /**
     * Constructor for percent
     *
     * @param opMode   The OpMode that will be passed, usually "this"
     * @param position position Encoder counts to move lift to, will be clipped too if not within range
     * @return LiftToPosition instance
     */
    public static LiftToPosition movePosition(StateMachineOpMode opMode, Lift lift, LiftPosition position) {
        if (instance == null) {
            instance = new LiftToPosition(opMode, lift, position);
        }
        instance.isCompleted();
        return instance;
    }

    private LiftToPosition(StateMachineOpMode opMode, Lift lift, LiftPosition position) {
        this.opMode = opMode;
        this.lift = lift;

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        difference = position.getPosition() - lift.getAveragePosition();
        // Positive (must go up) (positive power)
        // Negative (must go down) (negative power)

        targetPosition = position;

        time = new ElapsedTime();
    }

    @Override
    public void run() {

        lift.setPower((power > 0 && difference < 0) ? -1 * power * 0.35 : power);

    }

    @Override
    public boolean isCompleted() {
        boolean completed = false;
        int current = lift.getAveragePosition();
        if (difference < 0) { // We need to go down
            if (current <= targetPosition.getPosition()) completed = true;
        } else { // We need to go up
            if (current >= targetPosition.getPosition()) completed = true;
        }
        if (time.time() > 3) {
            completed = true;
        }
        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        lift.setPower(0);
        opMode.next();
        teardown();
    }

    public static void teardown() {
        instance = null;
    }
}