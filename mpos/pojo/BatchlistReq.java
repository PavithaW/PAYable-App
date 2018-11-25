package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchlistReq extends APIData {

    public static final Parcelable.Creator<BatchlistReq> CREATOR = new Parcelable.Creator<BatchlistReq>() {
        public BatchlistReq createFromParcel(Parcel in) {
            return new BatchlistReq(in);
        }

        public BatchlistReq[] newArray(int size) {
            return new BatchlistReq[size];
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


    public BatchlistReq() {

    }

    public BatchlistReq(Parcel in) {
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
