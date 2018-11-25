package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleAck extends APIData{
	
	public static final Parcelable.Creator<SimpleAck> CREATOR = new Parcelable.Creator<SimpleAck>() {
		public SimpleAck createFromParcel(Parcel in) {
			return new SimpleAck(in);
		}

		public SimpleAck[] newArray(int size) {
			return new SimpleAck[size];
		}
	};
	
	
	@SerializedName("status")
	@Expose
	private int status ;
	
	
	public SimpleAck(){
		
	}
	
	public SimpleAck(Parcel in){
		status = in.readInt();
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeInt(status);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
