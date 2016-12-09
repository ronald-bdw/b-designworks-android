package com.pairup.android.login.functional_area;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
public class Area implements Parcelable {

    private String country;
    private String code;

    public Area(String country, String code) {
        this.country = country;
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.code);
    }

    protected Area(Parcel in) {
        this.country = in.readString();
        this.code = in.readString();
    }

    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        @Override public Area[] newArray(int size) {
            return new Area[size];
        }
    };
}
