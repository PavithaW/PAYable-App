package com.mpos.util;

public class PasswordValidator {

	public static boolean validate(String password) {

		if (password == null) {
			return false;
		}

		password = password.trim();

		if (password.length() == 0) {
			return false;
		}

		if (password.length() < 7) {
			return false;
		}
		
		// ^.(?=.[a-z])(?=.[A-Z])(?=.[0-9]).$
		
		// "^[a-zA-Z0-9]*$"
		if (password.matches("^[a-zA-Z0-9]*$")) {
			return true;
		}
		
		/*if (password.matches("^([a-zA-Z+]+[0-9+]+)$")) {
			return true;
		}*/

		return false;
	}

}
