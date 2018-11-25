package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TXDsNfcReq extends APIData{

	public static final Parcelable.Creator<TXDsNfcReq> CREATOR = new Parcelable.Creator<TXDsNfcReq>() {
		public TXDsNfcReq createFromParcel(Parcel in) {
			return new TXDsNfcReq(in);
		}

		public TXDsNfcReq[] newArray(int size) {
			return new TXDsNfcReq[size];
		}
	};

	public TXDsNfcReq(){

	}
	
	@SerializedName("amount")
	@Expose
	private double amount ;
	
	@SerializedName("invoice")
	@Expose
	private int invoice ;
	
	@SerializedName("f35")
	@Expose
	private String f35 ;
	
	@SerializedName("ksn")
	@Expose
	private String ksn ;
	
	@SerializedName("chname")
	@Expose
	private String cardHolderName ;
	
	@SerializedName("crId")
	@Expose
	private String cardReaderId ;
	
	@SerializedName("currencyType")
	@Expose
	private int currencyType ;
	
	@SerializedName("installment")
	@Expose
	private int installment ;
	
	
	@SerializedName("tsToken")
	@Expose
	private long tsToken  = 0;
	
	@SerializedName("rndToken")
	@Expose
	private long rndToken = 0 ; 
	
	@SerializedName("clientTime")
	@Expose
	private Date clientTime ;
	
	@SerializedName("locationSrc")
	@Expose
	private int locationSrc;
	
	@SerializedName("merchantInvoiceId")
	@Expose
	private String merchantInvoiceId;
	
	@SerializedName("icData")
	@Expose
	private String icData;


	public TXDsNfcReq(Parcel in) {
		amount = in.readDouble();
		invoice = in.readInt();

		long val1 = in.readLong();

		if (val1 > 0) {
			clientTime = new Date(val1);
		} else {
			clientTime = null;
		}

		cardReaderId = in.readString();
		tsToken = in.readLong();
		rndToken = in.readLong();
		icData = in.readString();
		merchantInvoiceId = in.readString();
		currencyType = in.readInt();
		installment = in.readInt();
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(amount);
		out.writeInt(invoice);

		if (clientTime != null) {
			out.writeLong(clientTime.getTime());
		} else {
			out.writeLong(-1);
		}

		out.writeString(cardReaderId);
		out.writeLong(tsToken);
		out.writeLong(rndToken);
		out.writeString(icData);
		out.writeString(merchantInvoiceId);
		out.writeInt(currencyType);
		out.writeInt(installment);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}

	public String getF35() {
		return f35;
	}

	public void setF35(String f35) {
		this.f35 = f35;
	}

	public String getKsn() {
		return ksn;
	}

	public void setKsn(String ksn) {
		this.ksn = ksn;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardReaderId() {
		return cardReaderId;
	}

	public void setCardReaderId(String cardReaderId) {
		this.cardReaderId = cardReaderId;
	}

	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	public int getInstallment() {
		return installment;
	}

	public void setInstallment(int installment) {
		this.installment = installment;
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

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public int getLocationSrc() {
		return locationSrc;
	}

	public void setLocationSrc(int locationSrc) {
		this.locationSrc = locationSrc;
	}

	public String getMerchantInvoiceId() {
		return merchantInvoiceId;
	}

	public void setMerchantInvoiceId(String merchantInvoiceId) {
		this.merchantInvoiceId = merchantInvoiceId;
	}

	public String getIcData() {
		return icData;
	}

	public void setIcData(String icData) {
		this.icData = icData;
	}

}
