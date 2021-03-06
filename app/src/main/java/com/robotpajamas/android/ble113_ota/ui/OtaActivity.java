package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.renderscript.Element;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethUtils;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;

import javax.xml.datatype.DatatypeFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import timber.log.Timber;



public class OtaActivity extends Activity {
    private Switch mySwitch;
    public boolean switch_on;
    private ImageButton imgbtnDialog;
    private BluegigaPeripheral mBluegigaPeripheral;
    private int mTotalNumberOfPackets = 0;
    private int mCurrentPacket = 0;

    static int[] pins_rec = new int[2];
    static int[] pins_stop = new int[2];
    static String r_tx = "";

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


    @OnClick(R.id.setrecpin)
    void setRecPin(){
        EditText ed = (EditText) findViewById(R.id.WireAndPin);
        String input = ed.getText().toString();
        byte[] value1 = StringDataToByte(input);
        String data = BitToInt(value1, 0);

        if (data == null){

            Timber.d("Same data");

        }else {

            ed.setText(data);
            mBluegigaPeripheral.setRecPin(value1,response -> {});
            mWireAndPin.setText(String.format(getString(R.string.WireAndPin), data));
        }

    }

    @OnClick(R.id.setstoppin)
    void setStopRecPin(){

        EditText ed = (EditText) findViewById(R.id.RecStopPin);
        String input = ed.getText().toString();
        byte[] value1 = StringDataToByte(input);
        String data = BitToInt(value1, 2);

        if (data == null){
            Timber.d("same data2");
        }else{
            ed.setText(data);
            mBluegigaPeripheral.SetStopPin(value1,response -> {});
            mRecStopPin.setText(String.format(getString(R.string.RecStopPin), data));
        }

    }


    @OnClick(R.id.switch_rec)
    void autostop_switch(){

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                switch_on = mySwitch.isChecked();
                if (switch_on){

                    SharedPreferences.Editor editor = getSharedPreferences("Nita", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", true);
                    editor.commit();

                    byte[] value1 = {1};
                    mySwitch.setText("On");

                    mBluegigaPeripheral.setStopRec(value1, response -> {});

                }else {

                    SharedPreferences.Editor editor = getSharedPreferences("Nita", MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave", false);
                    editor.commit();

                    byte[] value1 = {0};
                    mySwitch.setText("Off");

                    mBluegigaPeripheral.setStopRec(value1, response -> {});
                }

            }

        });

    }


    @OnClick(R.id.GP_Button)
    void JumptoGroupNamePage() {
        ImageButton j = (ImageButton) findViewById(R.id.GP_Button);

        final Intent intent = new Intent(this, GroupNameActivity.class);
        intent.putExtra(getString(R.string.extra_mac_address), mBluegigaPeripheral.getMacAddress());
        startActivity(intent);

    }
    /*

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
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        ButterKnife.bind(this);


        String macAddress = getIntent().getStringExtra(getString(R.string.extra_mac_address));
        mBluegigaPeripheral = new BluegigaPeripheral(BlueteethManager.with(this).getPeripheral(macAddress));

        //++++++++++++++ Keep Auto Stop Rec Switch button status +++++++++++++++++
        mySwitch = (Switch) findViewById(R.id.switch_rec);
        SharedPreferences sharedPrefs = getSharedPreferences("Nita", MODE_PRIVATE);
        mySwitch.setChecked(sharedPrefs.getBoolean("NameOfThingToSave", false));
        //-------------- Keep Auto Stop Rec Switch button status -----------------



        //For information setting SW Version
        initView();

        //+++++++++++++++ Set Tx Power Spinner +++++++++++
        setspinnerTX();
        //----------------- Set Tx Power -----------------


        //+++++++++++++++ Read/Write  Transmit Duration ++
        getset_transmitDuration();
        //----------- Read/write Transmit Duration -------

        //+++++++++++++++ Read/ write Trig Delay +++++++++
        readTrigDelay();
        //------------- Write Trig Delay -----------------


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

                    //runOnUiThread(() -> mTXpower.setText(String.format("Tx Power: %s", ByteString.of((float)(data2/10), 0, data2.length))));
                    runOnUiThread(() -> mTXpower.setText(String.format("Tx Power: %s", txpowertodbm(data2))));
                    //runOnUiThread(() -> mTXpower.setText(spinnerTx.getSelectedItem().toString()));

                    //----- Read Group Name ------
                        //Read Rec Pins
                        mBluegigaPeripheral.readWireandPin(((response5, data5) -> {
                            if (response !=BlueteethResponse.NO_ERROR) {
                                return;
                            }
                            runOnUiThread(() -> mWireAndPin.setText(String.format(getString(R.string.WireAndPin) , BitToInt(data5, 1))));

                            //-- Transmit Duration -----
                            mBluegigaPeripheral.readTransmit(((response6, data6) -> {
                                if (response !=BlueteethResponse.NO_ERROR) {
                                    return;
                                }
                                runOnUiThread(() -> mTransmit.setText(String.format(getString(R.string.transmit_duration), transmitime(data6))));

                                // Read Stop rec
                                mBluegigaPeripheral.readGPINstop(((respons7, data7) -> {
                                    if (response !=BlueteethResponse.NO_ERROR) {
                                        return;
                                    }

                                    runOnUiThread(() -> mRecStopPin.setText(String.format(getString(R.string.RecStopPin) , BitToInt(data7, 3))));

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
                    //}));
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

    private String BitToInt(byte[] data, int flag)
    {
        //int[] iData = new int[8];
        int id = 0, j = 0;

        String str_ids = "";
        for(int i = 0; i < 8; i++)
        {
            if ( ((data[0] >> i) & 0x1) == 1) {
                //iData[id++] = i;
                if(id > 0)
                    str_ids += ",";
                id++;

                if (i != 0 ){
                    switch (flag)
                    {
                        case 0 : //set rec
                            for (int k = 0; k < pins_stop.length; k++) {
                                if (pins_stop[k] == i){
                                    Timber.d("the same");
                                    Toast.makeText(OtaActivity.this, "You Set Rec pin : " + i +"same as stop pin , please change it!!!", Toast.LENGTH_SHORT).show();
                                    return null;
                                }
                            }
                            pins_rec[j] = i ;
                            j++;
                            break;
                        case 1 : //read rec

                            pins_rec[j] = i ;
                            j++;

                            break;
                        case 2: //set Stop
                            for (int k = 0; k < pins_rec.length; k++) {
                                if (pins_rec[k] == i){
                                    Timber.d("the same");
                                    Toast.makeText(OtaActivity.this, "You Set Stop pin : " + i +"same as Rec pin, please change it!!!", Toast.LENGTH_SHORT).show();
                                    return null;
                                }
                            }
                            pins_stop[j] = i ;
                            j++;
                            break;
                        case 3: //read stop

                            pins_stop[j] = i ;
                            j++;
                            break;
                        default:
                            Timber.d("Wrong number");
                            return null;
                    }
                }
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
        //short sData = (short) ((short)((data[0] & 0xff) * 0x100) + (short)(data[1] & 0xff));
        //String txpower = Short.toString(sData);


        String value = "";

        //short sData = (short)((data[0] & (short)(0xff)) * (short)(0x100);// + (short)(data[1] & (short)0xff));
        return value;
    }

    private String txpowertodbm(byte[] data){


        float power = (float) (data[1]) /10;

        r_tx = Float.toString(power)  + " dbm";

        return r_tx;
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

            rc[rc_id++] = (byte) c ;
        }
        return rc;

        //for(int i = 0; i >  strHex.length() ; i++)
        //for(int i = strHex.length() - 1; i >= 0; i--)
    }

    private byte[] string2byte(String str){

        if (str == null) {
            return null;
        }

        int data = Integer.parseInt(str);
        byte[] byteArray = new byte[2];
        byteArray[0] = 0;
        byteArray[1] = (byte)data;
        return byteArray;

    }


    private String trigdelay(byte[] data){
        String transmit_dura = "";

        transmit_dura = Integer.toString(data[0]) + " secs";

        return transmit_dura;

    }

    private void dbmtotxpower(String dbm){

        StringBuilder sb = new StringBuilder();

        sb.append(dbm.toString()); //Grab String "-3.5 dbm"

        String sel_cat = sb.delete(5,9).toString(); //remove "dbm"
        float tx_f = (Float.parseFloat(sel_cat)) * 10; //revert dbm to value store in GATT table

        //prepare byte[] data set into Bluetooth Characteristic
        String tx_s = String.valueOf((int)tx_f);
        byte tx_b = Byte.parseByte(tx_s);

        byte[] data = {0, tx_b} ;
        mBluegigaPeripheral.setTxpower(data, response -> {});

    }

    private void timetotrigdelay(String Selected_item){
        String time = "";

        StringBuilder sb = new StringBuilder();


        sb.append(Selected_item.toString());  //Grab String "5 secs"
        time = sb.delete(2,6).toString(); //remove " secs"

        byte[] data = hex2Byte(time);
        mBluegigaPeripheral.setTrigDelay(data, response -> {});

    }

    private String transmitime(byte[] byteArray){

        String time = "";
        String transmit_dura = "";


        if (byteArray == null) {
            return null;
        }
        //time = new String(byteArray);
        time = Integer.toString(byteArray[0] + byteArray[1]);

        switch (time)
        {
            case "1":
                transmit_dura = "30 secs";
                break;
            case "2":
                transmit_dura = "1 mins";
                break;
            case "4":
                transmit_dura = "2 mins";
                break;
            case "10":
                transmit_dura = "5 mins";
                break;
            case "20":
                transmit_dura = "10 mins";
                break;
            case "30":
                transmit_dura = "15 mins";
                break;
            case "40":
                transmit_dura = "20 mins";
                break;
            default:
                transmit_dura = "30 secs";
        }
        return transmit_dura;

    }



    private void timetoTransmit(String Selected_item){

        String time = "";
        //byte[] time = new byte[Selected_item.length()];
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

        byte[] bdata = string2byte(time);

        mBluegigaPeripheral.setTransmitTime(bdata, response -> {});
    }

    // more then one spinner on the page.



    //Spinner for Txpower setting
    void setspinnerTX(){
        Spinner spinnerTx = (Spinner)findViewById(R.id.set_txpower);

        final String[] txpower = {" -3.5 dbm ", " 8.0 dbm ", " 7.5 dbm ", " 7.0 dbm ", " 6.5 dbm ", " 6.0 dbm ", " 5.5 dbm ", " 5.0 dbm ",
                " 5.0 dbm ", " 4.5 dbm ", " 4.0 dbm ", " 3.5 dbm ", " 3.0 dbm ", " 2.5 dbm ", " 2.0 dbm ", " 1.5 dbm ",
                " 1.0 dbm ", " 0.5 dbm "," 0   dbm ", " -0.5 dbm", " -1.0 dbm ", " -1.5 dbm ", " -2.0 dbm ", " -2.5 dbm ",
                " -3.0 dbm "," -3.5 dbm ", " -4.0 dbm ", " -4.5 dbm ", " -5.0 dbm ", " -5.5 dbm ", " -6.0 dbm ", " -6.5 dbm ",
                " -7.0 dbm ", " -7.5 dbm ", " -8.0 dbm "};
        ArrayAdapter<String> txpowerList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                txpower);

        spinnerTx.setAdapter(txpowerList);


        spinnerTx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //spinnerTx.setOnItemSelectedListener(new SpinnerSelectedListener(r_tx) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (r_tx == (String) spinnerTx.getItemAtPosition(position)){
                    return; //so nothing
                }else{
                    String selected_item = spinnerTx.getSelectedItem().toString();

                    dbmtotxpower(selected_item);
                    //mTXpower.setText(selected_item);
                    mTXpower.setText(String.format("Tx Power: %s",selected_item));

                    //Toast.makeText(OtaActivity.this, "You Set TX Power :" + txpower[position], Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Spinner for Read TrigDelay
    void readTrigDelay(){
        Spinner spinnerDelay = (Spinner)findViewById(R.id.trigdelay);

        final String[] trigdelay = {"5 secs", "6 secs", "7 secs", "8 secs", "9 secs", "0 secs", "1 secs", "2 secs", "3 secs", "4 secs"};
        ArrayAdapter<String> trigdelayList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                trigdelay);

        spinnerDelay.setAdapter(trigdelayList);
        spinnerDelay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selected_item = spinnerDelay.getSelectedItem().toString();

                timetotrigdelay(Selected_item);
                mTrigDelay.setText(String.format("Trig Delay:%s", Selected_item));

                //Toast.makeText(OtaActivity.this, "You Set Trig Delay :" + trigdelay[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    // Spinner for transmitDuration
    void getset_transmitDuration(){
        Spinner spinnerTransmit = (Spinner)findViewById(R.id.transmit);
        final String[] transmit = {"30 secs ", "1 mins", "2 mins", "5 mins", "10 mins", "15 mins","20 mins"};
        //final String[] transmit = {"20 mins", "1 mins", "2 mins", "5 mins", "10 mins", "15 mins","30 secs"};
        ArrayAdapter<String> transmitList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                transmit);

        spinnerTransmit.setAdapter(transmitList);
        spinnerTransmit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selected_item = spinnerTransmit.getSelectedItem().toString();

                timetoTransmit(Selected_item);
                mTransmit.setText(String.format("Transmit Duration:%s", Selected_item));

                //Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + transmit[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //++ For information setting SW Version
    private void initView() {
        imgbtnDialog = (ImageButton) findViewById(R.id.version_button);

        imgbtnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });

    }

    private void customDialog(){
        final View item = LayoutInflater.from(OtaActivity.this).inflate(R.layout.about, null);
        new AlertDialog.Builder(OtaActivity.this)
                .setView(item)
                /*.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) item.findViewById(R.id.edit_text);
                        Toast.makeText(getApplicationContext(), getString(R.string.hi) + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })*/
                .show();
    }
    //-- For information setting SW Version



}
