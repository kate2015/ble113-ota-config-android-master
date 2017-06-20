package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.robotpajamas.android.ble113_ota.R;

import timber.log.Timber;

/**
 * Created by nita.huang on 2017/6/19.
 */

public class GroupNameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_groupname);
        Timber.d("SplashActivity created! Nitaaaaaaa");
        //LogService.WriteLogFile("Result : Splash Activity create!");

    }


}
