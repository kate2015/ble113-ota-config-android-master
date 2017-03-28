package com.robotpajamas.android.ble113_ota.peripherals;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethUtils;
import com.robotpajamas.android.ble113_ota.listeners.OnBondingChangedListener;
import com.robotpajamas.android.ble113_ota.listeners.OnCharacteristicReadListener;
import com.robotpajamas.android.ble113_ota.listeners.OnConnectionChangedListener;

import java.util.UUID;

public class BaseBluetoothPeripheral {

    // Using standard 16bit UUIDs, transformed into the correct 128-bit UUID
    private static final UUID SERVICE_DEVICE_INFORMATION = UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    private static final UUID CUSTOM_SERVICE_INFORMATION = UUID.fromString("8409f408-63e3-4eea-aaf4-762ddec5319c");

    private static final UUID CHARACTERISTIC_SERIAL_NUMBER = UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_FIRMWARE_VERSION = UUID.fromString("6240cbea-6c84-4ace-b0a8-011447bb27e2");
    private static final UUID CHARACTERISTIC_MODEL_NAME = UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");

    private static final UUID CHARACTERISTIC_HARDWARE_VERSION = UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_SOFTWARE_VERSION = UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_MANUFACTURER_NAME = UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");

    private static final UUID CHARACTERISTIC_TX_POWER = UUID.fromString("F202F081-50BC-497A-AC0E-A3410C972B63");
    //private final static String CHARACTERISTIC_TX_POWER = "00002901-0000-1000-8000-00805f9b34fb";
    private static final UUID CHARACTERISTIC_TRANSMITDURATION = UUID.fromString("56a3688c-211e-48da-8676-f52d7053e8a8");
    private static final UUID CHARACTERISTIC_GPIN_AND_PINS = UUID.fromString("7b68f16b-543a-4403-b5d3-85d7e1bc2f3f");
    private static final UUID CHARACTERISTIC_GPIN_STOP_CMD_PINS = UUID.fromString("6147aac7-e1eb-41aa-b0b7-322d6bd1c755");
    private static final UUID CHARACTERISTIC_GROUP_1 = UUID.fromString("b13b5d92-74d2-4477-885b-9010108b20e5");

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
     * Reads the firmware version
     */
    public void readFirmwareVersion(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_FIRMWARE_VERSION, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Read the TX Power
     */
    public void readTXpower(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_TX_POWER, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
    * Read the Model Name
    * */
    public void readModelName(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_MODEL_NAME, SERVICE_DEVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

    /**
     * Read Transmit Duration
    * */
    public void readTransmit(OnCharacteristicReadListener onCharacteristicReadListener) {
        BlueteethUtils.read(CHARACTERISTIC_TRANSMITDURATION, CUSTOM_SERVICE_INFORMATION, mPeripheral, onCharacteristicReadListener);
    }

}

