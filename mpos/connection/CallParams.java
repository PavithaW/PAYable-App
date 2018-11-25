package com.mpos.connection;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

public class CallParams {

	private String strPayLoad = null;
	private EnumApi enumEndpoint = null;
	private Bundle getParams = null;	
	
	private String strUserId = null ;
	private static String strLogin = null ;
	private String strAuthId1 = null ;
	private String strAuthId2 = null ;
	
	private  String deviceId = null;
	private  String simId = null;
	
	private Context context ;
	
	private int serverIndex = 1 ;

	public CallParams(String uid,String lgn , String aid1 , String aid2 ,String did , String sid , EnumApi api , Context c){
		strUserId = uid ;
		strLogin = lgn ;
		strAuthId1 = aid1 ;
		strAuthId2 = aid2 ;
		deviceId = did ;
		simId = sid ;
		enumEndpoint = api ;
		context = c ;
	}
	

	public void setPayLoad(String data) {
		strPayLoad = data;
	}

	public EnumApi getEndpoint() {
		return enumEndpoint;
	}

	public String getPayLoadStr() {
		if(strPayLoad == null){
			return "{}" ;
		}
		return strPayLoad ;
	}

	public void setGetQueryParam(String key, String data) {

		if (getParams == null) {
			getParams = new Bundle();
		}

		getParams.putString(key, data);
	}

	public Bundle getGetQueryParam() {
		return getParams;
	}


	public String getStrUserId() {
		return strUserId;
	}

	public String getStrLogin(){
		return strLogin ;
	}

	public String getStrAuthId1() {
		return strAuthId1;
	}


	public String getStrAuthId2() {
		return strAuthId2;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public String getSimId() {
		return simId;
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}

	public int getServerIndex() {
		return serverIndex;
	}

	public void setServerIndex(int serverIndex) {
		this.serverIndex = serverIndex;
	}

}
