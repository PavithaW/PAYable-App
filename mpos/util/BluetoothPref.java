package com.mpos.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.setting.env.Config;

public class BluetoothPref {

    protected static final String TAG = "JEYLOGS";

    private static final String PREF_NAME = "BLUETOOTH_PREF";

    public static final String KEY_DEVICE_NAME = "device_name";

    public static final String KEY_DEVICE_ADDRESS = "device_address";

    public static void setDeviceReaderStatus(Context c, String devicename, String deviceadd) {
        SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(KEY_DEVICE_NAME, devicename);
        editor.putString(KEY_DEVICE_ADDRESS, deviceadd);
        editor.commit();
    }

    public static String getDeviceName(Context c) {
        SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_DEVICE_NAME, null);
    }

    public static String getDeviceAddress(Context c) {
        SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_DEVICE_ADDRESS, null);
    }

    public static String generateValidDeviceName(Context context) {

        String deviceId = Config.getReaderId(context);

        //String deviceId = "15010002000301100058" ;
        String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10);

        Log.i(TAG, "Generated device name: " + strValidName);

        return strValidName;
    }

    public static String generateValidDeviceNameAmex(Context context) {

        String deviceId = Config.getReaderIdAmex(context);

        //String deviceId = "15010002000301100058" ;
        String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10);

        Log.i(TAG, "Generated device name AMEX : " + strValidName);

        return strValidName;
    }

    public static boolean isRegiesterRequired(Context context) {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String strDeviceName = pref.getString(KEY_DEVICE_NAME, null);

        if (strDeviceName == null || strDeviceName.trim().length() < 5) {
            return true;
        }

        String strAddress = pref.getString(KEY_DEVICE_ADDRESS, null);

        if (strAddress == null || strAddress.trim().length() < 5) {
            return true;
        }

        String deviceId = Config.getReaderId(context);

        if (deviceId == null) {
            Log.i(TAG, "deviceId is null");
            return true;
        }

        String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10);

        if (!strDeviceName.equalsIgnoreCase(strValidName)) {
            return true;
        }

        return false;
    }


    //AMEX
    public static boolean isRegiesterRequiredAmex(Context context) {

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String strDeviceName = pref.getString(KEY_DEVICE_NAME, null);

        if (strDeviceName == null || strDeviceName.trim().length() < 5) {
            return true;
        }

        String strAddress = pref.getString(KEY_DEVICE_ADDRESS, null);

        if (strAddress == null || strAddress.trim().length() < 5) {
            return true;
        }

        String deviceId = Config.getReaderIdAmex(context);

        if (deviceId == null) {
            Log.i(TAG, "deviceId is null");
            return true;
        }

        String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10);

        if (!strDeviceName.equalsIgnoreCase(strValidName)) {
            return true;
        }

        return false;
    }


}
