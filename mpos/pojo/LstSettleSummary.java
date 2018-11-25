package com.mpos.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.util.PLog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class LstSettleSummary extends APIList<TxSettlementSummaryEle> {

    @Override
    protected ClassLoader onGetClassLoader() {
        return TxSettlementSummaryEle.class.getClassLoader();
    }

    @Override
    public void setList(String json) {

        Gson gson = new GsonBuilder().create();

        try {
            JSONArray arr = new JSONArray(json);

            if (arr == null || arr.length() == 0) {
                return;
            }

            listObj = new ArrayList<TxSettlementSummaryEle>();

            for (int i = 0; i < arr.length(); i++) {
                String strRes = arr.getJSONObject(i).toString();
                TxSettlementSummaryEle resData = gson.fromJson(strRes, TxSettlementSummaryEle.class);
                listObj.add(resData);
            }

        } catch (JSONException e) {
            PLog.i("JEYLOGS", "Exp:" + e.toString());
        }
    }

}
