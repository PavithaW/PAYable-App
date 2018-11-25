package com.cba.payable;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.TxSaleRes;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

public class SalesReceiptResendActivity extends GenericActivity {

    TextView txtTitle, txtSignatureSave, txtlike;
    Button btnEmail, btnText, btnNoReceipt;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private RecSale m_recSale;
    private TxSaleRes m_salesRes;
    public static boolean isResendClose = false;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_sales_receipt_resend);

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof RecSale) {
                    m_recSale = (RecSale) data[i];
                }
            }
        }

//        Config.RECEIPT_STATE = Config.RECEIPT_STATE_RESEND;
        m_salesRes = getTxSaleResFromRecSale(m_recSale);
        InitViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isResendClose) {
            finish();
        }

        Config.RECEIPT_STATE = Config.RECEIPT_STATE_RESEND;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isResendClose = false;
    }

    private TxSaleRes getTxSaleResFromRecSale(RecSale recSale) {
        m_salesRes = new TxSaleRes();
        m_salesRes.setTxId(recSale.getTxId());
        m_salesRes.setCardHolder(recSale.getCardHolder());
        m_salesRes.setCcLast4(recSale.getCcLast4());
        m_salesRes.setAmount(recSale.getAmount());
        m_salesRes.setCardType(recSale.getCardType());
        m_salesRes.setCurrencyType(recSale.getCurrencyType()); //added by amal 2017/7/13
//		m_salesRes.setServerTime(recSale.getServerTime());
        m_salesRes.setApprovalCode(recSale.getApprovalCode());
//		m_salesRes.setReceiptType(SaleReceiptm_salesRes.RECEIPT_SALE);
//		m_salesRes.setRrn(recSale.getRrn());
        m_salesRes.setBatchNo(recSale.getBatchNo());
//		m_salesRes.setStn(recSale.getStn());
//		m_salesRes.setAid(recSale.getAid());
//		m_salesRes.setExpDate(recSale.getExpDate());
//		m_salesRes.setAppName(recSale.getAppName());
//		m_salesRes.setEmail(recSale.geEmail());
        return m_salesRes;
    }

    public void onEmail(View v) {
        if (m_recSale == null) {
            return;
        }

        this.pushActivity(SendEmailActivity.class, m_salesRes);
    }

    public void onSMS(View v) {
        if (m_recSale == null) {
            return;
        }


        String currencyType = UtilityFunction.getCurrencyTypeString(m_recSale.getCurrencyType());

        if (currencyType.equals("LKR") && m_recSale.getAmount() < 200) {
            showDialog("E-Receipt", "Cannot send sms receipt, if amount is less than " + currencyType + " 200");
            return;
        }
        /*if (m_recSale.getAmount() < 200) {
            showDialog("E-Receipt", "Cannot send sms receipt, if amount is less than Rs.200");
            return;
        }*/

        this.pushActivity(SendTextActivity.class, m_recSale);
    }

    public void onNewSale(View v) {
        //finish() ;
        closeResendScreen();
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

}
