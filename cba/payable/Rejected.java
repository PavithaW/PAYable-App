package com.cba.payable;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.UtilityFunction;
import com.setting.env.Consts;

import java.text.DecimalFormat;

public class Rejected extends GenericActivity {

    public static final String AMMOUNT = "AMMOUNT";
    public static final String ERROR_CODE = "ERROR_CODE";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String CURRENCY_TYPE = "currency";

    private TextView txtTitle;
    private TextView txtAmount;
    private TextView txtAmountTxt;
    private TextView txtErorrCode;
    private TextView txtMsg;
    private TextView txtCardName;
    private TextView txtErrorCodetxt;

    private Button btnProceed;

    private String mErrorMessage;
    private int mErrorCode;
    private double mAmmount;
    private int currencyType;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.rejected);

        Intent intent = getIntent();
        mAmmount = intent.getDoubleExtra(AMMOUNT, 00.00);
        mErrorMessage = intent.getStringExtra(ERROR_MESSAGE);
        mErrorCode = intent.getIntExtra(ERROR_CODE, 000);
        currencyType = intent.getIntExtra(CURRENCY_TYPE, Consts.LKR);

        InitViews();

        updateViews();

    }

    private void updateViews() {

        DecimalFormat df = new DecimalFormat("0.00");
        txtAmount.setText(df.format(mAmmount) + " " + UtilityFunction.getCurrencyTypeString(currencyType));
        txtErorrCode.setText("" + mErrorCode);
        txtMsg.setText(mErrorMessage);
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);
        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtAmount = (TextView) findViewById(R.id.txtAmountValue);
        txtAmountTxt = (TextView) findViewById(R.id.txtAmount);
        txtErorrCode = (TextView) findViewById(R.id.txtApprovalCodeValueTx);
        txtMsg = (TextView) findViewById(R.id.txtApprovalCodeValue);
        txtCardName = (TextView) findViewById(R.id.txtCardName);
        txtErrorCodetxt = (TextView) findViewById(R.id.txtApprovalCode);

        btnProceed = (Button) findViewById(R.id.btnContinue);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Rejected_Tamil(txtTitle, txtAmountTxt, txtCardName, txtErrorCodetxt, btnProceed);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Rejected_Sinhala(txtTitle, txtAmountTxt, txtCardName, txtErrorCodetxt, btnProceed);
        } else {
            // do nothing
        }
    }

    public void onProceed(View v) {
        SalePad.isNewSale = true;
        SalePad_Keyentry.isNewSale = true;
        OrderTrackingActivity.isNewSale = true;
        OrderTrackingActivity.isClose = true;
        SalePad.signal_val = SalePad.SIGNAL_CLOSE;
        SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
        SalePad.isNewSale = true;
        SalePad_Keyentry.isNewSale = true;

        SalePad.signal_val = SalePad.SIGNAL_CLOSE;
        SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;

        if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
            pushActivity(OrderTrackingActivity.class);
        } else {
            pushActivity(SalePad_Keyentry.class);
        }
        finish();
    }

    @Override
    public void onBackPressed() {

    }

}
