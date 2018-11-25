package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class DSICCOflineErrReq extends APIData {
	
	public static final Parcelable.Creator<DSICCOflineErrReq> CREATOR = new Parcelable.Creator<DSICCOflineErrReq>() {
		public DSICCOflineErrReq createFromParcel(Parcel in) {
			return new DSICCOflineErrReq(in);
		}

		public DSICCOflineErrReq[] newArray(int size) {
			return new DSICCOflineErrReq[size];
		}
	};

	
	@SerializedName("strLog")
	@Expose
	private String strLog ;
	
	@SerializedName("amount")
	@Expose
	private double amount ;
	
	public DSICCOflineErrReq() {

	}
	
	public DSICCOflineErrReq(Parcel in){
		amount = in.readDouble();
		strLog = in.readString() ;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(amount) ;
		out.writeString(strLog) ;
	}

	public String getStrLog() {
		return strLog;
	}

	public void setStrLog(String strLog) {
		this.strLog = strLog;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
