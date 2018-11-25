package com.mpos.connection;

import android.content.Context;
import android.util.Log;

import com.setting.env.Environment;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;


public enum EnumApi {

    SAMPLE_CALL(0, "Sample", "this is sample test api", HttpMethod.POST),
    SignIn(1, "Signin", "login", HttpMethod.POST), //
    SignUp(2, "Signup", "Signup", HttpMethod.POST),
    Verification(3, "Verification", "Verification", HttpMethod.POST),
    VCResend(4, "VerificationRS", "resend Verification code", HttpMethod.POST),
    EReceiptSales(5, "EReceiptSales", "send receit via mail", HttpMethod.POST),
    Sales(6, "Sales", "process sales request", HttpMethod.POST),
    Signature(7, "Signature", "upload Signature", HttpMethod.POST),
    CloseTxHistory(8, "CloseTxHistory", "getting close Transaction history", HttpMethod.POST),
    Settlement(9, "Settlement", "Settlement process", HttpMethod.POST),
    voidTx(10, "Void", "Void transaction", HttpMethod.POST),
    EmvSales(11, "EmvSales", "process sales request", HttpMethod.POST),
    SettlementSummary(12, "SettlementSummary", "tx summary of the open batch", HttpMethod.POST),
    SettlementSummaryV2(12, "SettlementSummaryV2", "tx summary of the open batch", HttpMethod.POST),
    OpenTxHistory(13, "OpenTxHistory", "getting open Transaction history", HttpMethod.POST),
    ReceiptEmail(14, "ReceiptEmail", "sending ereceipt  via email", HttpMethod.POST),
    ReceiptSMS(15, "ReceiptSMS", "sending ereceipt  via SMS", HttpMethod.POST),
    DsSale(16, "DsSale", "sending sales request", HttpMethod.POST),
    DSEmvSale(17, "DSEmvSale", "sending emv sales request", HttpMethod.POST),
    ModifyPassword(18, "ModifyPassword", "change password", HttpMethod.POST),
    DSICCOflineErr(19, "DSICCOflineErr", "upload icc error logs", HttpMethod.POST),
    BatchEmvLogs(20, "BatchEmvLogs", "upload icc logs after processing issuer script", HttpMethod.POST),
    DeviceInfo(21, "DeviceInfo", "send device info", HttpMethod.POST),
    Batchlist(22, "BatchList", "Get batch list", HttpMethod.POST),
    BatchTxHistory(23, "BatchTxHistory", "Get batch tx history", HttpMethod.POST),
    Echo(24, "Echo", "checking network connectivity", HttpMethod.POST),
    KeyEntryTx(25, "KeyEntryTx", "key entry transaction", HttpMethod.POST),
    ResendReceiptEmail(26, "ResendReceiptEmail", "Resend email receipt", HttpMethod.POST),
    ResendReceiptSMS(27, "ResendReceiptSMS", "Resend sms receipt", HttpMethod.POST),
    UserDetail(28,"UserDetail","fetching user detail",HttpMethod.POST),
    DSNfcSale(29, "DSNFCSale", "sending emv sales request", HttpMethod.POST);


    public enum HttpMethod {
        GET, POST;
    }

    ;

    private HttpMethod enumHttpMethod = null;
    private int apicode;
    private String strApi;
    private String strDes;

    EnumApi(int code, String api, String des, HttpMethod method) {
        apicode = code;
        strApi = api;
        strDes = des;
        enumHttpMethod = method;
    }

    public HttpRequestBase getHttpRequest(String data, Context context, int serverIndex)
            throws UnsupportedEncodingException {

        String LOG_TAG = "payable " + EnumApi.class.getSimpleName();

        HttpRequestBase base = null;
        String strUri = Environment.getApiHost(context) + "/" + strApi;

        if (serverIndex == 2) {
            strUri = Environment.getAMEXApiHost() + "/" + strApi;
        } else {
            //strUri = strUri + "/" + strApi  ;
        }

        Log.i(LOG_TAG, "URL: " + strUri);

        switch (enumHttpMethod) {

            case GET: {
                base = new HttpGet(strUri);
            }
            break;

            case POST: {
                HttpPost post = new HttpPost(strUri);
                post.setEntity(new StringEntity((null == data) ? "" : data,
                        HTTP.UTF_8));
                base = post;
            }
            break;

        }

        return base;

    }

}
