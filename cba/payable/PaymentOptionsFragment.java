package com.cba.payable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.setting.env.Config;
import com.setting.env.Consts;
import com.setting.env.ProfileInfo;

public class PaymentOptionsFragment extends DialogFragment {

    private static final String LOG_TAG = "payable " + PaymentOptionsFragment.class.getSimpleName();

    private View installmentView;
    private View currencyView;

    private TextView titleTV;
    private TextView selectinstallmentTV;
    private TextView selectcurrencyTV;

    private RadioButton rbtn_12;
    private RadioButton rbtn_24;
    private RadioButton rbtn_36;

    private RadioButton rbtn_lkr;
    private RadioButton rbtn_usd;
    private RadioButton rbtn_aud;
    private RadioButton rbtn_eur;

    private EditText instalmentET;

    private Button btn_selectoptions;

    private static final String ARG_INSTALLMENT_ENABLED = "installment_enabled";
    private static final String ARG_MCURRENCY_ENNABLED = "multi_currency_enabled";
    private static final String ARG_LKR_ENNABLED = "lkr_enabled";
    private static final String ARG_USD_ENNABLED = "usd_enabled";
    private static final String ARG_GBP_ENNABLED = "gbp_enabled";
    private static final String ARG_EUR_ENNABLED = "eur_enabled";
    private static final String ARG_INSTALLMENTS = "number_of_installments";
    private static final String ARG_CURRENCY_TYPE = "currency_type";

    private boolean mInstallmentEnabled;
    //private boolean mMultiCurrencyEnabled;

    private int mInstallments;
    private int mCurrencyType = 1;

    private boolean mIsLkrEnabled;
    private boolean mIsUsdEnabled;
    private boolean mIsGbpEnabled;
    private boolean mIsEurEnabled;

    private OnFragmentInteractionListener mListener;
    private LinearLayout lkrTabLayout;
    private LinearLayout usdTabLayout;
    private LinearLayout gbpTabLayout;
    private LinearLayout euroTabLayout;
    private Sinhala_LangSelect sinlang_select;
    private Tamil_LangSelect tamlang_select;
    private int lang_status = 0;
    private TextView otherTv;
    private TextView installment12Tv;
    private TextView installment24Tv;
    private TextView installment36Tv;
    private int loginStatusVisa;
    private int loginStatusAmex;

    public PaymentOptionsFragment() {
        // Required empty public constructor
    }

    public static PaymentOptionsFragment newInstance(String strDisplay, boolean installmentEnabled, boolean multiCurrencyEnabled, ProfileInfo profileInfo, ProfileInfo profileInfoAmex, int installments, int currencyType) {
        PaymentOptionsFragment fragment = new PaymentOptionsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_INSTALLMENT_ENABLED, installmentEnabled);
        args.putBoolean(ARG_MCURRENCY_ENNABLED, multiCurrencyEnabled);

        args.putBoolean(ARG_LKR_ENNABLED, false);
        args.putBoolean(ARG_USD_ENNABLED, false);
        args.putBoolean(ARG_GBP_ENNABLED, false);
        args.putBoolean(ARG_EUR_ENNABLED, false);

        args.putInt(ARG_INSTALLMENTS, installments);
        args.putInt(ARG_CURRENCY_TYPE, currencyType);

//        if (profileInfo != null) {
//
//            args.putBoolean(ARG_LKR_ENNABLED, profileInfo.isLkrEnabled());
//            args.putBoolean(ARG_USD_ENNABLED, profileInfo.isUsdEnabled());
//            args.putBoolean(ARG_GBP_ENNABLED, profileInfo.isGbpEnabled());
//            args.putBoolean(ARG_EUR_ENNABLED, profileInfo.isEurEnabled());
//        }

        if (profileInfo != null && profileInfoAmex != null) {
            if (profileInfo.isLkrEnabled() && profileInfoAmex.isLkrEnabled()) {
                args.putBoolean(ARG_LKR_ENNABLED, true);
            } else {
                if (profileInfo.isLkrEnabled()) {
                    args.putBoolean(ARG_LKR_ENNABLED, profileInfo.isLkrEnabled());
                } else if (profileInfoAmex.isLkrEnabled()) {
                    args.putBoolean(ARG_LKR_ENNABLED, profileInfoAmex.isLkrEnabled());
                } else {
                    args.putBoolean(ARG_LKR_ENNABLED, false);
                }
            }

            if (profileInfo.isUsdEnabled() && profileInfoAmex.isUsdEnabled()) {
                args.putBoolean(ARG_USD_ENNABLED, true);
            } else {
                if (profileInfo.isUsdEnabled()) {
                    args.putBoolean(ARG_USD_ENNABLED, profileInfo.isUsdEnabled());
                } else if (profileInfoAmex.isUsdEnabled()) {
                    args.putBoolean(ARG_USD_ENNABLED, profileInfoAmex.isUsdEnabled());
                } else {
                    args.putBoolean(ARG_USD_ENNABLED, false);
                }
            }

            if (profileInfo.isGbpEnabled() && profileInfoAmex.isGbpEnabled()) {
                args.putBoolean(ARG_GBP_ENNABLED, true);
            } else {
                if (profileInfo.isGbpEnabled()) {
                    args.putBoolean(ARG_GBP_ENNABLED, profileInfo.isGbpEnabled());
                } else if (profileInfoAmex.isGbpEnabled()) {
                    args.putBoolean(ARG_GBP_ENNABLED, profileInfoAmex.isGbpEnabled());
                } else {
                    args.putBoolean(ARG_GBP_ENNABLED, false);
                }
            }

            if (profileInfo.isEurEnabled() && profileInfoAmex.isEurEnabled()) {
                args.putBoolean(ARG_EUR_ENNABLED, true);
            } else {
                if (profileInfo.isEurEnabled()) {
                    args.putBoolean(ARG_EUR_ENNABLED, profileInfo.isEurEnabled());
                } else if (profileInfoAmex.isEurEnabled()) {
                    args.putBoolean(ARG_EUR_ENNABLED, profileInfoAmex.isEurEnabled());
                } else {
                    args.putBoolean(ARG_EUR_ENNABLED, false);
                }
            }

        } else {
            if (profileInfo != null) {

                args.putBoolean(ARG_LKR_ENNABLED, profileInfo.isLkrEnabled());
                args.putBoolean(ARG_USD_ENNABLED, profileInfo.isUsdEnabled());
                args.putBoolean(ARG_GBP_ENNABLED, profileInfo.isGbpEnabled());
                args.putBoolean(ARG_EUR_ENNABLED, profileInfo.isEurEnabled());
            } else if (profileInfoAmex != null) {
                args.putBoolean(ARG_LKR_ENNABLED, profileInfoAmex.isLkrEnabled());
                args.putBoolean(ARG_USD_ENNABLED, profileInfoAmex.isUsdEnabled());
                args.putBoolean(ARG_GBP_ENNABLED, profileInfoAmex.isGbpEnabled());
                args.putBoolean(ARG_EUR_ENNABLED, profileInfoAmex.isEurEnabled());
            }
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mInstallmentEnabled = getArguments().getBoolean(ARG_INSTALLMENT_ENABLED);

            mInstallments = getArguments().getInt(ARG_INSTALLMENTS);
            mCurrencyType = getArguments().getInt(ARG_CURRENCY_TYPE);

            loginStatusVisa = Config.getState(getActivity());
            loginStatusAmex = Config.getAmexState(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_options, container, false);
    }

    public void onSelectPressed() {
        if (mListener != null) {
            mListener.onSelectPaymentOptions(mInstallments, mCurrencyType);
        }

        this.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        installmentView = (View) view.findViewById(R.id.installmentsView);
        currencyView = (View) view.findViewById(R.id.currencyView);

        initViews(view, currencyView);

        instalmentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rbtn_12.setChecked(false);
                rbtn_24.setChecked(false);
                rbtn_36.setChecked(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    mInstallments = Integer.parseInt(s.toString());
                }

            }
        });

        btn_selectoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectPressed();
            }
        });

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        updateViewsVisbility();
    }

    private void initViews(View view, View currencyView) {
        titleTV = (TextView) view.findViewById(R.id.txtTitle);
        selectinstallmentTV = (TextView) view.findViewById(R.id.select_installment_txt);
        selectcurrencyTV = (TextView) view.findViewById(R.id.select_currency_txt);
        otherTv = (TextView) view.findViewById(R.id.other_tv);

        installment12Tv = (TextView) view.findViewById(R.id.installment_12tv);
        installment24Tv = (TextView) view.findViewById(R.id.installment_24tv);
        installment36Tv = (TextView) view.findViewById(R.id.installment_36tv);

        rbtn_12 = (RadioButton) view.findViewById(R.id.rbtn_12);
        rbtn_24 = (RadioButton) view.findViewById(R.id.rbtn_24);
        rbtn_36 = (RadioButton) view.findViewById(R.id.rbtn_36);

        rbtn_lkr = (RadioButton) view.findViewById(R.id.rbtn_lkr);
        rbtn_usd = (RadioButton) view.findViewById(R.id.rbtn_usd);
        rbtn_aud = (RadioButton) view.findViewById(R.id.rbtn_aud);
        rbtn_eur = (RadioButton) view.findViewById(R.id.rbtn_eur);

        instalmentET = (EditText) view.findViewById(R.id.installment_edittext);
        btn_selectoptions = (Button) view.findViewById(R.id.btn_selectoptions);

        lkrTabLayout = (LinearLayout) currencyView.findViewById(R.id.lkrTabView);
        usdTabLayout = (LinearLayout) currencyView.findViewById(R.id.usdTabView);
        gbpTabLayout = (LinearLayout) currencyView.findViewById(R.id.gbpTabView);
        euroTabLayout = (LinearLayout) currencyView.findViewById(R.id.euroTabView);

        rbtn_12.setOnClickListener(rbtnClickLstner);
        rbtn_24.setOnClickListener(rbtnClickLstner);
        rbtn_36.setOnClickListener(rbtnClickLstner);

        rbtn_lkr.setOnClickListener(currencyRbtnClickLstner);
        rbtn_usd.setOnClickListener(currencyRbtnClickLstner);
        rbtn_aud.setOnClickListener(currencyRbtnClickLstner);
        rbtn_eur.setOnClickListener(currencyRbtnClickLstner);

        sinlang_select = new Sinhala_LangSelect(getActivity().getApplicationContext(), getActivity());
        tamlang_select = new Tamil_LangSelect(getActivity().getApplicationContext(), getActivity());
        lang_status = LangPrefs.getLanguage(getActivity().getApplicationContext());

        if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.paymentOption(titleTV, selectinstallmentTV, installment12Tv, installment24Tv, installment36Tv, otherTv, instalmentET, selectcurrencyTV, btn_selectoptions);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.paymentOption(titleTV, selectinstallmentTV, installment12Tv, installment24Tv, installment36Tv, otherTv, instalmentET, selectcurrencyTV, btn_selectoptions);
        }
    }

    private void updateViewsVisbility() {
        if (!mInstallmentEnabled) {
            selectinstallmentTV.setVisibility(View.GONE);
            installmentView.setVisibility(View.GONE);
        } else {

            switch (mInstallments) {
                case 12:
                    rbtn_12.setChecked(true);
                    break;
                case 24:
                    rbtn_24.setChecked(true);
                    break;
                case 36:
                    rbtn_36.setChecked(true);
                    break;
                case 0:
                    break;
                default:
                    instalmentET.setText(String.valueOf(mInstallments));
            }
        }

        setCurrencyView();
    }

    private void setCurrencyView() {

        if (loginStatusVisa == Config.STATUS_LOGIN && loginStatusAmex == Config.STATUS_LOGIN) {

            if (Config.isEnableLKR(getActivity()) || Config.isEnableLKRAmex(getActivity())) {
                mIsLkrEnabled = true;
            }

            if (Config.isEnableUSD(getActivity()) || Config.isEnableUSDAmex(getActivity())) {
                mIsUsdEnabled = true;
            }

            if (Config.isEnableGBP(getActivity()) || Config.isEnableGBPAmex(getActivity())) {
                mIsGbpEnabled = true;
            }

            if (Config.isEnableEUR(getActivity()) || Config.isEnableEURAmex(getActivity())) {
                mIsEurEnabled = true;
            }

            lkrTabLayout.setVisibility(mIsLkrEnabled ? View.VISIBLE : View.GONE);
            usdTabLayout.setVisibility(mIsUsdEnabled ? View.VISIBLE : View.GONE);
            gbpTabLayout.setVisibility(mIsGbpEnabled ? View.VISIBLE : View.GONE);
            euroTabLayout.setVisibility(mIsEurEnabled ? View.VISIBLE : View.GONE);

            /*if (Config.isEnableLKR(getActivity()) || Config.isEnableLKRAmex(getActivity())) {
                mIsLkrEnabled = true;
                lkrTabLayout.setVisibility(View.VISIBLE);
            } else {
                mIsLkrEnabled = false;
                lkrTabLayout.setVisibility(View.GONE);
            }

            if (Config.isEnableUSD(getActivity()) || Config.isEnableUSDAmex(getActivity())) {
                mIsUsdEnabled = true;
                usdTabLayout.setVisibility(View.VISIBLE);
            } else {
                mIsUsdEnabled = false;
                usdTabLayout.setVisibility(View.GONE);
            }

            if (Config.isEnableGBP(getActivity()) || Config.isEnableGBPAmex(getActivity())) {
                mIsGbpEnabled = true;
                gbpTabLayout.setVisibility(View.VISIBLE);
            } else {
                mIsGbpEnabled = false;
                gbpTabLayout.setVisibility(View.GONE);
            }

            if (Config.isEnableEUR(getActivity()) || Config.isEnableEURAmex(getActivity())) {
                mIsEurEnabled = true;
                euroTabLayout.setVisibility(View.VISIBLE);
            } else {
                mIsEurEnabled = false;
                euroTabLayout.setVisibility(View.GONE);
            }*/

        } else if (loginStatusVisa == Config.STATUS_LOGIN) {
            mIsLkrEnabled = Config.isEnableLKR(getActivity());
            mIsUsdEnabled = Config.isEnableUSD(getActivity());
            mIsGbpEnabled = Config.isEnableGBP(getActivity());
            mIsEurEnabled = Config.isEnableEUR(getActivity());

            lkrTabLayout.setVisibility(mIsLkrEnabled ? View.VISIBLE : View.GONE);
            usdTabLayout.setVisibility(mIsUsdEnabled ? View.VISIBLE : View.GONE);
            gbpTabLayout.setVisibility(mIsGbpEnabled ? View.VISIBLE : View.GONE);
            euroTabLayout.setVisibility(mIsEurEnabled ? View.VISIBLE : View.GONE);

        } else if (loginStatusAmex == Config.STATUS_LOGIN) {
            mIsLkrEnabled = Config.isEnableLKRAmex(getActivity());
            mIsUsdEnabled = Config.isEnableUSDAmex(getActivity());
            mIsGbpEnabled = Config.isEnableGBPAmex(getActivity());
            mIsEurEnabled = Config.isEnableEURAmex(getActivity());

            lkrTabLayout.setVisibility(mIsLkrEnabled ? View.VISIBLE : View.GONE);
            usdTabLayout.setVisibility(mIsUsdEnabled ? View.VISIBLE : View.GONE);
            gbpTabLayout.setVisibility(mIsGbpEnabled ? View.VISIBLE : View.GONE);
            euroTabLayout.setVisibility(mIsEurEnabled ? View.VISIBLE : View.GONE);
        }

        rbtn_lkr.setEnabled(mIsLkrEnabled);
        rbtn_usd.setEnabled(mIsUsdEnabled);
        rbtn_aud.setEnabled(mIsGbpEnabled);
        rbtn_eur.setEnabled(mIsEurEnabled);

        checkCurrentlyUseCurrency();
    }

    private void checkCurrentlyUseCurrency() {
        switch (mCurrencyType) {
            case Consts.LKR:
                rbtn_lkr.setChecked(true);
                break;
            case Consts.USD:
                rbtn_usd.setChecked(true);
                break;
            case Consts.GBP:
                rbtn_aud.setChecked(true);
                break;
            case Consts.EUR:
                rbtn_eur.setChecked(true);
                break;
            default:
                rbtn_lkr.setChecked(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    View.OnClickListener rbtnClickLstner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            instalmentET.setText("");
            rbtn_12.setChecked(R.id.rbtn_12 == id);
            rbtn_24.setChecked(R.id.rbtn_24 == id);
            rbtn_36.setChecked(R.id.rbtn_36 == id);

            switch (id) {
                case R.id.rbtn_12:
                    mInstallments = 12;
                    break;
                case R.id.rbtn_24:
                    mInstallments = 24;
                    break;
                case R.id.rbtn_36:
                    mInstallments = 36;
                    break;
                default:
                    mInstallments = 0;
            }
        }
    };

    View.OnClickListener currencyRbtnClickLstner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            rbtn_lkr.setChecked(R.id.rbtn_lkr == id);
            rbtn_usd.setChecked(R.id.rbtn_usd == id);
            rbtn_aud.setChecked(R.id.rbtn_aud == id);
            rbtn_eur.setChecked(R.id.rbtn_eur == id);

            switch (id) {
                case R.id.rbtn_lkr:
                    mCurrencyType = Consts.LKR;
                    break;
                case R.id.rbtn_usd:
                    mCurrencyType = Consts.USD;
                    break;
                case R.id.rbtn_aud:
                    mCurrencyType = Consts.GBP;
                    break;
                case R.id.rbtn_eur:
                    mCurrencyType = Consts.EUR;
                    break;
                default:
                    mCurrencyType = Consts.LKR;
            }
        }
    };

    public interface OnFragmentInteractionListener {
        void onSelectPaymentOptions(int installment, int currencyType);
    }
}
