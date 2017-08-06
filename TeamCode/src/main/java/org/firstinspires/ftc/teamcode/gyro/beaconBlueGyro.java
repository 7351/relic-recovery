package org.firstinspires.ftc.teamcode.gyro;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "beaconBlueGyro", group = "BNotWorking")
@Disabled
public class beaconBlueGyro extends StateMachineOpMode {
    ColorUtils.Color actedColor;

    DriveTrain driveTrain;
    ColorUtils colorUtils;
    BeaconUtils beaconUtils;
    Intake intake;
    FlyWheel flyWheel;
    RangeUtils rangeUtils;
    AHRS navx;

    EncoderDrive drive;
    EncoderTurn turn;

    boolean capBallGet = false;

    /* Selector variables */
    private String alliance = "Blue";
    private String beaconAmount = "2";
    private int shoot = 2;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        rangeUtils = new RangeUtils(hardwareMap);
        new Lift(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);

    }

    @Override
    public void start() {
        super.start();
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {

        if (stage == 0) { //Gyro Calibration
            if (!navx.isCalibrating()) {
                navx.zeroYaw();
                stage++;
            }
        }

        if (stage == 1) {//drive forward X cm/825 ticks to shooting position&start flywheel
            NewEncoderDrive.createDrive(this, 825);
            if (shoot > 0) {
                flyWheel.currentPower = flyWheel.defaultStartingPower;
                flyWheel.currentlyRunning = true;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 2) {//shoot particles and wait 2.5 seconds
            if (shoot == 1) {
                intake.setIntake(Intake.IntakeSpec.A, Intake.IntakeDirection.IN);
            }
            if (shoot == 2) {
                intake.setIntake(Intake.IntakeSpec.BOTH, Intake.IntakeDirection.IN);
            }
            if (time.time() > 2.5 || shoot <= 0) {
                next();
                intake.stopIntake(Intake.IntakeSpec.BOTH);
                intake.setIntake(Intake.IntakeSpec.A, Intake.IntakeDirection.OUT);
                flyWheel.currentlyRunning = false;
            }
        }

        if (stage == 3) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 4) { // Drive backwards X cm/625 ticks to get a better angle at the white line
            NewEncoderDrive.createDrive(this, -625, 0.2);
        }

        if (stage == 5) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 6) {// Turn 27 degrees to point at the white line for Beacon 1
            EncoderGyroTurn.createTurn(this, 40.5);
        }
        if (stage == 7) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 8) { // Drive until the color sensor sees the white line of Beacon 1
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3900, 0.45);
            }
            drive.runWithDecrementPower(0.0003); //slows down gradually to hit white line
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage = 222;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                stage = 908;
                drive = null;
                driveTrain.stopRobot();
                //AutonomousUtils.failSafeError(hardwareMap);
            }
        }
        if (stage == 222) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -500, 0.4);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                drive = null;
                stage = 9;
            }
        }
        if (stage == 9) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 10) { // Turn 32 degrees to face beacon 1
            EncoderGyroTurn.createTurn(this, 86); // This might not work, so you might need to change the degree
        }
        if (stage == 11) { // Wait
            if (time.time() > .35) {
                next();
            }
        }
        if (stage == 12) { // Drive until we see a beacon color
            if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 20) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(0.25);
                driveTrain.powerRight(0.25);
            } else {
                RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                next();
            }
        }

        if (stage == 13) { // Wait
            if (time.time() > 0.75) {
                next();
            }
        }

        if (stage == 14) { // Act on beacon with color sensor/Flip the sunglasses
            if (!colorUtils.beaconColor().equals(ColorUtils.Color.NONE)) {
                actedColor = beaconUtils.actOnBeaconWithColorSensor();
                stage++;
                time.reset();
            } else {
                stage = AutonomousUtils.DEADBEEF;
                AutonomousUtils.failSafeError(hardwareMap);
                time.reset();
            }

        }
        if (stage == 15) {// Get the range to the wall in cm + 50 ticks more, set encoders and drive to the wall
            if (drive == null) {
                int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19; // Get the distance to the wall in enc counts, -4 ajusts for chaisi
                counts += 300;
                drive = new EncoderDrive(driveTrain, counts + 25, 0.225); // Just a little umph to hit the button
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) { // Time failsafe just in case we need to bail
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }


        if (stage == 16) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 1
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                turn = new EncoderTurn(driveTrain, 8, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.3) {
                turn.completed();
                next();
            }
        }


        if (stage == 17) { // Make a 6 degree turn (wiggle back) to attempt to straighten up on beacon 1
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.COUNTERCLOCKWISE : GyroUtils.Direction.CLOCKWISE;
                turn = new EncoderTurn(driveTrain, 8, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.3) {
                turn.completed();
                next();
            }
        }

        if (stage == 18) { //Back up from Beacon 13cm with Range Sensor to prepare for turn
            if (rangeUtils.getDistance(DistanceUnit.CM, -1) <= 13) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(-0.55);
                driveTrain.powerRight(-0.55);

            } else {
                driveTrain.stopRobot();
                beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
                if (beaconAmount.equals("1")) stage = AutonomousUtils.COMPLETED;
                if (beaconAmount.equals("2")) stage++;
            }
        }

        if (stage == 19) { // Turn 73 degrees to point at the white line for Beacon 2
            EncoderGyroTurn.createTurn(this, 0);
        }

        if (stage == 20) { //wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 21) { // Drive until the color sensor sees the white line of Beacon 2
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3100, 0.45);
            }
            drive.runWithDecrementPower(0.000325); // slows down gradually to hit white line
            if (colorUtils.aboveWhiteLine() && Math.abs(driveTrain.RightFrontMotor.getCurrentPosition()) > 1000) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                stage = AutonomousUtils.DEADBEEF;
                driveTrain.stopRobot();
                AutonomousUtils.failSafeError(hardwareMap);
            }
        }

        if (stage == 22) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 23) { // Back up x cm/75 Ticks since we ran past the while line
            NewEncoderDrive.createDrive(this, -75, 0.3);
        }

        if (stage == 24) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 25) { // Turn 71.5 degrees to face beacon 2
            EncoderGyroTurn.createTurn(this, 90);
        }

        if (stage == 26) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 27) { // Drive until we see a beacon color
            if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 20) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(0.25);
                driveTrain.powerRight(0.25);
            } else {
                RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 28) { // Act on beacon with color sensor/Flip the sunglasses
            if (!colorUtils.beaconColor().equals(ColorUtils.Color.NONE)) {
                beaconUtils.actOnBeaconWithColorSensor();
                actedColor = beaconUtils.actOnBeaconWithColorSensor();
                stage++;
                time.reset();
            } else {
                stage = 626;
                AutonomousUtils.failSafeError(hardwareMap);
                time.reset();
            }

        }

        if (stage == 29) { // Get the range to the wall in cm + X ticks more, set encoders and drive to the wall
            if (drive == null) {
                int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19; // Get the distance to the wall in enc counts, -4 ajusts for chaisi
                counts += 250;
                drive = new EncoderDrive(driveTrain, counts + 25, 0.225); // Just a little umph to hit the button
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) { // Time failsafe just in case we need to bail
                next();
            }
        }


        if (stage == 30) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 6
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                turn = new EncoderTurn(driveTrain, 10, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.3) {
                next();
            }
        }

        if (stage == 31) { //wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 32) { ///Back up 10 cm with prox to prepare for turn
            if (rangeUtils.getDistance(DistanceUnit.CM, -1) <= 10) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(-0.55);
                driveTrain.powerRight(-0.55);
            } else {
                driveTrain.stopRobot();
                beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
                if (capBallGet) stage++;
                if (!capBallGet) stage = AutonomousUtils.COMPLETED;
                time.reset();
            }
        }
        if (stage == 33) {//Turn 31 degrees to point at cap ball
            EncoderGyroTurn.createTurn(this, -135);
        }
        if (stage == 34) {//drive to X cm/-3100 ticks to hit cap ball and park
            NewEncoderDrive.createDrive(this, 3900, 1);
        }
        if (stage == 908) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 80, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                turn = null;
                time.reset();
            }
        }

        if (stage == 909) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -3600, 0.25);
                drive.run();
            }
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                stage = 9909;
                drive = null;
                turn = null;
                driveTrain.stopRobot();
                AutonomousUtils.failSafeError(hardwareMap);
            }
        }
        if (stage == 910) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 130, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                drive = null;
                turn = null;
                stage = 27;
            }
        }


        telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Range", rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Beacon", colorUtils.beaconColor().toString());
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Time", String.valueOf(time.time()));
    }

    @Override
    public void next() {
        super.next();
        time.reset();
        drive = null;
        turn = null;
        driveTrain.stopRobot();
    }
}

