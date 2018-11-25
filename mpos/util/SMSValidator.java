package com.mpos.util;

public class SMSValidator {
	
	public static boolean validatePhoneNumber(String phoneNo){
		
		if (phoneNo.matches("\\d{9}")) {
			return true ;
		}

		if (phoneNo.matches("\\d{10}")) {
			try {
				//String[] parts = phoneNo.split("0");
				String str = phoneNo.substring(1);
				if (str != null) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}

		if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{7}")) {
			return true;
		}

		if (phoneNo.matches("\\(\\d{3}\\)-\\d{7}")) {
			return true;
		}
		
		
		return false ;
	}

}
