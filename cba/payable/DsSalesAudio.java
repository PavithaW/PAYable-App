package com.cba.payable;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.MsgClient;
import com.mpos.connection.MsgClientAmex;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.DSICCOflineErrReq;
import com.mpos.pojo.Order;
import com.mpos.pojo.TXDsNfcReq;
import com.mpos.pojo.TxDSSaleReq;
import com.mpos.pojo.TxDsEmvReq;
import com.mpos.pojo.TxSaleRes;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.Crypto;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class DsSalesAudio extends DSBaseTxActivity {

    private static final String LOG_TAG = DsSalesAudio.class.getSimpleName();

    private Amount m_amount;
    private TextView txtStatus;

    private TxSaleRes m_saleRes;
    private Order m_order = null;

    private AnimatedGifImageView animatedGifImageView;

    private TextView txtAmount, txtTitle, txtAmountValue;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    public static boolean isClose = false;
    private static final int EMV_CALLBACK = 5000;
    private static final int EMV_CALLBACK_AMEX = 6000;

    TxDsEmvReq req;
    TXDsNfcReq nfcReq;

    private ProfileInfo profileInfo, profileInfoAmex;

    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_swipe_card);
        unpairBluetoothDevices();
        Log.d(LOG_TAG, "onInitActivity : ");

        InitViews();

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

        m_saleRes = null;

        getProfiles();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isClose) {
            isClose = false;
            finish();
        }
    }

    private void getProfiles() {
        String profiles = Config.getUserProfiles(this);
        profileInfo = ProfileInfo.getInstance(profiles);

        String profilesAmex = Config.getUserProfilesAmex(this);
        profileInfoAmex = ProfileInfo.getInstance(profilesAmex);
    }

    protected void navigateToSigScreen() {

        Log.d(LOG_TAG, "navigateToSigScreen : ");

        if (m_saleRes != null) {
            SignaturePad.isClose = false;
            this.pushActivity(SignaturePad.class, m_saleRes);
            finish();
        }
    }

    protected POS_TYPE getPosType() {
        Log.d(LOG_TAG, "getPosType : ");
        /*
         * int readerId = Config.getActiveReader(getApplicationContext());
		 *
		 * if (readerId == Config.READER_DS_AUDIO) { return POS_TYPE.AUDIO; }
		 *
		 * if (readerId == Config.READER_DS_BT) { return POS_TYPE.BLUETOOTH; }
		 */
        return POS_TYPE.BLUETOOTH;
    }

    protected void statusCallBack(int status, String message) {
        Log.d(LOG_TAG, "statusCallBack : status= " + status + " message= " + message);
        storeMessageLog("Status code : " + status + "  message : " + message);

        if (status == CARD_ICC) {
            Log.d(LOG_TAG, "statusCallBack : status == CARD_ICC");
            // txtStatus.setText("Reading EMV. Please wait !!") ;
            txtStatus.setText("Please wait !!");
            return;
        }

        if (status == CARD_ICC_ONLINE) {
            Log.d(LOG_TAG, "statusCallBack : status == CARD_ICC_ONLINE");

            txtStatus.setText("Processing transaction.");
            return;
        }

        txtStatus.setText(message);
    }

    @Override
    protected String getCurrency() {
        return UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType());
    }

    protected String getAmount() {
        Log.d(LOG_TAG, "getAmount : ");
        // String str = Integer.toString((int) ((float) m_amount.getValue() *
        // 100));
        // BigDecimal b = new BigDecimal(m_amount.getValue()) ;

        BigDecimal b = new BigDecimal(Double.toString(m_amount.getValue()));
        b = b.multiply(new BigDecimal("100"));

		/*
         * Log.i(TAG, "m_amount : " + m_amount.getValue()); Log.i(TAG,
		 * "m_amount * 100: " + (float)m_amount.getValue() * 100); Log.i(TAG,
		 * "m_amount * 100.0: " + m_amount.getValue() * 100f); Log.i(TAG,
		 * "str : " + str);
		 *
		 * Log.i(TAG, "BigDecimal result : " + b.toString());
		 *
		 * Log.i(TAG, "BigDecimal plain string : " + b.toPlainString());
		 *
		 * Log.i(TAG, "BigDecimal big integer string : " +
		 * b.toBigInteger().toString());
		 */

        // return str;

        return b.toBigInteger().toString();

    }

/*

    protected void emvCallBack(String data) {

        Log.d(LOG_TAG, "emvCallBack : data = " + data);

        TxDsEmvReq req = new TxDsEmvReq();
        req.setAmount(m_amount.getValue());
        req.setCurrencyType(m_amount.getCurrencyType());
        req.setInstallment(m_amount.getInstallments());

        req.setClientTime(new Date());
        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(Crypto.generateLongToken());
        req.setInvoice(Config.generateINVOICE(this));

        //req.setTsToken(1000);
        //req.setRndToken(1000);

        if (m_order != null) {
            req.setMerchantInvoiceId(m_order.getMerchantInvoiceId());
        }

        if (deviceId != null) {
            req.setCardReaderId(deviceId);
        } else {
            req.setCardReaderId("none");
        }

        req.setIcData(data);

        m_saleRes = null;

        // Log.i(TAG, "About to call server");

        storeMessageLog("Sending emv transaction request for amount : "
                + m_amount.getValue()
                + " "
                + UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType()));

        m_client.dsEmvSale(5000, ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), req);

		*/
/*if (lang_status == LangPrefs.LAN_EN) {

			m_client.dsEmvSale(5000, "Please wait.", req);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.dsEmvSale(5000, "lreKdlr /|S is�kak'", req);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.dsEmvSale(5000, "rw;W nghWj;jpUq;fs;", req);

		} else {
			// do nothing
		}*//*


    }
*/

    protected void emvCallBack(String data) {

        Log.d(LOG_TAG, "emvCallBack : data = " + data);

        String strAid;

        if (data.startsWith("5F20")) {
            String hexadecimalString = data.substring(4, 6);

            //hex to binary conversion
            int num = (Integer.parseInt(hexadecimalString, 16));
            String binaryString = Integer.toBinaryString(num);

            //binary to decimal conversion
            int decimalValue = Integer.parseInt(binaryString, 2);

            int beginIndex = 6 + decimalValue * 2 + 2;
            int endIndex = beginIndex + 2;
            String string2 = data.substring(beginIndex, endIndex);

            int decimalValue2 = Integer.parseInt(Integer.toBinaryString((Integer.parseInt(string2, 16))), 2);

            Log.d(LOG_TAG, "decimal 2 = " + decimalValue2);

            beginIndex = 6 + decimalValue * 2 + 2 + 2;
            endIndex = beginIndex + decimalValue2 * 2;
            strAid = data.substring(beginIndex, endIndex);

            Log.d(LOG_TAG, "string3 = " + strAid);
        } else {
            showDialog("Error", "Invalid card");
            return;
        }

        req = new TxDsEmvReq();
        req.setAmount(m_amount.getValue());
        req.setCurrencyType(m_amount.getCurrencyType());
        req.setInstallment(m_amount.getInstallments());

        req.setClientTime(new Date());
        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(Crypto.generateLongToken());
        req.setInvoice(Config.generateINVOICE(this));

        if (m_order != null) {
            req.setMerchantInvoiceId(m_order.getMerchantInvoiceId());
        }

        if (deviceId != null) {
            req.setCardReaderId(deviceId);
        } else {
            req.setCardReaderId("none");
        }

        //latest fix with new readers 09/06/2018
        String tempData = "";

        if(data.endsWith("C9007100")){
            tempData = data.substring(0, data.length() - 8);
        }else {
            tempData = data;
        }
        req.setIcData(tempData);

        m_saleRes = null;

        // Log.i(TAG, "About to call server");

        storeMessageLog("Sending emv transaction request for amount : "
                + m_amount.getValue()
                + " "
                + UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType()));

        if (strAid != null) {
            if (strAid.startsWith("A000000025") || strAid.startsWith("A00000015")) {

                if (!m_client_amex.isLoggedIn()) {
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with AMEX/Diners to proceed amex or diners club card.");
                }

                if (profileInfoAmex != null) {
                    switch (m_amount.getCurrencyType()) {
                        case Consts.LKR:
                            if (profileInfoAmex.isLkrEnabled()) {
                                if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                } else {
                                    m_client_amex.dsEmvSale(EMV_CALLBACK_AMEX,
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
                                    m_client_amex.dsEmvSale(EMV_CALLBACK_AMEX,
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
                                    m_client_amex.dsEmvSale(EMV_CALLBACK_AMEX,
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
                                    m_client_amex.dsEmvSale(EMV_CALLBACK_AMEX,
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
                    showDialog("Error", "Please login with AMEX/DINERS ", 500);
                }
            } else {

                if (!m_client.isLoggedIn()) {
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    finish();
                    showToastMessage("Please login with VISA/MASTER to proceed visa or master card.");
                }

                if (profileInfo != null) {
                    switch (m_amount.getCurrencyType()) {
                        case Consts.LKR:
                            if (profileInfo.isLkrEnabled()) {
                                if (profileInfo.getLkrMaxAmount() < m_amount.getValue()) {
                                    //error message
                                    showDialog("Error", "You can't exceed LKR " + profileInfo.getLkrMaxAmount(), 500);
                                } else {
                                    m_client.dsEmvSale(EMV_CALLBACK,
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
                                    m_client.dsEmvSale(EMV_CALLBACK,
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
                                    m_client.dsEmvSale(EMV_CALLBACK,
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
                                    m_client.dsEmvSale(EMV_CALLBACK,
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
                    showDialog("Error", "Please login with VISA/MASTER", 500);
                }
            }
        }

		/*if (lang_status == LangPrefs.LAN_EN) {

			m_client.dsEmvSale(5000, "Please wait.", req);

		} else if (lang_status == LangPrefs.LAN_SIN) {

			m_client.dsEmvSale(5000, "lreKdlr /|S is�kak'", req);

		} else if (lang_status == LangPrefs.LAN_TA) {

			m_client.dsEmvSale(5000, "rw;W nghWj;jpUq;fs;", req);

		} else {
			// do nothing
		}*/

    }

    protected void iccErrCallBack(String data) {
        Log.d(LOG_TAG, "iccErrCallBack : data = " + data);
        DSICCOflineErrReq req = new DSICCOflineErrReq();
        req.setStrLog(data);
        req.setAmount(m_amount.getValue());
        m_client.DSICCOflineErr(4444, "Sending Error log. Please wait ..", req);
    }

    protected void iccIssuerScriptSuccessCallBack(String data) {
        Log.d(LOG_TAG, "iccIssuerScriptSuccessCallBack : data = " + data);
        DSICCOflineErrReq req = new DSICCOflineErrReq();
        req.setStrLog(data);
        req.setAmount(m_amount.getValue());
        m_client.BatchEmvLogs(5555, "Sending ICC log after processing issuer script. Please wait ..", req);
    }

    protected void iccIssuerScriptFailCallBack(String data) {
        Log.d(LOG_TAG, "iccIssuerScriptFailCallBack : data = " + data);
        DSICCOflineErrReq req = new DSICCOflineErrReq();
        req.setStrLog(data);
        req.setAmount(m_amount.getValue());
        m_client.BatchEmvLogs(6666, "Sending ICC log after processing issuer script. Please wait ..", req);
    }

    protected void nfcCallBack(String ksn, String cardHolder, String track2, String maskedPan,String nfcdata){

//        Log.i("LOG_TAG" , "inside  nfcCallBack");
//
//        Log.i("JEYLOGS", "nfc data : " + nfcdata);

        Log.d(LOG_TAG, "swipeCallBack : ksn = " + ksn + " cardHolder = " + cardHolder + " track2 = " + track2 + " maskedPan = " + maskedPan + " CURENCY " + m_amount.getCurrencyType());

        if (ksn == null || cardHolder == null || track2 == null || maskedPan == null) {
            showDialog("Error", "Couldn't recoganize the card.");
            return;
        }

        nfcReq = new TXDsNfcReq();
        nfcReq.setAmount(m_amount.getValue());
        nfcReq.setCurrencyType(m_amount.getCurrencyType());
        nfcReq.setInstallment(m_amount.getInstallments());
        nfcReq.setF35(track2);
        nfcReq.setCardHolderName(cardHolder);
        nfcReq.setKsn(ksn);
        nfcReq.setIcData(nfcdata);

        if (deviceId != null) {
            nfcReq.setCardReaderId(deviceId);
        } else {
            nfcReq.setCardReaderId("none");
        }

        if (m_order != null) {
            nfcReq.setMerchantInvoiceId(m_order.getMerchantInvoiceId());
        }

        nfcReq.setClientTime(new Date());
        nfcReq.setTsToken(System.currentTimeMillis());
        nfcReq.setRndToken(Crypto.generateLongToken());
        nfcReq.setInvoice(Config.generateINVOICE(this));



        if (maskedPan != null) {
            String firstFourDigit = maskedPan.substring(0, 4);

            int maskedPanNumeric = Integer.parseInt(firstFourDigit);
            int firstDigit = Integer.parseInt(maskedPan.substring(0, 1));
            int firstTwoDigits = Integer.parseInt(maskedPan.substring(0, 2));
            int firstThreeDigits = Integer.parseInt(maskedPan.substring(0, 3));

            if (firstDigit == 4 || (maskedPanNumeric >= 2221 && maskedPanNumeric <= 2720) ||
                    (firstTwoDigits >= 51 && firstTwoDigits <= 55)) {

                if (m_client.isLoggedIn()) {

                    // visa/master card
                    if (profileInfo != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfo.isLkrEnabled()) {
                                    if (profileInfo.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfo.getLkrMaxAmount(), 500);
                                    } else {
                                        m_client.dsNfcSale(EMV_CALLBACK,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client.dsNfcSale(EMV_CALLBACK,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client.dsNfcSale(EMV_CALLBACK,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client.dsNfcSale(EMV_CALLBACK,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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

                } else {
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with VISA/MASTER to proceed visa or master card.");
                    finish();
                }
            } else if (firstTwoDigits == 34 || firstTwoDigits == 37) {

                if (m_client_amex.isLoggedIn()) {
                    //american express card
                    if (profileInfoAmex != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfoAmex.isLkrEnabled()) {
                                    if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                    } else {
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with AMEX/Diners to proceed amex or diners club card.");
                    finish();
                }


            } else if ((firstThreeDigits >= 300 && firstThreeDigits <= 305) || firstThreeDigits == 3095 ||
                    firstTwoDigits == 36 || firstTwoDigits == 38 || firstTwoDigits == 39) {
                //diners club international

                if (m_client_amex.isLoggedIn()) {
                    if (profileInfoAmex != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfoAmex.isLkrEnabled()) {
                                    if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                    } else {
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                                        m_client_amex.dsNfcSale(EMV_CALLBACK_AMEX,
                                                ProgressDialogMessagesUtil.dsSalesAudioTranslation(lang_status), nfcReq);
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
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with AMEX/Diners to proceed amex or diners club card.");
                    finish();
                }
            }
        }


    }

    protected void swipeCallBack(String ksn, String cardHolder, String track2, String maskedPan) {

        Log.d(LOG_TAG, "swipeCallBack : ksn = " + ksn + " cardHolder = " + cardHolder + " track2 = " + track2 + " maskedPan = " + maskedPan + " CURENCY " + m_amount.getCurrencyType());

        if (ksn == null || cardHolder == null || track2 == null || maskedPan == null) {
            showDialog("Error", "Couldn't recoganize the card.");
            return;
        }

        TxDSSaleReq req = new TxDSSaleReq();
        req.setAmount(m_amount.getValue());
        req.setCurrencyType(m_amount.getCurrencyType());
        req.setInstallment(m_amount.getInstallments());
        req.setF35(track2);
        req.setCardHolderName(cardHolder);
        req.setKsn(ksn);
        req.setMaskedPan(maskedPan);
        // req.setCardReaderId("aa");

        if (deviceId != null) {
            req.setCardReaderId(deviceId);
        } else {
            req.setCardReaderId("none");
        }

        if (m_order != null) {
            req.setMerchantInvoiceId(m_order.getMerchantInvoiceId());
        }

        req.setClientTime(new Date());
        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(Crypto.generateLongToken());
        req.setInvoice(Config.generateINVOICE(this));

        if (isFallback) {
            req.setIsFallBack(1);
        } else {
            req.setIsFallBack(0);
        }

        m_saleRes = null;

        if (maskedPan != null) {
            String firstFourDigit = maskedPan.substring(0, 4);

            int maskedPanNumeric = Integer.parseInt(firstFourDigit);
            int firstDigit = Integer.parseInt(maskedPan.substring(0, 1));
            int firstTwoDigits = Integer.parseInt(maskedPan.substring(0, 2));
            int firstThreeDigits = Integer.parseInt(maskedPan.substring(0, 3));

            if (firstDigit == 4 || (maskedPanNumeric >= 2221 && maskedPanNumeric <= 2720) ||
                    (firstTwoDigits >= 51 && firstTwoDigits <= 55)) {

                if (m_client.isLoggedIn()) {

                    // visa/master card
                    if (profileInfo != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfo.isLkrEnabled()) {
                                    if (profileInfo.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfo.getLkrMaxAmount(), 500);
                                    } else {
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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

                } else {
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with VISA/MASTER to proceed visa or master card.");
                    finish();
                }
            } else if (firstTwoDigits == 34 || firstTwoDigits == 37) {

                if (m_client_amex.isLoggedIn()) {
                    //american express card
                    if (profileInfoAmex != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfoAmex.isLkrEnabled()) {
                                    if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                    } else {
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with AMEX/Diners to proceed amex or diners club card.");
                    finish();
                }


            } else if ((firstThreeDigits >= 300 && firstThreeDigits <= 305) || firstThreeDigits == 3095 ||
                    firstTwoDigits == 36 || firstTwoDigits == 38 || firstTwoDigits == 39) {
                //diners club international

                if (m_client_amex.isLoggedIn()) {
                    if (profileInfoAmex != null) {
                        switch (m_amount.getCurrencyType()) {
                            case Consts.LKR:
                                if (profileInfoAmex.isLkrEnabled()) {
                                    if (profileInfoAmex.getLkrMaxAmount() < m_amount.getValue()) {
                                        //error message
                                        showDialog("Error", "You can't exceed LKR " + profileInfoAmex.getLkrMaxAmount(), 500);
                                    } else {
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                                        this.pushActivity(SwipeLast4.class, req);
                                        finish();
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
                    writeIssuerScriptOnError("8A023035");
                    SelectPaymentMethod.signal_val = 111111;
                    showToastMessage("Please login with AMEX/Diners to proceed amex or diners club card.");
                    finish();
                }
            }
        }

    }

    public void onMSGDlgBtnClick(int callerId) {
        Log.d(LOG_TAG, "onMSGDlgBtnClick : callerId= " + callerId);
        if (callerId == 210) {
            fireOpenTxList();
            return;
        }

        if (callerId == 100) {
            finish();
            return;
        }

        if (callerId == 500) {
            SelectPaymentMethod.signal_val = SelectPaymentMethod.SIGNAL_CLOSE;
            finish();
        }

        super.onMSGDlgBtnClick(callerId);
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        Log.d(LOG_TAG, "onCallError : api= " + api + " callerId= " + callerId + "e= " + e.toString());

        if (callerId == 4444) {
            _onTransactionDeclined();
            return;
        }

        if (callerId == 6666) {
            _onTransactionDeclined();
            return;
        }

        if (callerId == 5555) {
            processSignature();
            return;
        }

        if (callerId == EMV_CALLBACK || callerId == EMV_CALLBACK_AMEX) {

            if (m_amount != null) {
                storeErrorLog("Received error in emv reponse for amount : "
                        + m_amount.getValue()
                        + " "
                        + UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType())
                        + "  Error code  : "
                        + e.getErrcode());

                turnoffIcc();
                showDialog("Error",
                        e.toString(),
                        100);
                return;
            }

            if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                turnoffIcc();
                showDialog("Error",
                        "Error in Communication has occurred. Please Check your last transaction and ensure the transaction has been completed successfully.",
                        210);
                return;
            }

            isICCOnlineTxFail = true;
            isICCOnlineTxSuccess = false;
            strF39ErrMsg = e.toString();

            serverExceptionCode = e.getErrcode();

            if (e.getIsState() == 1) {
                writeIssuerScriptOnError(e.getIsMessage());
                return;
            } else {
                // 8A023035
                writeIssuerScriptOnError("8A023035");
                return;
            }

            // turnoffIcc();
            // showDialog("Error", e.toString(), 100);
        }

        // super.onCallError(api, callerId, e) ;
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        Log.d(LOG_TAG, "onCallSuccess : api= " + api + " callerId= " + callerId + " data= " + data.getJson());

        if (callerId == 4444) {
            _onTransactionDeclined();
            return;
        }

        if (callerId == 4444) {
            _onTransactionDeclined();
            return;
        }

        if (callerId == 5555) {
            processSignature();
            return;
        }

        if (callerId == EMV_CALLBACK || callerId == EMV_CALLBACK_AMEX) {
            if (data != null) {
                if (data instanceof TxSaleRes) {

                    if (m_amount != null) {
                        storeMessageLog("Received success in emv reponse for amount : "
                                + m_amount.getValue()
                                + " " + UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType()));
                    }

                    m_saleRes = (TxSaleRes) data;
                    m_saleRes.setCurrencyType(m_amount != null ? m_amount.getCurrencyType() : Consts.LKR);

                    // processdSignature() ;

                    isICCOnlineTxSuccess = true;
                    isICCOnlineTxFail = false;
                    strF39ErrMsg = null;

                    if(nfcReq != null){
                        SignaturePad.isClose = false;
                        this.pushActivity(SignaturePad.class, m_saleRes);
                        finish();
                    }else if (m_saleRes.getArpc() != null) {
                        txtStatus.setText("Updating the card ...");
                        writeIssuerScriptOnSuccess(m_saleRes.getArpc());
                        // processSignature() ;
                    } else {
                        // 8A023030
                        writeIssuerScriptOnSuccess("8A023030");
                        // processSignature();
                    }

                }
            }

        }

    }

    /**
     * Initialization of views
     */
    private void InitViews() {

        m_client = new MsgClient(this);
        m_client.loadCredentials(this);

        m_client_amex = new MsgClientAmex(this);
        m_client_amex.loadCredentials(this);

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("");
        txtStatus.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmountValue = (TextView) findViewById(R.id.txtAmountValue);
        txtAmountValue.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.gifView));

        //if pinpad
        if (Config.getReaderType(getApplicationContext()) == 3) {
            animatedGifImageView.setAnimatedGif(R.raw.pincard, AnimatedGifImageView.TYPE.FIT_CENTER);
        } else {
            //if not pin pad, set the anim relevant to bank

            if ((m_client.isLoggedIn() && m_client_amex.isLoggedIn()) || m_client.isLoggedIn()) {
                if (Config.getBankCode(getApplicationContext()).equals("hnb")) {
                    animatedGifImageView.setAnimatedGif(R.raw.hnb_anim, AnimatedGifImageView.TYPE.FIT_CENTER);
                }else  if (Config.getBankCode(getApplicationContext()).equals("seylan")) {
                    animatedGifImageView.setAnimatedGif(R.raw.seylan_anim, AnimatedGifImageView.TYPE.FIT_CENTER);
                }else if (Config.getBankCode(getApplicationContext()).equals("commercial")) {
                    animatedGifImageView.setAnimatedGif(R.raw.com_anim, AnimatedGifImageView.TYPE.FIT_CENTER);
                }  else if (Config.getBankCode(getApplicationContext()).equals("boc")) {
                    animatedGifImageView.setAnimatedGif(R.raw.boc_anim, AnimatedGifImageView.TYPE.FIT_CENTER);
                }else{
                    animatedGifImageView.setAnimatedGif(R.raw.smallcard_gif, AnimatedGifImageView.TYPE.FIT_CENTER);
                }
            }else if(m_client_amex.isLoggedIn()){
                animatedGifImageView.setAnimatedGif(R.raw.ntb_anim, AnimatedGifImageView.TYPE.FIT_CENTER);
            }


        }


        if (com.setting.env.Environment.is_demo_version) {
            animatedGifImageView.setVisibility(View.GONE);
        } else {
            animatedGifImageView.setVisibility(View.VISIBLE);
        }


        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Swipe(txtTitle, txtAmount);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Swipe(txtTitle, txtAmount);
        } else {
            // do nothing
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
