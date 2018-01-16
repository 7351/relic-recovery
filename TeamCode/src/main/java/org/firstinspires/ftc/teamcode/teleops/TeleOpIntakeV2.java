package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOpIntakeV2")
@Disabled
public class TeleOpIntakeV2 extends TeleOp {

    @Override
    public void init() {
        super.init();
        intakev2 = true;
    }
}
