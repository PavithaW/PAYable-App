package com.mpos.pojo;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SalesReceipt extends APIData {
	
	public static final Parcelable.Creator<SalesReceipt> CREATOR = new Parcelable.Creator<SalesReceipt>() {
		public SalesReceipt createFromParcel(Parcel in) {
			return new SalesReceipt(in);
		}

		public SalesReceipt[] newArray(int size) {
			return new SalesReceipt[size];
		}
	};
	
	public SalesReceipt(){
		
	}
	
	public SalesReceipt(Parcel in) {
		id = in.readLong();
		clientTs = in.readLong();
		transToken = in.readLong();
		email= in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeLong(id);
		out.writeLong(clientTs);
		out.writeLong(transToken);
		out.writeString(email) ;
	}
	
	@SerializedName("id")
	@Expose
	private long id ;
		
	@SerializedName("clientTs")
	@Expose
	private long clientTs ;
	
	@SerializedName("transToken")
	@Expose
	private long transToken ;
	
	@SerializedName("email")
	@Expose
	private String email ;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClientTs() {
		return clientTs;
	}

	public void setClientTs(long clientTs) {
		this.clientTs = clientTs;
	}

	public long getTransToken() {
		return transToken;
	}

	public void setTransToken(long transToken) {
		this.transToken = transToken;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

}
