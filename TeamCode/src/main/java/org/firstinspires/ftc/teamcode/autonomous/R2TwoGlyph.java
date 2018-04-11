package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "R2TwoGlyph", group = "A-Team")
public class R2TwoGlyph extends R2OneGlyph {
    @Override
    public void init() {
        super.init();
        twoGlyph = true;
    }
}
