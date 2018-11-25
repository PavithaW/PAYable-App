package com.cba.payable;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.pojo.APIData;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Consts;

public class Settlement2 extends GenericActivity {

    TextView txtTitle, txtVisaTotalCount, txtMasterTotalCount, txtVisaAmount,
            txtMasterAmount, txtVisaCurrencyType, txtMasterCurrencyType, txtTotalSettlements,
            txtTotalAmount, txtCurrencyType;
    View lkrSelector, usdSelector, gbpSelector, eurSelector;

    private int mCurrencyType = Consts.ALL_Currency;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_settlement_2);
        initViews();
    }

    private void initViews() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        txtVisaTotalCount = (TextView) findViewById(R.id.txtVisaTotalCount);
        txtMasterTotalCount = (TextView) findViewById(R.id.txtMasterTotalCount);

        txtVisaAmount = (TextView) findViewById(R.id.txtVisaAmount);
        txtMasterAmount = (TextView) findViewById(R.id.txtMasterAmount);

        txtVisaCurrencyType = (TextView) findViewById(R.id.txtVisaCurrencyType);
        txtMasterCurrencyType = (TextView) findViewById(R.id.txtMasterCurrencyType);

        txtTotalSettlements = (TextView) findViewById(R.id.txtTotalSettlements);

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtCurrencyType = (TextView) findViewById(R.id.txtCurrencyType);

    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    public void onSettle(View v) {
        Toast.makeText(this, "on settle", Toast.LENGTH_LONG).show();
    }

    public void onCurrencySelect(View view) {
        int id = view.getId();

        if (id == R.id.image_lkr || id == R.id.textview_lkr) {
            selectLKR();
        }
        if (id == R.id.image_usd || id == R.id.textview_usd) {
            selectUSD();
        }
        if (id == R.id.image_gbp || id == R.id.textview_gbp) {
            selectGBP();
        }
        if (id == R.id.image_eur || id == R.id.textview_eur) {
            selectEUR();
        }
    }

    private void selectLKR() {
        mCurrencyType = Consts.LKR;
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        Toast.makeText(this,"LKR",Toast.LENGTH_LONG).show();
    }

    private void selectUSD() {
        mCurrencyType = Consts.USD;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        Toast.makeText(this,"USD",Toast.LENGTH_LONG).show();
    }

    private void selectGBP() {
        mCurrencyType = Consts.GBP;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        Toast.makeText(this,"GBP",Toast.LENGTH_LONG).show();
    }

    private void selectEUR() {
        mCurrencyType = Consts.EUR;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        Toast.makeText(this,"EUR",Toast.LENGTH_LONG).show();
    }
}
