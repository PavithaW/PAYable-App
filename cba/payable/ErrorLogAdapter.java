package com.cba.payable;

import java.util.ArrayList;


import com.mpos.pojo.ErrorLogBean;
import com.mpos.storage.LogDB;
import com.mpos.util.TypeFaceUtils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ErrorLogAdapter extends BaseAdapter {

	//ArrayList<ErrorLogBean> itemList;

	public Activity context;
	public LayoutInflater inflater;
	private int mLastPosition = -1;
	
	protected LogDB logDb ;
	private int no_of_records = 0;

	//ArrayList<ErrorLogBean> AllData = new ArrayList<ErrorLogBean>();

	public ErrorLogAdapter(Activity context, LogDB db) {
		// TODO Auto-generated constructor stub

		super();
		this.context = context;
		logDb = db ;
		no_of_records = db.getLogsCount() ;

	}

	@Override
	public int getCount() {
		if(no_of_records < 0){
			return 0 ;
		}
		return no_of_records ;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null ;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {

		TextView txtId, txtClassname, txtDate, txtError;
		RelativeLayout rlVertical, rlDivider;

	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lstdata_logs, null);
			holder = new ViewHolder();

			holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
			holder.txtClassname = (TextView) convertView.findViewById(R.id.txtClassname);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			holder.txtError = (TextView) convertView.findViewById(R.id.txtError);

			holder.rlVertical = (RelativeLayout) convertView.findViewById(R.id.rlVertical);
			holder.rlDivider = (RelativeLayout) convertView.findViewById(R.id.rlDivider);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		// ErrorLogBean bean = (ErrorLogBean) itemList.get(pos); logDb
		
		ErrorLogBean bean = logDb.getLogRowData(pos);

		if(bean != null){
			holder.txtId.setText(String.valueOf(bean.getId()));
			holder.txtId.setTypeface(TypeFaceUtils.setLatoRegular(context));

			holder.txtClassname.setText(bean.getClass_name());
			holder.txtClassname.setTypeface(TypeFaceUtils.setLatoRegular(context));

			holder.txtError.setText(bean.getAction());
			holder.txtError.setTypeface(TypeFaceUtils.setLatoRegular(context));

			holder.txtDate.setText(bean.getLogdate());
			holder.txtDate.setTypeface(TypeFaceUtils.setLatoRegular(context));

			if (bean.getLoglevel() == ErrorLogBean.LEVEL_ERROR) {
				convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.loglevel_selector));
				holder.rlDivider.setBackgroundColor(Color.parseColor("#FFFFFF"));
				holder.txtClassname.setTextColor(Color.parseColor("#666666"));
				holder.txtError.setTextColor(Color.parseColor("#FF3535"));
				holder.txtDate.setTextColor(Color.parseColor("#666666"));
			} else {
				convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.log_selector));
				holder.rlDivider.setBackgroundColor(Color.parseColor("#e5e5e5"));
				holder.txtClassname.setTextColor(Color.parseColor("#666666"));
				holder.txtError.setTextColor(Color.parseColor("#666666"));
				holder.txtDate.setTextColor(Color.parseColor("#666666"));
			}
		}


		return convertView;
	}

}
