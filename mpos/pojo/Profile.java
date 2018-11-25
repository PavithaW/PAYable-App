package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 4/25/2017.
 */

public class Profile {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("terminalId")
    @Expose
    private String terminalId;
    @SerializedName("cardAqId")
    @Expose
    private String cardAqId;
    @SerializedName("currency")
    @Expose
    private Integer currency;
    @SerializedName("max_amount")
    @Expose
    private Float maxAmount;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Float getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Float maxAmount) {
        this.maxAmount = maxAmount;
    }

}
