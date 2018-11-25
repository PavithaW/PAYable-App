package com.setting.env;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.MerchantProfiles;
import com.mpos.pojo.Profile;

import java.util.List;

public class Config {

    private final static String STRPREF = "PREFMPOSR1";

    private final static String KEY_APP_STATE = "mposappstate";
    private final static String KEY_USER_ID = "mpos_userId";
    private final static String KEY_USER_LOGIN_NAME = "mpos_username";
    private final static String KEY_USER_AUTH1 = "mpos_user_auth1";
    private final static String KEY_USER_AUTH2 = "mpos_user_auth2";
    private final static String KEY_USER_READER_ID = "mpos_user_readerid";
    private final static String KEY_USER_PWD_FLAG = "mpos_user_pwdflag";
    private final static String KEY_USER_READER_TYPE = "mpos_user_reader_type";
    private final static String KEY_USER_MAX_AMOUNT = "mpos_user_max_amount";
    private final static String KEY_USER_TETMINAL_ID = "mpos_user_terminal_id";
    private final static String KEY_USER_AQ_ID = "mpos_user_aq_id";
    private final static String KEY_USER_BUSINESS_NAME = "mpos_user_business_name";
    private final static String KEY_USER_BUSINESS_ADDRESS = "mpos_user_business_address";
    private final static String KEY_BANK_CODE = "mpos_bank_code";

    private final static String KEY_STN = "mpos_stn_val";
    private final static String KEY_INVOICE = "mpos_invoice_val_int";
    private final static String KEY_BATCH = "mpos_batch_val";
    private final static String KEY_READER = "mpos_crreader_val";
    private final static String KEY_APP_PERMISSIONS = "app_permissions";
    private final static String KEY_PROFILE = "app_profiles";
    private final static String KEY_API_VERSIONID = "api_versionid";
    private final static String KEY_PROFILE_STATUS = "profile_states";
    private final static String KEY_CURRENCY = "currency";
    private final static String KEY_MULTI_CURRENCY = "is_multi_currency_enable";

    private final static String KEY_LKR_STATUS = "lkr_status";
    private final static String KEY_USD_STATUS = "usd_status";
    private final static String KEY_GBP_STATUS = "gbp_status";
    private final static String KEY_EUR_STATUS = "eur_status";

    public static final int RECEIPT_STATE_NORMAL = 0;
    public static final int RECEIPT_STATE_RESEND = 1;
    public static int RECEIPT_STATE = RECEIPT_STATE_NORMAL;

    public final static int STATUS_LOGOUT = 0;
    public final static int STATUS_LOGIN = 1;

    public final static int READER_DEVICE1 = 1;
    public final static int READER_DS_AUDIO = 2;
    public final static int READER_DS_BT = 3;

    public static final float DEFAULT_MAX_AMOUNT = 50000;

    private final static String AMEX_KEY_APP_STATE = "amex_mposappstate";
    private final static String AMEX_KEY_USER_ID = "amex_mpos_userId";
    private final static String AMEX_KEY_USER_LOGIN_NAME = "amex_mpos_username";
    private final static String AMEX_KEY_USER_AUTH1 = "amex_mpos_user_auth1";
    private final static String AMEX_KEY_USER_AUTH2 = "amex_mpos_user_auth2";
    private final static String AMEX_KEY_USER_READER_ID = "amex_mpos_user_readerid";
    private final static String AMEX_KEY_USER_PWD_FLAG = "amex_mpos_user_pwdflag";
    private final static String AMEX_KEY_USER_READER_TYPE = "amex_mpos_user_reader_type";
    private final static String AMEX_KEY_USER_MAX_AMOUNT = "amex_mpos_user_max_amount";
    private final static String AMEX_KEY_USER_TETMINAL_ID = "amex_mpos_user_terminal_id";
    private final static String AMEX_KEY_USER_AQ_ID = "amex_mpos_user_aq_id";
    private final static String AMEX_KEY_USER_BUSINESS_NAME = "amex_mpos_user_business_name";
    private final static String AMEX_KEY_USER_BUSINESS_ADDRESS = "amex_mpos_user_business_address";
    private final static String AMEX_KEY_APP_PERMISSIONS = "amex_app_permissions";
    private final static String AMEX_KEY_PROFILE = "amex_app_profiles";
    private final static String AMEX_KEY_API_VERSIONID = "amex_api_versionid";
    private final static String AMEX_KEY_PROFILE_STATUS = "amex_profile_states";
    private final static String AMEX_KEY_CURRENCY = "amex_currency";

    private final static String KEY_LKR_STATUS_AMEX = "lkr_status_amex";
    private final static String KEY_USD_STATUS_AMEX = "usd_status_amex";
    private final static String KEY_GBP_STATUS_AMEX = "gbp_status_amex";
    private final static String KEY_EUR_STATUS_AMEX = "eur_status_amex";
    private final static String SIM_SERIAL = "sim_serial";

    private static final String LOG_TAG = Config.class.getSimpleName();

    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;


    public static void setState(Context c, int state) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(KEY_APP_STATE, state);
        editor.commit();
    }

    public static int getState(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_APP_STATE, 0);
    }

    public static void setAmexState(Context c, int state) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(AMEX_KEY_APP_STATE, state);
        editor.commit();
    }

    public static int getAmexState(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(AMEX_KEY_APP_STATE, 0);
    }

    public static int getActiveReader(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_READER, READER_DEVICE1);
    }

    public static int getCurrency(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_CURRENCY, Consts.LKR);
    }

    public static int getCurrencyAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(AMEX_KEY_CURRENCY, Consts.LKR);
    }

    public static String getReaderId(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_READER_ID, null);
    }

    public static void setReaderId(Context c, String readerId) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(KEY_USER_READER_ID, readerId);
        editor.commit();
    }


    public static String getReaderIdAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(AMEX_KEY_USER_READER_ID, null);
    }

    public static void setReaderIdAmex(Context c, String readerId) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(AMEX_KEY_USER_READER_ID, readerId);
        editor.commit();
    }

    public static void setActiveReader(Context c, int state) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(KEY_READER, state);
        editor.commit();
    }

    public static String getUserProfiles(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_PROFILE, null);
    }

    public static String getUserProfilesAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(AMEX_KEY_PROFILE, null);
    }

    public static void setUser(Context c, Merchant m) {
        int count_profile = 0;
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putInt(KEY_API_VERSIONID, -1);

        if (m.getVersionId() != null && m.getVersionId() > 0 && m.getProfile() != null) {
            Gson gson = new GsonBuilder().create();
            MerchantProfiles merchantProfiles = gson.fromJson("{\"profile\":"
                    + m.getProfile()
                    + "}", MerchantProfiles.class);

            List<Profile> profileList = merchantProfiles.getProfile();
            Profile profile = null;

            //reset currency status
            resetCurrencyStatus(c, false);

            for (Profile p :
                    profileList) {

                System.out.println("CURRENCY visa " + p.getCurrency() + " " + p.getStatus());

                //set currency status enable or not
                //  boolean isActive = p.getStatus() == 1;
                if (p.getStatus() == 1) {
                    count_profile++;
                }

                //keep courancy status
                setCurrencyStatus(c, p.getCurrency(), p.getStatus() == ACTIVE);

                if (p.getCurrency() == Consts.LKR && p.getStatus() == ACTIVE) {
                    profile = p;
                }

                if (profile == null && p.getStatus() == 1) {
                    profile = p;
                }

                // if (p.getCurrency() == Consts.LKR) profile = p;
            }


            setIsMultiCurrency(c, count_profile);

            if (profile == null) {
                profile = profileList.get(0);
            }

            if (profile != null) {
                m.setTerminalId(profile.getTerminalId());
                m.setCardAqId(profile.getCardAqId());
                m.setMax_amount(profile.getMaxAmount());
                editor.putInt(KEY_PROFILE_STATUS, profile.getStatus());
                editor.putInt(KEY_CURRENCY, profile.getCurrency());
            }
        }

        editor.putLong(KEY_USER_ID, m.getId());
        editor.putString(KEY_USER_LOGIN_NAME, m.getUserName());
        editor.putLong(KEY_USER_AUTH1, m.getAuth1());
        editor.putLong(KEY_USER_AUTH2, m.getAuth2());
        editor.putString(KEY_USER_READER_ID, m.getCardReaderId());
        //KEY_USER_PWD_FLAG
        editor.putInt(KEY_USER_PWD_FLAG, m.getPwdFlag());

        editor.putInt(KEY_USER_READER_TYPE, m.getReaderType());
        editor.putFloat(KEY_USER_MAX_AMOUNT, m.getMax_amount());
        editor.putString(KEY_USER_TETMINAL_ID, m.getTerminalId());
        editor.putString(KEY_USER_AQ_ID, m.getCardAqId());

        editor.putString(KEY_USER_BUSINESS_NAME, m.getBusinessName());
        editor.putString(KEY_USER_BUSINESS_ADDRESS, m.getAddress());
        editor.putLong(KEY_APP_PERMISSIONS, m.getPermissions());
        editor.putString(KEY_PROFILE, m.getProfile());
        //fix in dual sim phones
        editor.putString(SIM_SERIAL, m.getSimId());

        if (m.getVersionId() != null) {
            editor.putInt(KEY_API_VERSIONID, m.getVersionId());
        }

        editor.commit();
    }

    public static void setAmexUser(Context c, Merchant m) {
        int count_profile = 0;
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putInt(AMEX_KEY_API_VERSIONID, -1);

        if (m.getVersionId() != null && m.getVersionId() > 0 && m.getProfile() != null) {
            Gson gson = new GsonBuilder().create();
            MerchantProfiles merchantProfiles = gson.fromJson("{\"profile\":"
                    + m.getProfile()
                    + "}", MerchantProfiles.class);

            List<Profile> profileList = merchantProfiles.getProfile();
            Profile profile = null;

            //reset currency status
            resetCurrencyStatusAmex(c, false);

            for (Profile p :
                    profileList) {

                //set currency status enable or not
                //  boolean isActive = p.getStatus() == 1;
                if (p.getStatus() == 1) {
                    count_profile++;
                }

                //keep currency status
                setCurrencyStatusAmex(c, p.getCurrency(), p.getStatus() == ACTIVE);

                if (p.getCurrency() == Consts.LKR && p.getStatus() == ACTIVE) {
                    profile = p;
                }

                if (profile == null && p.getStatus() == 1) {
                    profile = p;
                }

                // if (p.getCurrency() == Consts.LKR) profile = p;
            }


            setIsMultiCurrency(c, count_profile);

            if (profile == null) {
                profile = profileList.get(0);
            }

            if (profile != null) {
                m.setTerminalId(profile.getTerminalId());
                m.setCardAqId(profile.getCardAqId());
                m.setMax_amount(profile.getMaxAmount());
                editor.putInt(AMEX_KEY_PROFILE_STATUS, profile.getStatus());
                editor.putInt(AMEX_KEY_CURRENCY, profile.getCurrency());
            }
        }

        editor.putLong(AMEX_KEY_USER_ID, m.getId());
        editor.putString(AMEX_KEY_USER_LOGIN_NAME, m.getUserName());
        editor.putLong(AMEX_KEY_USER_AUTH1, m.getAuth1());
        editor.putLong(AMEX_KEY_USER_AUTH2, m.getAuth2());
        editor.putString(AMEX_KEY_USER_READER_ID, m.getCardReaderId());
        //KEY_USER_PWD_FLAG
        editor.putInt(AMEX_KEY_USER_PWD_FLAG, m.getPwdFlag());

        editor.putInt(AMEX_KEY_USER_READER_TYPE, m.getReaderType());
        editor.putFloat(AMEX_KEY_USER_MAX_AMOUNT, m.getMax_amount());
        editor.putString(AMEX_KEY_USER_TETMINAL_ID, m.getTerminalId());
        editor.putString(AMEX_KEY_USER_AQ_ID, m.getCardAqId());

        editor.putString(AMEX_KEY_USER_BUSINESS_NAME, m.getBusinessName());
        editor.putString(AMEX_KEY_USER_BUSINESS_ADDRESS, m.getAddress());
        editor.putLong(AMEX_KEY_APP_PERMISSIONS, m.getPermissions());
        editor.putString(AMEX_KEY_PROFILE, m.getProfile());
        if (m.getVersionId() != null) {
            editor.putInt(AMEX_KEY_API_VERSIONID, m.getVersionId());
        }

        editor.apply();
    }

   /* public static void setAmexUser(Context c, Merchant m) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        if (m.getVersionId() > 0 && m.getProfile() != null) {
            Gson gson = new GsonBuilder().create();
            MerchantProfiles merchantProfiles = gson.fromJson("{\"profile\":"
                    + m.getProfile()
                    + "}", MerchantProfiles.class);

            List<Profile> profileList = merchantProfiles.getProfile();
            Profile profile = null;

            for (Profile p :
                    profileList) {
                if (p.getCurrency() == Consts.LKR) profile = p;
            }

            if (profile == null) {
                profile = profileList.get(0);
            }

            if (profile != null) {
                m.setTerminalId(profile.getTerminalId());
                m.setCardAqId(profile.getCardAqId());
                m.setMax_amount(profile.getMaxAmount());
                editor.putInt(AMEX_KEY_PROFILE_STATUS, profile.getStatus());
                editor.putInt(AMEX_KEY_CURRENCY, profile.getCurrency());
            }

            Log.e(LOG_TAG, m.getProfile());
        }

        editor.putLong(AMEX_KEY_USER_ID, m.getId());
        editor.putString(AMEX_KEY_USER_LOGIN_NAME, m.getUserName());
        editor.putLong(AMEX_KEY_USER_AUTH1, m.getAuth1());
        editor.putLong(AMEX_KEY_USER_AUTH2, m.getAuth2());
        editor.putString(AMEX_KEY_USER_READER_ID, m.getCardReaderId());
        //KEY_USER_PWD_FLAG
        editor.putInt(AMEX_KEY_USER_PWD_FLAG, m.getPwdFlag());

        editor.putInt(AMEX_KEY_USER_READER_TYPE, m.getReaderType());
        editor.putFloat(AMEX_KEY_USER_MAX_AMOUNT, m.getMax_amount());
        editor.putString(AMEX_KEY_USER_TETMINAL_ID, m.getTerminalId());
        editor.putString(AMEX_KEY_USER_AQ_ID, m.getCardAqId());

        editor.putString(AMEX_KEY_USER_BUSINESS_NAME, m.getBusinessName());
        editor.putString(AMEX_KEY_USER_BUSINESS_ADDRESS, m.getAddress());
        editor.putLong(AMEX_KEY_APP_PERMISSIONS, m.getPermissions());
        editor.putString(AMEX_KEY_PROFILE, m.getProfile());
        editor.putInt(AMEX_KEY_API_VERSIONID, m.getVersionId());

        editor.commit();

    }*/

    private static void setIsMultiCurrency(Context c, int count_profile) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_MULTI_CURRENCY, count_profile > 1);
        editor.apply();
    }

    public static boolean isMultiCurrencyEnabled(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_MULTI_CURRENCY, false);
    }


    //VISA currency
    public static void resetCurrencyStatus(Context c, Boolean status) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_LKR_STATUS, status);
        editor.putBoolean(KEY_USD_STATUS, status);
        editor.putBoolean(KEY_GBP_STATUS, status);
        editor.putBoolean(KEY_EUR_STATUS, status);
        editor.apply();
    }

    //set allowed currency
    private static void setCurrencyStatus(Context c, int type, Boolean status) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        System.out.println("VISA currency set " + type + " " + status);

        if (type == Consts.LKR) {
            editor.putBoolean(KEY_LKR_STATUS, status);
        } else if (type == Consts.USD) {
            editor.putBoolean(KEY_USD_STATUS, status);
        } else if (type == Consts.GBP) {
            editor.putBoolean(KEY_GBP_STATUS, status);
        } else if (type == Consts.EUR) {
            editor.putBoolean(KEY_EUR_STATUS, status);
        }

        editor.apply();
    }

    public static boolean isEnableLKR(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_LKR_STATUS, false);
    }

    public static boolean isEnableUSD(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_USD_STATUS, false);
    }

    public static boolean isEnableGBP(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_GBP_STATUS, false);
    }

    public static boolean isEnableEUR(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_EUR_STATUS, false);
    }
    //VISA currency end


    //AMEX currency
    public static void resetCurrencyStatusAmex(Context c, Boolean status) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_LKR_STATUS_AMEX, status);
        editor.putBoolean(KEY_USD_STATUS_AMEX, status);
        editor.putBoolean(KEY_GBP_STATUS_AMEX, status);
        editor.putBoolean(KEY_EUR_STATUS_AMEX, status);
        editor.apply();
    }

    private static void setCurrencyStatusAmex(Context c, int type, Boolean status) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        if (type == Consts.LKR) {
            editor.putBoolean(KEY_LKR_STATUS_AMEX, status);
        } else if (type == Consts.USD) {
            editor.putBoolean(KEY_USD_STATUS_AMEX, status);
        } else if (type == Consts.GBP) {
            editor.putBoolean(KEY_GBP_STATUS_AMEX, status);
        } else if (type == Consts.EUR) {
            editor.putBoolean(KEY_EUR_STATUS_AMEX, status);
        }
        editor.apply();
    }

    public static boolean isEnableLKRAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_LKR_STATUS_AMEX, false);
    }

    public static boolean isEnableUSDAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_USD_STATUS_AMEX, false);
    }

    public static boolean isEnableGBPAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_GBP_STATUS_AMEX, false);
    }

    public static boolean isEnableEURAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_EUR_STATUS_AMEX, false);
    }
    //AMEX currency end


    public static float getMaxTxValueAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getFloat(AMEX_KEY_USER_MAX_AMOUNT, DEFAULT_MAX_AMOUNT);
    }

    public static float getMaxTxValue(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getFloat(KEY_USER_MAX_AMOUNT, DEFAULT_MAX_AMOUNT);
    }

    public static int getVersionId(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_API_VERSIONID, -1);
    }

    public static int getVersionIdAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(AMEX_KEY_API_VERSIONID, -1);
    }

    public static int getPwdFlag(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_USER_PWD_FLAG, Merchant.PWD_FLAG_APP);
    }

    public static int getPwdFlagAmex(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getInt(AMEX_KEY_USER_PWD_FLAG, Merchant.PWD_FLAG_APP);
    }

    public static Merchant getProfile(Context c) {

        Merchant m = new Merchant();
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);

        m.setUserName(pref.getString(KEY_USER_LOGIN_NAME, ""));
        m.setTerminalId(pref.getString(KEY_USER_TETMINAL_ID, ""));
        m.setCardAqId(pref.getString(KEY_USER_AQ_ID, ""));

        m.setMax_amount(pref.getFloat(KEY_USER_MAX_AMOUNT, 0));
        m.setReaderType(pref.getInt(KEY_USER_READER_TYPE, 0));

        m.setBusinessName(pref.getString(KEY_USER_BUSINESS_NAME, ""));
        m.setAddress(pref.getString(KEY_USER_BUSINESS_ADDRESS, ""));
        m.setPermissions(pref.getLong(KEY_APP_PERMISSIONS, 0));

        return m;
    }

    public static Merchant getProfileAmex(Context c) {

        Merchant m = new Merchant();
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);

        m.setUserName(pref.getString(AMEX_KEY_USER_LOGIN_NAME, ""));
        m.setTerminalId(pref.getString(AMEX_KEY_USER_TETMINAL_ID, ""));
        m.setCardAqId(pref.getString(AMEX_KEY_USER_AQ_ID, ""));

        m.setMax_amount(pref.getFloat(AMEX_KEY_USER_MAX_AMOUNT, 0));
        m.setReaderType(pref.getInt(AMEX_KEY_USER_READER_TYPE, 0));

        m.setBusinessName(pref.getString(AMEX_KEY_USER_BUSINESS_NAME, ""));
        m.setAddress(pref.getString(AMEX_KEY_USER_BUSINESS_ADDRESS, ""));
        m.setPermissions(pref.getLong(AMEX_KEY_APP_PERMISSIONS, 0));

        return m;
    }

    public static Merchant getUser(Context c) {
        Merchant m = new Merchant();
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);

        m.setId(pref.getLong(KEY_USER_ID, -1));
        m.setUserName(pref.getString(KEY_USER_LOGIN_NAME, ""));
        m.setAuth1(pref.getLong(KEY_USER_AUTH1, 0));
        m.setAuth2(pref.getLong(KEY_USER_AUTH2, 0));
        m.setPermissions(pref.getLong(KEY_APP_PERMISSIONS, 0));
        m.setVersionId(pref.getInt(KEY_API_VERSIONID, 0));
        m.setCardReaderId(pref.getString(KEY_USER_READER_ID, null));
        m.setSimId(pref.getString(SIM_SERIAL, ""));

        return m;
    }

    public static Merchant getAmexUser(Context c) {
        Merchant m = new Merchant();
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);

        m.setId(pref.getLong(AMEX_KEY_USER_ID, -1));
        m.setUserName(pref.getString(AMEX_KEY_USER_LOGIN_NAME, ""));
        m.setAuth1(pref.getLong(AMEX_KEY_USER_AUTH1, 0));
        m.setAuth2(pref.getLong(AMEX_KEY_USER_AUTH2, 0));
        m.setPermissions(pref.getLong(AMEX_KEY_APP_PERMISSIONS, 0));
        m.setVersionId(pref.getInt(AMEX_KEY_API_VERSIONID, 0));
        m.setCardReaderId(pref.getString(KEY_USER_READER_ID, null));

        return m;
    }

    public static int generateSTN(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        int val = pref.getInt(KEY_STN, 0) + 1;

        if (val > 999999) {
            val = 1;
        }

        Editor editor = pref.edit();
        editor.putInt(KEY_STN, val);
        editor.commit();

        return val;
    }

    public static int generateINVOICE(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        int val = pref.getInt(KEY_INVOICE, 0) + 1;

        if (val > 999999999) {
            val = 1;
        }

        Editor editor = pref.edit();
        editor.putInt(KEY_INVOICE, val);
        editor.commit();

        return val;
    }

    public static int generateBatchId(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        int val = pref.getInt(KEY_BATCH, 0) + 1;

        if (val > 999999) {
            val = 1;
        }

        Editor editor = pref.edit();
        editor.putInt(KEY_BATCH, val);
        editor.commit();

        return val;
    }

    //bug id - 1945
    public static void setReaderType(Context c, int type) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,  Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(KEY_USER_READER_TYPE, type);
        editor.commit();
    }

    public static int getReaderType(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF, Context.MODE_PRIVATE);
        return pref.getInt(KEY_USER_READER_TYPE, 0);
    }

    public static String getBankCode(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_BANK_CODE, null);
    }

    public static void setBankCode(Context c, String code) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(KEY_BANK_CODE, code);
        editor.commit();
    }

    public static String getUserName(Context c) {
        SharedPreferences pref = c.getSharedPreferences(STRPREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_LOGIN_NAME, null);
    }

    public static boolean isOldWar(Context context){
        boolean isEnabledVisaLKR = Config.isEnableLKR(context);
        boolean isEnabledVisaUSD = Config.isEnableUSD(context);
        boolean isEnabledVisaGBP = Config.isEnableGBP(context);
        boolean isEnabledVisaEUR = Config.isEnableEUR(context);

        boolean isEnabledAmexLKR = Config.isEnableLKRAmex(context);
        boolean isEnabledAmexUSD = Config.isEnableUSDAmex(context);
        boolean isEnabledAmexGBP = Config.isEnableGBPAmex(context);
        boolean isEnabledAmexEUR = Config.isEnableEURAmex(context);

        if(!isEnabledVisaLKR && !isEnabledVisaUSD && !isEnabledVisaUSD && !isEnabledVisaGBP &&
                !isEnabledVisaEUR && !isEnabledAmexLKR && !isEnabledAmexUSD && !isEnabledAmexGBP && !isEnabledAmexEUR ){
            return true;
        }else{
            return false;
        }

    }


    public static boolean isKeyEntryOn() {
        return true;
    }

}
