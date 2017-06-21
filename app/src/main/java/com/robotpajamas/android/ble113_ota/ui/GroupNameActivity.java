package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;

import butterknife.Bind;
import butterknife.ButterKnife;
import okio.ByteString;
import timber.log.Timber;

/**
 * Created by nita.huang on 2017/6/19.
 */

public class GroupNameActivity extends Activity {
    private BluegigaPeripheral mBluegigaPeripheral;

    @Bind(R.id.textView)
    TextView mGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_groupname);

        ButterKnife.bind(this);

        String macAddress = getIntent().getStringExtra(getString(R.string.extra_mac_address));
        mBluegigaPeripheral = new BluegigaPeripheral(BlueteethManager.with(this).getPeripheral(macAddress));

        mBluegigaPeripheral.readGroupName(((response, data) -> {
            if (response !=BlueteethResponse.NO_ERROR) {
                return;
            }
            runOnUiThread(() -> mGroupName.setText(String.format(getString(R.string.group_name1), ByteString.of(data, 0, data.length).utf8())));

            }));

            Timber.d("GroupNameActivity created! Nitaaaaaaa");
        //LogService.WriteLogFile("Result : Splash Activity create!");

    }



}
