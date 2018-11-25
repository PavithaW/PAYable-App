package com.cba.payable;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.KeyEntryTxReq;
import com.mpos.pojo.Order;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.Crypto;
import com.mpos.util.DukptUtil;
import com.mpos.util.LangPrefs;
import com.mpos.util.Luhn;
import com.mpos.util.MonthYearCardTextWatcher;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UserPermisions;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

public class KeyEntryActivity extends GenericActivity {

    private static final String LOG_TAG = KeyEntryActivity.class.getSimpleName();

    private static final int KEY_ENTRY_TX = 1010;
    private static final int KEY_ENTRY_TX_AMEX = 2010;

    EditText edtCardOne, edtCardTwo, edtCardThree, edtCardFour, edtCardFive, edtMonth, edtCVV, edtPostal;
    TextView lblHeader, txtAmount, txtAmountValue, txtEnter;
    ImageView imgCard, imgCardView_two;
    ImageButton imgBack;
    RelativeLayout rlCard, rlMonthPostal;
    Button btnCharge;

    private String ccLast4Digit = "";
    private String ccNumber = "";

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;

    private Amount m_amount;
    private Order m_order;

    private int cardType = -1;
    private ProfileInfo profileInfo, profileInfoAmex;
    private boolean isAmexUser;
    private boolean isVisaUser;
    private boolean isGoodCardEntry;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_key_entry);
        initViews();

        isAmexUser = m_client_amex.isLoggedIn();
        isVisaUser = m_client.isLoggedIn();

        DecimalFormat df = new DecimalFormat("0.00");

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Amount) {
                    m_amount = (Amount) data[i];
                    txtAmountValue.setText(df.format(m_amount.getValue()) + " "
                            + UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType()));
                }

                if (data[i] instanceof Order) {
                    m_order = (Order) data[i];
                }
            }
        }

        getProfiles();
    }

    private void getProfiles() {
        String profiles = Config.getUserProfiles(this);
        profileInfo = ProfileInfo.getInstance(profiles);

        String profilesAmex = Config.getUserProfilesAmex(this);
        profileInfoAmex = ProfileInfo.getInstance(profilesAmex);
    }

    // Hide the Key Entry Numbers
    private class HiddenPassTransformationMethod implements TransformationMethod {

        private char DOT = '\u2022';

        @Override
        public CharSequence getTransformation(final CharSequence charSequence, final View view) {
            return new PassCharSequence(charSequence);
        }

        @Override
        public void onFocusChanged(final View view, final CharSequence charSequence, final boolean b, final int i,
                                   final Rect rect) {
            // nothing to do here
        }

        private class PassCharSequence implements CharSequence {

            private final CharSequence charSequence;

            public PassCharSequence(final CharSequence charSequence) {
                this.charSequence = charSequence;
            }

            @Override
            public char charAt(final int index) {
                return DOT;
            }

            @Override
            public int length() {
                return charSequence.length();
            }

            @Override
            public CharSequence subSequence(final int start, final int end) {
                return new PassCharSequence(charSequence.subSequence(start, end));
            }
        }
    }

    // First View TextWatchers
    private void firstViewTextWatchers() {

        edtCardOne.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCardOne.setTextColor(Color.BLACK);

                // TODO Auto-generated method stub
                if (edtCardOne.length() == 4) {

                    if (edtCardOne.getText().toString().startsWith("4")) {

                        if (isAmexUser && isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isAmexUser) {
                            isGoodCardEntry = false;
                        }

                        if (isGoodCardEntry) {
                            imgCard.setImageResource(R.drawable.ic_visa);
                            imgCardView_two.setImageResource(R.drawable.ic_visa);
                            cardType = 1;

                            edtCardTwo.setEnabled(true);
                            edtCardTwo.requestFocus();
                            edtCardTwo.setCursorVisible(true);
                        } else {
                            clearFirstNumbers("Please login with VISA/MASTER to proceed visa card.");
                        }

                    } else if (edtCardOne.getText().toString().startsWith("51") || edtCardOne.getText().toString().startsWith("52")
                            || edtCardOne.getText().toString().startsWith("53") || edtCardOne.getText().toString().startsWith("54")
                            || edtCardOne.getText().toString().startsWith("55")) {

                        if (isAmexUser && isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isAmexUser) {
                            isGoodCardEntry = false;
                        }

                        if (isGoodCardEntry) {
                            imgCard.setImageResource(R.drawable.ic_master);
                            imgCardView_two.setImageResource(R.drawable.ic_master);
                            cardType = 3;

                            edtCardTwo.setEnabled(true);
                            edtCardTwo.requestFocus();
                            edtCardTwo.setCursorVisible(true);
                        } else {
                            clearFirstNumbers("Please login with VISA/MASTER to proceed master card.");
                        }


                    } else if (edtCardOne.getText().toString().startsWith("34") ||
                            edtCardOne.getText().toString().startsWith("37")) {

                        if (isAmexUser && isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isVisaUser) {
                            isGoodCardEntry = false;
                        } else if (isAmexUser) {
                            isGoodCardEntry = true;
                        }

                        if (isGoodCardEntry) {
                            //american express card
                            imgCard.setImageResource(R.drawable.amex);
                            imgCardView_two.setImageResource(R.drawable.amex);
                            cardType = 2;

                            int maxLength = 6;
                            edtCardTwo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                            edtCardTwo.setHint("xxxxxx");

                            maxLength = 5;
                            edtCardThree.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                            edtCardThree.setHint("xxxxx");

                            edtCardTwo.setEnabled(true);
                            edtCardTwo.requestFocus();
                            edtCardTwo.setCursorVisible(true);

                            edtCardFour.setVisibility(View.INVISIBLE);
                        } else {
                            clearFirstNumbers("Please login with AMEX/Diners to proceed amex card.");
                        }


                    } else if (edtCardOne.getText().toString().startsWith("36") || edtCardOne.getText().toString().startsWith("38")
                            || edtCardOne.getText().toString().startsWith("39") || edtCardOne.getText().toString().startsWith("3095")
                            || edtCardOne.getText().toString().startsWith("300") || edtCardOne.getText().toString().startsWith("301")
                            || edtCardOne.getText().toString().startsWith("302") || edtCardOne.getText().toString().startsWith("303")
                            || edtCardOne.getText().toString().startsWith("304") || edtCardOne.getText().toString().startsWith("305")) {

                        if (isAmexUser && isVisaUser) {
                            isGoodCardEntry = true;
                        } else if (isVisaUser) {
                            isGoodCardEntry = false;
                        } else if (isAmexUser) {
                            isGoodCardEntry = true;
                        }

                        if (isGoodCardEntry) {
                            //diners club
                            imgCard.setImageResource(R.drawable.diners_club);
                            imgCardView_two.setImageResource(R.drawable.diners_club);
                            cardType = 4;

                            int maxLength = 6;
                            edtCardTwo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                            edtCardTwo.setHint("xxxxxx");

                            maxLength = 4;
                            edtCardThree.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                            edtCardThree.setHint("xxxx");

                            edtCardTwo.setEnabled(true);
                            edtCardTwo.requestFocus();
                            edtCardTwo.setCursorVisible(true);

                            edtCardFour.setVisibility(View.INVISIBLE);
                        } else {
                            clearFirstNumbers("Please login with AMEX/Diners to proceed diners club card.");
                        }

                    } else {
                        edtCardTwo.setEnabled(false);
                        edtCardOne.setTextColor(Color.RED);

                    }
                } else {
                    imgCard.setImageResource(R.drawable.ic_emty_card);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cardType == 2 || cardType == 4) {
                    if (s.length() == 0) {
                        edtCardFour.setVisibility(View.VISIBLE);

                        int maxLength = 4;
                        edtCardTwo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                        edtCardTwo.setHint("xxxx");

                        maxLength = 4;
                        edtCardThree.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                        edtCardThree.setHint("xxxx");
                    }


                }
            }
        });


        edtCardTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                edtCardTwo.setTextColor(Color.BLACK);

                /*if (cardType == 2) {
                    *//*if (edtCardTwo.length() == 6) {
                        edtCardThree.setEnabled(true);
                        edtCardThree.clearFocus();
                        edtCardThree.requestFocus();
                        edtCardThree.setCursorVisible(true);
                    }*//*
                } else {
                    if (edtCardTwo.length() == 4) {

                        edtCardThree.setEnabled(true);
                        edtCardThree.clearFocus();
                        edtCardThree.requestFocus();
                        edtCardThree.setCursorVisible(true);

                    }
                }*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (cardType == 2 || cardType == 4) {
                    if (edtCardTwo.length() == 6) {

                        edtCardThree.setEnabled(true);
                        edtCardThree.clearFocus();
                        edtCardThree.requestFocus();
                        edtCardThree.setCursorVisible(true);
                    } else if (edtCardTwo.length() == 0) {

                        //when clear
                        edtCardOne.setEnabled(true);
                        edtCardOne.clearFocus();
                        edtCardOne.requestFocus();
                        edtCardOne.setCursorVisible(true);
                    }
                } else {
                    if (edtCardTwo.length() == 4) {

                        edtCardThree.setEnabled(true);
                        edtCardThree.clearFocus();
                        edtCardThree.requestFocus();
                        edtCardThree.setCursorVisible(true);

                    } else if (s.length() == 0) {
                        //edtCardTwo.requestFocus();
                        edtCardOne.requestFocus();
                    }
                }
            }
        });

        edtCardThree.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                edtCardThree.setTextColor(Color.BLACK);

                if (cardType == 2) {
                    if (s.length() >= 1) {
                        //amex
                        ccLast4Digit = s.toString().substring(1, s.length());
                    }
                } else if (cardType == 4) {
                    //diners club
                    ccLast4Digit = s.toString();
                }

                if (cardType == 2) {
                    if (s.length() == 5) {

                        ccNumber = (edtCardOne.getText().toString() + edtCardTwo.getText().toString()
                                + edtCardThree.getText().toString()).trim();

                        if (Luhn.Check(ccNumber)) {

                            rlCard.setVisibility(View.INVISIBLE);

                            rightSlideAnimation(1);
                            rlMonthPostal.setVisibility(View.VISIBLE);

                            edtCardFive.setText(ccLast4Digit);
                            edtMonth.requestFocus();
                            edtMonth.setCursorVisible(true);
                        } else {
                            changeCreditCardColor(1);
                        }
                    }
                } else if (cardType == 4) {
                    if (s.length() == 4) {

                        ccNumber = (edtCardOne.getText().toString() + edtCardTwo.getText().toString()
                                + edtCardThree.getText().toString()).trim();

                        if (Luhn.Check(ccNumber)) {

                            rlCard.setVisibility(View.INVISIBLE);

                            rightSlideAnimation(1);
                            rlMonthPostal.setVisibility(View.VISIBLE);

                            edtCardFive.setText(ccLast4Digit);
                            edtMonth.requestFocus();
                            edtMonth.setCursorVisible(true);
                        } else {
                            changeCreditCardColor(1);
                        }
                    }
                } else {
                    if (edtCardThree.length() == 4) {

                        edtCardFour.setEnabled(true);
                        edtCardFour.clearFocus();
                        edtCardFour.requestFocus();
                        edtCardFour.setCursorVisible(true);

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() == 0) {

//						edtCardThree.requestFocus();
                    edtCardTwo.requestFocus();

                }
            }
        });

        edtCardFour.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                edtCardFour.setTextColor(Color.BLACK);

                if (s.length() == 4) {
                    ccLast4Digit = s.toString();

                    ccNumber = (edtCardOne.getText().toString() + edtCardTwo.getText().toString()
                            + edtCardThree.getText().toString() + edtCardFour.getText().toString()).trim();

                    if (Luhn.Check(ccNumber)) {

                        rlCard.setVisibility(View.INVISIBLE);

                        rightSlideAnimation(1);
                        rlMonthPostal.setVisibility(View.VISIBLE);

                        edtCardFive.setText(ccLast4Digit);
                        edtMonth.requestFocus();
                        edtMonth.setCursorVisible(true);
                    } else {
                        changeCreditCardColor(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() == 0) {

//						edtCardFour.requestFocus();
                    edtCardThree.requestFocus();

                }
            }
        });
    }


    private void clearFirstNumbers(String message) {
        edtCardOne.setText("");
        showToastMessage(message);
    }

    private void initViews() {
        rlCard = (RelativeLayout) findViewById(R.id.rlCard);

        rlMonthPostal = (RelativeLayout) findViewById(R.id.rlMonthPostal);

        lblHeader = (TextView) findViewById(R.id.lblHeader);
        lblHeader.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmountValue = (TextView) findViewById(R.id.txtAmountValue);

        txtEnter = (TextView) findViewById(R.id.txtEnter);
        txtEnter.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        imgCard = (ImageView) findViewById(R.id.imageView1);

        imgCardView_two = (ImageView) findViewById(R.id.imgCardView_two);

        imgBack = (ImageButton) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(onClickListener);

        btnCharge = (Button) findViewById(R.id.btnCharge);
        btnCharge.setBackgroundColor(getResources().getColor(R.color.void_dialogbtn));
        btnCharge.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        btnCharge.setEnabled(false);
        btnCharge.setOnClickListener(onClickListener);

        edtCardOne = (EditText) findViewById(R.id.edtOne);
        edtCardOne.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        //edtCardOne.setTransformationMethod(new HiddenPassTransformationMethod());
        edtCardOne.requestFocus();

        edtCardTwo = (EditText) findViewById(R.id.edtTwo);
        edtCardTwo.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
       // edtCardTwo.setTransformationMethod(new HiddenPassTransformationMethod());

        edtCardThree = (EditText) findViewById(R.id.edtThree);
        edtCardThree.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        //edtCardThree.setTransformationMethod(new HiddenPassTransformationMethod());

        edtCardFour = (EditText) findViewById(R.id.edtFour);
        edtCardFour.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        //edtCardFour.setTransformationMethod(new HiddenPassTransformationMethod());

        edtCardTwo.setEnabled(false);
        edtCardThree.setEnabled(false);
        edtCardFour.setEnabled(false);

        // Second View
        edtCardFive = (EditText) findViewById(R.id.edtFive);
        edtCardFive.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        edtMonth = (EditText) findViewById(R.id.edtMonth);
        edtMonth.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        edtMonth.addTextChangedListener(new MonthYearCardTextWatcher(getApplicationContext(), edtMonth, btnCharge, rlCard));


        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);
        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);
        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.enterCardDetails(lblHeader, txtAmount, txtEnter, btnCharge);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.enterCardDetails(lblHeader, txtAmount, txtEnter, btnCharge);
        }


        firstViewTextWatchers();
        setkeyListeners();

    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnCharge:
//				Intent inCharge = new Intent(getApplicationContext(), Approval.class);
//				startActivity(inCharge);
//				finish();zz
                    onCharge();
                    break;

                case R.id.imgBack:
//				Intent in = new Intent(getApplicationContext(), SelectPaymentMethod.class);
//				startActivity(in);
                    onBackPressed();
                    finish();
                    break;
            }
        }
    };

    private void onCharge() {

        long visaPermissions = Config.getUser(this).getPermissions();
        long amexPermissions = Config.getAmexUser(this).getPermissions();

        boolean isKeyentyEnableVisa = UserPermisions.isKeyEntryTransactionsON(visaPermissions);
        boolean isKeyentyEnableAmex = UserPermisions.isKeyEntryTransactionsON(amexPermissions);

        //if visa or mastercard profile do not have key entry enabled
        if((cardType == 3 || cardType == 1) && !isKeyentyEnableVisa){
            finish();
            showToastMessage("Key entry is not activated for VISA / MASTER profile.");
            return;
        //if amex profile does not have key entry enabled
        }else if(cardType == 2  && !isKeyentyEnableAmex){
            finish();
            showToastMessage("Key entry is not activated for AMEX profile.");
            return;
        }

        String expDate = edtMonth.getText().toString();

        if (expDate.length() < 3) {
            return;
        }

//		String ksn = "00000332100300E0006E";

        Random rand = new Random();

        int rand_int1 = rand.nextInt(899) + 100;
        int rand_int2 = rand.nextInt(899) + 100;
        int rand_int3 = rand.nextInt(899) + 100;

        String baseksn = "00000332100";

        String random = "" + rand_int1 + rand_int2 + rand_int3;

        String ksn = baseksn + random;

        StringBuilder cardNo = new StringBuilder("^");

        if (cardType == 2 || cardType == 4) {
            cardNo.append(edtCardOne.getText().toString());
            cardNo.append(edtCardTwo.getText().toString());
            cardNo.append(edtCardThree.getText().toString());
        } else {
            cardNo.append(edtCardOne.getText().toString());
            cardNo.append(edtCardTwo.getText().toString());
            cardNo.append(edtCardThree.getText().toString());
            cardNo.append(edtCardFour.getText().toString());
        }

        cardNo.append("=");

        String[] date = expDate.split("/");

        cardNo.append(date[1]);
        cardNo.append(date[0]);

        cardNo.append("^");

        int x = cardNo.length() % 8;

        if (x != 0) {
            for (int i = 0; i < (8 - x); i++) {
                cardNo.append("0");
            }
        }

        String strAsciiData = Crypto.asciiToHex(cardNo.toString());

        byte[] byte_ksn = Crypto.parseHexStr2Byte(ksn);
        //byte[] byte_bdk = Crypto.parseHexStr2Byte("0123456789ABCDEFFEDCBA9876543210");
        byte[] byte_bdk = Crypto.parseHexStr2Byte("AA23BB678CCBCDEFFED11A9877743210");

        byte[] ipek = DukptUtil.generateIPEK(byte_ksn, byte_bdk);
        byte[] dataKey = DukptUtil.getDataKey(byte_ksn, ipek);

        byte[] enRes = DukptUtil.TriDesEncryptionCBC(dataKey,
                Crypto.parseHexStr2Byte(strAsciiData));

        String strEncryptionCBC = Crypto.parseByte2HexStr(enRes);

        Log.d(LOG_TAG, strEncryptionCBC);

        KeyEntryTxReq req = new KeyEntryTxReq();
        req.setAmount(m_amount.getValue());
        req.setCurrencyType(m_amount.getCurrencyType());
        req.setInstallment(m_amount.getInstallments());
        req.setClientTime(new Date());
        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(Crypto.generateLongToken());
        req.setKsn(ksn);
        req.setKeyData(strEncryptionCBC);
        req.setLocationSrc(0);
        req.setInvoice(Config.generateINVOICE(this));

        if (m_order != null) {
            req.setMerchantInvoiceId(m_order.getMerchantInvoiceId());
        }

//        m_client.keyEntryTx(1010, ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);

        if (cardType != -1) {
            if (cardType == 1 || cardType == 3) {
                if (profileInfo != null) {
                    switch (m_amount.getCurrencyType()) {
                        case Consts.LKR:
                            if (profileInfo.isLkrEnabled()) {
                                if (profileInfo.getLkrMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed LKR " + profileInfo.getLkrMaxAmount(), 500);
                                } else {
                                    m_client.keyEntryTx(KEY_ENTRY_TX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "LKR currency has been disabled.", 500);
                            }

                            break;
                        case Consts.USD:
                            if (profileInfo.isUsdEnabled()) {
                                if (profileInfo.getUsdMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed USD " + profileInfo.getUsdMaxAmount(), 500);
                                } else {
                                    m_client.keyEntryTx(KEY_ENTRY_TX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "USD currency has been disabled.", 500);
                            }

                            break;
                        case Consts.GBP:
                            if (profileInfo.isGbpEnabled()) {
                                if (profileInfo.getGbpMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed GBP " + profileInfo.getGbpMaxAmount(), 500);
                                } else {
                                    m_client.keyEntryTx(KEY_ENTRY_TX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "GBP currency has been disabled.", 500);
                            }

                            break;
                        case Consts.EUR:
                            if (profileInfo.isEurEnabled()) {
                                if (profileInfo.getEurMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed EUR " + profileInfo.getEurMaxAmount(), 500);
                                } else {
                                    m_client.keyEntryTx(KEY_ENTRY_TX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "EUR currency has been disabled.", 500);
                            }

                            break;
                    }
                } else {
                    // null
                    showDialog("Error", "Please login with VISA/MASTER again!", 500);
                }
            } else if (cardType == 2 || cardType == 4) {
                if (profileInfoAmex != null) {
                    switch (m_amount.getCurrencyType()) {
                        case Consts.LKR:
                            if (profileInfoAmex.isLkrEnabled()) {
                                if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                } else {
                                    m_client_amex.keyEntryTx(KEY_ENTRY_TX_AMEX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "LKR currency has been disabled.", 500);
                            }

                            break;
                        case Consts.USD:
                            if (profileInfoAmex.isUsdEnabled()) {
                                if (profileInfoAmex.getUsdMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed USD " + profileInfoAmex.getUsdMaxAmount(), 500);
                                } else {
                                    m_client_amex.keyEntryTx(KEY_ENTRY_TX_AMEX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "USD currency has been disabled.", 500);
                            }

                            break;
                        case Consts.GBP:
                            if (profileInfoAmex.isGbpEnabled()) {
                                if (profileInfoAmex.getGbpMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed GBP " + profileInfoAmex.getGbpMaxAmount(), 500);
                                } else {
                                    m_client_amex.keyEntryTx(KEY_ENTRY_TX_AMEX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "GBP currency has been disabled.", 500);
                            }

                            break;
                        case Consts.EUR:
                            if (profileInfoAmex.isEurEnabled()) {
                                if (profileInfoAmex.getEurMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed EUR " + profileInfoAmex.getEurMaxAmount(), 500);
                                } else {
                                    m_client_amex.keyEntryTx(KEY_ENTRY_TX_AMEX,
                                            ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);
                                }
                            } else {
                                //error message
                                showDialog("Error", "EUR currency has been disabled.", 500);
                            }

                            break;
                    }
                } else {
                    // null
                    showDialog("Error", "Please login with AMEX/DINERS again!", 500);
                }
            } else {
                Log.i(LOG_TAG, "Invalid card type");
                showDialog("Error", "Invalid card type", 500);
            }
        } else {
            Log.i(LOG_TAG, "Invalid card type");
            showDialog("Error", "Invalid card type", 500);
        }

    }

    @Override
    public void onMSGDlgBtnClick(int callerId) {
        super.onMSGDlgBtnClick(callerId);

        if (callerId == 500) {
            SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
            finish();
        }
    }

    // Key Listeners when user types the backspace and clicks the Done button
    private void setkeyListeners() {
        edtCardTwo.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

					/*if (keyCode == KeyEvent.KEYCODE_DEL) {
                        txtAmount.setText("KEYCODE_DEL"+keyCode);
						changeCreditCardColor(0);

						if (edtCardTwo.getText().toString().length() == 0) {

							edtCardOne.requestFocus();
		
						}
					}*/

                return false;
            }
        });

        edtCardThree.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

					/*if (keyCode == KeyEvent.KEYCODE_DEL) {

						changeCreditCardColor(0);

						if (edtCardThree.getText().toString().length() == 0) {

							edtCardTwo.requestFocus();
						}
					}*/
                return false;
            }
        });

        edtCardFour.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

					/*if (keyCode == KeyEvent.KEYCODE_DEL) {

						changeCreditCardColor(0);

						if (edtCardFour.getText().toString().length() == 0) {

							edtCardThree.requestFocus();
						}
					}*/
                return false;
            }
        });

        edtMonth.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (cardType == 2) {
                        if (edtMonth.getText().toString().length() == 0) {

                            rlCard.setVisibility(View.VISIBLE);

                            // i = 1 -> rlMonthPostal animation, i = 2 -> rlCard
                            // animation
                            rightSlideAnimation(2);
                            rlMonthPostal.setVisibility(View.INVISIBLE);
                            edtCardThree.requestFocus();
                            edtCardThree.setSelection(edtCardThree.getText().length());
                            edtCardThree.setCursorVisible(true);

                        }
                    } else {
                        if (edtMonth.getText().toString().length() == 0) {

                            rlCard.setVisibility(View.VISIBLE);

                            // i = 1 -> rlMonthPostal animation, i = 2 -> rlCard
                            // animation
                            rightSlideAnimation(2);
                            rlMonthPostal.setVisibility(View.INVISIBLE);

                            //if the card is not a a diners club card (cardType != 4), set focus to the fourth edit text
                            if(cardType != 4){
                                edtCardFour.requestFocus();
                                edtCardFour.setSelection(edtCardFour.getText().length());
                                edtCardFour.setCursorVisible(true);
                            }

                        }
                    }
                }


                return false;
            }
        });


        edtCardFour.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //your code
                    if (edtCardFour.getText().toString().length() == 4 && Luhn.Check(ccNumber)) {
                        rlCard.setVisibility(View.INVISIBLE);

                        rightSlideAnimation(1);
                        rlMonthPostal.setVisibility(View.VISIBLE);

                        edtCardFive.setText(ccLast4Digit);
                        edtMonth.requestFocus();
                        edtMonth.setCursorVisible(true);
                    }
                }

                return false;
            }
        });


        edtCardThree.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (cardType == 2) {
                        if (arg0.length() == 5) {

                            ccNumber = (edtCardOne.getText().toString() + edtCardTwo.getText().toString()
                                    + edtCardThree.getText().toString()).trim();

                            if (Luhn.Check(ccNumber)) {

                                rlCard.setVisibility(View.INVISIBLE);

                                rightSlideAnimation(1);
                                rlMonthPostal.setVisibility(View.VISIBLE);

                                edtCardFive.setText(ccLast4Digit);
                                edtMonth.requestFocus();
                                edtMonth.setCursorVisible(true);
                            } else {
                                changeCreditCardColor(1);
                            }
                        }
                    } else if (cardType == 4) {
                        if (arg0.length() == 4) {

                            ccNumber = (edtCardOne.getText().toString() + edtCardTwo.getText().toString()
                                    + edtCardThree.getText().toString()).trim();

                            if (Luhn.Check(ccNumber)) {

                                rlCard.setVisibility(View.INVISIBLE);

                                rightSlideAnimation(1);
                                rlMonthPostal.setVisibility(View.VISIBLE);

                                edtCardFive.setText(ccLast4Digit);
                                edtMonth.requestFocus();
                                edtMonth.setCursorVisible(true);
                            } else {
                                changeCreditCardColor(1);
                            }
                        }
                    }
                }

                return false;
            }
        });

    }


    // i = 1 -> rlMonthPostal animation, i = 2 -> rlCard animation
    private void rightSlideAnimation(int i) {
        Animation RightSwipe = AnimationUtils.loadAnimation(KeyEntryActivity.this, R.anim.right_slide);

        if (i == 1) {
            rlMonthPostal.startAnimation(RightSwipe);
        } else {
            rlCard.startAnimation(RightSwipe);

            //if the card is a diners club card (cardType == 4), set focus to the third edit text
            if(cardType == 4){
                edtCardThree.requestFocus();
                edtCardThree.setSelection(edtCardThree.getText().length());
                edtCardThree.setCursorVisible(true);
            }

        }
    }

    // If status is 1 = show text in red, otherwise show the text in black
    private void changeCreditCardColor(int status) {
        if (status == 1) {
            edtCardOne.setTextColor(Color.RED);
            edtCardTwo.setTextColor(Color.RED);
            edtCardThree.setTextColor(Color.RED);
            edtCardFour.setTextColor(Color.RED);
        } else {
            edtCardOne.setTextColor(Color.BLACK);
            edtCardTwo.setTextColor(Color.BLACK);
            edtCardThree.setTextColor(Color.BLACK);
            edtCardFour.setTextColor(Color.BLACK);
        }

    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == KEY_ENTRY_TX) {
            if (data != null) {
                if (data instanceof TxSaleRes) {
                    TxSaleRes txSaleRes = (TxSaleRes) data;
                    txSaleRes.setCurrencyType(m_amount.getCurrencyType());
                    pushActivity(Approval.class, txSaleRes);
                    finish();
                }
            }
        }

        if (callerId == KEY_ENTRY_TX_AMEX) {
            if (data != null) {
                if (data instanceof TxSaleRes) {
                    TxSaleRes txSaleRes = (TxSaleRes) data;
                    txSaleRes.setCurrencyType(m_amount.getCurrencyType());
                    pushActivity(Approval.class, txSaleRes);
                    finish();
                }
            }
        }

    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION || e.getErrcode() == ApiException.TIMEOUT_ERROR) {
            showDialog("Error", "Couldn't connect with transaction server.Please check your internet connectivity.");
            return;
        }

        Intent intent = new Intent(this, Rejected.class);
        intent.putExtra(Rejected.AMMOUNT, m_amount.getValue());
        intent.putExtra(Rejected.CURRENCY_TYPE, m_amount.getCurrencyType());
        intent.putExtra(Rejected.ERROR_CODE, e.getErrcode());
        intent.putExtra(Rejected.ERROR_MESSAGE, e.toString());

        startActivity(intent);
        finish();

    }
}
