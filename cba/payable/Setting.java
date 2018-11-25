package com.cba.payable;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;
import com.mpos.storage.LogDB;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class Setting extends GenericActivity {

    protected static final String TAG = "JEYLOGS";

    TextView txtTitle, txtChangePassword, txtMerchant, txtCheckout, txtSelectLanguage, txtHardware,
            txtDeviceInfo, txtOrderTracking, txtLogs, txtScanReaders, txtLogout, txtLogout2, txtLogIn, txtReader;
    Button btnLogout;
    ToggleButton tgOrder;
    RelativeLayout rlChangePassword, rlMerchant, rlSelectLang, rlLogs, rlLogout,
            rlLogIn, rlScanReaders, rlCardReader, rlOrderTracking, rlLogout2, rlDeviceInfo;
    ImageView imageView1_1, imageView1_2, imageView2_1, imageView2_2, imageView3_1, imageView3_2;
    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;
    public static boolean isSettingsClose;
    public static  boolean isFromSettings;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isSettingsClose) {
            InitViews();

            m_client = new MsgClient(this);
            m_client.loadCredentials(this);

            m_client_amex = new MsgClientAmex(this);
            m_client_amex.loadCredentials(this);

            if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
                //dual login
                rlLogout.setVisibility(View.VISIBLE);
                rlLogout2.setVisibility(View.VISIBLE);
                rlLogIn.setVisibility(View.GONE);

                imageView1_1.setImageResource(R.drawable.amex);
                imageView1_2.setImageResource(R.drawable.diners_club);
                imageView3_1.setImageResource(R.drawable.visa_settings);
                imageView3_2.setImageResource(R.drawable.master);

                txtLogout.setText(getResources().getString(R.string.LogoutAD));
                txtLogout2.setText(getResources().getString(R.string.LogoutVM));
            } else {
                if (m_client.isLoggedIn()) {
                    //single login in visa/master
                    rlLogout.setVisibility(View.VISIBLE);
                    rlLogout2.setVisibility(View.GONE);
                    rlLogIn.setVisibility(View.VISIBLE);

                    imageView1_1.setImageResource(R.drawable.visa_settings);
                    imageView1_2.setImageResource(R.drawable.master);
                    imageView2_1.setImageResource(R.drawable.amex);
                    imageView2_2.setImageResource(R.drawable.diners_club);

                    txtLogout.setText(getResources().getString(R.string.LogoutVM));
                    txtLogIn.setText(getResources().getString(R.string.LogInAD));
                } else if (m_client_amex.isLoggedIn()) {
                    //single login in amex
                    rlLogout.setVisibility(View.VISIBLE);
                    rlLogout2.setVisibility(View.GONE);
                    rlLogIn.setVisibility(View.VISIBLE);

                    imageView1_1.setImageResource(R.drawable.amex);
                    imageView1_2.setImageResource(R.drawable.diners_club);
                    imageView2_1.setImageResource(R.drawable.visa_settings);
                    imageView2_2.setImageResource(R.drawable.master);

                    txtLogout.setText(getResources().getString(R.string.LogoutAD));
                    txtLogIn.setText(getResources().getString(R.string.LogInVM));
                }
            }

            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_Settings_2(txtLogout, txtLogout2, txtLogIn, m_client.isLoggedIn(),
                        m_client_amex.isLoggedIn());
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_Settings_2(txtLogout, txtLogout2, txtLogIn, m_client.isLoggedIn(),
                        m_client_amex.isLoggedIn());
            } else {
                // do nothing
            }
        } else {
            isSettingsClose = false;
            finish();
        }
    }

    /**
     * Onclick Functions for UI Variables
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.rlChangePassword: {
                    //pushActivity(ChangePwd.class);
                    Intent intent = new Intent(getApplicationContext(), ChangePwd.class);
                    intent.putExtra("caller", "settings");
                    intent.putExtra("PWChanged", true);
                    startActivity(intent);
                    break;
                }

                case R.id.rlCardReader: {
                    //pushActivity(BluetoothScanActivity.class);
                    Intent intent = new Intent(getApplicationContext(), BluetoothScanActivity.class);
                    intent.putExtra("isAmex", getIntent().getBooleanExtra("isAmex", false));
                    intent.putExtra("ACTION", "settings");
                    startActivity(intent);
                    break;
                }

                case R.id.rlMerchant: {
                    pushActivity(MerchantProfile_Activity.class);
                    break;
                }

                case R.id.rlSelectLang: {
                    pushActivity(ChangeLanguageActivity.class);
                    break;
                }

                case R.id.rlLogs: {
                    pushActivity(LogErrorsActivity.class);
                    break;
                }

                /*case R.id.rlDeviceInfo: {
                    pushActivity(DeviceInfo.class);
                    break;
                }*/

                case R.id.rlLogout: {
                    _fireLogOut();
                    break;
                }

                case R.id.rlLogout2: {
                    _fireLogOut2();
                    break;
                }

                case R.id.rlLogIn: {
                    _logIn();
                    break;
                }

            }
        }
    };

    public void onNavBack(View v) {
        //onNavBackPressed();
        goBack();
    }

    private void InitViews() {

        PayableApp payableApp = new PayableApp();

        LogDB db = new LogDB(this);

		/* for (int i = 0; i < 3; i++) {
             Log.d("Insert: ", "Inserting ..");
			 db.addErrorItem(new ErrorLogBean("Sign in " + String.valueOf(i),
			 "Nullpointer exception occured that would be repeated again if precautions are not taken. So therefore it would be good to do do some rectifications " + String.valueOf(i),-1));
			 }*/

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        /*txtProfileSettings = (TextView) findViewById(R.id.txtProfileSettings);
        txtProfileSettings.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        txtChangePassword = (TextView) findViewById(R.id.txtChangePassword);
        txtChangePassword.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtMerchant = (TextView) findViewById(R.id.txtMerchant);
        txtMerchant.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtScanReaders = (TextView) findViewById(R.id.txtScanReaders);
        txtScanReaders.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        /*txtCheckout = (TextView) findViewById(R.id.txtCheckout);
        txtCheckout.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        txtSelectLanguage = (TextView) findViewById(R.id.txtSelectLanguage);
        txtSelectLanguage.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        /*txtLog = (TextView) findViewById(R.id.txtLog);
        txtLog.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        txtOrderTracking = (TextView) findViewById(R.id.txtOrderTracking);
        txtOrderTracking.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        /*txtSignature = (TextView) findViewById(R.id.txtSignature);
        txtSignature.setTypeface(TypeFaceUtils.setLatoLight(getApplicationContext()));

        txtHardware = (TextView) findViewById(R.id.txtHardware);
        txtHardware.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        // txtPrinters = (TextView) findViewById(R.id.txtPrinters);
        // txtPrinters.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        /*txtDeviceInfo = (TextView) findViewById(R.id.txtDeviceInfo);
        txtDeviceInfo.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        tgOrder = (ToggleButton) findViewById(R.id.tgOrder);

        /*btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));
        btnLogout.setOnClickListener(onClickListener);

        rlScanReaders = (RelativeLayout) findViewById(R.id.rlScanReaders);
        rlScanReaders.setOnClickListener(onClickListener);*/

        rlChangePassword = (RelativeLayout) findViewById(R.id.rlChangePassword);
        rlChangePassword.setOnClickListener(onClickListener);

        rlMerchant = (RelativeLayout) findViewById(R.id.rlMerchant);
        rlMerchant.setOnClickListener(onClickListener);

        rlLogs = (RelativeLayout) findViewById(R.id.rlLogs);
        rlLogs.setOnClickListener(onClickListener);

        rlSelectLang = (RelativeLayout) findViewById(R.id.rlSelectLang);
        rlSelectLang.setOnClickListener(onClickListener);

        //order tracking relative layout
        rlOrderTracking = (RelativeLayout) findViewById(R.id.rlOrderTracking);
        rlOrderTracking.setOnClickListener(onClickListener);

        rlCardReader = (RelativeLayout) findViewById(R.id.rlCardReader);
        rlCardReader.setOnClickListener(onClickListener);

        rlLogout = (RelativeLayout) findViewById(R.id.rlLogout);
        rlLogout.setOnClickListener(onClickListener);

        rlLogout2 = (RelativeLayout) findViewById(R.id.rlLogout2);
        rlLogout2.setOnClickListener(onClickListener);

        rlLogIn = (RelativeLayout) findViewById(R.id.rlLogIn);
        rlLogIn.setOnClickListener(onClickListener);

        tgOrder = (ToggleButton) findViewById(R.id.tgOrder);

        txtLogout = (TextView) findViewById(R.id.logoutTextView);
        txtLogout.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtLogs = (TextView) findViewById(R.id.txtLogs);
        txtLogs.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtReader = (TextView) findViewById(R.id.txtScanReaders);
        txtReader.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtLogout2 = (TextView) findViewById(R.id.logoutTextView2);
        txtLogout2.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtLogIn = (TextView) findViewById(R.id.logInTextView2);
        txtLogIn.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        imageView1_1 = (ImageView) findViewById(R.id.visaImageView);
        imageView1_2 = (ImageView) findViewById(R.id.masterImageView);

        imageView2_1 = (ImageView) findViewById(R.id.amexImageView2);
        imageView2_2 = (ImageView) findViewById(R.id.dinersImageView2);

        imageView3_1 = (ImageView) findViewById(R.id.amexImageView);
        imageView3_2 = (ImageView) findViewById(R.id.dinersImageView);

        toggle_orderstatus();

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {
            /*if (Home.isAmexCardSelected) {
                txtTitle.setText("Settings AMEX");
            } else {
                txtTitle.setText("Settings Visa");
            }*/
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Settings(txtTitle, txtChangePassword, txtMerchant,
                    txtSelectLanguage, txtReader, txtLogs, txtOrderTracking);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Settings(txtTitle, txtChangePassword, txtMerchant,
                    txtSelectLanguage, txtReader, txtLogs, txtOrderTracking);

        } else {
            // do nothing
        }
    }

    private void toggle_orderstatus() {

        if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
            tgOrder.setChecked(true);
        } else {
            tgOrder.setChecked(false);
        }

        tgOrder.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (tgOrder.isChecked()) {
                    OrderTrackPref.setOrderStatus(getApplicationContext(), 1);
                }

                if (tgOrder.isChecked() == false) {
                    OrderTrackPref.setOrderStatus(getApplicationContext(), 0);
                }
            }
        });
    }

    private void _fireLogOut() {
        String deviceId = "";

        if (m_client.isLoggedIn()) {
            deviceId = Config.getReaderId(getApplicationContext());
        } else if (m_client_amex.isLoggedIn()) {
            deviceId = Config.getReaderIdAmex(getApplicationContext());
        }

        Log.i(TAG, "deviceId: " + deviceId);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
//            showToastMessage("logged out from the amex/diners");
            Merchant m = Config.getAmexUser(getApplicationContext());
            m.setAuth1(0);
            m.setAuth2(0);

            Config.setAmexState(getApplicationContext(), Config.STATUS_LOGOUT);
            Config.setAmexUser(getApplicationContext(), m);
            Config.resetCurrencyStatusAmex(getApplicationContext(), false);

            Intent intent = new Intent(this, LoginSecondary.class);
            startActivity(intent);
            finish();

            return;
        } else {
            if (m_client.isLoggedIn()) {

                String deviceId2 = Config.getReaderId(getApplicationContext());
                Log.i(TAG, "deviceId2: " + deviceId2);
                //single login in visa/master
                //showToastMessage("logged out from the visa/master");
                Merchant m = Config.getUser(getApplicationContext());
                m.setAuth1(0);
                m.setAuth2(0);

                Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
                Config.setUser(getApplicationContext(), m);
                Config.resetCurrencyStatus(getApplicationContext(), false);

                logOut();

                String deviceId3 = "";

                if (m_client.isLoggedIn()) {
                    deviceId3 = Config.getReaderId(getApplicationContext());
                } else if (m_client_amex.isLoggedIn()) {
                    deviceId3 = Config.getReaderIdAmex(getApplicationContext());
                }

                Log.i(TAG, "deviceId3: " + deviceId3);
                return;
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
//                showToastMessage("logged out from the amex/diners");
                Merchant m = Config.getAmexUser(getApplicationContext());
                m.setAuth1(0);
                m.setAuth2(0);

                Config.setAmexState(getApplicationContext(), Config.STATUS_LOGOUT);
                Config.setAmexUser(getApplicationContext(), m);
                Config.resetCurrencyStatusAmex(getApplicationContext(), false);

                logOut();
                return;
            }
        }
    }

    private void _fireLogOut2() {

        String deviceId = Config.getReaderId(getApplicationContext());
        Log.i(TAG, "** deviceId: " + deviceId);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            Merchant m = Config.getUser(getApplicationContext());
            m.setAuth1(0);
            m.setAuth2(0);
            m.setReaderType(Config.getReaderType(getApplicationContext()));

            Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
            Config.setUser(getApplicationContext(), m);
            Config.resetCurrencyStatus(getApplicationContext(), false);

            String deviceId2 = "";

            if (m_client.isLoggedIn()) {
                deviceId2 = Config.getReaderId(getApplicationContext());
            } else if (m_client_amex.isLoggedIn()) {
                deviceId2 = Config.getReaderIdAmex(getApplicationContext());
            }

            Log.i(TAG, "** deviceId2: " + deviceId2);


            logOut();

            String deviceId3 = Config.getReaderId(getApplicationContext());
            Log.i(TAG, "** deviceId3: " + deviceId3);


            return;
        } else {
            if (m_client.isLoggedIn()) {
                Config.resetCurrencyStatus(getApplicationContext(), false);
            } else if (m_client_amex.isLoggedIn()) {

            }
        }

    }

    private void _logIn() {
        //redirect to login pages

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {

        } else {
            isFromSettings = true;
            if (m_client.isLoggedIn()) {
                pushActivity(LoginSecondary.class);
                return;
            } else if (m_client_amex.isLoggedIn()) {
                pushActivity(Login.class);
                return;
            }
        }

    }

    public void goBack(){
        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            finish();
        }else{
            Login.isRedirectToSettings = false;
            Intent intent = new Intent(Setting.this, Home.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }
}