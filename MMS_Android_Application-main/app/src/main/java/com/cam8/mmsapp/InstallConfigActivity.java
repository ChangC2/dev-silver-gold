package com.cam8.mmsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.adapters.InstallImageAdapter;
import com.cam8.mmsapp.model.ImageInfo;
import com.cam8.mmsapp.network.APIServerUtil;
import com.cam8.mmsapp.network.ITSRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class InstallConfigActivity extends BaseActivity implements View.OnClickListener {

    EditText edtMachineName;
    EditText edtSerialNumber;

    EditText edtIncycleSignal;

    SwitchCompat switchOnOff;
    SwitchCompat switchNormalAbnormal;

    EditText edtCycleStartInterlockInterface;

    RecyclerView rcvImages;
    InstallImageAdapter installImageAdapter;
    ArrayList<ImageInfo> imageInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_install_config);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_install_configure);

        // Check Factory and Account Settings.
        if (TextUtils.isEmpty(appSettings.getCustomerID()) || TextUtils.isEmpty(appSettings.getMachineName())) {
            showAlert("Please set up factory and machine settings to set installation configuration.", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return;
        }

        edtMachineName = findViewById(R.id.edtMachineName);
        edtSerialNumber = findViewById(R.id.edtSerialNumber);

        edtIncycleSignal = findViewById(R.id.edtIncycleSignal);

        switchOnOff = findViewById(R.id.switchOnOff);
        switchNormalAbnormal = findViewById(R.id.switchNormalAbnormal);

        edtCycleStartInterlockInterface = findViewById(R.id.edtCycleStartInterlockInterface);

        findViewById(R.id.btnSave).setOnClickListener(this);

        rcvImages = findViewById(R.id.rcvImages);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rcvImages.setLayoutManager(layoutManager);
        rcvImages.hasFixedSize();
        installImageAdapter = new InstallImageAdapter(mContext, imageInfoArrayList, new InstallImageAdapter.OnImageListener() {
            @Override
            public void onAddImage() {

                if (checkPermissions(mContext, PERMISSION_REQUEST_IMAGE_STRING, false, PERMISSION_REQUEST_CODE_IMAGE)) {
                    ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
                        @Override
                        public void onTakeCameraSelected() {
                            Intent intent = new Intent(mContext, ImagePickerActivity.class);
                            intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

                            // setting aspect ratio
                            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

                            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 700);
                            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 700);

                            startActivityForResult(intent, 100);
                        }

                        @Override
                        public void onChooseGallerySelected() {
                            Intent intent = new Intent(mContext, ImagePickerActivity.class);
                            intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

                            // setting aspect ratio
                            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

                            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 700);
                            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 700);

                            startActivityForResult(intent, 100);
                        }
                    });
                }
            }

            @Override
            public void onRemoveImage(int position) {
                removeImage(position);
            }
        });
        rcvImages.setAdapter(installImageAdapter);

        getInstallationData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSave) {
            saveInstallationConfig();
        }
    }

    private void getInstallationData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("factory_id", appSettings.getCustomerID());
        requestParams.put("machine_id", appSettings.getMachineName());

        showProgressDialog();
        ITSRestClient.post(mContext, "get_mechine_details", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();

                Log.e("get_mechine_details", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        JSONObject detailsJson = response.getJSONObject("machine_details");
                        edtMachineName.setText(detailsJson.optString("machine_name"));
                        edtSerialNumber.setText(detailsJson.optString("serial_number"));
                        edtIncycleSignal.setText(detailsJson.optString("cycle_signal"));
                        edtCycleStartInterlockInterface.setText(detailsJson.optString("cycle_interlock_interface"));
                        switchOnOff.setChecked(detailsJson.optInt("cycle_interlock_on") > 0);
                        switchNormalAbnormal.setChecked(detailsJson.optInt("cycle_interlock_open") > 0);

                        // Get Images
                        imageInfoArrayList.clear();
                        JSONArray imageData = response.getJSONArray("images");
                        for (int i = 0; i < imageData.length(); i++) {
                            JSONObject jsonImage = imageData.getJSONObject(i);
                            imageInfoArrayList.add(new ImageInfo(jsonImage.optInt("id"),
                                    //"https://api.slymms.com/" + jsonImage.optString("image"),
                                    APIServerUtil.getUrl(mContext, APIServerUtil.APIType.resource, jsonImage.optString("image")),
                                    true));
                        }
                        installImageAdapter.notifyDataSetChanged();
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }
                        showAlert(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }
                showAlert(errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
            }
        });
    }

    private void uploadNewImage(Uri imgUri) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("factory_id", appSettings.getCustomerID());
        requestParams.put("machine_id", appSettings.getMachineName());

        try {
            requestParams.put("image", new File(imgUri.getPath()), "image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToastMessage("File is not exist!");
            return;
        }

        showProgressDialog();
        ITSRestClient.post(mContext, "upload_machine_image", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("Image", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        String image = response.getString("image");
                        getInstallationData();
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }
                        showAlert(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }
                showAlert(errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("completeMaintenanceTask", errorMsg);
            }
        });
    }

    private void removeImage(int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("factory_id", appSettings.getCustomerID());
        requestParams.put("id", imageInfoArrayList.get(position).getId());

        showProgressDialog();
        ITSRestClient.post(mContext, "delete_machine_image", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();

                //Log.e("removeImg", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        showToastMessage("Removed success!");
                        getInstallationData();
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }
                        showAlert(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }
                showAlert(errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("error", errorMsg);
            }
        });
    }

    private void saveInstallationConfig() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("factory_id", appSettings.getCustomerID());
        requestParams.put("machine_id", appSettings.getMachineName());

        requestParams.put("machine_name", edtMachineName.getText().toString().trim());
        requestParams.put("serial_number", edtSerialNumber.getText().toString().trim());
        requestParams.put("cycle_signal", edtIncycleSignal.getText().toString().trim());
        requestParams.put("cycle_interlock_interface", edtCycleStartInterlockInterface.getText().toString().trim());

        requestParams.put("cycle_interlock_on", switchOnOff.isChecked() ? 1 : 0);
        requestParams.put("cycle_interlock_open", switchNormalAbnormal.isChecked() ? 1 : 0);

        showProgressDialog();
        ITSRestClient.post(mContext, "set_machine_install_Config", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();

                Log.e("install_Config", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        showToastMessage("Save success!");
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }
                        showAlert(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }
                showAlert(errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("completeMaintenanceTask", errorMsg);
            }
        });
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

        if (requestCode == PERMISSION_REQUEST_CODE_IMAGE) {
            if (bAllGranted) {

            } else {
                showAlert("Need permissions to read image.");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri uri = data.getParcelableExtra("path");
            uploadNewImage(uri);
        }
    }
}
