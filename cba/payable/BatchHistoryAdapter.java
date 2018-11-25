package com.cba.payable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpos.connection.PaginationHandler;
import com.mpos.pojo.BatchlistRes;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;

public class BatchHistoryAdapter extends MposPageAdapter<BatchlistRes>{
	
	private DecimalFormat df;
	private SimpleDateFormat sdf;
	
	Tamil_LangSelect tamlang_select;
	Sinhala_LangSelect sinlang_select;
	
	int langstatus=0;
	
	public BatchHistoryAdapter(Context c, PaginationHandler ph, int pageSize, int cId) {
		super(c, ph, pageSize, cId);
		df = new DecimalFormat("####0.00");
		//sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.US);
		sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

		Activity activity = (Activity) c;
		
		tamlang_select = new Tamil_LangSelect(c, activity);
		sinlang_select = new Sinhala_LangSelect(c, activity);
		
		 langstatus = LangPrefs.getLanguage(c);
	}
	


	protected View renderListRow(BatchlistRes data, int position,
			View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.lstdata_settle, null);
			v.setTag(new Integer(ROW_DATA)) ;
		}else{
			int tag = ((Integer)convertView.getTag()).intValue() ;
			
			if(tag != ROW_DATA){
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.lstdata_settle, null);
				v.setTag(new Integer(ROW_DATA)) ;
			}
		}

		String ct = UtilityFunction.getCurrencyTypeString(data.getCurrencyType());

		TextView txtTotal = (TextView) v.findViewById(R.id.txtTotal);
		txtTotal.setText(df.format(data.getSalesTotal()) + " " + ct);
		
		TextView txtTs = (TextView) v.findViewById(R.id.txtDate);
		txtTs.setTypeface(TypeFaceUtils.setRobotoMedium(context));
		txtTs.setText(sdf.format(data.getServerTime())) ;
		
		TextView txtSaleCount = (TextView) v.findViewById(R.id.txtSalesValue); 
		TextView txtVoidcount = (TextView) v.findViewById(R.id.txtVoidValue); 
		
		TextView txtSales = (TextView)v.findViewById(R.id.txtSales);
		TextView txtVoids = (TextView)v.findViewById(R.id.txtVoids);
		
		
		txtSaleCount.setText("" + data.getNoOfSale()) ;
		txtVoidcount.setText("" + data.getNoOfVoid()) ;
		
		
		if (langstatus == LangPrefs.LAN_EN) {
			
		} else if (langstatus == LangPrefs.LAN_TA) {
			tamlang_select.Apply_SettlementListData(txtSales, txtVoids);
		} else if (langstatus == LangPrefs.LAN_SIN) {
			sinlang_select.Apply_SettlementListData(txtSales, txtVoids);
		} else {

		}
		
		
		
		
		return v;
	}

}
