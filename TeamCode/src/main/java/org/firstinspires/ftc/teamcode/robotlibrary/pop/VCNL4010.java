package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;

/**
 * Created by Dynamic Signals on 10/10/2017.
 */

@I2cSensor(name = "VCNL4010 Prox sensor", description = "Proximity sensor", xmlTag = "VCNL4010")
public class VCNL4010 extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    public VCNL4010(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);

        this.deviceClient.setI2cAddress(I2cAddr.create7bit(VCNL4010_Addresses.I2CADDR_DEFAULT / 2));

        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    protected boolean doInitialize() {

        setLedCurrent(20);
        setFrequency(VCNL4010_Frequency.VCNL4010_390K625);

        return true;
    }

    public void setLedCurrent(int i) {
        write(VCNL4010_Addresses.IRLED, (short) Range.clip(i, 0, 20));
    }

    public int getLedCurrent() {
        return read8(VCNL4010_Addresses.IRLED);
    }

    void setFrequency(VCNL4010_Frequency frequency) {
        int r = read8(VCNL4010_Addresses.MODTIMING);
        r &= ~(0b00011000);
        r |= frequency.getCode() << 3;
        write(VCNL4010_Addresses.MODTIMING, (short) r);
    }

    public int getProximity() {
        write(VCNL4010_Addresses.COMMAND, (short) VCNL4010_Addresses.MEASUREPROXIMITY);
        return TypeConversion.byteArrayToInt(readBlock(VCNL4010_Addresses.PROXIMITYDATA, 2), ByteOrder.BIG_ENDIAN);
    }

    public int getAmbient() {
        write(VCNL4010_Addresses.COMMAND, (short) VCNL4010_Addresses.MEASUREAMBIENT);
        return TypeConversion.byteArrayToInt(readBlock(VCNL4010_Addresses.AMBIENTDATA, 2), ByteOrder.BIG_ENDIAN);
    }

    public int getRevision() {
        return read8(VCNL4010_Addresses.PRODUCTID);
    }


    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Adafruit;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    protected void write(final int reg, short value) {
        deviceClient.write(reg, TypeConversion.shortToByteArray(value));
    }

    protected short read(int reg) {
        return TypeConversion.byteArrayToShort(deviceClient.read(reg, 2));
    }

    protected byte read8(int reg) {
        return deviceClient.read8(reg);
    }

    protected byte[] readBlock(int reg, int creg) {
        return deviceClient.read(reg, creg);
    }

    public final class VCNL4010_Addresses {
        public final static int I2CADDR_DEFAULT = 0x13;
        public final static int COMMAND = 0x80;
        public final static int PRODUCTID = 0x81;
        public final static int PROXRATE = 0x82;
        public final static int IRLED = 0x83;
        public final static int AMBIENTPARAMETER = 0x84;
        public final static int AMBIENTDATA = 0x85;
        public final static int PROXIMITYDATA = 0x87;
        public final static int INTCONTROL = 0x89;
        public final static int PROXINITYADJUST = 0x8A;
        public final static int INTSTAT = 0x8E;
        public final static int MODTIMING = 0x8F;
        public final static int MEASUREAMBIENT = 0x10;
        public final static int MEASUREPROXIMITY = 0x08;
        public final static int AMBIENTREADY = 0x40;
        public final static int PROXIMITYREADY = 0x20;

    }

    public enum VCNL4010_Frequency {
        VCNL4010_3M125(3),
        VCNL4010_1M5625(2),
        VCNL4010_781K25(1),
        VCNL4010_390K625(0);

        int code;

        VCNL4010_Frequency(int code) {
            this.code = code;
        }

        public int getCode() {return code;}
    }
}
