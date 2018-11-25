package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class KeyEntryTxReq extends APIData {

    public static final Parcelable.Creator<KeyEntryTxReq> CREATOR = new Parcelable.Creator<KeyEntryTxReq>() {
        public KeyEntryTxReq createFromParcel(Parcel in) {
            return new KeyEntryTxReq(in);
        }

        public KeyEntryTxReq[] newArray(int size) {
            return new KeyEntryTxReq[size];
        }
    };

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("clientTime")
    @Expose
    private Date clientTime;

    @SerializedName("tsToken")
    @Expose
    private long tsToken = 0;

    @SerializedName("rndToken")
    @Expose
    private long rndToken = 0;

    @SerializedName("locationSrc")
    @Expose
    private int locationSrc;

    @SerializedName("merchantInvoiceId")
    @Expose
    private String merchantInvoiceId;

    @SerializedName("keyData")
    @Expose
    private String keyData;

    @SerializedName("ksn")
    @Expose
    private String ksn;

    @SerializedName("invoice")
    @Expose
    private int invoice;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;

    @SerializedName("installment")
    @Expose
    private int installment;

    public KeyEntryTxReq() {

    }


    public KeyEntryTxReq(Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getClientTime() {
        return clientTime;
    }

    public void setClientTime(Date clientTime) {
        this.clientTime = clientTime;
    }

    public long getRndToken() {
        return rndToken;
    }

    public void setRndToken(long rndToken) {
        this.rndToken = rndToken;
    }

    public int getLocationSrc() {
        return locationSrc;
    }

    public void setLocationSrc(int locationSrc) {
        this.locationSrc = locationSrc;
    }

    public String getMerchantInvoiceId() {
        return merchantInvoiceId;
    }

    public void setMerchantInvoiceId(String merchantInvoiceId) {
        this.merchantInvoiceId = merchantInvoiceId;
    }

    public String getKeyData() {
        return keyData;
    }

    public void setKeyData(String keyData) {
        this.keyData = keyData;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    public long getTsToken() {
        return tsToken;
    }

    public void setTsToken(long tsToken) {
        this.tsToken = tsToken;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }
}
