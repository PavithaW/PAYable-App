package com.cba.payable;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;
import com.setting.env.Environment;
import com.setting.env.ProfileInfo;

import java.text.DecimalFormat;

public class MerchantProfile_Activity extends GenericActivity {

    TextView txtTitle;
    TextView lblUserName, lblBusinessName, lblCoporateAddress, lblMerchantId, lblTerminalId, lblAppVersion, lblCardType,
            lblAmount;
    TextView txtUserName, txtBusinessName, txtCoporateAddress, txtMerchantId, txtTerminalId, txtAppVersion, txtCardType,
            txtAmount;

    View lkrSelector, usdSelector, gbpSelector, eurSelector;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    ProfileInfo profileInfo;
    DecimalFormat df;
    private boolean isVisibleTab;
    Merchant m = null;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_new_merchant_profile);

        initViews();
        profileInfo = getProfileInfo();

        df = new DecimalFormat("####0.00");

        if (Home.isAmexCardSelected) {
            m = Config.getProfileAmex(this);
        } else {
            m = Config.getProfile(this);
        }

        txtUserName.setText(m.getUserName());
        /*txtTerminalId.setText(m.getTerminalId());
        txtMerchantId.setText(m.getCardAqId());
        txtAmount.setText(df.format(m.getMax_amount()));*/
        setupInitialViews();
        txtAppVersion.setText(Environment.getVersionInfo());

        if (m.getReaderType() == Merchant.READER_BT_NO_PIN) {
            txtCardType.setText("Bluetooth without pin pad");
        }

        if (m.getReaderType() == Merchant.READER_BT_PIN) {
            txtCardType.setText("Bluetooth pin pad");
        }

        txtBusinessName.setText(m.getBusinessName());
        txtCoporateAddress.setText(m.getAddress());

    }

    private void setupInitialViews() {

        if (Home.isAmexCardSelected) {
            if (Config.isEnableLKRAmex(this)) {
                selectLKR();
            } else if (Config.isEnableUSDAmex(this)) {
                selectUSD();
            } else if (Config.isEnableGBPAmex(this)) {
                selectGBP();
            } else if (Config.isEnableEURAmex(this)) {
                selectEUR();
            }else{
                txtTerminalId.setText(m.getTerminalId());
                txtMerchantId.setText(m.getCardAqId());
                txtAmount.setText(df.format(m.getMax_amount()));
            }
        } else {
            if (Config.isEnableLKR(this)) {
                selectLKR();
            } else if (Config.isEnableUSD(this)) {
                selectUSD();
            } else if (Config.isEnableGBP(this)) {
                selectGBP();
            } else if (Config.isEnableEUR(this)) {
                selectEUR();
            }else{
                txtTerminalId.setText(m.getTerminalId());
                txtMerchantId.setText(m.getCardAqId());
                txtAmount.setText(df.format(m.getMax_amount()));
            }
        }
    }


    private ProfileInfo getProfileInfo() {

        String profilesJson = null;

        if (Home.isAmexCardSelected) {
            profilesJson = Config.getUserProfilesAmex(this);
        } else {
            profilesJson = Config.getUserProfiles(this);
        }

        return ProfileInfo.getInstance(profilesJson);
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    private void initViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);
        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);
        lang_status = LangPrefs.getLanguage(getApplicationContext());

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        lblUserName = (TextView) findViewById(R.id.lblUserName);
        lblUserName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblBusinessName = (TextView) findViewById(R.id.lblBusinessName);
        lblBusinessName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblCoporateAddress = (TextView) findViewById(R.id.lblCoporateAddress);
        lblCoporateAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblMerchantId = (TextView) findViewById(R.id.lblMerchantId);
        lblMerchantId.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblTerminalId = (TextView) findViewById(R.id.lblTerminalId);
        lblTerminalId.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblAppVersion = (TextView) findViewById(R.id.lblAppVersion);
        lblAppVersion.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblCardType = (TextView) findViewById(R.id.lblCardType);
        lblCardType.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblAmount = (TextView) findViewById(R.id.lblAmount);
        lblAmount.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        lblUserName = (TextView) findViewById(R.id.lblUserName);
        lblUserName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtBusinessName = (TextView) findViewById(R.id.txtBusinessName);
        txtBusinessName.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtCoporateAddress = (TextView) findViewById(R.id.txtCoporateAddress);
        txtCoporateAddress.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtMerchantId = (TextView) findViewById(R.id.txtMerchantId);
        txtMerchantId.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtTerminalId = (TextView) findViewById(R.id.txtTerminalId);
        txtTerminalId.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAppVersion = (TextView) findViewById(R.id.txtAppVersion);
        txtAppVersion.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtCardType = (TextView) findViewById(R.id.txtCardType);
        txtCardType.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        if (lang_status == LangPrefs.LAN_EN) {
            /*if (Home.isAmexCardSelected) {
                txtTitle.setText("Merchant Profile AMEX");
            } else {
                txtTitle.setText("Merchant Profile VISA/MATER");
            }*/

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_MerchantProfile(txtTitle, lblUserName, lblBusinessName, lblCoporateAddress,
                    lblMerchantId, lblTerminalId, lblAppVersion, lblCardType, lblAmount);

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_MerchantProfile(txtTitle, lblUserName, lblBusinessName, lblCoporateAddress,
                    lblMerchantId, lblTerminalId, lblAppVersion, lblCardType, lblAmount);

        } else {
            // do nothing
        }

        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);


        if (Home.isAmexCardSelected) {
            isVisibleTab = Config.getVersionIdAmex(this) == -1;
        } else {
            isVisibleTab = Config.getVersionId(this) == -1;
        }


        if (isVisibleTab) {
            LinearLayout tabImgs = (LinearLayout) findViewById(R.id.tab_imgs);
            LinearLayout tabTxts = (LinearLayout) findViewById(R.id.tab_txts);
            LinearLayout tabViews = (LinearLayout) findViewById(R.id.tab_views);

            tabImgs.setVisibility(View.GONE);
            tabTxts.setVisibility(View.GONE);
            tabViews.setVisibility(View.GONE);
        } else {

            ImageView lkrImg = (ImageView) findViewById(R.id.image_lkr);
            ImageView usdImg = (ImageView) findViewById(R.id.image_usd);
            ImageView gbpImg = (ImageView) findViewById(R.id.image_gbp);
            ImageView eurImg = (ImageView) findViewById(R.id.image_eur);

            TextView lkrTxt = (TextView) findViewById(R.id.textview_lkr);
            TextView usdTxt = (TextView) findViewById(R.id.textview_usd);
            TextView gbpTxt = (TextView) findViewById(R.id.textview_gbp);
            TextView eurTxt = (TextView) findViewById(R.id.textview_eur);

            lkrImg.setVisibility(View.GONE);
            usdImg.setVisibility(View.GONE);
            gbpImg.setVisibility(View.GONE);
            eurImg.setVisibility(View.GONE);

            lkrTxt.setVisibility(View.GONE);
            usdTxt.setVisibility(View.GONE);
            gbpTxt.setVisibility(View.GONE);
            eurTxt.setVisibility(View.GONE);

            lkrSelector.setVisibility(View.GONE);
            usdSelector.setVisibility(View.GONE);
            gbpSelector.setVisibility(View.GONE);
            eurSelector.setVisibility(View.GONE);


            if (Home.isAmexCardSelected) {

                if (Config.isEnableLKRAmex(this)) {
                    lkrImg.setVisibility(View.VISIBLE);
                    lkrTxt.setVisibility(View.VISIBLE);
                    lkrSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableUSDAmex(this)) {
                    usdImg.setVisibility(View.VISIBLE);
                    usdTxt.setVisibility(View.VISIBLE);
                    usdSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableGBPAmex(this)) {
                    gbpImg.setVisibility(View.VISIBLE);
                    gbpTxt.setVisibility(View.VISIBLE);
                    gbpSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableEURAmex(this)) {
                    eurImg.setVisibility(View.VISIBLE);
                    eurTxt.setVisibility(View.VISIBLE);
                    eurSelector.setVisibility(View.VISIBLE);
                }

                //SET SELECTED TAB
                if (Config.isEnableLKRAmex(this)) {
                    lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableUSDAmex(this)) {
                    usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableGBPAmex(this)) {
                    gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableEURAmex(this)) {
                    eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                }
            } else {
                if (Config.isEnableLKR(this)) {
                    lkrImg.setVisibility(View.VISIBLE);
                    lkrTxt.setVisibility(View.VISIBLE);
                    lkrSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableUSD(this)) {
                    usdImg.setVisibility(View.VISIBLE);
                    usdTxt.setVisibility(View.VISIBLE);
                    usdSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableGBP(this)) {
                    gbpImg.setVisibility(View.VISIBLE);
                    gbpTxt.setVisibility(View.VISIBLE);
                    gbpSelector.setVisibility(View.VISIBLE);
                }

                if (Config.isEnableEUR(this)) {
                    eurImg.setVisibility(View.VISIBLE);
                    eurTxt.setVisibility(View.VISIBLE);
                    eurSelector.setVisibility(View.VISIBLE);
                }

                //SET SELECTED TAB
                if (Config.isEnableLKR(this)) {
                    lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableUSD(this)) {
                    usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableGBP(this)) {
                    gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                } else if (Config.isEnableEUR(this)) {
                    eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
                }
            }
        }
    }

    public void onCurrencySelect(View view) {

        int id = view.getId();

        if (id == R.id.image_lkr || id == R.id.textview_lkr) {
            if (profileInfo.isLkrEnabled()) selectLKR();
        }
        if (id == R.id.image_usd || id == R.id.textview_usd) {
            if (profileInfo.isUsdEnabled()) selectUSD();
        }
        if (id == R.id.image_gbp || id == R.id.textview_gbp) {
            if (profileInfo.isGbpEnabled()) selectGBP();
        }
        if (id == R.id.image_eur || id == R.id.textview_eur) {
            if (profileInfo.isEurEnabled()) selectEUR();
        }
    }

    private void selectLKR() {
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        txtMerchantId.setText(profileInfo.getLkrMerchantlId());
        txtTerminalId.setText(profileInfo.getLkrTerminalId());
        txtAmount.setText(df.format(profileInfo.getLkrMaxAmount()));
    }

    private void selectUSD() {
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        txtMerchantId.setText(profileInfo.getUsdMerchantId());
        txtTerminalId.setText(profileInfo.getUsdTerminalId());
        txtAmount.setText(df.format(profileInfo.getUsdMaxAmount()));
    }

    private void selectGBP() {
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        txtMerchantId.setText(profileInfo.getGbpMerchantId());
        txtTerminalId.setText(profileInfo.getGbpTerminalId());
        txtAmount.setText(df.format(profileInfo.getGbpMaxAmount()));
    }

    private void selectEUR() {
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        txtMerchantId.setText(profileInfo.getEurMerchantId());
        txtTerminalId.setText(profileInfo.getEurTerminalId());
        txtAmount.setText(df.format(profileInfo.getEurMaxAmount()));
    }
}
