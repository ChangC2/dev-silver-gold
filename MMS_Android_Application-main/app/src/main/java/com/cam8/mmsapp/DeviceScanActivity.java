/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cam8.mmsapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.MenuBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends BaseActivity {

    private BluetoothAdapter mBluetoothAdapter;

    private DeviceListAdapter listAdapter;
    private ArrayList<BluetoothDevice> listItems = new ArrayList<>();


    private boolean mScanning;
    private Handler mHandler;
    private Runnable mRunnable;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    ListView lvDevices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_devicescan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.title_devices);
        }

        lvDevices = findViewById(R.id.lvDevices);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                scanLeDevice(false);

                // Check Device Availability
                checkDeviceIsAvailableStatus();
            }
        };

        // Checks if Bluetooth is supported on the device.
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listAdapter.addDevice(device);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        if (!isBluetoothPermissionAllowedOverAndroid12()) {
            showToastMessage("Please allow Bluetooth Connect permission to connect PLC.");
        }

        // Initializes list view DeviceListAdapter.
        listAdapter = new DeviceListAdapter();
        lvDevices.setAdapter(listAdapter);
        scanLeDevice(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void connectDevice(BluetoothDevice device) {
        Intent intent = new Intent(mContext, DeviceTestActivity.class);
        intent.putExtra("device", device.getAddress());
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }

        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);

                    Field field = menu.getClass().
                            getDeclaredField("mOptionalIconsVisible");
                    field.setAccessible(true);
                    field.setBoolean(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                listAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);

                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        listAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(mRunnable, SCAN_PERIOD);

            mScanning = true;

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    listAdapter.addDevice(device);
                }
            }

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(bReciever, filter);
            mBluetoothAdapter.startDiscovery();
        } else {
            mHandler.removeCallbacks(mRunnable);

            mScanning = false;

            try{
                unregisterReceiver(bReciever);
            } catch (Exception e){
                e.printStackTrace();
            }

            mBluetoothAdapter.cancelDiscovery();
        }
        invalidateOptionsMenu();
    }

    private void checkDeviceIsAvailableStatus() {
        if (listAdapter.getCount() == 0) {
            final Dialog customDlg = new Dialog(this);
            customDlg.setContentView(R.layout.dialog_choose_ble_action);
            customDlg.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDlg.dismiss();
                }
            });

            customDlg.findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    customDlg.dismiss();

                    // Show Bluetooth Setting Screen
                    startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                }
            });

            customDlg.show();
        }
    }

    // Adapter for holding devices found through scanning.
    private class DeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public DeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.btnSelect = (TextView) view.findViewById(R.id.btnSelect);
                viewHolder.btnTest = (TextView) view.findViewById(R.id.btnTestConnect);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            viewHolder.deviceAddress.setText(device.getAddress());

            viewHolder.btnSelect.setTag(device);
            viewHolder.btnSelect.setOnClickListener(deviceSelectListener);

            viewHolder.btnTest.setTag(device);
            viewHolder.btnTest.setOnClickListener(deviceTestListener);
            return view;
        }

        View.OnClickListener deviceSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice device = (BluetoothDevice) view.getTag();
                appSettings.setBTDeviceName(device.getName());
                appSettings.setBTDeviceAddr(device.getAddress());
                finish();
            }
        };

        View.OnClickListener deviceTestListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice device = (BluetoothDevice) view.getTag();
                connectDevice(device);
            }
        };
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView btnSelect;
        TextView btnTest;
    }
}