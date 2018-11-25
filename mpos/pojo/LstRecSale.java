package com.mpos.pojo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.storage.RecSale;
import com.mpos.util.PLog;

public class LstRecSale extends APIList<RecSale>{

	@Override
	protected ClassLoader onGetClassLoader() {
		return RecSale.class.getClassLoader();
	}

	@Override
	public void setList(String json) {
		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy hh:mm:ss a").create();

		try{
			JSONArray arr = new JSONArray(json);
			
			if (arr == null || arr.length() == 0) {
				return;
			}

			listObj = new ArrayList<RecSale>();
			
			for (int i = 0; i < arr.length(); i++) {
				String strRes = arr.getJSONObject(i).toString() ;
				RecSale resData = gson.fromJson(strRes, RecSale.class);
				listObj.add(resData);

			}

		}catch (JSONException e) {
			PLog.i("JEYLOGS", "Exp:" + e.toString());
		}

		
	}

}
