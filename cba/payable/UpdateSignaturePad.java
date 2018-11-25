package com.cba.payable;

import android.graphics.Bitmap;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.ModelSignature;
import com.mpos.pojo.SimpleAck;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;

import java.text.DecimalFormat;

public class UpdateSignaturePad extends GenericActivity {

    private DrawingArea signPad;

    private RecSale m_recSale;

    TextView txtSignHere, txtConfirmation, txtAmount, txtAmountValue;
    Button btnSave, btnClear, btnApproved;
    TextView txtEnglish, txtSinhala, txtTamil;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    double screensize;
    DecimalFormat df;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_signature);

        df = new DecimalFormat("####0.00");

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof RecSale) {
                    m_recSale = (RecSale) data[i];
                    InitViews();

                    //txtAmountValue.setText("Rs. " + df.format(m_recSale.getAmount()));

                    txtAmountValue.setText(df.format(m_recSale.getAmount())
                            + " "
                            + UtilityFunction.getCurrencyTypeString(m_recSale.getCurrencyType()));
                }
            }

        }
    }

    public void onClear(View v) {
        signPad.clearPad();
    }

	/*public void onBackPressed() {

	}*/

    public void onContinue(View v) {

        if (!signPad.isValidSig()) {
            showDialog("Error", "Please put your signature.");
            return;
        }

        Bitmap img = signPad.fetchSignature();
        String strimg = UtilityFunction.encodeTobase64(img);
        // Log.i(TAG, strimg) ;

        ModelSignature sig = new ModelSignature();
        sig.setSaleId(m_recSale.getTxId());
        sig.setSignature(strimg);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            if (OpenTransations.isVMActivated) {
                m_client.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
            } else {
                m_client_amex.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
            }
        } else {
            if (m_client.isLoggedIn()) {
                m_client.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
            } else if (m_client_amex.isLoggedIn()) {
                m_client_amex.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
            }
        }
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == 5000) {
            if (data != null) {
                if (data instanceof SimpleAck) {

                    //this.pushActivity(SalesReceiptActivity.class, m_recSale);
                    //finish();
                    OpenTransations.isUpdatedSignature = true;
                    showInfo("E-Receipt", "Signature Updated successfully.", 100);

                }
            }
        }

    }

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 100) {
            finish();
            //gotoHome() ;
            //fireOpenTxList();
        } else if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
        }
    }

    private void InitViews() {

        screensize = getScreenSize();

        txtSignHere = (TextView) findViewById(R.id.txtSignHere);
        txtSignHere.setTypeface(TypeFaceUtils.setSignatureFont(this));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        txtAmountValue = (TextView) findViewById(R.id.txtAmountValue);
        txtAmountValue.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        txtConfirmation = (TextView) findViewById(R.id.txtConfirmation);
        txtConfirmation.setTypeface(TypeFaceUtils.setRobotoMedium(this));
        txtConfirmation.setMovementMethod(new ScrollingMovementMethod());

        txtEnglish = (TextView) findViewById(R.id.txtEnglish);
        txtEnglish.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));
        txtEnglish.setOnClickListener(onClickListener);

        txtSinhala = (TextView) findViewById(R.id.txtSinhala);
        txtSinhala.setText("isxy,");
        txtSinhala.setTextSize(19.0f);
        txtSinhala.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
        txtSinhala.setOnClickListener(onClickListener);

        txtTamil = (TextView) findViewById(R.id.txtTamil);
        txtTamil.setText("jkpo;");
        txtTamil.setTextSize(19.0f);
        txtTamil.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));
        txtTamil.setOnClickListener(onClickListener);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setTypeface(TypeFaceUtils.setRobotoMedium(this));

        btnApproved = (Button) findViewById(R.id.btnApproved);
        btnApproved.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        signPad = (DrawingArea) findViewById(R.id.signaturePad);

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());


        try {

            String strFooterText = "I <b>" + m_recSale.getCardHolder()
                    + "</b> , agree to pay the above total according to my card holder agreement. Card ending <b>"
                    + m_recSale.getCcLast4() + " </b> . Authorisation code <b>" + m_recSale.getApprovalCode() + "</b>";

            txtConfirmation.setText(Html.fromHtml(strFooterText));
            txtConfirmation.setTypeface(TypeFaceUtils.setRobotoMedium(UpdateSignaturePad.this));

            if (screensize > 6.8) {
                txtConfirmation.setTextSize(20.0f);
            }


            if (lang_status == 0) {
                txtEnglish.setTextColor(getResources().getColor(R.color.signin_btn));

                if (screensize > 6.8) {
                    txtEnglish.setTextSize(24.0f);
                    txtSignHere.setTextSize(108.f);
                }

            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved, "",
                        m_recSale.getCcLast4(), m_recSale.getApprovalCode(), m_recSale.getCardHolder());
                txtSinhala.setTextColor(getResources().getColor(R.color.signin_btn));
            } else if (lang_status == LangPrefs.LAN_TA) {
//			tamlang_select.Apply_Signature(txtAmount, txtSignHere, txtConfirmation, btnSave, btnClear, btnApproved, "",
//					m_salesRes.getCcLast4(), m_salesRes.getApprovalCode());
                tamlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved, "",
                        m_recSale.getCcLast4(), m_recSale.getApprovalCode());
                txtTamil.setTextColor(getResources().getColor(R.color.signin_btn));
            } else {
                // do nothing
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        signPad.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                txtSignHere.setVisibility(View.GONE);
                return false;
            }
        });

    }

    private View.OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {

                case R.id.txtEnglish: {
                    txtAmount.setText("Amount");
                    txtAmount.setTypeface(TypeFaceUtils.setRobotoRegular(UpdateSignaturePad.this));

                    txtSignHere.setText("Sign Here");
                    txtSignHere.setTypeface(TypeFaceUtils.setSignatureFont(UpdateSignaturePad.this));

                    btnSave.setText("Save");
                    btnSave.setTypeface(TypeFaceUtils.setRobotoMedium(UpdateSignaturePad.this));

                    btnClear.setTypeface(TypeFaceUtils.setRobotoMedium(UpdateSignaturePad.this));
                    btnClear.setText("Clear Signature");

                    btnApproved.setText("Approved");
                    btnApproved.setTypeface(TypeFaceUtils.setRobotoRegular(UpdateSignaturePad.this));

                    String strFooterText = "I <b>" + m_recSale.getCardHolder()
                            + "</b> , agree to pay the above total according to my card holder agreement. Card ending <b>"
                            + m_recSale.getCcLast4() + " </b> . Authorisation code <b>" + m_recSale.getApprovalCode() + "</b>";

                    // txtName.setText(m_salesRes.getCardHolder()) ;
                    txtConfirmation.setText(Html.fromHtml(strFooterText));
                    txtConfirmation.setTypeface(TypeFaceUtils.setRobotoMedium(UpdateSignaturePad.this));

                    txtEnglish.setTextColor(getResources().getColor(R.color.signin_btn));
                    txtTamil.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    txtSinhala.setTextColor(getResources().getColor(R.color.notregistered_signup_color));

                    if (screensize < 6.8) {

                        txtAmount.setTextSize(13.0f);
                        btnApproved.setTextSize(12.0f);
                        btnClear.setTextSize(12.0f);
                        btnSave.setTextSize(16.0f);
                        txtSignHere.setTextSize(58.0f);
                        txtConfirmation.setTextSize(13.0f);

                    }
                    if (screensize > 8.0) {
                        txtSignHere.setTextSize(108.0f);
                        txtAmount.setTextSize(30.0f);
                        txtConfirmation.setTextSize(30.0f);
                        btnSave.setTextSize(30.0f);
                        btnClear.setTextSize(30.0f);
                        btnApproved.setTextSize(30.0f);
                    }

                    break;
                }

                case R.id.txtTamil: {
                    tamlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved,
                            "", m_recSale.getCcLast4(), m_recSale.getApprovalCode());
                    txtTamil.setTextColor(getResources().getColor(R.color.signin_btn));
                    txtSinhala.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    txtEnglish.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    break;
                }

                case R.id.txtSinhala: {
                    sinlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved,
                            "", m_recSale.getCcLast4(), m_recSale.getApprovalCode(), m_recSale.getCardHolder());
                    txtSinhala.setTextColor(getResources().getColor(R.color.signin_btn));
                    txtTamil.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    txtEnglish.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    break;
                }
            }
        }
    };

    public double getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
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

}
