package com.cam8.icsapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {

    private long showStartTime;
    private final static long DELAY_TIME = 1500;
    private boolean isRunning;

    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(getVersionName());

        startAnimation();
        startSplash();

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
                            mAnimatorSet.setDuration(700);
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
            checkDeviceSettings();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isRunning) {
            this.isRunning = false;
        }
        finish();
    }

    private void checkDeviceSettings() {
        // Check Bluetooth Setting & Check App Version
        if (checkBLEService()) {
            Intent newIntent = new Intent(mContext, MainActivity.class);
            startActivity(newIntent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();
        }
    }

    private boolean checkBLEService() {

        if (isEmulator())
            return true;

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
            showToastMessage("Please allow Bluetooth to use app function");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }

        // Make the device to be discoverable by other devices for 60 seconds
        /*Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
        startActivity(discoverableIntent);*/
        // ---------------------------------------------------------------------------------

        return true;
    }

    private boolean isEmulator() {
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
                checkDeviceSettings();
            }
        }
    }
}