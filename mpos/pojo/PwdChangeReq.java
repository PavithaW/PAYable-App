package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PwdChangeReq extends APIData{
	
	public static final Parcelable.Creator<PwdChangeReq> CREATOR = new Parcelable.Creator<PwdChangeReq>() {
		public PwdChangeReq createFromParcel(Parcel in) {
			return new PwdChangeReq(in);
		}

		public PwdChangeReq[] newArray(int size) {
			return new PwdChangeReq[size];
		}
	};

	
	@SerializedName("pwd")
	@Expose
	private String pwd ;
	
	@SerializedName("newPwd")
	@Expose
	private String newPwd ;
	
	@SerializedName("confirmPwd")
	@Expose
	private String confirmPwd ;
	
	
	public PwdChangeReq() {

	}
	
	public PwdChangeReq(Parcel in){
		pwd = in.readString();
		newPwd = in.readString();
		confirmPwd = in.readString();
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeString(pwd);
		out.writeString(newPwd);
		out.writeString(confirmPwd);
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}
	
	

}
