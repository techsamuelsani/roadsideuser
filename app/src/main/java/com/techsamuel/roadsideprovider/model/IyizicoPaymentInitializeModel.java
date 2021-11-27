package com.techsamuel.roadsideprovider.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class IyizicoPaymentInitializeModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("systemTime")
    @Expose
    private Long systemTime;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("checkoutFormContent")
    @Expose
    private String checkoutFormContent;
    @SerializedName("tokenExpireTime")
    @Expose
    private Integer tokenExpireTime;
    @SerializedName("paymentPageUrl")
    @Expose
    private String paymentPageUrl;
    @SerializedName("payWithIyzicoPageUrl")
    @Expose
    private String payWithIyzicoPageUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Long systemTime) {
        this.systemTime = systemTime;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCheckoutFormContent() {
        return checkoutFormContent;
    }

    public void setCheckoutFormContent(String checkoutFormContent) {
        this.checkoutFormContent = checkoutFormContent;
    }

    public Integer getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Integer tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public String getPaymentPageUrl() {
        return paymentPageUrl;
    }

    public void setPaymentPageUrl(String paymentPageUrl) {
        this.paymentPageUrl = paymentPageUrl;
    }

    public String getPayWithIyzicoPageUrl() {
        return payWithIyzicoPageUrl;
    }

    public void setPayWithIyzicoPageUrl(String payWithIyzicoPageUrl) {
        this.payWithIyzicoPageUrl = payWithIyzicoPageUrl;
    }

}