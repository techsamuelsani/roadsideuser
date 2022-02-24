package com.techsamuel.roadsideprovider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryAndSubcategoryModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("base_price")
    @Expose
    private Double basePrice;
    @SerializedName("price_applicable_on_km")
    @Expose
    private Boolean priceApplicableOnKm;
    @SerializedName("price_applicable_on_minute")
    @Expose
    private Boolean priceApplicableOnMinute;
    @SerializedName("price_per_km")
    @Expose
    private Double pricePerKm;
    @SerializedName("price_per_minute")
    @Expose
    private Double pricePerMinute;
    @SerializedName("description")
    @Expose
    private String description;


    public CategoryAndSubcategoryModel(String name, String parent, Integer serviceId, String image, Double basePrice, Boolean priceApplicableOnKm, Boolean priceApplicableOnMinute, Double pricePerKm, Double pricePerMinute, String description) {
        this.name = name;
        this.parent = parent;
        this.serviceId = serviceId;
        this.image = image;
        this.basePrice = basePrice;
        this.priceApplicableOnKm = priceApplicableOnKm;
        this.priceApplicableOnMinute = priceApplicableOnMinute;
        this.pricePerKm = pricePerKm;
        this.pricePerMinute = pricePerMinute;
        this.description = description;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Boolean getPriceApplicableOnKm() {
        return priceApplicableOnKm;
    }

    public void setPriceApplicableOnKm(Boolean priceApplicableOnKm) {
        this.priceApplicableOnKm = priceApplicableOnKm;
    }

    public Boolean getPriceApplicableOnMinute() {
        return priceApplicableOnMinute;
    }

    public void setPriceApplicableOnMinute(Boolean priceApplicableOnMinute) {
        this.priceApplicableOnMinute = priceApplicableOnMinute;
    }

    public Double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public Double getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(Double pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
