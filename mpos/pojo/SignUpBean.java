package com.mpos.pojo;

public class SignUpBean {

	private String business_type;
	private int language;

	public SignUpBean() {
		super();
	}

	public SignUpBean(String business_type, int language) {
		super();
		this.business_type = business_type;
		this.language = language;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}
	
	

}
