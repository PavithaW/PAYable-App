package com.mpos.connection;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mpos.pojo.APIData;

public class ConThread extends AsyncTask<CallParams, String, ApiResponse> {

    private static final String LOG_TAG = "payable " + ConThread.class.getSimpleName();

    private int callerId;
    private MsgListener msgListener;
    private String strMessage;
    private Context context;

    @Override
    protected ApiResponse doInBackground(CallParams... args) {

        Log.i(LOG_TAG, "doInBackground : ");

        ApiResponse resData = null;

        if (args == null) {
            return resData;
        }

        CallParams para = args[0];

        if (para == null) {
            return resData;
        }

        try {

            String strRes = Connector.callApi(para, context);
            Log.i(LOG_TAG, "strRes = " + strRes);
            APIData jObj = JSMapper.getData(strRes, para);
            resData = new ApiResponse(jObj, para);

        } catch (ApiException e) {

            resData = new ApiResponse(e, para);
        }

        return resData;
    }

    public ConThread(int id, MsgListener listener, String str, Context c) {
        callerId = id;
        msgListener = listener;
        strMessage = str;
        context = c;
    }


    protected void onPreExecute() {

        Log.i(LOG_TAG, "onPreExecute : ");

        if (strMessage != null) {
            onProgressCallBack(strMessage);
        }
    }


    public void initiateCall(CallParams params) {

        Log.i(LOG_TAG, "initiateCall : ");

        execute(params);
    }

    protected void onPostExecute(ApiResponse res) {

        Log.i(LOG_TAG, "onPostExecute : ");

        try {
            APIData data = res.getData();
            onApiSuccessCallBack(res.getParams(), data);
        } catch (ApiException e) {
            onApiErrorCallBack(res.getParams(), e);
        }

        if (strMessage != null) {
            onProgressEndCallBack();
        }

    }

    private void onApiSuccessCallBack(CallParams para, APIData data) {

        Log.i(LOG_TAG, "onApiSuccessCallBack : ");

        if (msgListener != null) {
            if (msgListener.isCallBackSafe()) {
                msgListener.onCallSuccess(para.getEndpoint(), callerId, data);
            }
        }

    }

    private void onApiErrorCallBack(CallParams para, ApiException e) {

        Log.i(LOG_TAG, "onApiErrorCallBack : ");

        if (msgListener != null) {
            if (msgListener.isCallBackSafe()) {
                msgListener.onCallError(para.getEndpoint(), callerId, e);
            }
        }

    }

    private void onProgressCallBack(String message) {

        Log.i(LOG_TAG, "onProgressCallBack : "+message);

        if (msgListener != null) {
            if (msgListener.isCallBackSafe()) {
                msgListener.onCallProgress(callerId, message);
            }
        }
    }

    private void onProgressEndCallBack() {

        Log.i(LOG_TAG, "onProgressEndCallBack : ");

        if (msgListener != null) {
            msgListener.onCallEnd(callerId);
        }
    }

}
