package com.techsamuel.roadsideprovider.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ServiceCategoryModel {

    @SerializedName("category")
    @Expose
    private List<Category> category = null;

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public class Category {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("parent")
        @Expose
        private String parent;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("base_price")
        @Expose
        private Double basePrice;
        @SerializedName("service_id")
        @Expose
        private Integer serviceId;
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
        @SerializedName("is_subcategory_available")
        @Expose
        private Boolean isSubcategoryAvailable;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("subcategories")
        @Expose
        private List<Subcategory> subcategories = null;

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

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
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

        public Boolean getIsSubcategoryAvailable() {
            return isSubcategoryAvailable;
        }

        public void setIsSubcategoryAvailable(Boolean isSubcategoryAvailable) {
            this.isSubcategoryAvailable = isSubcategoryAvailable;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Subcategory> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<Subcategory> subcategories) {
            this.subcategories = subcategories;
        }

    }
    public class Subcategory {

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
        @SerializedName("is_subcategory_available")
        @Expose
        private Boolean isSubcategoryAvailable;
        @SerializedName("subcategories")
        @Expose
        private List<Object> subcategories = null;

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

        public Boolean getIsSubcategoryAvailable() {
            return isSubcategoryAvailable;
        }

        public void setIsSubcategoryAvailable(Boolean isSubcategoryAvailable) {
            this.isSubcategoryAvailable = isSubcategoryAvailable;
        }

        public List<Object> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<Object> subcategories) {
            this.subcategories = subcategories;
        }

    }

}
