package com.mpos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
	
	public static Date stringToDate(String date, String dateFromat) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat, Locale.US);
		sdf.setLenient(false);
		return sdf.parse(date);
		
	}

}
