package com.robotpajamas.android.ble113_ota.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

/**
 * Created by rsbu-aarontsai on 2017/2/22.
 */

public class SplashActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_splash);
        //LogService.WriteLogFile("Result : Splash Activity create!");

    }


    @Override
    protected void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause()
    {
        super.onPause();

    }
    @Override
    public void onStart()
    {
        super.onStart();

    }
    @Override
    public void onStop()
    {
        super.onStop();
        this.finish();

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }
}
