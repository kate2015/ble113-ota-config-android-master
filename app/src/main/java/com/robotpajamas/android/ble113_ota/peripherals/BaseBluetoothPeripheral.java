package com.robotpajamas.android.ble113_ota.peripherals;

import android.support.annotation.NonNull;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethUtils;
import com.robotpajamas.android.ble113_ota.listeners.OnBondingChangedListener;
import com.robotpajamas.android.ble113_ota.listeners.OnCharacteristicReadListener;
import com.robotpajamas.android.ble113_ota.listeners.OnCharacteristicWriteListener;
import com.robotpajamas.android.ble113_ota.listeners.OnConnectionChangedListener;

import java.util.UUID;

public class BaseBluetoothPeripheral {

    // Using standard 16bit UUIDs, transformed into the correct 128-bit UUID
    private static final UUID SERVICE_DEVICE_INFORMATION = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    private static final UUID CUSTOM_SERVICE_INFORMATION = UUID.fromString("8409f408-63e3-4eea-aaf4-762ddec5319c");

    private static final UUID CHARACTERISTIC_SERIAL_NUMBER = UUID.fromString("510f4784-5ecb-4c0f-92e3-2d7a9e28b71c");
    private static final UUID CHARACTERISTIC_FIRMWARE_VERSION = UUID.fromString("6240cbea-6c84-4ace-b0a8-011447bb27e2");
    //for test private static final UUID CHARACTERISTIC_FIRMWARE_VERSION = UUID.fromString("b13b5d92-74d2-4477-885b-9010108b20e5");
    private static final UUID CHARACTERISTIC_MODEL_NAME = UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");

    private static final UUID CHARACTERISTIC_HARDWARE_VERSION = UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_SOFTWARE_VERSION = UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_MANUFACTURER_NAME = UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");

    private static final UUID CHARACTERISTIC_TX_POWER = UUID.fromString("f202f081-50bc-497a-ac0e-a3410c972b63");
    // for test private static final UUID CHARACTERISTIC_TX_POWER = UUID.fromString("6240cbea-6c84-4ace-b0a8-011447bb27e2");
    private static final UUID CHARACTERISTIC_TRANSMITDURATION = UUID.fromString("56a3688c-211e-48da-8676-f52d7053e8a8");
    private static final UUID CHARACTERISTIC_GPIN_AND_PINS = UUID.fromString("7b68f16b-543a-4403-b5d3-85d7e1bc2f3f");
    private static final UUID CHARACTERISTIC_GPIN_STOP_CMD_PINS = UUID.fromString("6147aac7-e1eb-41aa-b0b7-322d6bd1c755");

    private static final UUID CHARACTERISTIC_GROUP_1 = UUID.fromString("b13b5d92-74d2-4477-885b-9010108b20e5");
    private static final UUID CHARACTERISTIC_GROUP_2 = UUID.fromString("ddcadc9b-d770-4686-aab7-147bf410dbdc");
    private static final UUID CHARACTERISTIC_GROUP_3 = UUID.fromString("51231aa8-5223-4161-99a8-150a2edfd2c4");
    private static final UUID CHARACTERISTIC_GROUP_4 = UUID.fromString("c72eda67-5a5f-4700-aaab-c1d2aa29483a");
    private static final UUID CHARACTERISTIC_GROUP_5 = UUID.fromString("a4c1bf38-13b5-4808-b04e-be07763fb3d9");

    private static final UUID CHARACTERISTIC_GROUP_6 = UUID.fromString("1d9cd7c6-f0c7-4dd9-b54b-3e980901a366");
    private static final UUID CHARACTERISTIC_GROUP_7 = UUID.fromString("e3cedc31-38d4-42a9-a07f-96678109abec");
    private static final UUID CHARACTERISTIC_GROUP_8 = UUID.fromString("ddd7cd7d-2fd0-48cc-8b7b-e5aac1e45dcc");
    private static final UUID CHARACTERISTIC_GROUP_9 = UUID.fromString("f8b5b592-a39f-4034-a65c-19366edac50f");
    private static final UUID CHARACTERISTIC_GROUP_10 = UUID.fromString("c217c144-166e-4d7b-a834-047a9c4bf690");

    private static final UUID CHARACTERISTIC_TRIG_DELAY = UUID.fromString("d4864911-7fa2-4912-8fa6-9ee6cfc6cf8a");
    private static final UUID CHARACTERISTIC_AUTO_STOPRECORDING = UUID.fromString("8bbb54e6-b6a9-493b-9736-a2b7c2283388");

    protected BlueteethDevice mPeripheral;

    protected String mName;
    protected String mMacAddress;

    protected String mManufacturerModel;
    protected String mSerialNumber;
    protected String mFirmwareRevision;
    protected String mHardwareRevision;
    protected String mSoftwareRevision;
    protected String mManufacturerName;

    BaseBluetoothPeripheral(BlueteethDevice peripheral) {
        mPeripheral = peripheral;
        mName = peripheral.getName();
        mMacAddress = peripheral.getMacAddress();
    }

    public String getName() {
        return mName;
    }

    public String getMacAddress() {
        return mMacAddress;
    }


    /**
     * Determines if this peripheral is currently connected or not
     */
    public boolean isConnected() {
        return mPeripheral.isConnected();
    }


    /**
     * Opens connection with a timeout to this device
     *
     * @param autoReconnect      Determines whether the Bluetooth should auto-reconnect (very slow, in background - should be false)
     * @param connectionCallback Will be called after connection success/failure
     */
    public void connect(boolean autoReconnect, OnConnectionChangedListener connectionCallback) {
        mPeripheral.connect(autoReconnect, connectionCallback);
    }

    /**
     * Opens connection with a timeout to this device
     *
     * @param autoReconnect      Determines whether the Bluetooth should auto-reconnect (very slow, in background - should be false)
     * @param connectionCallback Will be called after connection success/failure
     * @param bondingCallback    Will be called on pairing events
     */
    public void connect(boolean autoReconnect, OnConnectionChangedListener connectionCallback, OnBondingChangedListener bondingCallback) {
        mPeripheral.connect(autoReconnect, connectionCallback, bondingCallback);
    }

    /**
     * Disconnects from device
     *
     * @param callback Will be called after disconnection success/failure
     */
    public void disconnect(OnConnectionChangedListener callback) {
        mPeripheral.disconnect(callback);
    }

    /**
     * Releases all Bluetooth resources
     */
    public void close() {
        mPeripheral.close();
    }

    /**
     * Read the Model Name
     * */
    public void readModelName(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_MODEL_NAME, SERVICE_DEVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Reads the firmware version
     */
    public void readFirmwareVersion(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_FIRMWARE_VERSION, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Read MB SN
     */
    public void readMBSN(OnCharacteristicReadListener onCharacteristicReadListener){
        BlueteethUtils.read(CHARACTERISTIC_SERIAL_NUMBER, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Read the TX Power
     */
    public void readTXpower(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_TX_POWER, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Write the Tx Power
     */
   /* public void writeTXpower(OnCharacteristicWriteListener onCharacteristicWriteListener){
        byte[] data;
        data = new byte[]{0x00};
        mPeripheral.writeCharacteristic(data, CHARACTERISTIC_TX_POWER, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);
        BlueteethUtils.writeData(mOtaSource.readByteArray(PACKET_SIZE), CHARACTERISTIC_TX_POWER, CUSTOM_SERVICE_INFORMATION, mPeripheral, response -> {
                    mOnFirmwarePacketUploadedListener.call();
    }
    */

    /**
     * Read Transmit Duration
    * */
    public void readTransmit(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_TRANSMITDURATION, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }


    /**
     * Read GPIN STOP CMD PINS
     * */
    public void readGPINstop(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GPIN_STOP_CMD_PINS, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Write GPIN STOP CMD PINS

    public void writeStopPin(OnCharacteristicWriteListener onCharacteristicWriteListener){
        BlueteethUtils.writeData(CHARACTERISTIC_GPIN_STOP_CMD_PINS, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicWriteListener);
    }*/



    /**
     * Read Group Name
     */
    public void readGroupName(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_1, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName2(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_2, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName3(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_3, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName4(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_4, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName5(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_5, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName6(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_6, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName7(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_7, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName8(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_8, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName9(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_9, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    public void readGroupName10(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GROUP_10, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }


    /**
     * Read WIRE AND PIN#
     */
    public void readWireandPin(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_GPIN_AND_PINS, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Read TRIG DELAY TIME
     */
    public void readTrigDelay(OnCharacteristicReadListener onCharacteristicReadListener){
        BlueteethUtils.read(CHARACTERISTIC_TRIG_DELAY, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     *  Read Auto Stop Recording
     */
    public void readAutoStopRecording(OnCharacteristicReadListener onCharacteristicReadListener){
        BlueteethUtils.read(CHARACTERISTIC_AUTO_STOPRECORDING, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

}

