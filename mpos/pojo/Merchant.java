package com.mpos.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Merchant extends APIData {

    public static final int PWD_FLAG_PORTAL = 1;
    public static final int PWD_FLAG_APP = 2;

    public static final int READER_AUDIO_NO_PIN = 1;
    public static final int READER_BT_NO_PIN = 2;
    public static final int READER_BT_PIN = 3;

    public static final Parcelable.Creator<Merchant> CREATOR = new Parcelable.Creator<Merchant>() {
        public Merchant createFromParcel(Parcel in) {
            return new Merchant(in);
        }

        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };

    public Merchant() {

    }

    public Merchant(Parcel in) {
        id = in.readLong();
        userName = in.readString();
        pwd = in.readString();
        mobileNumber = in.readString();
        sureName = in.readString();
        firstName = in.readString();
        businessName = in.readString();
        address = in.readString();
        city = in.readString();
        deviceId = in.readString();
        simId = in.readString();
        cardReaderId = in.readString();
        status = in.readInt();
        auth1 = in.readLong();
        auth2 = in.readLong();
        pwdFlag = in.readInt();
        readerType = in.readInt();
        max_amount = in.readFloat();
        terminalId = in.readString();
        cardAqId = in.readString();
        permissions = in.readLong();
        profile = in.readString();
        versionId = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(userName);
        out.writeString(pwd);
        out.writeString(mobileNumber);
        out.writeString(sureName);
        out.writeString(firstName);
        out.writeString(businessName);
        out.writeString(address);
        out.writeString(city);
        out.writeString(deviceId);
        out.writeString(simId);
        out.writeString(cardReaderId);
        out.writeInt(status);
        out.writeLong(auth1);
        out.writeLong(auth2);
        out.writeInt(pwdFlag);
        out.writeInt(readerType);
        out.writeFloat(max_amount);
        out.writeString(terminalId);
        out.writeString(cardAqId);
        out.writeLong(permissions);
        out.writeString(profile);
        out.writeInt(versionId);
    }

    @SerializedName("id")
    @Expose
    private long id;//#########################

    @SerializedName("username")
    @Expose
    private String userName;//#########################

    @SerializedName("pwd")
    @Expose
    private String pwd;

    @SerializedName("mobile")
    @Expose
    private String mobileNumber;

    @SerializedName("surename")
    @Expose
    private String sureName;

    @SerializedName("firstname")
    @Expose
    private String firstName;

    @SerializedName("businessName")
    @Expose
    private String businessName;//#########################

    @SerializedName("address")
    @Expose
    private String address;//#########################

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("simId")
    @Expose
    private String simId;

    @SerializedName("cardReaderId")
    @Expose
    private String cardReaderId;//#########################

    @SerializedName("status")
    @Expose
    private int status;//#########################

    @SerializedName("auth1")
    @Expose
    private long auth1;//#########################

    @SerializedName("auth2")
    @Expose
    private long auth2;//#########################

    // added on 8th december 2015
    @SerializedName("pwdFlag")
    private int pwdFlag; //#########################

    // following fields added on 29th feburary 2015

    @SerializedName("readerType")
    @Expose
    private int readerType;//#########################

    @SerializedName("max_amount")
    @Expose
    private float max_amount;

    @SerializedName("terminalId")
    @Expose
    private String terminalId;

    @SerializedName("cardAqId")
    @Expose
    private String cardAqId;

    @SerializedName("permissions")
    @Expose
    private long permissions;    //#########################

    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName("versionId")
    @Expose
    private Integer versionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String str) {
        this.sureName = str;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String str) {
        this.businessName = str;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getCardReaderId() {
        return cardReaderId;
    }

    public void setCardReaderId(String cardReaderId) {
        this.cardReaderId = cardReaderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAuth1() {
        return auth1;
    }

    public void setAuth1(long auth1) {
        this.auth1 = auth1;
    }

    public long getAuth2() {
        return auth2;
    }

    public void setAuth2(long auth2) {
        this.auth2 = auth2;
    }

    public int getPwdFlag() {
        return pwdFlag;
    }

    public void setPwdFlag(int pwdFlag) {
        this.pwdFlag = pwdFlag;
    }

    public int getReaderType() {
        return readerType;
    }

    public void setReaderType(int readerType) {
        this.readerType = readerType;
    }


    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCardAqId() {
        return cardAqId;
    }

    public void setCardAqId(String cardAqId) {
        this.cardAqId = cardAqId;
    }

    public float getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(float max_amount) {
        this.max_amount = max_amount;
    }

    public long getPermissions() {
        return permissions;
    }

    public void setPermissions(long permissions) {
        this.permissions = permissions;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
}
