package com.cba.payable;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.EnumException;
import com.mpos.connection.PaginationHandler;
import com.mpos.pojo.APIData;
import com.mpos.pojo.BatchlistReq;
import com.mpos.pojo.BatchlistRes;
import com.mpos.pojo.LstBatchlistRes;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;
import com.setting.env.Consts;

public class BatchHistory extends GenericActivity implements PaginationHandler {

    private static final int CALL_BATCH_LIST = 1001;
    private static final int CALL_AMEX_BATCH_LIST = 5001;
    private static final int pageSize = 10;

    private BatchHistoryAdapter adapter;
    private int currentPageId = -1;

    RelativeLayout rlVisaMaster, rlAmexDiners;
    TextView txtTitle, txtVisaMaster, txtAmex;
    ImageView imageViewVisaMaster, imageViewAmex;

    View allSelector, lkrSelector, usdSelector, gbpSelector, eurSelector;
    private LinearLayout allTabLayout, lkrTabLayout, gbpTabLayout, usdTabLayout, euroTabLayout;

    ListView lsTx;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private int mCurrencyType = Consts.ALL_Currency;
    public static boolean isVMActivated = true;
    private boolean isCompletedDataLoad = false;

    protected void onInitActivity(APIData... data) {
        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            setContentView(R.layout.settlement_historylist_2);
            InitViewsSecondary();

            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);

            isVMActivated = true;
        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                setContentView(R.layout.settlement_historylist);

                InitView();

                lsTx = (ListView) this.findViewById(R.id.lstSettle);

                adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);

                lsTx.setAdapter(adapter);

                isVMActivated = true;
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                setContentView(R.layout.settlement_historylist);
                InitView();

                adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
                lsTx.setAdapter(adapter);

                isVMActivated = false;
            }
        }

        lsTx.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int position, long id) {
                _fire_detail(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //IF PORTAL CHANGED
        if (Home.isPortalChanged) {
            finish();
        }
    }

    public void selectALL(View view) {
        if (isCompletedDataLoad) {
            selectALL();
        }
    }

    public void selectLKR(View view) {
        if (isCompletedDataLoad) {
            selectLKR();
        }
    }

    public void selectUSD(View view) {
        if (isCompletedDataLoad) {
            selectUSD();
        }
    }

    public void selectGBP(View view) {
        if (isCompletedDataLoad) {
            selectGBP();
        }
    }

    public void selectEUR(View view) {
        if (isCompletedDataLoad) {
            selectEUR();
        }
    }

    private void _fire_detail(int pos) {
        if (adapter != null) {
            if (adapter.getRowType(pos) != BatchHistoryAdapter.ROW_DATA) {
                return;
            }

            BatchlistRes res = adapter.getRecord(pos);
            this.pushActivity(SettlementHistoryDetails_Activity.class, res);

        }
    }

    public void loadData(int pageId, int pageSize, int callerId) {
        currentPageId = pageId;
        BatchlistReq req = new BatchlistReq();
        req.setPageId(pageId);
        req.setPageSize(pageSize);
        req.setCurrencyType(mCurrencyType);

        if (callerId == CALL_BATCH_LIST) {
            System.out.println("trans: visa");
            m_client.fetchBatchList(CALL_BATCH_LIST, req);
        }

        if (callerId == CALL_AMEX_BATCH_LIST) {
            System.out.println("trans: Amex");
            m_client_amex.fetchBatchList(CALL_AMEX_BATCH_LIST, req);
        }
    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        isCompletedDataLoad = true;

        if (callerId == CALL_BATCH_LIST) {

            if (e.getErrcode() == EnumException.NO_RECORD_FOUND.getCode()) {
                adapter.onEmtyRecord();

                if (currentPageId == 0) {
                    showDialog("Error", e.toString());
                }

                return;
            }
            adapter.onError();
        }

        if (callerId == CALL_AMEX_BATCH_LIST) {

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

        if (callerId == CALL_BATCH_LIST) {
            if (data != null) {
                if (data instanceof LstBatchlistRes) {
                    adapter.insertRecords((LstBatchlistRes) data);
                }
            }
        }

        if (callerId == CALL_AMEX_BATCH_LIST) {
            if (data != null) {
                if (data instanceof LstBatchlistRes) {
                    adapter.insertRecords((LstBatchlistRes) data);
                }
            }
        }

        isCompletedDataLoad = true;
    }

    public void onNavBack(View v) {

        onNavBackPressed();
    }

    private void InitView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        allSelector = findViewById(R.id.view_all);
        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        lsTx = (ListView) this.findViewById(R.id.lstSettle);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_SettlementList(txtTitle);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_SettlementList(txtTitle);
        } else {
            // do nothing
        }

        tabViewControll();
    }

    private void InitViewsSecondary() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

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

        lsTx = (ListView) this.findViewById(R.id.lstSettle);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_SettlementList(txtTitle);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_SettlementList(txtTitle);
        } else {
            // do nothing
        }

        tabViewControll();
    }

    private void tabViewControll() {
        //TAB VIEW
        allTabLayout = (LinearLayout) findViewById(R.id.allTabView);
        lkrTabLayout = (LinearLayout) findViewById(R.id.lkrTabView);
        usdTabLayout = (LinearLayout) findViewById(R.id.usdTabView);
        gbpTabLayout = (LinearLayout) findViewById(R.id.gbpTabView);
        euroTabLayout = (LinearLayout) findViewById(R.id.euroTabView);

        allTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompletedDataLoad) {
                    selectALL();
                }
            }
        });

        lkrTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompletedDataLoad) {
                    selectLKR();
                }
            }
        });

        usdTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompletedDataLoad) {
                    selectUSD();
                }
            }
        });

        gbpTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompletedDataLoad) {
                    selectGBP();
                }
            }
        });

        euroTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEUR();
            }
        });

        tabCurrencyView();
    }

    private void tabCurrencyView() {
        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {

            if (isVMActivated) {
                currencyTabVisa();
            } else {
                currencyTabAmex();
            }
        } else if (m_client_amex.isLoggedIn()) {
            currencyTabAmex();
        } else if (m_client.isLoggedIn()) {
            currencyTabVisa();
        }


        /*if (isVMActivated) {
            // visa/master
            lkrTabLayout.setVisibility(Config.isEnableLKR(this) ? View.VISIBLE : View.GONE);
            usdTabLayout.setVisibility(Config.isEnableUSD(this) ? View.VISIBLE : View.GONE);
            gbpTabLayout.setVisibility(Config.isEnableGBP(this) ? View.VISIBLE : View.GONE);
            euroTabLayout.setVisibility(Config.isEnableEUR(this) ? View.VISIBLE : View.GONE);
        } else {
            //amex
            lkrTabLayout.setVisibility(Config.isEnableLKRAmex(this) ? View.VISIBLE : View.GONE);
            usdTabLayout.setVisibility(Config.isEnableUSDAmex(this) ? View.VISIBLE : View.GONE);
            gbpTabLayout.setVisibility(Config.isEnableGBPAmex(this) ? View.VISIBLE : View.GONE);
            euroTabLayout.setVisibility(Config.isEnableEURAmex(this) ? View.VISIBLE : View.GONE);
        }*/
    }

    private void currencyTabVisa() {
        // visa/master
        System.out.println("VIEWS visa");

        lkrTabLayout.setVisibility(Config.isEnableLKR(this) ? View.VISIBLE : View.GONE);
        usdTabLayout.setVisibility(Config.isEnableUSD(this) ? View.VISIBLE : View.GONE);
        gbpTabLayout.setVisibility(Config.isEnableGBP(this) ? View.VISIBLE : View.GONE);
        euroTabLayout.setVisibility(Config.isEnableEUR(this) ? View.VISIBLE : View.GONE);
    }

    private void currencyTabAmex() {
        //amex
        System.out.println("VIEWS amex");

        lkrTabLayout.setVisibility(Config.isEnableLKRAmex(this) ? View.VISIBLE : View.GONE);
        usdTabLayout.setVisibility(Config.isEnableUSDAmex(this) ? View.VISIBLE : View.GONE);
        gbpTabLayout.setVisibility(Config.isEnableGBPAmex(this) ? View.VISIBLE : View.GONE);
        euroTabLayout.setVisibility(Config.isEnableEURAmex(this) ? View.VISIBLE : View.GONE);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlVisaMaster:
                    if (isCompletedDataLoad) {
                        isVMActivated = true;
                        cardTypeChange(isVMActivated);
                        tabCurrencyView();
                    }
                    break;
                case R.id.rlAmexDiners:
                    if (isCompletedDataLoad) {
                        isVMActivated = false;
                        cardTypeChange(isVMActivated);
                        tabCurrencyView();
                    }
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

            /*adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);*/

        } else {
            // amex/diners
            rlVisaMaster.setBackgroundColor(Color.parseColor("#e5e5e5"));
            rlAmexDiners.setBackgroundColor(Color.parseColor("#ffffff"));
            imageViewVisaMaster.setImageResource(R.drawable.visa_icon_2);
            imageViewAmex.setImageResource(R.drawable.amex_icon_1);

            /*adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);*/
        }

        selectALL();
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
        isCompletedDataLoad = false;
        mCurrencyType = Consts.ALL_Currency;
        allSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // select all-visa/master
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);
        } else {
            // select all-amex/diners
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectLKR() {
        isCompletedDataLoad = false;
        mCurrencyType = Consts.LKR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // lkr-visa/master
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);
        } else {
            // lkr-amex/diners
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectUSD() {
        isCompletedDataLoad = false;
        mCurrencyType = Consts.USD;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // usd-visa/master
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);
        } else {
            // usd-amex/diners
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectGBP() {
        isCompletedDataLoad = false;
        mCurrencyType = Consts.GBP;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            // gbp-visa/master
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);
        } else {
            // gbp-amex/diners
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectEUR() {
        isCompletedDataLoad = false;
        mCurrencyType = Consts.EUR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        if (isVMActivated) {
            // eur-visa/master
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_BATCH_LIST);
            lsTx.setAdapter(adapter);
        } else {
            // eur-amex/diners
            adapter = new BatchHistoryAdapter(this, this, pageSize, CALL_AMEX_BATCH_LIST);
            lsTx.setAdapter(adapter);
        }
    }

    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVMActivated = true;
    }
}
