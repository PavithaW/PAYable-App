package com.cba.payable;

import android.app.Dialog;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.Order;
import com.mpos.pojo.SimpleReq;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;
import com.setting.env.Config;

import java.text.DecimalFormat;

public class SelectPaymentMethod extends DSBaseActivityNew {

    private static final String LOG_TAG = "payable " + SelectPaymentMethod.class.getSimpleName();

    private static final int ECHO_VALUE = 2222;
    private static final int CALL_NETWORK_CHECK = 100;
    private static final int CALL_NETWORK_CHECK_AMEX = 300;
    private static final int CALL_NETWORK_CHECK_KEY_ENTRY = 200;
    private static final int CALL_NETWORK_CHECK_KEY_ENTRY_AMEX = 400;

    public static final int SIGNAL_CLOSE = 111111;

    private TextView tvTitle;
    private TextView tvAmmouValue;
    private TextView tvAmmou;
    private TextView tvReadCard;
    private TextView tvEnterCard;
    private TextView txtReaderStatus;

    private AnimatedGifImageView animatedGifImageView;
    private ImageView imgBattery;

    private LinearLayout readCard;
    private LinearLayout enterCard;

    private Amount m_amount;
    private Dialog battery_dlg;

    private double valDisplay;

    private int battery_percentage = 0;
    public static int signal_val = 1;

    private boolean isReaderVerified = false;
    private boolean isNextScrClicked = false;
    public static boolean isSaleTimeout = false;
    private static boolean isReconnecting = false;
    public static boolean isNewSale = true;

    // variable to track event time
    private long mLastClickTime = 0;

    private Order m_order = null;

    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;
    int lang_status = 0;
    public static boolean isClosed;
    Dialog void_dialog;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.select_payment_method);
        unpairBluetoothDevices();
        Log.d(LOG_TAG, "onInitActivity : ");

        isReaderVerified = false;
        isReconnecting = false;
        navaigateReadingScr = false;

        InitViews();

        DecimalFormat df = new DecimalFormat("0.00");

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Amount) {
                    m_amount = (Amount) data[i];

                    //FORMAT NUMBER ACCORDING TO UNICODE
                    String amount = "";
                    String currencyCode = "";
                    if (lang_status == LangPrefs.LAN_EN) {
                        amount = df.format(m_amount.getValue());
                        currencyCode = UtilityFunction.getCurrencyTypeString(m_amount.getCurrencyType());
                    } else if (lang_status == LangPrefs.LAN_SIN) {
                        amount = df.format(m_amount.getValue()).replace(".", "'");
                        currencyCode = UtilityFunction.getCurrencyTypeStringSI(m_amount.getCurrencyType());
                    } else if (lang_status == LangPrefs.LAN_TA) {
                        amount = df.format(m_amount.getValue());
                        currencyCode = UtilityFunction.getCurrencyTypeStringTA(m_amount.getCurrencyType());
                    }

                    tvAmmouValue.setText(amount + " " + currencyCode);
                }
                if (data[i] instanceof Order) {
                    m_order = (Order) data[i];
                }
            }
        }
    }


    private void InitViews() {

        readCard = (LinearLayout) findViewById(R.id.readcard_ll);
        enterCard = (LinearLayout) findViewById(R.id.entercard_ll);

        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.gifView));
        animatedGifImageView.setAnimatedGif(R.raw.bluetooth_five, AnimatedGifImageView.TYPE.AS_IS);
        animatedGifImageView.setVisibility(View.GONE);

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        imgBattery = (ImageView) findViewById(R.id.imgBattery);

        tvTitle = (TextView) findViewById(R.id.txtTitle);
        tvTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

        tvAmmouValue = (TextView) findViewById(R.id.txtAmountValue);

        tvAmmou = (TextView) findViewById(R.id.txtAmount);
        tvAmmou.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        tvReadCard = (TextView) findViewById(R.id.tvReadCard);
        tvReadCard.setTypeface(TypeFaceUtils.setRobotoLight(this));

        tvEnterCard = (TextView) findViewById(R.id.tvEnterCard);
        tvEnterCard.setTypeface(TypeFaceUtils.setRobotoLight(this));

        txtReaderStatus = (TextView) findViewById(R.id.txtReaderStatus);

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.selectPaymentMethod(tvTitle, tvAmmou, tvReadCard, tvEnterCard, tvAmmouValue);
        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.selectPaymentMethod(tvTitle, tvAmmou, tvReadCard, tvEnterCard, tvAmmouValue);
        } else {
            // do nothing
        }


        //hide enterCard if logged with AMEX
        /*if (m_client_amex.isLoggedIn()) {
            enterCard.setVisibility(View.GONE);
        }
*/
    }

    public void onReadCard(View v) {
        Log.d(LOG_TAG, "onReadCard : ");

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        enterCard.setClickable(false);
        isNextScrClicked = true;
        _showConnectingGifView();
//		animatedGifImageView.setAnimatedGif(R.raw.bluetooth_five, AnimatedGifImageView.TYPE.AS_IS);
        cardReaderSetup();

//		_fireNextSecreenProcess() ;
    }


    public void onEnterCard(View v) {
//		startActivity(new Intent(this, KeyEntryActivity.class));

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        SimpleReq req = new SimpleReq();
        req.setValue(ECHO_VALUE);

//        m_client.echo(CALL_NETWORK_CHECK_KEY_ENTRY,
//                ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);


        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            m_client.echo(CALL_NETWORK_CHECK_KEY_ENTRY,
                    ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
        } else {
            if (m_client.isLoggedIn()) {
                m_client.echo(CALL_NETWORK_CHECK_KEY_ENTRY,
                        ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
            } else if (m_client_amex.isLoggedIn()) {
                m_client_amex.echo(CALL_NETWORK_CHECK_KEY_ENTRY_AMEX,
                        ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
            }
        }

    }

    private void openKeyEntry() {
        if (m_order != null) {
            this.pushActivity(KeyEntryActivity.class, m_amount, m_order);
        } else {
            this.pushActivity(KeyEntryActivity.class, m_amount);
        }
    }

    @Override
    protected void statusCallBack(int status, String message) {
        Log.d(LOG_TAG, "statusCallBack : status = " + status + "message = " + message);

        enterCard.setClickable(true);

        if (status == REQUEST_ENABLE_BT) {
            setReaderMessage("Reader not Connected.", 2);

        }

        if (status == BT_CONNECTING) {
            if (!isReconnecting) {
                setReaderMessage("Trying to connect with card reader...", 0);
                enterCard.setClickable(false);
            }

            isReaderVerified = false;
            return;
        }

        if (status == QPOS_NOT_DECTECTED) {
            setReaderMessage("Please turn on the card reader.", 2);
            isReaderVerified = false;
            isReconnecting = true;
            reconnect();
            return;
        }

        if (status == QPOS_CONNECTED) {
            setReaderMessage("Reader connected. Verifying ...", 0);
            getQposInfo();
            return;
        }

        if (status == BT_DIS_CONNECTED) {
            isReaderVerified = false;
            isReconnecting = true;
            setReaderMessage("Reader disconnected. Please check the reader.", 2);
            _showConnectingGifView();
            reconnect();
            return;
        }

    }

    private void _showConnectingGifView() {

        Log.d(LOG_TAG, "_showConnectingGifView : ");

        imgBattery.setVisibility(View.GONE);
        animatedGifImageView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void batteryStatusCallBack(String batteryLevel) {
        Log.d(LOG_TAG, "batteryStatusCallBack : batteryLevel = " + batteryLevel);

        setReaderMessage("Reader connected.", 1);

        int lvl = 0;

        if (batteryLevel == null || batteryLevel.trim().length() == 0) {
            lvl = 0;
        } else {
            try {
                lvl = Integer.parseInt(batteryLevel.substring(0, batteryLevel.length() - 1));
            } catch (NumberFormatException e) {
                lvl = 0;
            }
        }

        if (lvl < 50) {
            lvl = lvl - 2;
        }

        if (lvl < 0) {
            lvl = 0;
        }

        battery_percentage = lvl;
        isReaderVerified = true;

        showBatteryLevels(lvl);

        if (isNextScrClicked) {
            _fireNextSecreenProcess();
        }

    }

    private void _fireNextSecreenProcess() {

        Log.d(LOG_TAG, "_fireNextSecreenProcess : ");

        if (!isReaderVerified) {
            return;
        }

        if (isReaderVerified) {

            if (battery_percentage <= 2) {
                showDialog("Error", "Your battery percentage is below 2 percent. To continue to do transactions please plug in charger.");
                return;
            }

        }

        SimpleReq req = new SimpleReq();
        req.setValue(ECHO_VALUE);

//        m_client.echo(CALL_NETWORK_CHECK,
//                ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            m_client.echo(CALL_NETWORK_CHECK,
                    ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
        } else {
            if (m_client.isLoggedIn()) {
                m_client.echo(CALL_NETWORK_CHECK,
                        ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
            } else if (m_client_amex.isLoggedIn()) {
                m_client_amex.echo(CALL_NETWORK_CHECK_AMEX,
                        ProgressDialogMessagesUtil.salePadTranslation(lang_status), req);
            }
        }

        //_launchCardScreen();

    }

    private void showBatteryLevels(int battery_level) {

        Log.d(LOG_TAG, "showBatteryLevels : " + battery_level);

        animatedGifImageView.setVisibility(View.GONE);

        if (battery_level == 0) {
            imgBattery.setImageResource(R.drawable.battery_zero);

        } else if (0 < battery_level && battery_level <= 25) {
            imgBattery.setImageResource(R.drawable.battery_low);

        } else if (25 < battery_level && battery_level <= 50) {
            imgBattery.setImageResource(R.drawable.battery_medium);

        } else if (50 < battery_level && battery_level <= 75) {
            imgBattery.setImageResource(R.drawable.battery_good);

        } else if (75 < battery_level && battery_level <= 100) {
            imgBattery.setImageResource(R.drawable.battery_full);

        } else {
            imgBattery.setImageResource(R.drawable.no_result_battery);

        }

        imgBattery.setVisibility(View.VISIBLE);

    }

    private void setReaderMessage(String msg, int color) {

        Log.d(LOG_TAG, "setReaderMessage : msg = " + msg + ", color = " + color);

        txtReaderStatus.setText(msg);

        if (color == 0) // Loading or Connecting - blue color
        {
            txtReaderStatus.setTextColor(Color.parseColor("#4786C6"));
        } else if (color == 1) // Successfully Connected - Green color
        {
            txtReaderStatus.setTextColor(Color.GREEN);
        } else if (color == 2) // Disconnected- Red
        {
            txtReaderStatus.setTextColor(Color.RED);
        } else {
            txtReaderStatus.setTextColor(Color.parseColor("#4786C6"));
        }
    }


    protected void onResume() {


        Log.d(LOG_TAG, "onResume : ");

        if (signal_val == SIGNAL_CLOSE) {
            signal_val = -1;
            super.onResume();
            finish();
            return;
        }

        if (isNewSale) {
            onReAppear();
            isNewSale = false;
        } else if (isSaleTimeout == true) {
            isSaleTimeout = false;
            fireOpenTxList();
        }

        if (navaigateReadingScr) {
            navaigateReadingScr = false;
            isNextScrClicked = false;
            isReconnecting = false;
            //reconnect() ;
            refereshConnection();
        }

        super.onResume();
    }

    private void _launchCardScreen() {

        Log.d(LOG_TAG, "_launchCardScreen : ");

        if (!isReaderVerified) {
            isNextScrClicked = false;
            showDialog("Error", "Reader disconneccted. please check the reader.");
            return;
        }

        // Amount amount = new Amount(valDisplay / 100);

        int readerId = Config.getActiveReader(getApplicationContext());

        if (readerId == Config.READER_DEVICE1) {
            // this.pushActivity(SalesPadCR.class, amount);

        }

        if (readerId == Config.READER_DS_AUDIO || readerId == Config.READER_DS_BT) {
            // this.pushActivity(DsSalesAudio.class, amount);
        }

        navaigateReadingScr = true;

        disConnectPos();

        if (m_order != null) {
            this.pushActivity(DsSalesAudio.class, m_amount, m_order);
        } else {
            this.pushActivity(DsSalesAudio.class, m_amount);
        }

        //finish();

    }

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        Log.d(LOG_TAG, "onCallSuccess : EnumApi = " + api.toString() + "callerId = " + callerId
                + "APIData = " + data.getJson());

        if (callerId == CALL_NETWORK_CHECK) {
            _launchCardScreen();
        }

        if (callerId == CALL_NETWORK_CHECK_AMEX) {
            _launchCardScreen();
        }

        if (callerId == CALL_NETWORK_CHECK_KEY_ENTRY) {
            //openKeyEntry();
            Void_Dialog();
        }

        if (callerId == CALL_NETWORK_CHECK_KEY_ENTRY_AMEX) {
            //openKeyEntry();
            Void_Dialog();
        }
    }

    public void onNavBack(View v) {

        onNavBackPressed();
    }

    public void onBatteryClick(View v) {
        // show battery dialog
        if (imgBattery.getVisibility() == View.VISIBLE) {
            showBattery_Dialog();
        }
    }

    private void showBattery_Dialog() {

        try {

            battery_dlg = new Dialog(this);
            battery_dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            battery_dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            battery_dlg.setContentView(R.layout.dlg_batterylevel);
            battery_dlg.setCanceledOnTouchOutside(true);

            final TextView txtTitle = (TextView) battery_dlg.findViewById(R.id.txtTitle);
            txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));

            final TextView lblBatteryLevel = (TextView) battery_dlg.findViewById(R.id.lblBatteryLevel);
            lblBatteryLevel.setTypeface(TypeFaceUtils.setRobotoMedium(this));

            final TextView txtBatteryLevel = (TextView) battery_dlg.findViewById(R.id.txtBatteryLevel);
            txtBatteryLevel.setTypeface(TypeFaceUtils.setRobotoMedium(this));
            txtBatteryLevel.setText(battery_percentage + " %");

            final Button btnOk = (Button) battery_dlg.findViewById(R.id.btnOk);
            btnOk.setTypeface(TypeFaceUtils.setLatoLight(this));

            btnOk.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    battery_dlg.dismiss();
                }
            });

            final ImageView imgReader = (ImageView) battery_dlg.findViewById(R.id.imgReader);

            if (com.setting.env.Environment.is_demo_version) {
                imgReader.setVisibility(View.GONE);
            } else {
                imgReader.setVisibility(View.VISIBLE);
            }

            battery_dlg.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            SalePad.signal_val = SalePad.SIGNAL_CLOSE;
            SalePad_Keyentry.signal_val = SalePad_Keyentry.SIGNAL_CLOSE;
            OrderTrackingActivity.isClose = true;
            logoutAllNow();
        }
    }

    private void Void_Dialog() {

        try {

            void_dialog = new Dialog(this);
            void_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            void_dialog.setContentView(R.layout.dialog_void);
            void_dialog.setCanceledOnTouchOutside(true);

            final TextView txtTitle = (TextView) void_dialog
                    .findViewById(R.id.txtTitle);
            txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(this));
            txtTitle.setText("Please enter pin code");

            final EditText edtPin = (EditText) void_dialog
                    .findViewById(R.id.edtPin);
            edtPin.setTypeface(TypeFaceUtils.setLatoRegular(this));
            edtPin.requestFocus();

            final Button btnSubmit = (Button) void_dialog.findViewById(R.id.btnSubmit);
            btnSubmit.setTypeface(TypeFaceUtils.setLatoRegular(this));

            if (lang_status == LangPrefs.LAN_EN) {

            } else if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_Key_EntryPin(txtTitle, edtPin, btnSubmit);
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
                                SelectPaymentMethod.this,
                                getResources().getString(R.string.IncorrectPin),
                                Toast.LENGTH_LONG).show();

                    } else {
                        void_dialog.dismiss();
                        openKeyEntry();

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
                                    getApplicationContext(), SelectPaymentMethod.this);
                            sinlang_select.Apply_Key_EntryPin(txtTitle, edtPin,
                                    btnSubmit);

                        } else {

                            edtPin.setTypeface(TypeFaceUtils
                                    .setLatoRegular(SelectPaymentMethod.this));
                        }
                    } else if (lang_status == LangPrefs.LAN_TA) {
                        if (str == null || str.length() == 0) {
                            tamlang_select = new Tamil_LangSelect(
                                    getApplicationContext(), SelectPaymentMethod.this);
                            tamlang_select.Apply_VoidPin(txtTitle, edtPin,
                                    btnSubmit);
                        } else {

                            edtPin.setTypeface(TypeFaceUtils
                                    .setLatoRegular(SelectPaymentMethod.this));
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
}