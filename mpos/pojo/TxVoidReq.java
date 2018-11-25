package com.mpos.pojo;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxVoidReq extends APIData{
	
	public static final Parcelable.Creator<TxVoidReq> CREATOR = new Parcelable.Creator<TxVoidReq>() {
		public TxVoidReq createFromParcel(Parcel in) {
			return new TxVoidReq(in);
		}

		public TxVoidReq[] newArray(int size) {
			return new TxVoidReq[size];
		}
	};
	
	
	@SerializedName("txId")
	@Expose
	private long txId ;
	
	@SerializedName("crData")
	@Expose
	private String crData ;
	
	@SerializedName("clientTime")
	@Expose
	private Date clientTime ;
	
	@SerializedName("deviceID")
	@Expose
	private String deviceID ; //udid in sha1
	
	@SerializedName("tsToken")
	@Expose
	private long tsToken  = 0;
	
	@SerializedName("rndToken")
	@Expose
	private long rndToken = 0 ; 
	
	@SerializedName("latitude")
	@Expose
	private double latitude = 0 ;
	
	@SerializedName("longitude")
	@Expose
	private double longitude = 0;
	
	@SerializedName("repeatFlag")
	@Expose
	private int repeatFlag = 0;
	
	
	public TxVoidReq(){
		
	}
	
	public TxVoidReq(Parcel in){
		txId = in.readLong() ;
		crData = in.readString();
		
		long val1   = in.readLong() ;
		
		if(val1> 0){
			clientTime = new Date(val1) ;
		}else{
			clientTime = null ;
		}
		
		deviceID = in.readString() ;
		tsToken = in.readLong() ;
		rndToken = in.readLong() ;
		latitude = in.readDouble() ;
		longitude = in.readDouble() ;
		repeatFlag = in.readInt() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeLong(txId) ;
		out.writeString(crData) ;
		

		if(clientTime != null){
			out.writeLong(clientTime.getTime()) ;
		}else{
			out.writeLong(-1) ;
		}
		
		out.writeString(deviceID) ;
		out.writeLong(tsToken);
		out.writeLong(rndToken) ;
		out.writeDouble(latitude);
		out.writeDouble(longitude);
		out.writeInt(repeatFlag);
	}

	public long getTxId() {
		return txId;
	}

	public void setTxId(long txId) {
		this.txId = txId;
	}

	public String getCrData() {
		return crData;
	}

	public void setCrData(String crData) {
		this.crData = crData;
	}

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
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

	public int getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(int repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

}
