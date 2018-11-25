package com.mpos.connection;

public enum EnumException {
	
	INTERNAL_ERROR(100,"internal error"),
	PROCESS_BLOCKED(100,"couldn't complete the request"),
	INVALID_USER_AGENT(10000,"user agent is not valid"),
	INVALID_AUTHENTICATION(10001,"Invalid authentication") ,
	
	USER_NAME_EXIST(10002,"Username already exist"),
	MOBILE_NO_EXIST(10003,"Mobile number already exist"),
	SIM_ID_EXIST(10004,"Sim Id already exist"),
	DEVICE_ID_EXIST(10005,"Device Id already exist"),
	CR_ID_EXIST(10006,"Card Reader already exist"),
	EMAIL_ID_EXIST(10007,"Email Id already exist"),
	
	INVALID_VERIFICATION_CODE(10008,"Invalid verification code"),
	ACCESS_DENIED(10009,"user is blocked to access the system"),
	INVALID_REQUEST(10010,"Invalid requset"),
	INVALID_BANKRECORD(10011,"Invalid Bank record"),
	BANK_ACCESS_BLOCKED(10012,"Merchant bank account is denied"),
	
	
	NO_OPEN_TX(40001,"There are no open transactions."),
	
	TX_CLOSED(20001,"Transaction already closed"),
	TX_VOIDED(20002,"Transaction already voided"),
	

	NO_RECORD_FOUND(30001,"No record found");
	
	
	private int m_code ;
	private String m_description ;

	EnumException(int code, String des) {
		m_code = code ;
		m_description = des ;
	}
	
	public int getCode(){
		return m_code ;
	}
	
	public String getDescription(){
		return m_description ;
	}
	
	
}
