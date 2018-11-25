package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class VFCode extends APIData{
	
	public static final Parcelable.Creator<VFCode> CREATOR = new Parcelable.Creator<VFCode>() {
		public VFCode createFromParcel(Parcel in) {
			return new VFCode(in);
		}

		public VFCode[] newArray(int size) {
			return new VFCode[size];
		}
	};
	
	@SerializedName("mobilecode")
	@Expose
	private String mobilecode ;
	
	@SerializedName("emailcode")
	@Expose
	private String emailCode ;

	public VFCode(){
		
	}
	
	public VFCode(Parcel in){
		mobilecode = in.readString();
		emailCode = in.readString();
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mobilecode);
		out.writeString(emailCode);
	}

	public String getMobilecode() {
		return mobilecode;
	}

	public void setMobilecode(String mobilecode) {
		this.mobilecode = mobilecode;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
	
	
}
