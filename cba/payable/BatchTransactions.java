package com.cba.payable;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.EnumException;
import com.mpos.connection.PaginationHandler;
import com.mpos.pojo.APIData;
import com.mpos.pojo.BatchTxHistoryReq;
import com.mpos.pojo.BatchlistRes;
import com.mpos.pojo.LstRecSale;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Consts;

public class BatchTransactions extends GenericActivity implements
        PaginationHandler {

    private static final int CALL_TX_HIS = 500;
    private static final int CALL_TX_HIS_AMEX = 600;
    private static final int pageSize = 10;

    TextView txtTitle;

    View allSelector, lkrSelector, usdSelector, gbpSelector, eurSelector;
    RelativeLayout rlVisaMaster, rlAmexDiners;
    TextView txtVisaMaster, txtAmex;
    ImageView imageViewVisaMaster, imageViewAmex;

    ListView lsTx;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private CloseTxAdapter adapter;
    private int currentPageId = -1;

    private BatchlistRes m_BatchlistRes;

    boolean isVMActivated = true;
    private int mCurrencyType = Consts.ALL_Currency;

    protected void onInitActivity(APIData... data) {

        System.out.println("Bach Trans");

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof BatchlistRes) {
                    m_BatchlistRes = (BatchlistRes) data[i];
                }
            }
        }

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            setContentView(R.layout.activity_open_transactions);
            InitViewsSecondary();

            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);

            isVMActivated = true;
        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                setContentView(R.layout.activity_void);
                InitViews();

                adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
                lsTx.setAdapter(adapter);

                isVMActivated = true;
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                setContentView(R.layout.activity_void);
                InitViews();

                adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
                lsTx.setAdapter(adapter);

                isVMActivated = false;
            }
        }

        lsTx.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int position,
                                    long id) {
                _fire_detail(position);
            }
        });
    }


    private void _setTitle() {
        if (lang_status == LangPrefs.LAN_EN) {
            txtTitle.setText("Transactions");
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_TransactionListTitle(txtTitle);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_TransactionListTitle(txtTitle);
        } else {
            txtTitle.setText("Transactions");
        }
    }

    private void _fire_detail(int pos) {

        if (adapter != null) {

            if (adapter.getRowType(pos) != CloseTxAdapter.ROW_DATA) {
                return;
            }

            RecSale rec = adapter.getRecord(pos);
            this.pushActivity(TxDetail.class, rec);
        }
    }

    @Override
    public void loadData(int pageId, int pageSize, int callerId) {
        currentPageId = pageId;

        if (m_BatchlistRes != null) {
            BatchTxHistoryReq req = new BatchTxHistoryReq();
            req.setSettleId(m_BatchlistRes.getSettleId());
            req.setPageId(pageId);
            req.setPageSize(pageSize);

            if (callerId == CALL_TX_HIS) {
                m_client.fetchBatchTxHistory(CALL_TX_HIS, req);
            } else if (callerId == CALL_TX_HIS_AMEX) {
                m_client_amex.fetchBatchTxHistory(CALL_TX_HIS_AMEX, req);
            }
        }

    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        if (callerId == CALL_TX_HIS) {

            if (e.getErrcode() == EnumException.NO_RECORD_FOUND.getCode()) {
                adapter.onEmtyRecord();

                if (currentPageId == 0) {
                    showDialog("Error", e.toString());
                }

                return;
            }
            adapter.onError();
        }

        if (callerId == CALL_TX_HIS_AMEX) {
            if (e.getErrcode() == EnumException.NO_RECORD_FOUND.getCode()) {
                adapter.onEmtyRecord();

                if (currentPageId == 0) {
                    showDialog("Error", e.toString());
                }

                return;
            }
            adapter.onError();
        }

        super.onCallError(api, callerId, e);
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        if (callerId == CALL_TX_HIS) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }

        if (callerId == CALL_TX_HIS_AMEX) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }
    }

    public void onNavBack(View v) {

        onNavBackPressed();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlVisaMaster:
                    isVMActivated = true;
                    cardTypeChange(isVMActivated);
                    break;
                case R.id.rlAmexDiners:
                    isVMActivated = false;
                    cardTypeChange(isVMActivated);
                    break;
            }
        }
    };

    private void cardTypeChange(boolean isVMActivated) {

        if (isVMActivated) {
            // visa/master
            rlVisaMaster.setBackgroundColor(Color.parseColor("#ffffff"));
            rlAmexDiners.setBackgroundColor(Color.parseColor("#e5e5e5"));
            imageViewVisaMaster.setImageResource(R.drawable.visa_icon_1);
            imageViewAmex.setImageResource(R.drawable.amex_icon_2);

            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);

        } else {
            // amex/diners
            rlVisaMaster.setBackgroundColor(Color.parseColor("#e5e5e5"));
            rlAmexDiners.setBackgroundColor(Color.parseColor("#ffffff"));
            imageViewVisaMaster.setImageResource(R.drawable.visa_icon_2);
            imageViewAmex.setImageResource(R.drawable.amex_icon_1);

            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    public void onCurrencySelect(View view) {

        int id = view.getId();

        if (id == R.id.image_all || id == R.id.textview_all) {
            selectALL();
        }
        if (id == R.id.image_lkr || id == R.id.textview_lkr) {
            selectLKR();
        }
        if (id == R.id.image_usd || id == R.id.textview_usd) {
            selectUSD();
        }
        if (id == R.id.image_gbp || id == R.id.textview_gbp) {
            selectGBP();
        }
        if (id == R.id.image_eur || id == R.id.textview_eur) {
            selectEUR();
        }

    }

    private void selectALL() {
        mCurrencyType = Consts.ALL_Currency;
        allSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // select all-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);
        } else {
            // select all-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectLKR() {
        mCurrencyType = Consts.LKR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // lkr-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);
        } else {
            // lkr-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }

    }

    private void selectUSD() {
        mCurrencyType = Consts.USD;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // usd-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);
        } else {
            // usd-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectGBP() {
        mCurrencyType = Consts.GBP;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // gbp-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);
        } else {
            // gbp-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectEUR() {
        mCurrencyType = Consts.EUR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        if (isVMActivated) {
            // eur-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
            lsTx.setAdapter(adapter);
        } else {
            // eur-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    private void InitViews() {


        lsTx = (ListView) this.findViewById(R.id.lvVoid);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils
                .setRobotoRegular(getApplicationContext()));

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_VoidListTitle(txtTitle);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_VoidListTitle(txtTitle);
        } else {

        }

        _setTitle();

    }

    private void InitViewsSecondary() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setLatoLight(getApplicationContext()));

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        allSelector = findViewById(R.id.view_all);
        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        rlVisaMaster = (RelativeLayout) findViewById(R.id.rlVisaMaster);
        rlVisaMaster.setOnClickListener(onClickListener);

        rlAmexDiners = (RelativeLayout) findViewById(R.id.rlAmexDiners);
        rlAmexDiners.setOnClickListener(onClickListener);

        txtVisaMaster = (TextView) findViewById(R.id.txtVisaMaster);
        txtVisaMaster.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtAmex = (TextView) findViewById(R.id.txtAmex);
        txtAmex.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        imageViewVisaMaster = (ImageView) findViewById(R.id.imageViewVisaMaster);
        imageViewAmex = (ImageView) findViewById(R.id.imageViewAmex);

        lsTx = (ListView) this.findViewById(R.id.lvVoid);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_VoidListTitle(txtTitle);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_VoidListTitle(txtTitle);
        } else {

        }

        _setTitle();
    }

}
