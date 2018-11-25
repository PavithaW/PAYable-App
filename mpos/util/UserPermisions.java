package com.mpos.util;

public class UserPermisions {

    private final static long KEY_ENTRY_TRANSACTION = 1;
    private final static long VOID_TRASNACTION = 2;
    private final static long MULTIPLE_PROFILE = 4;
    private final static long KEY_ENTRY__ALL_BIN = 8;
    private final static long INSTALLMENT = 16;


    public static boolean isKeyEntryTransactionsON(long permission) {

        long res = permission & KEY_ENTRY_TRANSACTION;

        if (res == KEY_ENTRY_TRANSACTION) {
            return true;
        }

        return false;
    }


    public static long setKeyEntryTransactionsON(long permission) {
        return permission | KEY_ENTRY_TRANSACTION;
    }

    public static long setKeyEntryTransactionsOFF(long permission) {
        return permission & (~KEY_ENTRY_TRANSACTION);
    }


    public static boolean isVoidTransactionsON(long permission) {

        long res = permission & VOID_TRASNACTION;

        if (res == VOID_TRASNACTION) {
            return true;
        }


        return false;
    }

    public static long setVoidTransactionsON(long permission) {
        return permission | VOID_TRASNACTION;
    }

    public static long setVoidTransactionsOFF(long permission) {
        return permission & (~VOID_TRASNACTION);
    }

    public static boolean isKeyEntryForAllBinON(long permission) {

        long res = permission & KEY_ENTRY__ALL_BIN;

        if (res == KEY_ENTRY__ALL_BIN) {
            return true;
        }


        return false;
    }


    public static long setKeyEntryForAllBinON(long permission) {
        return permission | KEY_ENTRY__ALL_BIN;
    }

    public static long setKeyEntryForAllBinOFF(long permission) {
        return permission & (~KEY_ENTRY__ALL_BIN);
    }

    public static boolean isMultipleProfileON(long permission) {

        long res = permission & MULTIPLE_PROFILE;

        if (res == MULTIPLE_PROFILE) {
            return true;
        }

        return false;
    }
    public static boolean isInstallmentON(long permission) {

        long res = permission & INSTALLMENT;

        if (res == INSTALLMENT) {
            return true;
        }

        return false;
    }

    public static long setMultipleProfileON(long permission) {
        return permission | MULTIPLE_PROFILE;
    }

    public static long setMultipleProfileOFF(long permission) {
        return permission & (~MULTIPLE_PROFILE);
    }

    public static long setInstallmentON(long permission) {
        return permission | INSTALLMENT;
    }

    public static long setInstallmentOFF(long permission) {
        return permission & (~INSTALLMENT);
    }

    public static String getJsonProfile(long permission) {

        String str = "{\"key_entry_transaction\":";

        if (isKeyEntryTransactionsON(permission)) {
            str = str + "1";
        } else {
            str = str + "0";
        }


        str = str + " , \"void_trasnaction\":";

        if (isVoidTransactionsON(permission)) {
            str = str + "1";
        } else {
            str = str + "0";
        }

        str = str + " , \"MULTIPLE_PROFILE\":";

        if (isMultipleProfileON(permission)) {
            str = str + "1";
        } else {
            str = str + "0";
        }

        str = str + " , \"KEY_ENTRY__ALL_BIN\":";

        if (isKeyEntryForAllBinON(permission)) {
            str = str + "1";
        } else {
            str = str + "0";
        }

        str = str + " , \"INSTALLMENT\":";

        if (isInstallmentON(permission)) {
            str = str + "1";
        } else {
            str = str + "0";
        }


        return str + "}";
    }

}