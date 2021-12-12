package com.techsamuel.roadsideprovider.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseUser;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.activity.register.UserRegisterActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonRequests {

    public static void checkUserByPhone(Context context,String userPhone){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<UserModel> call=apiInterface.checkUserByPhone(userPhone);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                AppSharedPreferences.init(context);
                Intent intent = null;
                if(response.body().getStatus()==Config.API_SUCCESS){
                    if(!response.body().getId().equals(0)){
                        AppSharedPreferences.write(Config.SHARED_PREF_USER_ID,response.body().getId().toString());
                        AppSharedPreferences.writeUserModel(Config.SHARED_PREF_USER_MODEL,response.body());
                        intent=new Intent(context,MainActivity.class);
                    }
                }else{
                    intent=new Intent(context, UserRegisterActivity.class);
                }

                context.startActivity(intent);

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Tools.showToast(context,"Connecton to server failed");
            }
        });
    }

    public static void getUserById(Context context,String userId){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<UserModel> call=apiInterface.getUserById(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                AppSharedPreferences.init(context);
                if(response.body().getStatus()==Config.API_SUCCESS){
                    if(!response.body().getId().equals(0)){
                        AppSharedPreferences.writeUserModel(Config.SHARED_PREF_USER_MODEL,response.body());
                        Tools.showToast(context,response.body().getMessage().toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Tools.showToast(context,"Connecton to server failed");
                Log.d("CommonRequest",t.getMessage().toString());
            }
        });
    }

    public static void updateUserBalance(Context context, String userId, TextView textView){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<UserModel> call=apiInterface.getUserById(userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                AppSharedPreferences.init(context);

                if(response.body().getStatus()==Config.API_SUCCESS){
                    if(!response.body().getId().equals(0)){
                        AppSharedPreferences.writeUserModel(Config.SHARED_PREF_USER_MODEL,response.body());
                        textView.setText(response.body().getData().getWallet()+" TRY");

                    }
                }


            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Tools.showToast(context,"Connecton to server failed");
                Log.d("CommonRequest",t.getMessage().toString());
            }
        });
    }




//    public static void getIdByPhone(Context context,String providerPhone){
//        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
//        Call<DataSavedModel> call=apiInterface.getIdByPhone(providerPhone);
//        call.enqueue(new Callback<DataSavedModel>() {
//            @Override
//            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
//                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
//                AppSharedPreferences.init(context);
//                if(response.body().getStatus()==Config.API_SUCCESS){
//                    if(response.body().getId()!=null){
//                        AppSharedPreferences.write(Config.SHARED_PREF_PROVIDER_ID,response.body().getId().toString());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<DataSavedModel> call, Throwable t) {
//                Tools.showToast(context,"Retrofit Connecton Failed");
//            }
//        });
//    }
//
//    public static void updateLocationToServer(Context context, LatLng location){
//        AppSharedPreferences.init(context);
//        String latitude= String.valueOf(location.getLatitude());
//        String longitude= String.valueOf(location.getLongitude());
//        String fcm=AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,"");
//        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
//        String providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
//        Call<DataSavedModel> call=apiInterface.updateLocationToServer(providerId,latitude,longitude);
//        call.enqueue(new Callback<DataSavedModel>() {
//            @Override
//            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
//                Tools.showToast(context,response.body().getMessage().toString());
//            }
//
//            @Override
//            public void onFailure(Call<DataSavedModel> call, Throwable t) {
//                    Log.d("LocationAndFcmToServer",t.getMessage().toString());
//            }
//        });
//
//    }
//
//    public static void updateFcmToServer(Context context){
//        AppSharedPreferences.init(context);
//        String fcm=AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,"");
//        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
//        String providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
//        Call<DataSavedModel> call=apiInterface.updateFcmToServer(providerId,fcm);
//        call.enqueue(new Callback<DataSavedModel>() {
//            @Override
//            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
//                Tools.showToast(context,response.body().getMessage().toString());
//            }
//
//            @Override
//            public void onFailure(Call<DataSavedModel> call, Throwable t) {
//                Log.d("FcmToServer",t.getMessage().toString());
//            }
//        });
//
//    }
//
//
//
//    public static void getProviderById(Context context){
//        AppSharedPreferences.init(context);
//        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
//        String providerId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
//        Call<ProviderModel> call=apiInterface.getProviderById(providerId);
//        call.enqueue(new Callback<ProviderModel>() {
//            @Override
//            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
//                if(response.body().getStatus()==Config.API_SUCCESS){
//                    AppSharedPreferences.writeProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,response.body());
//                    Intent intent=new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ProviderModel> call, Throwable t) {
//                Log.d("ProviderModel",t.getMessage().toString());
//            }
//        });
//
//    }




}
