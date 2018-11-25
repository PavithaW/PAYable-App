package com.mpos.pojo;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxVoidRes extends APIData  {
	
	public static final Parcelable.Creator<TxVoidRes> CREATOR = new Parcelable.Creator<TxVoidRes>() {
		public TxVoidRes createFromParcel(Parcel in) {
			return new TxVoidRes(in);
		}

		public TxVoidRes[] newArray(int size) {
			return new TxVoidRes[size];
		}
	};
	
	@SerializedName("vid")
	@Expose
	private long voidId ;
	
	@SerializedName("sid")
	@Expose
	private long saleTxId ;
	
	@SerializedName("serverTime")
	@Expose
	private Date serverTime ;
	
	@SerializedName("stn")
	@Expose
	private int stn ;
	
	@SerializedName("rrn")
	@Expose
	private String rrn ;
	
	
	public TxVoidRes(){
		
	}
	
	public TxVoidRes(Parcel in){
		voidId = in.readLong() ;
		saleTxId = in.readLong() ;
		
		long val1   = in.readLong() ;
		
		if(val1> 0){
			serverTime = new Date(val1) ;
		}else{
			serverTime = null ;
		}
		
		stn = in.readInt() ;
		rrn = in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeLong(voidId) ;
		out.writeLong(saleTxId) ;
		
		if(serverTime != null){
			out.writeLong(serverTime.getTime()) ;
		}else{
			out.writeLong(-1) ;
		}
		
		out.writeInt(stn) ;
		out.writeString(rrn) ;
	}

	public long getVoidId() {
		return voidId;
	}

	public void setVoidId(long voidId) {
		this.voidId = voidId;
	}

	public long getSaleTxId() {
		return saleTxId;
	}

	public void setSaleTxId(long saleTxId) {
		this.saleTxId = saleTxId;
	}

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	public int getStn() {
		return stn;
	}

	public void setStn(int stn) {
		this.stn = stn;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	

}
