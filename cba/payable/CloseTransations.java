package com.cba.payable;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mpos.connection.ApiException;
import com.mpos.connection.EnumApi;
import com.mpos.connection.EnumException;
import com.mpos.connection.PaginationHandler;
import com.mpos.pojo.APIData;
import com.mpos.pojo.LstRecSale;
import com.mpos.pojo.TXHistoryReq;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;
import com.setting.env.Consts;

public class CloseTransations extends GenericActivity implements
        PaginationHandler {

    private int currentCallerId = 100;
    private int currentCallerIdAmex = 500;
    private static final int pageSize = 10;

    private ListView lsTx;
    private TXHistoryReq request;
    private CloseTxAdapter adapter;
    private EditText txtSearch;

    private LinearLayout lkrTabLayout, gbpTabLayout, usdTabLayout, euroTabLayout, allTabLayout;
    View allSelector, lkrSelector, usdSelector, gbpSelector, eurSelector;

    ImageButton ibFilter;
    RelativeLayout rlVisaMaster, rlAmexDiners;
    TextView txtTitle, txtVisaMaster, txtAmex;
    ImageView imageViewVisaMaster, imageViewAmex;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;

    private int mCurrencyType = Consts.ALL_Currency;
    public static boolean isVMActivated = true;
    private boolean isCompletedDataLoad = false;

    protected void onInitActivity(APIData... data) {

        hideSoftKeyboard();

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            setContentView(R.layout.activity_closelist_2);
            InitViewsSecondary();

            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);

            isVMActivated = true;
        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                setContentView(R.layout.activity_closelist);

                InitViews();

                adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
                lsTx.setAdapter(adapter);

                isVMActivated = true;
            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                setContentView(R.layout.activity_closelist);
                InitViews();

                adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
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

        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String str = txtSearch.getText().toString();

                if (lang_status == 0) {

                    if (str == null || str.trim().length() == 0) {
                        _fire_search(null);

                    }

                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {

                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), CloseTransations.this);
                        tamlang_select.Apply_HistoryList(txtTitle, txtSearch);

                        _fire_search(null);

                    } else {

                        txtSearch.setTypeface(TypeFaceUtils.setRobotoMedium(CloseTransations.this));

                    }

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {

                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), CloseTransations.this);
                        sinlang_select.Apply_HistoryList(txtTitle, txtSearch);

                        _fire_search(null);
                    } else {

                        txtSearch.setTypeface(TypeFaceUtils.setRobotoMedium(CloseTransations.this));

                    }
                }
            }
        });

        txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String str = txtSearch.getText().toString();
                    if (str == null || str.trim().length() == 0) {
                        _fire_search(null);
                    }

                    if(str != null){
                        _fire_search(str.trim());
                    }
                }

                return false;
            }
        });

        hideSoftKeyboard();

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

    private void _fire_search(String txt) {
        if (request == null) {
            request = new TXHistoryReq();
        }

        request.setSerchTerm(txt);

        if (isVMActivated) {
            currentCallerId++;
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            currentCallerIdAmex++;
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);
        }
    }

    private void _fire_detail(int pos) {
        // RecSale rec = adapter.getRecord(pos) ;
        // rec.setStatus(RecSale.STATUS_CLOSE) ;
        // this.pushActivity(TxDetail.class,rec);

        if (adapter != null) {
            if (adapter.getRowType(pos) != CloseTxAdapter.ROW_DATA) {
                return;
            }

            RecSale rec = adapter.getRecord(pos);
            this.pushActivity(TxDetail.class, rec);
        }
    }

    public void onFilter(View v) {
        pushActivity(Filter.class);
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

    @Override
    public void loadData(int pageId, int pageSize, int callerId) {


        if (request == null) {
            if (callerId == currentCallerId) {

                request = new TXHistoryReq();

                if (!Filter.isFiltered) {
                    request.setVisa(1);
                    request.setMaster(1);
                }

                request.setPageId(pageId);
                request.setPageSize(pageSize);
                request.setCurrencyType(mCurrencyType);

                System.out.println("dataload 1 " + request.getJson());
                m_client.closeTxHistory(callerId, request);
            }

            if (callerId == currentCallerIdAmex) {

                request = new TXHistoryReq();

                if (!Filter.isFiltered) {
                    request.setAmex(1);
                    request.setDc(1);
                }

                request.setPageId(pageId);
                request.setPageSize(pageSize);
                request.setCurrencyType(mCurrencyType);

                System.out.println("dataload 2 " + request.getJson());
                m_client_amex.closeTxHistory(callerId, request);
            }
        } else {

            if (callerId == currentCallerId) {

                System.out.println("Filter.isFiltered " + Filter.isFiltered);
                if (!Filter.isFiltered) {
                    request.setVisa(1);
                    request.setMaster(1);
                }

                request.setPageId(pageId);
                request.setPageSize(pageSize);
                request.setCurrencyType(mCurrencyType);

                System.out.println("dataload 3 " + request.getJson());
                m_client.closeTxHistory(callerId, request);
            }

            if (callerId == currentCallerIdAmex) {

                if (!Filter.isFiltered) {
                    request.setAmex(1);
                    request.setDc(1);
                }

                request.setPageId(pageId);
                request.setPageSize(pageSize);
                request.setCurrencyType(mCurrencyType);

                System.out.println("dataload 4 " + request.getJson());
                m_client_amex.closeTxHistory(callerId, request);
            }
        }
    }


    protected void onResultFromChild(APIData... val) {
        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (val[i] instanceof TXHistoryReq) {
                    request = (TXHistoryReq) val[i];
                    currentCallerId++;
                    adapter = new CloseTxAdapter(this, this, pageSize,
                            currentCallerId);
                    lsTx.setAdapter(adapter);
                    txtSearch.setText("");
                    return;
                }
            }
        }
    }

	/*
     * public void onMSGDlgBtnClick(int callerId){ if(callerId == 210){
	 * PLog.i(TAG, "inside the error dialog call back.") ; fireOpenTxList() ; }
	 * }
	 */

    public void onCallError(EnumApi api, int callerId, ApiException e) {
        printOnCallErrorLog(api, e);

        isCompletedDataLoad = true;

        if (e.getEnumException() == EnumException.NO_RECORD_FOUND) {
            if (callerId == currentCallerId) {
                adapter.onEmtyRecord();
            }

            if (callerId == currentCallerIdAmex) {
                adapter.onEmtyRecord();
            }
        }
        adapter.onError();
        super.onCallError(api, callerId, e);
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        if (callerId == currentCallerId) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }

        if (callerId == currentCallerIdAmex) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }

        isCompletedDataLoad = true;
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        allSelector = findViewById(R.id.view_all);
        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        lsTx = (ListView) findViewById(R.id.lstCloseTx);

        txtSearch = (EditText) findViewById(R.id.edtSearch);
        txtSearch.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        ibFilter = (ImageButton) findViewById(R.id.ibFilter);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_HistoryList(txtTitle, txtSearch);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_HistoryList(txtTitle, txtSearch);
        } else {

        }

        tabViewController();
    }

    public void InitViewsSecondary() {
        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        allSelector = findViewById(R.id.view_all);
        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

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

        lsTx = (ListView) findViewById(R.id.lstCloseTx);

        txtSearch = (EditText) findViewById(R.id.edtSearch);
        txtSearch.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        ibFilter = (ImageButton) findViewById(R.id.ibFilter);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_HistoryList(txtTitle, txtSearch);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_HistoryList(txtTitle, txtSearch);
        } else {

        }

        tabViewController();
    }

    private void tabViewController() {
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
        lkrTabLayout.setVisibility(Config.isEnableLKR(this) ? View.VISIBLE : View.GONE);
        usdTabLayout.setVisibility(Config.isEnableUSD(this) ? View.VISIBLE : View.GONE);
        gbpTabLayout.setVisibility(Config.isEnableGBP(this) ? View.VISIBLE : View.GONE);
        euroTabLayout.setVisibility(Config.isEnableEUR(this) ? View.VISIBLE : View.GONE);
    }

    private void currencyTabAmex() {
        //amex
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
                    txtSearch.setText("");
                    if (isCompletedDataLoad) {
                        isVMActivated = true;
                        request = null;
                        Filter.isFiltered = false;
                        cardTypeChange(isVMActivated);
                        tabCurrencyView();
                    }
                    break;
                case R.id.rlAmexDiners:
                    txtSearch.setText("");
                    if (isCompletedDataLoad) {
                        isVMActivated = false;
                        request = null;
                        Filter.isFiltered = false;
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

            /*adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);*/

        } else {
            // amex/diners
            rlVisaMaster.setBackgroundColor(Color.parseColor("#e5e5e5"));
            rlAmexDiners.setBackgroundColor(Color.parseColor("#ffffff"));
            imageViewVisaMaster.setImageResource(R.drawable.visa_icon_2);
            imageViewAmex.setImageResource(R.drawable.amex_icon_1);

            /*adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);*/
        }

        selectALL();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void selectALL() {
        isCompletedDataLoad = false;
        //Filter.isFiltered = false;
        mCurrencyType = Consts.ALL_Currency;
        allSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectLKR() {
        isCompletedDataLoad = false;
        //Filter.isFiltered = false;
        mCurrencyType = Consts.LKR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectUSD() {
        isCompletedDataLoad = false;
        //Filter.isFiltered = false;
        mCurrencyType = Consts.USD;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectGBP() {
        isCompletedDataLoad = false;
        // Filter.isFiltered = false;
        mCurrencyType = Consts.GBP;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        eurSelector.setBackgroundColor(0);

        if (isVMActivated) {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
            lsTx.setAdapter(adapter);
        }
    }

    private void selectEUR() {
        isCompletedDataLoad = false;
        //Filter.isFiltered = false;
        mCurrencyType = Consts.EUR;
        allSelector.setBackgroundColor(0);
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));

        if (isVMActivated) {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerId);
            lsTx.setAdapter(adapter);
        } else {
            adapter = new CloseTxAdapter(this, this, pageSize, currentCallerIdAmex);
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
        Filter.isFiltered = false;
        isVMActivated = true;
    }
}
