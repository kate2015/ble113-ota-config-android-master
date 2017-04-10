package com.robotpajamas.android.ble113_ota.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.peripherals.BluegigaPeripheral;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethResponse;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import timber.log.Timber;


public class OtaActivity extends Activity {
    private boolean mIsConnected;

    @Bind(R.id.progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.textview_modelname)
    TextView mModelnameTextview;

    @Bind(R.id.textview_firmware)
    TextView mFirmwareTextview;

    @Bind(R.id.textview_txpower)
    TextView mTXpower;

    @Bind(R.id.textview_RecStopPin)
    TextView mRecStopPin;

    @Bind(R.id.textview_GroupName)
    TextView mGroupName;

    //@Bind(R.id.textview_transmit)
    //TextView mTransmit;

    private BluegigaPeripheral mBluegigaPeripheral;
    private int mTotalNumberOfPackets = 0;
    private int mCurrentPacket = 0;

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
            });



            //----- Read Model Name ----------------
            mBluegigaPeripheral.readModelName(((response, data) -> {
                if (response !=BlueteethResponse.NO_ERROR) {
                    return;
                }
                runOnUiThread(() -> mModelnameTextview.setText(String.format("Model Name: %s", ByteString.of(data, 0, data.length).utf8())));
            }));


            //----- Read/Write  Transmit Duration --------------
            Spinner spinner = (Spinner)findViewById(R.id.transmit);
            final String[] transmit = {" 30secs ", " 1 mins ", " 2mins ", " 5 mins ", " 10 mins ", " 15 mins "," 20 mins "};
            ArrayAdapter<String> transmitList = new ArrayAdapter<>(OtaActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    transmit);

            spinner.setAdapter(transmitList);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + transmit[position], Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mBluegigaPeripheral.readTransmit(((response, data) -> {
                if (response !=BlueteethResponse.NO_ERROR) {
                    return;
                }
                //runOnUiThread(() -> mModelnameTextview.setText(String.format("Transmit Duration: %s", ByteString.of(data, 0, data.length).utf8())));
                runOnUiThread(() -> mModelnameTextview.setText(spinner.getSelectedItem().toString()));
            }));

            //-----  Read/Write  Tx Power Spinner -----
            Spinner spinnerTx = (Spinner)findViewById(R.id.txpower);
            final String[] txpower = {" +8.0 dbm ", " +7.5 dbm ", " +7.0 dbm ", " +6.5 dbm ", " +6.0 dbm ", " +5.5 dbm ", " +5.0 dbm ",
                    " +5.0 dbm ", " +4.5 dbm ", " +4.0 dbm ", " +3.5 dbm ", " +3.0 dbm ", " +2.5 dbm ", " +2.0 dbm ", " +1.5 dbm ",
                    " +1.0 dbm ", " +0.5 dbm ","     0 dbm ", " -0.5 dbm", " -1.0 dbm ", " -1.5 dbm ", " -2.0 dbm ", " -2.5 dbm ",
                    " -3.0 dbm ", " -3.5 dbm ", " -4.0 dbm ", " -4.5 dbm ", " -5.0 dbm ", " -5.5 dbm ", " -6.0 dbm ", " -6.5 dbm ",
                    " -7.0 dbm ", " -7.5 dbm ", " -8.0 dbm "};
            ArrayAdapter<String> txpowerList = new ArrayAdapter<>(OtaActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    txpower);

            spinnerTx.setAdapter(txpowerList);
            spinnerTx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + txpower[position], Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mBluegigaPeripheral.readTXpower((response, data) -> {
                if (response != BlueteethResponse.NO_ERROR) {
                    return;
                }
                //runOnUiThread(() -> mTXpower.setText(String.format("TX Power: %d", data.length)));
                runOnUiThread(() -> mTXpower.setText(spinnerTx.getSelectedItem().toString()));
            });

            //--------------------------
            //ReadGPINstop
            mBluegigaPeripheral.readGPINstop(((response, data) -> {
                if (response !=BlueteethResponse.NO_ERROR) {
                    return;
            }
            runOnUiThread(() -> mRecStopPin.setText(String.format(getString(R.string.RecStopPin), ByteString.of(data, 0, data.length).utf8())));
            }));
            //--------------//

            //----- Read Group Name ------
            mBluegigaPeripheral.readGroupName(((response, data) -> {
                if (response !=BlueteethResponse.NO_ERROR) {
                    return;
                }
                runOnUiThread(() -> mGroupName.setText(String.format(getString(R.string.group_name), ByteString.of(data, 0, data.length).utf8())));
            }));


        } else {
            //updateReceivedData(String.format("Attempting to connect to  %s - %s...", mSamplePeripheral.getName(), mSamplePeripheral.getMacAddress()));
            mBluegigaPeripheral.connect(true, isConnected -> {
                //updateReceivedData("Connection Status: " + Boolean.toString(isConnected));
                mIsConnected = isConnected;
                Timber.d("nitaa connected...");
                //runOnUiThread(mConnectionRunnable);
            });
        }
    }
-------------*/

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        ButterKnife.bind(this);

        String macAddress = getIntent().getStringExtra(getString(R.string.extra_mac_address));
        mBluegigaPeripheral = new BluegigaPeripheral(BlueteethManager.with(this).getPeripheral(macAddress));

        Spinner spinnerTx = (Spinner)findViewById(R.id.txpower);
        final String[] txpower = {" +8.0 dbm ", " +7.5 dbm ", " +7.0 dbm ", " +6.5 dbm ", " +6.0 dbm ", " +5.5 dbm ", " +5.0 dbm ",
                " +5.0 dbm ", " +4.5 dbm ", " +4.0 dbm ", " +3.5 dbm ", " +3.0 dbm ", " +2.5 dbm ", " +2.0 dbm ", " +1.5 dbm ",
                " +1.0 dbm ", " +0.5 dbm ","     0 dbm ", " -0.5 dbm", " -1.0 dbm ", " -1.5 dbm ", " -2.0 dbm ", " -2.5 dbm ",
                " -3.0 dbm ", " -3.5 dbm ", " -4.0 dbm ", " -4.5 dbm ", " -5.0 dbm ", " -5.5 dbm ", " -6.0 dbm ", " -6.5 dbm ",
                " -7.0 dbm ", " -7.5 dbm ", " -8.0 dbm "};
        ArrayAdapter<String> txpowerList = new ArrayAdapter<>(OtaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                txpower);

        spinnerTx.setAdapter(txpowerList);
        spinnerTx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OtaActivity.this, "You Set Transmit Duration :" + txpower[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //----- Read Firmware Version ---------
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

                    runOnUiThread(() -> mTXpower.setText(spinnerTx.getSelectedItem().toString()));

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
                            runOnUiThread(() -> mRecStopPin.setText(String.format(getString(R.string.RecStopPin), ByteString.of(data5, 0, data5.length).utf8())));
                        }));
                        // ---- Read GPIN stop
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
