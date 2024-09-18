package seemesave.businesshub.view.vendor.menu;

import static seemesave.businesshub.utils.G.ADDRESS_PICKER_REQUEST;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.PlacesAutoCompleteAdapter;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.utils.G;

public class LocationGoogleActivity extends BaseActivity implements OnMapReadyCallback, PlacesAutoCompleteAdapter.ClickListener {

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
    private String fullAddress = "";

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
        setContentView(R.layout.activity_location_google);
        activity = this;
        G.setLightFullScreen(activity);
        ButterKnife.bind(activity);
        initUI();
    }
    @Override
    public void onStart() {
        super.onStart();
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

        Intent intent = new Intent();
        intent.putExtra("fullAddress", fullAddress);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
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
                lat = arg0.latitude;
                lon = arg0.longitude;
                map.clear();
                drawMyMarker();
            }
        });
        drawMyMarker();
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

        lat = place.getLatLng().latitude;
        lon = place.getLatLng().longitude;
        if (mMap != null)
            mMap.clear();
        drawMyMarker();
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
