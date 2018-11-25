package com.mpos.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell on 4/25/2017.
 */

public class MerchantProfiles {

    @SerializedName("profile")
    @Expose
    private List<Profile> profile = null;

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

}
