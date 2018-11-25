package com.mpos.connection;

public class ApiException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public static final int INTERNAL_ERROR = 1000 ;
	public static final int TIMEOUT_ERROR = 1001 ;
	public static final int SCOCKET_EXCEPTION = 1002 ; 
	
	public static final int SWIPE_ON_ICC_EXECPTION = 30006 ;
	
	
	private String strException ;
	private int errCode ;
	
	private EnumException enumEx ;
	
	private int isState = 0 ;
	private String isMessage = null ;
	
	
	public ApiException(int code ,String msg){
		strException = msg;
		errCode = code;
		
		if(errCode == 30001){
			enumEx = EnumException.NO_RECORD_FOUND ;
		}
		
		if(errCode == 40001){
			enumEx = EnumException.NO_OPEN_TX ;
		}
		
	}
	
	public int getErrcode(){
		return errCode ;
	}
	
	public EnumException getEnumException(){
		return enumEx ;
	}
	
	public String toString(){
		
		if(strException != null)
			return strException  ;
		
		return super.toString() + "  Err code:" + errCode ;
		
	}

	public int getIsState() {
		return isState;
	}

	public void setIsState(int isState) {
		this.isState = isState;
	}

	public String getIsMessage() {
		return isMessage;
	}

	public void setIsMessage(String isMessage) {
		this.isMessage = isMessage;
	}

	
}
