package com.mpos.util;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LangPrefs {
	
	
	private static final String PREF_NAME = "LANG_PREF";
	
	public static final String KEY_LANG_STATUS ="lang_status";
	
	public static final int LAN_EN =0;
	public static final int LAN_TA =1;
	public static final int LAN_SIN =2;
	
	
//	public static void ApplyLanguage(Context context,String lang_status)
//	{
//		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//		Editor editor = pref.edit();
//		editor.putString(KEY_LANG_STATUS, lang_status);		
//		editor.commit();
//	}
	
	public static void setLanguage(Context c, int lang){
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_LANG_STATUS, lang) ;
		editor.commit() ;
	}
	
	public static int getLanguage(Context c){
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_LANG_STATUS, LAN_EN) ;
	}
	
//	public static HashMap<String,String> getLangDetails(Context context)
//	{
//		HashMap<String, String> lang_details = new HashMap<String, String>();
//		
//		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//		lang_details.put(KEY_LANG_STATUS, pref.getString(KEY_LANG_STATUS, null));
//		
//		return lang_details;
//	}
	
	

}
