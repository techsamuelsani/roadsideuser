package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.MessageAdapter;
import com.techsamuel.roadsideprovider.adapter.OrderAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.MessageItemClickListener;
import com.techsamuel.roadsideprovider.listener.OrderItemClickListener;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.model.OrdersModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentOrdersActivity extends AppCompatActivity {

    String userId;
    Toolbar toolbar;
    RecyclerView recyclerOrder;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        initToolbar();
        init();
    }

    private void initToolbar() {
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    private void init(){
        recyclerOrder=findViewById(R.id.recyler_orders);
        getAllOrders("current");

    }

    public void getAllOrders(String order_status_type){
        Log.d("CurrentOrdersActivity","Called");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrdersModel> call=apiInterface.getAllOrders(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,order_status_type);
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    orderAdapter=new OrderAdapter(CurrentOrdersActivity.this, response.body(),  new OrderItemClickListener() {
                        @Override
                        public void onItemClick(OrdersModel.Datum datum) {
                            Tools.showToast(CurrentOrdersActivity.this,datum.getId());
                        }

                    });
                    recyclerOrder.setLayoutManager(new LinearLayoutManager(CurrentOrdersActivity.this));
                    recyclerOrder.setAdapter(orderAdapter);
                    Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                }
            }
            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                Log.d("CurrentOrdersActivity",t.getMessage().toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        CurrentOrdersActivity.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}