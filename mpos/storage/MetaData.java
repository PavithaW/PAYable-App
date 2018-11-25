package com.mpos.storage;

public class MetaData {

	public static final int db_version = 3;
	public static final String db_name = "txapp.db";

	static final String TBL_SALES = "tblSales";
	static final String CREATE_TBL_SALES = "CREATE TABLE tblSales  (id integer PRIMARY KEY AUTOINCREMENT , txId long , cardHolder varchar , ccLast4 varchar , amount double  , cardType integer , ts long , status integer , approvalCode integer ) ";
	static final String INSERT_TBL_SALES = "insert into tblSales (txId , cardHolder , ccLast4 ,amount, cardType , ts ,status ,approvalCode  ) values (?,?,?,?,?,?,?,?)  ";

	
	// All Static variables
		// Database Version
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "logs.db";

		// logs table name
		static final String TBL_LOGS = "tblogs";
	    
		static final String KEY_ID ="id";
		static final String KEY_CLASS_NAME ="classname";
		static final String KEY_ACTION="action";
		static final String KEY_LOGLEVEL="loglevel";
		static final String KEY_LOGTOKEN="logtoken";
		static final String KEY_DATE ="logdate";
		
		static final String CREATE_LOG_TABLE=
				                "CREATE TABLE " + TBL_LOGS +"( " 
				                + KEY_ID + " integer primary key autoincrement not null, "
				                + KEY_CLASS_NAME + " text not null, "
				                + KEY_ACTION + " text not null, "
				                + KEY_LOGLEVEL + " integer not null, "
				                + KEY_LOGTOKEN + " integer not null, "
				                + KEY_DATE + " default current_timestamp not null"
				                + " ) ";
				                    
				
		static final String GET_ALL_LOGS = "select * from "+ TBL_LOGS +" order by id desc";		
				
	    static final String DELETE_ALL_BUT_LATEST = "delete from tblogs where id not in ( select id from tblogs order by id desc limit 400)";
	    
	    //static final String INSERT_TBL_LOG = "insert into tblogs (classname , action ,loglevel) values (?,?,?)";
	    
	    static final String INSERT_TBL_LOG = "insert into tblogs (classname , action , loglevel, logtoken) values (?,?,?,?)";
	    
	    static final String GET_COUNT_LOGS="select count(*) from tblogs";

	
}
