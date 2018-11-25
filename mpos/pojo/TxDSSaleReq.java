package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TxDSSaleReq extends APIData {

    public static final Parcelable.Creator<TxDSSaleReq> CREATOR = new Parcelable.Creator<TxDSSaleReq>() {
        public TxDSSaleReq createFromParcel(Parcel in) {
            return new TxDSSaleReq(in);
        }

        public TxDSSaleReq[] newArray(int size) {
            return new TxDSSaleReq[size];
        }
    };

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("invoice")
    @Expose
    private int invoice;

    @SerializedName("f35")
    @Expose
    private String f35;

    @SerializedName("chname")
    @Expose
    private String cardHolderName;

    @SerializedName("clientTime")
    @Expose
    private Date clientTime;

    @SerializedName("crId")
    @Expose
    private String cardReaderId;

    @SerializedName("tsToken")
    @Expose
    private long tsToken = 0;

    @SerializedName("rndToken")
    @Expose
    private long rndToken = 0;

    @SerializedName("ksn")
    @Expose
    private String ksn;

    @SerializedName("mp")
    @Expose
    private String maskedPan;

    @SerializedName("keypadL4")
    @Expose
    private String keypadL4;

    @SerializedName("isFallBack")
    @Expose
    private int isFallBack;


    // added on 8th april 2016
    @SerializedName("merchantInvoiceId")
    @Expose
    private String merchantInvoiceId;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;

    @SerializedName("installment")
    @Expose
    private int installment;


    public TxDSSaleReq() {

    }

    public TxDSSaleReq(Parcel in) {
        amount = in.readDouble();
        invoice = in.readInt();
        f35 = in.readString();

        cardHolderName = in.readString();

        long val1 = in.readLong();

        if (val1 > 0) {
            clientTime = new Date(val1);
        } else {
            clientTime = null;
        }

        cardReaderId = in.readString();
        tsToken = in.readLong();
        rndToken = in.readLong();
        ksn = in.readString();
        maskedPan = in.readString();
        keypadL4 = in.readString();
        isFallBack = in.readInt();
        merchantInvoiceId = in.readString();
        currencyType = in.readInt();
        installment = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeDouble(amount);
        out.writeInt(invoice);
        out.writeString(f35);
        out.writeString(cardHolderName);

        if (clientTime != null) {
            out.writeLong(clientTime.getTime());
        } else {
            out.writeLong(-1);
        }

        out.writeString(cardReaderId);
        out.writeLong(tsToken);
        out.writeLong(rndToken);
        out.writeString(ksn);
        out.writeString(maskedPan);
        out.writeString(keypadL4);
        out.writeInt(isFallBack);
        out.writeString(merchantInvoiceId);
        out.writeInt(currencyType);
        out.writeInt(installment);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getF35() {
        return f35;
    }

    public void setF35(String f35) {
        this.f35 = f35;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Date getClientTime() {
        return clientTime;
    }

    public void setClientTime(Date clientTime) {
        this.clientTime = clientTime;
    }

    public String getCardReaderId() {
        return cardReaderId;
    }

    public void setCardReaderId(String cardReaderId) {
        this.cardReaderId = cardReaderId;
    }

    public long getTsToken() {
        return tsToken;
    }

    public void setTsToken(long tsToken) {
        this.tsToken = tsToken;
    }

    public long getRndToken() {
        return rndToken;
    }

    public void setRndToken(long rndToken) {
        this.rndToken = rndToken;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public String getKeypadL4() {
        return keypadL4;
    }

    public void setKeypadL4(String keypadL4) {
        this.keypadL4 = keypadL4;
    }

    public int getIsFallBack() {
        return isFallBack;
    }

    public void setIsFallBack(int isFallBack) {
        this.isFallBack = isFallBack;
    }

    public String getMerchantInvoiceId() {
        return merchantInvoiceId;
    }

    public void setMerchantInvoiceId(String merchantInvoiceId) {
        this.merchantInvoiceId = merchantInvoiceId;
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
