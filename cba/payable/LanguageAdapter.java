package com.cba.payable;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class LanguageAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	public static final String PAYABLE_PREF = "PayablePrefFile";
	public static final String TAG = "LanguageAdapter";
	
	public LanguageAdapter(Context context, String[] values) {
		super(context, R.layout.layout_language_list, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.layout_language_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.tick);
		textView.setText(values[position]);
		

		if (position==0) {
			imageView.setImageResource(R.drawable.en);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		} else if (position==1) {
			imageView.setImageResource(R.drawable.si);
			Typeface fontFamily = Typeface.createFromAsset(context.getAssets(),
		            "fonts/iskpota.ttf");
			textView.setTypeface(fontFamily);
		} else if (position==2) {
			imageView.setImageResource(R.drawable.ta);
		} 
		
		
		
		SharedPreferences prefs = context.getSharedPreferences(PAYABLE_PREF,0);
		int languageSetting = prefs.getInt("languageSetting",0);
		
		
		imageView1.setVisibility(View.INVISIBLE);
		
		if(languageSetting == position){
			imageView1.setImageResource(R.drawable.tick);
			imageView1.setVisibility(View.VISIBLE);
		}
		

		return rowView;
	}
}
