package com.cba.payable;

import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceListAdapter extends BaseAdapter {

	private final Context context;
	private final String[] rowValues;

	public DeviceListAdapter(Context c, String[] values) {
		context = c;
		rowValues = values;
	}

	@Override
	public int getCount() {
		if (rowValues == null) {
			return 0;
		}

		return rowValues.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/*View rowView = LayoutInflater.from(context).inflate(
				R.layout.layout_language_list, parent, false);*/
		
		View rowView = LayoutInflater.from(context).inflate(
				R.layout.lst_devicedata, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.txtDeviceData);
		textView.setTypeface(TypeFaceUtils.setRobotoMedium(context));
		
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imgCheck);
		textView.setText(rowValues[position]);

		//imageView.setVisibility(View.GONE);

		if (position == 0) {
			if(Config.getActiveReader(context) == Config.READER_DEVICE1){
				imageView1.setImageResource(R.drawable.checked_filter);
				imageView1.setVisibility(View.VISIBLE);
			}else{
				imageView1.setVisibility(View.INVISIBLE);
			}
		}

		if (position == 1) {
			if(Config.getActiveReader(context) == Config.READER_DS_AUDIO){
				imageView1.setImageResource(R.drawable.checked_filter);
				imageView1.setVisibility(View.VISIBLE);
			}else{
				imageView1.setVisibility(View.INVISIBLE);
			}
		}

		if (position == 2) {
			int readerId = Config.getActiveReader(context) ;
			if (!(readerId == Config.READER_DEVICE1 || readerId == Config.READER_DS_AUDIO)){
				imageView1.setImageResource(R.drawable.checked_filter);
				imageView1.setVisibility(View.VISIBLE);
			}else{
				imageView1.setVisibility(View.INVISIBLE);
			}
		}

		return rowView;
	}

}
