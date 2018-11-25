package com.mpos.pojo;

public class ErrorLogBean {

	public static final int LEVEL_ERROR = 1 ;
	public static final int LEVEL_INFO = 2 ;
	
	private String class_name, action, logdate;
	private long id;
	private int loglevel;
	private int logtoken;

	public ErrorLogBean() {
		super();
	}

	public ErrorLogBean(String class_name, String action, int loglevel,int logtoken) {
		super();
		this.class_name = class_name;
		this.action = action;
		this.loglevel = loglevel;
		this.logtoken=logtoken;
	}

	public ErrorLogBean(long id, String class_name, String action, int loglevel,int logtoken, String logdate) {
		super();
		this.id = id;
		this.class_name = class_name;
		this.action = action;
		this.loglevel = loglevel;
		this.logtoken=logtoken;
		this.logdate = logdate;
		

	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getLoglevel() {
		return loglevel;
	}

	public void setLoglevel(int loglevel) {
		this.loglevel = loglevel;
	}

	public String getLogdate() {
		return logdate;
	}

	public void setLogdate(String logdate) {
		this.logdate = logdate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLogtoken() {
		return logtoken;
	}

	public void setLogtoken(int logtoken) {
		this.logtoken = logtoken;
	}

	
}
