package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.DialogServiceAdapter;
import com.techsamuel.roadsideprovider.adapter.ServiceAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.OnItemClickListener;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOrderActivity extends AppCompatActivity {
    //Toolbar toolbar;
    String userId;
    String providerId;
    EditText selectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        initToolbar();
        init();
    }

    private void initToolbar() {
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        (findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void init(){
        Intent intent=getIntent();
        providerId=intent.getStringExtra("provider_id");
        Tools.showToast(MakeOrderActivity.this,providerId);
        selectService=findViewById(R.id.select_service);
        selectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog(){
        final Dialog dialog = new Dialog(MakeOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_select_service);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        RecyclerView recyclerView=dialog.findViewById(R.id.recycler_service);
        getProviderSerivces(providerId,recyclerView,dialog);

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void getProviderSerivces(String providerId, RecyclerView recyclerView,Dialog dialog) {
        Log.d("MainActivity","Called");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ServiceModel> call=apiInterface.getServicesByProviderId(providerId);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    DialogServiceAdapter serviceAdapter=new DialogServiceAdapter(MakeOrderActivity.this, response.body(), new OnItemClickListener() {
                        @Override
                        public void onItemClick(ServiceModel.Datum item) {
                            Tools.showToast(MakeOrderActivity.this,item.getName());
                        }
                    });
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(MakeOrderActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(serviceAdapter);
                    dialog.show();
                    Log.d("MainActivity",response.body().getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        MakeOrderActivity.this.finish();
    }
}