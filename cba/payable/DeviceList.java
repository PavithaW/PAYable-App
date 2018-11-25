package com.cba.payable;

import com.mpos.pojo.APIData;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceList extends GenericActivity {
	
	TextView txtTitle;

	protected void onInitActivity(APIData... data) {
		setContentView(R.layout.layout_setting);
		
		 txtTitle = (TextView) findViewById(R.id.txtTitle);
		 txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		final ListView listview = (ListView) findViewById(R.id.listview);

		/*String[] values = new String[] {
				getResources().getString(R.string.reader_audio_pin),
				getResources().getString(R.string.reader_audio),
				getResources().getString(R.string.reader_bt) };*/
		
		String[] values = new String[] {"Device 1" , "Audio" , "Bluetooth"} ;

		final DeviceListAdapter adapter = new DeviceListAdapter(this, values);

		listview.setAdapter(adapter);
		
		 listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Config.setActiveReader(getApplicationContext(), arg2 + 1);
				
				adapter.notifyDataSetChanged() ;
			}
			 
		 });
		
	}

}
