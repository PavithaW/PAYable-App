package com.cba.payable;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.TxSaleRes;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.UtilityFunction;

import java.text.DecimalFormat;

//
public class Approval extends GenericActivity {

    private TextView txApproved;
    private TextView txtAmount;
    private TextView txtTxCompleted;
    private TextView txtSigNotRqrd;
    private TextView txtCardEndingTxt;
    private TextView txtCardEnding;
    private TextView txtAuthCodeTxt;
    private TextView txtAuthCode;
    private Button btnProceed;
    private ImageView imgCard;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    TxSaleRes req;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_approval);

        InitViews();

        if (null != data) {
            for (APIData aData : data) {
                if (aData instanceof TxSaleRes) {
                    req = (TxSaleRes) aData;
                    updateViews(req);
                }
            }
        }
    }

    private void updateViews(TxSaleRes req) {

        DecimalFormat df = new DecimalFormat("0.00");
        txtAmount.setText(df.format(req.getAmount())
                + " "
                + UtilityFunction.getCurrencyTypeString(req.getCurrencyType()));
        txtCardEnding.setText(req.getCcLast4());
        txtAuthCode.setText(req.getApprovalCode());

        if (req.getCardType() == RecSale.CARD_VISA) {
            imgCard.setImageResource(R.drawable.visacard_icon);
        } else if (req.getCardType() == RecSale.CARD_MASTER) {
            imgCard.setImageResource(R.drawable.mastercard_icon);
        } else if (req.getCardType() == RecSale.CARD_AMEX) {
            imgCard.setImageResource(R.drawable.amex);
        } else if (req.getCardType() == RecSale.CARD_DINERS) {
            imgCard.setImageResource(R.drawable.diners_club);
        }
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txApproved = (TextView) findViewById(R.id.txtAmount);
        txtAmount = (TextView) findViewById(R.id.txtAmountValue);
        txtTxCompleted = (TextView) findViewById(R.id.txtCardName);
        txtSigNotRqrd = (TextView) findViewById(R.id.txtApprovalCode);
        txtCardEndingTxt = (TextView) findViewById(R.id.cardEndingTXt);
        txtCardEnding = (TextView) findViewById(R.id.txt_card_end_date);
        txtAuthCodeTxt = (TextView) findViewById(R.id.auth_code_txt);
        txtAuthCode = (TextView) findViewById(R.id.txt_auth_code);

        btnProceed = (Button) findViewById(R.id.btnContinue);

        imgCard = (ImageView) findViewById(R.id.img_card);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Approval_Tamil(txtTitle, txApproved, txtTxCompleted, txtSigNotRqrd
                    , txtCardEndingTxt, txtAuthCodeTxt, btnProceed);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Approval_Sinhala(txtTitle, txApproved, txtTxCompleted, txtSigNotRqrd
                    , txtCardEndingTxt, txtAuthCodeTxt, btnProceed);
        }
    }

    public void onProceed(View v) {
//        SalePad.isNewSale = true;
//        SalePad_Keyentry.isNewSale = true;
//        OrderTrackingActivity.isNewSale = true;
//        OrderTrackingActivity.isClose = true;
//        SalePad.signal_val = SalePad.SIGNAL_CLOSE;
//        SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
//
//        if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
//            pushActivity(OrderTrackingActivity.class);
//        } else {
//            pushActivity(SalePad_Keyentry.class);
//        }
// /       finish();

        SalesReceiptActivity.isFromKeyEntry = true;
        req.setIsManual(1);
        this.pushActivity(SalesReceiptActivity.class, req);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}

















