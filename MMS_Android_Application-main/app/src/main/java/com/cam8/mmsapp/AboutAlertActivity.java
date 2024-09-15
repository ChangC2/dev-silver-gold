package com.cam8.mmsapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class AboutAlertActivity extends BaseActivity implements View.OnClickListener {

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:setting";

    class CarrierInfo {
        String name;
        String format;

        CarrierInfo(String name, String format) {
            this.name = name;
            this.format = format;
        }
    }

    ArrayList<CarrierInfo> carrierInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutalert);

        View sub_content = findViewById(R.id.sub_content);
        ViewCompat.setTransitionName(sub_content, VIEW_NAME_HEADER_IMAGE);
        findViewById(R.id.btnClose).setOnClickListener(this);

        carrierInfos.add(new CarrierInfo("Metro PCS:", "##########@mymetropcs.com"));
        carrierInfos.add(new CarrierInfo("T-Mobile:", "##########@tmomail.net"));
        carrierInfos.add(new CarrierInfo("Verizon Wireless:","##########@vtext.com"));
        carrierInfos.add(new CarrierInfo("AT&T:", "##########@txt.att.net"));
        carrierInfos.add(new CarrierInfo("Sprint PCS:", "##########@messaging.sprintpcs.com"));
        carrierInfos.add(new CarrierInfo("Nextel:", "##########@messaging.nextel.com"));
        carrierInfos.add(new CarrierInfo("Cricket:", "##########@sms.mycricket.com"));
        carrierInfos.add(new CarrierInfo("US Cellular:", "##########@email.uscc.net"));
        carrierInfos.add(new CarrierInfo("Cingular (GSM):", "##########@cingularme.com"));
        carrierInfos.add(new CarrierInfo("Cingular (TDMA):", "##########@mmode.com"));

        CarrierListAdapter listAdapter = new CarrierListAdapter();
        ListView listCarrier = findViewById(R.id.listCarrier);
        listCarrier.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnClose) {
            finish();
        }
    }

    private void copyClipboard(String label, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
        }
    }

    // Adapter for holding devices found through scanning.
    private class CarrierListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;

        public CarrierListAdapter() {
            super();

            mInflator = AboutAlertActivity.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return carrierInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return carrierInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_carrier, null);
                viewHolder = new ViewHolder();
                viewHolder.carrierName = (TextView) view.findViewById(R.id.tvCarrierName);
                viewHolder.carrierNumber = (TextView) view.findViewById(R.id.tvCarrierNumber);
                viewHolder.btnCopy = (TextView) view.findViewById(R.id.tvCopy);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            CarrierInfo carrierInfo = carrierInfos.get(i);
            viewHolder.carrierName.setText(carrierInfo.name);
            viewHolder.carrierNumber.setText(carrierInfo.format);

            viewHolder.btnCopy.setTag(carrierInfo);
            viewHolder.btnCopy.setOnClickListener(copyBtnListener);
            return view;
        }

        View.OnClickListener copyBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarrierInfo carrierInfo = (CarrierInfo) view.getTag();
                if (carrierInfo != null) {
                    copyClipboard(carrierInfo.name, carrierInfo.format);
                    showToastMessage("Copied!");
                }
            }
        };
    }

    static class ViewHolder {
        TextView carrierName;
        TextView carrierNumber;
        TextView btnCopy;
    }
}
