package com.mpos.util;

import com.setting.env.Environment;
import com.setting.env.Environment.Env;

import android.util.Log;




public class PLog {
	
	public static final String TAG = "JEYLOGS" ;
	
	public static void i(String tag , String msg){
		if(Environment.getEnvironment() != Env.LIVE){
			Log.i(tag, msg) ;
		}
	}
	
	public static void e(String tag , String msg){
		if(Environment.getEnvironment() != Env.LIVE){
			Log.e(tag, msg) ;
		}
	}
	
	public static void w(String tag , String msg){
		if(Environment.getEnvironment() != Env.LIVE){
			Log.w(tag, msg) ;
		}
	}

}
