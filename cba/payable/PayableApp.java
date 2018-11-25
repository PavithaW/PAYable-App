package com.cba.payable;

import com.mpos.storage.LogDB;

import android.app.Application;

public class PayableApp extends Application {

	/*
	 * private LocalDb db;
	 * 
	 * public void onCreate() { super.onCreate(); db = new LocalDb(this); }
	 * 
	 * public LocalDb getDbInstance(){ return db ; }
	 * 
	 * public void onTerminate(){ db.closeDb(); super.onTerminate(); }
	 */

	private LogDB db;

	public void onCreate() {
		super.onCreate();
		db = new LogDB(this);
		db.deleteAllButLatest400() ;
	}

	public LogDB getDbInstance() {

		return db;
	}

	public void onTerminate() {
		db.closeDb();
		super.onTerminate();
	}

}
