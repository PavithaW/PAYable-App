package com.mpos.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;

public class ProgressDialogMessagesUtil {

    Context context;
    LangPrefs langPrefs;

    static int lang_status = 0;

    public ProgressDialogMessagesUtil(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        lang_status = LangPrefs.getLanguage(this.context);
    }

    public static String loginTranslation(int lang_status) {

        if (lang_status == LangPrefs.LAN_EN) {
            return "Signing in...";
        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("msúfiñka mj;S".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {
            return "cs;EiofpwPu;fs; rw;W nghWj;jpUq;fs;";
        } else {
            return "Signing in...";
        }
    }

    public static String changePwdTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("lreKdlr /£ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "rw;W nghWj;jpUq;fs;";

        } else {

            return "Please wait...";
        }
    }

    public static String dsSalesAudioTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("lreKdlr /£ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "rw;W nghWj;jpUq;fs;";

        } else {

            return "Please wait...";
        }
    }

    public static String eReceiptResendTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Resending E-Receipt";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String(",ÿm; hjñka mj;S'".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "gw;Wr;rPl;L mDg;ggLfpd;wJ";

        } else {

            return "Sending E-Receipt";
        }
    }

    public static String eReceiptTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Sending E-Receipt";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String(",ÿm; hjñka mj;S'".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "gw;Wr;rPl;L mDg;ggLfpd;wJ";

        } else {

            return "Sending E-Receipt";
        }
    }

    public static String salePadTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Checking  network connectivity.\nplease wait ...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("wka;¾cd, iïnkaO;djh mÍlaId flfrñka mj;S' u|la /|S isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return ",iza ,izg;G rupghu;f;fg;gLfpd;wJ. rw;Wg; nghWj;jpUq;fs;";

        } else {

            return "Checking  network connectivity.\nplease wait ...";
        }
    }

    public static String swipeTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("lreKdlr /£ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "rw;W nghWj;jpUq;fs;";

        } else {

            return "Please wait...";
        }
    }

    public static String settlementTranslation(int lang_status) {
        String res = null;

        if (lang_status == LangPrefs.LAN_EN) {

            res = "Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("lreKdlr /£ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            res = "rw;W nghWj;jpUq;fs;";

        }
        return res;
    }

    public static String signPadTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Uploading signature.Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("w;aik we;=<;a flfrñka mj;S' lreKdlr /|S isákak'".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "ifnahg;gk; gjpNtw;wg;gLfpd;wJ. rw;W nghWj;jpUq;fs;";

        } else {

            return "Uploading signature.Please wait...";
        }
    }

    public static String txDetailTranslation(int lang_status) {
        if (lang_status == LangPrefs.LAN_EN) {

            return "Please wait...";

        } else if (lang_status == LangPrefs.LAN_SIN) {

            try {
                return new String("lreKdlr /£ isákak".getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }

        } else if (lang_status == LangPrefs.LAN_TA) {

            return "rw;W nghWj;jpUq;fs;";

        } else {

            return "Please wait...";
        }
    }

}
