package com.cba.payable;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.mpos.util.OrderTrackPref;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

public class ViewTransactionsActivity extends GenericActivity implements
        PaginationHandler {

    private static final String LOG_TAG = "payable TxDetail";

    private static final int CALL_TX_HIS = 500;
    private static final int CALL_TX_HIS_AMEX = 600;
    private static final int pageSize = 10;

    TextView txtTitle;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private CloseTxAdapter adapter;
    private int currentPageId = -1;

    private BatchlistRes m_BatchlistRes;

    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_view_transactions);

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof BatchlistRes) {
                    m_BatchlistRes = (BatchlistRes) data[i];
                }
            }
        }


        InitViews();
        ListView lsTx = (ListView) this.findViewById(R.id.lvVoid);

        _setTitle();

        if (BatchHistory.isVMActivated) {
            //VISA SELECTED
            System.out.println("CloseTxAdapter visa");
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS);
        } else {
            // AMEX SELECTED
            System.out.println("CloseTxAdapter amex");
            adapter = new CloseTxAdapter(this, this, pageSize, CALL_TX_HIS_AMEX);
        }

        lsTx.setAdapter(adapter);

        lsTx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View view, int position,
                                    long id) {
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
            Log.d(LOG_TAG, "txType : " + rec.getTxType());
            if(rec != null){
                this.pushActivity(TxDetail.class, rec);
            }else{
                this.pushActivity(TxDetail.class, null);
            }

        }
    }

    /*@Override
    public void loadData(int pageId, int pageSize, int callerId) {
        currentPageId = pageId;

        if (m_BatchlistRes != null) {
            BatchTxHistoryReq req = new BatchTxHistoryReq();
            req.setSettleId(m_BatchlistRes.getSettleId());
            req.setPageId(pageId);
            req.setPageSize(pageSize);
            m_client.fetchBatchTxHistory(CALL_TX_HIS, req);
        }

    }*/

    @Override
    public void loadData(int pageId, int pageSize, int callerId) {
        currentPageId = pageId;

        if (m_BatchlistRes != null) {
            BatchTxHistoryReq req = new BatchTxHistoryReq();
            req.setSettleId(m_BatchlistRes.getSettleId());
            req.setPageId(pageId);
            req.setPageSize(pageSize);

            if (callerId == CALL_TX_HIS) {
                System.out.println("fetchBatchTxHistory visa");
                m_client.fetchBatchTxHistory(CALL_TX_HIS, req);
            }

            if (callerId == CALL_TX_HIS_AMEX) {
                System.out.println("fetchBatchTxHistory amex");
                m_client_amex.fetchBatchTxHistory(CALL_TX_HIS_AMEX, req);
            }
        }

    }

    /*public void onCallError(EnumApi api, int callerId, ApiException e) {

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

        super.onCallError(api, callerId, e);
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {
        System.out.println("VIEW_TNS_DATA " + data.getJson());

        if (callerId == CALL_TX_HIS) {
            if (data != null) {
                if (data instanceof LstRecSale) {
                    adapter.insertRecords((LstRecSale) data);
                }
            }
        }
    }*/

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

    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            logoutAllNow();
            logOut();
        }
    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        System.out.println("VIEW_TNS_DATA " + data.getJson());

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

    private void InitViews() {

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

    }

}
