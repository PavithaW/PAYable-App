package com.setting.env;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.pojo.MerchantProfiles;
import com.mpos.pojo.Profile;

import java.util.List;

/**
 * Created by Dell on 5/5/2017.
 */

public class ProfileInfo {

    private static final String LOG_TAG = ProfileInfo.class.getSimpleName();
    private static ProfileInfo profileInfo;

    private float lkrMaxAmount;
    private float usdMaxAmount;
    private float gbpMaxAmount;
    private float eurMaxAmount;

    private String lkrTerminalId;
    private String usdTerminalId;
    private String gbpTerminalId;
    private String eurTerminalId;

    private String lkrMerchantId;
    private String usdMerchantId;
    private String gbpMerchantId;
    private String eurMerchantId;

    private boolean lkrEnabled;
    private boolean usdEnabled;
    private boolean gbpEnabled;
    private boolean eurEnabled;

    private ProfileInfo() {
    }

    public static ProfileInfo getInstance(String profileJson) {

        profileInfo = new ProfileInfo();

        Gson gson = new GsonBuilder().create();
        MerchantProfiles merchantProfiles = gson.fromJson("{\"profile\":" + profileJson + "}", MerchantProfiles.class);
        List<Profile> profileList = merchantProfiles.getProfile();

        if (profileList != null) {

            for (Profile p :
                    profileList) {
                switch (p.getCurrency()) {

                    case Consts.LKR:
                        profileInfo.lkrEnabled = true;
                        profileInfo.lkrMaxAmount = p.getMaxAmount();
                        profileInfo.lkrTerminalId = p.getTerminalId();
                        profileInfo.lkrMerchantId = p.getCardAqId();
                        System.out.println("Max P lkr " + profileInfo.lkrMaxAmount);
                        break;

                    case Consts.USD:
                        profileInfo.usdEnabled = true;
                        profileInfo.usdMaxAmount = p.getMaxAmount();
                        profileInfo.usdTerminalId = p.getTerminalId();
                        profileInfo.usdMerchantId = p.getCardAqId();
                        System.out.println("Max P usd " + profileInfo.usdMaxAmount);
                        break;

                    case Consts.GBP:
                        profileInfo.gbpEnabled = true;
                        profileInfo.gbpMaxAmount = p.getMaxAmount();
                        profileInfo.gbpTerminalId = p.getTerminalId();
                        profileInfo.gbpMerchantId = p.getCardAqId();
                        System.out.println("Max P gbp " + profileInfo.gbpMaxAmount);
                        break;

                    case Consts.EUR:
                        profileInfo.eurEnabled = true;
                        profileInfo.eurMaxAmount = p.getMaxAmount();
                        profileInfo.eurTerminalId = p.getTerminalId();
                        profileInfo.eurMerchantId = p.getCardAqId();
                        System.out.println("Max P eur " + profileInfo.eurMaxAmount);
                        break;
                }
            }
        }

        return profileInfo;
    }

    public float getLkrMaxAmount() {
        return lkrMaxAmount;
    }

    public float getUsdMaxAmount() {
        return usdMaxAmount;
    }

    public float getGbpMaxAmount() {
        return gbpMaxAmount;
    }

    public float getEurMaxAmount() {
        return eurMaxAmount;
    }

    public String getLkrTerminalId() {
        return lkrTerminalId;
    }

    public String getUsdTerminalId() {
        return usdTerminalId;
    }

    public String getGbpTerminalId() {
        return gbpTerminalId;
    }

    public String getEurTerminalId() {
        return eurTerminalId;
    }

    public String getLkrMerchantlId() {
        return lkrMerchantId;
    }

    public String getUsdMerchantId() {
        return usdMerchantId;
    }

    public String getGbpMerchantId() {
        return gbpMerchantId;
    }

    public String getEurMerchantId() {
        return eurMerchantId;
    }

    public boolean isLkrEnabled() {
        return lkrEnabled;
    }

    public boolean isUsdEnabled() {
        return usdEnabled;
    }

    public boolean isGbpEnabled() {
        return gbpEnabled;
    }

    public boolean isEurEnabled() {
        return eurEnabled;
    }
}
