package com.cba.payable;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
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

public class LoginSecondary extends GenericActivity {

    private static final String LOG_TAG = "payable " + LoginSecondary.class.getSimpleName();

    private ImageView imgLogo;
    private TextView txtSignIn, txtSignUp, signUpTxt;
    private EditText edtEmail, edtPassword, edtBankType;
    private Button btnSignIn;
    private RelativeLayout rlFooter, rlDouble, rlSignUp;

    LangPrefs langPrefs;
    ProgressDialogMessagesUtil proUtil;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    English_LangSelect englang_select;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    public static boolean isAmexLogin;

    String strBankCode = null;

    @Override
    protected void onInitActivity(APIData... data) {
        Log.d(LOG_TAG, "onInitActivity : ");
        setContentView(R.layout.activity_login_secondary);
        InitViews();
    }

    private void InitViews() {
        Log.d(LOG_TAG, "InitViews : ");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        if (com.setting.env.Environment.is_demo_version) {
            imgLogo.setVisibility(View.GONE);
        } else {
            imgLogo.setVisibility(View.VISIBLE);
        }

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        proUtil = new ProgressDialogMessagesUtil(getApplicationContext());

        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        txtSignIn.setTypeface(TypeFaceUtils.setLatoLight(this));

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(this));

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(this));

        edtBankType = (EditText) findViewById(R.id.edtBankType);
        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(this));

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setTypeface(TypeFaceUtils.setRobotoLight(this));
        btnSignIn.setOnClickListener(onClickListener);

        rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        rlFooter.setOnClickListener(onClickListener);

        //IF LOGIN SCREEN OPEN AFTER CHANGE PW, SO NO NEED TO SHOW BACK TO SETTINGS BUTTON
        if (!Setting.isFromSettings) {
            Setting.isFromSettings = false;
            rlFooter.setVisibility(View.INVISIBLE);
        } else {
            //IF THIS SCREEN LOAD FROM CHANGE_PW,. NO NEED TO SHOW SETTINGS BUTTON

            if (m_client.isLoggedIn() || m_client_amex.isLoggedIn()) {
                rlFooter.setVisibility(View.VISIBLE);
            }else if (ChangePwd.isPwChanged) {
                ChangePwd.isPwChanged = false;
                rlFooter.setVisibility(View.INVISIBLE);
            } else {
                rlFooter.setVisibility(View.VISIBLE);
            }
        }

        //show back to setting button even if logged in with one account
        if (m_client.isLoggedIn() || m_client_amex.isLoggedIn()) {
            rlFooter.setVisibility(View.VISIBLE);
        }

        //hide back to setting button if logged out from both accounts
        if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
            rlFooter.setVisibility(View.INVISIBLE);
        }


        rlSignUp = (RelativeLayout) findViewById(R.id.rlSignUp);
        rlSignUp.setOnClickListener(onClickListener);

        signUpTxt = (TextView) findViewById(R.id.logInTextView);
        signUpTxt.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_SignIn_Amex_Tamil(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, signUpTxt, txtSignUp);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_SignIn_Amex_Sinhala(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, signUpTxt, txtSignUp);
        } else {
            // do nothing
        }

        edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), LoginSecondary.this);
                        tamlang_select.Apply_SignIn_Email(edtEmail);
                    } else {
                        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), LoginSecondary.this);
                        sinlang_select.Apply_SignIn_Email(edtEmail);
                    } else {
                        edtEmail.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }
                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), LoginSecondary.this);
                        sinlang_select.Apply_SignIn_Password(edtPassword);
                    } else {
                        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), LoginSecondary.this);
                        tamlang_select.Apply_SignIn_Password(edtPassword);
                    } else {
                        edtPassword.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }
                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtBankType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), LoginSecondary.this);
                        sinlang_select.Apply_SignIn_Bank(edtBankType);
                    } else {
                        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), LoginSecondary.this);
                        tamlang_select.Apply_SignIn_Bank(edtBankType);
                    } else {
                        edtBankType.setTypeface(TypeFaceUtils.setLatoRegular(LoginSecondary.this));
                    }
                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtBankType.requestFocus();

        gestureDetector = new GestureDetector(new LoginSecondary.GestureListener());
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btnSignIn: {
                    //sign in button
                    onSignIn();
                    break;
                }

                case R.id.rlSignUp: {
                    //sign up button
                    pushActivity(SignUpSecondary.class);
                    break;
                }

                case R.id.rlFooter: {
                    //back to settings
                    //pushActivity(Setting.class);
                    onBackPressed();
                    break;
                }

            }
        }
    };

    public void onSignIn() {
        isAmexLogin = true;
        Log.d(LOG_TAG, "onSignIn : ");

        // get values from text box

        boolean isTested = false;

        String strEmail = edtEmail.getText().toString();
        String strPwd = edtPassword.getText().toString();
        strBankCode = edtBankType.getText().toString().trim().toLowerCase();
        Merchant amexMerchant = Config.getAmexUser(getApplicationContext());

        if (isTested) {
            //NTB AMEX
            strBankCode = "ntb";
            strEmail = "amaldev@cba.lk";
            strPwd = "test12345"; //test1234
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

        if (!strBankCode.equalsIgnoreCase("ntb")) {
            showDialog("Error", "Please enter valid bank code.");
            return;
        }

        //entered bank code can not be ntb if logged in from amex
        if(amexMerchant.getAuth1() != 0 && strBankCode.equalsIgnoreCase("ntb")){
            showDialog("Error", "Please enter valid bank code.");
            return;
        }

        if (strEmail == null) {
            showDialog("Error", "Please enter your username.");
            return;
        }

        strEmail = strEmail.trim();

        if (strEmail.length() == 0) {
            showDialog("Error", "Please enter your username.");
            return;
        }

        int res = CHARFilter.isBloackedCharExist(strEmail);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }

        if (strPwd == null) {
            showDialog("Error", "Please enter your password");
            return;
        }

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
        String simNo = mTelephonyMgr.getSimSerialNumber();

        Merchant m = new Merchant();

        m.setUserName(strEmail);
        m.setPwd(strPwd);

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

        //Config.setBankCode(getApplicationContext(), strBankCode);
        m_client_amex.signIn(100, ProgressDialogMessagesUtil.loginTranslation(lang_status), m);
    }

    @Override
    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        /*Setting.isClose = true;
        Home.isClose = true;*/

        Login.isRedirectToSettings = false;

        Log.d(LOG_TAG, "onCallSuccess : EnumApi = " + api + ", callerId = " + callerId + ", APIData = " + data.getJson());

        if (callerId == 100) {
            if (data != null) {
                if (data instanceof Merchant) {

                    isAmexLogin = false;

                    Merchant res = (Merchant) data;
                    Log.i(LOG_TAG, "saving user state");
                    Config.setAmexUser(getApplicationContext(), res);
                    Config.setAmexState(getApplicationContext(), Config.STATUS_LOGIN);
                    Config.setReaderIdAmex(getApplicationContext(), res.getCardReaderId());
                    Config.setReaderType(getApplicationContext(), res.getReaderType());

                    if (res.getPwdFlag() == Merchant.PWD_FLAG_PORTAL) {
                        //showChangePasswordAlert();
                        Home.isAmexCardSelected = true;
                        Intent intent = new Intent(LoginSecondary.this, ChangePwd.class);
                        intent.putExtra("caller", "login");
                        startActivity(intent);
                        finish();
                    } else {

                        if (BluetoothPref.isRegiesterRequiredAmex(getApplicationContext())) {
                            Home.isHomeActive = false;
                            Intent intent = new Intent(this, BluetoothScanActivity.class);
                            intent.putExtra("BANK_CODE", strBankCode);
                            intent.putExtra("ACTION", "amexlogin");
                            startActivity(intent);
                            finish();
                        } else {

                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            ComponentName cn = intent.getComponent();
                            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                            startActivity(mainIntent);

//                            if (Home.isHomeActive) {
//                                Setting.isSettingsClose = true;
//                                finish();
//                            } else {
//                                Intent intent = new Intent(this, Home.class);
//                                startActivity(intent);
//                                finish();
//                            }
                        }
                    }
                }
            }

        }
    }

    private void showChangePasswordAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginSecondary.this).create();
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Please change your AMEX account password.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(LoginSecondary.this, ChangePwd.class);
                        intent.putExtra("caller", "login");
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

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

            pushActivity(DeviceInfo.class);

            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(LOG_TAG, "dispatchTouchEvent : ");

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(ev);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + w.getLeft() - scrcoords[0];
            float y = ev.getRawY() + w.getTop() - scrcoords[1];

            if (ev.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    public void onBackPressed() {
//        if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
//            Setting.isSettingsClose = true;
//            finish();
//        } else {
//            super.onBackPressed();
//        }

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
            englang_select.Apply_SignIn_Amex_English(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, signUpTxt, txtSignUp);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_SignIn_Amex_Tamil(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, signUpTxt, txtSignUp);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_SignIn_Amex_Sinhala(txtSignIn, edtEmail, edtPassword, edtBankType, btnSignIn, signUpTxt, txtSignUp);
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
