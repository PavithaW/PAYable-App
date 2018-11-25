package com.mpos.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mpos.pojo.ErrorLogBean;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class LogDB {

	private static final String PREF_NAME = "LOGDB_PREF";

	private final static String KEY_COUNTER = "logdb_counter";
	private final static String KEY_LAST_ID = "logdb_last_id";

	private SQLiteDatabase db;
	private Context context;

	public LogDB(Context c) {
		context = c;
		OpenHelper openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();

		// TODO Auto-generated constructor stub
	}

	// Adding Error Item

	// Adding Error Item

	public void closeDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public void addErrorItem2(ErrorLogBean error) {
		// SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MetaData.KEY_CLASS_NAME, error.getClass_name());
		values.put(MetaData.KEY_ACTION, error.getAction());
		values.put(MetaData.KEY_LOGLEVEL, error.getLoglevel());
		values.put(MetaData.KEY_LOGTOKEN, error.getLogtoken());
		db.insert(MetaData.TBL_LOGS, null, values);

		int counter = getCounterValue();
		counter++;

		if (counter > 500) {
			deleteAllButLatest400();
			counter = 0;
		}

		setCounterValue(counter);

		// db.close();

	}

	public void addErrorItem(ErrorLogBean error) {
		long val = -1;

		SQLiteStatement is = db.compileStatement(MetaData.INSERT_TBL_LOG);

		is.bindString(1, error.getClass_name());
		is.bindString(2, error.getAction());
		is.bindLong(3, error.getLoglevel());
		is.bindLong(4, error.getLogtoken());
		// is.bindString(5, error.getLogdate());

		val = is.executeInsert();
		is.clearBindings();
		is.close();

		int counter = getCounterValue();
		counter++;

		if (counter > 500) {
			deleteAllButLatest400();
			counter = 0;
		}

		setCounterValue(counter);
		setLastId(val);

	}

	private void setCounterValue(int value) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_COUNTER, value);
		editor.commit();

	}

	private void setLastId(long value) {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(KEY_LAST_ID, value);
		editor.commit();

	}

	private long getLastId() {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getLong(KEY_LAST_ID, 0);
	}

	private int getCounterValue() {
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_COUNTER, 0);
	}

	public int getLogsCount() {
		int res = 0;

		Cursor cur = db.rawQuery(MetaData.GET_COUNT_LOGS, null);

		if (cur.moveToFirst()) {
			res = cur.getInt(0);
		}

		if (cur != null & !cur.isClosed()) {
			cur.close();
		}

		return res;
	}

	// Getting Error Item by Id
	public ErrorLogBean GetErrorBy_Action2(String action) {
		// SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(MetaData.TBL_LOGS,
				new String[] { MetaData.KEY_ID, MetaData.KEY_CLASS_NAME,
						MetaData.KEY_ACTION, MetaData.KEY_LOGLEVEL,
						MetaData.KEY_LOGTOKEN, MetaData.KEY_DATE },
				MetaData.KEY_ACTION + "=?", new String[] { action }, null,
				null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		ErrorLogBean errorUtil = new ErrorLogBean(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getString(2),
				Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor
						.getString(4)), cursor.getString(5));

		return errorUtil;
		

	}

	public ErrorLogBean getLogRowData(int position) {
		ErrorLogBean log = null;

		long id = getLastId() - position;

		String qu = "select id , classname , action ,loglevel,logtoken, logdate from tblogs where id=" + id;

		Cursor cur = db.rawQuery(qu, null);

		if (cur.moveToFirst()) {
			log = new ErrorLogBean();
			//log.setId(id);
			log.setId(position + 1) ;
			log.setClass_name(cur.getString(1));
			log.setAction(cur.getString(2));
			log.setLoglevel(cur.getInt(3));
			log.setLogtoken(cur.getInt(4));
			log.setLogdate(cur.getString(5));
			//Date d = new Date(cur.getLong(5));
			//log.setLogdate(d.toString()) ;
		}

		if (cur != null & !cur.isClosed()) {
			cur.close();
		}

		return log;

	}

	// Getting All Logs
	public ArrayList<ErrorLogBean> getAllErrors() {
		ArrayList<ErrorLogBean> errList = new ArrayList<ErrorLogBean>();

		// SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(MetaData.GET_ALL_LOGS, null);

		if (cursor.moveToFirst()) {
			do {
				ErrorLogBean errorUtil = new ErrorLogBean();
				errorUtil.setId(Integer.parseInt(cursor.getString(0)));
				errorUtil.setClass_name(cursor.getString(1));
				errorUtil.setAction(cursor.getString(2));
				errorUtil.setLoglevel(Integer.parseInt(cursor.getString(3)));
				errorUtil.setLogtoken(Integer.parseInt(cursor.getString(4)));
				errorUtil.setLogdate(cursor.getString(5));

				errList.add(errorUtil);

			} while (cursor.moveToNext());
		}

		return errList;
	}

	public void deleteAllButLatest400() {
		// SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(MetaData.DELETE_ALL_BUT_LATEST);
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			// super(context, MetaData.db_name, null, MetaData.db_version);
			super(context, MetaData.DATABASE_NAME, null,
					MetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MetaData.CREATE_LOG_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MetaData.TBL_LOGS);
			onCreate(db);
		}

	}
}
