package com.robotpajamas.android.ble113_ota.listeners;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

public interface OnCharacteristicReadListener {
    void call(BlueteethResponse response, byte[] data);
}
