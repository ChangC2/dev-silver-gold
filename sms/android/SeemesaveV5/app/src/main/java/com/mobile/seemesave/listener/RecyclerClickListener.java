package com.mobile.seemesave.listener;

import android.view.View;

public interface RecyclerClickListener {
    void onClick(View v, int position);
    void onClick(View v, int position, int type);
}