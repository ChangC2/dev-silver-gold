package com.cam8.mmsapp.multilang;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

/**
 * Class of primitive object used in every dialog in this library.
 */
public abstract class PrimitiveSelectableItem implements Parcelable {

    protected int id;
    protected String name;

    public PrimitiveSelectableItem() {
        id = 0;
        name = "";
    }

    public PrimitiveSelectableItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public PrimitiveSelectableItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public PrimitiveSelectableItem(Context context, @StringRes int idResId, @StringRes int nameResId) {
        this.id = idResId;
        this.name = context.getString(nameResId);
    }

    /**
     * Returns unique identifier of this item.
     * @return unique identifier of this item.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets unique identifier of this item.
     * @param id unique identifier to use for this item
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns name of this item.
     * @return name of item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of item that will be shown to user in dialog and view.
     * @param name name to use form this item
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}