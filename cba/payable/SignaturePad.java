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
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;

import java.text.DecimalFormat;

public class SignaturePad extends GenericActivity {

    private DrawingArea signPad;

    private TxSaleRes m_salesRes;

    TextView txtSignHere, txtConfirmation, txtAmount, txtAmountValue;
    Button btnSave, btnClear, btnApproved;
    TextView txtEnglish, txtSinhala, txtTamil;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    double screensize;
    DecimalFormat df;
    int cardType = -1;

    public static boolean isClose = false;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_signature);
        setContentView(R.layout.activity_signature);

        df = new DecimalFormat("####0.00");

		/*
         * TextView txtAmount = (TextView) findViewById(R.id.txtAmount);
		 * TextView txtName = (TextView) findViewById(R.id.txtACCName);
		 */

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof TxSaleRes) {
                    m_salesRes = (TxSaleRes) data[i];
                    cardType = m_salesRes.getCardType();
                    InitViews();
                    txtAmountValue.setText(df.format(m_salesRes.getAmount())
                            + " "
                            + UtilityFunction.getCurrencyTypeString(m_salesRes.getCurrencyType()));
                }
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isClose) {
            isClose = false;
            finish();
        }
    }

    public void onClear(View v) {
        signPad.clearPad();
    }

    public void onBackPressed() {

    }

    public void onContinue(View v) {

        if (!signPad.isValidSig()) {
            showDialog("Error", "Please put your signature.");
            return;
        }

        Bitmap img = signPad.fetchSignature();
        String strimg = UtilityFunction.encodeTobase64(img);
        // Log.i(TAG, strimg) ;

        ModelSignature sig = new ModelSignature();
        sig.setSaleId(m_salesRes.getTxId());
        sig.setSignature(strimg);

        // sig.setSaleId(376);

        if (cardType != -1) {
            switch (cardType) {
                case 1: {
                    //visa card
                    m_client.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
                    break;
                }
                case 2: {
                    //amex card
                    m_client_amex.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
                    break;
                }
                case 3: {
                    //master card
                    m_client.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
                    break;
//                    m_client.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
                }
                case 4: {
                    //diners card
                    m_client_amex.signature(5000, ProgressDialogMessagesUtil.signPadTranslation(lang_status), sig);
                    break;
                }
            }
        } else {

        }

		/*if (lang_status == LangPrefs.LAN_EN) {

			m_client.signature(5000, "Uploading signature.Please wait.", sig);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.signature(5000, "w;aik we;=<;a flfrñka mj;S' lreKdlr /|S isákak'", sig);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.signature(5000, "ifnaாg;gk;  gjpNtw;wg;gLfpd;wJ. rw;W nghWj;jpUq;fs;", sig);

		} else {
			// do nothing
		}*/


    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == 5000) {
            if (data != null) {
                if (data instanceof SimpleAck) {

                    //this.pushActivity(EReceipt.class, m_salesRes);

                    this.pushActivity(SalesReceiptActivity.class, m_salesRes);
                    finish();

                    // Intent intent = new Intent(this, EReceipt.class);
                    // startActivity(intent);
                    // finish() ;
                }
            }
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
        txtSinhala.setTextSize(18.0f);
        txtSinhala.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));
        txtSinhala.setOnClickListener(onClickListener);

        txtTamil = (TextView) findViewById(R.id.txtTamil);
        txtTamil.setText("jkpo;");
        txtTamil.setTextSize(18.0f);
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

            String strFooterText = "I <b>" + m_salesRes.getCardHolder()
                    + "</b> , agree to pay the above total according to my card holder agreement. Card ending <b>"
                    + m_salesRes.getCcLast4() + " </b> . Authorisation code <b>" + m_salesRes.getApprovalCode() + "</b>";

            txtConfirmation.setText(Html.fromHtml(strFooterText));
            txtConfirmation.setTypeface(TypeFaceUtils.setRobotoMedium(SignaturePad.this));

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
                        m_salesRes.getCcLast4(), m_salesRes.getApprovalCode(), m_salesRes.getCardHolder());
                txtSinhala.setTextColor(getResources().getColor(R.color.signin_btn));
            } else if (lang_status == LangPrefs.LAN_TA) {
//			tamlang_select.Apply_Signature(txtAmount, txtSignHere, txtConfirmation, btnSave, btnClear, btnApproved, "",
//					m_salesRes.getCcLast4(), m_salesRes.getApprovalCode());
                tamlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved, "",
                        m_salesRes.getCcLast4(), m_salesRes.getApprovalCode());
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

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 100) {
            finish();
            //gotoHome() ;
            //fireOpenTxList();
        } else if (callerId == 10001) {
            Home.isPortalChanged = true;
            DsSalesAudio.isClose = true;
            OrderTrackingActivity.isClose = true;
            SalePad.signal_val = SalePad.SIGNAL_CLOSE;
            SalePad_Keyentry.signal_val = SalePad.SIGNAL_CLOSE;
            SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
            logoutAllNow();
        }
    }

    private View.OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {

                case R.id.txtEnglish: {
                    txtAmount.setText("Amount");
                    txtAmount.setTypeface(TypeFaceUtils.setRobotoRegular(SignaturePad.this));

                    txtSignHere.setText("Sign Here");
                    txtSignHere.setTypeface(TypeFaceUtils.setSignatureFont(SignaturePad.this));

                    btnSave.setText("Save");
                    btnSave.setTypeface(TypeFaceUtils.setRobotoMedium(SignaturePad.this));

                    btnClear.setTypeface(TypeFaceUtils.setRobotoMedium(SignaturePad.this));
                    btnClear.setText("Clear Signature");

                    btnApproved.setText("Approved");
                    btnApproved.setTypeface(TypeFaceUtils.setRobotoRegular(SignaturePad.this));

                    String strFooterText = "I <b>" + m_salesRes.getCardHolder()
                            + "</b> , agree to pay the above total according to my card holder agreement. Card ending <b>"
                            + m_salesRes.getCcLast4() + " </b> . Authorisation code <b>" + m_salesRes.getApprovalCode() + "</b>";

                    // txtName.setText(m_salesRes.getCardHolder()) ;
                    txtConfirmation.setText(Html.fromHtml(strFooterText));
                    txtConfirmation.setTypeface(TypeFaceUtils.setRobotoMedium(SignaturePad.this));

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
                            "", m_salesRes.getCcLast4(), m_salesRes.getApprovalCode());
                    txtTamil.setTextColor(getResources().getColor(R.color.signin_btn));
                    txtSinhala.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    txtEnglish.setTextColor(getResources().getColor(R.color.notregistered_signup_color));
                    break;
                }

                case R.id.txtSinhala: {
                    sinlang_select.Apply_Signature(txtAmount, txtSignHere, btnSave, btnClear, btnApproved,
                            "", m_salesRes.getCcLast4(), m_salesRes.getApprovalCode(), m_salesRes.getCardHolder());
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
