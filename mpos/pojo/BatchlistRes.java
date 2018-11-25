package com.mpos.pojo;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchlistRes extends APIData {
	
	public static final Parcelable.Creator<BatchlistRes> CREATOR = new Parcelable.Creator<BatchlistRes>() {
		public BatchlistRes createFromParcel(Parcel in) {
			return new BatchlistRes(in);
		}

		public BatchlistRes[] newArray(int size) {
			return new BatchlistRes[size];
		}
	};

	@SerializedName("settleId")
	@Expose
	private long settleId;

	@SerializedName("clBatchId")
	@Expose
	private long clBatchId;

	@SerializedName("serverTime")
	@Expose
	private Date serverTime;

	@SerializedName("salesTotal")
	@Expose
	private double salesTotal ;
	
	@SerializedName("voidTotal")
	@Expose
	private double voidTotal ;
	
	@SerializedName("noOfSale")
	@Expose
	private int noOfSale ;
	
	@SerializedName("noOfVoid")
	@Expose
	private int noOfVoid ;
	
	@SerializedName("visaSalesTotal")
	@Expose
	private double visaSalesTotal ;
	
	@SerializedName("visaVoidTotal")
	@Expose
	private double visaVoidTotal ;
	
	@SerializedName("visaNoOfSale")
	@Expose
	private int visaNoOfSale ;
	
	@SerializedName("visaNoOfVoid")
	@Expose
	private int visaNoOfVoid ;
	
	@SerializedName("masterTotal")
	@Expose
	private double masterTotal ;
	
	@SerializedName("masterVoidTotal")
	@Expose
	private double masterVoidTotal ;
	
	@SerializedName("masterNoOfSale")
	@Expose
	private int masterNoOfSale ;
	
	@SerializedName("masterNoOfVoid")
	@Expose
	private int masterNoOfVoid ;

	// added on 2017-7-20 by jeyram
	@SerializedName("currencyType")
	@Expose
	private int currencyType;

	public BatchlistRes() {

	}

	public BatchlistRes(Parcel in) {

		settleId = in.readLong();
		clBatchId = in.readLong();

		long l = in.readLong();

		if (l == -1) {
			serverTime = null;
		} else {
			serverTime = new Date(l);
		}
		
		salesTotal = in.readDouble() ;
		voidTotal = in.readDouble() ;
		noOfSale = in.readInt() ;
		noOfVoid = in.readInt() ;
		
		visaSalesTotal = in.readDouble() ;
		visaVoidTotal = in.readDouble() ;
		visaNoOfSale = in.readInt() ;
		visaNoOfVoid = in.readInt() ;
		
		masterTotal = in.readDouble() ;
		masterVoidTotal = in.readDouble() ;
		masterNoOfSale = in.readInt() ;
		masterNoOfVoid = in.readInt() ;
		currencyType = in.readInt() ;

	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(settleId);
		out.writeLong(clBatchId);
		

		if(serverTime == null){
			out.writeLong(-1);
		}else{
			out.writeLong(serverTime.getTime()) ;
		}
		
		out.writeDouble(salesTotal);
		out.writeDouble(voidTotal);
		out.writeInt(noOfSale);
		out.writeInt(noOfVoid);
		
		out.writeDouble(visaSalesTotal);
		out.writeDouble(visaVoidTotal);
		out.writeInt(visaNoOfSale);
		out.writeInt(visaNoOfVoid);
		
		out.writeDouble(masterTotal);
		out.writeDouble(masterVoidTotal);
		out.writeInt(masterNoOfSale);
		out.writeInt(masterNoOfVoid);
		out.writeInt(currencyType);

	}

	public long getSettleId() {
		return settleId;
	}

	public void setSettleId(long settleId) {
		this.settleId = settleId;
	}

	public long getClBatchId() {
		return clBatchId;
	}

	public void setClBatchId(long clBatchId) {
		this.clBatchId = clBatchId;
	}

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}

	public double getSalesTotal() {
		return salesTotal;
	}

	public void setSalesTotal(double salesTotal) {
		this.salesTotal = salesTotal;
	}

	public double getVoidTotal() {
		return voidTotal;
	}

	public void setVoidTotal(double voidTotal) {
		this.voidTotal = voidTotal;
	}

	public int getNoOfSale() {
		return noOfSale;
	}

	public void setNoOfSale(int noOfSale) {
		this.noOfSale = noOfSale;
	}

	public int getNoOfVoid() {
		return noOfVoid;
	}

	public void setNoOfVoid(int noOfVoid) {
		this.noOfVoid = noOfVoid;
	}

	public double getVisaSalesTotal() {
		return visaSalesTotal;
	}

	public void setVisaSalesTotal(double visaSalesTotal) {
		this.visaSalesTotal = visaSalesTotal;
	}

	public double getVisaVoidTotal() {
		return visaVoidTotal;
	}

	public void setVisaVoidTotal(double visaVoidTotal) {
		this.visaVoidTotal = visaVoidTotal;
	}

	public int getVisaNoOfSale() {
		return visaNoOfSale;
	}

	public void setVisaNoOfSale(int visaNoOfSale) {
		this.visaNoOfSale = visaNoOfSale;
	}

	public int getVisaNoOfVoid() {
		return visaNoOfVoid;
	}

	public void setVisaNoOfVoid(int visaNoOfVoid) {
		this.visaNoOfVoid = visaNoOfVoid;
	}

	public double getMasterTotal() {
		return masterTotal;
	}

	public void setMasterTotal(double masterTotal) {
		this.masterTotal = masterTotal;
	}

	public double getMasterVoidTotal() {
		return masterVoidTotal;
	}

	public void setMasterVoidTotal(double masterVoidTotal) {
		this.masterVoidTotal = masterVoidTotal;
	}

	public int getMasterNoOfSale() {
		return masterNoOfSale;
	}

	public void setMasterNoOfSale(int masterNoOfSale) {
		this.masterNoOfSale = masterNoOfSale;
	}

	public int getMasterNoOfVoid() {
		return masterNoOfVoid;
	}

	public void setMasterNoOfVoid(int masterNoOfVoid) {
		this.masterNoOfVoid = masterNoOfVoid;
	}


	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}
}
