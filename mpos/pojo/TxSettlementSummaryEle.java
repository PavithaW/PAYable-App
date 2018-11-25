package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TxSettlementSummaryEle extends APIData {

    public static final Parcelable.Creator<TxSettlementSummaryEle> CREATOR = new Parcelable.Creator<TxSettlementSummaryEle>() {
        public TxSettlementSummaryEle createFromParcel(Parcel in) {
            return new TxSettlementSummaryEle(in);
        }

        public TxSettlementSummaryEle[] newArray(int size) {
            return new TxSettlementSummaryEle[size];
        }
    };

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("count")
    @Expose
    private int count;

	/*
	@SerializedName("amount")
	@Expose
	private double amount ;
	
	*/

    @SerializedName("amount")
    @Expose
    private BigDecimal amount;

    @SerializedName("currency")
    @Expose
    private int currency;

    public TxSettlementSummaryEle() {

    }

    public TxSettlementSummaryEle(Parcel in) {
        type = in.readInt();
        count = in.readInt();
        //amount = in.readDouble() ;
        amount = new BigDecimal(in.readString());
        currency = in.readInt();
    }


    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(type);
        out.writeInt(count);
        //out.writeDouble(amount) ;
        out.writeString(amount.toPlainString());
        out.writeInt(currency);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

	/*
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	*/

}
