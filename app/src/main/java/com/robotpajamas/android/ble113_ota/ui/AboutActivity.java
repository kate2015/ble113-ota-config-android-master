package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.robotpajamas.android.ble113_ota.R;
/**
 * Created by lingyu.huang on 2017/5/18.
 */

public class AboutActivity extends Activity {
    ImageButton mImgBack;
    Button mImgBack1;

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim)
    {
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);
        /*overridePendingTransition(R.transition.pull_in_left, R.transition.push_out_right);

        mImgBack = (ImageButton)findViewById(R.id.CameraListback);
        mImgBack1 = (Button)findViewById(R.id.CameraListback1);
        mImgBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    mImgBack.setImageResource(R.drawable.back01_click);
                }
                /*
                else if (event.getAction()==MotionEvent.ACTION_UP){
                    mImgBack.setImageResource(R.drawable.back01);
                }
//
                return false;
            }
        });
        mImgBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }


    @Override
    public void onBackPressed() {
        finish();
        //overridePendingTransition(R.transition.pull_in_right, R.transition.push_out_left);

    }
}
