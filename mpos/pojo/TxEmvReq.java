package com.mpos.pojo;


import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxEmvReq extends APIData {
	
	public static final Parcelable.Creator<TxEmvReq> CREATOR = new Parcelable.Creator<TxEmvReq>() {
		public TxEmvReq createFromParcel(Parcel in) {
			return new TxEmvReq(in);
		}

		public TxEmvReq[] newArray(int size) {
			return new TxEmvReq[size];
		}
	};
	
	
	@SerializedName("amount")
	@Expose
	private double amount ;
	
	
	@SerializedName("stn")
	@Expose
	private int valSTN ; //System Trace Number
	
	@SerializedName("invoice")
	@Expose
	private int invoice ; // field 62 
	
	@SerializedName("f55")
	@Expose
	private String f55 ;
	
	
	@SerializedName("f35")
	@Expose
	private String f35 ;
	
	@SerializedName("chname")
	@Expose
	private String cardHolderName ;
	
	
	@SerializedName("clientTime")
	@Expose
	private Date clientTime ;
	
	@SerializedName("deviceID")
	@Expose
	private String deviceID ;
	
	@SerializedName("tsToken")
	@Expose
	private long tsToken  = 0;
	
	@SerializedName("rndToken")
	@Expose
	private long rndToken = 0 ; 
	
	@SerializedName("ksn")
	@Expose
	private String ksn ;
	
	public TxEmvReq(){
		
	}
	
	public TxEmvReq(Parcel in){
		amount = in.readDouble() ;
		valSTN = in.readInt() ;
		invoice = in.readInt() ;
		f55 = in.readString();
		f35 = in.readString();
		cardHolderName = in.readString();
		
		long val1   = in.readLong() ;
		
		if(val1> 0){
			clientTime = new Date(val1) ;
		}else{
			clientTime = null ;
		}
		
		deviceID = in.readString() ;
		tsToken = in.readLong() ;
		rndToken = in.readLong() ;
		ksn = in.readString();
	}
	
	public void writeToParcel(Parcel out, int flags){
		
		out.writeDouble(amount) ;
		out.writeInt(valSTN);
		//out.writeLong(invoice) ;
		out.writeInt(invoice) ;
		
		out.writeString(f55);
		out.writeString(f35);
		out.writeString(cardHolderName);
		
		if(clientTime != null){
			out.writeLong(clientTime.getTime()) ;
		}else{
			out.writeLong(-1) ;
		}
		
		out.writeString(deviceID) ;
		out.writeLong(tsToken);
		out.writeLong(rndToken) ;
		out.writeString(ksn);
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public int getValSTN() {
		return valSTN;
	}


	public void setValSTN(int valSTN) {
		this.valSTN = valSTN;
	}


	public int getInvoice() {
		return invoice;
	}


	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}


	public String getF55() {
		return f55;
	}


	public void setF55(String f55) {
		this.f55 = f55;
	}


	public String getF35() {
		return f35;
	}


	public void setF35(String f35) {
		this.f35 = f35;
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


	public String getCardHolderName() {
		return cardHolderName;
	}


	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getKsn() {
		return ksn;
	}

	public void setKsn(String ksn) {
		this.ksn = ksn;
	}
	
	

}
