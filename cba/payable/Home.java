package com.cba.payable;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UserPermisions;
import com.setting.env.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home extends GenericActivity {

    public static boolean isPasswordChanged;
    public static boolean isPortalChanged;
    TextView txtTitle;
    Button btnSale, btnSettlement, btnVoid;
    ImageButton ibtrans_history, ibSettings, ibsettle_history;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;
    public static boolean isAmexCardSelected;
    public static boolean isHomeActive;
    private boolean isShowKeyEntry;
    ImageView imgLogo, imgLogoSeylan;

    @Override
    protected void onInitActivity(APIData... data) {
        // TODO Auto-generated method stub

        // setContentView(R.layout.layout_home) ;

        setContentView(R.layout.activity_home);

        InitViews();
    }

    public void onSale(View v) {
        // Intent intent = new Intent(this, SalePad.class);
        // startActivity(intent);
        // txt.setText("hello world") ;

        long visaPermissions = Config.getUser(this).getPermissions();
        long amexPermissions = Config.getAmexUser(this).getPermissions();

        boolean isKeyentyEnableVisa = UserPermisions.isKeyEntryTransactionsON(visaPermissions);
        boolean isKeyentyEnableAmex = UserPermisions.isKeyEntryTransactionsON(amexPermissions);

        if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
            pushActivity(OrderTrackingActivity.class);
        } else {

            if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
                if (isKeyentyEnableVisa || isKeyentyEnableAmex) {
                    isShowKeyEntry = true;
                } else {
                    isShowKeyEntry = false;
                }
            } else if (m_client.isLoggedIn()) {
                if (isKeyentyEnableVisa) {
                    isShowKeyEntry = true;
                } else {
                    isShowKeyEntry = false;
                }
            } else if (m_client_amex.isLoggedIn()) {
                if (isKeyentyEnableAmex) {
                    isShowKeyEntry = true;
                } else {
                    isShowKeyEntry = false;
                }
            }


            if (isShowKeyEntry) {
                pushActivity(SalePad_Keyentry.class);
            } else {
                pushActivity(SalePad.class);
            }


        }

    }

    public void onVoid(View v) {
        TxDetail.IS_SIGN_BTN_VISIBLE = true;
        pushActivity(OpenTransations.class);
        //pushActivity(BluetoothScanActivity.class);
    }

    public void onSet2(View v) {
        pushActivity(Settlement.class);
    }

    public void onSettle(View v) {
        // pushActivity(Settlement.class);
        // unittest() ;
    }

    /*public void onSetting(View v) {
        pushActivity(Setting.class);
        // unittest() ;
    }*/

    public void onSetting(View v) {
        goSettings();
    }

    private void goSettings() {
        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);


        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            isAmexCardSelected = false;
            Intent intent = new Intent(Home.this, SettingsSecondary.class);
            startActivityForResult(intent, 0);
        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                isAmexCardSelected = false;
                Intent intent = new Intent(Home.this, Setting.class);
                startActivityForResult(intent, 0);
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                isAmexCardSelected = true;
                Intent intent = new Intent(Home.this, Setting.class);
                startActivityForResult(intent, 0);
            }
        }
    }

    public void onHistory(View v) {
        TxDetail.IS_SIGN_BTN_VISIBLE = false;
        pushActivity(CloseTransations.class);
    }

    public void onSettleHistory(View v) {
        TxDetail.IS_SIGN_BTN_VISIBLE = false;
        pushActivity(BatchHistory.class);
    }

    private void unittest() {
        TxSaleRes res = new TxSaleRes();
        res.setTxId(25);
        res.setCardHolder("aaaa");
        res.setCcLast4("1234");
        res.setAmount(100);
        res.setCardType(2);
        //res.setServerTime(new Date());
        res.setApprovalCode("1234");

        pushActivity(SignaturePad.class, res);
    }

    private void InitViews() {

        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogoSeylan = (ImageView) findViewById(R.id.imgLogo_seylan);

        if (com.setting.env.Environment.is_demo_version) {
            imgLogo.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.VISIBLE);
        }

        //set home logo according to the logged in bank
        //if logged in from any other bank except amex
        if ((m_client.isLoggedIn() && m_client_amex.isLoggedIn()) || m_client.isLoggedIn()) {
            if (Config.getBankCode(getApplicationContext()).equals("hnb")) {
                imgLogo.setVisibility(View.GONE);
                imgLogoSeylan.setVisibility(View.VISIBLE);
                imgLogoSeylan.setBackgroundResource(R.drawable.momo_payable);

            }else  if (Config.getBankCode(getApplicationContext()).equals("seylan")) {
                imgLogo.setVisibility(View.GONE);
                imgLogoSeylan.setVisibility(View.VISIBLE);
                imgLogoSeylan.setBackgroundResource(R.drawable.seylan_payable_logo);

            }else if (Config.getBankCode(getApplicationContext()).equals("commercial")) {
                imgLogo.setVisibility(View.GONE);
                imgLogoSeylan.setVisibility(View.VISIBLE);
                imgLogoSeylan.setBackgroundResource(R.drawable.combank_logo);

            }else if (Config.getBankCode(getApplicationContext()).equals("boc")) {
                imgLogo.setVisibility(View.GONE);
                imgLogoSeylan.setVisibility(View.VISIBLE);
                imgLogoSeylan.setBackgroundResource(R.drawable.boc_logo);

            } else{
                imgLogoSeylan.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);
            }
        }else if(m_client_amex.isLoggedIn()){
            imgLogo.setVisibility(View.GONE);
            imgLogoSeylan.setVisibility(View.VISIBLE);
            imgLogoSeylan.setBackgroundResource(R.drawable.ntb_logo);
        }


        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        btnSale = (Button) findViewById(R.id.btnSale);
        btnSale.setTypeface(TypeFaceUtils.setRobotoLight(this));

        btnSettlement = (Button) findViewById(R.id.btnSettlement);
        btnSettlement.setTypeface(TypeFaceUtils.setRobotoLight(this));

        btnVoid = (Button) findViewById(R.id.btnVoid);
        btnVoid.setTypeface(TypeFaceUtils.setRobotoLight(this));

        ibtrans_history = (ImageButton) findViewById(R.id.ibtrans_history);

        ibsettle_history = (ImageButton) findViewById(R.id.ibsettle_history);

        ibSettings = (ImageButton) findViewById(R.id.ibSettings);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Home(txtTitle, btnSale, btnSettlement, btnVoid, ibtrans_history, ibsettle_history);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Home(txtTitle, btnSale, btnSettlement, btnVoid, ibtrans_history, ibsettle_history);
        } else {
            // do nothing
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isPasswordChanged) {
            isPasswordChanged = false;

            if (Home.isAmexCardSelected) {
                pushActivity(LoginSecondary.class);
            } else {
                pushActivity(Login.class);
            }
            finish();
        }

        //RESTART APPLICATION AFTER CHANGE THE LANGUAGE
        if (ChangeLanguageActivity.isLanguageChanged) {
            ChangeLanguageActivity.isLanguageChanged = false;
            pushActivity(Launcher.class);
            finish();
        }

        //IF PORTAL CHANGED
        if (isPortalChanged) {
            isPortalChanged = false;
            pushActivity(Login.class);
            finish();
        }

        //REDIRECT TO SETTINGS FROM LOGIN
        if (Login.isRedirectToSettings) {
            Login.isRedirectToSettings = false;
            goSettings();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isHomeActive = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isHomeActive = false;
    }
}
