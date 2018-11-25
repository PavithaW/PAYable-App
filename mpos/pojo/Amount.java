package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.setting.env.Consts;

public class Amount extends APIData {

    public static final Parcelable.Creator<Amount> CREATOR = new Parcelable.Creator<Amount>() {
        public Amount createFromParcel(Parcel in) {
            return new Amount(in);
        }

        public Amount[] newArray(int size) {
            return new Amount[size];
        }
    };

    private double value = 0;
    private int installments;
    private int currencyType = Consts.LKR;

    public Amount() {

    }

    public Amount(Parcel in) {
        //super(in) ;
        this.value = in.readDouble();
        this.installments = in.readInt();
        this.currencyType = in.readInt();
    }

    public Amount(double val) {
        value = val;

    }

    public Amount(double value, int installments, int currencyType) {
        this.value = value;
        this.installments = installments;
        this.currencyType = currencyType;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(value);
        out.writeInt(installments);
        out.writeInt(currencyType);

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }
}
