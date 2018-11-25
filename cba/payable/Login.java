package com.cba.payable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.connection.EnumApi;
import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;
import com.mpos.util.BluetoothPref;
import com.mpos.util.CHARFilter;
import com.mpos.util.English_LangSelect;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class Login extends GenericActivity {

    private static final String LOG_TAG = "payable " + Login.class.getSimpleName();

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    private EditText txtEmail, txtPwd;

    TextView txtSignIn, txtNotRegistered, txtSignUp;
    EditText edtEmail, edtPassword, edtBankType;
    // EditText edtBankType;
    ImageView imgLogo, imgLogoSeylan;
    RelativeLayout rlDouble, rlFooter;
    Button btnSignIn, btnSettings;

    LangPrefs langPrefs;
    ProgressDialogMessagesUtil proUtil;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    English_LangSelect englang_select;

    int lang_status = 0;
    private int pwFlag;
    public static boolean isRedirectToSettings;
    String simNo;
    String strBankCode = null;

    @Override
    protected void onInitActivity(APIData... data) {
        Log.d(LOG_TAG, "onInitActivity : ");
        setContentView(R.layout.activity_sign_in);

        if (m_client.isLoggedIn()) {
            pwFlag = Config.getPwdFlag(this);
            Home.isAmexCardSelected = false;
        } else if (m_client_amex.isLoggedIn()) {
            pwFlag = Config.getPwdFlagAmex(this);
            Home.isAmexCardSelected = true;
        }

        if (pwFlag == Merchant.PWD_FLAG_PORTAL) {
            Intent intent = new Intent(this, ChangePwd.class);
            intent.putExtra("caller", "login");
            startActivity(intent);
            finish();
        } else {
            InitViews();
        }

    }

    public void onSignIn(View v) {

        Log.d(LOG_TAG, "onSignIn : ");

        boolean isTested = false;

        String strEmail = edtEmail.getText().toString();
        String strPwd = edtPassword.getText().toString();
        strBankCode = edtBankType.getText().toString().trim().toLowerCase();
        Merchant amexMerchant = Config.getAmexUser(getApplicationContext());

        if (isTested) {
            //VISA/MASTER
            strBankCode = "seylan";
            strEmail = "tharaka123";
            strPwd = "test321";

            //AMEX
//            strBankCode = "ntb";
//            strEmail = "amaldev";
//            strPwd = "tharaka123";
        }

        if (strBankCode == null) {
            showDialog("Error", "Please enter your bank code.");
            return;
        }

        strBankCode = strBankCode.trim();

        if (strBankCode.length() == 0) {
            showDialog("Error", "Please enter your bank code.");
            return;
        }

        if (!strBankCode.equalsIgnoreCase("commercial")
                && !strBankCode.equalsIgnoreCase("seylan")
                && !strBankCode.equalsIgnoreCase("ntb")
                && !strBankCode.equalsIgnoreCase("cargills")
                && !strBankCode.equalsIgnoreCase("boc")) {

            showDialog("Error", "Please Enter valid bank code.");

            return;
        }

        //entered bank code can not be ntb if logged in from amex
        if(amexMerchant.getAuth1() != 0 && strBankCode.equalsIgnoreCase("ntb")){
            showDialog("Error", "Please enter valid bank code.");
            return;
        }

        // || !strBankCode.toLowerCase().equals("seylan")

        if (strEmail == null) {
            showDialog("Error", "Please enter your username.");
            return;
        }

        strEmail = strEmail.trim();

        if (strEmail.length() == 0) {
            showDialog("Error", "Please enter your username.");
            return;
        }

		/*
         * if(! EmailValidator.validate(strEmail)){ showDialog("Error" ,
		 * "Please enter valid email." ) ; return ; }
		 */

        int res = CHARFilter.isBloackedCharExist(strEmail);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }

        if (strPwd == null) {
            showDialog("Error", "Please enter your password");
            return;
        }

        // strPwd = strPwd.trim() ;

        if (strPwd.length() == 0) {
            showDialog("Error", "Please enter your password");
            return;
        }

        res = CHARFilter.isBloackedCharExist(strEmail);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }

        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = mTelephonyMgr.getDeviceId();
        simNo = mTelephonyMgr.getSimSerialNumber();

        Merchant m = new Merchant();

        // strEmail = "tstweb3injection@mail.com tstweb3injection@mail.com\" or
        // 1=1 or 1='1" ;
        m.setUserName(strEmail);
        m.setPwd(strPwd);

		/*
         * if(deviceId != null){ m.setDeviceId(deviceId) ; }else{
		 * m.setDeviceId("none"); }
		 *
		 * if(simNo != null){ m.setSimId(simNo) ; }else{ m.setSimId("none") ; }
		 */

        if (deviceId == null) {
            showDialog("Error", "Couldn't fetch your device id.");
            return;
        }

        if (simNo == null) {
            showDialog("Error", "Couldn't fetch your sim id.");
            return;
        }

        m.setDeviceId(deviceId);
        m.setSimId(simNo);

        hideSoftKeyboard();

        // m_client.signIn(100, "Signing in...", m);

		/*
         * if(BluetoothPref.isRegiesterRequired(getApplicationContext())){
		 * Intent intent = new Intent(this, BluetoothScanActivity.class);
		 * startActivity(intent); }else{ Intent intent = new Intent(this,
		 * Home.class); startActivity(intent); }
		 */
        Config.setBankCode(getApplicationContext(), strBankCode);
        if (strBankCode.equalsIgnoreCase("ntb")) {
            m_client_amex.signIn(101, ProgressDialogMessagesUtil.loginTranslation(lang_status), m);
        } else {
            m_client.signIn(100, ProgressDialogMessagesUtil.loginTranslation(lang_status), m);
        }


    }

    public void onSignUp(View v) {
        Log.d(LOG_TAG, "onSignUp : ");
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        //pushActivity(Home.class);
    }

    /**
     * Initialization of Views
     */
    private void InitViews() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Log.d(LOG_TAG, "InitViews : ");

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        //this is the big logo with powered  by PAYABLE
        //imgLogoSeylan = (ImageView) findViewById(R.id.imgLogo_seylan);

        if (com.setting.env.Environment.is_demo_version) {
            imgLogo.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.VISIBLE);
        }

        String signuptext = "<font color=#C1C1C1>Request Your </font> <font color=#8C8D8E>Free Card Reader</font>";

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        proUtil = new ProgressDialogMessagesUtil(getApplicationContext());

        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        txtSignIn.setTypeface(TypeFaceUtils.setLatoLight(this));

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setTypeface(TypeFaceUtils.setRobotoRegular(this));
        txtSignUp.setText(Html.fromHtml(signuptext));

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(this));

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(this));

        edtBankType = (EditText) findViewById(R.id.edtBankType);
        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(this));

		/*
         * edtBankType = (EditText) findViewById(R.id.edtBankType);
		 * edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(this));
		 */

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setTypeface(TypeFaceUtils.setRobotoLight(this));

        btnSettings = (Button) findViewById(R.id.goSettingsBtn);
        btnSettings.setTypeface(TypeFaceUtils.setRobotoLight(this));

        if (!Setting.isFromSettings) {
            Setting.isFromSettings = false;
            btnSettings.setVisibility(View.INVISIBLE);
        } else {
            if (m_client.isLoggedIn() || m_client_amex.isLoggedIn()) {
                btnSettings.setVisibility(View.VISIBLE);
            } else {
                btnSettings.setVisibility(View.INVISIBLE);
            }
        }

        if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
            btnSettings.setVisibility(View.INVISIBLE);
        }

        //IF THIS SCREEN LOAD FROM CHANGE_PW,. NO NEED TO SHOW SETTINGS BUTTON
        if (ChangePwd.isPwChanged) {
            ChangePwd.isPwChanged = false;
            btnSettings.setVisibility(View.INVISIBLE);
        } else {
            if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
                btnSettings.setVisibility(View.INVISIBLE);
            } else {
                btnSettings.setVisibility(View.VISIBLE);
            }
        }

        //show back to setting button even if logged in with one account
        if (m_client.isLoggedIn() || m_client_amex.isLoggedIn()) {
            btnSettings.setVisibility(View.VISIBLE);
        }

        //hide back to setting button if logged out from both accounts
        if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
            btnSettings.setVisibility(View.INVISIBLE);
        }

        rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);


        edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {

                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), Login.this);
                        tamlang_select.Apply_SignIn_Email(edtEmail);
                    } else {

                        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));

                    }

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {

                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), Login.this);
                        sinlang_select.Apply_SignIn_Email(edtEmail);
                    } else {

                        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));
                    }
                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {

                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), Login.this);
                        sinlang_select.Apply_SignIn_Password(edtPassword);
                    } else {

                        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));

                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {

                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), Login.this);
                        tamlang_select.Apply_SignIn_Password(edtPassword);
                    } else {

                        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));

                    }

                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        edtBankType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {

                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), Login.this);
                        sinlang_select.Apply_SignIn_Bank(edtBankType);
                    } else {

                        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));

                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {

                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), Login.this);
                        tamlang_select.Apply_SignIn_Bank(edtBankType);
                    } else {

                        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(Login.this));

                    }

                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        edtBankType.requestFocus();
        // edtEmail.requestFocus();

        gestureDetector = new GestureDetector(new GestureListener());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

        rlDouble = (RelativeLayout) findViewById(R.id.rlDouble);
        rlDouble.setOnTouchListener(gestureListener);

    }

	/*
     * public void onCallError(EnumApi api ,int callerId , ApiException e){
	 * printOnCallErrorLog(api,e) ; }
	 */

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        Log.d(LOG_TAG, "onCallSuccess : EnumApi = " + api + ", callerId = " + callerId + ", APIData = " + data.getJson());

        isRedirectToSettings = false;

        if (callerId == 100) {
            if (data != null) {
                if (data instanceof Merchant) {
                    Merchant res = (Merchant) data;
                    res.setSimId(simNo);
                    Log.i(LOG_TAG, "saving user state");
                    Config.setUser(getApplicationContext(), res);
                    Config.setState(getApplicationContext(), Config.STATUS_LOGIN);
                    Config.setReaderId(getApplicationContext(), res.getCardReaderId());
                    Config.setReaderType(getApplicationContext(), res.getReaderType());

                    if (res.getPwdFlag() == Merchant.PWD_FLAG_PORTAL) {
                        Home.isAmexCardSelected = false;

                        Intent intent = new Intent(this, ChangePwd.class);
                        intent.putExtra("caller", "login");
                        startActivity(intent);
                    } else {

                        if (BluetoothPref.isRegiesterRequired(getApplicationContext())) {
                            Home.isHomeActive = false;
                            Intent intent = new Intent(this, BluetoothScanActivity.class);
                            intent.putExtra("ACTION", "normal");
                            intent.putExtra("BANK_CODE", strBankCode);
                            startActivity(intent);
                        } else {
//                            if (Home.isHomeActive) {
//                                Setting.isSettingsClose = true;
//                                finish();
//                            } else {
//                                pushActivity(Home.class);
//                            }

                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            ComponentName cn = intent.getComponent();
                            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                            startActivity(mainIntent);
                        }

                    }

                    //finish();
                }
            }
        } else if (callerId == 101) {
            if (data != null) {
                if (data instanceof Merchant) {
                    Merchant res = (Merchant) data;
                    res.setSimId(simNo);
                    Log.i(LOG_TAG, "saving user state");
                    Config.setAmexUser(getApplicationContext(), res);
                    Config.setAmexState(getApplicationContext(), Config.STATUS_LOGIN);
                    Config.setReaderIdAmex(getApplicationContext(), res.getCardReaderId());
                    Config.setReaderType(getApplicationContext(), res.getReaderType());

                    if (res.getPwdFlag() == Merchant.PWD_FLAG_PORTAL) {
                        Home.isAmexCardSelected = true;
                        Intent intent = new Intent(this, ChangePwd.class);
                        intent.putExtra("caller", "login");
                        intent.putExtra("PWChanged", true);
                        startActivity(intent);
                    } else {

                        if (BluetoothPref.isRegiesterRequired(getApplicationContext())) {
                            Home.isHomeActive = false;
                            Intent intent = new Intent(this, BluetoothScanActivity.class);
                            intent.putExtra("BANK_CODE", strBankCode);
                            intent.putExtra("ACTION", "normal");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            ComponentName cn = intent.getComponent();
                            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                            startActivity(mainIntent);
                        }

                    }

                    finish();
                }
            }
        }
    }

    public void goSettings(View view) {
        isRedirectToSettings = true;
        onBackPressed();
    }

    class GestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d(LOG_TAG, "GestureListener : x = " + x + ", y = " + y);


            // Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            // Toast.makeText(Login.this, "Hi You just Tapped",
            // Toast.LENGTH_LONG).show();

            pushActivity(DeviceInfo.class);

            return true;
        }
    }

    /**
     * Hide Keyboard on Outside Touch
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Log.d(LOG_TAG, "dispatchTouchEvent : ");

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


    @Override
    public void onBackPressed() {
        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);

        if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
            Setting.isSettingsClose = true;
            //finish();
            this.finishAffinity();
        } else {
            //super.onBackPressed();
            Setting.isSettingsClose = false;
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
        }
    }

    //clear text boxes
    @Override
    protected void onResume() {
        edtEmail.setText("");
        edtPassword.setText("");
        edtBankType.setText("");

        englang_select = new English_LangSelect();
        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);
        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);
        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {
            englang_select.Apply_SignIn_English(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, txtSignUp, btnSettings);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_SignIn_Tamil(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, txtSignUp, btnSettings);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_SignIn_Sinhala(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, txtSignUp, btnSettings);
        } else {
            // do nothing
        }

        super.onResume();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Setting.isSettingsClose = true;
    }*/
}
