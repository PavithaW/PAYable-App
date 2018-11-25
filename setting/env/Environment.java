package com.setting.env;

import android.content.Context;

import com.cba.payable.R;

public class Environment {

    public enum Env {
        QA, DEV, LIVE, DEMO
    }

    ;

    public enum Advt {
        ON, OFF
    }

    ;

    public final static boolean is_demo_version = false;

    private final static Env currentEnvironment = Env.LIVE;

    private final static Advt currentADVTStatus = Advt.OFF;

    private static final int appType_Id = 10000;
    private static final int version_Id = 1015;
    private static final String strVersion = "2.0";

    public static Advt getADVTStatus() {
        return currentADVTStatus;
    }

    public static String getVersionInfo() {
        return strVersion;
    }

    public static Env getEnvironment() {
        return currentEnvironment;
    }




    //SEYLAN
    public static String getApiHost(Context context) {

        switch (currentEnvironment) {

            case QA:
                return "https://123.231.14.207:8088/MobilePaymentR1000";

            case DEV:
                return "https://123.231.14.207:8088/MobilePaymentR1000";

            case LIVE:
                //LIVE GOOGLE PLAY
                if (Config.getBankCode(context).equals("seylan")) {
                    return context.getResources().getString(R.string.sey_live_server);
                } else if (Config.getBankCode(context).equals("commercial")) {
                    return context.getResources().getString(R.string.com_live_server);
                } else if (Config.getBankCode(context).equals("hnb")) {
                    return context.getResources().getString(R.string.hnb_live_server);
                } else if (Config.getBankCode(context).equals("boc")) {
                    return context.getResources().getString(R.string.boc_live_server);
                }else if (Config.getBankCode(context).equals("cargills")) {
                    return context.getResources().getString(R.string.cargills_live_server);
                }else {
                    return null;
                }

            case DEMO:
                return "https://123.231.14.207:8080/MobilePaymentR1000";

            default:
                return "http://192.168.1.9:8080/MobilePaymentR1000";
        }
    }


    //AMEX
    public static String getAMEXApiHost() {

        switch (currentEnvironment) {

            case QA:
                return "https://123.231.14.207:8095/MobilePaymentR1000";

            case DEV:
                return "https://123.231.14.207:8095/MobilePaymentR1000";

            case LIVE:
                return "https://mpos.nationstrust.com:8080/MobilePaymentR1000";

            case DEMO:
                return "https://123.231.14.207:8095/MobilePaymentR1000";

            default:
                return "http://192.168.1.9:8080/MobilePaymentR1000";
        }
    }

    public static int getVersionId() {
        return version_Id;
    }

    public static int getAppTypeId() {
        return appType_Id;
    }

}
