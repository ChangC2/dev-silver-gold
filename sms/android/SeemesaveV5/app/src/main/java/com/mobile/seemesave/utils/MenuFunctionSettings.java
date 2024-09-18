package com.mobile.seemesave.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mobile.seemesave.R;


public class MenuFunctionSettings extends LinearLayout {

    private View binding;
    private ItemClickListener itemClickListener;
    private ActionListener actionListener;

    public interface ActionListener {
        void onCheckedListener(boolean isChecked);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface ItemClickListener {
        void OnItemClickListener();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuFunctionSettings(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutInflater.from(context).inflate(R.layout.layout_menu_function_settings, this, true);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray attrArr = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MenuFunction, 0, 0);
        String title = attrArr.getString(R.styleable.MenuFunction_inf_title);
        String subDescription = attrArr.getString(R.styleable.MenuFunction_inf_selection_text_description);
        int  titleColor = attrArr.getColor(R.styleable.MenuFunction_inf_title_color, getResources().getColor(R.color.grey_dark));
        int  descriptionColor = attrArr.getColor(R.styleable.MenuFunction_inf_description_color, getResources().getColor(R.color.grey_light));
        if (!TextUtils.isEmpty(title)){
            ((TextView) binding.findViewById(R.id.tvTitle)).setText(title);
            ((TextView) binding.findViewById(R.id.tvTitle)).setTextColor(titleColor);
        }

        String description = attrArr.getString(R.styleable.MenuFunction_inf_description);
        if (!TextUtils.isEmpty(description)) {
            ((TextView)binding.findViewById(R.id.tvDescription)).setText(description);
            ((TextView)binding.findViewById(R.id.tvDescription)).setTextColor(descriptionColor);
            ((TextView)binding.findViewById(R.id.tvDescription)).setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(subDescription)){
            ((TextView) binding.findViewById(R.id.tvSubDescription)).setText(subDescription);
            ((TextView) binding.findViewById(R.id.tvSubDescription)).setVisibility(VISIBLE);
        }

        Drawable icon = attrArr.getDrawable(R.styleable.MenuFunction_inf_icon);
        if (icon != null) {
            ((ImageView)binding.findViewById(R.id.imIcon)).setImageDrawable(icon);
            ((ImageView)binding.findViewById(R.id.imIcon)).setVisibility(VISIBLE);
        }

        Drawable iconSub = attrArr.getDrawable(R.styleable.MenuFunction_inf_icon_sub);
        if (iconSub != null) {
            ((ImageView)binding.findViewById(R.id.imIconSub)).setImageDrawable(iconSub);
            ((ImageView)binding.findViewById(R.id.imIconSub)).setVisibility(VISIBLE);
        }

        boolean isShowIconSub = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_icon_subscription, false);
        ((ImageView)binding.findViewById(R.id.imIconSub)).setVisibility(isShowIconSub ? VISIBLE : GONE);

        boolean isShowIcon = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_icon, true);
        ((ImageView)binding.findViewById(R.id.imIcon)).setVisibility(isShowIcon ? VISIBLE : GONE);

        boolean isShowSwitchCheck = attrArr.getBoolean(R.styleable.MenuFunction_inf_show_switch_check, false);
        ((SwitchCompat)binding.findViewById(R.id.swSelect)).setVisibility(isShowSwitchCheck ? VISIBLE : GONE);

        initControl();
    }

    private void initControl() {
        binding.findViewById(R.id.container).setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.OnItemClickListener();
        });

//        binding.swSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (actionListener != null)
//                actionListener.onCheckedListener(isChecked);
//        });
        ((SwitchCompat)binding.findViewById(R.id.swSelect)).setOnClickListener(v -> {
            if (actionListener != null)
                actionListener.onCheckedListener(((SwitchCompat)binding.findViewById(R.id.swSelect)).isChecked());
        });
    }

    public void setEnable(boolean isEnable){
        ((ConstraintLayout)binding.findViewById(R.id.container)).setEnabled(isEnable);
        ((ConstraintLayout)binding.findViewById(R.id.container)).setClickable(isEnable);
        if (isEnable){
            ((ConstraintLayout)binding.findViewById(R.id.container)).setAlpha(1.0f);
        }else {
            ((ConstraintLayout)binding.findViewById(R.id.container)).setAlpha(0.5f);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
            ((TextView)binding.findViewById(R.id.tvTitle)).setText(title);
    }

    public void setDescription(String description) {
        if (!TextUtils.isEmpty(description))
            ((TextView)binding.findViewById(R.id.tvDescription)).setText(description);
    }

    public void setSubDescription(String subDescription) {
        if (!TextUtils.isEmpty(subDescription))
            ((TextView)binding.findViewById(R.id.tvSubDescription)).setText(subDescription);
            ((TextView)binding.findViewById(R.id.tvSubDescription)).setVisibility(VISIBLE);
    }

    public void setIconSub(int icon) {
        ((ImageView)binding.findViewById(R.id.imIconSub)).setImageResource(icon);
        ((ImageView)binding.findViewById(R.id.imIconSub)).setVisibility(VISIBLE);
    }

    public void setIconVisible(boolean isShown){
        if (isShown){
            ((ImageView)binding.findViewById(R.id.imIcon)).setVisibility(VISIBLE);
        }else {
            ((ImageView)binding.findViewById(R.id.imIcon)).setVisibility(GONE);
        }
    }

    public void setTitleColor(int color){
        ((TextView)binding.findViewById(R.id.tvTitle)).setTextColor(color);
    }

    public void setDescriptionColor(int color){
        ((TextView)binding.findViewById(R.id.tvDescription)).setTextColor(color);
    }

    public void setSwChecked(boolean isChecked) {
        ((SwitchCompat)binding.findViewById(R.id.swSelect)).setChecked(isChecked);
    }

    public void getSwChecked() {
        ((SwitchCompat)binding.findViewById(R.id.swSelect)).isChecked();
    }

    public void setSwitchEnable(boolean isEnable) {
        setEnabled(isEnable);
        setAlpha(isEnable ? 1.0f : 0.3f);
        ((SwitchCompat)binding.findViewById(R.id.swSelect)).setEnabled(isEnable);
    }


}
