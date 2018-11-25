package com.mpos.pojo;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class CRSale extends APIData{
	
	

	public static final Parcelable.Creator<CRSale> CREATOR = new Parcelable.Creator<CRSale>() {
		public CRSale createFromParcel(Parcel in) {
			return new CRSale(in);
		}

		public CRSale[] newArray(int size) {
			return new CRSale[size];
		}
	};
	
	@SerializedName("crData")
	@Expose
	private String crData ;
	
	@SerializedName("amount")
	@Expose
	private double amount ;
	
	@SerializedName("stn")
	@Expose
	private int valSTN = 0 ; //System Trace Number
	
	
	@SerializedName("invoice")
	@Expose
	private long invoice  = 0;
	
	@SerializedName("clientTime")
	@Expose
	private Date clientTime ;
	
	
	// parameter for transation recovery
	
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
	
	@SerializedName("keypadL4")
	@Expose
	private String keypadL4 ;

	@SerializedName("currencyType")
	@Expose
	private int currencyType;

	public CRSale(){
		
	}
	
	public CRSale(Parcel in){
		crData = in.readString();
		amount = in.readDouble() ;
		valSTN = in.readInt() ;
		invoice = in.readLong() ;
		
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
		keypadL4 = in.readString();
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeString(crData) ;
		out.writeDouble(amount) ;
		out.writeInt(valSTN);
		out.writeLong(invoice) ;
		
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
		out.writeString(keypadL4) ;
	}
	
	
	public String getCrData() {
		return crData;
	}
	public void setCrData(String crData) {
		this.crData = crData;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getClientTime() {
		return clientTime;
	}
	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
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

	public int getValSTN() {
		return valSTN;
	}

	public void setValSTN(int valSTN) {
		this.valSTN = valSTN;
	}

	public long getInvoice() {
		return invoice;
	}

	public void setInvoice(long invoice) {
		this.invoice = invoice;
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

	public String getKeypadL4() {
		return keypadL4;
	}

	public void setKeypadL4(String keypadL4) {
		this.keypadL4 = keypadL4;
	}


	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}
}
