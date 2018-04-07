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
    private boolean nextStage = true;
    static int maxCounts = 1588;
    private LiftPosition targetPosition;
    ElapsedTime time;

    int difference;

    double power = 1;

    // Controls all w trigger
    // D-pad left - low position right off ground
    // D-pad up - level up
    // D-pad down - ground level

    public enum LiftPosition {
        GROUND(5),
        FIRST(450),
        SECOND(GROUND.position),
        FOURTH(GROUND.position),
        TOP(1500);

        private int position; // Array containing data

        LiftPosition(int counts) { // Constructor
            this.position = counts;
        }

        public int getPosition() { // Return data
            return position;
        }

    }

    public static LiftToPosition movePosition(Autonomous autonomous, LiftPosition position, boolean nextStage) {
        if (instance == null) {
            instance = new LiftToPosition(autonomous, autonomous.lift, position, nextStage);
        }
        instance.isCompleted();
        return instance;
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
            instance = new LiftToPosition(opMode, lift, position, true);
        }
        instance.isCompleted();
        return instance;
    }

    private LiftToPosition(StateMachineOpMode opMode, Lift lift, LiftPosition position, boolean nextStage) {
        this.opMode = opMode;
        this.lift = lift;
        this.nextStage = nextStage;

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        difference = position.getPosition() - lift.LiftMotor.getCurrentPosition();
        // Positive (must go up) (positive power)
        // Negative (must go down) (negative power)

        targetPosition = position;

        time = new ElapsedTime();
    }

    @Override
    public void run() {

        lift.setPower((power > 0 && difference < 0) ? -1 * power * 0.8 : power);

    }

    @Override
    public boolean isCompleted() {
        boolean completed = false;
        int current = lift.LiftMotor.getCurrentPosition();
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
        if (nextStage) opMode.next();
        teardown();
    }

    public static void teardown() {
        instance = null;
    }
}