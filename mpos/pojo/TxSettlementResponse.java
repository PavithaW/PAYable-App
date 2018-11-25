package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxSettlementResponse extends APIData{
	
	
	public static final Parcelable.Creator<TxSettlementResponse> CREATOR = new Parcelable.Creator<TxSettlementResponse>() {
		public TxSettlementResponse createFromParcel(Parcel in) {
			return new TxSettlementResponse(in);
		}

		public TxSettlementResponse[] newArray(int size) {
			return new TxSettlementResponse[size];
		}
	};

	
	@SerializedName("totalSales")
	@Expose
	private int totalSales ;
	
	@SerializedName("totalAmount")
	@Expose
	private double totalAmount ;
	
	
	public TxSettlementResponse(){
		
	}
	
	public TxSettlementResponse(Parcel in){
		totalSales = in.readInt() ;
		totalAmount = in.readDouble() ;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(totalSales) ;
		out.writeDouble(totalAmount) ;
		
	}

	public int getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

}
