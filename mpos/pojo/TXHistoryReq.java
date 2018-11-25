package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TXHistoryReq extends APIData {

    public static final Parcelable.Creator<TXHistoryReq> CREATOR = new Parcelable.Creator<TXHistoryReq>() {
        public TXHistoryReq createFromParcel(Parcel in) {
            return new TXHistoryReq(in);
        }

        public TXHistoryReq[] newArray(int size) {
            return new TXHistoryReq[size];
        }
    };

    @SerializedName("serchTerm")
    @Expose
    private String serchTerm;

    @SerializedName("pageId")
    @Expose
    private int pageId;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;

    @SerializedName("master")
    @Expose
    private int master = 0;

    @SerializedName("visa")
    @Expose
    private int visa = 0;

    @SerializedName("amex")
    @Expose
    private int amex = 0;

    @SerializedName("dc")
    @Expose
    private int dc = 0;

    @SerializedName("stDate")
    @Expose
    private Date stDate;

    @SerializedName("enDate")
    @Expose
    private Date enDate;

    @SerializedName("currencyType")
    @Expose
    private int currencyType;

    public TXHistoryReq() {

    }

    public TXHistoryReq(Parcel in) {
        serchTerm = in.readString();
        pageId = in.readInt();
        pageSize = in.readInt();
        master = in.readInt();
        visa = in.readInt();
        amex = in.readInt();
        dc = in.readInt();

        long val1 = in.readLong();
        if (val1 > 0) {
            stDate = new Date(val1);
        } else {
            stDate = null;
        }

        long val2 = in.readLong();
        if (val2 > 0) {
            enDate = new Date(val2);
        } else {
            enDate = null;
        }
        currencyType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(serchTerm);
        out.writeInt(pageId);
        out.writeInt(pageSize);
        out.writeInt(master);
        out.writeInt(visa);
        out.writeInt(amex);
        out.writeInt(dc);

        if (stDate == null) {
            out.writeLong(-1);
        } else {
            out.writeLong(stDate.getTime());
        }

        if (enDate == null) {
            out.writeLong(-1);
        } else {
            out.writeLong(enDate.getTime());
        }
        out.writeInt(currencyType);

    }

    public String getSerchTerm() {
        return serchTerm;
    }

    public void setSerchTerm(String serchTerm) {
        this.serchTerm = serchTerm;
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

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public int getVisa() {
        return visa;
    }

    public void setVisa(int visa) {
        this.visa = visa;
    }

    public int getAmex() {
        return amex;
    }

    public void setAmex(int amex) {
        this.amex = amex;
    }

    public Date getStDate() {
        return stDate;
    }

    public void setStDate(Date stDate) {
        this.stDate = stDate;
    }

    public Date getEnDate() {
        return enDate;
    }

    public void setEnDate(Date enDate) {
        this.enDate = enDate;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public int getDc() {
        return dc;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }
}
