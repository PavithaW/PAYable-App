package com.cba.payable;


import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

public class SalesReceiptActivity extends GenericActivity {

    TextView txtTitle, txtSignatureSave, txtlike;
    Button btnEmail, btnText, btnNoReceipt;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private TxSaleRes m_salesRes;
    RelativeLayout rlSignatureSave;
    public static boolean isFromKeyEntry = false;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_sales_receipt);


        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof TxSaleRes) {
                    m_salesRes = (TxSaleRes) data[i];
                }
            }
        }

        Config.RECEIPT_STATE = Config.RECEIPT_STATE_NORMAL;
        InitViews();
    }

    public void onBackPressed() {

    }

    public void onEmail(View v) {
        if (m_salesRes == null) {
            return;
        }
        this.pushActivity(SendEmailActivity.class, m_salesRes);
    }

    public void onSMS(View v) {
        if (m_salesRes == null) {
            return;
        }

        String currencyType = UtilityFunction.getCurrencyTypeString(m_salesRes.getCurrencyType());

        if (currencyType.equals("LKR") && m_salesRes.getAmount() < 200) {
            showDialog("E-Receipt", "Cannot send sms receipt, if amount is less than " + currencyType + " 200");
            return;
        }

        this.pushActivity(SendTextActivity.class, m_salesRes);
    }


    public void onNewSale(View v) {

        //only visa no track =trackstatus 2  select
        //only visa with track =trackstatus 1  select

        //only amex no track =trackstatus 2  select
        //only amex with track = trackstatus 1  select

        //both no trck = trackstatus 2  selectpayment method
        //both with trck = trackstatus 1  selectpayment method


        if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
            SalePad.isNewSale = true;
            SalePad_Keyentry.isNewSale = true;

            OrderTrackingActivity.isNewSale = true;
            OrderTrackingActivity.isClose = true;

            SalePad.signal_val = SalePad.SIGNAL_CLOSE;
            SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
            SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;

        } else {
            //without track id
            SalePad.isNewSale = true;
            SalePad_Keyentry.isNewSale = true;

            SalePad.signal_val = SalePad.SIGNAL_CLOSE;
            SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
            SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
        }

        // 1 = resend 0 = normal
        if (Config.RECEIPT_STATE == Config.RECEIPT_STATE_NORMAL) {
            closeReceiptScreen();
        } else {
            closeResendScreen();
        }
    }

    private void InitViews() {

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtSignatureSave = (TextView) findViewById(R.id.txtSignatureSave);
        txtSignatureSave.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtlike = (TextView) findViewById(R.id.txtlike);
        txtlike.setTypeface(TypeFaceUtils.setLatoLight(getApplicationContext()));

        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));
        //	btnEmail.setOnClickListener(onClickListener);

        btnText = (Button) findViewById(R.id.btnText);
        btnText.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));
        //btnText.setOnClickListener(onClickListener);

        btnNoReceipt = (Button) findViewById(R.id.btnNoReceipt);
        btnNoReceipt.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));

        rlSignatureSave = (RelativeLayout) findViewById(R.id.rlSignatureSave);
        if(isFromKeyEntry){
            rlSignatureSave.setVisibility(View.GONE);
         }

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Receipt(txtTitle, txtSignatureSave, txtlike, btnEmail, btnText, btnNoReceipt);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Receipt(txtTitle, txtSignatureSave, txtlike, btnEmail, btnText, btnNoReceipt);
        } else {
            // do nothing
        }

    }

    @Override
    protected void onPause() {
        isFromKeyEntry = false;
        super.onPause();
    }
}
