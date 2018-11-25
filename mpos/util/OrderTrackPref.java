package com.mpos.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OrderTrackPref {

	private static final String PREF_NAME = "ORDER_PREF";

	public static final String KEY_ORDER_STATUS = "order_status";

	public static final int ORD_OFF = 0;
	public static final int ORD_ON = 1;
	
	public static void setOrderStatus(Context c,int order)
	{
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_ORDER_STATUS, order) ;
		editor.commit() ;
	}
	
	public static int getOrderStatus(Context c){
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_ORDER_STATUS, ORD_OFF) ;
	}


}
