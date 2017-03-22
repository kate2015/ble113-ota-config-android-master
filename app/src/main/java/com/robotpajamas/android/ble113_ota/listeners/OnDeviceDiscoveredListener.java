package com.robotpajamas.android.ble113_ota.listeners;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;

public interface OnDeviceDiscoveredListener {
    void call(BlueteethDevice blueteethDevice);
}
