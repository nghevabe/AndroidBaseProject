package com.example.mybaseproject.ui.entities;

import com.google.gson.annotations.SerializedName;

public class LicenseEntity {
    @SerializedName("bankCode")
    private String bankCode;
    @SerializedName("aesKey")
    private String aesKey;
    @SerializedName("macKey")
    private String macKey;
    @SerializedName("keyId")
    private String keyId;
    @SerializedName("urlRoot")
    private String urlRoot;
    @SerializedName("pinning1")
    private String pinning1;
    @SerializedName("pinning2")
    private String pinning2;
    @SerializedName("pinning3")
    private String pinning3;
    @SerializedName("hostName")
    private String hostName;
    @SerializedName("userName")
    private String userName;
    @SerializedName("password")
    private String password;

    public String getBankCode() {
        return bankCode;
    }

    public String getAesKey() {
        return aesKey;
    }

    public String getMacKey() {
        return macKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getUrlRoot() {
        return urlRoot;
    }

    public String getPinning1() {
        return pinning1;
    }

    public String getPinning2() {
        return pinning2;
    }

    public String getPinning3() {
        return pinning3;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

