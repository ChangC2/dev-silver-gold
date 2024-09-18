package com.mobile.seemesave.view.profile;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.StoreAdapter;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.menu.LocationGoogleActivity;
import com.mobile.seemesave.view_model.main.StoreFragViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class StoreSelectActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{

    private StoreSelectActivity activity;
    private StoreFragViewModel mViewModel;
    
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StoreAdapter recyclerAdapter;

    @BindView(R.id.btnCollect)
    LinearLayout btnCollect;
    @BindView(R.id.btnDeliver)
    LinearLayout btnDeliver;

    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.txt_deliver)
    TextView txtDeliver;

    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.editSearch)
    EditText editSearch;

    ArrayList<StoreModel> storeCollectList = new ArrayList<>();
    ArrayList<StoreModel> storeDeliverList = new ArrayList<>();

    ArrayList<StoreModel> srcCollectList = new ArrayList<>();
    ArrayList<StoreModel> srcDeliverList = new ArrayList<>();

    private String searchKey = "";

    GoogleMap mMap;
    Marker myMarker;
    Circle circle = null;
    double lat = 0.0;
    double lon = 0.0;
    private View infoWindow;

    int DEFAULT_ZOOM = 10;

    int promotionsCount = 0;
    int productsCount = 0;
    boolean isFollowing = false;
    double rating = 0.0;
    int followings = 0;

    int offset = 0;
    private boolean collectTab = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StoreFragViewModel.class);
        setContentView(R.layout.activity_store_select);
        ButterKnife.bind(this);
        activity = this;
        lat = G.user.getLatitude();
        lon = G.user.getLongitude();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        String location = G.getAddress();
        Log.e("address_update:1", location);
        txtLocation.setText(location);
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
            storeCollectList.clear();
            storeDeliverList.clear();
            srcCollectList.clear();
            srcDeliverList.clear();
            if (list != null && list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isIs_click_collect()) {
                        storeCollectList.add(list.get(i));
                        srcCollectList.add(list.get(i));
                    } else {
                        storeDeliverList.add(list.get(i));
                        srcDeliverList.add(list.get(i));
                    }
                }
                if (collectTab) {
                    recyclerAdapter.setData(storeCollectList);
                } else {
                    recyclerAdapter.setData(storeDeliverList);
                }
                drawStores();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {

        editSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchKey = editSearch.getText().toString().toLowerCase();
                onSearch();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        initMapService();
        setRecycler();

        mViewModel.loadData();
    }
    private void onSearch() {
        if (collectTab) {
            storeCollectList.clear();
            for (int i = 0; i < srcCollectList.size(); i++) {
                if (srcCollectList.get(i).getName().toLowerCase().contains(searchKey) || srcCollectList.get(i).getAddress().toLowerCase().contains(searchKey)) {
                    storeCollectList.add(srcCollectList.get(i));
                }
            }
        } else {
            storeDeliverList.clear();
            for (int i = 0; i < srcDeliverList.size(); i++) {
                if (srcDeliverList.get(i).getName().toLowerCase().contains(searchKey) || srcDeliverList.get(i).getAddress().toLowerCase().contains(searchKey)) {
                    storeDeliverList.add(srcDeliverList.get(i));
                }
            }
        }

        if (recyclerAdapter == null) {
            setRecycler();
        } else {
            if (collectTab) {
                recyclerAdapter.setData(storeCollectList);
            } else {
                recyclerAdapter.setData(storeDeliverList);
            }
        }
        drawStores();
    }
    private void initMapService() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    private void setRecycler() {
        recyclerAdapter = new StoreAdapter(activity, storeCollectList, new StoreAdapter.StoreRecyclerListener() {
            @Override
            public void onItemClicked(int pos, StoreModel model) {
            }

            @Override
            public void onRate(int pos, StoreModel model) {

            }

            @Override
            public void onFollow(int pos, StoreModel model) {

            }

            @Override
            public void onUnFollow(int pos, StoreModel model) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        mMap = map;
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setMapToolbarEnabled(false);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_my_location);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 40, 40, false);

        MarkerOptions usermo = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        myMarker = mMap.addMarker(usermo);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lon), DEFAULT_ZOOM));


        infoWindow = getLayoutInflater().inflate(R.layout.dlg_store_info, null);
        mMap.setInfoWindowAdapter(new CustomInfoAdapter());
        mMap.setOnMarkerClickListener(this);
    }
    private void drawStores(){
        if (mMap == null) return;
        mMap.clear();
        lat = G.user.getLatitude();
        lon = G.user.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat,
                        lon), DEFAULT_ZOOM));
        BitmapDrawable clickCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(activity, R.drawable.ic_marker_blue);
        BitmapDrawable deliverCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(activity,R.drawable.ic_marker_grey);
        Bitmap a = clickCollectDrw.getBitmap();
        Bitmap b = deliverCollectDrw.getBitmap();
        Bitmap clickCollectIcon = Bitmap.createScaledBitmap(a, 40, 50, false);
        Bitmap deliverCollectIcon = Bitmap.createScaledBitmap(b, 40, 50, false);

        if (collectTab) {
            for (int i = 0; i < storeCollectList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(storeCollectList.get(i).getCoordinates().get(0), storeCollectList.get(i).getCoordinates().get(1)))
                        .icon(BitmapDescriptorFactory.fromBitmap(clickCollectIcon))
                        .title(storeCollectList.get(i).getName());

                Marker storeMarker = mMap.addMarker(markerOptions);
                storeMarker.setTag(i);
            }
        } else {
            for (int i = 0; i < storeDeliverList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(storeDeliverList.get(i).getCoordinates().get(0), storeDeliverList.get(i).getCoordinates().get(1)))
                        .icon(BitmapDescriptorFactory.fromBitmap(deliverCollectIcon))
                        .title(storeDeliverList.get(i).getName());

                Marker storeMarker = mMap.addMarker(markerOptions);
                storeMarker.setTag(i);
            }
        }
    }
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

//            float currentZoomLevel = getZoomLevel(circle);
//            float animateZomm = currentZoomLevel;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        } else {
            circle.remove();
            circle = null;
        }
    }
    private float getZoomLevel(Circle circle) {
        float zoomLevel = 0;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel ;
    }


    class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker arg0) {
            return infoWindow;
        }

        @Override
        public View getInfoWindow(Marker arg0) {
            return null;
        }
    }
    private void showInfoDlg(Marker marker){
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setContentView(R.layout.dlg_store_info);

        TextView txtStoreName = dialog.findViewById(R.id.txtStoreName);
        TextView txtPromotions = dialog.findViewById(R.id.txtPromotions);
        TextView txtProducts = dialog.findViewById(R.id.txtProducts);
        TextView txtFollowers = dialog.findViewById(R.id.txtFollowers);
        MaterialRatingBar ratingbar = dialog.findViewById(R.id.ratingbar);
        Button btnDetails = dialog.findViewById(R.id.btnDetails);
        Button btnViewClickCollect = dialog.findViewById(R.id.btnViewClickCollect);
        ImageView imgStore = dialog.findViewById(R.id.imgStore);
        txtPromotions.setText(String.format(java.util.Locale.US, "Promotions:%d", promotionsCount));
        txtProducts.setText(String.format(java.util.Locale.US, "Products:%d", productsCount));
        txtFollowers.setText(String.format(java.util.Locale.US, "Followers:%d", followings));
        ratingbar.setRating((float) rating);
        StoreModel store = collectTab ? storeCollectList.get((int) marker.getTag()) : storeDeliverList.get((int) marker.getTag());
        btnViewClickCollect.setVisibility(store.isIs_click_collect() ? View.VISIBLE : View.GONE);

        txtStoreName.setText(store.getName());
        Glide.with(activity)
                .load(store.getLogo())
                .fitCenter()
                .into(imgStore);


        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StoresMapActivityOld.this, StorePromosActivity.class);
//                intent.putExtra("store_id", store.getId());
//                intent.putExtra("is_click_collect", false);
//                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnViewClickCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StoresMapActivityOld.this, StorePromosActivity.class);
//                intent.putExtra("store_id", store.getId());
//                intent.putExtra("is_click_collect", true);
//                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        myMarker.hideInfoWindow();
        marker.hideInfoWindow();
//        if (!marker.equals(myMarker)) {
//            apiCallForGetStore(marker);
//            return true;
//        }
        return true;
    }
    private void onLocation() {
        Intent i = G.isAvailableGoogleApi(activity)
                ? new Intent(activity, LocationGoogleActivity.class)
                : new Intent(activity, LocationGoogleActivity.class);
        startActivityForResult(i, ADDRESS_PICKER_REQUEST);
    }
    @OnClick({R.id.btBack, R.id.btnCollect, R.id.btnDeliver, R.id.imgLocation})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnCollect:
                setCollectBtn(0);
                break;
            case R.id.btnDeliver:
                setCollectBtn(1);
                break;
            case R.id.imgLocation:
                onLocation();
                break;
        }
    }
    private void setCollectBtn(int tab) {
        switch (tab) {
            case 0:
                btnCollect.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_20));
                txtCollect.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                btnDeliver.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_white_rect_20));
                txtDeliver.setTextColor(ContextCompat.getColor(activity, R.color.grey_dark));
                collectTab = true;
                if (recyclerAdapter != null) {
                    recyclerAdapter.setData(storeCollectList);
                }
                drawStores();
                break;
            case 1:
                btnDeliver.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_20));
                txtDeliver.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                btnCollect.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_white_rect_20));
                txtCollect.setTextColor(ContextCompat.getColor(activity, R.color.grey_dark));
                collectTab = false;
                if (recyclerAdapter != null) {
                    recyclerAdapter.setData(storeDeliverList);
                }
                drawStores();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("fullAddress") != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra("lat", 0.0);
                    double currentLongitude = data.getDoubleExtra("lon", 0.0);
                    G.user.setLatitude(currentLatitude);
                    G.user.setLongitude(currentLongitude);
                    G.user.setAddress(data.getStringExtra("fullAddress"));
                    G.saveUserAddress(currentLatitude, currentLongitude, data.getStringExtra("fullAddress"));
                    JsonObject json = new JsonObject();
                    json.addProperty("latitude", G.user.getLatitude());
                    json.addProperty("longitude", G.user.getLongitude());
                    Log.e("location_update:", "3");
                    UserApi.updateLocation(json);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
