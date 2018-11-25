package com.mpos.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cba.payable.CustomTypefaceSpan;
import com.cba.payable.R;
import com.setting.env.Consts;

public class Tamil_LangSelect {

    Context context;
    Activity activity;
    static Typeface font_tamil, font_english;

    double screensize;

    public Tamil_LangSelect(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        font_tamil = Typeface.createFromAsset(context.getAssets(), "Tamil_Font/Baminiii.TTF");
        font_english = Typeface.createFromAsset(context.getAssets(), "Roboto_Font/Roboto-Medium.ttf");

        screensize = getScreenSize();
    }

    public void Apply_Settlements(TextView txtTitle, TextView txtTitle1, TextView txtTitle2, TextView txtTitle3,
                                  TextView txtVisaSettlement, TextView txtMasterSettlement, TextView txtAmexSettlement,
                                  TextView txtDinersSettlement, TextView txtTotalSettlements, TextView txtTotalAmount,
                                  TextView txtSettleNow) {
        String firstWord;
        String secondWord;

        Spannable spannable;
        float textSize = 25.0f;

        txtTitle.setText("nfhLg;gdT");
        txtTitle.setTypeface(font_tamil);

        txtTitle1.setText("VISA/MASTERCARD nfhLg;gdTfs;");
        txtTitle1.setTypeface(font_tamil);

        firstWord = "VISA/MASTERCARD ";
        secondWord = "nfhLg;gdTfs;";

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle1.setText(spannable);

        firstWord = "AMEX/DINERS CLUB ";
        secondWord = "nfhLg;gdTfs;";

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle2.setText(spannable);

        txtTitle3.setText("RUf;fk;");
        txtTitle3.setTypeface(font_tamil);

        txtVisaSettlement.setText("nfhLg;gdT");
        txtVisaSettlement.setTypeface(font_tamil);

        txtMasterSettlement.setText("nfhLg;gdT");
        txtMasterSettlement.setTypeface(font_tamil);

        txtAmexSettlement.setText("nfhLg;gdT");
        txtAmexSettlement.setTypeface(font_tamil);

        txtDinersSettlement.setText("nfhLg;gdT");
        txtDinersSettlement.setTypeface(font_tamil);

        txtTotalSettlements.setText("nkhj;j nfhLg;gdT");
        txtTotalSettlements.setTypeface(font_tamil);

        txtTotalAmount.setText("nkhj;j njhif");
        txtTotalAmount.setTypeface(font_tamil);

        txtSettleNow.setText("nfhLg;gdT nra;f");
        txtSettleNow.setTypeface(font_tamil);

    }

    public void Apply_Settlements_2(TextView txtTitle1, boolean isVMLoggedIn, boolean isAmexLoggedIn) {

        if (isVMLoggedIn) {
            txtTitle1.setText("nfhLg;gdT nfhLg;gdT");
        } else if (isAmexLoggedIn) {
            txtTitle1.setText("m. vf;];gpu]; / ildu;]; fpsg;");
        }

        txtTitle1.setTypeface(font_tamil);

        /*String firstWord = "";
        String secondWord = "";

        Spannable spannable;
        float textSize = 25.0f;

        if (isVMLoggedIn) {
            firstWord = "VISA/MASTERCARD ";
            secondWord = "nfhLg;gdT";
        } else if (isAmexLoggedIn) {
            firstWord = "AMEX/DINERS CLUB ";
            secondWord = "nfhLg;gdT";
        }

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle1.setText(spannable);*/
    }

    public void Apply_Secondary_SignUp_Title(TextView txtTitle) {

        String firstWord = "gjpT nra;f ";
        String secondWord = "AMEX/DINERS CLUB";

        float textSize = 25.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle.setText(spannable);
    }


    public void Apply_Secondary_SignUp_Amex_Text(TextView amexTxt) {
        String firstWord = "AMEX ";
        String secondWord = "kw;Wk; ";
        String thirdWord = "Diners Club ";
        String fourthWord = "ml;il itj;jpUg;gtu;fSf;fhd gjpT nra;jy";

        float textSize = 30.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord + thirdWord + fourthWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length() + secondWord.length(), firstWord.length() + secondWord.length() + thirdWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), firstWord.length() + secondWord.length() + thirdWord.length(), firstWord.length() + secondWord.length() + thirdWord.length() + fourthWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        amexTxt.setText(spannable);
    }

    // ===========Sign In==========//
    public void Apply_Settings_2(TextView txtLogout1, TextView txtLogout2, TextView txtLogIn, boolean isVMLoggedIn,
                                 boolean isAmexLoggedIn) {

        if (isVMLoggedIn && isAmexLoggedIn) {
            txtLogout1.setText("ntspNaW");
            txtLogout2.setText("ntspNaW");
        } else {
            if (isVMLoggedIn) {
                txtLogout1.setText("ntspNaW");
                txtLogIn.setText("cs; Eiojy;");
            } else if (isAmexLoggedIn) {
                txtLogout1.setText("ntspNaW");
                txtLogIn.setText("cs; Eiojy;");
            }
        }

        txtLogout1.setTypeface(font_tamil);
        txtLogout2.setTypeface(font_tamil);
        txtLogIn.setTypeface(font_tamil);

        txtLogout1.setTextSize(14.0f);
        txtLogout2.setTextSize(14.0f);
        txtLogIn.setTextSize(14.0f);

        /*String firstWord;
        String secondWord;

        Spannable spannable;
        float textSize = 18.0f;

        if (isVMLoggedIn && isAmexLoggedIn) {
            firstWord = "ntspNaW ";
            secondWord = "AMEX/DINERS CLUB";

            spannable = new SpannableString(firstWord + secondWord);

            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            txtLogout1.setText(spannable);

            firstWord = "ntspNaW ";
            secondWord = "VISA/MASTERCARD";

            spannable = new SpannableString(firstWord + secondWord);

            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            txtLogout2.setText(spannable);
        } else {
            if (isVMLoggedIn) {
                firstWord = "ntspNaW ";
                secondWord = "VISA/MASTERCARD";

                spannable = new SpannableString(firstWord + secondWord);

                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtLogout1.setText(spannable);

                firstWord = "cs; Eiojy; ";
                secondWord = "AMEX/DINERS CLUB";

                spannable = new SpannableString(firstWord + secondWord);

                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtLogIn.setText(spannable);
            } else if (isAmexLoggedIn) {
                firstWord = "ntspNaW ";
                secondWord = "AMEX/DINERS CLUB";

                spannable = new SpannableString(firstWord + secondWord);

                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtLogout1.setText(spannable);

                firstWord = "cs; Eiojy; ";
                secondWord = "VISA/MASTERCARD";

                spannable = new SpannableString(firstWord + secondWord);

                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtLogIn.setText(spannable);
            }
        }*/

    }

    public void Apply_SignIn_Amex_Tamil(TextView txtSignIn, EditText edtEmail, EditText edtPassword, EditText edtBank,
                                        Button btnSignIn, TextView txtSignUp, TextView settings) {
        txtSignIn.setText("cs; Eiojy;");
        txtSignIn.setTypeface(font_tamil);

        edtEmail.setHint("gadu; ngau;");
        edtEmail.setTypeface(font_tamil);

        edtPassword.setHint("flTr;nrhy;");
        edtPassword.setTypeface(font_tamil);

        edtBank.setHint("tq;fp FwpaPL");
        edtBank.setTypeface(font_tamil);

        btnSignIn.setText("cs; Eiojy;");
        btnSignIn.setTypeface(font_tamil);

        String firstWord = "gjpT nra;f ";
        String secondWord = "AMEX/DINERS CLUB";

        float textSize = 22.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_tamil, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtSignUp.setText(spannable);

        settings.setText("mikg;GfSf;F jpUk;Gf");
        settings.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtSignIn.setTextSize(22.0f);
            edtEmail.setTextSize(18.0f);
            edtPassword.setTextSize(18.0f);
            edtBank.setTextSize(18.0f);
            btnSignIn.setTextSize(20.0f);
            txtSignUp.setTextSize(16.0f);
            settings.setTextSize(20.f);
        }
    }

    public void Apply_SignIn_Tamil(TextView txtSignIn, EditText edtEmail, EditText edtPassword,
                                   EditText edtBank, Button btnSignIn, TextView txtSignUp, Button btnSettings) {
        txtSignIn.setText("cs; Eiojy;");
        txtSignIn.setTypeface(font_tamil);

        edtEmail.setHint("gadu; ngau;");
        edtEmail.setTypeface(font_tamil);

        edtPassword.setHint("flTr;nrhy;");
        edtPassword.setTypeface(font_tamil);

        edtBank.setHint("tq;fp FwpaPL");
        edtBank.setTypeface(font_tamil);

        btnSignIn.setText("cs; Eiojy;");
        btnSignIn.setTypeface(font_tamil);

        String signuptext = "<font color=#C1C1C1>cq;fs; ,ytr</font> <font color=#8C8D8E> ml;il thrpg;ghid tpz;zg;gk; nra;aTk;</font>";

        txtSignUp.setText("cq;fs; ,ytr ml;il thrpg;ghid tpz;zg;gk; nra;aTk;");

        btnSettings.setText("mikg;GfSf;F jpUk;Gf");
        btnSettings.setTypeface(font_tamil);

        txtSignUp.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtEmail.setTextSize(16.0f);
            edtPassword.setTextSize(16.0f);
            edtBank.setTextSize(16.0f);
            txtSignUp.setTextSize(13.0f);
            btnSignIn.setTextSize(18.0f);
            btnSettings.setTextSize(18.f);
        }
    }

    public void Apply_SignIn_Email(EditText edtEmail) {
        edtEmail.setHint("gadu; ngau;");
        edtEmail.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtEmail.setTextSize(16.0f);

        }
    }

    public void Apply_SignIn_Password(EditText edtPassword) {
        edtPassword.setHint("flTr;nrhy;");
        edtPassword.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtPassword.setTextSize(16.0f);

        }
    }

    public void Apply_SignIn_Bank(EditText edtBank) {
        edtBank.setHint("tq;fp FwpaPL");
        edtBank.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtBank.setTextSize(16.0f);

        }
    }

    public void Apply_SignUp_Tamil(TextView txtTitle, EditText edtFirstName, EditText edtLasttName, EditText edtMobileNumber,
                                   EditText edtEmailAddress, EditText edtCompanyName, EditText edtBusinessDes, TextView txtSelectCategory, Button btnSubmit) {
        txtTitle.setText(",ytr ml;il thrpg;ghd; tpz;zg;gk;");
        txtTitle.setTypeface(font_tamil);

        edtFirstName.setHint("Kjw; ngau;");
        edtFirstName.setTypeface(font_tamil);

        edtLasttName.setHint("filrp ngau;");
        edtLasttName.setTypeface(font_tamil);

        edtMobileNumber.setHint("ifnjhiyNgrp vz");
        edtMobileNumber.setTypeface(font_tamil);

        edtEmailAddress.setHint("kpd;dQ;ry; Kftup");
        edtEmailAddress.setTypeface(font_tamil);

        edtCompanyName.setHint("epWtdk;");
        edtCompanyName.setTypeface(font_tamil);

        edtBusinessDes.setHint("cq;fs; tpahghu tpsf;fk;");
        edtBusinessDes.setTypeface(font_tamil);

        txtSelectCategory.setHint("tpahghu gpupT Nju;e;njLf;f");
        txtSelectCategory.setTypeface(font_tamil);

        btnSubmit.setText("rku;g;gpf;f");
        btnSubmit.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            txtTitle.setTextSize(16.0f);
            edtFirstName.setTextSize(18.0f);
            edtLasttName.setTextSize(18.0f);
            edtMobileNumber.setTextSize(18.0f);
            edtEmailAddress.setTextSize(18.0f);
            edtCompanyName.setTextSize(18.0f);
            edtBusinessDes.setTextSize(18.0f);
            txtSelectCategory.setTextSize(18.0f);
            btnSubmit.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_FirstName(EditText edtFirstName) {
        edtFirstName.setHint("Kjw; ngau;");
        edtFirstName.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtFirstName.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_LastName(EditText edtLasttName) {
        edtLasttName.setHint("filrp ngau;");
        edtLasttName.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtLasttName.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Mobile(EditText edtMobileNumber) {
        edtMobileNumber.setHint("ifnjhiyNgrp vz");
        edtMobileNumber.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtMobileNumber.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Email(EditText edtEmailAddress) {
        edtEmailAddress.setHint("kpd;dQ;ry; Kftup");
        edtEmailAddress.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtEmailAddress.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Company(EditText edtCompany) {
        edtCompany.setHint("epWtdk;");
        edtCompany.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtCompany.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_BusinessDes(EditText edtBusinessDes) {
        edtBusinessDes.setHint("cq;fs; tpahghu tpsf;fk;");
        edtBusinessDes.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtBusinessDes.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Title(TextView txtTitle) {
        txtTitle.setText(",ytr ml;il thrpg;ghd; tpz;zg;gk;");
        txtTitle.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            txtTitle.setTextSize(14.0f);
        }
    }

    public void Apply_SignUp_Submit(Button btnSubmit) {
        btnSubmit.setText("rku;g;gpf;f");
        btnSubmit.setTypeface(font_tamil);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            btnSubmit.setTextSize(18.0f);
        }
    }
    // ============ Home Tamil===========//

    public void Apply_Settings(TextView txtTitle, TextView txtMerchant, TextView txtChangePassword,
                               TextView txtSelectLanguage, TextView txtScanReaders, TextView txtLog, TextView txtOrderTracking) {
        txtTitle.setText("mikg;Gfs;");
        txtTitle.setTypeface(font_tamil);

        txtMerchant.setText("flTr;nrhy;iy khw;Wf");
        txtMerchant.setTypeface(font_tamil);

        txtChangePassword.setText("Ratptu mikg;Gfs;");
        txtChangePassword.setTypeface(font_tamil);

        txtSelectLanguage.setText("nkhop Nju;e;njLf;Ff");
        txtSelectLanguage.setTypeface(font_tamil);

        txtScanReaders.setText("ml;il thrpg;ghd;");
        txtScanReaders.setTypeface(font_tamil);

        txtLog.setText("gjpTfs;");
        txtLog.setTypeface(font_tamil);

        txtOrderTracking.setText("nghUl;L  fz;fhzpg;G");
        txtOrderTracking.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtMerchant.setTextSize(16.0f);
            txtChangePassword.setTextSize(16.0f);
            txtSelectLanguage.setTextSize(16.0f);
            txtScanReaders.setTextSize(16.0f);
            txtLog.setTextSize(16.0f);
            txtOrderTracking.setTextSize(16.0f);
        }

    }

    public void Apply_Home(TextView txtTitle, Button btnSale, Button btnSettlement, Button btnVoid,
                           ImageButton ibhistory, ImageButton ibsettle_history) {
        txtTitle.setText("Kfg;G");
        txtTitle.setTypeface(font_tamil);

        btnSale.setText("tpw;gid");
        btnSale.setTypeface(font_tamil);

        btnSettlement.setText("nfhLg;gdT");
        btnSettlement.setTypeface(font_tamil);

        btnVoid.setText("jpwe;j gupkhw;wq;fs;");
        btnVoid.setTypeface(font_tamil);

        ibhistory.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transaction_history_tamil));
        ibsettle_history.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.settlement_history_tamil));

        if (screensize < 6.8) {
            txtTitle.setTextSize(22.0f); //

            btnSale.setTextSize(22.0f);
            btnSettlement.setTextSize(22.0f);
            btnVoid.setTextSize(22.0f);

        }

    }

    // ============ Void List Tamil===========//
    // by amal 2017-7-14
    public void Apply_VoidList(TextView txtType, String status) {

        if (status.equals("Sale")) {
            txtType.setText("tpw;gid");
        } else {
            txtType.setText("nry;Ygbaw;wJ");
        }

        if (screensize < 6.8) {
            txtType.setTextSize(14.0f);
        }

        txtType.setTypeface(font_tamil);
    }

    /*public void Apply_VoidList(TextView txtType) {

        if (txtType.getText().toString().equals("Sale")) {
            txtType.setText("tpw;gid");
            txtType.setTypeface(font_tamil);
        } else {
            txtType.setText("nry;Ygbaw;wJ");
            txtType.setTypeface(font_tamil);
        }

        if (screensize < 6.8) {
            txtType.setTextSize(14.0f);

        }
    }*/

    public void Apply_VoidListTitle(TextView txtTitle) {
        txtTitle.setText("jpwe;j gupkhw;wq;fs;");
        txtTitle.setTypeface(font_tamil);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
        }

    }

    public void Apply_TransactionListTitle(TextView txtTitle) {
        txtTitle.setText("gupkhw;wq;fs;");
        txtTitle.setTypeface(font_tamil);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
        }

    }

    // Tamil
    public void Apply_VoidDetails(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode,
                                  TextView lblSalesTime, Button btnVoid, Button btnSign, Button btnReceipt, TextView lblTrackingId) {
        // TODO Auto-generated method stub
        txtTitle.setText("nfhLf;fy; thq;fy; tpguk;");
        txtTitle.setTypeface(font_tamil);

        txtPaidWith.setText("gzk; %yk;");
        txtPaidWith.setTypeface(font_tamil);

        txtApprovalCode.setText("xg;Gjy; FwpaPL");
        txtApprovalCode.setTypeface(font_tamil);

        lblSalesTime.setText("tpw;gid jpfjp ");
        lblSalesTime.setTypeface(font_tamil);

        btnVoid.setText("nry;Ygbaw;wjhf;F");
        btnVoid.setTypeface(font_tamil);

        btnSign.setText("ifnahg;gk; ,lTk;");
        btnSign.setTypeface(font_tamil);

        btnReceipt.setText("gw;Wr;rPl;il mDg;Gf");
        btnReceipt.setTypeface(font_tamil);

        lblTrackingId.setText("mwpf;if ,yf;fk;");
        lblTrackingId.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(20.0f);
            txtApprovalCode.setTextSize(20.0f);
            btnVoid.setTextSize(20.0f);
            btnReceipt.setTextSize(20.0f);
            btnSign.setTextSize(20.0f);
            lblTrackingId.setTextSize(20.0f);
        }

    }

    public void Apply_VoidDetails_WithVoid(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode,
                                           TextView lblSalesTime, TextView lblVoidTime,
                                           Button btnVoid, Button btnSign, Button btnReceipt, TextView lblTrackingId) {
        // TODO Auto-generated method stub
        txtTitle.setText("nfhLf;fy; thq;fy; tpguk;");
        txtTitle.setTypeface(font_tamil);

        txtPaidWith.setText("gzk; %yk;");
        txtPaidWith.setTypeface(font_tamil);

        txtApprovalCode.setText("xg;Gjy; FwpaPL");
        txtApprovalCode.setTypeface(font_tamil);

        lblSalesTime.setText("tpw;gid jpfjp ");
        lblSalesTime.setTypeface(font_tamil);

        lblVoidTime.setText("nry;Ygbaw;w jpfjp ");
        lblVoidTime.setTypeface(font_tamil);

        btnVoid.setText("nry;Ygbaw;wjhf;F");
        btnVoid.setTypeface(font_tamil);

        btnSign.setText("ifnahg;gk; ,lTk;");
        btnSign.setTypeface(font_tamil);

        btnReceipt.setText("gw;Wr;rPl;il mDg;Gf");
        btnReceipt.setTypeface(font_tamil);

        lblTrackingId.setText("mwpf;if ,yf;fk;");
        lblTrackingId.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(20.0f);
            txtApprovalCode.setTextSize(20.0f);
            btnVoid.setTextSize(20.0f);
            btnReceipt.setTextSize(20.0f);
            btnSign.setTextSize(20.0f);
            lblTrackingId.setTextSize(20.0f);
        }

    }

    public void Apply_VoidErrorMessage(TextView txtTitle, TextView txtMessage, Button btnOk, Button btnCancel,
                                       String amount, int currencyType) {
        txtTitle.setText("cq;fs; mtjhdj;jpw;F");
        txtTitle.setTypeface(font_tamil);

        String CURRENCY = UtilityFunction.getCurrencyTypeStringTA(currencyType);

        txtMessage.setText(
                "ePq;fs; xU tpw;gidapid nry;Ygbaw;wjhf;f cs;sPu;fs;. ,e;j nray;ghl;bd; %yk; ePq;fs;  tpw;gid " + CURRENCY + " "
                        + amount
                        + " apid  ml;iljhuUf;F kPsj;jUfpwPu;fs;. ,e;j nray;ghl;il kWgbAk; khw;wKbahJ. ,e;j nray;ghl;il njhlu tpUk;gpUfpwPu;fsh ?");
        txtMessage.setTypeface(font_tamil);

        btnOk.setText("Mk;");
        btnOk.setTypeface(font_tamil);

        btnCancel.setText(",y;iy");
        btnCancel.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtMessage.setTextSize(16.0f);
            btnOk.setTextSize(18.0f);
            btnCancel.setTextSize(18.0f);

        }
    }

    // Tamil
    public void Apply_VoidPin(TextView txtTitle, EditText edtPin, Button btnSubmit) {
        txtTitle.setText("gupkhw;w tptuq;fs;"); // transaction details
        txtTitle.setTypeface(font_tamil);

        edtPin.setHint(",ufrpa vz;iz cs;splTk;");
        edtPin.setTypeface(font_tamil);

        btnSubmit.setText("rku;g;gpf;f"); // submit
        btnSubmit.setTypeface(font_tamil);

        if (screensize < 6.8) {
            edtPin.setTextSize(16.0f);
            btnSubmit.setTextSize(16.0f);

        }
    }

    // public String getIncorrect_PIN(String pin_message)
    // {
    // pin_message =
    //
    // return pin_message;
    // }

    public void Apply_VoidReceipt(TextView txtTitle, TextView txtVoidComplete, TextView txtlike, Button btnEmail,
                                  Button btnText, Button btnNoReceipt) {
        txtTitle.setText("urPJ");
        txtTitle.setTypeface(font_tamil);

        txtVoidComplete.setText("KOikahf  nry;Ygbaw;wpahfpAs;sJ");
        txtVoidComplete.setTypeface(font_tamil);

        txtlike.setText("cq;fSf;F urPJ Ntz;Lkh ?");
        txtlike.setTypeface(font_tamil);

        btnEmail.setText("kpd;dQ;ry;");
        btnEmail.setTypeface(font_tamil);

        btnText.setText("FWe;jfty;");
        btnText.setTypeface(font_tamil);

        btnNoReceipt.setText("Ntz;lhk;");
        btnNoReceipt.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtVoidComplete.setTextSize(18.0f);
            txtlike.setTextSize(16.0f);
            btnEmail.setTextSize(20.0f);
            btnText.setTextSize(20.0f);
            btnNoReceipt.setTextSize(20.0f);
        }
    }

    public void Apply_HistoryList(TextView txtTitle, EditText edtSearch) {
        txtTitle.setText("fhyf;NfhL");
        txtTitle.setTypeface(font_tamil);

        edtSearch.setHint("NjLf");
        edtSearch.setTypeface(font_tamil);

        if (screensize < 6.8) {
            edtSearch.setTextSize(14.0f);
            txtTitle.setTextSize(18.0f);
        }

    }

    // Tamil
    public void Apply_HistoryDetails(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode) {
        txtTitle.setText("fhyf;NfhL tptuk;");
        txtTitle.setTypeface(font_tamil);

        txtPaidWith.setText("gzk; %yk;");
        txtPaidWith.setTypeface(font_tamil);

        txtApprovalCode.setText("xg;Gjy; FwpaPL");
        txtApprovalCode.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(20.0f);
            txtApprovalCode.setTextSize(20.0f);
        }
    }

    // Tamil
    public void Apply_FilterHistory(TextView txtTitle, TextView txtFilterOptions, TextView txtStarts,
                                    TextView txtSelectDate, TextView txtEnds, TextView txtSelectEndDate,
                                    TextView txtCardType, TextView txtVisa, TextView txtMaster,
                                    TextView txtAmex, TextView txtDiners) {
        txtTitle.setText("fhyf;NfhL tbfl;ly;");
        txtTitle.setTypeface(font_tamil);

        txtFilterOptions.setText("fhyf;NfhL Nju;e;njLf;f");
        txtFilterOptions.setTypeface(font_tamil);

        txtStarts.setText("njhlf;f jpfjp"); // à®¤à¯Šà®Ÿà®•à¯�à®•
        // à®¤à®¿à®•à®¤à®¿
        txtStarts.setTypeface(font_tamil);

        txtEnds.setText(",Wjp jpfjp"); // à®‡à®±à¯�à®¤à®¿ à®¤à®¿à®•à®¤à®¿
        txtEnds.setTypeface(font_tamil);

        txtSelectDate.setHint("Nju;e;njLf;f");
        txtSelectDate.setTypeface(font_tamil);

        txtSelectEndDate.setHint("Nju;e;njLf;f");
        txtSelectEndDate.setTypeface(font_tamil);

        txtCardType.setText("ml;il tif ");
        txtCardType.setTypeface(font_tamil);


        txtVisa.setText("tprh");
        txtVisa.setTypeface(font_tamil);

        txtMaster.setText("kh];lu;fhu;L");
        txtMaster.setTypeface(font_tamil);

        txtAmex.setText("mnkupf;fd; vf;];gpu];");
        txtAmex.setTypeface(font_tamil);

        txtDiners.setText("ildu;]; fpsg;");
        txtDiners.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtFilterOptions.setTextSize(16.0f);
            txtStarts.setTextSize(16.0f);
            txtEnds.setTextSize(16.0f);
            txtSelectDate.setTextSize(16.0f);
            txtSelectEndDate.setTextSize(16.0f);
            txtCardType.setTextSize(16.0f);
            txtVisa.setTextSize(16.0f);
            txtMaster.setTextSize(16.0f);
            txtAmex.setTextSize(16.0f);
            txtDiners.setTextSize(16.0f);
        }
    }

    // Tamil
    public void Apply_Settlements(TextView txtTitle, TextView txtVisaSettlement, TextView txtMasterSettlement,
                                  TextView txtSummary, TextView txtTotalSettlements, TextView txtTotalAmount, TextView txtSettleNow) {
        txtTitle.setText("nfhLg;gdT");
        txtTitle.setTypeface(font_tamil);

        txtVisaSettlement.setText("nfhLg;gdTfs;");
        txtVisaSettlement.setTypeface(font_tamil);

        txtMasterSettlement.setText("nfhLg;gdTfs;");
        txtMasterSettlement.setTypeface(font_tamil);

        txtSummary.setText("RUf;fk;");
        txtSummary.setTypeface(font_tamil);

        txtTotalSettlements.setText("nkhj;j nfhLg;gdT");
        txtTotalSettlements.setTypeface(font_tamil);

        txtTotalAmount.setText("nkhj;j njhif");
        txtTotalAmount.setTypeface(font_tamil);

        txtSettleNow.setText("nfhLg;gdT  nra;f");
        txtSettleNow.setTypeface(font_tamil);

    }

    // Tamil
    public void Apply_Sale(TextView txtTitle, TextView txtReadCard, Button btnMoreOptions, Button btnClearOptions) {

        txtTitle.setText("tpw;gid");
        txtTitle.setTypeface(font_tamil);

//		txtReadCard.setText("ml;ilia thrpf;fTk;");
        txtReadCard.setText("ml;ilia thrpf;fTk;");
        txtReadCard.setTypeface(font_tamil);

        btnMoreOptions.setText("NkYk; fl;lzk; nrYj;Jk; Kiwfs;");
        btnMoreOptions.setTypeface(font_tamil);

        btnClearOptions.setText("mop");
        btnClearOptions.setTypeface(font_tamil);
    }

    // Tamil Insert/ Swipe
    public void Apply_Swipe(TextView txtTitle, TextView txtAmount) {
        txtTitle.setText("ml;ilia Nja;f;fTk; my;yJ cl;GFj;jTk;");
        txtTitle.setTypeface(font_tamil);

        txtAmount.setText("njhif");
        txtAmount.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(14.0f);

        }
    }

    // Tamil
    public void Apply_Swipe_Dialog(TextView txtTransaction, Button btnAddSignature) {
        txtTransaction.setText("cq;fs; gzg;gupkhw;wk; epiwTw;wJ");
        txtTransaction.setTypeface(font_tamil);

        btnAddSignature.setText("ifnahg;gk; ,lTk;");
        btnAddSignature.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTransaction.setTextSize(16.0f);

        }
    }

    public void Apply_Signature(TextView txtAmount, TextView txtSignHere, Button btnSave,
                                Button btnClear, Button btnApproved, String name, String card_no, String code) {
        txtSignHere.setText("ifnahg;gk; ,lTk;"); // ifnaà®¾g;gk; ,lTk;
        txtSignHere.setTypeface(font_tamil);

        txtAmount.setText("njhif");
        txtAmount.setTypeface(font_tamil);

        /*txtConfirmation.setText(
                "ehd; vdJ ml;il thbf;ifahsu; xg;ge;jj;jpw;F mikthf NkNy cs;s nkhj;j njhifia nrYj;j xg;Gf;nfhs;fpNwd;. ml;ilapd; KbT ,yf;fq;fs; "
                        + card_no + " mq;fPfhu mDkjp FwpaPL " + code);

        txtConfirmation.setTypeface(font_tamil);
        txtConfirmation.setText(txtConfirmation.getText().toString());*/

        btnSave.setText("rku;g;gpf;f");
        btnSave.setTypeface(font_tamil);

        btnClear.setText("ifnahg;gk; ePf;Ff");
        btnClear.setTypeface(font_tamil);

        btnApproved.setText("mq;fpfupf;fgl;lJ");
        btnApproved.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtSignHere.setTextSize(36.0f);
            txtAmount.setTextSize(14.0f);
            //txtConfirmation.setTextSize(12.0f);
            btnSave.setTextSize(16.0f);
            btnClear.setTextSize(13.0f);
            btnApproved.setTextSize(11.0f);
        } else {
            txtSignHere.setTextSize(86.0f);
            txtAmount.setTextSize(20.0f);
            //txtConfirmation.setTextSize(22.0f);
            btnSave.setTextSize(26.0f);
            btnClear.setTextSize(18.0f);
            btnApproved.setTextSize(16.0f);
        }

    }

    // Tamil
    public void Apply_Receipt(TextView txtTitle, TextView txtSignatureSave, TextView txtlike, Button btnEmail,
                              Button btnText, Button btnNoReceipt) {

        txtTitle.setText("urPJ");
        txtTitle.setTypeface(font_tamil);

        txtSignatureSave.setText("ifnahg;gk; Vw;Wf; nfhs;sg;gl;lJ");
        txtSignatureSave.setTypeface(font_tamil);

        txtlike.setText("cq;fSf;F urPJ Ntz;Lkh ?");
        txtlike.setTypeface(font_tamil);

        btnEmail.setText("kpd;dQ;ry;");
        btnEmail.setTypeface(font_tamil);

        btnText.setText("FWe;jfty;");
        btnText.setTypeface(font_tamil);

        btnNoReceipt.setText("Ntz;lhk;");
        btnNoReceipt.setTypeface(font_tamil);

        ////////////////////////////////////////////////////////////////

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtSignatureSave.setTextSize(22.0f);
            txtlike.setTextSize(18.0f);
            btnEmail.setTextSize(20.0f);
            btnText.setTextSize(20.0f);
            btnNoReceipt.setTextSize(20.0f);
        }
    }

    // Tamil
    public void Apply_SendEmail(TextView txtTitle, TextView txtPaymentDetails, TextView txtAmount, TextView txtCardName,
                                TextView txtApprovalCode, TextView txtSendVia, EditText edtEmail,
                                Button btnSend, TextView txtAmountValue) {
        txtTitle.setText("kpd;dQ;ry; mDg;Gf");
        txtTitle.setTypeface(font_tamil);

        txtPaymentDetails.setText("fl;lz tptuq;fs;");
        txtPaymentDetails.setTypeface(font_tamil);

        txtAmount.setText("njhif");
        txtAmount.setTypeface(font_tamil);

        txtCardName.setText("ml;il ngau;");
        txtCardName.setTypeface(font_tamil);

        txtApprovalCode.setText("xg;Gjy; FwpaPL");
        txtApprovalCode.setTypeface(font_tamil);

        txtSendVia.setText("kpd;dQ;ry; topahf mDg;gy; ");
        txtSendVia.setTypeface(font_tamil);

        edtEmail.setHint("kpd;dQ;riy gjpTnra;f");
        edtEmail.setTypeface(font_tamil);

        txtAmountValue.setTypeface(font_tamil);

        btnSend.setText("mDg;Gf");
        btnSend.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtPaymentDetails.setTextSize(22.0f);
            txtAmount.setTextSize(18.0f);
            txtCardName.setTextSize(18.0f);
            txtApprovalCode.setTextSize(18.0f);
            txtSendVia.setTextSize(16.0f);
            edtEmail.setTextSize(16.0f);
            txtAmountValue.setTextSize(18.0f);
        }

    }

    public void Apply_SendText(TextView txtTitle, TextView txtPaymentDetails, TextView txtAmount, TextView txtCardName,
                               TextView txtApprovalCode, TextView txtSendVia, EditText edtPhone,
                               Button btnSend, TextView txtAmountValue) {
        txtTitle.setText("FWe;jfty; mDg;Gf");
        txtTitle.setTypeface(font_tamil);

        txtPaymentDetails.setText("fl;lz tptuq;fs;");
        txtPaymentDetails.setTypeface(font_tamil);

        txtAmount.setText("njhif");
        txtAmount.setTypeface(font_tamil);

        txtCardName.setText("ml;il ngau;");
        txtCardName.setTypeface(font_tamil);

        txtApprovalCode.setText("xg;Gjy; FwpaPL");
        txtApprovalCode.setTypeface(font_tamil);

        txtSendVia.setText("FWe;jfty; topahf mDg;gy;");
        txtSendVia.setTypeface(font_tamil);

        edtPhone.setHint("njhiyNgrp vz;iz gjpTnra;f");
        edtPhone.setTypeface(font_tamil);

        btnSend.setText("mDg;Gf");
        btnSend.setTypeface(font_tamil);
        txtAmountValue.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtPaymentDetails.setTextSize(22.0f);
            txtAmount.setTextSize(18.0f);
            txtCardName.setTextSize(18.0f);
            txtApprovalCode.setTextSize(18.0f);
            txtSendVia.setTextSize(16.0f);
            edtPhone.setTextSize(14.0f);
            txtAmountValue.setTextSize(18.0f);
        }
    }

    public void Apply_LastDigit(TextView txtTitle, TextView txtPleaseEnter, EditText edtDigit, Button btnSubmit) {
        txtTitle.setText("filrp ,yf;fq;fs;");
        txtTitle.setTypeface(font_tamil);

        txtPleaseEnter.setText("jaT nra;J cq;fs; ml;ilapd;  ,Wjp 4 ,yf;fq;fis gjpT nra;f  ");
        txtPleaseEnter.setTypeface(font_tamil);

        edtDigit.setHint(",Wjp 4 ,yf;fq;fis gjpT nra;f ");
        edtDigit.setTypeface(font_tamil);

        btnSubmit.setText("rku;g;gpf;f");
        btnSubmit.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);

            txtPleaseEnter.setTextSize(16.0f);
            edtDigit.setTextSize(18.0f);
            btnSubmit.setTextSize(18.0f);

        }

    }

    // Settlement List

    public void Apply_SettlementList(TextView txtTitle) {
        txtTitle.setText("nfhLg;gdTfs; fhyf;NfhL ");
        txtTitle.setTypeface(font_tamil);
    }

    public void Apply_SettlementListData(TextView txtSales, TextView txtVoids) {
        txtSales.setText("tpw;gidfs;");
        txtSales.setTypeface(font_tamil);

        txtVoids.setText("nry;Ygbaw;wit");
        txtVoids.setTypeface(font_tamil);

        if (screensize < 6.8) {

            txtSales.setTextSize(16.0f);
            txtVoids.setTextSize(16.0f);

        }
    }

    // Settlement History Details

    public void Apply_SettlementHistoryDetails(String salescount, String voidcount, TextView txtSalesCount,
                                               TextView txtVoidsCount, Button btnViewTransactions, TextView txtSales,
                                               TextView txtVoids, String selectedCurrencyType) {
        txtSalesCount.setText("tpw;gidfs; " + salescount);
        txtSalesCount.setTypeface(font_tamil);

        txtVoidsCount.setText("nry;Ygbaw;wit " + voidcount);
        txtVoidsCount.setTypeface(font_tamil);

        btnViewTransactions.setText("gupkhw;wq;fis fhz;f");
        btnViewTransactions.setTypeface(font_tamil);

        txtSales.setText("tpw;gidfs;" + " - " + selectedCurrencyType);
        txtSales.setTypeface(font_tamil);

        txtVoids.setText("nry;Ygbaw;wit" + " - " + selectedCurrencyType);
        txtVoids.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtSalesCount.setTextSize(14.0f);
            txtVoidsCount.setTextSize(14.0f);

            txtSales.setTextSize(14.0f);
            txtVoids.setTextSize(14.0f);

        }

    }

    public void Apply_OrderTracking(TextView txtTitle, EditText edtOrder, Button btnContinue) {
        txtTitle.setText("nghUl;L  fz;fhzpg;G");
        txtTitle.setTypeface(font_tamil);

        edtOrder.setHint("nghUl;L fz;fhzpg;G ,yf;fj;ij cs;sPLf");
        edtOrder.setTypeface(font_tamil);

        btnContinue.setText("njhlu;f"); // rku;g;gpf;f
        btnContinue.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            edtOrder.setTextSize(16.0f);
            btnContinue.setTextSize(16.0f);

        }

    }

    public void Apply_OrderTracking_Ordertext(EditText edtOrder) {

        edtOrder.setHint("nghUl;L fz;fhzpg;G ,yf;fj;ij cs;sPLf");
        edtOrder.setTypeface(font_tamil);

        if (screensize < 6.8) {

            edtOrder.setTextSize(16.0f);

        }

    }

    public void Apply_Settings(TextView txtTitle, TextView txtChangePassword,
                               TextView txtMerchant, TextView txtCheckout, TextView txtSelectLanguage, TextView txtOrderTracking,
                               TextView txtHardware, TextView txtDeviceInfo, Button btnLogout) {
        txtTitle.setText("mikg;Gfs;");
        txtTitle.setTypeface(font_tamil);

        txtChangePassword.setText("flTr;nrhy;iy khw;Wf");
        txtChangePassword.setTypeface(font_tamil);

        txtMerchant.setText("Ratptuk;");
        txtMerchant.setTypeface(font_tamil);

        txtCheckout.setText("tpNrl mikg;Gfs;");
        txtCheckout.setTypeface(font_tamil);

        txtSelectLanguage.setText("nkhop Nju;e;njLf;Ff");
        txtSelectLanguage.setTypeface(font_tamil);

        txtOrderTracking.setText("nghUl;L  fz;fhzpg;G");
        txtOrderTracking.setTypeface(font_tamil);

        txtHardware.setText("td;nghUs;");
        txtHardware.setTypeface(font_tamil);

        txtDeviceInfo.setText("ifnjhiyNgrp  tptuq;fs;");
        txtDeviceInfo.setTypeface(font_tamil);

        btnLogout.setText("ntspNaW");
        btnLogout.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtChangePassword.setTextSize(16.0f);
            txtMerchant.setTextSize(16.0f);
            txtCheckout.setTextSize(16.0f);
            txtSelectLanguage.setTextSize(16.0f);
            txtHardware.setTextSize(16.0f);
            txtOrderTracking.setTextSize(16.0f);
            txtDeviceInfo.setTextSize(16.0f);
            btnLogout.setTextSize(16.0f);

        }

    }

    public void Apply_ChangePassword(TextView txtTitle, EditText edtCurrentPassword, EditText edtNewPass,
                                     EditText edtRePass, Button btnSubmit) {
        txtTitle.setText("flTr;nrhy;iy khw;Wf");
        txtTitle.setTypeface(font_tamil);

        edtCurrentPassword.setHint("jw;Nghija flTr;nrhy;");
        edtCurrentPassword.setTypeface(font_tamil);

        edtNewPass.setHint("Gjpa flTr;nrhy;");
        edtNewPass.setTypeface(font_tamil);

        edtRePass.setHint("cWjpahd flTr;nrhy;");
        edtRePass.setTypeface(font_tamil);

        btnSubmit.setText("rku;g;gpf;f");
        btnSubmit.setTypeface(font_tamil);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            edtCurrentPassword.setTextSize(16.0f);
            edtNewPass.setTextSize(16.0f);
            edtRePass.setTextSize(16.0f);
            btnSubmit.setTextSize(18.0f);

        }
    }

    public void Apply_ChangePassword_CurrentPass(EditText edtCurrentPassword) {
        edtCurrentPassword.setHint("jw;Nghija flTr;nrhy;");
        edtCurrentPassword.setTypeface(font_tamil);

        if (screensize < 6.8) {

            edtCurrentPassword.setTextSize(16.0f);

        }
    }

    public void Apply_ChangePassword_NewPass(EditText edtNewPass) {
        edtNewPass.setHint("Gjpa flTr;nrhy;");
        edtNewPass.setTypeface(font_tamil);

        if (screensize < 6.8) {

            edtNewPass.setTextSize(16.0f);

        }
    }

    public void Apply_ChangePassword_RePass(EditText edtRePass) {
        edtRePass.setHint("cWjpahd flTr;nrhy;");
        edtRePass.setTypeface(font_tamil);

        if (screensize < 6.8) {

            edtRePass.setTextSize(16.0f);

        }
    }

    // TextView lblUserName, lblBusinessName, lblCoporateAddress, lblMerchantId,
    // lblTerminalId, lblAppVersion, lblCardType,
    // lblAmount;

    public void Apply_MerchantProfile(TextView txtTitle, TextView lblUserName, TextView lblBusinessName,
                                      TextView lblCoporateAddress, TextView lblMerchantId, TextView lblTerminalId, TextView lblAppVersion,
                                      TextView lblCardType, TextView lblAmount) {

        txtTitle.setText("Ratptuk;");
        txtTitle.setTypeface(font_tamil);

        lblUserName.setText("gadu; ngau;");
        lblUserName.setTypeface(font_tamil);

        lblBusinessName.setText("tpahghu ngau;");
        lblBusinessName.setTypeface(font_tamil);

        lblCoporateAddress.setText("epWtd Kftup");
        lblCoporateAddress.setTypeface(font_tamil);

        lblMerchantId.setText("tpahghup milahs vz;");
        lblMerchantId.setTypeface(font_tamil);

        lblTerminalId.setText("nlu;kpdy; milahs vz;");
        lblTerminalId.setTypeface(font_tamil);

        lblAppVersion.setText("gjpg;G");
        lblAppVersion.setTypeface(font_tamil);

        lblCardType.setText("fld; ml;il rhjd tif");
        lblCardType.setTypeface(font_tamil);

        lblAmount.setText("mjpfgl;r njhif");
        lblAmount.setTypeface(font_tamil);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
            lblUserName.setTextSize(14.0f);
            lblBusinessName.setTextSize(14.0f);
            lblCoporateAddress.setTextSize(14.0f);
            lblMerchantId.setTextSize(14.0f);
            lblTerminalId.setTextSize(14.0f);
            lblAppVersion.setTextSize(14.0f);
            lblCardType.setTextSize(14.0f);
            lblAmount.setTextSize(14.0f);

        }

    }

    public void Apply_ChangeLanguage(TextView txtTitle, TextView txtEnglish, TextView txtSinhala, TextView txtTamil) {
        txtTitle.setText("nkhop Nju;e;njLf;Ff");
        txtTitle.setTypeface(font_tamil);

        // txtEnglish.setText("Mq;fpyk;");
        // txtEnglish.setTypeface(font_tamil);
        //
        // txtSinhala.setText("rpq;fsk;");
        // txtSinhala.setTypeface(font_tamil);
        //
        // txtTamil.setText("jkpo;");
        // txtTamil.setTypeface(font_tamil);
    }

    public void Apply_ShowLanguage(TextView txtTitle) {
        txtTitle.setText("jkpOf;F nkhopkhw;wk; nra;ag;gl;Ls;sJ ");
        txtTitle.setTypeface(font_tamil);
    }

    public void Apply_ShowLanguage_Validation(TextView txtTitle) {
        txtTitle.setText("NtW nkhop Nju;T nra;f");
        txtTitle.setTypeface(font_tamil);
    }


    public void apply_bluetoothscan(TextView txtHeader, Button btnScan, Button btnCancel) {
        txtHeader.setText("];Nfd; rhjdq;fs;");
        txtHeader.setTypeface(font_tamil);

        btnScan.setText("];Nfd;");
        btnScan.setTypeface(font_tamil);

        btnCancel.setText("uj;J nra;f");
        btnCancel.setTypeface(font_tamil);

        if (screensize < 6.8) {

            txtHeader.setTextSize(14.0f);
            btnScan.setTextSize(14.0f);
            btnCancel.setTextSize(14.0f);
        }
    }

    public void apply_scan_tv_status(TextView tv_status, int num) {
        if (num == 0) // Scanning Devices, Please wait...
        {

            tv_status.setText("rhjdq;fs; ];Nfd; nra;ag;gLfpd;wd. rw;W nghWj;jpUq;fs;.");
            tv_status.setTypeface(font_tamil);

        }

        if (num == 1) // Reader is registered with app successfully
        {
            tv_status.setText("ntw;wpfukhf uPlu;  gjpT nra;ag;gl;Ls;sJ");
            tv_status.setTypeface(font_tamil);
        }

        if (num == 2) // Reader not found.Please turn on your reader
        {
            tv_status.setText("uPliu fz;Lgpbf;fKbatpy;iy . cq;fs; uPliu  Md; nra;Aq;fs;");
            tv_status.setTypeface(font_tamil);
        }

        if (num == 3) // Bluetooth is turned off.Please turn it on
        {
            tv_status.setText("jaTnra;J gS^j;ij Md; nra;A+q;fs;.");
            tv_status.setTypeface(font_tamil);
        }

        if (num == 4) // Bluetooth is unsupported by this device
        {
            tv_status.setText("gS^j; ,e;j rhjdj;jpw;F MjuT ,y;iy.");
            tv_status.setTypeface(font_tamil);
        }

        if (screensize < 6.8) {

            tv_status.setTextSize(14.0f);
        }

    }


    // ================== Progress Dialog Messages ======================//

    public String Pro_Signingin(String signin) {

        return signin;
    }

    public double getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        return screenInches;
    }

    public void enterCardDetails(TextView tittle, TextView txtAmount, TextView txtEnter, Button btnCharge) {
        tittle.setText("ml;il tpguq;fis gjpaTk;");
        tittle.setTypeface(font_tamil);

        txtAmount.setText("njhif ");
        txtAmount.setTypeface(font_tamil);

        txtEnter.setText("ml;il ,yf;fk; kw;Wk; fhyhtjp jpfjpapid gjpaTk;");
        txtEnter.setTypeface(font_tamil);

        btnCharge.setText("mwtpLf");
        btnCharge.setTypeface(font_tamil);

        if (screensize < 6.8) {

            tittle.setTextSize(18.0f);
            txtAmount.setTextSize(25.0f);
            btnCharge.setTextSize(25.0f);
        }
    }

    public void selectPaymentMethod(TextView tvTitle, TextView tvAmmou, TextView tvReadCard,
                                    TextView tvEnterCard, TextView tvAmmouValue) {
        tvTitle.setText("nfhLg;gdT Kiwiaj; njupTnra;f");
        tvTitle.setTypeface(font_tamil);

        tvAmmou.setText("njhif ");
        tvAmmou.setTypeface(font_tamil);

        tvReadCard.setText("ml;ilia thrp");
        tvReadCard.setTypeface(font_tamil);

        tvEnterCard.setText("ml;il tpguq;fis gjpaTk;");
        tvEnterCard.setTypeface(font_tamil);
        tvAmmouValue.setTypeface(font_tamil);

        if (screensize < 6.8) {
            tvTitle.setTextSize(22.0f);
            tvAmmou.setTextSize(22.0f); //
            tvReadCard.setTextSize(22.0f);
            tvEnterCard.setTextSize(22.0f);
            tvAmmouValue.setTextSize(22.0f);
        }

    }

    public void Apply_Approval_Tamil(TextView txtTitle, TextView txApproved, TextView txtTxCompleted,
                                     TextView txtSigNotRqrd, TextView txtCardEndingTxt, TextView txtAuthCodeTxt, Button btnProceed) {


        txtTitle.setText("mq;fPfupf;fg;gl;lJ");
        txtTitle.setTypeface(font_tamil, Typeface.BOLD);

        txApproved.setText("njhif");
        txApproved.setTypeface(font_tamil, Typeface.BOLD);

        txtTxCompleted.setText("gupkhw;wk; G+u;j;jpnra;ag;gl;lJ");
        txtTxCompleted.setTypeface(font_tamil);

        txtSigNotRqrd.setText("ifnahg;gk; mtrpakpy;iy");
        txtSigNotRqrd.setTypeface(font_tamil);

        txtCardEndingTxt.setText("ml;il ,Wjp ,yf;fk;");
        txtCardEndingTxt.setTypeface(font_tamil);

        txtAuthCodeTxt.setText("mq;fPfhu FwpaPL");
        txtAuthCodeTxt.setTypeface(font_tamil);

        btnProceed.setText("njhlu;f");
        btnProceed.setTypeface(font_tamil, Typeface.BOLD);

        if (screensize < 6.8) {
            txtTitle.setTextSize(25.0f);
            txApproved.setTextSize(22.0f);
            txtTxCompleted.setTextSize(18.0f);
            txtSigNotRqrd.setTextSize(18.0f);
            txtCardEndingTxt.setTextSize(15.0f);
            txtAuthCodeTxt.setTextSize(15.0f);
            btnProceed.setTextSize(25.0f);
        }

    }

    public void Apply_Rejected_Tamil(TextView txtTitle, TextView txtAmountTxt, TextView txtCardName,
                                     TextView txtErrorCodetxt, Button btnProceed) {
        txtTitle.setText("epuhfupf;fg;gl;lJ");
        txtTitle.setTypeface(font_tamil, Typeface.BOLD);

        txtAmountTxt.setText("njhif");
        txtAmountTxt.setTypeface(font_tamil);

        txtCardName.setText("gupkhw;wk; G+u;j;jpnra;ag;gl;ltpy;iy!");
        txtCardName.setTypeface(font_tamil);

        txtErrorCodetxt.setText("gpioahd FwpaPL");
        txtErrorCodetxt.setTypeface(font_tamil);

        btnProceed.setText("njhlu;f");
        btnProceed.setTypeface(font_tamil, Typeface.BOLD);

        if (screensize < 6.8) {
            txtTitle.setTextSize(25.0f);
            txtAmountTxt.setTextSize(22.0f);
            txtCardName.setTextSize(18.0f);
            txtErrorCodetxt.setTextSize(18.0f);
            btnProceed.setTextSize(25.0f);
        }

    }

    public void paymentOption(TextView titleTV, TextView selectinstallmentTV, TextView insta_12,
                              TextView insta_24, TextView insta_36, TextView otherTv,
                              EditText instalmentET, TextView txtTransaction, Button btnAddSignature) {
        titleTV.setText("nfhLg;gdT Kiwfs;"); //payment option
        selectinstallmentTV.setText("jtiz Kiw jpl;lj;ijj; njupT nra;f"); //select installment
        insta_12.setText("jtizKiw"); //12 installment
        insta_24.setText("jtizKiw"); //24 installment
        insta_36.setText("jtizKiw"); //36 installment
        otherTv.setText("NtW"); //other
        instalmentET.setHint("jtiz Kiwfspd; vz;zpf;if"); //enter no of installment
        txtTransaction.setText("ehza myifj; njupT nra;f"); // select currency
        btnAddSignature.setText("njupT nra;f");// select

        titleTV.setTypeface(font_tamil);
        selectinstallmentTV.setTypeface(font_tamil);
        insta_12.setTypeface(font_tamil);
        insta_24.setTypeface(font_tamil);
        insta_36.setTypeface(font_tamil);
        otherTv.setTypeface(font_tamil);
        instalmentET.setTypeface(font_tamil);
        txtTransaction.setTypeface(font_tamil);
        btnAddSignature.setTypeface(font_tamil);
    }

    public void apply_Installemt(Button btnMoreOptions, StringBuilder stringBuilder,
                                 int mInstallments, int mCurrencyType, double valDisplay) {
        switch (mCurrencyType) {
            case Consts.LKR:
                stringBuilder.append("&gh ");
                break;
            case Consts.USD:
                stringBuilder.append("m.nlhyu; ");
                break;
            case Consts.GBP:
                stringBuilder.append("gTz;l; ");
                break;
            case Consts.EUR:
                stringBuilder.append("A+Nuh ");
                break;
            default:
                stringBuilder.append("&gh ");
        }
        stringBuilder.append(String.valueOf(valDisplay));
        stringBuilder.append(" _ ");
        stringBuilder.append(String.valueOf(mInstallments)).append(" jtizKiw");
        btnMoreOptions.setTypeface(font_tamil);
    }

    public void apply_more_payment(Button btnMoreOptions) {
        btnMoreOptions.setText("NkYk; fl;lzk; nrYj;Jk; Kiwfs;");
        btnMoreOptions.setTypeface(font_tamil);
    }

    public void setInstallment(TextView insTv, String status) {
        insTv.setText("jt - " + status);
        insTv.setTypeface(font_tamil);
        insTv.setTextSize(12.0f);
    }

    public void Apply_Secondary_Settings(TextView txtTitle, TextView txtCaption, TextView txtVisa, TextView txtAmex) {

        txtTitle.setTypeface(font_tamil);
        txtCaption.setTypeface(font_tamil);
        txtVisa.setTypeface(font_tamil);
        txtAmex.setTypeface(font_tamil);

        txtTitle.setText("mikg;Gfs;");
        txtCaption.setText("ml;il tifia njupTnra;f");
        txtVisa.setText("tprh - kh];lu;fhu;L");
        txtAmex.setText("mnk]; - ildu;]; fpsg;");

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtCaption.setTextSize(16.0f);
            txtVisa.setTextSize(16.0f);
            txtAmex.setTextSize(16.0f);
        }
    }
}
