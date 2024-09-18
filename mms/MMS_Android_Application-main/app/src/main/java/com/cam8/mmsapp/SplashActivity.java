package com.cam8.mmsapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SplashActivity extends BaseActivity {

    private long showStartTime;
    private final static long DELAY_TIME = 800;
    private boolean isRunning;

    TextView tvVersion;
    TextView tvTestVersion;

    public static byte[] calculateCRC(byte[] data, int numberOfBytes, int startByte) {
        byte[] auchCRCHi = new byte[]{0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64};
        byte[] auchCRCLo = new byte[]{0, -64, -63, 1, -61, 3, 2, -62, -58, 6, 7, -57, 5, -59, -60, 4, -52, 12, 13, -51, 15, -49, -50, 14, 10, -54, -53, 11, -55, 9, 8, -56, -40, 24, 25, -39, 27, -37, -38, 26, 30, -34, -33, 31, -35, 29, 28, -36, 20, -44, -43, 21, -41, 23, 22, -42, -46, 18, 19, -45, 17, -47, -48, 16, -16, 48, 49, -15, 51, -13, -14, 50, 54, -10, -9, 55, -11, 53, 52, -12, 60, -4, -3, 61, -1, 63, 62, -2, -6, 58, 59, -5, 57, -7, -8, 56, 40, -24, -23, 41, -21, 43, 42, -22, -18, 46, 47, -17, 45, -19, -20, 44, -28, 36, 37, -27, 39, -25, -26, 38, 34, -30, -29, 35, -31, 33, 32, -32, -96, 96, 97, -95, 99, -93, -94, 98, 102, -90, -89, 103, -91, 101, 100, -92, 108, -84, -83, 109, -81, 111, 110, -82, -86, 106, 107, -85, 105, -87, -88, 104, 120, -72, -71, 121, -69, 123, 122, -70, -66, 126, 127, -65, 125, -67, -68, 124, -76, 116, 117, -75, 119, -73, -74, 118, 114, -78, -77, 115, -79, 113, 112, -80, 80, -112, -111, 81, -109, 83, 82, -110, -106, 86, 87, -105, 85, -107, -108, 84, -100, 92, 93, -99, 95, -97, -98, 94, 90, -102, -101, 91, -103, 89, 88, -104, -120, 72, 73, -119, 75, -117, -118, 74, 78, -114, -113, 79, -115, 77, 76, -116, 68, -124, -123, 69, -121, 71, 70, -122, -126, 66, 67, -125, 65, -127, -128, 64};
        short usDataLen = (short)numberOfBytes;
        byte uchCRCHi = -1;
        byte uchCRCLo = -1;

        for(int i = 0; usDataLen > 0; ++i) {
            --usDataLen;
            int uIndex = uchCRCLo ^ data[i + startByte];
            if (uIndex < 0) {
                uIndex += 256;
            }

            uchCRCLo = (byte)(uchCRCHi ^ auchCRCHi[uIndex]);
            uchCRCHi = auchCRCLo[uIndex];
        }

        byte[] returnValue = new byte[]{uchCRCLo, uchCRCHi};
        return returnValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // --- CRC Calc Test -----------------------------------------------------------------------
        //int[] pszBuf = new int[]{ 0x01, 0x03, 0xd0, 0x00, 0x00, 0x26 };
        // 0x01 0x03 0xD0 0x00 0x00 0x26 0xFC 0xD0
        byte[] pszBufB = new byte[]{ 0x01, 0x03, (byte)0xd0, 0x00, 0x00, 0x26 };

        byte[] xx = calculateCRC(pszBufB, 6, 0);
        String strTestHexVal = String.format("0x%X%X", xx[0], xx[1]);

        int unLength = 6;
        int CRC = 0xFFFF;
        int CRC_count;
        for(CRC_count = 0; CRC_count < unLength; CRC_count++){
            int i;

            int byteVal = ((int) pszBufB[CRC_count]) & 0xff;

            CRC = CRC ^ byteVal;
            for(i = 0; i < 8; i++) {
                if ((CRC & 1) > 0) {
                    CRC >>= 1;
                    CRC ^= 0xA001;
                } else {
                    CRC >>=1;
                }
            }
        }

        byte highByte = (byte) (CRC>>8);
        byte lowByte = (byte) (CRC & 0xFF);
        String crcString = String.format("0x%X%X", lowByte, highByte);
        // -----------------------------------------------------------------------------------------


        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(getVersionName());

        tvTestVersion = (TextView) findViewById(R.id.tvTestVersion);
        if (MyApplication.TEST_VERSION) {
            tvTestVersion.setVisibility(View.VISIBLE);
        } else {
            tvTestVersion.setVisibility(View.GONE);
        }

        startAnimation();
        startSplash();

        /*DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        dbHelper.removeAllGanttData();
        dbHelper.close();*/

        // Get KeyHassh
        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startAnimation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            tvVersion.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tvVersion.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            AnimatorSet mAnimatorSet = new AnimatorSet();
                            mAnimatorSet.playTogether(ObjectAnimator.ofFloat(tvVersion, "alpha", 0, 1, 1, 1),
                                    ObjectAnimator.ofFloat(tvVersion, "scaleX", 0.3f, 1.05f, 0.9f, 1),
                                    ObjectAnimator.ofFloat(tvVersion, "scaleY", 0.3f, 1.05f, 0.9f, 1));
                            mAnimatorSet.setDuration(1500);
                            mAnimatorSet.start();
                        }
                    });
        }
    }

    private void startSplash() {

        showStartTime = System.currentTimeMillis();
        isRunning = true;

        Thread background = new Thread() {
            public void run() {
                try {
                    // Delay Time
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - showStartTime < DELAY_TIME) {
                        try {
                            // Delay for DELAY_TIME
                            Thread.sleep(showStartTime + DELAY_TIME
                                    - currentTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                } catch (Exception e) {
                    return;
                } finally {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            doFinish();
                        }
                    });
                }
            }
        };

        background.start();
    }

    private void doFinish() {
        if (this.isRunning) {
            gotoMainScreen();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isRunning) {
            this.isRunning = false;
        }
        finish();
    }

    private void gotoMainScreen() {
        // Check Device Capabilities for operation in the Main Screen.
        // Check Bluetooth Setting & Check App Version
        if (checkBLEService() && isBluetoothPermissionAllowedOverAndroid12()) {
            Intent newIntent = new Intent(mContext, MainActivity.class);
            startActivity(newIntent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }
    }

    private boolean checkBLEService() {

        if (isEmulator()) {
            appSettings.setBTDeviceAddr("12:34:56:78:90:AB");
            appSettings.setBTDeviceName("BT Device");
            return true;
        }

        BluetoothAdapter bluetoothAdapter;
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            showAlert(R.string.error_bluetooth_not_supported, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            showToastMessage(R.string.error_bluetooth_not_enabled);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }

        // Make the device to be discoverable by other devices for 60 seconds
        /* Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
        startActivity(discoverableIntent);*/
        // ---------------------------------------------------------------------------------

        return true;
    }

    private boolean isEmulator() {
        Log.e("emu", "Finger:" + Build.FINGERPRINT);
        Log.e("emu", "Model:" + Build.MODEL);
        Log.e("emu", "BOARD:" + Build.BOARD);
        Log.e("emu", "MANUFACTURER:" + Build.MANUFACTURER);
        Log.e("emu", "HOST:" + Build.HOST);
        Log.e("emu", "BRAND:" + Build.BRAND);
        Log.e("emu", "DEVICE:" + Build.DEVICE);
        Log.e("emu", "PRODUCT:" + Build.PRODUCT);

        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.BOARD == "QC_Reference_Phone" //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST.startsWith("Build") //MSI App Player
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode == Activity.RESULT_CANCELED) {
                showAlert(R.string.error_bluetooth_not_enabled, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            } else {
                gotoMainScreen();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check All Permission was granted
        boolean bAllGranted = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                bAllGranted = false;
                break;
            }
        }

        if (requestCode == PERMISSION_REQUEST_CODE_BLUETOOTH) {
            if (bAllGranted) {
                gotoMainScreen();
            } else {
                showAlert("Need Bluetooth Connect permissions to connect PLC.");
            }
        }
    }
}