package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSignature extends APIData{
	
	public static final Parcelable.Creator<ModelSignature> CREATOR = new Parcelable.Creator<ModelSignature>() {
		public ModelSignature createFromParcel(Parcel in) {
			return new ModelSignature(in);
		}

		public ModelSignature[] newArray(int size) {
			return new ModelSignature[size];
		}
	};
	
	@SerializedName("saleId")
	@Expose
	private long saleId  = -1;
	
	@SerializedName("sig")
	@Expose
	private String signature ;
	
	public ModelSignature(){
		
	}
	
	public ModelSignature(Parcel in){
		saleId = in.readLong() ;
		signature = in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeLong(saleId);
		out.writeString(signature) ;
	}

	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	

}
