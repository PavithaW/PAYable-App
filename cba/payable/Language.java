package com.cba.payable;

import android.content.SharedPreferences;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mpos.pojo.APIData;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.PLog;

public class Language extends GenericActivity {
	
	LanguageAdapter adapter;
	public static final String PAYABLE_PREF = "PayablePrefFile";
	
	@Override
	protected void onInitActivity(APIData... data) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.layout_language);
		
		final ListView listview = (ListView) findViewById(R.id.listview);
		
	/*	String[] values = new String[] { 
				getResources().getString(R.string.language1),
				getResources().getString(R.string.language2),
				getResources().getString(R.string.language3)		
		};
		*/

		
		/* adapter = new
				LanguageAdapter(this, values);
		*/
		listview.setAdapter(adapter);
		 


        listview.setOnItemClickListener(new OnItemClickListener() {
        	

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			SharedPreferences preferences = getSharedPreferences(PAYABLE_PREF, 0);
			int value = preferences.getInt("languageSetting",7);
			
			SharedPreferences.Editor editor = getSharedPreferences(PAYABLE_PREF, MODE_PRIVATE).edit();
			
			
			
			int itemPosition = arg2;
			
			if(itemPosition==0){
				editor.putInt("languageSetting", 0);
			}else if(itemPosition==1){
				editor.putInt("languageSetting", 1);
			}else{
				editor.putInt("languageSetting", 2);				
			}				
				editor.commit();
			
			ImageView imageView = (ImageView) arg1.findViewById(R.id.tick);
			imageView.setImageResource(R.drawable.tick);
			imageView.setVisibility(View.VISIBLE);

			adapter.notifyDataSetChanged();
			
		}

      }); 
		
		
		
	}

}
