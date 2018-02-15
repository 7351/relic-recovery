package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

/**
 * Created by Leo on 11/21/2017.
 */

public abstract class Autonomous extends StateMachineOpMode {

    OpModeManagerImpl manager;

    public String alliance;
    public VuforiaSystem vuforiaSystem;
    public Intake intake;
    public Lift lift;
    public ColorUtils colorUtils;
    public GyroUtils gyroUtils;
    public JewelKicker kicker;
    public RelicRecoveryVuMark relicRecoveryVuMark;
    AutoTransitioner autoTransitioner;

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    @Override
    public void init() {

        vuforiaSystem = new VuforiaSystem();
        colorUtils = new ColorUtils(this);
        kicker = new JewelKicker(this);
        gyroUtils = GyroUtils.getInstance(this);
        intake = new Intake(this);
        lift = new Lift(this);
        lift.setGlyphGrabberPosition(Lift.GripperServoPosition.GRIP);

        autoTransitioner = new AutoTransitioner();
        autoTransitioner.setNewTransition(this, "TeleOp");

        /* To be implemented in the future
        if (getClass().getName().contains("R")) {
            alliance = "Red";
        }
        if (getClass().getName().contains("B")) {
            alliance = "Blue";
        }*/

        CameraDevice.getInstance().setFlashTorchMode(true);

    }

    @Override
    public void init_loop() {
        telemetry.addData("VuMark (Not Saved)", vuforiaSystem.getVuMark().toString().toLowerCase());
    }

    @Override
    public void stop() {
        manager = (OpModeManagerImpl) internalOpModeServices;
        manager.initActiveOpMode("TeleOp");
        CameraDevice.getInstance().setFlashTorchMode(false);
    }

}
