package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class BatchTxHistoryReq extends APIData{
	
	public static final Parcelable.Creator<BatchTxHistoryReq> CREATOR = new Parcelable.Creator<BatchTxHistoryReq>() {
		public BatchTxHistoryReq createFromParcel(Parcel in) {
			return new BatchTxHistoryReq(in);
		}

		public BatchTxHistoryReq[] newArray(int size) {
			return new BatchTxHistoryReq[size];
		}
	};
	
	@SerializedName("settleId")
	@Expose
	private long settleId ;

	@SerializedName("pageId")
	@Expose
	private int pageId ;
	
	@SerializedName("pageSize")
	@Expose
	private int pageSize ;

	
	public BatchTxHistoryReq() {

	}

	public BatchTxHistoryReq(Parcel in){
		settleId = in.readLong() ;
		pageId = in.readInt() ;
		pageSize = in.readInt() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeLong(settleId);
		out.writeInt(pageId) ;
		out.writeInt(pageSize);
	}

	public long getSettleId() {
		return settleId;
	}

	public void setSettleId(long settleId) {
		this.settleId = settleId;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
}
