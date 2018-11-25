package com.mpos.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
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

public class Sinhala_LangSelect {

    Context context;
    Activity activity;
    static Typeface font_sinhala, font_english;

    double screensize;

    // Sign In Variables
    // static TextView txtSignIn;
    // static EditText edtEmail,edtPassword;
    // static Button btnSignIn;

    public Sinhala_LangSelect(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        font_sinhala = Typeface.createFromAsset(context.getAssets(), "Sinhala_Font/dlmanel.TTF");
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

        txtTitle.setText("msh��");
        txtTitle.setTypeface(font_sinhala);

        /*firstWord = "VISA/MASTERCARD ";
        secondWord = "msh��";

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

        txtTitle1.setTypeface(font_sinhala);
        txtTitle1.setText("�id$udiag� msh��");

        /*firstWord = "AMEX/DINERS CLUB ";
        secondWord = "msh��";

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/


        txtTitle2.setText("wefulaia$vhsk�ia la,� msh��");
        txtTitle2.setTypeface(font_sinhala);

        txtTitle3.setText("idrdxYh");
        txtTitle3.setTypeface(font_sinhala);

        txtVisaSettlement.setText("msh��");
        txtVisaSettlement.setTypeface(font_sinhala);

        txtMasterSettlement.setText("msh��");
        txtMasterSettlement.setTypeface(font_sinhala);

        txtAmexSettlement.setText("msh��");
        txtAmexSettlement.setTypeface(font_sinhala);

        txtDinersSettlement.setText("msh��");
        txtDinersSettlement.setTypeface(font_sinhala);

        txtTotalSettlements.setText("msh��j, tl;=j");
        txtTotalSettlements.setTypeface(font_sinhala);

        txtTotalAmount.setText("uq� uqo,");
        txtTotalAmount.setTypeface(font_sinhala);

        txtSettleNow.setText("oeka mshjkak");
        txtSettleNow.setTypeface(font_sinhala);

    }

    public void Apply_Settlements_2(TextView txtTitle1, boolean isVMLoggedIn, boolean isAmexLoggedIn) {

        if (isVMLoggedIn) {
            txtTitle1.setText("�id$udiag� msh��");
        } else if (isAmexLoggedIn) {
            txtTitle1.setText("wefulaia$vhsk�ia la,� msh��");
        }

        txtTitle1.setTypeface(font_sinhala);


        /*String firstWord = "";
        String secondWord = "";

        Spannable spannable;
        float textSize = 25.0f;

        if (isVMLoggedIn) {
            firstWord = "VISA/MASTERCARD ";
            secondWord = "msh��";
        } else if (isAmexLoggedIn) {
            firstWord = "AMEX/DINERS CLUB ";
            secondWord = "msh��";
        }

        spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle1.setText(spannable);*/

    }

    // ===========Sign In==========//
    public void Apply_Settings_2(TextView txtLogout1, TextView txtLogout2, TextView txtLogIn, boolean isVMLoggedIn,
                                 boolean isAmexLoggedIn) {

        String firstWord;
        String secondWord;

        if (isVMLoggedIn && isAmexLoggedIn) {
            System.out.println("LOGIN both");
            txtLogout1.setText("bj;aj hkak");
            txtLogout2.setText("bj;aj hkak");

            /*firstWord = "bj;ajhkak ";
            secondWord = "AMEX/DINERS CLUB";

            spannable = new SpannableString(firstWord + secondWord);

            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



            firstWord = "bj;ajhkak ";
            secondWord = "VISA/MASTERCARD";

            // Create a new spannable with the two strings
            spannable = new SpannableString(firstWord + secondWord);

            // Set the custom typeface to span over a section of the spannable object
            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the text of a textView with the spannable object
            txtLogout2.setText(spannable);*/
        } else {
            if (isVMLoggedIn) {

                System.out.println("LOGIN visa");
                txtLogout1.setText("bj;aj hkak");
                txtLogIn.setText("we;=,a jkak");

                /*firstWord = "bj;ajhkak ";
                secondWord = "VISA/MASTERCARD";

                // Create a new spannable with the two strings
                spannable = new SpannableString(firstWord + secondWord);

                // Set the custom typeface to span over a section of the spannable object
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set the text of a textView with the spannable object
                txtLogout1.setText(spannable);

                firstWord = "we;=,a jkak ";
                secondWord = "AMEX/DINERS CLUB";

                // Create a new spannable with the two strings
                spannable = new SpannableString(firstWord + secondWord);

                // Set the custom typeface to span over a section of the spannable object
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set the text of a textView with the spannable object
                txtLogIn.setText(spannable);*/
            } else if (isAmexLoggedIn) {
                /*firstWord = "bj;ajhkak";
                secondWord = "AMEX/DINERS CLUB";

                // Create a new spannable with the two strings
                spannable = new SpannableString(firstWord + secondWord);

                // Set the custom typeface to span over a section of the spannable object
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set the text of a textView with the spannable object
                txtLogout1.setText(spannable);

                firstWord = "we;=,a jkak ";
                secondWord = "VISA/MASTERCARD";

                // Create a new spannable with the two strings
                spannable = new SpannableString(firstWord + secondWord);

                // Set the custom typeface to span over a section of the spannable object
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textsize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textsize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set the text of a textView with the spannable object
                txtLogIn.setText(spannable);*/

                System.out.println("LOGIN amex");
                txtLogout1.setText("bj;aj hkak");
                txtLogIn.setText("we;=,a jkak");
            }
        }

        txtLogout1.setTypeface(font_sinhala);
        txtLogout2.setTypeface(font_sinhala);
        txtLogIn.setTypeface(font_sinhala);

        txtLogout1.setTextSize(16.0f);
        txtLogout2.setTextSize(16.0f);
        txtLogIn.setTextSize(16.0f);

    }

    public void Apply_Secondary_SignUp_Title(TextView txtTitle) {
        String firstWord = "ms�iSu ";
        String secondWord = "AMEX/DINERS CLUB";

        float textSize = 25.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTitle.setText(spannable);

    }

    public void Apply_Secondary_SignUp_Amex_Text(TextView amexTxt) {
        String firstWord = "fuu ms�iSu ";
        String secondWord = "AMEX ";
        String thirdWord = "iy ";
        String fourthWord = "Diners Club ";
        String fifthWord = "ld� m;a ys�hkag";

        float textSize = 30.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord + thirdWord + fourthWord + fifthWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), firstWord.length() + secondWord.length(), firstWord.length() + secondWord.length() + thirdWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length() + secondWord.length() + thirdWord.length(), firstWord.length() + secondWord.length() + thirdWord.length() + fourthWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), firstWord.length() + secondWord.length() + thirdWord.length() + fourthWord.length(), firstWord.length() + secondWord.length() + thirdWord.length() + fourthWord.length() + fifthWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        amexTxt.setText(spannable);
    }

    public void Apply_SignIn_Amex_Sinhala(TextView txtSignIn, EditText edtEmail, EditText edtPassword, EditText edtBank,
                                          Button btnSignIn, TextView txtSignUp, TextView settings) {
        txtSignIn.setText("ms�fikak");
        txtSignIn.setTypeface(font_sinhala);

        edtEmail.setHint("m�YS,lhdf.a ku");
        edtEmail.setTypeface(font_sinhala);

        edtPassword.setHint("uqr moh");
        edtPassword.setTypeface(font_sinhala);

        edtBank.setHint("nexl= flaa;h");
        edtBank.setTypeface(font_sinhala);

        btnSignIn.setText("ms�fikak");
        btnSignIn.setTypeface(font_sinhala);

        String firstWord = "wh�� lrkak ";
        String secondWord = "AMEX/DINERS CLUB";

        float textSize = 22.0f;

        Spannable spannable = new SpannableString(firstWord + secondWord);

        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_sinhala, textSize), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("sans-serif", font_english, textSize), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtSignUp.setText(spannable);

        settings.setText("kej; ieliS� fj; hkak");
        settings.setTypeface(font_sinhala);

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

    public void Apply_SignIn_Sinhala(TextView txtSignIn, EditText edtEmail, EditText edtPassword,
                                     EditText edtBank, Button btnSignIn, TextView txtSignUp, Button btnSettings) {
        txtSignIn.setText("ms�fikak");
        txtSignIn.setTypeface(font_sinhala);

        edtEmail.setHint("m�YS,lhdf.a ku");
        edtEmail.setTypeface(font_sinhala);

        edtPassword.setHint("uqr moh");
        edtPassword.setTypeface(font_sinhala);

        edtBank.setHint("nexl= flaa;h");
        edtBank.setTypeface(font_sinhala);

        btnSignIn.setText("ms�fikak");
        btnSignIn.setTypeface(font_sinhala);

        String signuptext = "<font color=#C1C1C1>Tng fkd�f,aa ,efnk</font> <font color=#8C8D8E> ld� �vrh wh�� lrkak'</font>";

        // <font color=#cc0029>Erste Farbe</font> <font color=#ffcc00>zweite Farbe</font>"

        //	txtSignUp.setText("Tng fkd�f,aa ,efnk ld� �vrh wh�� lrkak'");
        txtSignUp.setText(Html.fromHtml(signuptext));
        txtSignUp.setTypeface(font_sinhala);

        btnSettings.setText("kej; ieliS� fj; hkak");
        btnSettings.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtEmail.setTextSize(18.0f);
            edtPassword.setTextSize(18.0f);
            edtBank.setTextSize(18.0f);
            btnSignIn.setTextSize(20.0f);
            txtSignUp.setTextSize(16.0f);
            btnSettings.setTextSize(20.f);
        }
    }


    public void Apply_SignIn_Email(EditText edtEmail) {
        edtEmail.setHint("m�YS,lhdf.a ku");
        edtEmail.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtEmail.setTextSize(18.0f);

        }
    }

    public void Apply_SignIn_Password(EditText edtPassword) {
        edtPassword.setHint("uqr moh");
        edtPassword.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtPassword.setTextSize(18.0f);

        }
    }

    public void Apply_SignIn_Bank(EditText edtBank) {
        edtBank.setHint("nexl= flaa;h");
        edtBank.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            edtBank.setTextSize(18.0f);

        }
    }


    public void Apply_SignUp_Sinhala(TextView txtTitle, EditText edtFirstName, EditText edtLasttName, EditText edtMobileNumber,
                                     EditText edtEmailAddress, EditText edtCompanyName, EditText edtBusinessDes, TextView txtSelectCategory, Button btnSubmit) {
        txtTitle.setText("fkd�f,aa ,efnk ld� �vrh wh�� lrkak'");
        txtTitle.setTypeface(font_sinhala);

        edtFirstName.setHint("uq,a ku");
        edtFirstName.setTypeface(font_sinhala);

        edtLasttName.setHint("jdi.u");
        edtLasttName.setTypeface(font_sinhala);

        edtMobileNumber.setHint("cx.u �rl:k wxlh");
        edtMobileNumber.setTypeface(font_sinhala);

        edtEmailAddress.setHint("Bf�,a ,smskh");
        edtEmailAddress.setTypeface(font_sinhala);

        edtCompanyName.setHint("wdh;kfhaa ku");
        edtCompanyName.setTypeface(font_sinhala);

        edtBusinessDes.setHint("jHdmdrh ms<sn| �ia;r");
        edtBusinessDes.setTypeface(font_sinhala);

        txtSelectCategory.setHint("jHdmdr leg.�h f;darkak");
        txtSelectCategory.setTypeface(font_sinhala);

        btnSubmit.setText("b��hg hkak");
        btnSubmit.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);
            txtTitle.setTextSize(14.0f);
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
        edtFirstName.setHint("uq,a ku");
        edtFirstName.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtFirstName.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_LastName(EditText edtLasttName) {
        edtLasttName.setHint("jdi.u");
        edtLasttName.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtLasttName.setTextSize(18.0f);
        }
    }

    public void Apply_Key_EntryPin(TextView txtTitle, EditText edtPin, Button btnSubmit) {
        txtTitle.setText("mska wxlh we;=,;a lrkak");
        txtTitle.setTypeface(font_sinhala);

        edtPin.setHint("ryia wxlh we;=<;a lrkak");
        edtPin.setTypeface(font_sinhala);

        btnSubmit.setText("b��hg hkak");
        btnSubmit.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            edtPin.setTextSize(16.0f);
            btnSubmit.setTextSize(16.0f);

        }
    }


    public void Apply_SignUp_Mobile(EditText edtMobileNumber) {
        edtMobileNumber.setHint("cx.u �rl:k wxlh");
        edtMobileNumber.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtMobileNumber.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Email(EditText edtEmailAddress) {
        edtEmailAddress.setHint("Bf�,a ,smskh");
        edtEmailAddress.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtEmailAddress.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Company(EditText edtCompany) {
        edtCompany.setHint("wdh;kfhaa ku");
        edtCompany.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtCompany.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_BusinessDes(EditText edtBusinessDes) {
        edtBusinessDes.setHint("jHdmdrh ms<sn| �ia;r");
        edtBusinessDes.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            edtBusinessDes.setTextSize(18.0f);
        }
    }

    public void Apply_SignUp_Title(TextView txtTitle) {
        txtTitle.setText("fkd�f,aa ,efnk ld� �vrh wh�� lrkak'");
        txtTitle.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            txtTitle.setTextSize(14.0f);
        }
    }

    public void Apply_SignUp_Submit(Button btnSubmit) {
        btnSubmit.setText("b��hg hkak");
        btnSubmit.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtSignIn.setTextSize(22.0f);

            btnSubmit.setTextSize(18.0f);
        }
    }

    // ============ Home ===========//

    public void Apply_Home(TextView txtTitle, Button btnSale, Button btnSettlement, Button btnVoid,
                           ImageButton ibhistory, ImageButton ibsettle_history) {
        txtTitle.setText("ksjyk");
        txtTitle.setTypeface(font_sinhala);

        btnSale.setText("�lsKq�");
        btnSale.setTypeface(font_sinhala);

        btnSettlement.setText("msh�u");
        btnSettlement.setTypeface(font_sinhala);

        btnVoid.setText(".kqfokq");
        btnVoid.setTypeface(font_sinhala);

        try {

            ibhistory.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transaction_history_sinhala));
            ibsettle_history
                    .setBackgroundDrawable(context.getResources().getDrawable(R.drawable.settlement_history_sinhala));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (screensize < 6.8) {
            txtTitle.setTextSize(22.0f);
            btnSale.setTextSize(22.0f); //
            btnSettlement.setTextSize(22.0f);
            btnVoid.setTextSize(22.0f);
        }

    }

    // ============ Void List ===========//
// by amal 2017-7-14
    public void Apply_VoidList(TextView txtType, String status) {

        if (status.equals("Sale")) {
            txtType.setText("�lsKq�");
        } else {
            txtType.setText("wj,x.= ls�u");
        }

        if (screensize < 6.8) {
            txtType.setTextSize(14.0f);
        }
        txtType.setTypeface(font_sinhala);
    }

    /*public void Apply_VoidList(TextView txtType) {

        System.out.println("txtType "+txtType.getText().toString());

        if (txtType.getText().toString().equals("Sale")) {
            txtType.setText("�lsKq�");
            txtType.setTypeface(font_sinhala);
        } else {
            txtType.setText("wj,x.= ls�u");
            txtType.setTypeface(font_sinhala);
        }

        if (screensize < 6.8) {
            txtType.setTextSize(14.0f);

        }

    }*/

    public void Apply_VoidListTitle(TextView txtTitle) {
        txtTitle.setText(".kqfokq");
        txtTitle.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
        }

    }

    public void Apply_TransactionListTitle(TextView txtTitle) {
        txtTitle.setText(".kqfokq");
        txtTitle.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
        }

    }

    public void Apply_VoidDetails(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode,
                                  TextView lblSalesDate, Button btnVoid, Button btnSign, Button btnReceipt, TextView lblTrackingId) {
        // TODO Auto-generated method stub
        txtTitle.setText(".kqfokq f;dr;=re");
        txtTitle.setTypeface(font_sinhala);

        txtPaidWith.setText("ld� mf;ys ku");
        txtPaidWith.setTypeface(font_sinhala);

        txtApprovalCode.setText("wkqu; ls�f� wxlh");
        txtApprovalCode.setTypeface(font_sinhala);

        lblSalesDate.setText("�lsKq� �kh");
        lblSalesDate.setTypeface(font_sinhala);

        btnVoid.setText("wj,x.= lrkak");
        btnVoid.setTypeface(font_sinhala);

        btnSign.setText("w;aik fhdokak");
        btnSign.setTypeface(font_sinhala);

        btnReceipt.setText(",�m; kej; hjkak");
        btnReceipt.setTypeface(font_sinhala);

        lblTrackingId.setText("jd�;d wxlh");
        lblTrackingId.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(18.0f);
            txtApprovalCode.setTextSize(18.0f);
            btnVoid.setTextSize(20.0f);
            btnSign.setTextSize(20.0f);
            btnReceipt.setTextSize(20.0f);
            lblSalesDate.setTextSize(18.0f);
            lblSalesDate.setTextSize(18.0f);
        }

    }

    public void Apply_VoidDetails_WithVoid(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode,
                                           TextView lblSalesTime, TextView lblVoidTime, Button btnVoid,
                                           Button btnSign, Button btnReceipt, TextView lblTrackingId) {
        // TODO Auto-generated method stub
        txtTitle.setText(".kqfokq f;dr;=re");
        txtTitle.setTypeface(font_sinhala);

        txtPaidWith.setText("ld� mf;ys ku");
        txtPaidWith.setTypeface(font_sinhala);

        txtApprovalCode.setText("wkqu; ls�f� wxlh");
        txtApprovalCode.setTypeface(font_sinhala);

        lblSalesTime.setText("�lsKq� �kh");
        lblSalesTime.setTypeface(font_sinhala);

        lblVoidTime.setText("wj,x.= ls�f� �kh");
        lblVoidTime.setTypeface(font_sinhala);

        btnVoid.setText("wj,x.= lrkak");
        btnVoid.setTypeface(font_sinhala);

        btnSign.setText("w;aik fhdokak");
        btnSign.setTypeface(font_sinhala);

        btnReceipt.setText(",�m; kej; hjkak");
        btnReceipt.setTypeface(font_sinhala);

        lblTrackingId.setText("jd�;d wxlh");
        lblTrackingId.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(18.0f);
            txtApprovalCode.setTextSize(18.0f);
            btnVoid.setTextSize(20.0f);
            btnSign.setTextSize(20.0f);
            btnReceipt.setTextSize(20.0f);
            lblSalesTime.setTextSize(18.0f);
            lblVoidTime.setTextSize(18.0f);
            lblTrackingId.setTextSize(15.0f);
        }
    }

    public void Apply_VoidErrorMessage(TextView txtTitle, TextView txtMessage, Button btnOk, Button btnCancel,
                                       String amount, int currencyType) {
        txtTitle.setText("Tf� wjOdkhg");
        txtTitle.setTypeface(font_sinhala);

        String CURRENCY = UtilityFunction.getCurrencyTypeStringSI(currencyType);

        txtMessage.setText("fuu l%shdj,sfhka .kqfokqj wj,x.= fjhs' .kqfokqj wj,x.= l< �g ld�m;a ys�hdg " + CURRENCY + " "
                + amount.replace(".", "'")
                + " l uqo,a m%udKh kej; ys� fjhs' fuu l%shdj,sh kej; wj,x.= l< fkdyel' b��hg hkako@");
        txtMessage.setTypeface(font_sinhala);

        btnOk.setText("T�");
        btnOk.setTypeface(font_sinhala);

        btnCancel.setText("ke;");
        btnCancel.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtMessage.setTextSize(18.0f);
            btnOk.setTextSize(20.0f);
            btnCancel.setTextSize(20.0f);

        }
    }

    public void Apply_VoidPin(TextView txtTitle, EditText edtPin, Button btnSubmit) {
        txtTitle.setText(".kqfokq f;dr;=re");
        txtTitle.setTypeface(font_sinhala);

        edtPin.setHint("mska wxlh we;=<;a lrkak");
        edtPin.setTypeface(font_sinhala);

        btnSubmit.setText("b��hg hkak");
        btnSubmit.setTypeface(font_sinhala);

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
        txtTitle.setText("�is�");
        txtTitle.setTypeface(font_sinhala);

        txtVoidComplete.setText("wj,x.= ls�u id�:lhs");
        txtVoidComplete.setTypeface(font_sinhala);

        txtlike.setText("�is� m;la wjYHo@");
        txtlike.setTypeface(font_sinhala);

        btnEmail.setText("Bf�,a");
        btnEmail.setTypeface(font_sinhala);

        btnText.setText("fl�mKsjqv");
        btnText.setTypeface(font_sinhala);

        btnNoReceipt.setText("ke;");
        btnNoReceipt.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtVoidComplete.setTextSize(22.0f);
            txtlike.setTextSize(18.0f);
            btnEmail.setTextSize(20.0f);
            btnText.setTextSize(20.0f);
            btnNoReceipt.setTextSize(20.0f);
        }
    }

    public void Apply_HistoryList(TextView txtTitle, EditText edtSearch) {
        txtTitle.setText("miq.sh .kqfokq");
        txtTitle.setTypeface(font_sinhala);

        edtSearch.setHint("fidhkak");
        edtSearch.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            edtSearch.setTextSize(16.0f);
            txtTitle.setTextSize(18.0f);
        }

    }

    public void Apply_HistoryDetails(TextView txtTitle, TextView txtPaidWith, TextView txtApprovalCode) {
        txtTitle.setText("miq.sh o;a; f;dr;=re");
        txtTitle.setTypeface(font_sinhala);

        txtPaidWith.setText("ld� mf;ys ku");
        txtPaidWith.setTypeface(font_sinhala);

        txtApprovalCode.setText("wkqu; ls�f� fla;h");
        txtApprovalCode.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtPaidWith.setTextSize(20.0f);
            txtApprovalCode.setTextSize(20.0f);
        }
    }

    public void Apply_FilterHistory(TextView txtTitle, TextView txtFilterOptions, TextView txtStarts,
                                    TextView txtSelectDate, TextView txtEnds, TextView txtSelectEndDate,
                                    TextView txtCardType, TextView txtVisa, TextView txtMaster,
                                    TextView txtAmex, TextView txtDiners) {
        txtTitle.setText("fijq�");
        txtTitle.setTypeface(font_sinhala);

        txtFilterOptions.setText("fijq� jrKhka");
        txtFilterOptions.setTypeface(font_sinhala);

        txtStarts.setText("isg");
        txtStarts.setTypeface(font_sinhala);

        txtEnds.setText("olajd");
        txtEnds.setTypeface(font_sinhala);

        txtSelectDate.setHint("�kh f;darkak");
        txtSelectDate.setTypeface(font_sinhala);

        txtSelectEndDate.setHint("�kh f;darkak");
        txtSelectEndDate.setTypeface(font_sinhala);

        txtCardType.setText("ld� j�.h");
        txtCardType.setTypeface(font_sinhala);

        txtVisa.setText("�id");
        txtVisa.setTypeface(font_sinhala);

        txtMaster.setText("udiag�");
        txtMaster.setTypeface(font_sinhala);

        txtAmex.setText("weu�lka tlaiam%ia");
        txtAmex.setTypeface(font_sinhala);

        txtDiners.setText("vhsk�ia la,�");
        txtDiners.setTypeface(font_sinhala);

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

    public void Apply_Settlements(TextView txtTitle, TextView txtVisaSettlement, TextView txtMasterSettlement,
                                  TextView txtSummary, TextView txtTotalSettlements, TextView txtTotalAmount, TextView txtSettleNow) {
        txtTitle.setText("msh�u");
        txtTitle.setTypeface(font_sinhala);

        txtVisaSettlement.setText("msh��");
        txtVisaSettlement.setTypeface(font_sinhala);

        txtMasterSettlement.setText("msh��");
        txtMasterSettlement.setTypeface(font_sinhala);

        txtSummary.setText("idrdxYh");
        txtSummary.setTypeface(font_sinhala);

        txtTotalSettlements.setText("msh��j, tl;=j");
        txtTotalSettlements.setTypeface(font_sinhala);

        txtTotalAmount.setText("uq� uqo,");
        txtTotalAmount.setTypeface(font_sinhala);

        txtSettleNow.setText("oeka mshjkak");
        txtSettleNow.setTypeface(font_sinhala);

    }

    public void Apply_Sale(TextView txtTitle, TextView txtReadCard, Button btnMoreOptions, Button btnClearOptions) {
        txtTitle.setText("�lsKq�");
        txtTitle.setTypeface(font_sinhala);

//		txtReadCard.setText("ld�m; lshjkak");
        txtReadCard.setText("ld�m; lshjkak");
        txtReadCard.setTypeface(font_sinhala);

        btnMoreOptions.setText("wu;r f.�� l%u");
        btnMoreOptions.setTypeface(font_sinhala);

        btnClearOptions.setText("ulkak");
        btnClearOptions.setTypeface(font_sinhala);
    }

    public void Apply_Sale_Key_Entry(TextView txtTitle, TextView txtReadCard, Button btnMoreOptions, Button btnClearOptions) {
        txtTitle.setText("�lsKq�");
        txtTitle.setTypeface(font_sinhala);

//		txtReadCard.setText("ld�m; lshjkak");
        txtReadCard.setText("ld�m; wh lrkak");
        txtReadCard.setTypeface(font_sinhala);

        btnMoreOptions.setText("wu;r f.�� l%u");
        btnMoreOptions.setTypeface(font_sinhala);

        btnClearOptions.setText("ulkak");
        btnClearOptions.setTypeface(font_sinhala);
    }

    public void apply_Installemt(Button btnMoreOptions, StringBuilder stringBuilder,
                                 int mInstallments, int currencyType, double valDisplay) {
        switch (currencyType) {
            case Consts.LKR:
                stringBuilder.append("re ");
                break;
            case Consts.USD:
                stringBuilder.append("we'fvd' ");
                break;
            case Consts.GBP:
                stringBuilder.append("mjq� ");
                break;
            case Consts.EUR:
                stringBuilder.append("hqfrda ");
                break;
            default:
                stringBuilder.append("re ");
        }
        stringBuilder.append(String.valueOf(valDisplay));
        stringBuilder.append(" $ ");
        stringBuilder.append(String.valueOf(mInstallments)).append(" jd�l");
        btnMoreOptions.setTypeface(font_sinhala);
    }

    public void Apply_Swipe(TextView txtTitle, TextView txtAmount) {
        txtTitle.setText("ld�m; iajhsma $ we;=<;a lrkak");
        txtTitle.setTypeface(font_sinhala);

        txtAmount.setText("uqo,");
        txtAmount.setTypeface(font_sinhala);
    }

    public void Apply_Swipe_Dialog(TextView txtTransaction, Button btnAddSignature) {
        txtTransaction.setText("Tf� .kqfokqj id�:lhs'");
        txtTransaction.setTypeface(font_sinhala);

        btnAddSignature.setText("w;aik fhdokak'");
        btnAddSignature.setTypeface(font_sinhala);
    }

    public void paymentOption(TextView titleTV, TextView selectinstallmentTV, TextView insta_12,
                              TextView insta_24, TextView insta_36, TextView otherTv,
                              EditText instalmentET, TextView txtTransaction, Button btnAddSignature) {
        titleTV.setText("wu;r f.�� l%u"); //payment option
        selectinstallmentTV.setText("jd�l l%uh f;darkak"); //select installment
        insta_12.setText("jd�l"); //12 installment
        insta_24.setText("jd�l"); //24 installment
        insta_36.setText("jd�l"); //36 installment
        otherTv.setText("fjk;a"); //other
        instalmentET.setHint("jd�l .Kk we;=,a lrkak"); //enter no of installemt
        txtTransaction.setText("uqo,a j�.h f;darkak"); // select currency
        btnAddSignature.setText("f;darkak");// select

        titleTV.setTypeface(font_sinhala);
        selectinstallmentTV.setTypeface(font_sinhala);
        insta_12.setTypeface(font_sinhala);
        insta_24.setTypeface(font_sinhala);
        insta_36.setTypeface(font_sinhala);
        otherTv.setTypeface(font_sinhala);
        instalmentET.setTypeface(font_sinhala);
        txtTransaction.setTypeface(font_sinhala);
        btnAddSignature.setTypeface(font_sinhala);
    }

    public void Apply_Signature(TextView txtAmount, TextView txtSignHere, Button btnSave,
                                Button btnClear, Button btnApproved, String name, String card_no, String code, String cardName) {
        txtSignHere.setText("w;aik fhdokak");
        txtSignHere.setTypeface(font_sinhala);

        txtAmount.setText("uqo,");
        txtAmount.setTypeface(font_sinhala);

        /*txtConfirmation.setText("uu udf.aa ld� ys� tl�;djfhys i|yka i�mQ�K uqo, f.�ug tl� fj�' ld�mf;ys wjika wxl y;r "
                + card_no + "'wjir ,nd �f� flaa;h' " + code);

        txtConfirmation.setTypeface(font_sinhala);
        txtConfirmation.setText(txtConfirmation.getText().toString());*/

        btnSave.setText("b��hg hkak");
        btnSave.setTypeface(font_sinhala);

        btnClear.setText("w;aik ulkak");
        btnClear.setTypeface(font_sinhala);

        btnApproved.setText("wkqu;hs");
        btnApproved.setTypeface(font_sinhala);

        System.out.println("screensize " + screensize);

        if (screensize < 6.8) {
            txtAmount.setTextSize(18.0f);
            //txtConfirmation.setTextSize(15.0f);
            btnSave.setTextSize(16.0f);
            btnClear.setTextSize(16.0f);
            btnApproved.setTextSize(16.0f);
            txtSignHere.setTextSize(36.0f);
        } else {
            txtSignHere.setTextSize(56.0f);
            txtAmount.setTextSize(20.0f);
            //txtConfirmation.setTextSize(22.0f);
            btnSave.setTextSize(26.0f);
            btnClear.setTextSize(18.0f);
            btnApproved.setTextSize(18.0f);
        }

    }

    public void Apply_Receipt(TextView txtTitle, TextView txtSignatureSave, TextView txtlike, Button btnEmail,
                              Button btnText, Button btnNoReceipt) {
        txtTitle.setText("�is�");
        txtTitle.setTypeface(font_sinhala);

        txtSignatureSave.setText("w;aik we;=<;a lrk ,�'");
        txtSignatureSave.setTypeface(font_sinhala);

        txtlike.setText("�is� m;la wjYHo@");
        txtlike.setTypeface(font_sinhala);

        btnEmail.setText("Bf�,a");
        btnEmail.setTypeface(font_sinhala);

        btnText.setText("fl�mKsjqv");
        btnText.setTypeface(font_sinhala);

        btnNoReceipt.setText("ke;");
        btnNoReceipt.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(18.0f);
            txtSignatureSave.setTextSize(22.0f);
            txtlike.setTextSize(18.0f);
            btnEmail.setTextSize(20.0f);
            btnText.setTextSize(20.0f);
            btnNoReceipt.setTextSize(20.0f);
        }
    }

    public void Apply_SendEmail(TextView txtTitle, TextView txtPaymentDetails, TextView txtAmount, TextView txtCardName,
                                TextView txtApprovalCode, TextView txtSendVia, EditText edtEmail,
                                Button btnSend, TextView txtAmountValue) {
        txtTitle.setText("Bf�,a hjkak");
        txtTitle.setTypeface(font_sinhala);

        txtPaymentDetails.setText(".kqfokq f;dr;=re");
        txtPaymentDetails.setTypeface(font_sinhala);

        txtAmount.setText("uqo,");
        txtAmount.setTypeface(font_sinhala);

        txtCardName.setText("ld�mf;aa ku");
        txtCardName.setTypeface(font_sinhala);

        txtApprovalCode.setText("wkqu; ls�f� wxlh");
        txtApprovalCode.setTypeface(font_sinhala);

        txtSendVia.setText("Bf�,a u.ska hjkak");
        txtSendVia.setTypeface(font_sinhala);

        txtAmountValue.setTypeface(font_sinhala);

        edtEmail.setHint("Bf�,a ,smskh we;=<;a lrkak");
        edtEmail.setTypeface(font_sinhala);

        btnSend.setText("hjkak");
        btnSend.setTypeface(font_sinhala);

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
        txtTitle.setText("fl� mKsjqvh hjkak");
        txtTitle.setTypeface(font_sinhala);

        txtPaymentDetails.setText(".kqfokq f;dr;=re");
        txtPaymentDetails.setTypeface(font_sinhala);

        txtAmount.setText("uqo,");
        txtAmount.setTypeface(font_sinhala);

        txtCardName.setText("ld�mf;aa ku");
        txtCardName.setTypeface(font_sinhala);

        txtApprovalCode.setText("wkqu; ls�f� wxlh");
        txtApprovalCode.setTypeface(font_sinhala);

        txtSendVia.setText("fl� mKsjqv u.ska hjkak");
        txtSendVia.setTypeface(font_sinhala);

        edtPhone.setHint("cx.u �rl;k wxlh we;=<;a lrkak");
        edtPhone.setTypeface(font_sinhala);

        btnSend.setText("hjkak");
        btnSend.setTypeface(font_sinhala);
        txtAmountValue.setTypeface(font_sinhala);

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
        txtTitle.setText("wjidk wxl y;r");
        txtTitle.setTypeface(font_sinhala);

        // txtYourPrivacy.setText("Tf� fm!oa.,sl;ajh wmg jeo.;a'");
        // txtYourPrivacy.setTypeface(font_sinhala);

        txtPleaseEnter.setText("lreKdlr Tf� ld�mf;ys wjika wxl y;r we;=<;a lrkak'");
        txtPleaseEnter.setTypeface(font_sinhala);

        edtDigit.setHint("wjika wxl y;r we;=<;a lrkak'");
        edtDigit.setTypeface(font_sinhala);

        btnSubmit.setText("hjkak");
        btnSubmit.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            // txtYourPrivacy.setTextSize(20.0f);
            txtPleaseEnter.setTextSize(16.0f);
            edtDigit.setTextSize(18.0f);
            btnSubmit.setTextSize(18.0f);

        }

    }

    // Settlement List

    public void Apply_SettlementList(TextView txtTitle) {
        txtTitle.setText("miq.sh msh��");
        txtTitle.setTypeface(font_sinhala);
    }

    public void Apply_SettlementListData(TextView txtSales, TextView txtVoids) {
        txtSales.setText("�lsKq�");
        txtSales.setTypeface(font_sinhala);

        txtVoids.setText("wj,x.= ls�u");
        txtVoids.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            txtSales.setTextSize(16.0f);
            txtVoids.setTextSize(16.0f);

        }
    }

    // Settlement History Details

    public void Apply_SettlementHistoryDetails(String salescount, String voidcount, TextView txtSalesCount,
                                               TextView txtVoidsCount, Button btnViewTransactions,
                                               TextView txtSales, TextView txtVoids, String selectedCurrencyType) {
        txtSalesCount.setText("�lsKq� " + salescount);
        txtSalesCount.setTypeface(font_sinhala);

        txtVoidsCount.setText("wj,x.= ls�u " + voidcount);
        txtVoidsCount.setTypeface(font_sinhala);

        btnViewTransactions.setText(".kqfokq n,kak'");
        btnViewTransactions.setTypeface(font_sinhala);

        txtSales.setText("�lsKq�" + " - " + selectedCurrencyType);
        txtSales.setTypeface(font_sinhala);

        txtVoids.setText("wj,x.= ls�u" + " - " + selectedCurrencyType);
        txtVoids.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            // txtYourPrivacy.setTextSize(20.0f);
            txtSalesCount.setTextSize(16.0f);
            txtVoidsCount.setTextSize(16.0f);
            btnViewTransactions.setTextSize(18.0f);

            txtSales.setTextSize(16.0f);
            txtVoids.setTextSize(16.0f);
        }

    }

    public void Apply_OrderTracking(TextView txtTitle, EditText edtOrder, Button btnContinue) {
        txtTitle.setText("weKjq� jd�;d");
        txtTitle.setTypeface(font_sinhala);

        edtOrder.setHint("jd�;d wxlh we;=<;a lrkak (");
        edtOrder.setTypeface(font_sinhala);

        btnContinue.setText("b��hg hkak");
        btnContinue.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            edtOrder.setTextSize(16.0f);
            btnContinue.setTextSize(16.0f);

        }

    }

    public void Apply_OrderTracking_Ordertext(EditText edtOrder) {

        edtOrder.setHint("jd�;d wxlh we;=<;a lrkak (");
        edtOrder.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            edtOrder.setTextSize(16.0f);

        }

    }

    public void Apply_Settings(TextView txtTitle, TextView txtChangePassword,
                               TextView txtMerchant, TextView txtSelectLanguage, TextView txtCardReader, TextView txtLogs,
                               TextView txtOrderTracking) {
        txtTitle.setText("ieliq�");
        txtTitle.setTypeface(font_sinhala);

        txtChangePassword.setText("uqr moh fjkia ls�u");
        txtChangePassword.setTypeface(font_sinhala);

        txtMerchant.setText("jHdmd�lhdf.aa f;dr;=re");
        txtMerchant.setTypeface(font_sinhala);

        txtSelectLanguage.setText("NdIdj f;aa�u");
        txtSelectLanguage.setTypeface(font_sinhala);

        txtCardReader.setText("ld� m; lshjk oDvdx.h");
        txtCardReader.setTypeface(font_sinhala);

        txtLogs.setText("igyka");
        txtLogs.setTypeface(font_sinhala);

        txtOrderTracking.setText("weKjq� jd�;d");
        txtOrderTracking.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtChangePassword.setTextSize(16.0f);
            txtMerchant.setTextSize(16.0f);
            txtSelectLanguage.setTextSize(16.0f);
            txtOrderTracking.setTextSize(16.0f);
            txtCardReader.setTextSize(16.0f);
            txtLogs.setTextSize(16.0f);
        }

    }

    public void Apply_ChangePassword(TextView txtTitle, EditText edtCurrentPassword, EditText edtNewPass,
                                     EditText edtRePass, Button btnSubmit) {
        txtTitle.setText("uqr moh fjkia ls�u");
        txtTitle.setTypeface(font_sinhala);

        edtCurrentPassword.setHint("oeka ;sfnk uqr moh");
        edtCurrentPassword.setTypeface(font_sinhala);

        edtNewPass.setHint("kj uqr moh");
        edtNewPass.setTypeface(font_sinhala);

        edtRePass.setHint("kj uqr moh ia�r lrkak");
        edtRePass.setTypeface(font_sinhala);

        btnSubmit.setText("hjkak");
        btnSubmit.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            edtCurrentPassword.setTextSize(18.0f);
            edtNewPass.setTextSize(18.0f);
            edtRePass.setTextSize(18.0f);
            btnSubmit.setTextSize(18.0f);

        }
    }

    public void Apply_ChangePassword_CurrentPass(EditText edtCurrentPassword) {
        edtCurrentPassword.setHint("oeka ;sfnk uqr moh");
        edtCurrentPassword.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            edtCurrentPassword.setTextSize(18.0f);

        }
    }

    public void Apply_ChangePassword_NewPass(EditText edtNewPass) {
        edtNewPass.setHint("kj uqr moh");
        edtNewPass.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            edtNewPass.setTextSize(18.0f);

        }
    }

    public void Apply_ChangePassword_RePass(EditText edtRePass) {
        edtRePass.setHint("kj uqr moh ia�r lrkak");
        edtRePass.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            edtRePass.setTextSize(18.0f);

        }
    }

    public void Apply_MerchantProfile(TextView txtTitle, TextView lblUserName, TextView lblBusinessName,
                                      TextView lblCoporateAddress, TextView lblMerchantId, TextView lblTerminalId, TextView lblAppVersion,
                                      TextView lblCardType, TextView lblAmount) {
        txtTitle.setText("jHdmd�lhdf.aa f;dr;=re");
        txtTitle.setTypeface(font_sinhala);

        lblUserName.setText("m�YS,lhdf.a ku");
        lblUserName.setTypeface(font_sinhala);

        lblBusinessName.setText("jHdmdrfhaa ku");
        lblBusinessName.setTypeface(font_sinhala);

        lblCoporateAddress.setText("jHdmdrfhaa ,smskh");
        lblCoporateAddress.setTypeface(font_sinhala);

        lblMerchantId.setText("jHdmd�lhdf.aa wxlh");
        lblMerchantId.setTypeface(font_sinhala);

        lblTerminalId.setText("g��k,a wxlh");
        lblTerminalId.setTypeface(font_sinhala);

        lblAppVersion.setText("fh�� wkqjdoh");
        lblAppVersion.setTypeface(font_sinhala);

        lblCardType.setText("ld� �vrfhaa ud�,sh");
        lblCardType.setTypeface(font_sinhala);

        lblAmount.setText("Wm�u uqo,");
        lblAmount.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            txtTitle.setTextSize(18.0f);
            lblUserName.setTextSize(16.0f);
            lblBusinessName.setTextSize(16.0f);
            lblCoporateAddress.setTextSize(16.0f);
            lblMerchantId.setTextSize(16.0f);
            lblTerminalId.setTextSize(16.0f);
            lblAppVersion.setTextSize(16.0f);
            lblCardType.setTextSize(16.0f);
            lblAmount.setTextSize(16.0f);

        }

    }

    public void Apply_ChangeLanguage(TextView txtTitle, TextView txtEnglish, TextView txtSinhala, TextView txtTamil) {
        txtTitle.setText("NdIdj f;aa�u");
        txtTitle.setTypeface(font_sinhala);

        // txtEnglish.setText("bx.%Sis");
        // txtEnglish.setTypeface(font_sinhala);
        //
        // txtSinhala.setText("isxy,");
        // txtSinhala.setTypeface(font_sinhala);
        //
        // txtTamil.setText("fou<");
        // txtTamil.setTypeface(font_sinhala);
    }

    public void Apply_ShowLanguage(TextView txtTitle) {
        txtTitle.setText("Tnf.a NdIdj isxy,g fjkia lr we;");
        txtTitle.setTypeface(font_sinhala);
    }

    public void Apply_ShowLanguage_Validation(TextView txtTitle) {
        txtTitle.setText("lreKdlr fjkia NdIdjla f;darkak");
        txtTitle.setTypeface(font_sinhala);
    }

    public void apply_bluetoothscan(TextView txtHeader, Button btnScan, Button btnCancel) {
        txtHeader.setText("�v� ialEka ls�u");
        txtHeader.setTypeface(font_sinhala);

        btnScan.setText("ialEka lrkak");
        btnScan.setTypeface(font_sinhala);

        btnCancel.setText("wj,x.= lrkak");
        btnCancel.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            txtHeader.setTextSize(14.0f);
            btnScan.setTextSize(14.0f);
            btnCancel.setTextSize(14.0f);
        }
    }

    public void apply_scan_tv_status(TextView tv_status, int num) {
        if (num == 0) // Scanning Devices, Please wait...
        {
            tv_status.setText("�jhsia tl ialEka fj�ka mj;S' /|S is�kak'");
            tv_status.setTypeface(font_sinhala);
        }

        if (num == 1) // Reader is registered with app successfully
        {
            tv_status.setText("�vrh id�:lj ,shdm�x� lrk ,�'");
            tv_status.setTypeface(font_sinhala);
        }

        if (num == 2) // Reader not found.Please turn on your reader
        {
            tv_status.setText("�vrh fidhd .ekSug fkdyel' Tf� �vrh l%shd;aul lrkak'");
            tv_status.setTypeface(font_sinhala);
        }

        if (num == 3) // Bluetooth is turned off.Please turn it on
        {
            tv_status.setText("Tf� ��gQ;a l%shd;aul lrkak'");
            tv_status.setTypeface(font_sinhala);
        }

        if (num == 4) // Bluetooth is unsupported by this device
        {
            tv_status.setText("fuu �jhsih i|yd ��gQ;a iydh fkdolajhs'");
            tv_status.setTypeface(font_sinhala);
        }

        if (screensize < 6.8) {

            tv_status.setTextSize(14.0f);
        }

    }

    // ================== Progress Dialog Messages ======================//

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
        tittle.setText("ld�m;a wxlh we;=,;a lrkak");
        tittle.setTypeface(font_sinhala);

        txtAmount.setText("uqo, ");
        txtAmount.setTypeface(font_sinhala);

        txtEnter.setText("ld� wxlh iy l,a bl=;a�f� �kh we;=,a lrkak");
        txtEnter.setTypeface(font_sinhala);

        btnCharge.setText("wh lrkak");
        btnCharge.setTypeface(font_sinhala);

        if (screensize < 6.8) {

            tittle.setTextSize(18.0f);
            txtAmount.setTextSize(25.0f);
            btnCharge.setTextSize(25.0f);
        }
    }

    public void selectPaymentMethod(TextView tvTitle, TextView tvAmmou, TextView tvReadCard,
                                    TextView tvEnterCard, TextView tvAmmouValue) {
        tvTitle.setText("f.�� l%uh f;darkak");
        tvTitle.setTypeface(font_sinhala);

        tvAmmou.setText("uqo, ");
        tvAmmou.setTypeface(font_sinhala);

        tvReadCard.setText("ld�m; lshjkak");
        tvReadCard.setTypeface(font_sinhala);

        tvEnterCard.setText("ld� wxlh we;=,a lrkak");
        tvEnterCard.setTypeface(font_sinhala);
        tvAmmouValue.setTypeface(font_sinhala);

        if (screensize < 6.8) {
            tvTitle.setTextSize(22.0f);
            tvAmmou.setTextSize(22.0f);
            tvReadCard.setTextSize(22.0f);
            tvEnterCard.setTextSize(22.0f);
            tvAmmouValue.setTextSize(22.0f);
        }

    }

    public void Apply_Approval_Sinhala(TextView txtTitle, TextView txApproved, TextView txtTxCompleted
            , TextView txtSigNotRqrd, TextView txtCardEndingTxt, TextView txtAuthCodeTxt, Button btnProceed) {
        txtTitle.setText("wkqu;h");
        txtTitle.setTypeface(font_sinhala, Typeface.BOLD);

        txApproved.setText("uqo, ");
        txApproved.setTypeface(font_sinhala, Typeface.BOLD);

        txtTxCompleted.setText(".kqfokqj wjidk �h");
        txtTxCompleted.setTypeface(font_sinhala);

        txtSigNotRqrd.setText("w;aik wjYH ke;");
        txtSigNotRqrd.setTypeface(font_sinhala);

        txtCardEndingTxt.setText("ld�mf;a wjidk wxl");
        txtCardEndingTxt.setTypeface(font_sinhala);

        txtAuthCodeTxt.setText("wkqu; ls�f� wxlh");
        txtAuthCodeTxt.setTypeface(font_sinhala);

        btnProceed.setText("b��hg hkak");
        btnProceed.setTypeface(font_sinhala, Typeface.BOLD);

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

    public void Apply_Rejected_Sinhala(TextView txtTitle, TextView txtAmountTxt, TextView txtCardName, TextView txtErrorCodetxt, Button btnProceed) {

        txtTitle.setText("m%;slafIam �h");
        txtTitle.setTypeface(font_sinhala, Typeface.BOLD);

        txtAmountTxt.setText("uqo, ");
        txtAmountTxt.setTypeface(font_sinhala);

        txtCardName.setText(".kqfokqj wid�;l �h`");
        txtCardName.setTypeface(font_sinhala);

        txtErrorCodetxt.setText("fodaI wxlh");
        txtErrorCodetxt.setTypeface(font_sinhala);

        btnProceed.setText("b��hg hkak");
        btnProceed.setTypeface(font_sinhala, Typeface.BOLD);

        if (screensize < 6.8) {
            txtTitle.setTextSize(25.0f);
            txtAmountTxt.setTextSize(22.0f);
            txtCardName.setTextSize(18.0f);
            txtErrorCodetxt.setTextSize(18.0f);
            btnProceed.setTextSize(25.0f);

        }

    }

    public void apply_more_payment(Button btnMoreOptions) {
        btnMoreOptions.setText("wu;r f.�� l%u");
        btnMoreOptions.setTypeface(font_sinhala);
    }

    public void setInstallment(TextView insTv, String status) {
        insTv.setText("jd�l " + status);
        insTv.setTypeface(font_sinhala);
        insTv.setTextSize(12.0f);
    }

    public void Apply_Secondary_Settings(TextView txtTitle, TextView txtCaption, TextView txtVisa, TextView txtAmex) {

        txtTitle.setTypeface(font_sinhala);
        txtCaption.setTypeface(font_sinhala);
        txtVisa.setTypeface(font_sinhala);
        txtAmex.setTypeface(font_sinhala);

        txtTitle.setText("ieliq�");
        txtCaption.setText("ld� j�.h f;darkak");
        txtVisa.setText("�id$udiag�ld�");
        txtAmex.setText("wefulaia$vhsk�ia la,�");

        if (screensize < 6.8) {
            txtTitle.setTextSize(16.0f);
            txtCaption.setTextSize(16.0f);
            txtVisa.setTextSize(16.0f);
            txtAmex.setTextSize(16.0f);
        }
    }
}
