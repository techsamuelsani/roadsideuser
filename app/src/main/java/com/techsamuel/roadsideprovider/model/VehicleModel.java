
package com.techsamuel.roadsideprovider.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


@Generated("jsonschema2pojo")
public class VehicleModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("vmake")
        @Expose
        private String vmake;
        @SerializedName("vmodel")
        @Expose
        private String vmodel;
        @SerializedName("plate_no")
        @Expose
        private String plateNo;
        @SerializedName("vyear")
        @Expose
        private String vyear;
        @SerializedName("vr_exp_date")
        @Expose
        private String vrExpDate;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVmake() {
            return vmake;
        }

        public void setVmake(String vmake) {
            this.vmake = vmake;
        }

        public String getVmodel() {
            return vmodel;
        }

        public void setVmodel(String vmodel) {
            this.vmodel = vmodel;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public String getVyear() {
            return vyear;
        }

        public void setVyear(String vyear) {
            this.vyear = vyear;
        }

        public String getVrExpDate() {
            return vrExpDate;
        }

        public void setVrExpDate(String vrExpDate) {
            this.vrExpDate = vrExpDate;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

}




