package com.robotpajamas.android.ble113_ota.listeners;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;

import java.util.List;

public interface OnScanCompletedListener {
    void call(List<BlueteethDevice> blueteethDevices);
}
