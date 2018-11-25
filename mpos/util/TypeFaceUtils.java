package com.mpos.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Dell on 11/26/2015.
 */
public class TypeFaceUtils {

    //================    Set Lato Fonts ==================//

    public static Typeface setLatoLight(Context context) //
    {
        return Typeface.createFromAsset(context.getAssets(), "Lato_Font/Lato-Light.ttf");
    }

    public static Typeface setLatoRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Lato_Font/Lato-Regular.ttf");
    }

    public static Typeface setLatoBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Lato_Font/Lato-Bold.ttf");
    }

    //================  Set Roboto Fonts ====================//

    public static Typeface setRobotoLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto_Font/Roboto-Light.ttf");
    }

    public static Typeface setRobotoMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto_Font/Roboto-Medium.ttf");
    }

    public static Typeface setRobotoRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto_Font/Roboto-Regular.ttf");
    }

    public static Typeface setRobotoBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto_Font/Roboto-Bold.ttf");
    }

    public static Typeface setSignatureFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "signature/kinescope.otf");
    }


    public static Typeface setMurasuit(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Tamil/Murasuit.ttf");
    }

    //
    public static Typeface setTAMGobi(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Tamil/TAMGobi.ttf");
    }

//  Sinhala Fonts

    public static Typeface setSinhalaFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Sinhala_Font/dlmanel.TTF");
    }

//  Tamil Fonts

    public static Typeface setBaminiFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Tamil_Font/Baminiii.TTF");
    }
}
