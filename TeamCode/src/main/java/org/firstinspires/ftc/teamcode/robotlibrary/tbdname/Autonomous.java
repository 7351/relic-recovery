package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

/**
 * Created by Leo on 11/21/2017.
 */

public abstract class Autonomous extends StateMachineOpMode {

    OpModeManagerImpl manager;

    public String alliance;
    //public VuforiaSystem vuforiaSystem;
    //public RangeUtils rangeUtils;
    public Intake intake;
    public Lift lift;
    public ColorUtils colorUtils;
    public GyroUtils gyroUtils;
    public JewelKicker kicker;
    public RelicRecoveryVuMark relicRecoveryVuMark;
    AutoTransitioner autoTransitioner;
    public ColorUtils.Color jewelColor;
    public int amountOfColumns;

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    @Override
    public void init() {

        //vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
        kicker = new JewelKicker(this);
        gyroUtils = GyroUtils.getInstance(this);
        intake = new Intake(this);
        //rangeUtils = new RangeUtils(hardwareMap);
        lift = new Lift(this);
        lift.setGlyphGrabberPosition(Lift.ServoPosition.CLOSED);

        autoTransitioner = new AutoTransitioner();
        autoTransitioner.setNewTransition(this, "TeleOp");

    }

    @Override
    public void stop() {
        manager = (OpModeManagerImpl) internalOpModeServices;
        manager.initActiveOpMode("TeleOp");
    }

}
