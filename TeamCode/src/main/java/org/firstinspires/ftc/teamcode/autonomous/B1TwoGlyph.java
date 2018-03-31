package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name = "B1TwoGlyph", group = "A-Team")
public class B1TwoGlyph extends B1OneGlyph {
    @Override
    public void init() {
        super.init();
        twoGlyph = true;
    }
}
