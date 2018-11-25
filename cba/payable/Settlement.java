package com.cba.payable;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.EnumException;
import com.mpos.pojo.APIData;
import com.mpos.pojo.LstSettleSummary;
import com.mpos.pojo.SimpleReq;
import com.mpos.pojo.TxSettlementReq;
import com.mpos.pojo.TxSettlementResponse;
import com.mpos.pojo.TxSettlementSummaryEle;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;
import com.setting.env.Consts;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Settlement extends GenericActivity {

    private static final int CALL_SUMMARY_DATA = 100;
    private static final int CALL_SUMMARY_DATA_AMEX = 101;
    private static final int CALL_BATCH_CLOSE = 200;
    private static final int CALL_BATCH_CLOSE_AMEX = 201;
    TextView txtTitle, txtVisaSettlement, txtMasterSettlement, txtVisaTotalCount, txtMasterTotalCount, txtVisaAmount,
            txtMasterAmount, txtSettleNow;
    TextView txtTotalSettlements, txtTotalAmount, txtTotalAmountCount, txtTitle1, txtTitle2, txtTitle3, txtAmexSettlement,
            txtDinersSettlement, txtAmexTotalCount, txtDinersTotalCount, txtAmexAmount, txtDinersAmount, txtTotalSettlementsCount,
            txtVisaCurrencyType, txtMasterCurrencyType, txtAmexCurrencyType, txtDinersCurrencyType, txtCurrencyType;
    RelativeLayout rlSettlementOk;
    LinearLayout layout_1, layout_2;
    ImageView imgVisa, imgMaster, imgAmex, imgDiners;
    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;
    View lkrSelector, usdSelector, gbpSelector, eurSelector;
    SimpleReq sampReq;
    BigDecimal totalAmount_1;
    int totalCount = 0;
    int totalSales = 0;
    double totalAmount = 0.0;
    private TXAdapter adapter;
    // private Button btnSettle ;
    private DecimalFormat df;
    private int mCurrencyType = Consts.LKR;
    private int errorOn = 0;
    private boolean isSocketExceptionOccured = false;
    private boolean isTimeOutErrorOccured = false;
    private boolean isSettleAvailable = false;
    private boolean isSettleAvailableAmex = false;
    private boolean isErrorOnSettlement = false;
    private int countSettlements;
    private double countAmount;
    boolean callSent = false;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_settlement);

        setContentView(R.layout.activity_settlement_2);

        initViews();

        // lsTx = (ListView) this.findViewById(R.id.lstOpenTx);
        // btnSettle = (Button)this.findViewById(R.id.btnSettle);

        df = new DecimalFormat("####0.00");
        totalAmount_1 = new BigDecimal("0");

        /*if (Config.getVersionId(this) == -1) {
            SimpleReq req = new SimpleReq();
            req.setValue(1);
            m_client.settlementSummary(CALL_SUMMARY_DATA, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
        } else {
            downloadData();
        }*/


        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {

            sampReq = new SimpleReq();
            sampReq.setValue(1);
            sampReq.setCurrencyType(mCurrencyType);

            //dual login
            layout_1.setVisibility(View.VISIBLE);
            layout_2.setVisibility(View.VISIBLE);

            if (Config.getVersionId(this) == -1) {
                SimpleReq req = new SimpleReq();
                req.setValue(1);
                //m_client.settlementSummary(CALL_SUMMARY_DATA, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
            } else {
                //downloadData(CALL_SUMMARY_DATA);
            }
        } else {

            if (m_client.isLoggedIn()) {
                layout_1.setVisibility(View.VISIBLE);
                layout_2.setVisibility(View.GONE);

                //single login in visa/master
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    sinlang_select.Apply_Settlements_2(txtTitle1, m_client.isLoggedIn(), m_client_amex.isLoggedIn());
                } else if (lang_status == LangPrefs.LAN_TA) {
                    tamlang_select.Apply_Settlements_2(txtTitle1, m_client.isLoggedIn(), m_client_amex.isLoggedIn());
                } else {
                    // do nothing
                }

                imgVisa.setImageResource(R.drawable.visa_settings);
                imgMaster.setImageResource(R.drawable.master);

                if (Config.getVersionId(this) == -1) {
                    SimpleReq req = new SimpleReq();
                    req.setValue(1);

                    //m_client.settlementSummary(CALL_SUMMARY_DATA, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                } else {
                    //downloadData(CALL_SUMMARY_DATA);
                }
            } else if (m_client_amex.isLoggedIn()) {

                layout_1.setVisibility(View.GONE);
                layout_2.setVisibility(View.VISIBLE);

                //single login in amex
                if (lang_status == LangPrefs.LAN_EN) {

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    sinlang_select.Apply_Settlements_2(txtTitle1, m_client.isLoggedIn(), m_client_amex.isLoggedIn());
                } else if (lang_status == LangPrefs.LAN_TA) {
                    tamlang_select.Apply_Settlements_2(txtTitle1, m_client.isLoggedIn(), m_client_amex.isLoggedIn());
                } else {
                    // do nothing
                }
                imgVisa.setImageResource(R.drawable.amex);
                imgMaster.setImageResource(R.drawable.diners_club);

                if (Config.getVersionId(this) == -1) {
                    SimpleReq req = new SimpleReq();
                    req.setValue(1);

                    //m_client_amex.settlementSummary(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                } else {
                    //downloadData(CALL_SUMMARY_DATA_AMEX);
                }
            }
        }
    }

    private void downloadData(int callerId) {

        callSent = true;

        if (callerId == CALL_SUMMARY_DATA) {
            SimpleReq req = new SimpleReq();
            req.setValue(1);
            req.setCurrencyType(mCurrencyType);
            df = new DecimalFormat("####0.00");

            sampReq = req;

            m_client.settlementSummaryV2(CALL_SUMMARY_DATA, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);

        } else if (callerId == CALL_SUMMARY_DATA_AMEX) {
            SimpleReq req = new SimpleReq();
            req.setValue(1);
            req.setCurrencyType(mCurrencyType);
            df = new DecimalFormat("####0.00");
            m_client_amex.settlementSummaryV2(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
        }

    }

    public void onSettle(View v) {

        if (!isSettleAvailable && !isSettleAvailableAmex) {
            showDialog("Error", "There are no sales to Settle.");
            return;
        }

        if (isNetworkConnected()) {
            if (m_client.isLoggedIn() && m_client.isLoggedIn()) {

                if (isSettleAvailable) {
                    TxSettlementReq req = new TxSettlementReq();
                    req.setValSTN(Config.generateSTN(this));
                    req.setBatchNumber(Config.generateBatchId(this));
                    req.setCurrencyType(mCurrencyType);

                    m_client.settlement(CALL_BATCH_CLOSE, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);

                }

                if (isSettleAvailableAmex) {
                    TxSettlementReq req = new TxSettlementReq();
                    req.setValSTN(Config.generateSTN(this));
                    req.setBatchNumber(Config.generateBatchId(this));
                    req.setCurrencyType(mCurrencyType);

                    m_client_amex.settlement(CALL_BATCH_CLOSE_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                }
            } else {
                if (m_client.isLoggedIn()) {
                    TxSettlementReq req = new TxSettlementReq();
                    req.setValSTN(Config.generateSTN(this));
                    req.setBatchNumber(Config.generateBatchId(this));
                    req.setCurrencyType(mCurrencyType);

                    m_client.settlement(CALL_BATCH_CLOSE, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                } else if (m_client_amex.isLoggedIn()) {
                    TxSettlementReq req = new TxSettlementReq();
                    req.setValSTN(Config.generateSTN(this));
                    req.setBatchNumber(Config.generateBatchId(this));
                    req.setCurrencyType(mCurrencyType);

                    m_client_amex.settlement(CALL_BATCH_CLOSE_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                }
            }
        } else {
            showDialog("Error", "Please Connect to the internet");
        }
    }

    private void updateValues(LstSettleSummary res) {

        txtVisaTotalCount.setText("0");
        txtVisaAmount.setText(df.format(0));
        txtMasterTotalCount.setText("0");
        txtMasterAmount.setText(df.format(0));
        txtTotalSettlementsCount.setText("0");
        txtTotalAmountCount.setText(df.format(0));

        int totalCount = 0;
        BigDecimal totalAmount = new BigDecimal("0");

        if (res == null || res.getSize() == 0) {
            isSettleAvailable = false;
            rlSettlementOk.setVisibility(View.INVISIBLE);
        } else {

            for (int i = 0; i < res.getSize(); i++) {

                TxSettlementSummaryEle ele = res.getData(i);

                if (ele != null && ele.getCurrency() == 0) {
                    ele.setCurrency(mCurrencyType);
                }

                if (ele != null && ele.getType() == RecSale.CARD_VISA && mCurrencyType == ele.getCurrency()) {

                    totalCount = totalCount + ele.getCount();
                    totalAmount = totalAmount.add(ele.getAmount());

                    txtVisaTotalCount.setText("" + ele.getCount());
                    txtVisaAmount.setText(df.format(ele.getAmount()));
                    isSettleAvailable = true;
                }

                if (ele != null && ele.getType() == RecSale.CARD_MASTER && mCurrencyType == ele.getCurrency()) {

                    totalCount = totalCount + ele.getCount();
                    totalAmount = totalAmount.add(ele.getAmount());

                    txtMasterTotalCount.setText("" + ele.getCount());
                    txtMasterAmount.setText(df.format(ele.getAmount()));
                    isSettleAvailable = true;
                }
            }


            //txtTotalSettlementsCount.setText("" + totalCount);
            //txtTotalAmountCount.setText(df.format(totalAmount));

            calculateTotal(totalCount, totalAmount);
            controlSettlementBtn();
            callSent = false;
        }
    }

    //AMEX DATA
    private void updateValuesAmex(LstSettleSummary res) {
        txtAmexTotalCount.setText("0");
        txtAmexAmount.setText(df.format(0));
        txtDinersTotalCount.setText("0");
        txtDinersAmount.setText(df.format(0));
        txtTotalSettlementsCount.setText("0");
        txtTotalAmountCount.setText(df.format(0));

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login

            if (res == null || res.getSize() == 0) {
                isSettleAvailableAmex = false;
                rlSettlementOk.setVisibility(View.INVISIBLE);
            } else {

                for (int i = 0; i < res.getSize(); i++) {

                    TxSettlementSummaryEle ele = res.getData(i);

                    if (ele.getType() == RecSale.CARD_AMEX && mCurrencyType == ele.getCurrency()) {
                        isSettleAvailableAmex = true;
                        totalCount = totalCount + ele.getCount();
                        totalAmount_1 = totalAmount_1.add(ele.getAmount());

                        txtAmexTotalCount.setText(String.valueOf(ele.getCount()));
                        txtAmexAmount.setText(df.format(ele.getAmount()));
                    }

                    if (ele.getType() == RecSale.CARD_DINERS && mCurrencyType == ele.getCurrency()) {
                        isSettleAvailableAmex = true;
                        totalCount = totalCount + ele.getCount();
                        totalAmount_1 = totalAmount_1.add(ele.getAmount());

                        txtDinersTotalCount.setText(String.valueOf(ele.getCount()));
                        txtDinersAmount.setText(df.format(ele.getAmount()));
                    }
                }

                if (isSettleAvailableAmex) {
                    rlSettlementOk.setVisibility(View.VISIBLE);
                }

            }

        } else {

            if (m_client_amex.isLoggedIn()) {
                //single login in amex
                if (res == null || res.getSize() == 0) {
                    isSettleAvailableAmex = false;
                    rlSettlementOk.setVisibility(View.INVISIBLE);

                } else {

                    for (int i = 0; i < res.getSize(); i++) {

                        TxSettlementSummaryEle ele = res.getData(i);

                        if (ele != null && ele.getType() == RecSale.CARD_AMEX && mCurrencyType == ele.getCurrency()) {
                            isSettleAvailableAmex = true;
                            txtAmexTotalCount.setText(String.valueOf(ele.getCount()));
                            txtAmexAmount.setText(df.format(ele.getAmount()));

                            totalCount = totalCount + ele.getCount();
                            totalAmount_1 = totalAmount_1.add(ele.getAmount());
                        }

                        if (ele != null && ele.getType() == RecSale.CARD_DINERS && mCurrencyType == ele.getCurrency()) {
                            isSettleAvailableAmex = true;
                            txtDinersTotalCount.setText(String.valueOf(ele.getCount()));
                            txtDinersAmount.setText(df.format(ele.getAmount()));

                            totalCount = totalCount + ele.getCount();
                            totalAmount_1 = totalAmount_1.add(ele.getAmount());
                        }
                    }

                    if (isSettleAvailableAmex) {
                        rlSettlementOk.setVisibility(View.VISIBLE);
                    }
                }


                //txtTotalSettlementsCount.setText(String.valueOf(totalCount));
                //txtTotalAmountCount.setText(df.format(totalAmount_1));
            }
        }

        calculateTotal(totalCount, totalAmount_1);
        controlSettlementBtn();
        callSent = false;
    }

    private void controlSettlementBtn() {

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            if (isSettleAvailable || isSettleAvailableAmex) {
                rlSettlementOk.setVisibility(View.VISIBLE);
            } else {
                rlSettlementOk.setVisibility(View.INVISIBLE);
            }
        } else if (m_client.isLoggedIn()) {
            if (isSettleAvailable) {
                rlSettlementOk.setVisibility(View.VISIBLE);
            } else {
                rlSettlementOk.setVisibility(View.INVISIBLE);
            }
        } else if (m_client_amex.isLoggedIn()) {
            if (isSettleAvailableAmex) {
                rlSettlementOk.setVisibility(View.VISIBLE);
            } else {
                rlSettlementOk.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void calculateTotal(int totalCount, BigDecimal totalAmount) {
        countAmount += Double.valueOf(String.valueOf(totalAmount));
        txtTotalAmountCount.setText(df.format(countAmount));

        countSettlements += totalCount;
        txtTotalSettlementsCount.setText("" + countSettlements);
    }


    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 200) {
            txtVisaTotalCount.setText("0");
            txtVisaAmount.setText(df.format(0));
            txtMasterTotalCount.setText("0");
            txtMasterAmount.setText(df.format(0));
            txtAmexTotalCount.setText("0");
            txtAmexAmount.setText(df.format(0));
            txtDinersTotalCount.setText("0");
            txtDinersAmount.setText(df.format(0));
            txtTotalSettlementsCount.setText("0");
            txtTotalAmountCount.setText(df.format(0));
            isSettleAvailable = false;
            isSettleAvailableAmex = false;
            //finish();
        } else if (callerId == 210) {
            fireOpenTxList();
        } else if (callerId == 111) {
            // btnSettle.setEnabled(true) ;
        } else if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
        }
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        System.out.println("settlementStatus ok " + data.getJson());

        TxSettlementReq req_tx = new TxSettlementReq();
        req_tx.setValSTN(Config.generateSTN(this));
        req_tx.setBatchNumber(Config.generateBatchId(this));
        req_tx.setCurrencyType(mCurrencyType);

        if (callerId == CALL_BATCH_CLOSE) {
            if (data != null) {
                if (data instanceof TxSettlementResponse) {

                    if (m_client_amex.isLoggedIn()) {
                        //dual login
                        TxSettlementResponse res = (TxSettlementResponse) data;
                        totalSales += res.getTotalSales();
                        totalAmount += res.getTotalAmount();

                        if (isSettleAvailableAmex) {
                            m_client_amex.settlement(CALL_BATCH_CLOSE_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req_tx);
                        } else {
                            showInfo("Settlement", "Settlement completed.\n Total transactions:" + totalSales
                                    + " Amount:" + df.format(totalAmount), 200);
                        }
                    } else {

                        TxSettlementResponse res = (TxSettlementResponse) data;
                        totalSales += res.getTotalSales();
                        totalAmount += res.getTotalAmount();

                        showInfo("Settlement", "Settlement completed.\n Total transactions:" + totalSales
                                + " Amount:" + df.format(totalAmount), 200);
                    }
                }
            }

            return;
        }

        if (callerId == CALL_BATCH_CLOSE_AMEX) {
            if (data != null) {
                if (data instanceof TxSettlementResponse) {

                    TxSettlementResponse res = (TxSettlementResponse) data;
                    totalSales += res.getTotalSales();
                    totalAmount += res.getTotalAmount();

                    showInfo("Settlement", "Settlement completed.\n Total transactions:" + totalSales
                            + " Amount:" + df.format(totalAmount), 200);

                    if (isErrorOnSettlement) {
                        //visa/master request fails - dual login
                        showToastMessage("visa/master request fails - dual login");
                    }
                }
            } // URL: https://123.231.14.207:8095/MobilePaymentR1000/SettlementSummary
            return;
        }

        if (callerId == CALL_SUMMARY_DATA) {
            if (data != null) {
                if (data instanceof LstSettleSummary) {
                    LstSettleSummary res = (LstSettleSummary) data;
                    //adapter = new TXAdapter(res);
                    //lsTx.setAdapter(adapter);
                    updateValues(res);

                    if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
                        //dual login
                        if (Config.getVersionId(this) == -1) {
                            SimpleReq req = new SimpleReq();
                            req.setValue(1);
                            m_client_amex.settlementSummary(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req);
                        } else {
                            downloadData(CALL_SUMMARY_DATA_AMEX);
                        }
                    }

                }
            }

            return;
        }

        if (callerId == CALL_SUMMARY_DATA_AMEX) {
            if (data != null) {
                if (data instanceof LstSettleSummary) {
                    LstSettleSummary res = (LstSettleSummary) data;
                    updateValuesAmex(res);
                }
            }

            if (errorOn == 1) {
                if (isSocketExceptionOccured) {
                    showDialog("Error", "VISA/MASTERCARD settlement failed. Please check your internet connection.");
                } else if (isTimeOutErrorOccured) {
                    showDialog("Error", "VISA/MASTERCARD settlement failed. Connection to the server timeout.");
                }
            }

            return;
        }
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        callSent = false;

        if (callerId == CALL_BATCH_CLOSE) {
            isErrorOnSettlement = true;

            if (m_client_amex.isLoggedIn()) {
                if (isSettleAvailableAmex) {
                    TxSettlementReq req_tx = new TxSettlementReq();
                    req_tx.setValSTN(Config.generateSTN(this));
                    req_tx.setBatchNumber(Config.generateBatchId(this));
                    req_tx.setCurrencyType(mCurrencyType);

                    m_client_amex.settlement(CALL_BATCH_CLOSE_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), req_tx);
                }
            } else {
                //visa/master request fails - single login
                //showDialog("Error", "VISA/MASTERCARD settlement failed.");
                showDialog("Error", "Visa/Master " + e.toString());
            }
            return;
        }

        if (callerId == CALL_BATCH_CLOSE_AMEX) {
            //error message
            if (isErrorOnSettlement) {
                //both requests fails - dual login
                showDialog("Error", "Settlement failed for all card types.");
            } else {
                //showDialog("Error", "AMEX/Diners " + e.toString());
            }
            return;
        }

        if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
            showDialog("Error",
                    "Error in Communication has occurred. Please Check your last transaction and ensure the transaction has been completed successfully.",
                    210);
            return;
        }

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            if (callerId == CALL_SUMMARY_DATA) {
                if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION) {
                    errorOn = 1;
                    isSocketExceptionOccured = true;
                    m_client_amex.settlementSummaryV2(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), sampReq);
                    return;
                }

                if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                    errorOn = 1;
                    isTimeOutErrorOccured = true;
                    m_client_amex.settlementSummaryV2(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), sampReq);
                    return;
                }
            } else if (callerId == CALL_SUMMARY_DATA_AMEX) {
                if (errorOn == 0) {
                    if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION) {
                        errorOn = 2; //error on amex request only
                        showDialog("Error", "AMEX/DINERS CLUB settlement failed. Please check your internet connection.");
                        return;
                    }

                    if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                        errorOn = 2; //error on amex request only
                        showDialog("Error", "AMEX/DINERS CLUB settlement failed. Connection to the server timeout.");
                        return;
                    }

                } else if (errorOn == 1) {
                    if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION) {
                        errorOn = 3; //error on both
                        if (isSocketExceptionOccured) {
                            showDialog("Error", "Please check your internet connection.");
                            return;
                        } else if (isTimeOutErrorOccured) {
                            showDialog("Error", "Connection to the server timeout for VISA/MASTERCARD settlement and check your internet connection for AMEX/DINERSCLUB Settlement.");
                            return;
                        }

                    }

                    if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                        errorOn = 3; //error on both
                        if (isSocketExceptionOccured) {
                            showDialog("Error", "Please check your internet connection for VISA/MASTERCARD settlement and server timeout for AMEX/DINERSCLUB settlement.");
                            return;
                        } else if (isTimeOutErrorOccured) {
                            showDialog("Error", "Connection to the server timeout.");
                            return;
                        }
                    }
                }
            }
        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION) {
                    showDialog("Error", "VISA/Master Card settlement failed. Please check your internet connection.");
                    return;
                }

                if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                    showDialog("Error", "VISA/Master Card settlement failed. Connection to the server timeout.");
                    return;
                }
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                if (e.getErrcode() == ApiException.SCOCKET_EXCEPTION) {
                    showDialog("Error", "AMEX/Diners Club settlement failed. Please check your internet connection.");
                    return;
                }

                if (e.getErrcode() == ApiException.TIMEOUT_ERROR) {
                    showDialog("Error", "AMEX/Diners Club settlement failed. Connection to the server timeout.");
                    return;
                }
            }
        }

        if (callerId == CALL_SUMMARY_DATA) {
            if (e.getEnumException() == EnumException.NO_RECORD_FOUND) {
                isSettleAvailable = false;
                rlSettlementOk.setVisibility(View.INVISIBLE);

                if (isSettleAvailable || isSettleAvailableAmex) {
                    rlSettlementOk.setVisibility(View.VISIBLE);
                }

                if (m_client_amex.isLoggedIn()) {
                    m_client_amex.settlementSummaryV2(CALL_SUMMARY_DATA_AMEX, ProgressDialogMessagesUtil.settlementTranslation(lang_status), sampReq);
                }

                return;
            }
        }

        if (callerId == CALL_SUMMARY_DATA_AMEX) {
            if (e.getEnumException() == EnumException.NO_RECORD_FOUND) {
                isSettleAvailableAmex = false;
                rlSettlementOk.setVisibility(View.INVISIBLE);

                if (isSettleAvailable || isSettleAvailableAmex) {
                    rlSettlementOk.setVisibility(View.VISIBLE);
                }

                return;
            }
        }

        super.onCallError(api, callerId, e);
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    public void onCurrencySelect(View view) {
        int id = view.getId();

        if (id == R.id.image_lkr || id == R.id.textview_lkr) {
            resetValues();
            selectLKR();
        }
        if (id == R.id.image_usd || id == R.id.textview_usd) {
            resetValues();
            selectUSD();
        }
        if (id == R.id.image_gbp || id == R.id.textview_gbp) {
            resetValues();
            selectGBP();
        }
        if (id == R.id.image_eur || id == R.id.textview_eur) {
            resetValues();
            selectEUR();
        }
    }

    private void selectLKR() {
        if(callSent){
            return;
        }
        mCurrencyType = Consts.LKR;
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        txtVisaCurrencyType.setText("LKR");
        txtMasterCurrencyType.setText("LKR");
        txtCurrencyType.setText("LKR");

        txtAmexCurrencyType.setText("LKR");
        txtDinersCurrencyType.setText("LKR");

        countSettlements = 0;
        countAmount = 0;
        totalCount = 0;
        totalAmount_1 = new BigDecimal("0");

        totalSales = 0;
        totalAmount = 0.0;
        isSettleAvailable = false;
        isSettleAvailableAmex = false;

        isErrorOnSettlement = false;

        if (m_client.isLoggedIn()) {
            downloadData(CALL_SUMMARY_DATA);
        } else {
            downloadData(CALL_SUMMARY_DATA_AMEX);
        }
    }

    private void selectUSD() {
        if(callSent){
            return;
        }
        mCurrencyType = Consts.USD;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        txtVisaCurrencyType.setText("USD");
        txtMasterCurrencyType.setText("USD");
        txtCurrencyType.setText("USD");

        txtAmexCurrencyType.setText("USD");
        txtDinersCurrencyType.setText("USD");

        countSettlements = 0;
        countAmount = 0;
        totalCount = 0;
        totalAmount_1 = new BigDecimal("0");

        totalSales = 0;
        totalAmount = 0.0;
        isSettleAvailable = false;
        isSettleAvailableAmex = false;

        isErrorOnSettlement = false;

        if (m_client.isLoggedIn()) {
            downloadData(CALL_SUMMARY_DATA);
        } else {
            downloadData(CALL_SUMMARY_DATA_AMEX);
        }
    }

    private void selectGBP() {
        if(callSent){
            return;
        }
        mCurrencyType = Consts.GBP;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        txtVisaCurrencyType.setText("GBP");
        txtMasterCurrencyType.setText("GBP");
        txtCurrencyType.setText("GBP");

        txtAmexCurrencyType.setText("GBP");
        txtDinersCurrencyType.setText("GBP");

        countSettlements = 0;
        countAmount = 0;
        totalCount = 0;
        totalAmount_1 = new BigDecimal("0");

        totalSales = 0;
        totalAmount = 0.0;
        isSettleAvailable = false;
        isSettleAvailableAmex = false;

        isErrorOnSettlement = false;

        if (m_client.isLoggedIn()) {
            downloadData(CALL_SUMMARY_DATA);
        } else {
            downloadData(CALL_SUMMARY_DATA_AMEX);
        }
    }

    private void selectEUR() {
        if(callSent){
            return;
        }
        mCurrencyType = Consts.EUR;
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        txtVisaCurrencyType.setText("EUR");
        txtMasterCurrencyType.setText("EUR");
        txtCurrencyType.setText("EUR");

        txtAmexCurrencyType.setText("EUR");
        txtDinersCurrencyType.setText("EUR");

        countSettlements = 0;
        countAmount = 0;
        totalCount = 0;
        totalAmount_1 = new BigDecimal("0");

        totalSales = 0;
        totalAmount = 0.0;
        isSettleAvailable = false;
        isSettleAvailableAmex = false;

        isErrorOnSettlement = false;

        if (m_client.isLoggedIn()) {
            downloadData(CALL_SUMMARY_DATA);
        } else {
            downloadData(CALL_SUMMARY_DATA_AMEX);
        }
    }

    private void initViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        ProgressDialogMessagesUtil pro = new ProgressDialogMessagesUtil(getApplicationContext());

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        txtVisaCurrencyType = (TextView) findViewById(R.id.txtVisaCurrencyType);
        txtMasterCurrencyType = (TextView) findViewById(R.id.txtMasterCurrencyType);
        txtCurrencyType = (TextView) findViewById(R.id.txtCurrencyType);

        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);

        txtTitle1 = (TextView) findViewById(R.id.txtTitle1);
        txtTitle1.setTypeface(TypeFaceUtils.setRobotoBold(getApplicationContext()));

        txtTitle2 = (TextView) findViewById(R.id.txtTitle2);
        txtTitle2.setTypeface(TypeFaceUtils.setRobotoBold(getApplicationContext()));

        txtTitle3 = (TextView) findViewById(R.id.txtTitle3);
        txtTitle3.setTypeface(TypeFaceUtils.setRobotoBold(getApplicationContext()));

        imgVisa = (ImageView) findViewById(R.id.imgVisa);
        imgMaster = (ImageView) findViewById(R.id.imgMaster);
        imgAmex = (ImageView) findViewById(R.id.imgAmex);
        imgDiners = (ImageView) findViewById(R.id.imgDiners);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVisaSettlement = (TextView) findViewById(R.id.txtVisaSettlement);
        txtVisaSettlement.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtMasterSettlement = (TextView) findViewById(R.id.txtMasterSettlement);
        txtMasterSettlement.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtAmexSettlement = (TextView) findViewById(R.id.txtAmexSettlement);
        txtAmexSettlement.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtDinersSettlement = (TextView) findViewById(R.id.txtDinersSettlement);
        txtDinersSettlement.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtVisaTotalCount = (TextView) findViewById(R.id.txtVisaTotalCount);
        txtVisaTotalCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtMasterTotalCount = (TextView) findViewById(R.id.txtMasterTotalCount);
        txtMasterTotalCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtAmexTotalCount = (TextView) findViewById(R.id.txtAmexTotalCount);
        txtAmexTotalCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtDinersTotalCount = (TextView) findViewById(R.id.txtDinersTotalCount);
        txtDinersTotalCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtVisaAmount = (TextView) findViewById(R.id.txtVisaAmount);
        txtVisaAmount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtMasterAmount = (TextView) findViewById(R.id.txtMasterAmount);
        txtMasterAmount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtAmexAmount = (TextView) findViewById(R.id.txtAmexAmount);
        txtAmexAmount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtDinersAmount = (TextView) findViewById(R.id.txtDinersAmount);
        txtDinersAmount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        /*txtSummary = (TextView) findViewById(R.id.txtSummary);
        txtSummary.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

		/*txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        txtDateTime.setTypeface(TypeFaceUtils.setRobotoLight(getApplicationContext()));*/

        txtTotalSettlements = (TextView) findViewById(R.id.txtTotalSettlements);
        txtTotalSettlements.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtTotalSettlementsCount = (TextView) findViewById(R.id.txtTotalSettlementsCount);
        txtTotalSettlementsCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        txtTotalAmount.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtTotalAmountCount = (TextView) findViewById(R.id.txtTotalAmountCount);
        txtTotalAmountCount.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtSettleNow = (TextView) findViewById(R.id.txtSettleNow);
        txtSettleNow.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtVisaCurrencyType = (TextView) findViewById(R.id.txtVisaCurrencyType);
        txtVisaCurrencyType.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtMasterCurrencyType = (TextView) findViewById(R.id.txtMasterCurrencyType);
        txtMasterCurrencyType.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtAmexCurrencyType = (TextView) findViewById(R.id.txtAmexCurrencyType);
        txtAmexCurrencyType.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtDinersCurrencyType = (TextView) findViewById(R.id.txtDinersCurrencyType);
        txtDinersCurrencyType.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtCurrencyType = (TextView) findViewById(R.id.txtCurrencyType);
        txtCurrencyType.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        rlSettlementOk = (RelativeLayout) findViewById(R.id.rlSettlementOk);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Settlements(txtTitle, txtTitle1, txtTitle2, txtTitle3, txtVisaSettlement,
                    txtMasterSettlement, txtAmexSettlement, txtDinersSettlement, txtTotalSettlements, txtTotalAmount, txtSettleNow);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Settlements(txtTitle, txtTitle1, txtTitle2, txtTitle3, txtVisaSettlement,
                    txtMasterSettlement, txtAmexSettlement, txtDinersSettlement, txtTotalSettlements, txtTotalAmount, txtSettleNow);
        } else {
            // do nothing
        }

        //TAB VIEW
        tabViewSetup();
    }

    private void tabViewSetup() {

        boolean isEnabledVisaLKR = Config.isEnableLKR(this);
        boolean isEnabledVisaUSD = Config.isEnableUSD(this);
        boolean isEnabledVisaGBP = Config.isEnableGBP(this);
        boolean isEnabledVisaEUR = Config.isEnableEUR(this);

        boolean isEnabledAmexLKR = Config.isEnableLKRAmex(this);
        boolean isEnabledAmexUSD = Config.isEnableUSDAmex(this);
        boolean isEnabledAmexGBP = Config.isEnableGBPAmex(this);
        boolean isEnabledAmexEUR = Config.isEnableEURAmex(this);

        LinearLayout lkrTabLayout = (LinearLayout) findViewById(R.id.lkrTabView);
        LinearLayout usdTabLayout = (LinearLayout) findViewById(R.id.usdTabView);
        LinearLayout gbpTabLayout = (LinearLayout) findViewById(R.id.gbpTabView);
        LinearLayout euroTabLayout = (LinearLayout) findViewById(R.id.eurTabView);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //DUAL LOGIN
            if (isEnabledVisaLKR || isEnabledAmexLKR) {
                lkrTabLayout.setVisibility(View.VISIBLE);
            } else {
                lkrTabLayout.setVisibility(View.GONE);
            }

            if (isEnabledVisaUSD || isEnabledAmexUSD) {
                usdTabLayout.setVisibility(View.VISIBLE);
            } else {
                usdTabLayout.setVisibility(View.GONE);
            }

            if (isEnabledVisaGBP || isEnabledAmexGBP) {
                gbpTabLayout.setVisibility(View.VISIBLE);
            } else {
                gbpTabLayout.setVisibility(View.GONE);
            }

            if (isEnabledVisaEUR || isEnabledAmexEUR) {
                euroTabLayout.setVisibility(View.VISIBLE);
            } else {
                euroTabLayout.setVisibility(View.GONE);
            }

            //SET DEFAULT CURRENCY TYPE
            if (isEnabledVisaLKR || isEnabledAmexLKR) {
                selectLKR();
            } else if (isEnabledVisaUSD || isEnabledAmexUSD) {
                selectUSD();
            } else if (isEnabledVisaGBP || isEnabledAmexGBP) {
                selectGBP();
            } else if (isEnabledVisaEUR || isEnabledAmexEUR) {
                selectEUR();
            }

        } else {

            // SINGLE LOGIN

            if (m_client.isLoggedIn()) {
                // VISA LOGIN

                lkrTabLayout.setVisibility(isEnabledVisaLKR ? View.VISIBLE : View.GONE);
                usdTabLayout.setVisibility(isEnabledVisaUSD ? View.VISIBLE : View.GONE);
                gbpTabLayout.setVisibility(isEnabledVisaGBP ? View.VISIBLE : View.GONE);
                euroTabLayout.setVisibility(isEnabledVisaEUR ? View.VISIBLE : View.GONE);

                //SELECT PRIMARY CURRENCY TAB
                if (isEnabledVisaLKR) {
                    selectLKR();
                } else if (isEnabledVisaUSD) {
                    selectUSD();
                } else if (isEnabledVisaGBP) {
                    selectGBP();
                } else if (isEnabledVisaEUR) {
                    selectEUR();
                }

            } else if (m_client_amex.isLoggedIn()) {
                //AMEX LOGIN

                lkrTabLayout.setVisibility(isEnabledAmexLKR ? View.VISIBLE : View.GONE);
                usdTabLayout.setVisibility(isEnabledAmexUSD ? View.VISIBLE : View.GONE);
                gbpTabLayout.setVisibility(isEnabledAmexGBP ? View.VISIBLE : View.GONE);
                euroTabLayout.setVisibility(isEnabledAmexEUR ? View.VISIBLE : View.GONE);

                //SELECT PRIMARY CURRENCY TAB
                if (isEnabledAmexLKR) {
                    selectLKR();
                } else if (isEnabledAmexUSD) {
                    selectUSD();
                } else if (isEnabledAmexGBP) {
                    selectGBP();
                } else if (isEnabledAmexEUR) {
                    selectEUR();
                }
            }
        }
    }

    private class TXAdapter extends BaseAdapter {

        private LstSettleSummary lstData;

        private int totalCount = 0;
        // private double totalAmount = 0 ;

        private BigDecimal totalAmount;

        public TXAdapter(LstSettleSummary lst) {
            super();
            lstData = lst;
            calculateTotals();
        }

        private void calculateTotals() {

            totalCount = 0;
            // totalAmount = 0 ;
            totalAmount = new BigDecimal("0");

            if (lstData == null || lstData.getSize() == 0) {
                return;
            }

            for (int i = 0; i < lstData.getSize(); i++) {
                TxSettlementSummaryEle ele = lstData.getData(i);

                if(ele != null){
                    totalCount = totalCount + ele.getCount();
                    totalAmount = totalAmount.add(ele.getAmount());
                }

            }

        }

        public int getCount() {
            if (lstData != null) {
                return lstData.getSize() + 1;
            }
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.lstrowsettlement, null);
            }

            TextView txtAmount = (TextView) v.findViewById(R.id.textViewPrice);
            TextView txtCount = (TextView) v.findViewById(R.id.txtCount);
            TextView hidden = (TextView) v.findViewById(R.id.textViewHidden);
            ImageView img = (ImageView) v.findViewById(R.id.imageView1);

            if (position == lstData.getSize()) {
                txtCount.setText("" + totalCount);
                // txtAmount.setText("" + totalAmount) ;
                txtAmount.setText(df.format(totalAmount));
                hidden.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            } else {
                TxSettlementSummaryEle ele = lstData.getData(position);
                txtCount.setText("" + ele.getCount());
                // txtAmount.setText("" + ele.getAmount()) ;
                txtAmount.setText(df.format(ele.getAmount()));
                hidden.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);

                if (ele.getType() == RecSale.CARD_VISA) {
                    img.setImageResource(R.drawable.visa);
                }

                if (ele.getType() == RecSale.CARD_MASTER) {
                    img.setImageResource(R.drawable.mastercard);
                }

            }

            return v;
        }

    }


    private void resetValues() {
        txtVisaTotalCount.setText("0");
        txtVisaAmount.setText(df.format(0));
        txtMasterTotalCount.setText("0");
        txtMasterAmount.setText(df.format(0));
        txtAmexTotalCount.setText("0");
        txtAmexAmount.setText(df.format(0));
        txtDinersTotalCount.setText("0");
        txtDinersAmount.setText(df.format(0));
        txtTotalSettlementsCount.setText("0");
        txtTotalAmountCount.setText(df.format(0));
        isSettleAvailable = false;
        isSettleAvailableAmex = false;
    }
}
