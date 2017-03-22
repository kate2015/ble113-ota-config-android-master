package com.robotpajamas.android.ble113_ota.ui;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.robotpajamas.android.ble113_ota.R;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethDevice;
import com.robotpajamas.android.ble113_ota.blueteeth.BlueteethManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends ListActivity {

    private static final int REQ_BLUETOOTH_ENABLE = 1000;
    private static final int DEVICE_SCAN_MILLISECONDS = 10000;

    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefresh;
    private DeviceScanListAdapter mDeviceAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // If BLE support isn't there, quit the app
        checkBluetoothSupport();

        mSwipeRefresh.setOnRefreshListener(this::startScanning);
        mDeviceAdapter = new DeviceScanListAdapter(this);
        setListAdapter(mDeviceAdapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mDeviceAdapter.clear();

        // Start automatic scan
        mSwipeRefresh.setRefreshing(true);
        startScanning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScanning();
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        stopScanning();

        BlueteethDevice blueteethDevice = mDeviceAdapter.getItem(position);
        final Intent intent = new Intent(this, OtaActivity.class);
        intent.putExtra(getString(R.string.extra_mac_address), blueteethDevice.getMacAddress());
        startActivity(intent);
    }

    private void startScanning() {
        // Clear existing devices (assumes none are connected)
        Timber.d("Start scanning");
        mDeviceAdapter.clear();
        BlueteethManager.with(this).scanForPeripherals(DEVICE_SCAN_MILLISECONDS,
                blueteethDevice -> {
                    Timber.d("%s - %s", blueteethDevice.getName(), blueteethDevice.getMacAddress());
                    mDeviceAdapter.add(blueteethDevice);
                }, blueteethDevices -> {
                    Timber.d("On Scan completed");
                    mSwipeRefresh.setRefreshing(false);
                });
    }

    private void stopScanning() {
        // Update the button, and shut off the progress bar
        mSwipeRefresh.setRefreshing(false);
        BlueteethManager.with(this).stopScanForPeripherals();
    }

    private void checkBluetoothSupport() {
        // Check for BLE support - also checked from Android manifest.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            exitApp("No BLE Support...");
        }

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            exitApp("No BLE Support...");
        }

        //noinspection ConstantConditions
        if (!btAdapter.isEnabled()) {
            enableBluetooth();
        }
    }

    private void exitApp(String reason) {
        // Something failed, exit the app and send a toast as to why
        Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_LONG).show();
        finish();
    }

    private void enableBluetooth() {
        // Ask user to enable bluetooth if it is currently disabled
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQ_BLUETOOTH_ENABLE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}