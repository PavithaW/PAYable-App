package com.cba.payable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.ResendSaleReceiptReq;
import com.mpos.pojo.SaleReceiptReq;
import com.mpos.pojo.TxSaleRes;
import com.mpos.pojo.TxVoidRes;
import com.mpos.storage.RecSale;
import com.mpos.util.CHARFilter;
import com.mpos.util.LangPrefs;
import com.mpos.util.OrderTrackPref;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.SMSValidator;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendTextActivity extends GenericActivity {

    TextView txtTitle, txtPaymentDetails, txtAmount, txtAmountValue,
            txtCardName, txtCardNameValue, txtApprovalCode,
            txtApprovalCodeValue, txtSendVia;
    EditText edtPhone;
    Button btnSend;
    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;
    private TxSaleRes m_salesRes;
    private RecSale m_recSale;
    private TxVoidRes m_voidRes;
    private boolean isSalesReceipt;
    private String currencyTypeLang;
    private int currencyType;
    private int cardType;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_send_text);

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof TxSaleRes) {
                    m_salesRes = (TxSaleRes) data[i];
                    currencyType = m_salesRes.getCurrencyType();
                }

                if (data[i] instanceof RecSale) {
                    m_recSale = (RecSale) data[i];
                    currencyType = m_recSale.getCurrencyType();
                }

                if (data[i] instanceof TxVoidRes) {
                    m_voidRes = (TxVoidRes) data[i];
                }
            }
        }

        InitViews();

        DecimalFormat df = new DecimalFormat("####0.00");

        if (m_salesRes != null) {

            //FORMAT NUMBER ACCORDING TO UNICODE
            String amount = "";
            if (lang_status == LangPrefs.LAN_EN) {
                amount = df.format(m_salesRes.getAmount());
            } else if (lang_status == LangPrefs.LAN_SIN) {
                amount = df.format(m_salesRes.getAmount()).replace(".", "'");
            } else if (lang_status == LangPrefs.LAN_TA) {
                amount = df.format(m_salesRes.getAmount());
            }

            txtCardNameValue.setText(m_salesRes.getCardHolder());
            //txtAmountValue.setText(df.format(m_salesRes.getAmount()));
            txtAmountValue.setText(amount + " " + currencyTypeLang);
            txtApprovalCodeValue.setText(m_salesRes.getApprovalCode());

            isSalesReceipt = true;
        }

        if (m_recSale != null) {

            //FORMAT NUMBER ACCORDING TO UNICODE
            String amount = "";
            if (lang_status == LangPrefs.LAN_EN) {
                amount = df.format(m_recSale.getAmount());
            } else if (lang_status == LangPrefs.LAN_SIN) {
                amount = df.format(m_recSale.getAmount()).replace(".", "'");
            } else if (lang_status == LangPrefs.LAN_TA) {
                amount = df.format(m_recSale.getAmount());
            }

            txtCardNameValue.setText(m_recSale.getCardHolder());
            //txtAmountValue.setText(df.format(m_recSale.getAmount()));
            txtAmountValue.setText(amount + " " + currencyTypeLang);
            txtApprovalCodeValue.setText(m_recSale.getApprovalCode());

            isSalesReceipt = false;
        }


    }

    public void onSend(View v) {

        String strMobile = edtPhone.getText().toString().trim();

        if (strMobile == null || strMobile.trim().length() == 0) {
            showDialog("Error", "Please enter the mobile Number");
            return;
        }

        // if 0 lnth should 10
        if (strMobile.startsWith("0")) {
            if (strMobile.length() != 10) {
                showDialog("Error", "Please enter a valid mobile number with 10 digits");
                return;
            }
        }

        if (strMobile.length() < 9) {
            showDialog("Error", "Please enter valid mobile number, without start country code.");
            return;
        }

        strMobile = strMobile.trim();

        int res = CHARFilter.isBloackedCharExist(strMobile);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }


        if (!SMSValidator.validatePhoneNumber(strMobile)) {
            showDialog("Error", "Please enter valid mobile number.");
            return;
        }


        // 1 = resend 0 = normal
        if (Config.RECEIPT_STATE == Config.RECEIPT_STATE_NORMAL) {

            if ((m_salesRes == null) && (m_recSale == null)) {
                return;
            }

            SaleReceiptReq req = new SaleReceiptReq();

            if (m_salesRes != null) {

                req.setTxId(m_salesRes.getTxId());
                req.setCardHolder(m_salesRes.getCardHolder());
                req.setCcLast4(m_salesRes.getCcLast4());
                req.setAmount(m_salesRes.getAmount());
                req.setCardType(m_salesRes.getCardType());
                //req.setServerTime(m_salesRes.getServerTime());

                Log.i("DATE JEYLOGS" , "Date to send server : " + m_salesRes.getServerTime());
                // create  date object from this string
                DateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.ENGLISH);
                Date date = null;
                try{
                    date = format.parse(m_salesRes.getServerTime());
                }catch (Exception e){

                }
                req.setServerTime(date);


                req.setApprovalCode(m_salesRes.getApprovalCode());
                req.setReceiptType(SaleReceiptReq.RECEIPT_SALE);
                // req.setEmail(strEmail) ;
                req.setMobile(strMobile);

                req.setBatchNo(m_salesRes.getBatchNo());
                req.setStn(m_salesRes.getStn());
                req.setRrn(m_salesRes.getRrn());
                req.setCurrencyType(m_salesRes.getCurrencyType());

                cardType = m_salesRes.getCardType();
            }

            if (m_recSale != null) {

                req.setTxId(m_recSale.getTxId());
                req.setCardHolder(m_recSale.getCardHolder());
                req.setCcLast4(m_recSale.getCcLast4());
                req.setAmount(m_recSale.getAmount());
                req.setCardType(m_recSale.getCardType());
                req.setServerTime(m_recSale.getVoidTs());
                req.setApprovalCode(m_recSale.getApprovalCode());
                req.setReceiptType(SaleReceiptReq.RECEIPT_VOID);
                // req.setEmail(strEmail) ;
                req.setMobile(strMobile);

                // Log.i(TAG, "BATCH : " + m_recSale.getBatchNo());
                req.setBatchNo(m_recSale.getBatchNo());
                // Log.i(TAG, "RRN : " + m_voidRes.getRrn());
                req.setStn(m_voidRes.getStn());
                req.setRrn(m_voidRes.getRrn());
                req.setCurrencyType(m_recSale.getCurrencyType());

                cardType = m_recSale.getCardType();
            }


            if (cardType == RecSale.CARD_VISA || cardType == RecSale.CARD_MASTER) {
                m_client.smsReceipt(100, ProgressDialogMessagesUtil.eReceiptTranslation(lang_status), req);
            } else if (cardType == RecSale.CARD_AMEX || cardType == RecSale.CARD_DINERS) {
                m_client_amex.smsReceipt(100, ProgressDialogMessagesUtil.eReceiptTranslation(lang_status), req);
            }


        } else {
            //resend
            ResendSaleReceiptReq resendSaleReceiptReq = new ResendSaleReceiptReq();
            resendSaleReceiptReq.setMobile(strMobile);
            resendSaleReceiptReq.setTxId(m_recSale.getTxId());

            if (m_recSale.getCardType() == RecSale.CARD_VISA || m_recSale.getCardType() == RecSale.CARD_MASTER) {
                m_client.resendSmsReceipt(101, ProgressDialogMessagesUtil.eReceiptResendTranslation(lang_status), resendSaleReceiptReq);
            } else if (m_recSale.getCardType() == RecSale.CARD_AMEX || m_recSale.getCardType() == RecSale.CARD_DINERS) {
                m_client_amex.resendSmsReceipt(101, ProgressDialogMessagesUtil.eReceiptResendTranslation(lang_status), resendSaleReceiptReq);
            }
        }

	/*	if (lang_status == LangPrefs.LAN_EN) {

			m_client.smsReceipt(100, "Sending E-Receipt", req);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.smsReceipt(100, ",�m; hj�ka mj;S'", req);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.smsReceipt(100, "gw;Wr;rPl;L mDg;ggLfpd;wJ", req);

		} else {
			// do nothing
		}*/

        //m_client.smsReceipt(100, "Sending E-Receipt", req);

    }

    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 210) {

            if (isSalesReceipt) {
                //SalePad.isNewSale = true ;
                //closeReceiptScreen() ;

                if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {

                    SalePad.isNewSale = true;
                    SalePad_Keyentry.isNewSale = true;

                    OrderTrackingActivity.isNewSale = true;
                    OrderTrackingActivity.isClose = true;

                    SalePad.signal_val = SalePad.SIGNAL_CLOSE;
                    SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
                    SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
                } else {
                    SalePad.isNewSale = true;
                    SalePad_Keyentry.isNewSale = true;

                    SalePad.signal_val = SalePad.SIGNAL_CLOSE;
                    SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
                    SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
                }
            }
        } else if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
            logOut();
            //finish();
        }

        // 1 = resend 0 = normal
        if (Config.RECEIPT_STATE == Config.RECEIPT_STATE_NORMAL) {
            closeReceiptScreen();
        } else {
            Config.RECEIPT_STATE = Config.RECEIPT_STATE_NORMAL;
            closeResendScreen();
        }
    }

    /*public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 210) {

            if (isSalesReceipt) {
                //SalePad.isNewSale = true ;
                //closeReceiptScreen() ;

                if (OrderTrackPref.getOrderStatus(getApplicationContext()) == 1) {
                    DsSalesAudio.isClose = true;
                    SalePad.isNewSale = true;
                    SalePad_Keyentry.isNewSale = true;
                    OrderTrackingActivity.isNewSale = true;
                    OrderTrackingActivity.isClose = true;
                    SalePad.signal_val = SalePad.SIGNAL_CLOSE;
                    SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
                } else {
                    DsSalesAudio.isClose = true;
                    SalePad.isNewSale = true;
                    SalePad_Keyentry.isNewSale = true;

                    SalePad.signal_val = SalePad.SIGNAL_CLOSE;
                    SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
                    SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
                }
            }
        }

        // 1 = resend 0 = normal
        if (Config.RECEIPT_STATE == Config.RECEIPT_STATE_NORMAL) {
            closeReceiptScreen();
        } else {
            closeResendScreen();
        }
    }*/

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == 100) {
            showInfo("E-Receipt", "Receipt was sent successfully", 210);
        } else if (callerId == 101) {
            showInfo("E-Receipt", "Receipt was resent successfully", 210);
        }
    }

    private void InitViews() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtPaymentDetails = (TextView) findViewById(R.id.txtPaymentDetails);
        txtPaymentDetails.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtAmountValue = (TextView) findViewById(R.id.txtAmountValue);
        txtAmountValue.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtCardName = (TextView) findViewById(R.id.txtCardName);
        txtCardName.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtCardNameValue = (TextView) findViewById(R.id.txtCardNameValue);
        txtCardNameValue.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtApprovalCode = (TextView) findViewById(R.id.txtApprovalCode);
        txtApprovalCode.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtApprovalCodeValue = (TextView) findViewById(R.id.txtApprovalCodeValue);
        txtApprovalCodeValue.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtSendVia = (TextView) findViewById(R.id.txtSendVia);
        txtSendVia.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

            currencyTypeLang = UtilityFunction.getCurrencyTypeString(currencyType);

        } else if (lang_status == LangPrefs.LAN_SIN) {

            currencyTypeLang = UtilityFunction.getCurrencyTypeStringSI(currencyType);

            sinlang_select.Apply_SendText(txtTitle, txtPaymentDetails,
                    txtAmount, txtCardName, txtApprovalCode, txtSendVia,
                    edtPhone, btnSend, txtAmountValue);

        } else if (lang_status == LangPrefs.LAN_TA) {

            currencyTypeLang = UtilityFunction.getCurrencyTypeStringTA(currencyType);

            tamlang_select.Apply_SendText(txtTitle, txtPaymentDetails,
                    txtAmount, txtCardName, txtApprovalCode, txtSendVia,
                    edtPhone, btnSend, txtAmountValue);
        } else {
            // do nothing
        }

        edtPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2,
                                      int arg3) {

                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {
                        sinlang_select = new Sinhala_LangSelect(
                                getApplicationContext(), SendTextActivity.this);
                        sinlang_select.Apply_SendText(txtTitle,
                                txtPaymentDetails, txtAmount, txtCardName,
                                txtApprovalCode, txtSendVia, edtPhone, btnSend, txtAmountValue);

                    } else {

                        edtPhone.setTypeface(TypeFaceUtils
                                .setLatoRegular(SendTextActivity.this));
                    }
                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {
                        tamlang_select = new Tamil_LangSelect(
                                getApplicationContext(), SendTextActivity.this);
                        tamlang_select.Apply_SendText(txtTitle,
                                txtPaymentDetails, txtAmount, txtCardName,
                                txtApprovalCode, txtSendVia, edtPhone, btnSend, txtAmountValue);
                    } else {

                        edtPhone.setTypeface(TypeFaceUtils
                                .setLatoRegular(SendTextActivity.this));
                    }

                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNo = s.toString();

                if (phoneNo.startsWith("+")) {
                    s.replace(0, 1, "0");
                } else if (phoneNo.startsWith("+9")) {
                    s.replace(0, 2, "0");
                } else if (phoneNo.startsWith("+94")) {
                    s.replace(0, 3, "0");
                } else if (phoneNo.startsWith("94")) {
                    s.replace(0, 2, "0");
                }

            }
        });

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Config.RECEIPT_STATE = Config.RECEIPT_STATE_NORMAL;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        //Config.RECEIPT_STATE = Config.RECEIPT_STATE_NORMAL;
    }
}
