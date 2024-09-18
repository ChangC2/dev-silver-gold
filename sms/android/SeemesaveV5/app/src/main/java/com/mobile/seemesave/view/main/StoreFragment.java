package com.mobile.seemesave.view.main;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;
import static com.mobile.seemesave.view.main.MainActivity.BACKGROUND_REQUEST_CODES;
import static com.mobile.seemesave.view.main.MainActivity.PERM_REQUEST_CODES;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;
import java.util.List;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.StoreAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.service.GpsTracker;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.menu.LocationGoogleActivity;
import com.mobile.seemesave.view_model.main.StoreFragViewModel;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class StoreFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{
    private StoreFragViewModel mViewModel;
    private View mFragView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private StoreAdapter recyclerAdapter;

    private boolean collectTab = true;

    @BindView(R.id.btnCollect)
    LinearLayout btnCollect;
    @BindView(R.id.btnDeliver)
    LinearLayout btnDeliver;

    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.txt_deliver)
    TextView txtDeliver;

    @BindView(R.id.editSearch)
    EditText editSearch;
    @BindView(R.id.txtLocation)
    TextView txtLocation;

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

    String[] perms = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "update_location":
                    if (G.isNetworkAvailable(getActivity())) {
                        mViewModel.setIsBusy(true);
                        mViewModel.loadData();
                    }
                    break;
            }
        }
    };
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("update_location");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver,
                filter);
    }
    public StoreFragment() {
    }

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
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
        mViewModel = new ViewModelProvider(this).get(StoreFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.bind(this, mFragView);
        lat = G.user.getLatitude();
        lon = G.user.getLongitude();
        initView();
        registerBroadcast();
        return mFragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

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

        editSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchKey = editSearch.getText().toString().toLowerCase();
                    onSearch();
                    return true;
                }
                return false;
            }
        });
        initMapService();
        setRecycler();


        if (G.isNetworkAvailable(getActivity())) {
            mViewModel.setIsBusy(true);
            mViewModel.loadData();
        } else {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "StoreFragment", "");
                if (!TextUtils.isEmpty(local_data)) {
                    mViewModel.loadLocalData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void checkPerm() {
        List<String> permlist = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String perm : perms) {
                if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm);
                    permlist.add(perm);
                }
            }
            if (permlist.size() > 0) {
                G.showDlg(getActivity(), getString(R.string.alert_location), new ClickListener() {
                    @Override
                    public void onClick(boolean flag) {
                        ActivityCompat.requestPermissions(getActivity(), permlist.toArray(new String[permlist.size()]), PERM_REQUEST_CODES);
                    }
                });
            } else {
                GpsTracker gpsTracker = new GpsTracker(getActivity());
                if (gpsTracker.canGetLocation() && gpsTracker.getLocation() != null) {
                    G.location = gpsTracker.getLocation();
                }

                checkBackgroundPermission();
            }
        }
    }
    private void checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("");
                alertDialog.setMessage(getString(R.string.alert_background_location));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_REQUEST_CODES);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("");
                alertDialog.setMessage(getString(R.string.alert_background_location));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = new Uri.Builder()
                                .scheme("package")
                                .opaquePart(getActivity().getPackageName())
                                .build();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                        getActivity().startActivityIfNeeded(intent, BACKGROUND_REQUEST_CODES);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        } else {
            Intent i = G.isAvailableGoogleApi(getActivity())
                    ? new Intent(getActivity(), LocationGoogleActivity.class)
                    : new Intent(getActivity(), LocationGoogleActivity.class);
            startActivityForResult(i, ADDRESS_PICKER_REQUEST);
        }
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    private void setRecycler() {
        recyclerAdapter = new StoreAdapter(getActivity(), storeCollectList, new StoreAdapter.StoreRecyclerListener() {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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

        lat = G.user.getLatitude();
        lon = G.user.getLongitude();

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
        BitmapDrawable clickCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.ic_marker_blue);
        BitmapDrawable deliverCollectDrw = (BitmapDrawable) ContextCompat.getDrawable(getActivity(),R.drawable.ic_marker_grey);
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
        Dialog dialog = new Dialog(getActivity());
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
        Glide.with(getActivity())
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
        checkPerm();
    }
    @OnClick({R.id.btnCollect, R.id.btnDeliver, R.id.imgLocation})
    public void onClickButtons(View view) {
        switch (view.getId()) {
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
                btnCollect.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_blue_rect_20));
                txtCollect.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
                btnDeliver.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_white_rect_20));
                txtDeliver.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_dark));
                collectTab = true;
                if (recyclerAdapter != null) {
                    recyclerAdapter.setData(storeCollectList);
                }
                drawStores();
                break;
            case 1:
                btnDeliver.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_blue_rect_20));
                txtDeliver.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
                btnCollect.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bk_white_rect_20));
                txtCollect.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_dark));
                collectTab = false;
                if (recyclerAdapter != null) {
                    recyclerAdapter.setData(storeDeliverList);
                }
                drawStores();
                break;
        }
    }
}