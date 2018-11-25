package com.cba.payable;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.connection.MsgListener;
import com.mpos.pojo.APIData;
import com.mpos.pojo.ErrorLogBean;
import com.mpos.pojo.Merchant;
import com.mpos.storage.LogDB;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.Crypto;
import com.mpos.util.LangPrefs;
import com.mpos.util.PLog;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;

abstract public class GenericActivity extends FragmentActivity implements MsgListener, HDErrorCallBack {

    private static final String LOG_TAG = "payable " + GenericActivity.class.getSimpleName();

    protected MsgClient m_client;
    protected MsgClientAmex m_client_amex;

    // protected LocalDb m_localDb;w\
    protected LogDB logDb;

    private boolean isActivityAvailable;
    protected boolean isFreshLaunch;
    protected boolean isVisible;

    protected DateFormat dateFormat, timeFormat;

    private ProgressDialog progress;
    private Dialog progressV2;

    private static final int NAVIGATE_TO_SALE = 10001;
    private static final int NAVIGATE_TO_HOME = 10002;
    private static final int NAVIGATE_TO_LOGOUT = 10003;
    private static final int NAVIGATE_TO_OPENTX = 10004;
    private static final int CLOSE_ERECEIPT = 10005;

    protected Tamil_LangSelect tamlang_select;
    protected Sinhala_LangSelect sinlang_select;

    int lang_status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate : ");


        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);
        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        _defaultConfig();
        isActivityAvailable = true;
        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);

        PayableApp app = ((PayableApp) getApplicationContext());
        logDb = app.getDbInstance();

        APIData[] data = null;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("data")) {
                Parcelable[] parcelData = extras.getParcelableArray("data");
                if (parcelData != null) {
                    if (parcelData.length > 0) {
                        data = new APIData[parcelData.length];
                        Log.d(LOG_TAG, "onCreate : parcelData.length = " + parcelData.length);
                        for (int i = 0; i < parcelData.length; i++) {
                            data[i] = (APIData) parcelData[i];
                        }
                    }
                }
            }
        }

        dateFormat = DateFormat.getDateInstance();
        timeFormat = DateFormat.getTimeInstance();

        isFreshLaunch = true;
        // setupCardReader();
        onInitActivity(data);

        setupCardReader();

    }

    protected void lockView(boolean state, int... ids) {

        Log.d(LOG_TAG, "lockView : ");

        if (ids != null) {
            for (int id : ids) {
                View v = findViewById(id);
                v.setEnabled(!state);
            }
        }
    }

    private void _defaultConfig() {

        Log.d(LOG_TAG, "_defaultConfig : ");

        config();
    }

    protected void config() {
        Log.d(LOG_TAG, "config : ");
    }

    protected void setupCardReader() {
        Log.d(LOG_TAG, "setupCardReader : ");
    }

    abstract protected void onInitActivity(APIData... data);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    protected void onResume() {
        Log.d(LOG_TAG, "onResume : ");

        isFreshLaunch = false;
        isVisible = true;
        isActivityAvailable = true;

        super.onResume();

        // hideSoftKeyboard() ;
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    protected void onRestart() {

        Log.d(LOG_TAG, "onRestart : ");

        isActivityAvailable = true;
        super.onRestart();
    }

    protected void onPause() {

        Log.d(LOG_TAG, "onPause : ");

        isVisible = false;
        super.onPause();
    }

	/*
     * protected void onRestoreInstanceState(Bundle outState){ Log.i(TAG,
	 * "inside onRestoreInstanceState. - " + getClass().getName());
	 * isActivityAvailable = true; super.onRestoreInstanceState(outState); }
	 */

    protected void onSaveInstanceState(Bundle outState) {
        // Log.i(TAG, "inside onSaveInstanceState. - " + getClass().getName());
        // isActivityAvailable = false;
        super.onSaveInstanceState(outState);
    }

    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy : ");

        isActivityAvailable = false;

        super.onDestroy();
    }

    protected void pushActivity(Class<?> child, APIData... data) {

        Log.d(LOG_TAG, "pushActivity : ");

        if (child == null)
            return;
        Log.d(LOG_TAG, "pushAcivity : child = " + child.getSimpleName());
        Intent intent = new Intent();
        intent.setClass(this, child);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (data != null) {
            intent.putExtra("data", data);
        }

        startActivityForResult(intent, 0);

    }

    /*protected void gotoHome() {

        String cls = getClass().getName();

        Log.d(LOG_TAG, "gotoHome : " + cls);

        if (!cls.endsWith("Home")) {
            setResult(NAVIGATE_TO_HOME);
            finish();
        } else {
            onReAppear();
        }
    }*/

    protected void gotoHome() {

        String cls = getClass().getName();

        Log.d(LOG_TAG, "gotoHome : " + cls);

        if (!cls.endsWith("Home")) {

            if (cls.endsWith("VoidReceiptActivity")) {
                finish();
            } else {
                setResult(NAVIGATE_TO_HOME);
                finish();
            }
        } else {
            onReAppear();
        }
    }

    protected void gotoSale() {
        Log.d(LOG_TAG, "gotoSale : ");

        String cls = getClass().getName();

        if (!cls.endsWith("SalePad")) {
            setResult(NAVIGATE_TO_SALE);
            finish();
        } else {
            onReAppear();
        }
    }

    protected void logOut() {
        Log.d(LOG_TAG, "logOut : ");

        String cls = getClass().getName();

        if (cls.endsWith("Login")) {
            return;
        }

        Merchant amexUser = Config.getAmexUser(getApplicationContext());
        Merchant visaUser = Config.getUser(getApplicationContext());

        //check if both users are logged out
        if(amexUser.getAuth1() == 0 && visaUser.getAuth1() == 0){
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);

            Intent intent = new Intent(getApplicationContext(), Login.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            startActivity(mainIntent);
            return;
        }

        if (!cls.endsWith("Home")) {
            setResult(NAVIGATE_TO_LOGOUT);
            finish();
        } else {
            pushActivity(Login.class);
            //if either logged amex or visa no need to finish (2017-08-30)
            if (!m_client.isLoggedIn() || !m_client_amex.isLoggedIn()) {
                finish();
            }
        }
    }

    protected void closeResendScreen() {
        SalesReceiptResendActivity.isResendClose = true;
        finish();
    }

    protected void goSalesResume() {
        SignaturePad.isClose = true;
        SalePad_Keyentry.isClose = true;
        SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
        DsSalesAudio.isClose = true;
        finish();
    }

    protected void closeReceiptScreen() {

        String cls = getClass().getName();

        Log.i(LOG_TAG, "inside closeReceiptScreen ");
        Log.i(LOG_TAG, "closeReceiptScreen : cls = " + cls);

        //cls.endsWith("SalesReceiptResendActivity")
        if (cls.endsWith("SendTextActivity") || cls.endsWith("SendEmailActivity") || cls.endsWith("SalesReceiptActivity")) {
            //setResult(CLOSE_ERECEIPT);
            setResult(NAVIGATE_TO_HOME);
            finish();
            return;
        }

        /*if () {
            //setResult(CLOSE_ERECEIPT);
            setResult(NAVIGATE_TO_HOME);
            finish();
            return;
        }

        if () {
            //setResult(CLOSE_ERECEIPT);
            setResult(NAVIGATE_TO_HOME);
            finish();
            return;
        }*/

        if (cls.endsWith("VoidReceiptActivity")) {
            setResult(CLOSE_ERECEIPT);
            finish();
            return;
        }

        if (cls.endsWith("SelectPaymentMethod")) {
            setResult(CLOSE_ERECEIPT);
            finish();
            return;
        }

        if (cls.endsWith("SignaturePad")) {
            setResult(CLOSE_ERECEIPT);
            finish();
            return;
        }

        if (cls.endsWith("DsSalesAudio")) {
            setResult(CLOSE_ERECEIPT);
            finish();
            return;
        }

        if (cls.endsWith("Approval")) {
            setResult(CLOSE_ERECEIPT);
            finish();
            return;
        }
    }

    protected void fireOpenTxList() {
        String cls = getClass().getName();

        Log.d(LOG_TAG, "fireOpenTxList : cls = " + cls);

        if (!cls.endsWith("Home")) {
            setResult(NAVIGATE_TO_OPENTX);
            finish();
        } else {
            pushActivity(OpenTransations.class);
            // finish() ;
        }

        // pushActivity(OpenTransations.class);
        // finish() ;

    }

    protected void onReAppear() {
        Log.d(LOG_TAG, "onReAppear : ");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(LOG_TAG, "onActivityResult : requestCode = " + requestCode + " resultCode" + resultCode);

        if (resultCode == NAVIGATE_TO_SALE) {
            gotoSale();
            return;
        }

        if (resultCode == NAVIGATE_TO_HOME) {
            gotoHome();
            return;
        }

        if (resultCode == NAVIGATE_TO_LOGOUT) {
            logOut();
            return;
        }

        if (resultCode == NAVIGATE_TO_OPENTX) {
            fireOpenTxList();
            return;
        }

        if (resultCode == CLOSE_ERECEIPT) {
            closeReceiptScreen();
            return;
        }

        if (Activity.RESULT_OK == resultCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (extras.containsKey("data")) {
                    Parcelable[] resSet = extras.getParcelableArray("data");

                    if (resSet != null && resSet.length > 0) {
                        APIData[] dataSet = new APIData[resSet.length];

                        for (int i = 0; i < resSet.length; i++) {
                            dataSet[i] = (APIData) resSet[i];
                        }

                        onResultFromChild(dataSet);

                    }
                }
            }
        }

    }

    protected void onResultFromChild(APIData... val) {
        Log.d(LOG_TAG, "onResultFromChild : ");
    }

    protected void finishWithResult(APIData... val) {
        Log.d(LOG_TAG, "finishWithResult : ");

        if (val != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data", val);
            setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
    }

    protected void showProgressDialog(String message) {

        Log.d(LOG_TAG, "showProgressDialog : ");

		/*
         * if (progress != null && progress.isShowing()) { progress.dismiss(); }
		 * 
		 * progress = ProgressDialog.show(this, "", message, true);
		 */

        if (progressV2 != null && progressV2.isShowing()) {
            progressV2.dismiss();
        }

        progressV2 = new Dialog(this, R.style.TransparentProgressDialog);
        progressV2.setContentView(R.layout.progress_loader);

        TextView txtLoading = (TextView) progressV2.findViewById(R.id.txtLoading);
        if (lang_status == LangPrefs.LAN_EN) {
            txtLoading.setTypeface(TypeFaceUtils.setRobotoMedium(this));
        } else if (lang_status == LangPrefs.LAN_TA) {
            message = "jaT nra;J fhj;jpUq;fs;";
            txtLoading.setTypeface(TypeFaceUtils.setBaminiFont(this));
        } else if (lang_status == LangPrefs.LAN_SIN) {
            try {
                message = new String("lreKdlr /§ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            txtLoading.setTypeface(TypeFaceUtils.setSinhalaFont(this));
        }

        txtLoading.setText(message);

        // dialog.findViewById(R.id.loading_icon).startAnimation(AnimationUtils.loadAnimation(SignInActivity.this,
        // R.anim.rotate360));
        ((AnimatedGifImageView) progressV2.findViewById(R.id.loading_icon))
                .setAnimatedGif(R.raw.loader_main_normal_white_one, AnimatedGifImageView.TYPE.AS_IS);

        progressV2.setCancelable(false);
        progressV2.show();
    }


    protected void dismissProgressDialog() {

        Log.d(LOG_TAG, "dismissProgressDialog : ");

		/*
         * if (progress != null && progress.isShowing()) { progress.dismiss(); }
		 */

        if (progressV2 != null && progressV2.isShowing()) {
            progressV2.dismiss();
        }
    }

    protected void showToastMessage(String msg) {

        Log.d(LOG_TAG, "showToastMessage : " + msg);

        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }

    protected void showToastMessage_local(String msg, String page, int status) {
        Log.d(LOG_TAG, "showToastMessage_local : " + msg + " " + page + " " + status);
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);

        if (status == LangPrefs.LAN_EN) {

        } else if (status == LangPrefs.LAN_TA) {
            // sinlang_select.Apply_SignIn_Sinhala(txtSignIn, edtEmail,
            // edtPassword, btnSignIn);
            if (page.equals("changelang")) {
                tamlang_select.Apply_ShowLanguage(messageTextView);
            } else if (page.equals("changelang_val")) {

            }
        } else if (status == LangPrefs.LAN_SIN) {
            if (page.equals("changelang")) {
                sinlang_select.Apply_ShowLanguage(messageTextView);
            } else if (page.equals("changelang_val")) {

            }
        } else {
            // do nothing
        }

        messageTextView.setTextSize(14.0f);
        toast.show();
    }

    protected void showInfo(String title, String msg) {

        Log.d(LOG_TAG, "showInfo : title = " + title + ", msg = " + msg);

        storeMessageLog(title + " -> " + msg);
        if (!isCallBackSafe()) {
            storeErrorLog("UI callback is not safe");
            return;
        }

        new HDPayableSuccess_dlg(title, msg).show(getSupportFragmentManager(), title);

    }

    protected void showInfo(String title, String msg, int callerId) {
        Log.d(LOG_TAG, "showInfo : title = " + title + ", msg = " + msg + ", callerId = " + callerId);
        storeMessageLog(title + " -> " + msg);
        if (!isCallBackSafe()) {
            storeErrorLog("UI callback is not safe");
            return;
        }

        new HDPayableSuccess_dlg(title, msg, this, callerId).show(getSupportFragmentManager(), title);
    }

    protected void showDialog(String title, String msg) {

        Log.d(LOG_TAG, "showDialog : title = " + title + ", msg = " + msg);

		/*
         * if(! isVisible){ return ; }
		 */

        storeErrorLog(title + " -> " + msg);
        if (!isCallBackSafe()) {
            storeErrorLog("UI callback is not safe");
            return;
        }

        new HDPayableFailure_dlg(title, msg).show(getSupportFragmentManager(), title);
    }

    protected void showDialog(String title, String msg, int callerId) {

        Log.d(LOG_TAG, "showDialog : title = " + title + ", msg = " + msg + ", callerId = " + callerId);

		/*
         * if(! isVisible){ return ; }
		 */

        storeErrorLog(title + " -> " + msg);

        if (!isCallBackSafe()) {
            storeErrorLog("UI callback is not safe");
            return;
        }

        new HDPayableFailure_dlg(title, msg, this, callerId).show(getSupportFragmentManager(), title);

        // HDErrorMsgFragment h;
    }

    protected void storeMessageLog(String message) {
        Log.d(LOG_TAG, "storeMessageLog : ");
        ErrorLogBean info = new ErrorLogBean(getClass().getName(), message, ErrorLogBean.LEVEL_INFO, Crypto.generateIntToken());
        logDb.addErrorItem(info);
    }

    protected void storeErrorLog(String message) {
        Log.d(LOG_TAG, "storeErrorLog : ");
        ErrorLogBean error = new ErrorLogBean(getClass().getName(), message, ErrorLogBean.LEVEL_ERROR, Crypto.generateIntToken());
        logDb.addErrorItem(error);
    }

    public boolean isCallBackSafe() {
        Log.d(LOG_TAG, "isCallBackSafe : " + isActivityAvailable);
        return isActivityAvailable;
    }

    public void onCallProgress(int callerId, String message) {
        if (callerId != -100) {
            showProgressDialog(message);
        }
    }

    public void onCallEnd(int callerId) {
        Log.d(LOG_TAG, "onCallEnd : callerId = " + callerId);
        dismissProgressDialog();
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {
        // Log.i(TAG, "Received exception in generic activity");
        // Log.i(TAG, "Exception is:- " + e.toString());

        Log.d(LOG_TAG, "onCallError : EnumApi = " + api + ", callerId = " + callerId + "ApiException = " + e.toString());

        printOnCallErrorLog(api, e);

        if (e.getErrcode() == 10001) {
            showDialog("Error", "Invalid authentication.please sign in again.", 10001);
            return;
        }

        if (e.getErrcode() == 10009) {
            showDialog("Error", "Your account is blocked.", 10001);
            return;
        }

        if (e.getErrcode() == 10017) {
            showDialog("Error", "Your account is waiting for the verification.", 10001);
            return;
        }

        if (e.getErrcode() == 10018) {
            showDialog("Error", "Your account is waiting for the activation.", 10001);
            return;
        }

        if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION || e.getErrcode() == ApiException.TIMEOUT_ERROR) {
            showDialog("Error", "Couldn't connect with transaction server.Please check your internet connectivity.");
            return;
        }

        showDialog("Error", e.toString());
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        Log.d(LOG_TAG, "onCallSuccess : EnumApi = " + api + ", callerId = " + callerId);
    }

    protected void printOnCallErrorLog(EnumApi api, ApiException e) {
        Log.d(LOG_TAG, "printOnCallErrorLog : EnumApi = " + api + ", ApiException = " + e.toString());
        PLog.e("JEYLOGS", "Endpoint:" + api.toString() + "  " + e.toString() + "  error code:" + e.getErrcode());
    }

    public void onMSGDlgBtnClick(int callerId) {
        Log.d(LOG_TAG, "onMSGDlgBtnClick : callerId = " + callerId);

        if (!LoginSecondary.isAmexLogin) {
            if (callerId == 10001) {
                Merchant m = Config.getUser(getApplicationContext());
                m.setAuth1(0);
                m.setAuth2(0);

                Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
                Config.setUser(getApplicationContext(), m);

                logOut();
                return;
            }
            finish();
        }
    }

    public void logoutAllNow() {
        //VISA LOGOUT
        boolean seylanLoggedOut = false;
        boolean amexLoggedOut = false;
        if (m_client.isLoggedIn()) {
            Merchant m = Config.getUser(getApplicationContext());
            m.setAuth1(0);
            m.setAuth2(0);

            Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
            Config.setUser(getApplicationContext(), m);
            seylanLoggedOut = true;
        }

        //AMEX LOGOUT
        if (m_client_amex.isLoggedIn()) {
            Merchant m = Config.getAmexUser(getApplicationContext());
            m.setAuth1(0);
            m.setAuth2(0);

            Config.setAmexState(getApplicationContext(), Config.STATUS_LOGOUT);
            Config.setAmexUser(getApplicationContext(), m);
            amexLoggedOut = true;
        }

        //if logged out from both accounts, clear all shared preferences
        if(seylanLoggedOut && amexLoggedOut){
            SharedPreferences settings = getApplicationContext().getSharedPreferences("PREFMPOSR1", Context.MODE_PRIVATE);
            settings.edit().clear().commit();
        }

        finish();
    }

    public void onNavBackPressed() {
        Log.d(LOG_TAG, "onNavBackPressed : ");
        onBackPressed();
        finish();
    }

    public void hideSoftKeyboard() {

        Log.d(LOG_TAG, "hideSoftKeyboard : ");

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
