package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.PageAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.PageItemClickListener;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ServiceCategoryModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ServiceCategoryModel> call=apiInterface.getServiceCategory();
        call.enqueue(new Callback<ServiceCategoryModel>() {
            @Override
            public void onResponse(Call<ServiceCategoryModel> call, Response<ServiceCategoryModel> response) {
                //Tools.showToast(MainActivity.this,response.body().getMessage().toString());
                System.out.println(response.body().getCategory().toString());

            }

            @Override
            public void onFailure(Call<ServiceCategoryModel> call, Throwable t) {
                Log.d("TestActivity",t.getMessage().toString());

            }
        });
    }
}