package com.cba.payable;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.BatchlistRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SettlementHistoryDetails_Activity extends GenericActivity {

    TextView txtTitle, txtSales, txtSalesAmount, txtSalesCount, txtVisaAmount, txtVisaCount, txtMasterAmount,
            txtMasterCount;
    TextView txtVoids, txtVoidsAmount, txtVoidsCount, txtVoidVisaAmount, txtVisaVoidCount, txtMasterVoidAmount,
            txtMasterVoidCount;
    ImageView salseVisaCard1, salseVisaCard2, salseMasterCard1, salseMasterCard2;
    ImageView voidVisaCard1, voidVisaCard2, voidMasterCard1, voidMasterCard2;
    Button btnViewTransactions;

    private BatchlistRes m_BatchlistRes;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    DecimalFormat df;
    private int selectedCurrencyType;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_settlement_history_details);

        InitViews();

        //SimpleDateFormat sdf_date = new SimpleDateFormat("MMMM dd, yyyy kk:mm a", Locale.US);
        SimpleDateFormat sdf_date = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof BatchlistRes) {
                    m_BatchlistRes = (BatchlistRes) data[i];
                }
            }
        }

        if (m_BatchlistRes != null) {

            selectedCurrencyType = m_BatchlistRes.getCurrencyType();

            txtTitle.setText(sdf_date.format(m_BatchlistRes.getServerTime()));

            // Sales Values //

            txtSalesAmount.setText(df.format(m_BatchlistRes.getSalesTotal()));

            txtVisaAmount.setText(df.format(m_BatchlistRes.getVisaSalesTotal()));
            txtVisaCount.setText(String.valueOf(m_BatchlistRes.getVisaNoOfSale()));
            txtMasterAmount.setText(df.format(m_BatchlistRes.getMasterTotal()));
            txtMasterCount.setText(String.valueOf(m_BatchlistRes.getMasterNoOfSale()));

            txtVoidsAmount.setText(df.format(m_BatchlistRes.getVoidTotal()));

            txtVoidVisaAmount.setText(df.format(m_BatchlistRes.getVisaVoidTotal()));
            txtVisaVoidCount.setText(String.valueOf(m_BatchlistRes.getVisaNoOfVoid()));
            txtMasterVoidAmount.setText(df.format(m_BatchlistRes.getMasterVoidTotal()));
            txtMasterVoidCount.setText(String.valueOf(m_BatchlistRes.getMasterNoOfVoid()));

            if (lang_status == LangPrefs.LAN_EN) {

                String currencyType = UtilityFunction.getCurrencyTypeString(selectedCurrencyType);

                txtSales.setText("SALES - " + currencyType);
                txtVoids.setText("VOIDS - " + currencyType);
                txtSalesCount.setText(String.valueOf(m_BatchlistRes.getNoOfSale()) + " SALES");
                txtVoidsCount.setText(String.valueOf(m_BatchlistRes.getNoOfVoid()) + " VOIDS");

            } else if (lang_status == LangPrefs.LAN_TA) {

                String currencyType = UtilityFunction.getCurrencyTypeStringTA(selectedCurrencyType);

                tamlang_select.Apply_SettlementHistoryDetails(String.valueOf(m_BatchlistRes.getNoOfSale()),
                        String.valueOf(m_BatchlistRes.getNoOfVoid()), txtSalesCount, txtVoidsCount, btnViewTransactions,
                        txtSales, txtVoids, currencyType);

            } else if (lang_status == LangPrefs.LAN_SIN) {

                String currencyType = UtilityFunction.getCurrencyTypeStringSI(selectedCurrencyType);

                sinlang_select.Apply_SettlementHistoryDetails(String.valueOf(m_BatchlistRes.getNoOfSale()),
                        String.valueOf(m_BatchlistRes.getNoOfVoid()), txtSalesCount, txtVoidsCount, btnViewTransactions,
                        txtSales, txtVoids, currencyType);

            } else {
                // do nothing
                txtSalesCount.setText(String.format("%s SALES", String.valueOf(m_BatchlistRes.getNoOfSale())));
                txtVoidsCount.setText(String.format("%s VOIDS", String.valueOf(m_BatchlistRes.getNoOfVoid())));
            }

            //change card type icons
            if (BatchHistory.isVMActivated) {
                salseVisaCard1.setImageResource(R.drawable.visacard_icon);
                salseVisaCard2.setImageResource(R.drawable.visacard_icon);
                salseMasterCard1.setImageResource(R.drawable.mastercard_icon);
                salseMasterCard2.setImageResource(R.drawable.mastercard_icon);

                voidVisaCard1.setImageResource(R.drawable.visacard_icon);
                voidVisaCard2.setImageResource(R.drawable.visacard_icon);
                voidMasterCard1.setImageResource(R.drawable.mastercard_icon);
                voidMasterCard2.setImageResource(R.drawable.mastercard_icon);
            } else {
                salseVisaCard1.setImageResource(R.drawable.amex);
                salseVisaCard2.setImageResource(R.drawable.amex);
                salseMasterCard1.setImageResource(R.drawable.diners_club);
                salseMasterCard2.setImageResource(R.drawable.diners_club);

                voidVisaCard1.setImageResource(R.drawable.amex);
                voidVisaCard2.setImageResource(R.drawable.amex);
                voidMasterCard1.setImageResource(R.drawable.diners_club);
                voidMasterCard2.setImageResource(R.drawable.diners_club);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //IF PORTAL CHANGED
        if (Home.isPortalChanged) {
            finish();
        }
    }

    public void onViewTx(View v) {
        if (m_BatchlistRes != null) {
            //this.pushActivity(BatchTransactions.class, m_BatchlistRes);
            this.pushActivity(ViewTransactionsActivity.class, m_BatchlistRes);
        }
    }

    public void onNavBack(View v) {

        onNavBackPressed();
    }

    private void InitViews() {
        df = new DecimalFormat("####0.00");

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtSales = (TextView) findViewById(R.id.txtSales);
        txtSales.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtSalesAmount = (TextView) findViewById(R.id.txtSalesAmount);
        txtSalesAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtSalesCount = (TextView) findViewById(R.id.txtSalesCount);
        txtSalesCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVisaAmount = (TextView) findViewById(R.id.txtVisaAmount);
        txtVisaAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVisaCount = (TextView) findViewById(R.id.txtVisaCount);
        txtVisaCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtMasterAmount = (TextView) findViewById(R.id.txtMasterAmount);
        txtMasterAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtMasterCount = (TextView) findViewById(R.id.txtMasterCount);
        txtMasterCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVoids = (TextView) findViewById(R.id.txtVoids);
        txtVoids.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtVoidsAmount = (TextView) findViewById(R.id.txtVoidsAmount);
        txtVoidsAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVoidsCount = (TextView) findViewById(R.id.txtVoidsCount);
        txtVoidsCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVoidVisaAmount = (TextView) findViewById(R.id.txtVoidVisaAmount);
        txtVoidVisaAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVisaVoidCount = (TextView) findViewById(R.id.txtVisaVoidCount);
        txtVisaVoidCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtMasterVoidAmount = (TextView) findViewById(R.id.txtMasterVoidAmount);
        txtMasterVoidAmount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtMasterVoidCount = (TextView) findViewById(R.id.txtMasterVoidCount);
        txtMasterVoidCount.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        btnViewTransactions = (Button) findViewById(R.id.btnViewTransactions);
        btnViewTransactions.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));


        salseVisaCard1 = (ImageView) findViewById(R.id.imgVisaCardAmount);
        salseVisaCard2 = (ImageView) findViewById(R.id.imgVisaCount);
        salseMasterCard1 = (ImageView) findViewById(R.id.imgMaster_Amount);
        salseMasterCard2 = (ImageView) findViewById(R.id.imgMaster_Count);

        voidVisaCard1 = (ImageView) findViewById(R.id.imgVisaCardVoidAmount);
        voidVisaCard2 = (ImageView) findViewById(R.id.imgVisaVoidCount);
        voidMasterCard1 = (ImageView) findViewById(R.id.imgMasterVoidAmount);
        voidMasterCard2 = (ImageView) findViewById(R.id.imgMasterVoidCount);

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());
    }
}
