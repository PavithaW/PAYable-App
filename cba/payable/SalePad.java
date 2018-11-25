package com.cba.payable;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.connection.EnumApi;
import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.Order;
import com.mpos.pojo.SimpleReq;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.LangPrefs;
import com.mpos.util.ProgressDialogMessagesUtil;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UserPermisions;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

import java.text.DecimalFormat;

public class SalePad extends DSBaseActivity implements PaymentOptionsFragment.OnFragmentInteractionListener {

    //private static final String LOG_TAG = "payable " + SalePad.class.getSimpleName();
    private static final String LOG_TAG = "jeylogs";

    private String strDisplay;
    private double valDisplay;

    private double valTotal;

    private DecimalFormat df;

    public static boolean isNewSale = true;
    public static boolean isSaleTimeout = false;
    private static boolean isReconnecting = false;

    /*private boolean isInstallmentEnabled;
    private boolean isMultiCurrencyEnabled;*/
    private boolean isMultiCurrency;
    private boolean isInstallment;

    public static int signal_val = 1;

    public static final int SIGNAL_CLOSE = 111111;

    // private static final int MAX_AMOUNT = 999999 ;
    // private static final String STR_MAX_WARRNGING = "You can't exceed Rs.
    // 50000.00";
    // private static final int MAX_AMOUNT_BREAKER2 = 5000;
    private static final int ECHO_VALUE = 2222;

    private static final int CALL_NETWORK_CHECK = 100;
    private static final int CALL_NETWORK_CHECK_AMEX = 200;

    RelativeLayout rlReadCard;
    TextView txtReadCard, txtTitle, txtCalValue, txtReaderStatus;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnDot, btnZero, btnClear;

    private TextView currencyTypeTv;
    private ImageView currencyFlag;
    private LinearLayout optionSelectView;
    private Button btnClearOptions;
    private Button btnMoreOptions;

    private AnimatedGifImageView animatedGifImageView;
    ImageView imgBattery;

    Dialog battery_dlg;

    LangPrefs langPrefs;
    Sinhala_LangSelect sinlang_select;
    Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private float MAX_AMOUNT = 500;
    private float MAX_AMOUNT_LKR;
    private float MAX_AMOUNT_USD;
    private float MAX_AMOUNT_GBP;
    private float MAX_AMOUNT_EUR;
    private String STR_MAX_WARRNGING = "";
    private String STR_MIN_WARRNGING = "";

    private Order m_order = null;

    private int battery_percentage = 0;

    private boolean isReaderVerified = false;
    private boolean isNextScrClicked = false;

    private int mInstallments;
    private int mCurrencyType = Consts.LKR;
    private int mDefaultCurrencyType = Consts.LKR;

    private ProfileInfo mProfileInfo;
    private ProfileInfo mProfileInfoAmex;

    private float MAX_AMOUNT_AMEX = 500;
    private float MAX_AMOUNT_AMEX_LKR;
    private float MAX_AMOUNT_AMEX_USD;
    private float MAX_AMOUNT_AMEX_GBP;
    private float MAX_AMOUNT_AMEX_EUR;

    private boolean isInstallmentEnabledAmex;
    private boolean isMultiCurrencyEnabledAmex;
    private boolean isDualLogin;
    private boolean isVisaLogin;
    private boolean isAmexLogin;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_sale);

        setContentView(R.layout.activity_sale);
        unpairBluetoothDevices();
        setMerchantPermissions();
        MAX_AMOUNT = Config.getMaxTxValue(this);
        MAX_AMOUNT_AMEX = Config.getMaxTxValueAmex(this);

        DecimalFormat df = new DecimalFormat("####0.00");

        if (MAX_AMOUNT <= MAX_AMOUNT_AMEX) {
            MAX_AMOUNT = MAX_AMOUNT_AMEX;
            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(MAX_AMOUNT_AMEX);
        } else {
            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(MAX_AMOUNT);
        }

        isReaderVerified = false;
        isReconnecting = false;
        navaigateReadingScr = false;


        setMaxValues();
        setMaxValuesAmex();
        InitViews();

        // txtCalValue = (TextView) findViewById(R.id.txtValue);
        txtCalValue.setText("0.00");
        df = new DecimalFormat("0.00");
        strDisplay = "";
        valDisplay = 0;
        valTotal = 0;
        isNewSale = true;

        if (null != data) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Order) {
                    m_order = (Order) data[i];
                }
            }
        }

    }

    public void onMoreOptions(View view) {

        PaymentOptionsFragment frag = PaymentOptionsFragment.newInstance(strDisplay, isInstallment
                , isMultiCurrency
                , mProfileInfo
                , mProfileInfoAmex
                , mInstallments
                , mCurrencyType);
        FragmentManager fm = getSupportFragmentManager();
        frag.show(fm, "PaymentOptionsFragment");
    }

    private void setMaxValues() {
        Merchant oldWarMerchant = Config.getProfile(this);
        if (mProfileInfo != null) {
            MAX_AMOUNT_LKR = mProfileInfo.getLkrMaxAmount();
            MAX_AMOUNT_USD = mProfileInfo.getUsdMaxAmount();
            MAX_AMOUNT_GBP = mProfileInfo.getGbpMaxAmount();
            MAX_AMOUNT_EUR = mProfileInfo.getEurMaxAmount();
        }
    }

    private void setMaxValuesAmex() {
        if (mProfileInfoAmex != null) {
            MAX_AMOUNT_AMEX_LKR = mProfileInfoAmex.getLkrMaxAmount();
            MAX_AMOUNT_AMEX_USD = mProfileInfoAmex.getUsdMaxAmount();
            MAX_AMOUNT_AMEX_GBP = mProfileInfoAmex.getGbpMaxAmount();
            MAX_AMOUNT_AMEX_EUR = mProfileInfoAmex.getEurMaxAmount();
        }
    }

    private void setMerchantPermissions() {
        /*long permission = Config.getUser(this).getPermissions();

        isInstallmentEnabled = UserPermisions.isInstallmentON(permission);
        isMultiCurrencyEnabled = Config.isMultiCurrencyEnabled(getApplicationContext());

        long permissionAmex = Config.getAmexUser(this).getPermissions();

        isInstallmentEnabledAmex = UserPermisions.isInstallmentON(permissionAmex);
        isMultiCurrencyEnabledAmex = Config.isMultiCurrencyEnabled(getApplicationContext());

        getProfiles();*/

        long permission = Config.getUser(this).getPermissions();

        boolean isInstallmentEnabled = UserPermisions.isInstallmentON(permission);
        boolean isMultiCurrencyEnabled = Config.isMultiCurrencyEnabled(getApplicationContext());

        long permissionAmex = Config.getAmexUser(this).getPermissions();

        boolean isInstallmentEnabledAmex = UserPermisions.isInstallmentON(permissionAmex);
        boolean isMultiCurrencyEnabledAmex = Config.isMultiCurrencyEnabled(getApplicationContext());

        //CHECK isInstallment ENABLE STATUS FROM BOTH AND SINGLE LOGIN
        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            if (isInstallmentEnabled || isInstallmentEnabledAmex) {
                isInstallment = true;
            } else {
                isInstallment = false;
            }
        } else if (m_client.isLoggedIn()) {
            if (isInstallmentEnabled) {
                isInstallment = true;
            } else {
                isInstallment = false;
            }
        } else if (m_client_amex.isLoggedIn()) {
            if (isInstallmentEnabledAmex) {
                isInstallment = true;
            } else {
                isInstallment = false;
            }
        }

        //CHECK isMultiCurrency ENABLE STATUS FROM BOTH AND SINGLE LOGIN
        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            if (isMultiCurrencyEnabled && isMultiCurrencyEnabledAmex) {
                isMultiCurrency = true;
            } else {
                isMultiCurrency = false;
            }
        } else if (m_client.isLoggedIn()) {
            if (isMultiCurrencyEnabled) {
                isMultiCurrency = true;
            } else {
                isMultiCurrency = false;
            }
        } else if (m_client_amex.isLoggedIn()) {
            if (isMultiCurrencyEnabledAmex) {
                isMultiCurrency = true;
            } else {
                isMultiCurrency = false;
            }
        }

        getProfiles();
    }

    private void getProfiles() {
        String profiles = Config.getUserProfiles(this);
        mProfileInfo = ProfileInfo.getInstance(profiles);

        String profilesAmex = Config.getUserProfilesAmex(this);
        mProfileInfoAmex = ProfileInfo.getInstance(profilesAmex);


        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            isDualLogin = true;
        } else {
            if (m_client.isLoggedIn()) {
                isVisaLogin = true;
            } else if (m_client_amex.isLoggedIn()) {
                isAmexLogin = true;
            }
        }
    }

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

        //lvl = 2 ;

        battery_percentage = lvl;
        isReaderVerified = true;

        showBatteryLevels(lvl);

        if (isNextScrClicked) {
            _fireNextSecreenProcess();
        }

    }

    protected void statusCallBack(int status, String message) {

        Log.d(LOG_TAG, "statusCallBack : status = " + status + "message = " + message);

        //Log.i(TAG, "status :" + status);

        //storeMessageLog("Status code : " + status + "  message : " + message) ;

        if (status == REQUEST_ENABLE_BT) {
            setReaderMessage("Reader not Connected.", 2);
        }


        if (status == BT_CONNECTING) {
            if (!isReconnecting) {
                setReaderMessage("Trying to connect with cardreader ...", 0);
            }

            isReaderVerified = false;
            return;
        }

        if (status == QPOS_NOT_DECTECTED) {
            setReaderMessage("Please turn on the cardreader.", 2);
            isReaderVerified = false;
            isReconnecting = true;
            reconnect();
            return;
        }

        if (status == QPOS_CONNECTED) {
            setReaderMessage("Reader connected.Verifying ...", 0);
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

    protected void onResume() {

        Log.d(LOG_TAG, "onResume  : " + signal_val + " " + isNewSale);

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

    protected void onReAppear() {

        Log.d(LOG_TAG, "onReAppear : ");

        txtCalValue.setText("0.00");
        strDisplay = "";
        valDisplay = 0;
        valTotal = 0;

        isNextScrClicked = false;
    }

    private void _refershDisplay() {

        Log.d(LOG_TAG, "_refershDisplay : ");

        isNextScrClicked = false;

        if (strDisplay == null || strDisplay.length() == 0) {
            valDisplay = 0;
            strDisplay = "";
            //txtCalValue.setText(df.format(0));
            txtCalValue.setText("0.00");
            return;
        }

        valDisplay = Double.valueOf(strDisplay);
        //	txtCalValue.setText(df.format(valDisplay / 100));*/

        if (strDisplay.length() > 2) {
            txtCalValue.setText(strDisplay.substring(0, strDisplay.length() - 2) + "." + strDisplay.substring(strDisplay.length() - 2));

        } else {
            if (strDisplay.length() == 1) {
                txtCalValue.setText("0.0" + strDisplay);
            } else if (strDisplay.length() == 2) {
                txtCalValue.setText("0." + strDisplay);
            }
        }

    }

    private void _totalDisplay() {
        Log.d(LOG_TAG, "_totalDisplay : ");
        txtCalValue.setText(df.format(valTotal / 100));
    }

    public void onKey1(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "1";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "1";
        _refershDisplay();
    }

    public void onKey2(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "2";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "2";
        _refershDisplay();
    }

    public void onKey3(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "3";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "3";
        _refershDisplay();
    }

    public void onKey4(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "4";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "4";
        _refershDisplay();
    }

    public void onKey5(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "5";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "5";
        _refershDisplay();
    }

    public void onKey6(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "6";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "6";
        _refershDisplay();
    }

    public void onKey7(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "7";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "7";
        _refershDisplay();
    }

    public void onKey8(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "8";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "8";
        _refershDisplay();
    }

    public void onKey9(View v) {

        String strTmp = strDisplay;
        strTmp = strTmp + "9";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "9";
        _refershDisplay();
    }

    public void onKey0(View v) {
        if (valDisplay == 0) {
            return;
        }

        String strTmp = strDisplay;
        strTmp = strTmp + "0";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "0";
        _refershDisplay();
    }

    public void onKeydouble(View v) {
        if (valDisplay == 0) {
            return;
        }

        String strTmp = strDisplay;
        strTmp = strTmp + "00";

        double d = Double.valueOf(strTmp) / 100;

        if (d > MAX_AMOUNT) {
            showDialog("Error", STR_MAX_WARRNGING);
            return;
        }

        strDisplay = strDisplay + "00";
        _refershDisplay();
    }

    public void onKeyC(View v) {
        strDisplay = null;
        valDisplay = 0;
        _refershDisplay();
    }

    public void onKeyPlus(View v) {
        if (strDisplay == null || strDisplay.length() == 0) {
            return;
        }

        Log.d("SalePad", "strDisplay=" + strDisplay);
        Log.d("SalePad", "strDisplay=" + strDisplay.length());

        strDisplay = strDisplay.substring(0, strDisplay.length() - 1);
        _refershDisplay();
    }

    private void _launchCardScreen() {

        Log.d(LOG_TAG, "_launchCardScreen : ");

        if (!isReaderVerified) {
            isNextScrClicked = false;
            showDialog("Error", "Reader disconneccted. please check the reader.");
            return;
        }

        Amount amount = new Amount(valDisplay / 100, mInstallments, mCurrencyType);

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
            this.pushActivity(DsSalesAudio.class, amount, m_order);
        } else {
            this.pushActivity(DsSalesAudio.class, amount);
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

    public void onReadCard(View v) {

        Log.d(LOG_TAG, "onReadCard : ");

        if ((valDisplay / 100) < 1) {
            String s = "null";
            switch (mCurrencyType) {
                case Consts.LKR:
                    s = "LKR";
                    break;
                case Consts.USD:
                    s = "USD";
                    break;
                case Consts.GBP:
                    s = "GBP";
                    break;
                case Consts.EUR:
                    s = "EUR";
                    break;
            }
            showDialog("Error", "Please enter minimum 1.00 " + s);
            return;
        }

        isNextScrClicked = true;

        _fireNextSecreenProcess();

		/*if( ! isReaderVerified){
            return ;
		}
		
		if( isReaderVerified){
			
			if(battery_percentage < 5){
				showDialog("Error", "Please charge your card reader.");
				return;
			}
			
		}

		SimpleReq req = new SimpleReq();
		req.setValue(ECHO_VALUE);

		// m_client.echo(CALL_NETWORK_CHECK,
		// ProgressDialogMessagesUtil.salePadTranslation(), req);

		_launchCardScreen();

		// _launchCardScreen() ;
*/
    }

	/*
     * public void onCallError(EnumApi api, int callerId, ApiException e) { if
	 * (callerId == CALL_NETWORK_CHECK) { showDialog("Error",
	 * "Couldn't connect with transaction server.Please check your internet connectivity."
	 * ); } }
	 */

    public void onCallSuccess(EnumApi api, int callerId, APIData data) {

        Log.d(LOG_TAG, "onCallSuccess : EnumApi = " + api.toString() + "callerId = " + callerId
                + "APIData = " + data.getJson());

        if (callerId == CALL_NETWORK_CHECK) {
            _launchCardScreen();
        }

        if (callerId == CALL_NETWORK_CHECK_AMEX) {
            _launchCardScreen();
        }
    }

    public void onNavBack(View v) {

        onNavBackPressed();
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        rlReadCard = (RelativeLayout) findViewById(R.id.rlReadCard);
        // rlReadCard.setOnClickListener(onClickListener);

        txtReaderStatus = (TextView) findViewById(R.id.txtReaderStatus);
        txtReaderStatus.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtCalValue = (TextView) findViewById(R.id.txtCalValue);
        txtCalValue.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        txtCalValue.setOnTouchListener(otl);

        txtReadCard = (TextView) findViewById(R.id.txtReadCard);
        txtReadCard.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        currencyTypeTv = (TextView) findViewById(R.id.txtv_currency_type);
        currencyFlag = (ImageView) findViewById(R.id.imgv_cflag);
        optionSelectView = (LinearLayout) findViewById(R.id.optionSelectView);
        btnClearOptions = (Button) findViewById(R.id.btnClearOptions);
        btnMoreOptions = (Button) findViewById(R.id.btnMoreOptions);

        updateViewForCurrencyOrInstallment();

        btnOne = (Button) findViewById(R.id.btnOne);
        btnOne.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnTwo.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnThree = (Button) findViewById(R.id.btnThree);
        btnThree.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnFour = (Button) findViewById(R.id.btnFour);
        btnFour.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnFive = (Button) findViewById(R.id.btnFive);
        btnFive.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnSix = (Button) findViewById(R.id.btnSix);
        btnSix.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnSeven.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnEight = (Button) findViewById(R.id.btnEight);
        btnEight.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnNine = (Button) findViewById(R.id.btnNine);
        btnNine.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnDot = (Button) findViewById(R.id.btnDoubleZero);
        btnDot.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnZero = (Button) findViewById(R.id.btnZero);
        btnZero.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.gifView));
        animatedGifImageView.setAnimatedGif(R.raw.bluetooth_five, AnimatedGifImageView.TYPE.AS_IS);

        imgBattery = (ImageView) findViewById(R.id.imgBattery);

        // imgBattery.setBackgroundDrawable(showBatteryLevels());

        imgBattery.setVisibility(View.GONE);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Sale(txtTitle, txtReadCard, btnMoreOptions, btnClearOptions);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Sale(txtTitle, txtReadCard, btnMoreOptions, btnClearOptions);
        } else {
            // do nothing
        }
    }

    /*private void updateViewForCurrencyOrInstallment() {
        if (isMultiCurrencyEnabled || isInstallmentEnabled || isMultiCurrencyEnabledAmex || isInstallmentEnabledAmex) {
            optionSelectView.setVisibility(View.VISIBLE);
        }

        if (isMultiCurrencyEnabled) {
            mCurrencyType = mDefaultCurrencyType = Config.getCurrency(this);
            setCurrency(mDefaultCurrencyType);
        } else {
            DecimalFormat df = new DecimalFormat("####0.00");
            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(Config.getMaxTxValue(this));
        }
    }*/

    private void updateViewForCurrencyOrInstallment() {
        if (isMultiCurrency || isInstallment) {
            optionSelectView.setVisibility(View.VISIBLE);
        }

        if (isDualLogin || isVisaLogin) {
            if (Config.isEnableLKR(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.LKR;
            } else if (Config.isEnableUSD(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.USD;
            } else if (Config.isEnableGBP(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.GBP;
            } else if (Config.isEnableEUR(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.EUR;
            }

        } else if (isAmexLogin) {
            if (Config.isEnableLKRAmex(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.LKR;
            } else if (Config.isEnableUSDAmex(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.USD;
            } else if (Config.isEnableGBPAmex(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.GBP;
            } else if (Config.isEnableEURAmex(this)) {
                mCurrencyType = mDefaultCurrencyType = Consts.EUR;
            }
        }

        setCurrency(mDefaultCurrencyType);

        if (!isMultiCurrency) {
            DecimalFormat df = new DecimalFormat("####0.00");
            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(Config.getMaxTxValue(this));
        }
    }

    public void onClearOptions(View view) {
        mCurrencyType = mDefaultCurrencyType;
        btnClearOptions.setVisibility(View.GONE);
        btnMoreOptions.setText("More payment options");
        setCurrency(mCurrencyType);
        setInstallments(0);
    }

    private void setInstallments(int installments) {
        mInstallments = installments;

        if (installments > 0) {
            StringBuilder stringBuilder = new StringBuilder();


            if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.apply_Installemt(btnMoreOptions, stringBuilder, mInstallments, mCurrencyType, valDisplay / 100);
                btnMoreOptions.setText(stringBuilder.toString().replace(".", "'"));
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.apply_Installemt(btnMoreOptions, stringBuilder, mInstallments, mCurrencyType, valDisplay / 100);
                btnMoreOptions.setText(stringBuilder.toString().replace("\"", "."));
                btnMoreOptions.setText(stringBuilder.toString().replace("_", "-"));
            } else {
                switch (mCurrencyType) {
                    case Consts.LKR:
                        stringBuilder.append("LKR ");
                        break;
                    case Consts.USD:
                        stringBuilder.append("USD ");
                        break;
                    case Consts.GBP:
                        stringBuilder.append("GBP ");
                        break;
                    case Consts.EUR:
                        stringBuilder.append("EUR ");
                        break;
                    default:
                        stringBuilder.append("LKR ");
                }

                stringBuilder.append(String.valueOf(valDisplay / 100));
                stringBuilder.append(" / ");
                stringBuilder.append(String.valueOf(mInstallments)).append(" Installments");
                btnMoreOptions.setText(stringBuilder.toString());
            }

        } else {

            if (lang_status == LangPrefs.LAN_SIN) {
                sinlang_select.apply_more_payment(btnMoreOptions);
            } else if (lang_status == LangPrefs.LAN_TA) {
                tamlang_select.apply_more_payment(btnMoreOptions);
            } else {
                btnMoreOptions.setText("More payment options");
            }


            if (mCurrencyType == mDefaultCurrencyType) {
                btnClearOptions.setVisibility(View.GONE);
            }

        }
    }

    /*private void setInstallments(int installments) {
        mInstallments = installments;
        if (installments > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(valDisplay / 100));
            switch (mCurrencyType) {
                case Consts.LKR:
                    stringBuilder.append(" LKR");
                    break;
                case Consts.USD:
                    stringBuilder.append(" USD");
                    break;
                case Consts.GBP:
                    stringBuilder.append(" GBP");
                    break;
                case Consts.EUR:
                    stringBuilder.append(" EUR");
                    break;
                default:
                    stringBuilder.append(" LKR");
            }
            stringBuilder.append(" / ");
            if (lang_status == LangPrefs.LAN_SIN) {
                stringBuilder.append(String.valueOf(mInstallments)).append(" jd�l");
            } else if (lang_status == LangPrefs.LAN_TA) {
                stringBuilder.append(String.valueOf(mInstallments)).append(" jtizKiw");
            } else {
                stringBuilder.append(String.valueOf(mInstallments)).append(" Installments");
            }

            btnMoreOptions.setText(stringBuilder.toString());
        } else {

            if (lang_status == LangPrefs.LAN_SIN) {
                btnMoreOptions.setText("wu;r f.�� l%u");
            } else if (lang_status == LangPrefs.LAN_TA) {
                btnMoreOptions.setText("NkYk; fl;lzk; nrYj;Jk; Kiwfs;");
            } else {
                btnMoreOptions.setText("More payment options");
            }

            if (mCurrencyType == mDefaultCurrencyType) {
                btnClearOptions.setVisibility(View.GONE);
            }

        }
    }*/

    private void setCurrency(int currencyType) {

        mCurrencyType = currencyType;

        DecimalFormat df = new DecimalFormat("####0.00");

        switch (mCurrencyType) {
            case Consts.LKR:
                currencyFlag.setImageResource(R.drawable.flag_lkr);
                currencyTypeTv.setText("LKR");

                if (MAX_AMOUNT_LKR <= MAX_AMOUNT_AMEX_LKR) {
                    MAX_AMOUNT = MAX_AMOUNT_AMEX_LKR;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_AMEX_LKR) + " LKR";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 LKR";
                } else {
                    MAX_AMOUNT = MAX_AMOUNT_LKR;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_LKR) + " LKR";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 LKR";
                }

                break;
            case Consts.USD:
                currencyFlag.setImageResource(R.drawable.flag_usd);
                currencyTypeTv.setText("USD");

                if (MAX_AMOUNT_USD <= MAX_AMOUNT_AMEX_USD) {
                    MAX_AMOUNT = MAX_AMOUNT_AMEX_USD;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_AMEX_USD) + " USD";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 USD";
                } else {
                    MAX_AMOUNT = MAX_AMOUNT_USD;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_USD) + " USD";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 USD";
                }

                break;
            case Consts.GBP:
                currencyFlag.setImageResource(R.drawable.flag_gbp);
                currencyTypeTv.setText("GBP");

                if (MAX_AMOUNT_GBP <= MAX_AMOUNT_AMEX_GBP) {
                    MAX_AMOUNT = MAX_AMOUNT_AMEX_GBP;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_AMEX_GBP) + " GBP";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 GBP";
                } else {
                    MAX_AMOUNT = MAX_AMOUNT_GBP;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_GBP) + " GBP";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 GBP";
                }

                break;
            case Consts.EUR:
                currencyFlag.setImageResource(R.drawable.flag_eur);
                currencyTypeTv.setText("EUR");

                if (MAX_AMOUNT_EUR <= MAX_AMOUNT_AMEX_EUR) {
                    MAX_AMOUNT = MAX_AMOUNT_AMEX_EUR;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_AMEX_EUR) + " EUR";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 EUR";
                } else {
                    MAX_AMOUNT = MAX_AMOUNT_EUR;
                    STR_MAX_WARRNGING = "You can't exceed " + df.format(MAX_AMOUNT_EUR) + " EUR";
                    STR_MIN_WARRNGING = "Please enter minimum 1.00 EUR";
                }

                break;
        }
    }


    public void onBatteryClick(View v) {
        // show battery dialog
        showBattery_Dialog();
    }

    private void _showConnectingGifView() {

        Log.d(LOG_TAG, "_showConnectingGifView : ");

        imgBattery.setVisibility(View.GONE);
        animatedGifImageView.setVisibility(View.VISIBLE);
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
                    // TODO Auto-generated method stub
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

    private void setReaderMessage(String msg, int color) {

        Log.d(LOG_TAG, "setReaderMessage : msg = " + msg + ", color = " + color);

        // txtReaderStatus.setText("");
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

    private OnTouchListener otl = new OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    @Override
    public void onSelectPaymentOptions(int installments, int currencyType) {
        if (mCurrencyType != currencyType) {
            setCurrency(currencyType);

            if (!strDisplay.equals("") && strDisplay != null) {

                double d = Double.valueOf(strDisplay) / 100;

                if (d > MAX_AMOUNT) {
                    showDialog("Error", STR_MAX_WARRNGING);
                    strDisplay = null;
                    valDisplay = 0;
                    _refershDisplay();

                    installments = 0;

                    if (mCurrencyType != mDefaultCurrencyType) {
                        btnClearOptions.setVisibility(View.GONE);
                    }
                }

            }

        }

        if (installments > 0 || mCurrencyType != mDefaultCurrencyType) {
            btnClearOptions.setVisibility(View.VISIBLE);
        } else {
            btnClearOptions.setVisibility(View.GONE);
        }

        setInstallments(installments);

    }

    public void onMSGDlgBtnClick(int callerId) {

        if (callerId == 10001) {
            Home.isPortalChanged = true;
            OrderTrackingActivity.isClose = true;
            logoutAllNow();
            finish();
        }
    }
}
