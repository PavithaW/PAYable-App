package com.mpos.util;

public class EmvParseException  extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String strException;
	
	public EmvParseException(String msg){
		strException = msg ;
	}

	public String toString() {

		if (strException != null)
			return strException;

		return super.toString();

	}

}
