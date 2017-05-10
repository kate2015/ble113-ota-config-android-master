package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Element;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethUtils;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.datatype.DatatypeFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import timber.log.Timber;

import static android.R.attr.onClick;


public class OtaActivity extends Activity {
    private boolean mIsConnected;

    @Bind(R.id.progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.textview_modelname)
    TextView mModelnameTextview;

    @Bind(R.id.textview_firmware)
    TextView mFirmwareTextview;

    @Bind(R.id.textview_mb)
    TextView mMBSNTextview;

    @Bind(R.id.textview_txpower)
    TextView mTXpower;

    //@Bind(R.id.set_txpower)
    //TextView msetTXpower;

    @Bind(R.id.textview_transmit)
    TextView mTransmit;

    @Bind(R.id.textview_trigdelay)
    TextView mTrigDelay;

    @Bind(R.id.textView_autostop)
    TextView mAutoStopRec;

    @Bind(R.id.textview_WireAndPin)
    //@Bind(R.id.WireAndPin)
    TextView mWireAndPin;

    @Bind(R.id.textview_RecStopPin)
    //@Bind(R.id.RecStopPin)
    TextView mRecStopPin;

    @Bind(R.id.setstoppin)
    Button mSetStop;

    @Bind(R.id.textview_GroupName)
    TextView mGroupName;



    private BluegigaPeripheral mBluegigaPeripheral;
    private int mTotalNumberOfPackets = 0;
    private int mCurrentPacket = 0;

    /*private RadioGroup mRadioGroup, mRadGrpRegion;
    private RadioButton mRadioButtonEnable,
                        mRadioButtonDisable;*/

    //private boolean mStatus = true;

    /*/------
    @OnClick(R.id.Connect)
    void connect() {
        if (mIsConnected) {
            //updateReceivedData(String.format("Attempting to disconnect from %s - %s...", mSamplePeripheral.getName(), mSamplePeripheral.getMacAddress()));
            mBluegigaPeripheral.disconnect(isConnected -> {
                //updateReceivedData("Connection Status: " + Boolean.toString(isConnected) + "\n");
                mIsConnected = isConnected;
                Timber.d("nitaa disconnected...");
                //runOnUiThread(mConnectionRunnable);
            });

            //----- Read Firmware Version ---------
            mBluegigaPeripheral.readFirmwareVersion((response, data) -> {
                if (response != BlueteethResponse.NO_ERROR) {
                    return;
                }
                runOnUiThread(() -> mFirmwareTextview.setText(String.format(getString(R.string.firmware_version), ByteString.of(data, 0, data.length).utf8())));
            });*/


    @OnClick(R.id.setrecpin)
    void setRecPin(){
        EditText ed = (EditText) findViewById(R.id.WireAndPin);
        String input = ed.getText().toString();
        byte[] value1 = StringDataToByte(input);
        String data = BitToInt(value1);
        ed.setText(data);
        mBluegigaPeripheral.setRecPin(value1,response -> {});

    }

    @OnClick(R.id.setstoppin)
    void setStopRecPin(){
        //mBluegigaPeripheral.SetStopPin();
        EditText ed = (EditText) findViewById(R.id.RecStopPin);
        String input = ed.getText().toString();
        byte[] value1 = StringDataToByte(input);
        String data = BitToInt(value1);
        ed.setText(data);
        mBluegigaPeripheral.SetStopPin(value1,response -> {});

    }

    @OnClick(R.id.setgroupname)
    void setGroupName(){
        EditText ed = (EditText) findViewById(R.id.edittext_Groupname);
        String input = ed.getText().toString();
        //byte[] value1 = StringDataToByte(input);
        ed.setText(input);

        mBluegigaPeripheral.setGroupName(input.getBytes(),response -> {});
    }


    @OnClick(R.id.radioButton_dis)
    void disableRec(){
        //RadioButton mRadioButtonDisable = (RadioButton) findViewById(R.id.radioButton_dis);
        //String input = mRadioButtonDisable.getText().toString();
        byte[] value1 = {0};

        //if (mRadioButtonDisable.isChecked())
            mBluegigaPeripheral.setStopRec(value1, response -> {});


    }

    @OnClick(R.id.radioButton_en)
    void enableRec(){
        //RadioButton mRadioButtonEnable = (RadioButton) findViewById(R.id.radioButton_en);
        //String input = mRadioButtonEnable.getText().toString();
        byte[] value1 = {1};

        mBluegigaPeripheral.setStopRec(value1, response -> {});

    }

    @OnClick(R.id.button_upload_010)
    void startFirmwareUpdate010() {
        mCurrentPacket = 0;

        File otaFile = createTempFile(Okio.buffer(Okio.source(getResources().openRawResource(R.raw.fw_ota_app))));
        mTotalNumberOfPackets = mBluegigaPeripheral.updateFirmware(otaFile,
                () -> {
                    Timber.d("Firmware packet uploaded. %d of %d", ++mCurrentPacket, mTotalNumberOfPackets);
                    mProgressBar.incrementProgressBy(1);
                }, () -> Timber.d("Firmware update completed"));

        mProgressBar.setMax(mTotalNumberOfPackets);
    }

    @OnClick(R.id.button_upload_011)
    void startFirmwareUpdate011() {
        mCurrentPacket = 0;

        File otaFile = createTempFile(Okio.buffer(Okio.source(getResources().openRawResource(R.raw.fw_ota_nita_app))));
        mTotalNumberOfPackets = mBluegigaPeripheral.updateFirmware(otaFile,
                () -> {
                    Timber.d("Firmware packet uploaded. %d of %d", ++mCurrentPacket, mTotalNumberOfPackets);
                    mProgressBar.incrementProgressBy(1);
                }, () -> Timber.d("Firmware update completed"));

        mProgressBar.setMax(mTotalNumberOfPackets);
    }

    public File createTempFile(BufferedSource inputSource) {
        File file;
        try {
            file = File.createTempFile("otaFile", null, getCacheDir());
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(inputSource);
            sink.close();
            inputSource.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] StringDataToByte(String strData)
    {
        String[] strArray = strData.split(",");
        byte[] bData = new byte[1];

        for(int i = 0; i < strArray.length; i++)
        {
            bData[0] |= (1 << Byte.parseByte(strArray[i]));
        }
        return bData;
    }

    private String BitToInt(byte[] data)
    {
        //int[] iData = new int[8];
        int id = 0;
        String str_ids = "";
        for(int i = 0; i < 8; i++)
        {
            if ( ((data[0] >> i) & 0x1) == 1) {
                //iData[id++] = i;
                if(id > 0)
                    str_ids += ",";
                id++;
                str_ids += Integer.toString(i);
            }


        }
        Timber.d("nitaa bit to string %s",str_ids );


        return str_ids;
    }

    private String byte2string(byte[] data){
        String button = "";
        String value = "";

        button = Integer.toString(data[0]);

        switch (button)
        {
            case "1":
                value = "Enable";
                break;
            case "0":
                value = "Disable";
                break;
            default:
                value = "Disable";
        }
        return value;
    }

    private String TwoBytesToShort(byte[] data) {
        short sData = (short) ((short)((data[0] & 0xff) * 0x100) + (short)(data[1] & 0xff));

        //short sData = (short)((data[0] & (short)(0xff)) * (short)(0x100);// + (short)(data[1] & (short)0xff));
        return Short.toString(sData);
    }

    private byte[] hex2Byte(String hexString) {
        byte[] bytes = new byte[hexString.length()];

        char c = hexString.charAt(0);
        if(c >= '0' && c <= '9')
        {
            c -= '0';
        }else if (c >= 'A' && c <= 'F') {
            c -= 'A';
        }else if (c >= 'a' && c <= 'f') {
            c -= 'a';
        }
        bytes[0] = (byte) c;

        return bytes;
    }

    private byte[] StrHex2ByteArry(String strHex)
    {
        byte[] rc = new byte[strHex.length()];
        int rc_id = 0;

        for(int i = strHex.length() - 1; i >= 0; i--)
        {
            char c = strHex.charAt(i);
            if(c >= '0' && c <= '9')
            {
                c -= '0';
            }else if (c >= 'A' && c <= 'F') {
                c -= 'A';
            }else if (c >= 'a' && c <= 'f') {
                c -= 'a';
            }
            rc[rc_id++] = (byte) c;
        }
        return rc;

        //for(int i = 0; i >  strHex.length() ; i++)
        //for(int i = strHex.length() - 1; i >= 0; i--)
    }

    private String trigdelay(byte[] data){
        String time = "";
        String transmit_dura = "";

        time = Integer.toString(data[0]);

        switch (time)
        {
            case "0":
                transmit_dura = "0 secs";
                break;
            case "1":
                transmit_dura = "1 sec";
                break;
            case "2":
                transmit_dura = "2 secs";
                break;
            case "3":
                transmit_dura = "3 secs";
                break;
            case "4":
                transmit_dura = "4 secs";
                break;
            case "5":
                transmit_dura = "5 secs";
                break;
            case "6":
                transmit_dura = "6 secs";
                break;
            case "7":
                transmit_dura = "7 secs";
                break;
            case "8":
                transmit_dura = "8 secs";
                break;
            case "9":
                transmit_dura = "9 secs";
                break;
        }
        return transmit_dura;

    }

    private void timetotrigdelay(String Selected_item){
        String time = "";
        switch (Selected_item){
            case "0 secs":
                time = "0";
                break;
            case "1 secs":
                time = "1";
                break;
            case "2 secs":
                time = "2";
                break;
            case "3 secs":
                time = "3";
                break;
            case "4 secs":
                time = "4";
                break;
            case "5 secs":
                time = "5";
                break;
            case "6 secs":
                time = "6";
                break;
            case "7 secs":
                time = "7";
                break;
            case "8 secs":
                time = "8";
                break;
            case "9 secs":
                time = "9";
                break;
            default:
                time = "5";
        }
        byte[] data = hex2Byte(time);
        mBluegigaPeripheral.setTrigDelay(data, response -> {});

    }

    private String transmitime(byte[] data){

        String time = "";
        String transmit_dura = "";

        time = Integer.toString(data[0]*10 + data[1]);

        switch (time)
        {
            case "1":
                transmit_dura = "30 secs";
                break;
            case "20":
                transmit_dura = "1 mins";
                break;
            case "40":
                transmit_dura = "2 mins";
                break;
            case "10":
                transmit_dura = "5 mins";
                break;
            case "2":
                transmit_dura = "10 mins";
                break;
            case "3":
                transmit_dura = "15 mins";
                break;
            case "4":
                transmit_dura = "20 mins";
                break;
        }
        return transmit_dura;

    }



    private void timetoTransmit(String Selected_item){

        String time = "";
        switch (Selected_item){
            case "30 secs":
                time = "1";
                break;
            case "1 mins":
                time = "2";
                break;
            case "2 mins":
                time = "4";
                break;
            case "5 mins":
                time = "10";
                break;
            case "10 mins":
                time = "20";
                break;
            case "15 mins":
                time = "30";
                break;
            case "20 mins":
                time = "40";
                break;
            default:
                time = "1";
        }

        byte[] bdata = StrHex2ByteArry(time);

        mBluegigaPeripheral.setTransmitTime(bdata, response -> {});
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        ButterKnife.bind(this);

        String macAddress = getIntent().getStringExtra(getString(R.string.extra_mac_address));
        mBluegigaPeripheral = new BluegigaPeripheral(BlueteethManager.with(this).getPeripheral(macAddress));

        //+++++++++++++++ Set Tx Power Spinner +++++++++++++++
        Spinner spinnerTx = (Spinner)findViewById(R.id.set_txpower);

        final String[] txpower = {" +8.0 dbm ", " +7.5 dbm ", " +7.0 dbm ", " +6.5 dbm ", " +6.0 dbm ", " +5.5 dbm ", " +5.0 dbm ",
                " +5.0 dbm ", " +4.5 dbm ", " +4.0 dbm ", " +3.5 dbm ", " +3.0 dbm ", " +2.5 dbm ", " +2.0 dbm ", " +1.5 dbm ",
                " +1.0 dbm ", " +0.5 dbm ","     0 dbm ", " -0.5 dbm", " -1.0 dbm ", " -1.5 dbm ", " -2.0 dbm ", " -2.5 dbm ",
                " -3.0 dbm ", " -3.5 dbm ", " -4.0 dbm ", " -4.5 dbm ", " -5.0 dbm ", " -5.5 dbm ", " -6.0 dbm ", " -6.5 dbm ",
                " -7.0 dbm ", " -7.5 dbm ", " -8.0 dbm "};
        ArrayAdapter<String> txpowerList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                txpower);

        spinnerTx.setAdapter(txpowerList);

        /*ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(OtaActivity.this,
                R.array.txpower_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTx.setAdapter(adapter);
        */

        spinnerTx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + txpower[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //----------------- Set Tx Power -----------------


        //+++++++++++++++ Read/Write  Transmit Duration +++++++++++++++
        Spinner spinnerTransmit = (Spinner)findViewById(R.id.transmit);
        //final String[] transmit = {"30 secs ", " 1 mins ", " 2mins ", " 5 mins ", " 10 mins ", " 15 mins "," 20 mins "};
        final String[] transmit = {"20 mins", "1 mins", "2mins", "5 mins", "10 mins", "15 mins","30 secs"};
        ArrayAdapter<String> transmitList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                transmit);

        spinnerTransmit.setAdapter(transmitList);
        spinnerTransmit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selected_item = spinnerTransmit.getSelectedItem().toString();

                timetoTransmit(Selected_item);

                Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + transmit[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //----------- Read/write Transmit Duration -----------------

        //+++++++++++++++ Read/ write Trig Delay +++++++++++++++

        Spinner spinnerDelay = (Spinner)findViewById(R.id.trigdelay);

        final String[] trigdelay = {"0 secs", "1 secs", "2 secs", "3 secs", "4 secs", "5 secs", "6secs", "7secs", "8secs", "9secs"};
        ArrayAdapter<String> trigdelayList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                trigdelay);

        spinnerDelay.setAdapter(trigdelayList);
        spinnerDelay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selected_item = spinnerDelay.getSelectedItem().toString();

                timetotrigdelay(Selected_item);

                Toast.makeText(OtaActivity.this, "You Set Trig Delay :" + trigdelay[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        //------------- Write Trig Delay --------------------------

        //+++++++++++++++ Enable/Disable auto Stop Recording +++++++++++++++
        /*private void onRadioButtonClicked(View view){

            boolean checked = ((RadioButton)view).isChecked();

            switch (findViewById().getBottom()){
                case R.id.radioButton_dis:
                    if (checked)
                        //
                    break;
                case R.id.radioButton_en:
                    if (checked)
                        //
                    break;
            }
        }*/

        //--------------- Enable/Disable auto Stop Recording ---------------

        //+++++++++++++++ Read Firmware Version +++++++++++++++
        mBluegigaPeripheral.readFirmwareVersion((response, data) -> {
            if (response != BlueteethResponse.NO_ERROR) {
                return;
            }
            runOnUiThread(() -> mFirmwareTextview.setText(String.format(getString(R.string.firmware_version), ByteString.of(data, 0, data.length).utf8())));

            // /----- Read Model Name ----------------
            mBluegigaPeripheral.readModelName(((response1, data1) -> {
                if (response !=BlueteethResponse.NO_ERROR) {
                    return;
                }
                runOnUiThread(() -> mModelnameTextview.setText(String.format("Model Name: %s", ByteString.of(data1, 0, data1.length).utf8())));

                //-----  Read/Write  Tx Power Spinner -----
                mBluegigaPeripheral.readTXpower((response2, data2) -> {
                    if (response != BlueteethResponse.NO_ERROR) {
                        return;
                    }

                    //runOnUiThread(() -> mTXpower.setText(String.format("Tx Power: %s", ByteString.of(data2, 0, data2.length))));
                    runOnUiThread(() -> mTXpower.setText(String.format("Tx Power: %s", TwoBytesToShort(data2))));
                    //runOnUiThread(() -> mTXpower.setText(spinnerTx.getSelectedItem().toString()));

                    //----- Read Group Name ------
                    mBluegigaPeripheral.readGroupName(((response3, data3) -> {
                        if (response !=BlueteethResponse.NO_ERROR) {
                            return;
                        }
                        runOnUiThread(() -> mGroupName.setText(String.format(getString(R.string.group_name), ByteString.of(data3, 0, data3.length).utf8())));

                        //ReadGPINstop
                        mBluegigaPeripheral.readGPINstop(((response5, data5) -> {
                            if (response !=BlueteethResponse.NO_ERROR) {
                                return;
                            }
                            //runOnUiThread(() -> mRecStopPin.setText(String.format(getString(R.string.RecStopPin), ByteString.of(data5, 0, data5.length))));
                            runOnUiThread(() -> mRecStopPin.setText(String.format(getString(R.string.RecStopPin) , BitToInt(data5))));

                            //-- Transmit Duration -----
                            mBluegigaPeripheral.readTransmit(((response6, data6) -> {
                                if (response !=BlueteethResponse.NO_ERROR) {
                                    return;
                                }
                                runOnUiThread(() -> mTransmit.setText(String.format(getString(R.string.transmit_duration), transmitime(data6))));

                                // Read Wire And Pin
                                mBluegigaPeripheral.readWireandPin(((respons7, data7) -> {
                                    if (response !=BlueteethResponse.NO_ERROR) {
                                        return;
                                    }

                                    runOnUiThread(() -> mWireAndPin.setText(String.format(getString(R.string.WireAndPin) , BitToInt(data7))));

                                    // Read MBSN
                                    mBluegigaPeripheral.readMBSN(((respons8, data8) -> {
                                        if (response != BlueteethResponse.NO_ERROR) {
                                            return;
                                        }
                                        runOnUiThread(() -> mMBSNTextview.setText(String.format(getString(R.string.MB_SN) , ByteString.of(data8, 0 , data8.length).utf8())));
                                        // --- Read MBSN ------//

                                        //Read TrigDelay
                                        mBluegigaPeripheral.readTrigDelay((respons9, data9) -> {
                                            if (response != BlueteethResponse.NO_ERROR) {
                                                return;
                                            }
                                            runOnUiThread(() -> mTrigDelay.setText(String.format(getString(R.string.trig_delay), trigdelay(data9))));

                                            //Read Auto Stop Recording
                                            mBluegigaPeripheral.readAutoStopRecording((respons10, data10) -> {
                                                if (response != BlueteethResponse.NO_ERROR) {
                                                    return;
                                                }
                                                runOnUiThread(() -> mAutoStopRec.setText(String.format(getString(R.string.autostop), byte2string(data10))));

                                            });
                                            //----- Auto Stop Recording -----//
                                        });
                                        // ----- Read TrigDelay ------//

                                    }));
                                    //----- Set Stop pin ----- //

                                }));
                                //-----Read Wire and pin -----

                            }));
                            //----- Transmit Duration stop -----
                        }));
                        // ----- Read GPIN stop
                    }));
                    // ---- Read Group Name
                });

                //-------Model Name-------------------
            }));
            //----- Firmware Version ------
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluegigaPeripheral.isConnected()) {
            mBluegigaPeripheral.disconnect(null);
        }
    }




}
