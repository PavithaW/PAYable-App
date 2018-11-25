package com.cba.payable;

import android.app.Dialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.TxVoidReq;
import com.mpos.pojo.TxVoidRes;
import com.mpos.storage.RecSale;
import com.mpos.util.Crypto;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TxDetail extends GenericActivity {

    private static final String LOG_TAG = "payable " + TxDetail.class.getSimpleName();

    private static final int CALL_VOID = 250;
    public static boolean IS_SIGN_BTN_VISIBLE;

    private RecSale m_recSale;

    TextView txtTitle, txtCCLast4, txtPaidWith, txtCardName, txtApprovalCode,
            txtApprovalCodeValue, txtDate, txtTime, txtAmount;
    TextView lblSalesDate, lblVoidDate, lblTrackingId, txtVoidDate,
            txtVoidTime, txtTrackingId;

    Button btnVoid, btnSign, btnReceipt;
    ImageView imgCardType;

    Dialog void_dialog, void_confirm_dialog;

    int lang_status = 0;
    DecimalFormat df;
    // boolean track_id_status=false;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    private LinearLayout installmentView;
    private TextView installment_tv;
    private int installment;
    private TextView installment_amount_tv;
    int cardType = -1;
    private SimpleDateFormat sdf_date;
    private SimpleDateFormat sdf_time;
    private int txType;
    public static boolean IS_FROM_OPEN_TX = false;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_detailtx);

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof RecSale) {
                    m_recSale = (RecSale) data[i];
                    cardType = m_recSale.getCardType();
                }
            }
        }

        df = new DecimalFormat("####0.00");
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm a",
                Locale.US);*/

        SimpleDateFormat sdf_date = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        //SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a"); //Wed Nov 29 13:32:16 GMT+05:30 2017
        SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a", Locale.US);

		/*
         * TextView txtName = (TextView) findViewById(R.id.txtName); TextView
		 * txtCCLast4 = (TextView) findViewById(R.id.txtCCLast4); TextView
		 * txtSale = (TextView) findViewById(R.id.txtSale); Button btn =
		 * (Button) findViewById(R.id.btnVoid); ImageView imgCard = (ImageView)
		 * findViewById(R.id.imgCard);
		 *
		 * TextView txtVoidlbl = (TextView) findViewById(R.id.txtVoidDatelbl);
		 * TextView txtSaleDate = (TextView) findViewById(R.id.txtSaleDate);
		 * TextView txtVoidDate = (TextView) findViewById(R.id.txtVoidDate);
		 * TextView txtApprovaCode = (TextView)
		 * findViewById(R.id.txtApprovalCode);
		 */
        if (m_recSale != null) {
            installment = m_recSale.getInstallment();

            if (m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID
                    || m_recSale.getStatus() == RecSale.STATUS_OPEN_VOID) {

                ShowTrackId();
                InitViews();

                btnVoid.setVisibility(View.GONE);
                btnReceipt.setVisibility(View.GONE);

                // txtVoidlbl.setVisibility(View.VISIBLE);
                // txtVoidDate.setVisibility(View.VISIBLE);
                txtVoidDate.setText(sdf_date.format(m_recSale.getVoidTs()));
                txtVoidTime.setText(sdf_time.format(m_recSale.getVoidTs()));

                System.out.println("dateTIme 1v " + m_recSale.getVoidTs());

            } else {

                ShowTrackId();
                InitViews();

                // btnVoid.setVisibility(View.VISIBLE);
                // txtVoidlbl.setVisibility(View.INVISIBLE);
                // txtVoidDate.setVisibility(View.INVISIBLE);

            }

            txType = m_recSale.getTxType();
            Log.d(LOG_TAG, "txType : " + txType);

			/*if(m_recSale.getTxType() == RecSale.MANUAL_TRANSACTION) {
                btnSign.setVisibility(View.GONE);
				btnReceipt.setVisibility(View.GONE);
			}*/


            if (txType == RecSale.MANUAL_TRANSACTION) {
                txtCardName.setText("Manual Transaction");
                btnSign.setVisibility(View.GONE);
            } else {
                txtCardName.setText(m_recSale.getCardHolder().trim());
            }

            showHideResendBtn(txType);

            txtCCLast4.setText(m_recSale.getCcLast4());
            txtAmount.setText(df.format(m_recSale.getAmount()) + " " + UtilityFunction.getCurrencyTypeString(m_recSale.getCurrencyType()));
            txtDate.setText(sdf_date.format(m_recSale.getTime()));
            txtTime.setText(sdf_time.format(m_recSale.getTime()));

            System.out.println("dateTIme 2v " + m_recSale.getTime());

            // txtApprovaCode.setText(Integer.toString(m_recSale.getApprovalCode()))
            // ;
            txtApprovalCodeValue.setText(m_recSale.getApprovalCode());

            if (m_recSale.getStatus() == RecSale.STATUS_CLOSE) {
                btnVoid.setVisibility(View.GONE);
            }

            if (m_recSale.getCardType() == RecSale.CARD_VISA) {
                imgCardType.setImageResource(R.drawable.visacard_icon);
            } else if (m_recSale.getCardType() == RecSale.CARD_MASTER) {
                imgCardType.setImageResource(R.drawable.mastercard_icon);
            } else if (m_recSale.getCardType() == RecSale.CARD_AMEX) {
                imgCardType.setImageResource(R.drawable.amex);
            } else if (m_recSale.getCardType() == RecSale.CARD_DINERS) {
                imgCardType.setImageResource(R.drawable.diners_club);
            }

            if (m_recSale.getMerchantInvoiceId() != null && m_recSale.getMerchantInvoiceId().trim().length() > 0) {
                txtTrackingId.setText(m_recSale.getMerchantInvoiceId());
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //IF PORTAL CHANGED
        if (!Home.isPortalChanged) {
            if (OpenTransations.isUpdatedSignature) {
                btnSign.setVisibility(View.GONE);
            }
        } else {
            finish();
        }

        if (OpenTransations.isVOidTransaction) {
            btnReceipt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        IS_FROM_OPEN_TX = false;
        super.onPause();
    }

    private void showHideResendBtn(int txType){
        //OLD WAR NOT SUPPORT
        if (Config.getVersionId(this) < 2) {
            //btnReceipt.setVisibility(View.GONE);
        } else {

            if (m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID)  {
                btnReceipt.setVisibility(View.GONE);
            } else {
                btnReceipt.setVisibility(View.VISIBLE);
            }
        }

        //new requirement for manual transaction which are already void and not coming from openTX (23/02/2017)
        if (txType == RecSale.MANUAL_TRANSACTION && m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID && !IS_FROM_OPEN_TX) {
            btnReceipt.setVisibility(View.GONE);
        }


        if (m_recSale.getStatus() == RecSale.STATUS_OPEN_VOID || OpenTransations.isVOidTransaction)  {
            btnReceipt.setVisibility(View.GONE);
        }

 }

    public void onVoid(View v) {
        if (m_recSale != null) {
            // this.pushActivity(VoidSwipe.class , m_recSale);
            // Void_Dialog();
            Void_Confirmation_Dialog();
            // _voidTx();
        }

    }

    public void onSign(View v) {
        pushActivity(UpdateSignaturePad.class, m_recSale);
        //finish();
    }

    public void onReceipt(View v) {
        pushActivity(SalesReceiptResendActivity.class, m_recSale);
    }

    private void _voidTx() {
        TxVoidReq req = new TxVoidReq();
        req.setTxId(m_recSale.getTxId());

        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String did = mTelephonyMgr.getDeviceId();
        String didHash = Crypto.generateSHA1(did);
        req.setDeviceID(didHash);
        req.setClientTime(new Date());

        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(Crypto.generateLongToken());

        if (cardType != -1) {
            if (cardType == 1 || cardType == 3) {
                //visa & master card
                m_client.voidRequest(CALL_VOID, ProgressDialogMessagesUtil.txDetailTranslation(lang_status), req);
            } else if (cardType == 2 || cardType == 4) {
                //amex & diners club card
                m_client_amex.voidRequest(CALL_VOID, ProgressDialogMessagesUtil.txDetailTranslation(lang_status), req);
            }
        } else {
            showToastMessage("Invalid Card Type");
            finish();
        }

        //m_client.voidRequest(CALL_VOID, ProgressDialogMessagesUtil.txDetailTranslation(lang_status), req);

		/*if (lang_status == LangPrefs.LAN_EN) {

			m_client.voidRequest(CALL_VOID, "Please wait...", req);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.voidRequest(CALL_VOID, "lreKdlr /|S isákak'", req);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.voidRequest(CALL_VOID, "rw;W nghWj;jpUq;fs;", req);

		} else {
			// do nothing
		}
*/

    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        if (callerId == CALL_VOID) {
            if (data != null) {
                if (data instanceof TxVoidRes) {
                    TxVoidRes res = (TxVoidRes) data;
                    m_recSale.setVoidTs(res.getServerTime());

                    OpenTransations.isVoidTransaction = true;
                    OpenTransations.voidServerTime = res.getServerTime();

                    btnSign.setVisibility(View.GONE);
                    btnVoid.setVisibility(View.GONE);
                    btnReceipt.setVisibility(View.GONE);

//                    if (txType != RecSale.MANUAL_TRANSACTION) {
//                        this.pushActivity(VoidReceiptActivity.class, m_recSale, res);
//                        finish();
//                    }

                    this.pushActivity(VoidReceiptActivity.class, m_recSale, res);
                }
            }
        }
    }

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 210) {
            fireOpenTxList();
        }
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {
        if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
            showDialog(
                    "Error",
                    "Error in Communication has occurred. Please Check your last transaction and ensure the transaction has been completed successfully.",
                    210);
            return;

        }

        super.onCallError(api, callerId, e);
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        txtCCLast4 = (TextView) findViewById(R.id.txtCCLast4);
        txtCCLast4.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        txtPaidWith = (TextView) findViewById(R.id.txtPaidWith);
        txtPaidWith.setTypeface(TypeFaceUtils
                .setLatoRegular(getApplicationContext()));

        txtCardName = (TextView) findViewById(R.id.txtCardName);
        txtCardName.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        txtApprovalCode = (TextView) findViewById(R.id.txtApprovalCode);
        txtApprovalCode.setTypeface(TypeFaceUtils
                .setLatoRegular(getApplicationContext()));

        txtApprovalCodeValue = (TextView) findViewById(R.id.txtApprovalCodeValue);
        txtApprovalCodeValue.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        lblSalesDate = (TextView) findViewById(R.id.lblSalesDate);
        lblSalesDate.setTypeface(TypeFaceUtils
                .setLatoRegular(getApplicationContext()));

        lblTrackingId = (TextView) findViewById(R.id.lblTrackingId);
        lblTrackingId.setTypeface(TypeFaceUtils
                .setLatoRegular(getApplicationContext()));

        txtTrackingId = (TextView) findViewById(R.id.txtTrackingId);
        txtTrackingId.setTypeface(TypeFaceUtils
                .setLatoRegular(getApplicationContext()));

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils
                .setLatoBold(getApplicationContext()));

        btnVoid = (Button) findViewById(R.id.btnVoid);
        btnVoid.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        btnSign = (Button) findViewById(R.id.btnSign);
        btnSign.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        btnReceipt = (Button) findViewById(R.id.btnReceipt);
        btnReceipt.setTypeface(TypeFaceUtils
                .setRobotoMedium(getApplicationContext()));

        //INSTALLMENT VIEW
        installmentView = (LinearLayout) findViewById(R.id.installmentView);

        if (installment != 0) {
            installment_tv = (TextView) findViewById(R.id.inst_tv);
            installment_amount_tv = (TextView) findViewById(R.id.inst_amount_tv);

            installmentView.setVisibility(View.VISIBLE);

            if (lang_status == LangPrefs.LAN_EN) {

                installment_tv.setTypeface(TypeFaceUtils
                        .setRobotoMedium(getApplicationContext()));
                installment_amount_tv.setTypeface(TypeFaceUtils
                        .setRobotoMedium(getApplicationContext()));

                installment_tv.setText("Installment");
                installment_amount_tv.setText(String.valueOf(installment) + " Months");

            } else if (lang_status == LangPrefs.LAN_SIN) {

                installment_tv.setTypeface(TypeFaceUtils
                        .setSinhalaFont(getApplicationContext()));
                installment_amount_tv.setTypeface(TypeFaceUtils
                        .setSinhalaFont(getApplicationContext()));

                installment_tv.setText("jdßl");
                installment_amount_tv.setText(String.valueOf(installment) + " udi");

                installment_tv.setTextSize(18.0f);
                installment_amount_tv.setTextSize(18.0f);

            } else if (lang_status == LangPrefs.LAN_TA) {

                installment_tv.setTypeface(TypeFaceUtils
                        .setBaminiFont(getApplicationContext()));
                installment_amount_tv.setTypeface(TypeFaceUtils
                        .setBaminiFont(getApplicationContext()));

                installment_tv.setText("jtizKiwfs;");
                installment_amount_tv.setText(String.valueOf(installment) + " khjq;fs;");

                installment_tv.setTextSize(18.0f);
                installment_amount_tv.setTextSize(18.0f);
            }

        } else {
            installmentView.setVisibility(View.GONE);
        }

        imgCardType = (ImageView) findViewById(R.id.imgCardType);


        //HIDE SHOW SIGN BUTTON
        if (IS_SIGN_BTN_VISIBLE) {
            if (m_recSale.getSigFlag() == RecSale.SIGNATURE_NOT_AVAILABLE) {

                if (m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID
                        || m_recSale.getStatus() == RecSale.STATUS_OPEN_VOID) {
                    btnSign.setVisibility(View.GONE);
                } else {
                    btnSign.setVisibility(View.VISIBLE);
                }

            } else {
                btnSign.setVisibility(View.GONE);
            }
        } else {
            btnSign.setVisibility(View.GONE);
        }

        if (m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID
                || m_recSale.getStatus() == RecSale.STATUS_OPEN_VOID) {

            btnSign.setVisibility(View.GONE);

            lblVoidDate = (TextView) findViewById(R.id.lblVoidDate);
            lblVoidDate.setTypeface(TypeFaceUtils
                    .setLatoRegular(getApplicationContext()));

            txtVoidDate = (TextView) findViewById(R.id.txtVoidDate);
            txtVoidDate.setTypeface(TypeFaceUtils
                    .setRobotoMedium(getApplicationContext()));

            txtVoidTime = (TextView) findViewById(R.id.txtVoidTime);
            txtVoidTime.setTypeface(TypeFaceUtils
                    .setRobotoMedium(getApplicationContext()));

            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {

                sinlang_select.Apply_VoidDetails_WithVoid(txtTitle,
                        txtPaidWith, txtApprovalCode, lblSalesDate,
                        lblVoidDate, btnVoid, btnSign, btnReceipt, lblTrackingId);

            } else if (lang_status == LangPrefs.LAN_TA) {

                tamlang_select.Apply_VoidDetails_WithVoid(txtTitle,
                        txtPaidWith, txtApprovalCode, lblSalesDate,
                        lblVoidDate, btnVoid, btnSign, btnReceipt, lblTrackingId);
            } else {
                // do nothing
            }

        } else {

            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidDetails(txtTitle, txtPaidWith,
                        txtApprovalCode, lblSalesDate, btnVoid, btnSign, btnReceipt, lblTrackingId);
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidDetails(txtTitle, txtPaidWith,
                        txtApprovalCode, lblSalesDate, btnVoid, btnSign, btnReceipt, lblTrackingId);
            } else {
                // do nothing
            }

        }

    }

    private void Void_Confirmation_Dialog() {
        try {

            void_confirm_dialog = new Dialog(this);
            void_confirm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            void_confirm_dialog.setContentView(R.layout.dialog_voidconfirm);
            void_confirm_dialog.setCanceledOnTouchOutside(true);

            final TextView txtTitle = (TextView) void_confirm_dialog
                    .findViewById(R.id.txtTitle);
            txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

            final TextView txtMessage = (TextView) void_confirm_dialog
                    .findViewById(R.id.txtMessage);
            txtMessage.setTypeface(TypeFaceUtils.setRobotoLight(this));

            Button btnOk = (Button) void_confirm_dialog
                    .findViewById(R.id.btnOk);
            btnOk.setTypeface(TypeFaceUtils.setRobotoBold(this));

            Button btnCancel = (Button) void_confirm_dialog
                    .findViewById(R.id.btnCancel);
            btnCancel.setTypeface(TypeFaceUtils.setRobotoBold(this));

            String CURRENCY = UtilityFunction.getCurrencyTypeString(m_recSale.getCurrencyType());

            txtMessage
                    .setText("You are about to VOID a Sales transaction. When you VOID you will return the amount " + CURRENCY + "."
                            + df.format(m_recSale.getAmount())
                            + " back to the card holder. "
                            + "This process can not be reversed. Would you like to Proceed with a VOID?");

            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    void_confirm_dialog.dismiss();
                    Void_Dialog();
                }
            });

            btnCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    void_confirm_dialog.dismiss();
                }
            });

            //Double d_amount = new Double(.getAmount());

            int currencyType = m_recSale.getCurrencyType();

            if (lang_status == LangPrefs.LAN_EN) {
            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidErrorMessage(txtTitle, txtMessage,
                        btnOk, btnCancel, df.format(m_recSale.getAmount()), currencyType);
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidErrorMessage(txtTitle, txtMessage,
                        btnOk, btnCancel, df.format(m_recSale.getAmount()), currencyType);
            } else {
                // do nothing
            }

            void_confirm_dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void Void_Confirmation_Dialog() {
        try {

            void_confirm_dialog = new Dialog(this);
            void_confirm_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            void_confirm_dialog.setContentView(R.layout.dialog_voidconfirm);
            void_confirm_dialog.setCanceledOnTouchOutside(true);

            final TextView txtTitle = (TextView) void_confirm_dialog
                    .findViewById(R.id.txtTitle);
            txtTitle.setTypeface(TypeFaceUtils.setLatoBold(this));

            final TextView txtMessage = (TextView) void_confirm_dialog
                    .findViewById(R.id.txtMessage);
            txtMessage.setTypeface(TypeFaceUtils.setRobotoLight(this));

            Button btnOk = (Button) void_confirm_dialog
                    .findViewById(R.id.btnOk);
            btnOk.setTypeface(TypeFaceUtils.setRobotoBold(this));

            Button btnCancel = (Button) void_confirm_dialog
                    .findViewById(R.id.btnCancel);
            btnCancel.setTypeface(TypeFaceUtils.setRobotoBold(this));

            txtMessage
                    .setText("You are about to VOID a Sales transaction. When you VOID you will return the amount Rs."
                            + String.valueOf(df.format(m_recSale.getAmount()))
                            + " back to the card holder. "
                            + "This process can not be reversed. Would you like to Proceed with a VOID?");

            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    void_confirm_dialog.dismiss();
                    Void_Dialog();
                }
            });

            btnCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    void_confirm_dialog.dismiss();
                }
            });

            Double d_amount = new Double(m_recSale.getAmount());
            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {

                sinlang_select.Apply_VoidErrorMessage(txtTitle, txtMessage,
                        btnOk, btnCancel, d_amount.intValue());
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidErrorMessage(txtTitle, txtMessage,
                        btnOk, btnCancel, df.format(m_recSale.getAmount()));
            } else {
                // do nothing
            }

            void_confirm_dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void Void_Dialog() {

        try {

            void_dialog = new Dialog(this);
            void_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            void_dialog.setContentView(R.layout.dialog_void);
            void_dialog.setCanceledOnTouchOutside(true);

            final TextView txtTitle = (TextView) void_dialog
                    .findViewById(R.id.txtTitle);
            txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

            final EditText edtPin = (EditText) void_dialog
                    .findViewById(R.id.edtPin);
            edtPin.setTypeface(TypeFaceUtils.setLatoRegular(this));
            edtPin.requestFocus();

            final Button btnSubmit = (Button) void_dialog
                    .findViewById(R.id.btnSubmit);
            btnSubmit.setTypeface(TypeFaceUtils.setLatoRegular(this));

            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidPin(txtTitle, edtPin, btnSubmit);
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidPin(txtTitle, edtPin, btnSubmit);
            } else {
                // do nothing
            }

            btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    if (!edtPin.getText().toString().equals("0000")) {
                        Toast.makeText(
                                TxDetail.this,
                                getResources().getString(R.string.IncorrectPin),
                                Toast.LENGTH_LONG).show();

                    } else {
                        void_dialog.dismiss();
                        _voidTx();
                        // Intent in = new Intent(getApplicationContext(),
                        // VoidReceiptActivity.class);
                        // startActivity(in);
                        // finish();
                    }
                }
            });

            edtPin.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence str, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub

                    if (lang_status == LangPrefs.LAN_EN) {

                    } else if (lang_status == LangPrefs.LAN_SIN) {
                        if (str == null || str.length() == 0) {
                            sinlang_select = new Sinhala_LangSelect(
                                    getApplicationContext(), TxDetail.this);
                            sinlang_select.Apply_VoidPin(txtTitle, edtPin,
                                    btnSubmit);

                        } else {

                            edtPin.setTypeface(TypeFaceUtils
                                    .setLatoRegular(TxDetail.this));
                        }
                    } else if (lang_status == LangPrefs.LAN_TA) {
                        if (str == null || str.length() == 0) {
                            tamlang_select = new Tamil_LangSelect(
                                    getApplicationContext(), TxDetail.this);
                            tamlang_select.Apply_VoidPin(txtTitle, edtPin,
                                    btnSubmit);
                        } else {

                            edtPin.setTypeface(TypeFaceUtils
                                    .setLatoRegular(TxDetail.this));
                        }

                    } else {
                        // do nothing
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }
            });

			/*
             * Display display = getWindowManager().getDefaultDisplay();
			 *
			 * WindowManager.LayoutParams lp;
			 *
			 * lp = new WindowManager.LayoutParams();
			 * lp.copyFrom(void_dialog.getWindow().getAttributes()); lp.width =
			 * (int) (display.getWidth() * 0.8); lp.height = (int)
			 * (display.getHeight() * 0.5);
			 *
			 * void_dialog.getWindow().setAttributes(lp);
			 */

            void_dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //SETUP VIEW
    private void ShowTrackId() {

        if (m_recSale.getStatus() == RecSale.STATUS_CLOSE_VOID
                || m_recSale.getStatus() == RecSale.STATUS_OPEN_VOID) {

            if (m_recSale.getMerchantInvoiceId() != null && m_recSale.getMerchantInvoiceId().trim().length() > 0) {
                setContentView(R.layout.activity_void_track_details);
            } else {
                setContentView(R.layout.activity_void_details);
            }
        } else {

            if (m_recSale.getMerchantInvoiceId() != null && m_recSale.getMerchantInvoiceId().trim().length() > 0) {
                setContentView(R.layout.activity_history_track_details);
            } else {
                setContentView(R.layout.activity_history_details);
            }
        }
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
