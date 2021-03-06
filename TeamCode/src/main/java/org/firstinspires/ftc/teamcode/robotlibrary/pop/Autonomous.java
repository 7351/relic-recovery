package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

/**
 * Created by Leo on 11/21/2017.
 */

public abstract class Autonomous extends StateMachineOpMode {

    OpModeManagerImpl manager;

    public int startingPosition = CLOSE;
    public final static int CLOSE = 1; // Closer to the relic scoring zone
    public final static int FAR = 2; // Farther away from scoring zone

    public String alliance;
    public VuforiaSystem vuforiaSystem;
    public Intake intake;
    public Lift lift;
    public ColorUtils colorUtils;
    public GyroUtils gyroUtils;
    public JewelKicker kicker;
    public DriveTrain driveTrain;
    public RelicRecoveryVuMark relicRecoveryVuMark, initialVuMark;
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
        driveTrain = new DriveTrain(this);

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

    public void getVuMark() {
        RelicRecoveryVuMark scannedVuMark = vuforiaSystem.getVuMark();
        if (!scannedVuMark.equals(RelicRecoveryVuMark.UNKNOWN)) {
            relicRecoveryVuMark = scannedVuMark;
        }
    }

    @Override
    public void stop() {
        kicker.setJewelKickerPosition(JewelKicker.ServoPosition.INROBOT);
        manager = (OpModeManagerImpl) internalOpModeServices;
        manager.initActiveOpMode("TeleOp");
        CameraDevice.getInstance().setFlashTorchMode(false);
    }

}
