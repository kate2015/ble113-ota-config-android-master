package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by nita.huang on 2017/6/19.
 */

public class GroupNameActivity extends OtaActivity {
    private BluegigaPeripheral mBluegigaPeripheral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_groupname);
        ButterKnife.bind(this);

        String macAddress = getIntent().getStringExtra(getString(R.string.extra_mac_address));
        mBluegigaPeripheral = new BluegigaPeripheral(BlueteethManager.with(this).getPeripheral(macAddress));

        Timber.d("SplashActivity created! Nitaaaaaaa");
        //LogService.WriteLogFile("Result : Splash Activity create!");



    }


}
