
package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.StateMachineOpMode;
import org.firstinspires.ftc.teamcode.robotlibrary.tbdname.VuforiaSystem;

@Autonomous(name = "Concept: VuMark Id", group = "Concept")
public class ConceptVuMarkIdentification extends StateMachineOpMode {

    VuforiaSystem vuforiaSystem;

    @Override
    public void init() {

        vuforiaSystem = new VuforiaSystem();

    }

    @Override
    public void loop() {

        telemetry.addData("Relic Pictograph", vuforiaSystem.getVuMark()); // get the current status of the pictograph.
        // Represented as an enum. Options are: UNKNOWN, LEFT, CENTER, RIGHT

        switch (vuforiaSystem.getVuMark()) {
            case CENTER:
                // Run code for storing in center
                break;
            case LEFT:
                // Run code for storing in left
                break;
            case RIGHT:
                // Run code for storing in right
                break;
        }

    }
}
