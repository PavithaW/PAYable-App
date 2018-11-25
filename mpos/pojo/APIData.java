package com.mpos.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Parcelable;

public abstract class APIData implements Parcelable{
	
	public String getJson(){
		final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() ; 
		//builder.setDateFormat("yyyy-MM-dd kk:mm a");
		builder.setDateFormat("MMM dd, yyyy hh:mm:ss aa");
	    final Gson gson = builder.create() ;
	    return gson.toJson(this) ;
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
