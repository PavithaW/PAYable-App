package com.mpos.connection;

import com.mpos.pojo.APIData;


public interface MsgListener {	
	
	public boolean isCallBackSafe() ;
	public void onCallProgress(int callerId , String message) ;
	public void onCallEnd(int callerId ) ;
	public void onCallError(EnumApi api ,int callerId , ApiException e) ;
	public void onCallSuccess(EnumApi api ,int callerId , APIData data  ) ;

}
