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
import com.mpos.pojo.LstRecSale;
import com.mpos.pojo.OpenTxHistoryReq;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

import java.util.Date;

public class OpenTransations extends GenericActivity implements PaginationHandler {

    private static final int CALL_OPEN_TX = 500;
    private static final int CALL_OPEN_TX_AMEX = 100;
    private static final int pageSize = 10;
    private static final String LOG_TAG = "payable " + OpenTransations.class.getSimpleName();
    public static boolean isVOidTransaction;

    TextView txtTitle;
    ListView lsTx;
    View allSelector, lkrSelector, usdSelector, gbpSelector, eurSelector;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;
    int page_status = 1;

    // private DecimalFormat df ;
    // private SimpleDateFormat sdf ;

    private CloseTxAdapter adapter;

    private int currentPageId = -1;
    private int mCurrencyType = Consts.ALL_Currency;

    private ProfileInfo mProfileInfo;

    RelativeLayout rlVisaMaster, rlAmexDiners;
    TextView txtVisaMaster, txtAmex;
    ImageView imageViewVisaMaster, imageViewAmex;

    public static boolean isVMActivated = true;
    public static boolean wentToDetails = false;
    private View tabView;
    private LinearLayout allTabLayout, lkrTabLayout, gbpTabLayout, usdTabLayout, euroTabLayout;

    public static boolean isUpdatedSignature;
    public static boolean isVoidTransaction;
    public static Date voidServerTime;
    private RecSale rec = null;
    private boolean isCompletedDataLoad = false;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_opentx);



        // TXAdapter adapterold = new TXAdapter();
        // lsTx.setAdapter(adapterold);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            //dual login
            setContentView(R.layout.activity_open_transactions);
            InitViewsSecondary();

            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);

            if(!wentToDetails){
                isVMActivated = true;
            }

        } else {
            if (m_client.isLoggedIn()) {
                //single login in visa/master
                setContentView(R.layout.activity_void);

                InitViews();

                //page_status = 2 ;
                setPageStatus();

                lsTx = (ListView) this.findViewById(R.id.lvVoid);
                // df = new DecimalFormat("####0.00") ;
                // sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm a",Locale.US);

                adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
                lsTx.setAdapter(adapter);

                if(!wentToDetails){
                    isVMActivated = true;
                }else {
                    selectALL();
                }

            } else if (m_client_amex.isLoggedIn()) {
                //single login in amex
                setContentView(R.layout.activity_void);
                InitViews();

                adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
                lsTx.setAdapter(adapter);

                //isVMActivated = false;
                if(!wentToDetails){
                    isVMActivated = false;
                }else {
                    selectALL();
                }
            }
        }

        setPageStatus();

        lsTx.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int position, long id) {
                _fire_detail(position);
            }
        });

        if (isVOidTransaction) {
            isVOidTransaction = false;
            rec.setStatus(RecSale.STATUS_CLOSE_VOID);
            rec.setVoidTs(voidServerTime);
            adapter.notifyDataSetChanged();
        }

        //IF PORTAL CHANGED
        if (!Home.isPortalChanged) {
            if (isVoidTransaction) {
                isVoidTransaction = false;
                rec.setStatus(RecSale.STATUS_CLOSE_VOID);
                rec.setVoidTs(voidServerTime);
                adapter.notifyDataSetChanged();
            }

            if (isUpdatedSignature) {
                isUpdatedSignature = false;
                rec.setSigFlag(RecSale.SIGNATURE_AVAILABLE);
                adapter.notifyDataSetChanged();
            }
        } else {
            finish();
        }

        //retains position when comes back from TxDetails - when logged in from seylan and amex
        if(m_client.isLoggedIn() && m_client_amex.isLoggedIn() && wentToDetails){
            cardTypeChange(isVMActivated);
            tabCurrencyView();
        }

        wentToDetails = false;
    }


    private void _fire_detail(int pos) {
        // RecSale rec = m_localDb.getSalesRowData(pos) ;
        // this.pushActivity(TxDetail.class,rec);

        if (adapter != null) {

            if (adapter.getRowType(pos) != CloseTxAdapter.ROW_DATA) {
                return;
            }

            wentToDetails = true;
            rec = (RecSale) adapter.getRecord(pos);
            TxDetail.IS_FROM_OPEN_TX = true;
            this.pushActivity(TxDetail.class, rec);
        }
    }

    public void loadData(int pageId, int pageSize, int callerId) {
        currentPageId = pageId;
        OpenTxHistoryReq req = new OpenTxHistoryReq();
        req.setPageId(pageId);
        req.setPageSize(pageSize);
        req.setCurrencyType(mCurrencyType);

        if (callerId == CALL_OPEN_TX) {
            m_client.openTxHistory(CALL_OPEN_TX, req);
        }

        if (callerId == CALL_OPEN_TX_AMEX) {
            m_client_amex.openTxHistory(CALL_OPEN_TX_AMEX, req);
        }

    }

    public void onCallError(EnumApi api, int callerId, ApiException e) {

        isCompletedDataLoad = true;

        if (callerId == CALL_OPEN_TX) {

            if (e.getErrcode() == EnumException.NO_RECORD_FOUND.getCode()) {
                adapter.onEmtyRecord();

                if (currentPageId == 0) {
                    showDialog("Error", e.toString());
                }

                return;
            }
            adapter.onError();
        }

        if (callerId == CALL_OPEN_TX_AMEX) {

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
        if (callerId == CALL_OPEN_TX) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }

        if (callerId == CALL_OPEN_TX_AMEX) {
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

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        allSelector = findViewById(R.id.view_all);
        lkrSelector = findViewById(R.id.view_lkr);
        usdSelector = findViewById(R.id.view_usd);
        gbpSelector = findViewById(R.id.view_gbp);
        eurSelector = findViewById(R.id.view_eur);

        lsTx = (ListView) this.findViewById(R.id.lvVoid);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_VoidListTitle(txtTitle);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_VoidListTitle(txtTitle);
        } else {

        }

        //TAB VIEW
        tabView = findViewById(R.id.tab_view);
        tabControll();
    }

    private void InitViewsSecondary() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

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

        tabView = findViewById(R.id.tab_view);
        tabControll();
    }

    private void tabControll() {

        int versionId = -1;

        if (m_client.isLoggedIn()) {
            versionId = Config.getVersionId(this);
        } else if (m_client_amex.isLoggedIn()) {
            versionId = Config.getVersionIdAmex(this);
        }

        if (versionId == -1) {
            tabView.setVisibility(View.GONE);
        } else {
            tabView.setVisibility(View.VISIBLE);

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
                    if (isCompletedDataLoad) {
                        selectEUR();
                    }
                }
            });

            tabCurrencyView();
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


    private void setPageStatus() {
        if (page_status == 1) {
            // Open Transaction
            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidListTitle(txtTitle);
            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidListTitle(txtTitle);
            } else {

            }
        }
        if (page_status == 2) {
            // Transaction
            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.Apply_TransactionListTitle(txtTitle);
            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_TransactionListTitle(txtTitle);
            } else {

            }
        }
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

            /*adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);*/

        } else {
            // amex/diners
            rlVisaMaster.setBackgroundColor(Color.parseColor("#e5e5e5"));
            rlAmexDiners.setBackgroundColor(Color.parseColor("#ffffff"));
            imageViewVisaMaster.setImageResource(R.drawable.visa_icon_2);
            imageViewAmex.setImageResource(R.drawable.amex_icon_1);

            /*adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
            lsTx.setAdapter(adapter);*/
        }

        selectALL();
    }

    public void currencyLkr() {

    }

    /*public void onCurrencySelect(View view) {

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

    }*/

    private void selectALL() {
        isCompletedDataLoad = false;
        mCurrencyType = Consts.ALL_Currency;
        allSelector.setBackgroundColor(ContextCompat.getColor(this, R.color.app_light_theme));
        lkrSelector.setBackgroundColor(0);
        usdSelector.setBackgroundColor(0);
        gbpSelector.setBackgroundColor(0);
        eurSelector.setBackgroundColor(0);

        lkrSelector.setEnabled(false);
        usdSelector.setEnabled(false);
        gbpSelector.setEnabled(false);
        eurSelector.setEnabled(false);

        if (isVMActivated) {
            // select all-visa/master
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);
        } else {
            // select all-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
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
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);
        } else {
            // lkr-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
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
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);
        } else {
            // usd-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
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
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);
        } else {
            // gbp-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
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
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX);
            lsTx.setAdapter(adapter);
        } else {
            // eur-amex/diners
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_OPEN_TX_AMEX);
            lsTx.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TxDetail.IS_SIGN_BTN_VISIBLE = false;
        isVMActivated = true;
    }

    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
            finish();
        }
    }
}
