package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleReq extends APIData {


    public static final Parcelable.Creator<SimpleReq> CREATOR = new Parcelable.Creator<SimpleReq>() {
        public SimpleReq createFromParcel(Parcel in) {
            return new SimpleReq(in);
        }

        public SimpleReq[] newArray(int size) {
            return new SimpleReq[size];
        }
    };

    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;

    public SimpleReq() {

    }

    public SimpleReq(Parcel in) {
        value = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(value);
        out.writeInt(currencyType);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }
}
