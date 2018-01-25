package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

public class VuforiaSystem {


    public VuforiaLocalizer vuforia;
    public VuforiaTrackable relicTemplate;
    public VuforiaTrackables relicTrackables;

    /* Vuforia Preferences */
    public static final String VUFORIA_LICENSE_KEY = "AbXnxf//////AAAAGRuNC5J8ZEyftEBQmHGLn/JRAsckJezlsbt+FqzEIevPs5nHoqNr8RxWAOXkyTKIYfEkL17legkgm4sV7qv3qcJXlVQE1Xlo/UKbwVQBgzEfGZi9M3d3tgaJNLEeDe1VLXCVrGyrGSThbd364UF/+nsZMhnFGcnLavxaH8N0QWS5QiAgdbV71V4SLS2vWzML4leBiAxl8qqitSqHEmlez4xF5BoyADuT3lLanURW+g+guX7jFo8ONDzI+xjBsi5BCnI41USBfJdhRnh272sUgdpJFetdTQKIlvRifwHOzGz9oX1WpFSOid+NE76fLon5sHVRx4ztQrqBtSQN3J9CgaJo0DjkDyTMbJBTTE56n2Yi";
    private static final VuforiaLocalizer.CameraDirection DIRECTION = VuforiaLocalizer.CameraDirection.BACK;
    private static final String TEMPLATE_NAME = "RelicVuMark";

    /**
     * Constructor with the initialization of the Vuforia variable.
     */

    public VuforiaSystem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (vuforia == null) {
                    VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
                    vuforiaParameters.cameraDirection = DIRECTION;
                    vuforiaParameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
                    vuforia = ClassFactory.createVuforiaLocalizer(vuforiaParameters);
                } else {
                    RobotLog.a("Vuforia already initialized!");
                }
                relicTrackables = vuforia.loadTrackablesFromAsset(TEMPLATE_NAME);
                relicTemplate = relicTrackables.get(0);
                relicTrackables.activate();
            }
        }).run();
    }

    /**
     * Borrowed from FTC Team 492 for OpenGLMatrix Constructor
     * This method creates a location matrix that can be used to relocate an object to its final location
     * <p>
     * by rotating and translating the object from the origin of the field. It is doing the operation in
     * <p>
     * the order of the parameters. In other words, it will first rotate the object on the X-axis, then
     * <p>
     * rotate on the Y-axis, then rotate on the Z-axis, then translate on the X-axis, then translate on
     * <p>
     * the Y-axis and finally translate on the Z-axis.
     *
     * @param rotateX    specifies rotation on the X-axis.
     * @param rotateY    specifies rotation on the Y-axis.
     * @param rotateZ    specifies rotation on the Z-axis.
     * @param translateX specifies translation on the X-axis.
     * @param translateY specifies translation on the Y-axis.
     * @param translateZ specifies translation on the Z-axis.
     * @return returns the location matrix.
     */

    public OpenGLMatrix locationMatrix(float rotateX, float rotateY, float rotateZ, float translateX, float translateY, float translateZ) {
        return OpenGLMatrix.translation(translateX, translateY, translateZ)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, rotateX, rotateY, rotateZ));

    }

    public RelicRecoveryVuMark getVuMark() {
        return RelicRecoveryVuMark.from(relicTemplate);
    }

    public VectorF getVuMarkLocation() {
        RelicRecoveryVuMark vuMark = getVuMark(); // Get the RelicRecoveryVuMark and if it's visible
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) { // If it's visible
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
            if (pose != null) {
                return pose.getTranslation(); // Return the VectorF translation
            }
        }
        return new OpenGLMatrix().getTranslation(); // Gracefully create an empty matrix so we don't have to return a null value
    }

    public double distanceBetweenTargets(OpenGLMatrix PhoneLocationMatrix, OpenGLMatrix TargetLocationMatrix) {
        VectorF phoneVector = PhoneLocationMatrix.getTranslation();
        VectorF targetVector = TargetLocationMatrix.getTranslation();
        double[] orderedPairPhone = {phoneVector.get(0), phoneVector.get(2)};
        double[] orderedPairTarget = {targetVector.get(0), phoneVector.get(2)};
        double differenceX = orderedPairPhone[0] - orderedPairTarget[0];
        double differenceZ = orderedPairPhone[1] - orderedPairTarget[1];
        return Math.abs(Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceZ, 2)));
    }


}
