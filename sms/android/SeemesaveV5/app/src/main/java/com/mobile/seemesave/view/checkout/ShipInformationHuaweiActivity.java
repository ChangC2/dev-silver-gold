package com.mobile.seemesave.view.checkout;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.view_model.checkout.CheckOutDeliverViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipInformationHuaweiActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText editName;

    @BindView(R.id.editEmail)
    TextInputEditText editEmail;

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
    private ShipInformationHuaweiActivity activity;

    double lat = -28.48322, lon = 24.676997;
    double store_lat = 0.00, store_lon = 0.00;
    GoogleMap mMap;
    Marker marker;
    private Circle circle = null;
    int DEFAULT_ZOOM = 5;

    private String description = "";
    private String currency = "";
    int cart_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutDeliverViewModel.class);
        setContentView(R.layout.activity_ship_information);
        ButterKnife.bind(this);
        activity = this;
        cart_id = getIntent().getIntExtra("cart_id", -1);

        initView();
    }
    private void initView() {

    }
}
