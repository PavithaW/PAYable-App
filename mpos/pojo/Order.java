package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Order extends APIData {
	
	public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
		public Order createFromParcel(Parcel in) {
			return new Order(in);
		}

		public Order[] newArray(int size) {
			return new Order[size];
		}
	};
	
	
	private String merchantInvoiceId;
	
	
	public Order(){
		
	}
	
	public Order(Parcel in){
		merchantInvoiceId = in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeString(merchantInvoiceId);
	}

	public String getMerchantInvoiceId() {
		return merchantInvoiceId;
	}

	public void setMerchantInvoiceId(String merchantInvoiceId) {
		this.merchantInvoiceId = merchantInvoiceId;
	}

}
