package com.mpos.storage;

import java.util.ArrayList;
import java.util.Date;

import com.mpos.pojo.TxSaleRes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class LocalDb {

	

	private Context context;
	private SQLiteDatabase db;

	public LocalDb(Context c) {
		context = c;
		OpenHelper openHelper = new OpenHelper(context);
		this.db = openHelper.getWritableDatabase();
	}

	public void closeDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public boolean isOpen() {
		return db.isOpen();
	}
	
	
	public int getNumberOfCardType(){
		int res = 0;
		
		Cursor cur = db.rawQuery("select count(*) from tblSales GROUP BY cardType", null);
		
		if (cur.moveToFirst()) {
			do{
				int val = cur.getInt(0);
				res = res + val ;
			}while(cur.moveToNext()) ;
		}
		
		

		if (cur != null & !cur.isClosed()) {
			cur.close();
		}
		
		return res;
	}
	
	public int voidTransaction(long tx){
		ContentValues cv = new ContentValues();
		//cv.put("status", RecSale.STATUS_VOID);
		return db.update(MetaData.TBL_SALES, cv, "txId = " + tx, null) ;

	}
	
	public void refershTable(){
		db.delete(MetaData.TBL_SALES, null, null);
		db.delete("sqlite_sequence", "name =?",
				new String[] { MetaData.TBL_SALES });

	}

	public int getTotalSales() {
		
		int res = 0;
		
		Cursor cur = db.rawQuery("select count(*) from tblSales", null);


		if (cur.moveToFirst()) {
			res = cur.getInt(0);
		}

		if (cur != null & !cur.isClosed()) {
			cur.close();
		}

		return res;
	}
	
	public ArrayList<RecSettle> getSettlementRowData(){
		
		ArrayList<RecSettle> arrRes = null ;
		
		Cursor cur = db.rawQuery("select cardType,count(*),sum(amount) from tblSales GROUP BY cardType", null);
		
		if (cur.moveToFirst()) {
			arrRes = new ArrayList<RecSettle>() ;
			
			do{
				RecSettle r = new RecSettle() ;
				
				r.setCardType(cur.getInt(0)) ;
				r.setCount(cur.getInt(1)) ;
				r.setTotalAmount(cur.getDouble(2)) ;
				
				arrRes.add(r) ;
				
			}while(cur.moveToNext()) ;
		}
		
		if (cur != null & !cur.isClosed()) {
			cur.close();
		}
		
		return arrRes ;
		
	}

	public RecSale getSalesRowData(int i) {
		
		RecSale rec = null ;

		String qu = "select txId , cardHolder , ccLast4 ,amount, cardType , ts ,status ,approvalCode from tblSales where id="
				+ (i + 1);
		
		Cursor cur = db.rawQuery(qu, null);
		
		if (cur.moveToFirst()){
			rec = new RecSale() ;
			rec.setId(i) ;
			rec.setTxId(cur.getLong(0)) ;
			rec.setCardHolder(cur.getString(1)) ;
			rec.setCcLast4(cur.getString(2)) ;
			rec.setAmount(cur.getDouble(3));
			rec.setCardType(cur.getInt(4)) ;
			rec.setTs(cur.getLong(5)) ;
			rec.setTime(new Date(rec.getTs())) ;
			rec.setStatus(cur.getInt(6)) ;
			//rec.setApprovalCode(cur.getInt(7)) ;
		}
		
		if (cur != null & !cur.isClosed()) {
			cur.close();
		}

		return rec;
	}

	public void clearSalesTable() {
		db.delete(MetaData.TBL_SALES, null, null);
		db.delete("sqlite_sequence", "name =?",
				new String[] { MetaData.TBL_SALES });
	}

	public long saveSalesData(TxSaleRes sale) {

		long val = -1;

		SQLiteStatement is = db.compileStatement(MetaData.INSERT_TBL_SALES);

		is.bindLong(1, sale.getTxId());
		is.bindString(2, sale.getCardHolder());
		is.bindString(3, sale.getCcLast4());
		is.bindDouble(4, sale.getAmount());
		is.bindLong(5, sale.getCardType());
		//is.bindLong(6, sale.getServerTime().getTime());
		is.bindLong(7, RecSale.STATUS_OPEN);
		//is.bindLong(8, sale.getApprovalCode());

		val = is.executeInsert();
		is.clearBindings();
		is.close();

		return val;
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, MetaData.db_name, null, MetaData.db_version);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MetaData.CREATE_TBL_SALES);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MetaData.TBL_SALES);
			onCreate(db);
		}
	}

}
