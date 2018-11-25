package com.cba.payable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.CRSale;
import com.mpos.pojo.TxDSSaleReq;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;

public class SwipeLast4 extends GenericActivity {

    private CRSale m_sale;
    private TxDSSaleReq ds_sale;
    // private TxEmvReq testobj ;

    private boolean is_DsReader = false;

    private EditText txtL4;

    TextView txtTitle, txtPleaseEnter;
    EditText edtDigit;
    Button btnSubmit;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    String maskedPan;

    protected void onInitActivity(APIData... data) {

        // setContentView(R.layout.layout_l4);

        setContentView(R.layout.activity_last_digit);
        // txtL4 = (EditText) findViewById(R.id.txt_l4_num);

        InitViews();

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof CRSale) {
                    m_sale = (CRSale) data[i];
                }

                if (data[i] instanceof TxDSSaleReq) {
                    ds_sale = (TxDSSaleReq) data[i];
                    is_DsReader = true;
                    maskedPan = ds_sale.getMaskedPan();
                }

                // TxEmvReq
            }

        }
    }

    private void _txOnDevice1() {
        String strL4 = edtDigit.getText().toString();

        if (strL4 == null || strL4.length() != 4) {
            showDialog("Error", "Please enter last 4 digits.");
            return;
        }

        if (m_sale == null) {
            showDialog("Error", "Invalid swipe.Please try again.");
            return;
        }

        m_sale.setKeypadL4(strL4);
        //storeErrorLog("Sending emv transaction request for amount : " + m_amount.getValue()) ;
        //m_client.sale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), m_sale);

        if (maskedPan != null) {
            String firstFourDigit = maskedPan.substring(0, 4);

            int maskedPanNumeric = Integer.parseInt(firstFourDigit);
            int firstDigit = Integer.parseInt(maskedPan.substring(0, 1));
            int firstTwoDigits = Integer.parseInt(maskedPan.substring(0, 2));
            int firstThreeDigits = Integer.parseInt(maskedPan.substring(0, 3));

            if (firstDigit == 4) {
                // visa card
                if (m_client.isLoggedIn()) {
                    m_client.sale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), m_sale);
                } else {
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with VISA/MASTER system to proceed visa card.");
                }
            } else if ((maskedPanNumeric >= 2221 && maskedPanNumeric <= 2720) ||
                    (firstTwoDigits >= 51 && firstTwoDigits <= 55)) {
                //master card
                if (m_client.isLoggedIn()) {
                    m_client.sale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), m_sale);
                } else {
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with VISA/MASTER system to proceed master card.");
                }
            } else if (firstTwoDigits == 34 || firstTwoDigits == 37) {

                //american express card
                if (m_client_amex.isLoggedIn()) {
                    m_client_amex.sale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), m_sale);
                } else {
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with AMEX system to proceed AMEX card.");
                }

            } else if ((firstThreeDigits >= 300 && firstThreeDigits <= 305) || firstThreeDigits == 309 ||
                    firstTwoDigits == 36 || firstTwoDigits == 38 || firstTwoDigits == 39) {
                //diners club international
                if (m_client_amex.isLoggedIn()) {
                    m_client_amex.sale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), m_sale);
                } else {
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with AMEX system to proceed diners club card.");
                }
            }
        }

	/*	if (lang_status == LangPrefs.LAN_EN) {

			m_client.sale(4000, "Please wait.", m_sale);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.sale(4000, "lreKdlr /|S isákak'", m_sale);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.sale(4000, "rw;W nghWj;jpUq;fs;", m_sale);

		} else {
			// do nothing
		}*/


    }

    private void _txOnDsDevice() {

        String strL4 = edtDigit.getText().toString();

        if (strL4 == null || strL4.length() != 4) {
            showDialog("Error", "Please enter last 4 digits.");
            return;
        }

        if (ds_sale == null) {
            showDialog("Error", "Invalid swipe.Please try again.");
            return;
        }

        String cardL4 = ds_sale.getMaskedPan().substring(ds_sale.getMaskedPan().length() - 4);

        if (!cardL4.equalsIgnoreCase(strL4)) {
            showDialog("Error", "Last 4 digit doesn't match.");
            return;
        }

        // showDialog("Error", "ok.");
        // m_client.sale(4000, "Please wait.", m_sale);

        ds_sale.setKeypadL4(strL4);
        //Log.i(TAG, "Req data 2:" + ds_sale.getJson());

        storeMessageLog("Sending swipe transaction request for amount : " + ds_sale.getAmount());

        if (maskedPan != null) {
            String firstFourDigit = maskedPan.substring(0, 4);

            int maskedPanNumeric = Integer.parseInt(firstFourDigit);
            int firstDigit = Integer.parseInt(maskedPan.substring(0, 1));
            int firstTwoDigits = Integer.parseInt(maskedPan.substring(0, 2));
            int firstThreeDigits = Integer.parseInt(maskedPan.substring(0, 3));

            if (firstDigit == 4) {
                // visa card
                m_client.dsSwipeSale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), ds_sale);
            } else if ((maskedPanNumeric >= 2221 && maskedPanNumeric <= 2720) ||
                    (firstTwoDigits >= 51 && firstTwoDigits <= 55)) {
                //master card
                m_client.dsSwipeSale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), ds_sale);
            } else if (firstTwoDigits >= 34 && firstTwoDigits <= 37) {
                //american express card
                m_client_amex.dsSwipeSale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), ds_sale);
            } else if ((firstThreeDigits >= 300 && firstThreeDigits <= 305) || firstThreeDigits == 3095 ||
                    firstTwoDigits == 36 || firstTwoDigits == 38 || firstTwoDigits == 39) {
                //diners club international
                m_client_amex.dsSwipeSale(4000, ProgressDialogMessagesUtil.swipeTranslation(lang_status), ds_sale);
            }
        }
    }

    public void onSendTx(View v) {

        if (!is_DsReader) {
            _txOnDevice1();
            return;
        }

        _txOnDsDevice();

    }

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 210) {
            // fireOpenTxList();
            SalePad.isSaleTimeout = true;
            finish();
        }

        if (callerId == 220) {
            if (ds_sale != null) {
                Amount amount = new Amount(ds_sale.getAmount(), 0, ds_sale.getCurrencyType());
                //Amount amount = new Amount(ds_sale.getAmount());
                this.pushActivity(DsSalesAudio.class, amount);
                finish();
            } else {
                finish();
            }
            return;
        }else if (callerId == 10001) {
            logoutAllNow();
            logOut();
        }
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        if (ds_sale != null) {
            storeErrorLog("Received error in swipe reponse for amount : " + ds_sale.getAmount() + "  Error code  : " + e.getErrcode());
        }

        if (e.getErrcode() == ApiException.SWIPE_ON_ICC_EXECPTION) {
            showDialog("Error", "Can't swipe the chip card.please insert it.", 220);
            return;
        }

        if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
            showDialog("Error",
                    "Error in Communication has occurred. Please Check your last transaction and ensure the transaction has been completed successfully.",
                    210);
            return;
        }

        super.onCallError(api, callerId, e);
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == 4000) {
            if (data != null) {
                if (data instanceof TxSaleRes) {

                    //PLog.i(TAG, "Received json response from server");

                    if (ds_sale != null) {
                        storeMessageLog("Received success reponse for swipe tx amount : " + ds_sale.getAmount());
                    }

                    TxSaleRes salesRes = (TxSaleRes) data;
                    salesRes.setCurrencyType(ds_sale.getCurrencyType());
                    this.pushActivity(SignaturePad.class, salesRes);
                    finish();


                    // load signature pad

                }
            }
        }

    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

//		txtYourPrivacy = (TextView) findViewById(R.id.txtYourPrivacy);
//		txtYourPrivacy.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtPleaseEnter = (TextView) findViewById(R.id.txtPleaseEnter);
        txtPleaseEnter.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        edtDigit = (EditText) findViewById(R.id.edtDigit);
        edtDigit.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_LastDigit(txtTitle, txtPleaseEnter, edtDigit, btnSubmit);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_LastDigit(txtTitle, txtPleaseEnter, edtDigit, btnSubmit);
        } else {
            // do nothing
        }

        edtDigit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), SwipeLast4.this);
                        sinlang_select.Apply_LastDigit(txtTitle, txtPleaseEnter, edtDigit, btnSubmit);
                    } else {
                        edtDigit.setTypeface(TypeFaceUtils.setLatoRegular(SwipeLast4.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), SwipeLast4.this);
                        tamlang_select.Apply_LastDigit(txtTitle, txtPleaseEnter, edtDigit, btnSubmit);
                        ;
                    } else {

                        edtDigit.setTypeface(TypeFaceUtils.setLatoRegular(SwipeLast4.this));
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
    }

}
