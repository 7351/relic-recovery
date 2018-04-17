package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "B2TwoGlyph", group = "A-Team")
public class B2TwoGlyph extends B2OneGlyph {
    @Override
    public void init() {
        super.init();
        twoGlyph = true;
    }
}
