
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
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

        telemetry.addData("Relic Pictograph", vuforiaSystem.getVuMark());

        switch (vuforiaSystem.getVuMark()) {
            case CENTER:
                break;
        }

        telemetry.addData("Brown", ((VuforiaTrackableDefaultListener) vuforiaSystem.brownCube.getListener()).isVisible());
        telemetry.addData("Grey", ((VuforiaTrackableDefaultListener) vuforiaSystem.greyCube.getListener()).isVisible());

    }
}
