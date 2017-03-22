package com.robotpajamas.android.ble113_ota.listeners;

import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

public interface OnServicesDiscoveredListener {
    void call(BlueteethResponse response);
}
