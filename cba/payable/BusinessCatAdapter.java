package com.cba.payable;

import java.util.ArrayList;

import com.mpos.pojo.SignUpBean;
import com.mpos.util.TypeFaceUtils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusinessCatAdapter extends BaseAdapter {

	ArrayList<SignUpBean> itemList;

	public Activity context;
	public LayoutInflater inflater;
	private int mLastPosition = -1;

	ArrayList<SignUpBean> AllData = new ArrayList<SignUpBean>();

	public BusinessCatAdapter(Activity context, ArrayList<SignUpBean> itemList) {
		// TODO Auto-generated constructor stub

		super();
		this.context = context;
		this.itemList = itemList;
		AllData = (ArrayList<SignUpBean>) itemList.clone();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {

		TextView txtBusinessCat;

	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lstbusiness_data, null);
			holder = new ViewHolder();

			holder.txtBusinessCat = (TextView) convertView.findViewById(R.id.txtBusinessCat);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		SignUpBean bean = (SignUpBean) itemList.get(pos);
		
		if(bean.getLanguage()==0)
		{
		holder.txtBusinessCat.setText(bean.getBusiness_type());
		holder.txtBusinessCat.setTypeface(TypeFaceUtils.setRobotoRegular(context));
		
		}
		
		else if(bean.getLanguage()==1)
		{
			holder.txtBusinessCat.setText(bean.getBusiness_type());
			holder.txtBusinessCat.setTypeface(TypeFaceUtils.setBaminiFont(context));
		}
		
		else
		{
			holder.txtBusinessCat.setText(bean.getBusiness_type());
			holder.txtBusinessCat.setTypeface(TypeFaceUtils.setSinhalaFont(context));
		}

		return convertView;
	}

}
