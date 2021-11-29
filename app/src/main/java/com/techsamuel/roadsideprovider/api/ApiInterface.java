package com.techsamuel.roadsideprovider.api;

import com.techsamuel.roadsideprovider.model.AdminUser;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.TransactionModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("fetchAllAdmin")
    Call<AdminUser> fetchAllAdmin();


    @FormUrlEncoded
    @POST("checkUserByPhone")
    Call<UserModel> checkUserByPhone(@Field("user_phone") String user_phone);

    @FormUrlEncoded
    @POST("getUserById")
    Call<UserModel> getUserById(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getAllTransactionById")
    Call<TransactionModel> getAllTransactionById(@Field("user_id") String user_id);

    @Multipart
    @POST("saveUserDetails")
    Call<UserModel> saveUserDetails(@Part MultipartBody.Part user_phone, @Part MultipartBody.Part name,@Part MultipartBody.Part profilePhotoFile, @Part MultipartBody.Part email,
                                    @Part MultipartBody.Part password, @Part MultipartBody.Part confirm_password,
                                    @Part MultipartBody.Part device_type);

    @FormUrlEncoded
    @POST("getAllServices")
    Call<ServiceModel> getAllServices(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("getServicesByProviderId")
    Call<ServiceModel> getServicesByProviderId(@Field("provider_id") String providerId);

    @FormUrlEncoded
    @POST("registerVehicle")
    Call<DataSavedModel> registerVehicle(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                            @Field("user_type") String user_type, @Field("user_id") String user_id,
                                       @Field("vmake") String vmake,@Field("vmodel") String vmodel, @Field("plate_no") String plate_no,
                                       @Field("vyear") String vyear, @Field("vr_exp_date") String vr_exp_date
                                       );

    @FormUrlEncoded
    @POST("getRegisteredVehicle")
    Call<VehicleModel> getRegisteredVehicle(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                            @Field("user_type") String user_type, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("updateDeviceInformationToServer")
    Call<DataSavedModel> updateDeviceInformationToServer(@Field("device_type") String device_type,@Field("user_type") String user_type,@Field("user_id") String user_id, @Field("lang_code") String lang_code,
                                            @Field("latitude") String latitude, @Field("longitude") String longitude,@Field("fcm") String fcm,
                                                         @Field("device_id") String device_id,@Field("firebase_id") String firebase_id);

    @FormUrlEncoded
    @POST("getAllSettings")
    Call<SettingsModel> getAllSettings(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("getAllProviders")
    Call<ProviderModel> getAllProviders(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                        @Field("user_type") String user_type, @Field("user_id") String user_id,
                                        @Field("user_lat") String user_lat, @Field("user_long") String user_long
                                        );

    @FormUrlEncoded
    @POST("getProviderById")
    Call<ProviderModel> getProviderById(@Field("provider_id") String providerId);

    @FormUrlEncoded
    @POST("getMessageByTypeAndId")
    Call<MessageModel> getMessageByTypeAndId(@Field("device_type") String device_type,@Field("lang_code") String lang_code,
                                             @Field("user_type") String user_type,@Field("user_id") String user_id);




}
