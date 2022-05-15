package com.techsamuel.roadsideprovider.api;

import com.techsamuel.roadsideprovider.model.AdminUser;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.OrdersModel;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ReviewReasonModel;
import com.techsamuel.roadsideprovider.model.ServiceCategoryModel;
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
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("fetchAllAdmin")
    Call<AdminUser> fetchAllAdmin();

    @GET("getServiceCategory")
    Call<ServiceCategoryModel> getServiceCategory();


    @FormUrlEncoded
    @POST("checkUserByPhone")
    Call<UserModel> checkUserByPhone(@Field("user_phone") String user_phone);

    @FormUrlEncoded
    @POST("getUserById")
    Call<UserModel> getUserById(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getAllTransactionById")
    Call<TransactionModel> getAllTransactionById(@Field("user_type") String user_type,@Field("user_id") String user_id);

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
                                        @Field("user_lat") String user_lat, @Field("user_long") String user_long,
                                        @Field("service_id") String service_id
                                        );
    @FormUrlEncoded
    @POST("getNearestProvider")
    Call<ProviderModel> getNearestProvider(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                        @Field("user_type") String user_type, @Field("user_id") String user_id,
                                        @Field("user_lat") String user_lat, @Field("user_long") String user_long,
                                        @Field("service_id") String service_id
    );

    @FormUrlEncoded
    @POST("getProviderById")
    Call<ProviderModel> getProviderById(@Field("provider_id") String providerId);

    @FormUrlEncoded
    @POST("getMessageByTypeAndId")
    Call<MessageModel> getMessageByTypeAndId(@Field("device_type") String device_type,@Field("lang_code") String lang_code,
                                             @Field("user_type") String user_type,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getAllOrders")
    Call<OrdersModel> getAllOrders(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                   @Field("user_type") String user_type,@Field("user_id") String user_id, @Field("order_status_type") String order_status_type);

    @FormUrlEncoded
    @POST("getOrderDetailsById")
    Call<OrderModel> getOrderDetailsById(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                   @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("changeOrderStatusById")
    Call<DataSavedModel> changeOrderStatusById(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                         @Field("order_id") String order_id,@Field("input_status") String input_status);

    @Multipart
    @POST("createNewOrder")
    Call<OrderModel> createNewOrder(@Query("device_type") String device_type, @Query("lang_code") String lang_code,
                                    @Query("user_id") String user_id, @Query("provider_id") String provider_id,
                                    @Query("service_id[]") ArrayList<String> service_id, @Query("service_name[]") ArrayList<String> service_name,
                                    @Query("vehicle_id") String vehicle_id, @Query("vehicle_name") String vehicle_name,
                                    @Query("order_type") String order_type, @Query("service_description") String service_description,
                                    @Query("location") String location, @Query("latitude") String latitude, @Query("longitude") String longitude,
                                    @Part List<MultipartBody.Part> service_images);

    @FormUrlEncoded
    @POST("getPagesByDevicyType")
    Call<PageModel> getPagesByDevicyType(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("getReviewAndReason")
    Call<ReviewReasonModel> getReviewAndReason(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                               @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("orderActivityRequest")
    Call<DataSavedModel> orderActivityRequest(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                         @Field("user_type") String user_type,@Field("user_id") String user_id,
                                         @Field("order_id") String order_id,@Field("details") String details,@Field("type") String type,@Field("ratings") String ratings);

    @FormUrlEncoded
    @POST("saveOrderRequestGetId")
        // id	user_id	provider_id	service_id	service_name	accepted	date	timezone	base_price	price_per_minute
        // price_per_km	totalMinutes	totalKms	approx_cost1	approx_cost2	userLat	userLong	providerLat	providerLong
        // order_type	payment_type	notes

    Call<DataSavedModel> saveOrderRequestGetId(@Field("user_id") int user_id, @Field("provider_id") int provider_id,
                                               @Field("service_id") int service_id, @Field("service_name") String service_name,
                                              @Field("accepted") boolean accepted,@Field("rejected") boolean rejected,@Field("date") String date,
                                              @Field("timeZone") String timeZone,@Field("base_price") double base_price,@Field("price_per_minute") double price_per_minute,@Field("price_per_km") double price_per_km,
                                              @Field("totalMinutes") double totalMinutes,@Field("totalKms") double totalKms,@Field("approx_cost1") double approx_cost1,@Field("approx_cost2") double approx_cost2,@Field("userLat") double userLat,
                                              @Field("userLong") double userLong,@Field("providerLat") double providerLat,@Field("providerLong") double providerLong,@Field("dropOffLat") double dropOffLat,@Field("dropOffLong") double dropOffLong,
                                               @Field("order_type") String order_type, @Field("payment_type") String payment_type, @Field("notes") String notes,
                                               @Field("providerPhone") String providerPhone, @Field("markAsArrived") boolean markAsArrived, @Field("markAsCompleted") boolean markAsCompleted);

}
