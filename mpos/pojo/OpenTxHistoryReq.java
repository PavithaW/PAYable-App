package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenTxHistoryReq extends APIData {

    public static final Parcelable.Creator<OpenTxHistoryReq> CREATOR = new Parcelable.Creator<OpenTxHistoryReq>() {
        public OpenTxHistoryReq createFromParcel(Parcel in) {
            return new OpenTxHistoryReq(in);
        }

        public OpenTxHistoryReq[] newArray(int size) {
            return new OpenTxHistoryReq[size];
        }
    };


    @SerializedName("pageId")
    @Expose
    private int pageId;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;

    public OpenTxHistoryReq() {

    }

    public OpenTxHistoryReq(Parcel in) {
        pageId = in.readInt();
        pageSize = in.readInt();
        currencyType = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(pageId);
        out.writeInt(pageSize);
        out.writeInt(currencyType);
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }
}
