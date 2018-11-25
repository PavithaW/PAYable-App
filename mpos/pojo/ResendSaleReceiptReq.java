package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendSaleReceiptReq extends APIData {

    public static final Parcelable.Creator<ResendSaleReceiptReq> CREATOR = new Parcelable.Creator<ResendSaleReceiptReq>() {
        public ResendSaleReceiptReq createFromParcel(Parcel in) {
            return new ResendSaleReceiptReq(in);
        }

        public ResendSaleReceiptReq[] newArray(int size) {
            return new ResendSaleReceiptReq[size];
        }
    };

    @SerializedName("txId")
    @Expose
    private long txId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    public ResendSaleReceiptReq() {

    }

    public ResendSaleReceiptReq(Parcel in) {
        txId = in.readLong();
        email = in.readString();
        mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(txId);
        out.writeString(email);
        out.writeString(mobile);
    }

    public long getTxId() {
        return txId;
    }

    public void setTxId(long txId) {
        this.txId = txId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
