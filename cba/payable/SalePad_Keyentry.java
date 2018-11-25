package com.cba.payable;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.Amount;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.Order;
import com.mpos.util.AnimatedGifImageView;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UserPermisions;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

import java.text.DecimalFormat;

public class SalePad_Keyentry extends GenericActivity implements PaymentOptionsFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = "payable " + SalePad_Keyentry.class.getSimpleName();
    public static boolean isClose = false;

    private String strDisplay;
    private double valDisplay;
    private double valTotal;
    private DecimalFormat df;

    public static boolean isNewSale = true;
    public static boolean isSaleTimeout = false;
    private static boolean isReconnecting = false;

    public static int signal_val = 1;
    public static final int SIGNAL_CLOSE = 111111;
    private static final int ECHO_VALUE = 2222;
    private static final int CALL_NETWORK_CHECK = 100;

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

    //private boolean isInstallmentEnabled;
    private boolean isMultiCurrency;
    private boolean isInstallment;
   // private boolean isMultiCurrencyEnabled;

    private int mInstallments;
    private int mCurrencyType = Consts.LKR;
    private int mDefaultCurrencyType = Consts.LKR;

    private ProfileInfo profileInfo;

    //private boolean isInstallmentEnabledAmex;
    //private boolean isMultiCurrencyEnabledAmex;

    private ProfileInfo profileInfoAmex;

    private float MAX_AMOUNT_AMEX = 500;
    private float MAX_AMOUNT_AMEX_LKR;
    private float MAX_AMOUNT_AMEX_USD;
    private float MAX_AMOUNT_AMEX_GBP;
    private float MAX_AMOUNT_AMEX_EUR;
    private boolean isDualLogin;
    private boolean isVisaLogin;
    private boolean isAmexLogin;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_sale_pad__keyentry);

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

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            isDualLogin = true;
        } else {
            if (m_client.isLoggedIn()) {
                isVisaLogin = true;
            } else if (m_client_amex.isLoggedIn()) {
                isAmexLogin = true;
            }
        }

        setMaxValues();
        setMaxValuesAmex();

        initViews();

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

    private void setMaxValues() {
        Merchant oldWarMerchant = Config.getProfile(this);

        if (profileInfo != null) {
            MAX_AMOUNT_LKR = profileInfo.getLkrMaxAmount();
            MAX_AMOUNT_USD = profileInfo.getUsdMaxAmount();
            MAX_AMOUNT_GBP = profileInfo.getGbpMaxAmount();
            MAX_AMOUNT_EUR = profileInfo.getEurMaxAmount();
        }

        if(MAX_AMOUNT_LKR == 0){
            MAX_AMOUNT_LKR = oldWarMerchant.getMax_amount();
        }
    }

    private void setMaxValuesAmex() {
        if (profileInfoAmex != null) {
            MAX_AMOUNT_AMEX_LKR = profileInfoAmex.getLkrMaxAmount();
            MAX_AMOUNT_AMEX_USD = profileInfoAmex.getUsdMaxAmount();
            MAX_AMOUNT_AMEX_GBP = profileInfoAmex.getGbpMaxAmount();
            MAX_AMOUNT_AMEX_EUR = profileInfoAmex.getEurMaxAmount();
        }
    }

    private void setMerchantPermissions() {

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
        profileInfo = ProfileInfo.getInstance(profiles);

        String profilesAmex = Config.getUserProfilesAmex(this);
        profileInfoAmex = ProfileInfo.getInstance(profilesAmex);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "onResume salePadKey : " + signal_val + " " + isNewSale);

        if (signal_val == SIGNAL_CLOSE) {
            signal_val = -1;
            finish();
            return;
        }

        if (isNewSale) {
            onReAppear();
            isNewSale = false;
        }

        if (isClose) {
            isClose = false;
            finish();
        }

    }

    protected void onReAppear() {

        Log.d(LOG_TAG, "onReAppear : ");

        txtCalValue.setText("0.00");
        strDisplay = "";
        valDisplay = 0;
        valTotal = 0;
        isNextScrClicked = false;
    }

    private void initViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        rlReadCard = (RelativeLayout) findViewById(R.id.rlReadCard);
        // rlReadCard.setOnClickListener(onClickListener);

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
            sinlang_select.Apply_Sale_Key_Entry(txtTitle, txtReadCard, btnMoreOptions, btnClearOptions);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Sale(txtTitle, txtReadCard, btnMoreOptions, btnClearOptions);
        } else {
            // do nothing
        }

    }

    private void updateViewForCurrencyOrInstallment() {
        if (isMultiCurrency || isInstallment) {
            optionSelectView.setVisibility(View.VISIBLE);
        }

        int totalCurrencyCount = 0;

        if (Config.isEnableLKR(this) || Config.isEnableLKRAmex(this)) {
            totalCurrencyCount ++;
        }

        if (Config.isEnableUSD(this) || Config.isEnableUSDAmex(this)) {
            totalCurrencyCount ++;
        }

        if (Config.isEnableGBP(this) || Config.isEnableGBPAmex(this)) {
            totalCurrencyCount ++;
        }

        if (Config.isEnableEUR(this) || Config.isEnableEURAmex(this)) {
            totalCurrencyCount ++;
        }

        //BUG ID - 1836 : Hide options button if no installments and more than one currency is available
        if(totalCurrencyCount == 1 && !isInstallment){
            optionSelectView.setVisibility(View.GONE);
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

            //mCurrencyType = mDefaultCurrencyType = Config.getCurrencyAmex(this);
        }

        setCurrency(mDefaultCurrencyType);

//        if (!isMultiCurrency) {
//            DecimalFormat df = new DecimalFormat("####0.00");
//            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(Config.getMaxTxValue(this));
//        }

        /*if (isMultiCurrencyEnabled) {
            mCurrencyType = mDefaultCurrencyType = Config.getCurrency(this);
            setCurrency(mDefaultCurrencyType);
        } else {
            DecimalFormat df = new DecimalFormat("####0.00");
            STR_MAX_WARRNGING = "You can't exceed Rs. " + df.format(Config.getMaxTxValue(this));
        }*/
    }

    private void _refershDisplay() {

        Log.d(LOG_TAG, "_refershDisplay : ");

        isNextScrClicked = false;

        if (strDisplay == null || strDisplay.length() == 0) {
            valDisplay = 0;
            strDisplay = "";
            //txtCalValue.setText(df.format(0));
            txtCalValue.setText("0.00");
            //setInstallmentsOnType(new Double(0));
            setInstallmentsOnType(new Double(txtCalValue.getText().toString()));
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
        setInstallmentsOnType(new Double(txtCalValue.getText().toString()));
    }

    private void setInstallmentsOnType(double valDisplay) {

        if (mInstallments != 0) {
            if (valDisplay >= 0) {
                StringBuilder stringBuilder = new StringBuilder();

                if (lang_status == LangPrefs.LAN_SIN) {
                    sinlang_select.apply_Installemt(btnMoreOptions, stringBuilder, mInstallments, mCurrencyType, valDisplay);
                    btnMoreOptions.setText(stringBuilder.toString().replace(".", "'"));
                } else if (lang_status == LangPrefs.LAN_TA) {
                    tamlang_select.apply_Installemt(btnMoreOptions, stringBuilder, mInstallments, mCurrencyType, valDisplay);
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

                    stringBuilder.append(String.valueOf(valDisplay));
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
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    public void onMoreOptions(View view) {

        PaymentOptionsFragment frag = PaymentOptionsFragment.newInstance(strDisplay, isInstallment
                , isMultiCurrency
                , profileInfo
                , profileInfoAmex
                , mInstallments
                , mCurrencyType);
        FragmentManager fm = getSupportFragmentManager();
        frag.show(fm, "PaymentOptionsFragment");
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


        /*strDisplay = null;
        valDisplay = 0;
        _refershDisplay();*/
        //setInstallments(0);

        strDisplay = "";
        valDisplay = 0;
        setInstallmentsOnType(0);
        txtCalValue.setText("0.00");

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
    }

    private void _fireNextSecreenProcess() {
        Amount amount = new Amount(valDisplay / 100, mInstallments, mCurrencyType);

        if (m_order != null) {
            this.pushActivity(SelectPaymentMethod.class, amount, m_order);
        } else {
            this.pushActivity(SelectPaymentMethod.class, amount);
        }

    }

    public void onKeyPlus(View v) {
        if (strDisplay != null) {

            Log.d("SalePad", "strDisplay=" + strDisplay);
            Log.d("SalePad", "strDisplay=" + strDisplay.length());

            if (strDisplay.length() != 0) {
                strDisplay = strDisplay.substring(0, strDisplay.length() - 1);
            } else {
                strDisplay = "0";
            }

            _refershDisplay();
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

    public void onClearOptions(View view) {
        mCurrencyType = mDefaultCurrencyType;
        btnClearOptions.setVisibility(View.GONE);
        btnMoreOptions.setText("More payment options");
        setCurrency(mCurrencyType);
        setInstallments(0);
    }
}
