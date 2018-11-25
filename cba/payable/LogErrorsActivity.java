package com.cba.payable;

import java.util.ArrayList;

import com.mpos.pojo.APIData;
import com.mpos.pojo.ErrorLogBean;
import com.mpos.storage.LogDB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LogErrorsActivity extends GenericActivity {

	ListView lstError;
	ErrorLogAdapter errorlog_adapter;

	ArrayList<ErrorLogBean> errList;

	  
	
	
	
	protected void onInitActivity(APIData... data){
		setContentView(R.layout.activity_log_errors);
		InitViews();
	}

	private void InitViews() {


		final PayableApp payapp = (PayableApp) getApplicationContext();



		lstError = (ListView) findViewById(R.id.lstError);

		//logdb.deleteAllButLatest400();


		errorlog_adapter = new ErrorLogAdapter(LogErrorsActivity.this,logDb);
		lstError.setAdapter(errorlog_adapter);

		lstError.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub

				/*String logclass = errList.get(pos).getClass_name();
				String logdate = errList.get(pos).getLogdate();
				String logdesc = errList.get(pos).getAction();
				int loglevel = errList.get(pos).getLoglevel();
				int logtoken = errList.get(pos).getLogtoken();

				Intent in = new Intent(getApplicationContext(), LogDescriptionActivity.class);
				in.putExtra("logclass", logclass);
				in.putExtra("logdate", logdate);
				in.putExtra("logdesc", logdesc);
				in.putExtra("loglevel", loglevel);
				in.putExtra("logtoken", logtoken);
				startActivity(in);*/

			}
		});
	}

	public void onNavBack(View v) {

		onNavBackPressed();
	}

}
