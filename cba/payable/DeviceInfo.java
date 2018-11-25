package com.cba.payable;

import android.app.Dialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.DeviceInforReq;
import com.mpos.pojo.SimpleAck;
import com.mpos.util.LangPrefs;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class DeviceInfo extends GenericActivity {

    private String strDeviceId = null, strSimId = null;

    private Dialog dialog;

    TextView txtTitle, lblIMEI, lblSIM, txtSend;
    EditText txtIMEI, txtSimId, edttag, edtBankCode;
    String strBankCode;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_deviceinfo);
        setContentView(R.layout.activity_deviceinfo);
        InitViews();

        _loadDetails();

        // {"txcount":12 , "txvalue":200 , "users":20 , "ts":123131231221}
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void _loadDetails() {

        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        strDeviceId = mTelephonyMgr.getDeviceId();
        strSimId = mTelephonyMgr.getSimSerialNumber();

        txtIMEI.setText(strDeviceId);
        txtSimId.setText(strSimId);

    }

    public void onSend(View v) {

        strBankCode = edtBankCode.getText().toString().trim().toLowerCase();
        String strTag = edttag.getText().toString();

        if (strBankCode == null) {
            showDialog("Error", "Please enter your bank code.");
            return;
        } else if (strBankCode.length() == 0) {
            showDialog("Error", "Please enter your bank code.");
            return;
        } else if (strTag.length() == 0) {
            showDialog("Error", "Please enter tag");
            return;
        } else if (!strBankCode.equalsIgnoreCase("commercial")
                && !strBankCode.equalsIgnoreCase("seylan")
                && !strBankCode.equalsIgnoreCase("ntb")
                && !strBankCode.equalsIgnoreCase("cargills")
                && !strBankCode.equalsIgnoreCase("boc")) {
            showDialog("Error", "Please Enter valid bank code.");
            return;
        } else {
            Config.setBankCode(getApplicationContext(), strBankCode);
            dismissDialog();
            dialog = new Dialog(DeviceInfo.this);
            dialog.setContentView(R.layout.dlg_passcode);
            //dialog.setTitle("Enter the code:");

            dialog.findViewById(R.id.btnPassCode).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String str = ((EditText) (dialog.findViewById(R.id.txtCode))).getText().toString();
                    dismissDialog();
                    _submitData(str);
                }
            });
            dialog.show();
        }


    }

    private void _submitData(String code) {
        String progressMessage = "";

        if (lang_status == LangPrefs.LAN_EN) {
            progressMessage = "Sending device information.";
        } else if (lang_status == LangPrefs.LAN_SIN) {
            progressMessage = "f;dr;=re hj�ka";
        } else if (lang_status == LangPrefs.LAN_TA) {
            progressMessage = "fUtpapd; jfty;fs; mDg;gg;gLfpwJ";
        }

        hideSoftKeyboard();

        if (!code.startsWith("*#94")) {
            showDialog("Error", "Invalid passcode");
            return;
        }

        String strTag = edttag.getText().toString();

        DeviceInforReq req = new DeviceInforReq();
        req.setStrIMEI(strDeviceId);
        req.setStrSimSerial(strSimId + "   tag: " + strTag);
        req.setStrToken(code);

        if(strBankCode.equalsIgnoreCase("ntb")){
            m_client_amex.sendDeviceInfo(1111, progressMessage, req);
        }else{
            m_client.sendDeviceInfo(1111, progressMessage, req);
        }

    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        if (callerId == 1111) {
            if (data != null) {
                if (data instanceof SimpleAck) {
                    SimpleAck res = (SimpleAck) data;
                    if (res.getStatus() == 1) {
                        showInfo("Device info", "Device details was sent successfully");
                        return;
                    }
                    if (res.getStatus() == -1) {
                        showDialog("Device info", "Error with email.");
                        return;
                    }

                    if (res.getStatus() == -2) {
                        showDialog("Device info", "Invalid passcode.");
                        return;
                    }

                }
            }

        }
    }

    private void InitViews() {

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        lblIMEI = (TextView) findViewById(R.id.lblIMEI);
        lblIMEI.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        lblSIM = (TextView) findViewById(R.id.lblSIM);
        lblSIM.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        txtSend = (TextView) findViewById(R.id.txtSend);
        txtSend.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        txtIMEI = (EditText) findViewById(R.id.txtIMEI);
        txtIMEI.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtSimId = (EditText) findViewById(R.id.txtSimId);
        txtSimId.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        edttag = (EditText) findViewById(R.id.edttag);
        edttag.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        edtBankCode = (EditText) findViewById(R.id.edtBankCode);
        edtBankCode.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        edtBankCode.requestFocus();

    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            // Log.d("Activity", "Touch event " + event.getRawX() + "," +
            // event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() +
            // "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + "
            // coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

}
