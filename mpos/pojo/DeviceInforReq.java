package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInforReq extends APIData {
	
	public static final Parcelable.Creator<DeviceInforReq> CREATOR = new Parcelable.Creator<DeviceInforReq>() {
		public DeviceInforReq createFromParcel(Parcel in) {
			return new DeviceInforReq(in);
		}

		public DeviceInforReq[] newArray(int size) {
			return new DeviceInforReq[size];
		}
	};
	
	@SerializedName("IMEI")
	@Expose
	private String strIMEI ;
	
	@SerializedName("SimSerial")
	@Expose
	private String strSimSerial ;
	
	@SerializedName("token")
	@Expose
	private String strToken ;
	
	public DeviceInforReq(){
		
	}
	
	public DeviceInforReq(Parcel in){
		strIMEI = in.readString() ;
		strSimSerial = in.readString();
		strToken = in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeString(strIMEI);
		out.writeString(strSimSerial);
		out.writeString(strToken);
	}

	public String getStrIMEI() {
		return strIMEI;
	}

	public void setStrIMEI(String strIMEI) {
		this.strIMEI = strIMEI;
	}

	public String getStrSimSerial() {
		return strSimSerial;
	}

	public void setStrSimSerial(String strSimSerial) {
		this.strSimSerial = strSimSerial;
	}

	public String getStrToken() {
		return strToken;
	}

	public void setStrToken(String strToken) {
		this.strToken = strToken;
	}
	
	

}
