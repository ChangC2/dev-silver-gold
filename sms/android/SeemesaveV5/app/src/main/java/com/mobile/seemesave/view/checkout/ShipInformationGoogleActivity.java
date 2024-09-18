package com.mobile.seemesave.view.checkout;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;
import static com.mobile.seemesave.utils.G.isValidMobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.DeliverShipModel;
import com.mobile.seemesave.model.req.CheckoutDeliverReq;
import com.mobile.seemesave.model.res.CheckoutRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.menu.LocationGoogleActivity;
import com.mobile.seemesave.view_model.checkout.CheckOutDeliverViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShipInformationGoogleActivity extends BaseActivity implements OnMapReadyCallback {

    @BindView(R.id.name)
    EditText editName;

    @BindView(R.id.editEmail)
    EditText editEmail;

    @BindView(R.id.editMobile)
    EditText editMobile;

    @BindView(R.id.country_picker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.street1)
    EditText editStreet1;

    @BindView(R.id.street2)
    EditText editStreet2;

    @BindView(R.id.suburb)
    EditText editSuburb;

    @BindView(R.id.city)
    EditText editCity;

    @BindView(R.id.state)
    EditText editState;

    @BindView(R.id.country)
    EditText editCountry;

    @BindView(R.id.postal_code)
    EditText editPostalCode;

    @BindView(R.id.btnPay)
    LinearLayout btnPay;

    @BindView(R.id.selLocation)
    TextView selLocation;

    private CheckOutDeliverViewModel mViewModel;
    private ShipInformationGoogleActivity activity;

    double lat = -28.48322, lon = 24.676997;
    double store_lat = 0.00, store_lon = 0.00;
    GoogleMap mMap;
    Marker marker;
    Circle circle = null;
    int DEFAULT_ZOOM = 5;

    String description = "";
    String currency = "";
    int cart_id = -1;
    DeliverShipModel shipData = new DeliverShipModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutDeliverViewModel.class);
        setContentView(R.layout.activity_ship_information);
        ButterKnife.bind(this);
        activity = this;
        cart_id = getIntent().getIntExtra("cart_id", -1);
        Gson gson = new Gson();
        shipData = gson.fromJson(getIntent().getStringExtra("shipData"), DeliverShipModel.class);
        description = getIntent().getStringExtra("description");
        currency = getIntent().getStringExtra("currency");
        lat = getIntent().getDoubleExtra("store_lat", 0.00);
        lon = getIntent().getDoubleExtra("store_lon", 0.00);
        store_lat = getIntent().getDoubleExtra("store_lat", 0.00);
        store_lon = getIntent().getDoubleExtra("store_lon", 0.00);
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    private void initView() {
        initMapService();
        setData();
    }
    private void setData() {
        editName.setText(shipData.getDelivery_name());
        if (!TextUtils.isEmpty(shipData.getDelivery_contact_number())) {
            if (shipData.getDelivery_contact_number().split(" ").length > 1) {
                String country_code = shipData.getDelivery_contact_number().split(" ")[0];
                String phone_number = shipData.getDelivery_contact_number().split(" ")[1];
                try {
                    countryCodePicker.setCountryForPhoneCode(Integer.valueOf(country_code));
                } catch (Exception e) {
                    countryCodePicker.setCountryForPhoneCode(27);
                }
                editMobile.setText(phone_number);
            } else {
                countryCodePicker.setCountryForPhoneCode(27);
            }
        } else {
            countryCodePicker.setCountryForPhoneCode(27);
        }
        editEmail.setText(shipData.getDelivery_email());
        editPostalCode.setText(shipData.getDelivery_postal_code());
        editStreet1.setText(shipData.getDelivery_street1());
        editStreet2.setText(shipData.getDelivery_street2());
        editSuburb.setText(shipData.getDelivery_suburb());
        editCity.setText(shipData.getDelivery_city());
        editState.setText(shipData.getDelivery_state());
        editCountry.setText(shipData.getDelivery_country());
    }
    private boolean validateParam() {
        if (G.isNetworkAvailable(activity)) {
            int dist = (int) G.meterDistanceBetweenPoints(store_lat, store_lon, lat, lon);;
            if (dist > 50000) {
                Toast.makeText(activity, R.string.msg_receiptor_location_scale, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editName.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editMobile.getText().toString().trim())) {
                Toast.makeText(activity, R.string.msg_receiptor_number, Toast.LENGTH_LONG).show();
                return false;
            }
            if (!isValidMobile(editMobile.getText().toString().trim())) {
                Toast.makeText(activity, R.string.msg_valid_receiptor_number, Toast.LENGTH_LONG).show();
                return false;
            }
            if (!G.isValidEmail(editEmail.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_email, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editStreet1.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_street1, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editSuburb.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_suburb, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editCity.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_city, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editState.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_state, Toast.LENGTH_LONG).show();
                return false;
            }
            if (TextUtils.isEmpty(editCountry.getText().toString())) {
                Toast.makeText(activity, R.string.msg_receiptor_country, Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else {
            Toast.makeText(activity, R.string.msg_offline, Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private void onPay() {
        if (validateParam()) {
            ClickListener listener = new ClickListener() {
                @Override
                public void onClick(boolean flag) {
                    CheckoutDeliverReq req = new CheckoutDeliverReq();
                    req.setId(cart_id);
                    req.setCurrency(currency);
                    req.setDelivery_note(description);
                    req.setBag_string("");
                    req.setDelivery_name(editName.getText().toString());
                    req.setDelivery_contact_number(countryCodePicker.getSelectedCountryCode() + " " + editMobile.getText().toString().trim());
                    req.setDelivery_email(editEmail.getText().toString());
                    req.setDelivery_postal_code(editPostalCode.getText().toString());
                    req.setDelivery_street1(editStreet1.getText().toString());
                    req.setDelivery_street2(editStreet2.getText().toString());
                    req.setDelivery_suburb(editSuburb.getText().toString());
                    req.setDelivery_city(editCity.getText().toString());
                    req.setDelivery_state(editState.getText().toString());
                    req.setDelivery_country(editCountry.getText().toString());
                    req.setDelivery_latitude(String.valueOf(lat));
                    req.setDelivery_longitude(String.valueOf(lon));
                    mViewModel.checkOut(req);
                }
            };
            G.showDlg(activity, "SeeMeSave does not accept any cancellation of orders, all cancellations and refunds have to be negotiated with the relevant store that you are purchasing from.", listener, true);
        }
    }
    private void initMapService() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);
    }
    private void onSelectLocation() {
        Intent i = new Intent(activity, LocationGoogleActivity.class);
        i.putExtra("deliver", true);
        i.putExtra("store_lat", store_lat);
        i.putExtra("store_lon", store_lon);
        startActivityForResult(i, ADDRESS_PICKER_REQUEST);
    }
    @OnClick({R.id.imgBack, R.id.selLocation, R.id.btnPay})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.selLocation:
                onSelectLocation();
                break;
            case R.id.btnPay:
                onPay();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        mMap = map;
        map.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
//                lat = arg0.latitude;
//                lon = arg0.longitude;
//                map.clear();
//                drawMyMarker();
            }
        });
        drawMyMarker();
    }
    private void drawMyMarker() {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_my_loc);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 40, 40, false);

        MarkerOptions usermo = new MarkerOptions()
                .position(new LatLng(store_lat, store_lon))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("Receipter Location");
        marker = mMap.addMarker(usermo);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(store_lat,
                        store_lon), DEFAULT_ZOOM));
        drawCircleOnMap();
    }
    private void drawCircleOnMap() {
        if (mMap == null) return;
        if (circle == null) {
            double iMeter = 50000.0;
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(store_lat, store_lon))
                    .radius(iMeter) // Converting Miles into Meters...
                    .strokeColor(Color.parseColor("#60a7db"))
                    .fillColor(Color.parseColor("#400084d3"))
                    .strokeWidth(5));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store_lat, store_lon), 6));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
        } else {
            circle.remove();
            circle = null;
            drawCircleOnMap();
        }
    }
    private void drawDeliver() {
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker_blue);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 35, 50, false);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(markerOptions);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("fullAddress") != null) {
                    lat = data.getDoubleExtra("lat", 0.0);
                    lon = data.getDoubleExtra("lon", 0.0);
                    mMap.clear();
                    drawMyMarker();
                    drawDeliver();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCheckoutResult(CheckoutRes res) {
        if (res.isStatus()) {
            Intent intent = new Intent(activity, PaymentActivity.class);
            intent.putExtra("id", cart_id);
            intent.putExtra("deliver", true);
            startActivity(intent);
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
