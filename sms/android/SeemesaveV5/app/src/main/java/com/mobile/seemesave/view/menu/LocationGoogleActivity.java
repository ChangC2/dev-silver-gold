package com.mobile.seemesave.view.menu;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.PlacesAutoCompleteAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.menu.LocationViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationGoogleActivity extends BaseActivity implements OnMapReadyCallback, PlacesAutoCompleteAdapter.ClickListener {
    private LocationViewModel mViewModel;

    GoogleMap mMap;
    Marker marker;

    int DEFAULT_ZOOM = 18;

    @BindView(R.id.imgBack)
    ImageView btnBack;

    @BindView(R.id.places_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.place_search)
    EditText editSearch;


    double lat = -28.48322, lon = 24.676997;
    double store_lat = 0, store_lon = 0;
    String fullAddress = "";

    ArrayList<StoreModel> stores = new ArrayList<>();

    private boolean deliver_flag = false;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;

    private LocationGoogleActivity activity;

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        setContentView(R.layout.activity_location_google);
        activity = this;
        G.setLightFullScreen(activity);
        ButterKnife.bind(activity);
        deliver_flag = getIntent().getBooleanExtra("deliver", false);
        if (deliver_flag) {
            lat = getIntent().getDoubleExtra("store_lat", 0.00);
            lon = getIntent().getDoubleExtra("store_lon", 0.00);
            store_lat = getIntent().getDoubleExtra("store_lat", 0.00);
            store_lon = getIntent().getDoubleExtra("store_lon", 0.00);
        } else {
            lat = G.user.getLatitude();
            lon = G.user.getLongitude();
        }
        fullAddress = G.getAddress();
        initUI();
    }
    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getStoreList().observe(this, list -> {
            if (list != null && list.size()>0) {
                stores.addAll(list);
                drawStores();
            }
        });
    }
    private void initUI() {
        initMapService();
        setUpPlaceAutoComplete();
    }

    private void initMapService() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);
    }
    private void setUpPlaceAutoComplete() {
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        editSearch.addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    private void onUpdate() {
        Intent intent = new Intent();
        // add data into intent and send back to Parent Activity
        if (deliver_flag) {
            intent.putExtra("lat", store_lat);
            intent.putExtra("lon", store_lon);
        } else {
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
        }
        intent.putExtra("fullAddress", fullAddress);
        setResult(ADDRESS_PICKER_REQUEST, intent);
        finish();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        if (map == null) return;
        mMap = map;
        map.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                if (deliver_flag) {
                    store_lat = arg0.latitude;
                    store_lon = arg0.longitude;
                } else {
                    lat = arg0.latitude;
                    lon = arg0.longitude;
                }

                Geocoder gcd = new Geocoder(activity,
                        Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(lat,
                            lon, 1);
                    if (addresses.size() > 0) {
                        fullAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (deliver_flag) {
                    int dist = (int) G.meterDistanceBetweenPoints(store_lat, store_lon, lat, lon);
                    Log.e("deliver_flag", dist + "");
                    if (dist <= 50000) {
                        map.clear();
                        drawMyMarker();
                        drawDeliver();
                    }
                } else {
                    map.clear();
                    drawMyMarker();
                    mViewModel.loadData(lat, lon);
                }
            }
        });
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        drawMyMarker();
        if (G.is_login() && !deliver_flag) {
            mViewModel.loadData(lat, lon);
        }
    }
    private void drawMyMarker() {
        if (mMap == null) return;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitmapdraw = (BitmapDrawable) getDrawable(R.drawable.ic_my_loc);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);

        MarkerOptions usermo = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("Store Location");
        marker = mMap.addMarker(usermo);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lon), DEFAULT_ZOOM));
        drawCircleOnMap();
    }
    private void drawStores() {
        BitmapDrawable clickCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(activity, R.drawable.ic_marker_blue);
        BitmapDrawable deliverCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(activity, R.drawable.ic_marker_grey);
        Bitmap a = clickCollectDrw.getBitmap();
        Bitmap b = deliverCollectDrw.getBitmap();
        Bitmap clickCollectIcon = Bitmap.createScaledBitmap(a, 50, 60, false);
        Bitmap deliverCollectIcon = Bitmap.createScaledBitmap(b, 50, 60, false);
        for (int i = 0; i < stores.size(); i++) {
            try {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(stores.get(i).isIs_click_collect() ? clickCollectIcon : deliverCollectIcon);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(stores.get(i).getCoordinates().get(0), stores.get(i).getCoordinates().get(1)))
                        .icon(icon)
                        .title(stores.get(i).getName());

                Marker storeMarker = mMap.addMarker(markerOptions);
                storeMarker.setTag(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void drawDeliver() {
        BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(activity, R.drawable.ic_store_red);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(store_lat, store_lon));
//            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(markerOptions);
    }

    private Circle circle = null;

    private void drawCircleOnMap() {
        if (mMap == null) return;
        if (circle == null) {
            double iMeter = 50000.0;
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .radius(iMeter) // Converting Miles into Meters...
                    .strokeColor(Color.parseColor("#60a7db"))
                    .fillColor(Color.parseColor("#400084d3"))
                    .strokeWidth(5));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 9));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(9), 1500, null);
        } else {
            circle.remove();
            circle = null;
            drawCircleOnMap();
        }
    }
    @Override
    public void click(Place place) {
        fullAddress = place.getAddress();
        recyclerView.setVisibility(View.GONE);
        editSearch.setText("");
        G.hideSoftKeyboard(activity);

        if (deliver_flag) {
            store_lat = place.getLatLng().latitude;
            store_lon = place.getLatLng().longitude;
            int dist = (int) G.meterDistanceBetweenPoints(store_lat, store_lon, lat, lon);
            if (dist <= 50000) {
                if (mMap != null)
                    mMap.clear();
                drawMyMarker();
                drawDeliver();
            }
        } else {
            lat = place.getLatLng().latitude;
            lon = place.getLatLng().longitude;
            if (mMap != null)
                mMap.clear();
            drawMyMarker();
            mViewModel.loadData(lat, lon);
        }
    }
    @OnClick({R.id.btnSave, R.id.imgBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnSave:
                onUpdate();
                break;
        }
    }
}
