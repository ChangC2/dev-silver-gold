package seemesave.businesshub.view.supplier.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseFragment;

public class HeatmapFragment extends BaseFragment implements OnMapReadyCallback {
    private View mFragView;


    double store_lat = -28.48322, store_lon = 24.676997;
    GoogleMap mMap;
    Marker marker;
    private Circle circle = null;
    int DEFAULT_ZOOM = 5;

    public HeatmapFragment() {
    }

    public static HeatmapFragment newInstance() {
        HeatmapFragment fragment = new HeatmapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.fragment_heatmap, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        initMapService();
    }
    private void addHeatMap() {
        if (mMap == null) return;
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.clear();
        double lat1 = -28.48322;
        double lng1 = 24.676997;
        latLngs.add(new LatLng(lat1, lng1));
        double lat2 = -29.48322;
        double lng2 = 24.576997;
        latLngs.add(new LatLng(lat2, lng2));
        double lat3 = -28.46322;
        double lng3 = 24.676497;
        latLngs.add(new LatLng(lat3, lng3));
        double lat4 = -28.45322;
        double lng4 = 24.673997;
        latLngs.add(new LatLng(lat4, lng4));
        double lat5 = -28.38322;
        double lng5 = 24.576997;
        latLngs.add(new LatLng(lat5, lng5));
        double lat6 = -28.58322;
        double lng6 = 24.676697;
        latLngs.add(new LatLng(lat6, lng6));
        double lat7 = -28.48522;
        double lng7 = 24.676497;
        latLngs.add(new LatLng(lat7, lng7));
        double lat8 = -28.48222;
        double lng8 = 24.676297;
        latLngs.add(new LatLng(lat8, lng8));
        double lat9 = -28.48622;
        double lng9 = 24.673997;
        latLngs.add(new LatLng(lat9, lng9));


        int[] colors = {
                Color.parseColor("#b29cff"), // green
                Color.parseColor("#562EDE")  // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

// Create the tile provider.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(latLngs)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private void initMapService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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
            mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 8.8), 2000, null);
        } else {
            circle.remove();
            circle = null;
            drawCircleOnMap();
        }
    }
    private void drawStores(){
//        for (int i = 0; i < searchedStores.size(); i++) {
//
//            BitmapDrawable store_loc = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.ic_location_red);
//            Bitmap a = store_loc.getBitmap();
//            Bitmap storeIcon = Bitmap.createScaledBitmap(a, 40, 50, false);
//
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(new LatLng(searchedStores.get(i).getCoordinates().get(0), searchedStores.get(i).getCoordinates().get(1)))
//                    .icon(BitmapDescriptorFactory.fromBitmap(storeIcon)
//                    .title(searchedStores.get(i).getName());
//
//            Marker storeMarker = mMap.addMarker(markerOptions);
//            storeMarker.setTag(i);
//        }
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
        addHeatMap();
        drawCircleOnMap();
    }


}