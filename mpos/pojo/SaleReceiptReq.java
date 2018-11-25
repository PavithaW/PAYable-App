package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SaleReceiptReq extends APIData {

    public static final int RECEIPT_SALE = 1;
    public static final int RECEIPT_VOID = 2;

    public static final Parcelable.Creator<SaleReceiptReq> CREATOR = new Parcelable.Creator<SaleReceiptReq>() {
        public SaleReceiptReq createFromParcel(Parcel in) {
            return new SaleReceiptReq(in);
        }

        public SaleReceiptReq[] newArray(int size) {
            return new SaleReceiptReq[size];
        }
    };

    @SerializedName("txId")
    @Expose
    private long txId;

    @SerializedName("ch")
    @Expose
    private String cardHolder;

    @SerializedName("cc")
    @Expose
    private String ccLast4;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("cardType")
    @Expose
    private int cardType;

    @SerializedName("time")
    @Expose
    private Date serverTime;

    @SerializedName("approvalCode")
    @Expose
    private String approvalCode;

    @SerializedName("receiptType")
    @Expose
    private int receiptType;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    // added following new fields on 7th august 2015

    @SerializedName("batchNo")
    @Expose
    private int batchNo;

    @SerializedName("stn")
    @Expose
    private int stn;

    @SerializedName("rrn")
    @Expose
    private String rrn;

    @SerializedName("expDate")
    @Expose
    private String expDate;

    @SerializedName("aid")
    @Expose
    private String aid;

    @SerializedName("appName")
    @Expose
    private String appName;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;


    public SaleReceiptReq() {

    }

    public SaleReceiptReq(Parcel in) {
        txId = in.readLong();
        cardHolder = in.readString();
        ccLast4 = in.readString();
        amount = in.readDouble();
        cardType = in.readInt();

        long l = in.readLong();

        if (l == -1) {
            serverTime = null;
        } else {
            serverTime = new Date(l);
        }

        //approvalCode = in.readInt();
        approvalCode = in.readString();
        receiptType = in.readInt();
        email = in.readString();
        mobile = in.readString();

        batchNo = in.readInt();
        stn = in.readInt();
        rrn = in.readString();

        expDate = in.readString();
        aid = in.readString();
        appName = in.readString();
        currencyType = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(txId);
        out.writeString(cardHolder);
        out.writeString(ccLast4);
        out.writeDouble(amount);
        out.writeInt(cardType);

        if (serverTime == null) {
            out.writeLong(-1);
        } else {
            out.writeLong(serverTime.getTime());
        }

        //out.writeInt(approvalCode);
        out.writeString(approvalCode);
        out.writeInt(receiptType);
        out.writeString(email);
        out.writeString(mobile);

        out.writeInt(batchNo);
        out.writeInt(stn);
        out.writeString(rrn);

        out.writeString(expDate);
        out.writeString(aid);
        out.writeString(appName);
        out.writeInt(currencyType);
    }

    public long getTxId() {
        return txId;
    }

    public void setTxId(long txId) {
        this.txId = txId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCcLast4() {
        return ccLast4;
    }

    public void setCcLast4(String ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public int getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(int receiptType) {
        this.receiptType = receiptType;
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

    public int getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(int batchNo) {
        this.batchNo = batchNo;
    }

    public int getStn() {
        return stn;
    }

    public void setStn(int stn) {
        this.stn = stn;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }
}
