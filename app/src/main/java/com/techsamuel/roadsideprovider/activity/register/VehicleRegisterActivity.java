package com.techsamuel.roadsideprovider.activity.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;
import com.techsamuel.roadsideprovider.tools.YearPickerDialog;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class VehicleRegisterActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText vmake;
    EditText vmodel;
    EditText vyear;
    EditText plateno;
    EditText vrExpDate;
    Calendar calendar;
    Button registerBtn;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_register);
        initToolbar();
        init();

    }

    private void initToolbar() {
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register Vehicle");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //new PaymentTask().execute("");
    }
    private void init(){
        vmake=findViewById(R.id.vmake);
        vmodel=findViewById(R.id.vmodel);
        plateno=findViewById(R.id.plateNumber);
        vyear=findViewById(R.id.vyear);
        vrExpDate=findViewById(R.id.exp_date);
        registerBtn=findViewById(R.id.registerBtn);
        vyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearPickerDialog newFragment = new YearPickerDialog(vyear);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });
        vrExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateOrYear(false,vrExpDate);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVehicle();
            }
        });
    }

    private void registerVehicle(){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.registerVehicle(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,vmake.getText().toString(),
                vmodel.getText().toString(),plateno.getText().toString(),vyear.getText().toString(),vrExpDate.getText().toString()
                );
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, retrofit2.Response<DataSavedModel> response) {
                Log.d("VehicleRegisterActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Tools.showToast(VehicleRegisterActivity.this,"Vehicle Registered");
                    VehicleRegisterActivity.this.finish();
                }else{
                    Tools.showToast(VehicleRegisterActivity.this,response.body().getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("VehicleRegisterActivity",t.getMessage().toString());

            }
        });
    }




    private void selectDateOrYear(boolean isOnlyYear,EditText editText){
        calendar = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String dateFormat="";
                    if(isOnlyYear){
                        dateFormat="YYYY";
                    }else{
                        dateFormat="MM/dd/yyyy";
                    }
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormat, Locale.US);
                    editText.setText(simpleDateFormat.format(calendar.getTime()));
                }

            };

            new DatePickerDialog(VehicleRegisterActivity.this, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





}