package com.techsamuel.roadsideprovider.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Generated("jsonschema2pojo")
public class OrderModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_details")
    @Expose
    private List<ServiceDetail> serviceDetails = null;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<ServiceDetail> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class ServiceDetail {

        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("service_price")
        @Expose
        private String servicePrice;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

    }
    public class Datum {

        @SerializedName("order_type")
        @Expose
        private String orderType;
        @SerializedName("provider_lat")
        @Expose
        private String providerLat;
        @SerializedName("provider_long")
        @Expose
        private String providerLong;
        @SerializedName("user_lat")
        @Expose
        private String userLat;
        @SerializedName("user_long")
        @Expose
        private String userLong;
        @SerializedName("service_fee_per_km")
        @Expose
        private Integer serviceFeePerKm;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("service_fee")
        @Expose
        private String serviceFee;
        @SerializedName("service_charge")
        @Expose
        private String serviceCharge;
        @SerializedName("total_service_cost")
        @Expose
        private Integer totalServiceCost;
        @SerializedName("total_cost")
        @Expose
        private String totalCost;

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getProviderLat() {
            return providerLat;
        }

        public void setProviderLat(String providerLat) {
            this.providerLat = providerLat;
        }

        public String getProviderLong() {
            return providerLong;
        }

        public void setProviderLong(String providerLong) {
            this.providerLong = providerLong;
        }

        public String getUserLat() {
            return userLat;
        }

        public void setUserLat(String userLat) {
            this.userLat = userLat;
        }

        public String getUserLong() {
            return userLong;
        }

        public void setUserLong(String userLong) {
            this.userLong = userLong;
        }

        public Integer getServiceFeePerKm() {
            return serviceFeePerKm;
        }

        public void setServiceFeePerKm(Integer serviceFeePerKm) {
            this.serviceFeePerKm = serviceFeePerKm;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(String serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public Integer getTotalServiceCost() {
            return totalServiceCost;
        }

        public void setTotalServiceCost(Integer totalServiceCost) {
            this.totalServiceCost = totalServiceCost;
        }

        public String getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(String totalCost) {
            this.totalCost = totalCost;
        }

    }

}

