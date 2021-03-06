package com.techsamuel.roadsideprovider.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.MessageAdapter;
import com.techsamuel.roadsideprovider.adapter.OrderAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.MessageItemClickListener;
import com.techsamuel.roadsideprovider.listener.OrderItemClickListener;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.model.OrderModel;
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
    BeautifulProgressDialog beautifulProgressDialog;
    LinearLayout lytNoOrder;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);
        
    }

    private void initToolbar() {
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
        init();

    }
    private void init(){
        recyclerOrder=findViewById(R.id.recyler_orders);
        lytNoOrder=findViewById(R.id.lyt_no_order);
        getAllOrders("current");
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initToolbar();
            }
        });

    }


    public void getAllOrders(String order_status_type){
        Log.d("CurrentOrdersActivity","Called");
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrdersModel> call=apiInterface.getAllOrders(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,order_status_type);
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                beautifulProgressDialog.dismiss();
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    if(response.body().getSize()>0){
                        lytNoOrder.setVisibility(View.GONE);
                        recyclerOrder.setVisibility(View.VISIBLE);
                        orderAdapter=new OrderAdapter(CurrentOrdersActivity.this, response.body(),  new OrderItemClickListener() {
                            @Override
                            public void onItemClick(OrdersModel.Datum datum) {
                                //Tools.showToast(CurrentOrdersActivity.this,datum.getId());
                                getOrderDetailsById(datum.getId());
                            }

                        });
                        recyclerOrder.setLayoutManager(new LinearLayoutManager(CurrentOrdersActivity.this));
                        recyclerOrder.setAdapter(orderAdapter);
                        Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                    }else{
                        lytNoOrder.setVisibility(View.VISIBLE);
                        recyclerOrder.setVisibility(View.GONE);
                    }

                }
            }
            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                beautifulProgressDialog.dismiss();
                Log.d("CurrentOrdersActivity",t.getMessage().toString());
                lytNoOrder.setVisibility(View.VISIBLE);
                recyclerOrder.setVisibility(View.GONE);

            }
        });

    }

    private void getOrderDetailsById(String id) {
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrderModel> call=apiInterface.getOrderDetailsById(Config.DEVICE_TYPE,Config.LANG_CODE,id);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    AppSharedPreferences.writeOrderModel(Config.SHARED_PREF_ORDER_MODEL,response.body());

                }
            }
            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.d("CurrentOrdersActivity",t.getMessage().toString());
                beautifulProgressDialog.dismiss();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolbar();
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