package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "R1TwoGlyph", group = "A-Team")
public class R1TwoGlyph extends R1OneGlyph {
    @Override
    public void init() {
        super.init();
        twoGlyph = true;
    }
}
