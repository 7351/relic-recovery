package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by leo on 3/25/17.
 */

public class LiftToPosition implements Routine {

    private static LiftToPosition instance;

    private Lift lift;
    private StateMachineOpMode opMode;
    static int maxCounts = 1588;

    int[] initialPositions;
    int difference;

    double power = 0.75;

    // 175 - Position low
    // 640 - Position 2nd next
    // 1173 - Position 3rd next (2nd highest)
    // 1550 - Position 4th (highest)
    // Controls all w trigger
    // D-pad left - low position right off ground
    // D-pad up - level up
    // D-pad down - ground level

    public enum LiftPosition {
        GROUND(20),
        FIRST(175),
        SECOND(640),
        THIRD(1173),
        FOURTH(1550);

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
    public static LiftToPosition moveCount(StateMachineOpMode opMode, Lift lift, LiftPosition position) {
        if (instance == null) {
            instance = new LiftToPosition(opMode, lift, position);
        }
        instance.isCompleted();
        return instance;
    }

    private LiftToPosition(StateMachineOpMode opMode, Lift lift, LiftPosition position) {
        this.opMode = opMode;
        this.lift = lift;

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        initialPositions = lift.getCurrentPositions();
        int averageCurrent = (initialPositions[0] + initialPositions[1]) / 2;

        difference = position.getPosition() - averageCurrent;
        // Positive (must go up) (positive power)
        // Negative (must go down) (negative power)

        lift.setTargetPosition(position.getPosition());

    }

    @Override
    public void run() {

        lift.setPower((power > 0 && difference < 0) ? -1 * power : power);

    }

    @Override
    public boolean isCompleted() {
        boolean completed = !lift.isBusy();
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
        instance = null;
    }

    public static void teardown() {
        instance = null;
    }
}