package com.mpos.util;

import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by "Don" on 2/1/2018.
 * Class Functionality :-
 */

public class English_LangSelect {

    public void Apply_SignIn_Amex_English(TextView txtSignIn, EditText edtEmail, EditText edtPassword, EditText edtBank,
                                          Button btnSignIn, TextView txtSignUp, TextView settings) {
        txtSignIn.setText("Sign In");
        txtSignIn.setTypeface(null);

        edtEmail.setHint("Username");
        edtEmail.setTypeface(null);

        edtPassword.setHint("Password");
        edtPassword.setTypeface(null);

        edtBank.setHint("Bank Code");
        edtBank.setTypeface(null);

        btnSignIn.setText("Sign In");
        btnSignIn.setTypeface(null);

        String boldText = "Sign Up</b> AMEX/DINERS CLUB";
        txtSignUp.setText(Html.fromHtml(boldText));
        txtSignUp.setTypeface(null);

        settings.setText("Back to Settings");
        settings.setTypeface(null);
    }

    public void Apply_SignIn_Seylan_English(TextView txtSignIn, EditText edtEmail, EditText edtPassword, EditText edtBank,
                                          Button btnSignIn, TextView txtSignUp, TextView settings) {
        txtSignIn.setText("Sign In");
        txtSignIn.setTypeface(null);

        edtEmail.setHint("Username");
        edtEmail.setTypeface(null);

        edtPassword.setHint("Password");
        edtPassword.setTypeface(null);

        edtBank.setHint("Bank Code");
        edtBank.setTypeface(null);

        btnSignIn.setText("Sign In");
        btnSignIn.setTypeface(null);

        String boldText = "Sign Up</b> AMEX/DINERS CLUB";
        txtSignUp.setText(Html.fromHtml(boldText));
        txtSignUp.setTypeface(null);

        settings.setText("Back to Settings");
        settings.setTypeface(null);
    }

    public void Apply_SignIn_English(TextView txtSignIn, EditText edtEmail, EditText edtPassword, EditText edtBankType, Button btnSignIn, TextView txtSignUp, Button btnSettings) {
        txtSignIn.setText("Sign In");
        txtSignIn.setTypeface(null);

        edtEmail.setHint("Username");
        edtEmail.setTypeface(null);

        edtPassword.setHint("Password");
        edtPassword.setTypeface(null);

        edtBankType.setHint("Bank Code");
        edtBankType.setTypeface(null);

        btnSignIn.setText("Sign In");
        btnSignIn.setTypeface(null);

        String boldText = "Request your</b> Free Card Reader";
        txtSignUp.setText(Html.fromHtml(boldText));
        txtSignUp.setTypeface(null);

        btnSettings.setText("Back to Settings");
        btnSettings.setTypeface(null);
    }
}
