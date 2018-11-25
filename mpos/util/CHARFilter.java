package com.mpos.util;

import java.util.regex.Pattern;

public class CHARFilter {
	
	public final static char blockedCharacter[]  = {';' ,'\'' , '\"' , '\\'} ;
	
	
	public static int isBloackedCharExist(String str){
		
		if(str == null){
			return 0 ;
		}
		
		str = str.trim() ;
		
		if( str.length() == 0){
			return 0 ;
		}
		
		int res = 0 ;
		
		for(int i = 0 ; i < blockedCharacter.length ; i++ ){
			res = str.indexOf(blockedCharacter[i]);
			
			if(res >= 0){
				return (i+1) ;
				//return 0 ;
			}
		}
		
		return 0 ;
	}

	// CHECK WHETHER TEXT & NUMBER IS INCLUDED OR NOT
	public static boolean isTextorNumber(String str) {
		if (str.matches("\\d*")) {
			// this is a full NUMBER
			return false;
		} else {
			// not full number
			if (Pattern.matches("[a-zA-Z]+", str)) {
				// this is a full STRING
				return false;
			} else {
				// not full string
				return true;
			}
		}
	}

}
