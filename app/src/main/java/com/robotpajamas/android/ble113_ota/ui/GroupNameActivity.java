package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.ByteString;
import timber.log.Timber;

/**
 * Created by nita.huang on 2017/6/19.
 */

public class GroupNameActivity extends Activity {
    private BluegigaPeripheral mBluegigaPeripheral;

    private static final UUID CHARACTERISTIC_GROUP_1 = UUID.fromString("b13b5d92-74d2-4477-885b-9010108b20e5");

    @Bind(R.id.textView)
    TextView mGroupName;

    @Bind(R.id.textView2)
    TextView mGroupName2;

    @Bind(R.id.textView3)
    TextView mGroupName3;

    @Bind(R.id.textView4)
    TextView mGroupName4;

    @Bind(R.id.textView5)
    TextView mGroupName5;

    @Bind(R.id.textView6)
    TextView mGroupName6;

    @Bind(R.id.textView7)
    TextView mGroupName7;

    @Bind(R.id.textView8)
    TextView mGroupName8;

    @Bind(R.id.textView9)
    TextView mGroupName9;

    @Bind(R.id.textView10)
    TextView mGroupName10;


    @OnClick(R.id.setgroupname1)
    void setGroupName(){
        EditText ed = (EditText) findViewById(R.id.editText1);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName(input.getBytes(),response -> {});
        mGroupName.setText(String.format(getString(R.string.group_name1),input));
    }

    @OnClick(R.id.setgroupname2)
    void setGroupName2(){
        EditText ed = (EditText) findViewById(R.id.editText2);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName2(input.getBytes(),response -> {});
        mGroupName2.setText(String.format(getString(R.string.group_name2),input));
    }

    @OnClick(R.id.setgroupname3)
    void setGroupName3(){
        EditText ed = (EditText) findViewById(R.id.editText3);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName3(input.getBytes(),response -> {});
        mGroupName3.setText(String.format(getString(R.string.group_name3),input));
    }

    @OnClick(R.id.setgroupname4)
    void setGroupName4(){
        EditText ed = (EditText) findViewById(R.id.editText4);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName4(input.getBytes(),response -> {});
        mGroupName4.setText(String.format(getString(R.string.group_name4),input));
    }

    @OnClick(R.id.setgroupname5)
    void setGroupName5(){
        EditText ed = (EditText) findViewById(R.id.editText5);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName5(input.getBytes(),response -> {});
        mGroupName5.setText(String.format(getString(R.string.group_name5),input));
    }

    @OnClick(R.id.setgroupname6)
    void setGroupName6(){
        EditText ed = (EditText) findViewById(R.id.editText6);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName6(input.getBytes(),response -> {});
        mGroupName6.setText(String.format(getString(R.string.group_name6),input));
    }

    @OnClick(R.id.setgroupname7)
    void setGroupName7(){
        EditText ed = (EditText) findViewById(R.id.editText7);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName7(input.getBytes(),response -> {});
        mGroupName7.setText(String.format(getString(R.string.group_name7),input));
    }

    @OnClick(R.id.setgroupname8)
    void setGroupName8(){
        EditText ed = (EditText) findViewById(R.id.editText8);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName8(input.getBytes(),response -> {});
        mGroupName8.setText(String.format(getString(R.string.group_name8),input));
    }

    @OnClick(R.id.setgroupname9)
    void setGroupName9(){
        EditText ed = (EditText) findViewById(R.id.editText9);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName9(input.getBytes(),response -> {});
        mGroupName9.setText(String.format(getString(R.string.group_name9),input));
    }

    @OnClick(R.id.setgroupname10)
    void setGroupName10(){
        EditText ed = (EditText) findViewById(R.id.editText10);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName10(input.getBytes(),response -> {});
        mGroupName10.setText(String.format(getString(R.string.group_name10),input));
    }

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

                mBluegigaPeripheral.readGroupName2(((response1, data1) -> {
                    if (response != BlueteethResponse.NO_ERROR) {
                        return;
                    }
                    runOnUiThread(() -> mGroupName2.setText(String.format(getString(R.string.group_name2), ByteString.of(data1, 0, data1.length).utf8())));

                    mBluegigaPeripheral.readGroupName3(((response2, data2) -> {
                        if (response != BlueteethResponse.NO_ERROR) {
                            return;
                        }
                        runOnUiThread(() -> mGroupName3.setText(String.format(getString(R.string.group_name3), ByteString.of(data2, 0, data2.length).utf8())));

                        mBluegigaPeripheral.readGroupName4(((response3, data3) -> {
                            if (response != BlueteethResponse.NO_ERROR) {
                                return;
                            }
                            runOnUiThread(() -> mGroupName4.setText(String.format(getString(R.string.group_name4), ByteString.of(data3, 0, data3.length).utf8())));

                            mBluegigaPeripheral.readGroupName5(((response4, data4) -> {
                                if (response != BlueteethResponse.NO_ERROR) {
                                    return;
                                }
                                runOnUiThread(() -> mGroupName5.setText(String.format(getString(R.string.group_name5), ByteString.of(data4, 0, data4.length).utf8())));

                                mBluegigaPeripheral.readGroupName6(((response5, data5) -> {
                                    if (response != BlueteethResponse.NO_ERROR) {
                                        return;
                                    }
                                    runOnUiThread(() -> mGroupName6.setText(String.format(getString(R.string.group_name6), ByteString.of(data5, 0, data5.length).utf8())));

                                    mBluegigaPeripheral.readGroupName7(((response6, data6) -> {
                                        if (response != BlueteethResponse.NO_ERROR) {
                                            return;
                                        }
                                        runOnUiThread(() -> mGroupName7.setText(String.format(getString(R.string.group_name7), ByteString.of(data6, 0, data6.length).utf8())));

                                        mBluegigaPeripheral.readGroupName8(((response7, data7) -> {
                                            if (response != BlueteethResponse.NO_ERROR) {
                                                return;
                                            }
                                            runOnUiThread(() -> mGroupName8.setText(String.format(getString(R.string.group_name8), ByteString.of(data7, 0, data7.length).utf8())));

                                            mBluegigaPeripheral.readGroupName9(((response8, data8) -> {
                                                if (response != BlueteethResponse.NO_ERROR) {
                                                    return;
                                                }
                                                runOnUiThread(() -> mGroupName9.setText(String.format(getString(R.string.group_name9), ByteString.of(data8, 0, data8.length).utf8())));

                                                mBluegigaPeripheral.readGroupName10(((response9, data9) -> {
                                                    if (response != BlueteethResponse.NO_ERROR) {
                                                        return;
                                                    }
                                                    runOnUiThread(() -> mGroupName10.setText(String.format(getString(R.string.group_name10), ByteString.of(data9, 0, data9.length).utf8())));
                                                })); //GroupName10
                                            }));  //GroupName9

                                        }));  //GroupName8
                                    })); //GroupName7

                                }));  //GrouopName6
                            })); //GroupName5
                        })); //GroupName4
                    }));  //GroupName3
                }));  //GroupName2
            }));  //GroupName1

            Timber.d("GroupNameActivity created! Nitaaaaaaa");
        //LogService.WriteLogFile("Result : Splash Activity create!");

    }



}
