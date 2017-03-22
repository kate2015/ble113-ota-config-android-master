package com.robotpajamas.android.ble113_ota.listeners;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

public interface OnCharacteristicWriteListener {
    void call(BlueteethResponse response);
}
