package com.cba.payable;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.Order;
import com.mpos.util.CHARFilter;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UserPermisions;
import com.setting.env.Config;

public class OrderTrackingActivity extends GenericActivity {

    TextView txtTitle;
    EditText edtOrder;
    Button btnContinue;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    private boolean isShowKeyEntry;
    int lang_status = 0;

    public static boolean isNewSale = true;
    public static boolean isClose = false;

    @Override
    protected void onInitActivity(APIData... data) {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_order_tracking);

        InitViews();
    }

    protected void onResume() {
        super.onResume();

        if (isClose) {
            isClose = false;
            finish();
        } else if (isNewSale) {
            onReAppear();
            isNewSale = false;
        }
    }

    protected void onReAppear() {
        edtOrder.setText("");
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        edtOrder = (EditText) findViewById(R.id.edtOrder);
        edtOrder.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnContinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // pushActivity(SalePad.class);
                _fireSalesPad();
            }
        });

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_OrderTracking(txtTitle, edtOrder, btnContinue);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_OrderTracking(txtTitle, edtOrder, btnContinue);

        } else {
            // do nothing
        }

        edtOrder.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (lang_status == 0) {

                } else if (lang_status == LangPrefs.LAN_TA) {
                    if (str == null || str.length() == 0) {

                        tamlang_select = new Tamil_LangSelect(getApplicationContext(), OrderTrackingActivity.this);
                        tamlang_select.Apply_OrderTracking_Ordertext(edtOrder);
                    } else {

                        edtOrder.setTypeface(TypeFaceUtils.setLatoRegular(OrderTrackingActivity.this));

                    }

                } else if (lang_status == LangPrefs.LAN_SIN) {
                    if (str == null || str.length() == 0) {

                        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), OrderTrackingActivity.this);
                        sinlang_select.Apply_OrderTracking_Ordertext(edtOrder);
                    } else {

                        edtOrder.setTypeface(TypeFaceUtils.setLatoRegular(OrderTrackingActivity.this));

                    }
                } else {
                    // do nothing
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void _fireSalesPad() {

        String str = edtOrder.getText().toString();

        if (str == null) {
            showDialog("Error", "Please enter tracking id");
            return;
        }

        str = str.trim();

        if (str.length() == 0) {
            showDialog("Error", "Please enter tracking id");
            return;
        }


        int res = CHARFilter.isBloackedCharExist(str);

        if (res > 0) {
            showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
            return;
        }


        Order order = new Order();
        order.setMerchantInvoiceId(str);

//		pushActivity(SalePad.class, order);

        /*long permissions = Config.getUser(this).getPermissions();
        boolean isKeyentyEnable = UserPermisions.isKeyEntryTransactionsON(permissions);

        if (isKeyentyEnable) {
            pushActivity(SalePad_Keyentry.class, order);
        } else {
            pushActivity(SalePad.class, order);
        }*/

        long visaPermissions = Config.getUser(this).getPermissions();
        long amexPermissions = Config.getAmexUser(this).getPermissions();

        boolean isKeyentyEnableVisa = UserPermisions.isKeyEntryTransactionsON(visaPermissions);
        boolean isKeyentyEnableAmex = UserPermisions.isKeyEntryTransactionsON(amexPermissions);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            if (isKeyentyEnableVisa && isKeyentyEnableAmex) {
                isShowKeyEntry = true;
            } else {
                isShowKeyEntry = false;
            }
        } else if (m_client.isLoggedIn()) {
            if (isKeyentyEnableVisa) {
                isShowKeyEntry = true;
            } else {
                isShowKeyEntry = false;
            }
        } else if (m_client_amex.isLoggedIn()) {
            if (isKeyentyEnableAmex) {
                isShowKeyEntry = true;
            } else {
                isShowKeyEntry = false;
            }
        }

        if (isShowKeyEntry) {
            pushActivity(SalePad_Keyentry.class, order);
        } else {
            pushActivity(SalePad.class, order);
        }
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            // Log.d("Activity", "Touch event " + event.getRawX() + "," +
            // event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() +
            // "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + "
            // coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

}
