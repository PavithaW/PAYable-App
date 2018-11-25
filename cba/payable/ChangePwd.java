package com.cba.payable;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.connection.EnumApi;
import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.PwdChangeReq;
import com.mpos.pojo.SimpleAck;
import com.mpos.util.CHARFilter;
import com.mpos.util.LangPrefs;
import com.mpos.util.PasswordValidator;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class ChangePwd extends GenericActivity {

    TextView txtTitle;
    EditText edtCurrentPassword, edtNewPass, edtRePass;
    Button btnSubmit;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    private String actionType = "settings";
    public static boolean isPwChanged;

	/*private EditText txtPwd, txtNewPwd, txtConfirmPwd;*/

    protected void onInitActivity(APIData... data) {
        //setContentView(R.layout.layout_pwd);

        setContentView(R.layout.activity_change_password);

        InitViews();

        Intent pwIntent = getIntent();
        try {
            actionType = pwIntent.getStringExtra("caller");
        } catch (Exception e) {
            actionType = "settings";
        }


/*		txtPwd = (EditText) findViewById(R.id.txtPwd);
        txtNewPwd = (EditText) findViewById(R.id.txtNewPwd);
		txtConfirmPwd = (EditText) findViewById(R.id.txtConfirmPwd);*/
    }

    public void onNavBack(View v) {
        //onNavBackPressed();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (actionType != null && actionType.equals("login")) {
            Toast.makeText(getApplicationContext(), "Please change the password", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            super.onBackPressed();
        }
    }

    public void onChangePwd() {

        String strPwd = edtCurrentPassword.getText().toString();

        if (strPwd == null) {
            showDialog("Error", "Please enter your password");
            return;
        }

        strPwd = strPwd.trim();

        if (strPwd.length() == 0) {
            showDialog("Error", "Please enter your password");
            return;
        }

        int res = CHARFilter.isBloackedCharExist(strPwd);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }

        String strNewPwd = edtNewPass.getText().toString();

        if (strNewPwd == null) {
            showDialog("Error", "Please enter your new password");
            return;
        }

        strNewPwd = strNewPwd.trim();

        if (strNewPwd.length() == 0) {
            showDialog("Error", "Please enter your new password");
            return;
        }

        if (strNewPwd.length() < 7) {
            showDialog("Error", "Password must contain minimum 7 letters.");
            return;
        }

        res = CHARFilter.isBloackedCharExist(strNewPwd);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }

        if (!PasswordValidator.validate(strNewPwd)) {
            showDialog("Error", "Password must contain minimum one character and one number.");
            return;
        }

        String strConfirmPwd = edtRePass.getText().toString();

        if (strConfirmPwd == null) {
            showDialog("Error", "Please enter the confirmation");
            return;
        }

        strConfirmPwd = strConfirmPwd.trim();

        if (strConfirmPwd.length() == 0) {
            showDialog("Error", "Please enter the confirmation");
            return;
        }

        res = CHARFilter.isBloackedCharExist(strConfirmPwd);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }


        if (!strNewPwd.equals(strConfirmPwd)) {
            showDialog("Error", "New password and confirmation does not match.");
            return;
        }

        if (strPwd.equals(strNewPwd)) {
            showDialog("Error", "New password and old password cannot be same.");
            return;
        }

        //CHECK NUMBER and TEXT
        boolean isGoodPw = CHARFilter.isTextorNumber(strConfirmPwd);

        if (isGoodPw) {

            PwdChangeReq req = new PwdChangeReq();
            req.setPwd(strPwd);
            req.setNewPwd(strNewPwd);
            req.setConfirmPwd(strConfirmPwd);

            //	m_client.modifyPassword(100, "Please wait...", req);

            if (Home.isAmexCardSelected) {
                if (m_client_amex.isLoggedIn()) {
                    m_client_amex.modifyPassword(100, ProgressDialogMessagesUtil.changePwdTranslation(lang_status), req);
                }
            } else {
                if (m_client.isLoggedIn()) {
                    m_client.modifyPassword(100, ProgressDialogMessagesUtil.changePwdTranslation(lang_status), req);
                }
            }
        } else {
            showDialog("Error", "New password should be both text and number included password");
        }

    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        edtCurrentPassword = (EditText) findViewById(R.id.edtCurrentPassword);
        edtCurrentPassword.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtNewPass.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        edtRePass = (EditText) findViewById(R.id.edtRePass);
        edtRePass.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangePwd();
            }
        });

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_ChangePassword(txtTitle, edtCurrentPassword, edtNewPass, edtRePass, btnSubmit);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_ChangePassword(txtTitle, edtCurrentPassword, edtNewPass, edtRePass, btnSubmit);
        } else {
            // do nothing
        }

        TextChange_Functions();
    }

    private void TextChange_Functions() {
        edtCurrentPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), ChangePwd.this);
                        sinlang_select.Apply_ChangePassword_CurrentPass(edtCurrentPassword);

                    } else {

                        edtCurrentPassword.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), ChangePwd.this);
                        tamlang_select.Apply_ChangePassword_CurrentPass(edtCurrentPassword);
                    } else {

                        edtCurrentPassword.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
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

        edtNewPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), ChangePwd.this);
                        sinlang_select.Apply_ChangePassword_NewPass(edtNewPass);

                    } else {

                        edtNewPass.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), ChangePwd.this);
                        tamlang_select.Apply_ChangePassword_NewPass(edtNewPass);
                    } else {

                        edtNewPass.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
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

        edtRePass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), ChangePwd.this);
                        sinlang_select.Apply_ChangePassword_RePass(edtRePass);

                    } else {

                        edtRePass.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), ChangePwd.this);
                        tamlang_select.Apply_ChangePassword_RePass(edtRePass);
                    } else {

                        edtRePass.setTypeface(TypeFaceUtils.setLatoRegular(ChangePwd.this));
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
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        if (callerId == 100) {
            if (data != null) {
                if (data instanceof SimpleAck) {

                    showToastMessage("Password successfully changed. Please sign-in again.");

                    logoutAccounts();

                    String str = getIntent().getStringExtra("caller");

                    if (str == null) {
                        logOut();
                        return;
                    }

                    if (str.equalsIgnoreCase("launcher") || str.equalsIgnoreCase("login")) {

                        isPwChanged = true;
                        if (Home.isAmexCardSelected) {
                            if(getIntent().getBooleanExtra("PWChanged", false)){//bug ID 0001809
                                pushActivity(Login.class);
                            }else{
                                pushActivity(LoginSecondary.class);
                            }
                        } else {
                            pushActivity(Login.class);
                        }
                        finish();

                    } else if (str.equalsIgnoreCase("settings")) {

                        Setting.isSettingsClose = true;
                        SettingsSecondary.isSettingsSecondaryClose = true;
                        Home.isPasswordChanged = true;

                        m_client = new MsgClient(this);
                        m_client.loadCredentials(this);

                        m_client_amex = new MsgClientAmex(this);
                        m_client_amex.loadCredentials(this);

                        if(getIntent().getBooleanExtra("PWChanged", false)){//bug ID 0001809
//                            finish();
//                            pushActivity(Login.class);
                            Home.isPasswordChanged = false;

                            //no one logged in
                            if (!m_client.isLoggedIn() && !m_client_amex.isLoggedIn()) {
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                ComponentName cn = intent.getComponent();
                                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                startActivity(mainIntent);
                            } else if(Home.isAmexCardSelected){
                                Home.isAmexCardSelected = false;
                                pushActivity(LoginSecondary.class);
                                return;
                            }else{
                                finish();
                                pushActivity(Login.class);
                                return;
                            }

                        }else{
                            finish();
                        }

                    }else if(getIntent().getBooleanExtra("fromLauncher", false)){
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        ComponentName cn = intent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                        startActivity(mainIntent);
                    }

                    //logOut();
                }
            }
        }
    }


    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            Setting.isSettingsClose = true;
            SettingsSecondary.isSettingsSecondaryClose = true;
            logoutAllNow();
            finish();
        }
    }

    //Endpoint:OpenTxHistory  java.net.SocketTimeoutException: SSL handshake timed out  error code:1001


    private void logoutAccounts() {
        if (Home.isAmexCardSelected) {
            //single login in amex
            if (m_client_amex.isLoggedIn()) {
                Merchant m = Config.getAmexUser(getApplicationContext());
                m.setAuth1(0);
                m.setAuth2(0);

                Config.setAmexState(getApplicationContext(), Config.STATUS_LOGOUT);
                Config.setAmexUser(getApplicationContext(), m);
            }
        } else {
            //single login in visa/master
            if (m_client.isLoggedIn()) {
                Merchant m = Config.getUser(getApplicationContext());
                m.setAuth1(0);
                m.setAuth2(0);

                Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
                Config.setUser(getApplicationContext(), m);
            }
        }
    }

}
