package com.robotpajamas.android.ble113_ota.peripherals;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.robotpajamas.android.ble113_ota.listeners.OnCharacteristicWriteListener;
import com.robotpajamas.android.ble113_ota.listeners.OnFirmwarePacketUploadedListener;
import com.robotpajamas.android.ble113_ota.listeners.OnFirmwareUpdateCompleteListener;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import timber.log.Timber;

public class BluegigaPeripheral extends BaseBluetoothPeripheral {

    /* OTA Service */
    private static final UUID SERVICE_OTA = UUID.fromString("1d14d6ee-fd63-4fa1-bfa4-8f47b42119f0");
    private static final UUID CUSTOM_SERVICE_INFORMATION = UUID.fromString("8409f408-63e3-4eea-aaf4-762ddec5319c");

    private static final UUID CHARACTERISTIC_TX_POWER = UUID.fromString("f202f081-50bc-497a-ac0e-a3410c972b63");
    private static final UUID CHARACTERISTIC_TRANSMITDURATION = UUID.fromString("56a3688c-211e-48da-8676-f52d7053e8a8");
    private static final UUID CHARACTERISTIC_CONTROL_NO_ACK = UUID.fromString("F7BF3564-FB6D-4E53-88A4-5E37E0326063");
    private static final UUID CHARACTERISTIC_DATA_NO_ACK = UUID.fromString("984227F3-34FC-4045-A5D0-2C581F81A153");

    private static final UUID CHARACTERISTIC_GPIN_AND_PINS = UUID.fromString("7b68f16b-543a-4403-b5d3-85d7e1bc2f3f");
    private static final UUID CHARACTERISTIC_GPIN_STOP_CMD_PINS = UUID.fromString("6147aac7-e1eb-41aa-b0b7-322d6bd1c755");
    private static final UUID CHARACTERISTIC_GROUP_1 = UUID.fromString("b13b5d92-74d2-4477-885b-9010108b20e5");
    private static final UUID CHARACTERISTIC_TRIG_DELAY = UUID.fromString("d4864911-7fa2-4912-8fa6-9ee6cfc6cf8a");

    private static final int PACKET_SIZE = 20;


    Handler mHandler = new Handler(Looper.getMainLooper());

    private BufferedSource mOtaSource = new Buffer();
    private OnFirmwarePacketUploadedListener mOnFirmwarePacketUploadedListener;
    private OnFirmwareUpdateCompleteListener mOnFirmwareUpdateCompleteListener;

    public BluegigaPeripheral(BlueteethDevice device) {
        super(device);
    }

    /**
     * Set GPIN STOP CMD PINS
     */
    public void SetStopPin(byte[] data, @NonNull OnCharacteristicWriteListener onCharacteristicWriteListener){

        mPeripheral.writeCharacteristic(data,CHARACTERISTIC_GPIN_STOP_CMD_PINS, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);

    }

    /**
     *
     * @param data
     * @param onCharacteristicWriteListener
     * @return
     */
    public void setRecPin(byte[] data, @NonNull OnCharacteristicWriteListener onCharacteristicWriteListener){

        mPeripheral.writeCharacteristic(data,CHARACTERISTIC_GPIN_AND_PINS, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);

    }

    /**
     *
     * @param data
     * @param onCharacteristicWriteListener
     * @return
     * @return
     */
    public void setGroupName(byte[] data, @NonNull OnCharacteristicWriteListener onCharacteristicWriteListener){

        mPeripheral.writeCharacteristic(data, CHARACTERISTIC_GROUP_1, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);

    }

    /**
     *
     * @param data
     * @param onCharacteristicWriteListener
     * @param
     * @return
     */
    public void setTransmitTime(byte[] data, @NonNull OnCharacteristicWriteListener onCharacteristicWriteListener){

        mPeripheral.writeCharacteristic(data, CHARACTERISTIC_TRANSMITDURATION, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);

    }

    /**
     *
     * @param data
     * @param onCharacteristicWriteListener
     * @param
     * @return
     */
    public void setTrigDelay(byte[] data, @NonNull OnCharacteristicWriteListener onCharacteristicWriteListener){

        mPeripheral.writeCharacteristic(data, CHARACTERISTIC_TRIG_DELAY, CUSTOM_SERVICE_INFORMATION, onCharacteristicWriteListener);
    }

    public int updateFirmware(File  otaFile,
                              @NonNull OnFirmwarePacketUploadedListener onFirmwarePacketUploadedListener,
                              @NonNull OnFirmwareUpdateCompleteListener onFirmwareUpdateCompleteListener) {
        Timber.d("Starting firmware update");

        mOnFirmwarePacketUploadedListener = onFirmwarePacketUploadedListener;
        mOnFirmwareUpdateCompleteListener = onFirmwareUpdateCompleteListener;

        int totalNumberOfPackets = (int) (otaFile.length() / 20);


        try {
            byte[] data;
            boolean sendDfu = mPeripheral.isConnected();

            data = new byte[]{0x00};
            mPeripheral.writeCharacteristic(data, CHARACTERISTIC_CONTROL_NO_ACK, SERVICE_OTA, response -> mOnFirmwareUpdateCompleteListener.call());

            if (sendDfu) {
                mOtaSource = Okio.buffer(Okio.source(otaFile));
                BlueteethUtils.writeData(mOtaSource.readByteArray(PACKET_SIZE), CHARACTERISTIC_DATA_NO_ACK, SERVICE_OTA, mPeripheral, response -> {
                    mOnFirmwarePacketUploadedListener.call();
                    mHandler.postDelayed(uploadNextPacket, 300);
                });
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return totalNumberOfPackets;
    }

    private Runnable uploadNextPacket = new Runnable() {
        @Override
        public void run() {
            //byte[] dfu_data = new byte[PACKET_SIZE];

            try {
                byte[] data;
                boolean sendReset = mOtaSource.exhausted();
                if (sendReset) {
                    data = new byte[]{0x03};
                    mPeripheral.writeCharacteristic(data, CHARACTERISTIC_CONTROL_NO_ACK, SERVICE_OTA, response -> mOnFirmwareUpdateCompleteListener.call());
                } else {

                    data = mOtaSource.readByteArray(PACKET_SIZE);
                    mPeripheral.writeCharacteristic(data, CHARACTERISTIC_DATA_NO_ACK, SERVICE_OTA, response -> {
                        mOnFirmwarePacketUploadedListener.call();
                        mHandler.postDelayed(uploadNextPacket, 300);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}

