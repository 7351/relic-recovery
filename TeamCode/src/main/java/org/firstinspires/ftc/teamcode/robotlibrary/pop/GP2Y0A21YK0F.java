package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Dynamic Signals on 10/29/2017.
 */

public class GP2Y0A21YK0F implements DistanceSensor {

    AnalogInput input;

    public GP2Y0A21YK0F(StateMachineOpMode opMode) {
        input = opMode.hardwareMap.analogInput.get("analogDistance");
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        double cm = 12.48126278*Math.pow(input.getVoltage(),-1.064538346);
        switch (unit) {
            case CM:
                return cm;
            case MM:
                return cm * 10;
            case INCH:
                return cm * 0.393701;
            case METER:
                return cm / 100;
            default:
                return DistanceUnit.infinity;
        }
    }

    @Override
    public Manufacturer getManufacturer() {
        return input.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return input.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return input.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return input.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {
        input.close();
    }
}
