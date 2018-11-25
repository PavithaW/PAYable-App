package com.mpos.pojo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.storage.RecSale;
import com.mpos.util.PLog;

public class LstBatchlistRes extends APIList<BatchlistRes>{
	
	protected ClassLoader onGetClassLoader() {
		return RecSale.class.getClassLoader();
	}

	public void setList(String json) {
		Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy hh:mm:ss a").create();

		try{
			JSONArray arr = new JSONArray(json);
			
			if (arr == null || arr.length() == 0) {
				return;
			}

			listObj = new ArrayList<BatchlistRes>();
			
			for (int i = 0; i < arr.length(); i++) {
				String strRes = arr.getJSONObject(i).toString() ;
				BatchlistRes resData = gson.fromJson(strRes, BatchlistRes.class);
				listObj.add(resData);

			}

		}catch (JSONException e) {
			PLog.i("JEYLOGS", "Exp:" + e.toString());
		}

		
	}

}
