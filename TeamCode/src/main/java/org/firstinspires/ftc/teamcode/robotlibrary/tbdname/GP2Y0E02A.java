package org.firstinspires.ftc.teamcode.robotlibrary.tbdname;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Dynamic Signals on 10/24/2017.
 */

@I2cSensor(name = "GP2Y0E02A IR Dist Sensor", description = "IR Distance sensor by Sharp", xmlTag = "GP2Y0E02A")
public class GP2Y0E02A extends I2cDeviceSynchDevice<I2cDeviceSynch> implements DistanceSensor {

    private int shift = 0;

    public GP2Y0E02A(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);

        this.deviceClient.setI2cAddress(I2cAddr.create7bit(GP2Y0E02A_Addresses.I2CADDR_DEFAULT / 2));

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        byte[] distanceRaw = deviceClient.read(GP2Y0E02A_Addresses.DISTANCE_ADDR, 2);
        int shift = deviceClient.read8(GP2Y0E02A_Addresses.SHIFT_ADDR);
        int cm = (distanceRaw[0] * 16 + distanceRaw[1]) / 16 / (2^shift);
        switch (unit) {
            case CM:
                return cm;
            case MM:
                return cm*10;
            case INCH:
                return cm*0.393701;
            case METER:
                return cm/100;
            default:
                return DistanceUnit.infinity;
        }
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Unknown;
    }

    @Override
    public String getDeviceName() {
        return deviceClient.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    protected boolean doInitialize() {
        shift = deviceClient.read8(GP2Y0E02A_Addresses.SHIFT_ADDR);
        return true;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {
        deviceClient.close();
    }

    public final class GP2Y0E02A_Addresses {
        private final static int I2CADDR_DEFAULT = 0x80;
        private final static int SHIFT_ADDR = 0x35;
        private final static int DISTANCE_ADDR = 0x5E;
        private final static int RIGHT_EDGE_ADDR = 0xF8; // C
        private final static int LEFT_EDGE_ADDR = 0xF9; // A
        private final static int PEAK_EDGE_ADDR = 0xFA; // B
    }
}
