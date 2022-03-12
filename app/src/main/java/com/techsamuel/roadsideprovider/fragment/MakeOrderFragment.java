package com.techsamuel.roadsideprovider.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseViewModel;
import com.techsamuel.roadsideprovider.model.CategoryAndSubcategoryModel;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderRequest;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    LinearLayout lytStartingFrom;
    LinearLayout lytMinutes;
    LinearLayout lytKilometers;
    TextView priceStartingForm;
    TextView priceMinutes;
    TextView priceKilometers;
    RadioButton radioPickup;
    RadioButton radioDropoff;
    RadioButton radioCash;
    RadioButton radioCard;
    TextView approximateCost;
    EditText inputNotes;
    EditText inputPickup;
    EditText inputDropOff;
    Button btnMakeOrder;
    LatLng userLocation;
    ProgressBar progressBar;
    RelativeLayout lyt_no_provider;
    RelativeLayout lyt_order;
    RelativeLayout lyt_order_success;
    String userId;
    String providerId;


    ProviderModel.Datum choosenProvider;
    int service_id;
    String order_type;
    String payment_type;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CategoryAndSubcategoryModel data;
    SettingsModel settingsModel;

    public MakeOrderFragment(CategoryAndSubcategoryModel data) {
        this.data=data;
        settingsModel= AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        //userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");

    }
    public MakeOrderFragment() {
        // Required empty public constructor
    }

    public MakeOrderFragment(CategoryAndSubcategoryModel data, LatLng lastLocation) {
        this.userLocation=lastLocation;
        this.data=data;
        settingsModel= AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakeOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakeOrderFragment newInstance(String param1, String param2) {
        MakeOrderFragment fragment = new MakeOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_make_order, container, false);

        lytStartingFrom=view.findViewById(R.id.layout_starting_form);
        lytMinutes=view.findViewById(R.id.layout_minutes);
        lytKilometers=view.findViewById(R.id.layout_kilometers);
        priceStartingForm=view.findViewById(R.id.price_starting_form);
         priceMinutes=view.findViewById(R.id.price_minutes);
         priceKilometers=view.findViewById(R.id.price_kilometers);;
         radioPickup=view.findViewById(R.id.radio_pickup);
         radioDropoff=view.findViewById(R.id.radio_dropoff);
         inputPickup=view.findViewById(R.id.input_pickup);
         inputDropOff=view.findViewById(R.id.input_dropoff);
         radioCash=view.findViewById(R.id.radio_cash);
         radioCard=view.findViewById(R.id.radio_card);
         approximateCost=view.findViewById(R.id.aproximate_cost);
         inputNotes=view.findViewById(R.id.input_notes);
         btnMakeOrder=view.findViewById(R.id.btn_make_order);
         progressBar=view.findViewById(R.id.progress_bar);
         lyt_no_provider=view.findViewById(R.id.lyt_no_provider);
         lyt_order=view.findViewById(R.id.layout_order);
         lyt_order_success=view.findViewById(R.id.lyt_order_success);
         init();
        return view;
    }


    private void init() {
        priceStartingForm.setText(data.getBasePrice()+" "+settingsModel.getData().getAppCurrency());
        priceKilometers.setText(data.getPricePerKm()+" "+settingsModel.getData().getAppCurrency());
        priceMinutes.setText(data.getPricePerMinute()+" "+settingsModel.getData().getAppCurrency());
        service_id=data.getServiceId();

        fetchNearestProvider(userLocation);

    }

    private void fetchNearestProvider(LatLng userLocation) {
        progressBar.setVisibility(View.VISIBLE);
        lyt_order.setVisibility(View.GONE);
        lyt_no_provider.setVisibility(View.GONE);
        lyt_order_success.setVisibility(View.GONE);
        //Tools.showToast(getContext(),userLocation.toString());
        getNearestProvider(userLocation);

    }

    private void getNearestProvider(LatLng lastLocation){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getNearestProvider(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,
                String.valueOf(lastLocation.latitude), String.valueOf(lastLocation.longitude),String.valueOf(service_id));
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                assert response.body() != null;
                if (response.body().getSize() > 0) {
                    System.out.println(response.body().getDistance().toString());
                    assignOrderToAProvider(response.body());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        lyt_order.setVisibility(View.GONE);
                        lyt_no_provider.setVisibility(View.VISIBLE);
                      lyt_order_success.setVisibility(View.GONE);
                    }
            }
            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("AllProvider",t.getMessage().toString());
                progressBar.setVisibility(View.GONE);
                lyt_order.setVisibility(View.GONE);
                lyt_no_provider.setVisibility(View.VISIBLE);
                lyt_order_success.setVisibility(View.GONE);
                //Tools.showToast(getContext(),t.getMessage());
            }
        });

    }
    int i=0;
    private void assignOrderToAProvider(ProviderModel providerModel) {
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(providerModel);

        List sortedProviderIndex=Tools.sortIndex(providerModel.getDistance());
        int selectedIndex= (int) sortedProviderIndex.get(0);

        progressBar.setVisibility(View.GONE);
        lyt_order.setVisibility(View.VISIBLE);
        lyt_no_provider.setVisibility(View.GONE);
       // Tools.showToast(getContext(), String.valueOf(selectedIndex));

        choosenProvider=providerModel.getData().get(selectedIndex);


//        Gson gson2 = new Gson();
//        String jsonSt2r = gson.toJson(choosenProvider);
//        System.out.println(jsonSt2r);

       inputDropOff.setText(Tools.getAdressFromLatLong(getContext(),String.valueOf(userLocation.latitude),String.valueOf(userLocation.longitude)));
       inputPickup.setText(Tools.getAdressFromLatLong(getContext(),String.valueOf(choosenProvider.getLatitude()),String.valueOf(choosenProvider.getLongitude())));

        initView();


    }


    private void initView(){

        double totalMinutes=Tools.timeInMintues(userLocation,new LatLng(Double.valueOf(choosenProvider.getLatitude()),Double.valueOf(choosenProvider.getLongitude())));
        double totalKms=Tools.distanceInKm(userLocation,new LatLng(Double.valueOf(choosenProvider.getLatitude()),Double.valueOf(choosenProvider.getLongitude())));
        double perMinuteCost=data.getPricePerMinute();
        double perKmCost=data.getPricePerKm();
        double TotalminutesCost=perMinuteCost*totalMinutes;
        double TotalkmCost=perKmCost*totalKms;
        int approxCost1= (int) (data.getBasePrice()+TotalminutesCost+TotalkmCost);
        int approxCost2= (int) (data.getBasePrice()+TotalminutesCost+TotalkmCost+20);
        approximateCost.setText(getString(R.string.approximate_cost)+" "+approxCost1+"-"+approxCost2+" "+settingsModel.getData().getAppCurrency());

        radioPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_type=Config.ORDER_TYPE_PICKUP;
                if(radioDropoff.isChecked()){
                    radioDropoff.setChecked(false);
                }
            }
        });
        radioDropoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_type=Config.ORDER_TYPE_DELIVERY;
                if(radioPickup.isChecked()){
                    radioPickup.setChecked(false);
                }
            }
        });
        radioCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type=Config.PAYMENT_TYPE_CASH;
                if(radioCard.isChecked()){
                    radioCard.setChecked(false);
                }
            }
        });
        radioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_type=Config.PAYMENT_TYPE_CARD;
                if(radioCash.isChecked()){
                    radioCash.setChecked(false);
                }
            }
        });

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderRequestGetId();
            }
        });
    }

    private void saveOrderRequestGetId() {

        progressBar.setVisibility(View.VISIBLE);
        lyt_order.setVisibility(View.GONE);
        lyt_no_provider.setVisibility(View.GONE);
        lyt_order_success.setVisibility(View.GONE);

       //  public OrderRequest(int id, int user_id, int provider_id, int service_id, String service_name, boolean accepted, String date, double base_price, double price_per_minute, double price_per_km, double totalMinutes, double totalKms, double approx_cost1, double approx_cost2, double userLat, double userLong, double providerLat, double providerLong, String order_type, String payment_type, String notes)
        providerId=choosenProvider.getId();
        int provider_id=Integer.parseInt(providerId);
        int user_id=Integer.parseInt(userId);
        int serviceId=data.getServiceId();
        String serviceName=data.getName();
        boolean accepted=false;
        boolean rejected=false;

        //No need For Constructor
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String timeZone= TimeZone.getDefault().getDisplayName();
        String strDate = dateFormat.format(date).toString();
        double basePrice=data.getBasePrice();
        double perMinuteCost=data.getPricePerMinute();
        double perKmCost=data.getPricePerKm();
        double totalMinutes=Tools.timeInMintues(userLocation,new LatLng(Double.valueOf(choosenProvider.getLatitude()),Double.valueOf(choosenProvider.getLongitude())));
        double totalKms=Tools.distanceInKm(userLocation,new LatLng(Double.valueOf(choosenProvider.getLatitude()),Double.valueOf(choosenProvider.getLongitude())));

        //No need For Constructor
        double TotalminutesCost=perMinuteCost*totalMinutes;
        double TotalkmCost=perKmCost*totalKms;


        double approxCost1=data.getBasePrice()+TotalminutesCost+TotalkmCost;
        double approxCost2=data.getBasePrice()+TotalminutesCost+TotalkmCost+20;
        double userLat=userLocation.latitude;
        double userLong=userLocation.longitude;
        double providerLat=Double.valueOf(choosenProvider.getLatitude());
        double providerLong=Double.valueOf(choosenProvider.getLongitude());
        String orderType=order_type;
        String paymentType=payment_type;
        String notes=inputNotes.getText().toString();


        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.saveOrderRequestGetId(user_id,provider_id,serviceId,serviceName,accepted,rejected,
                strDate,timeZone,basePrice,perMinuteCost, perKmCost,totalMinutes,totalKms,approxCost1,approxCost2,
                userLat,userLong,providerLat,providerLong,orderType,paymentType,notes);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                if(response.body().getError()==false){
                    System.out.println("Order id"+response.body().getId());
                    int orderId=response.body().getId();
                    OrderRequest orderRequest=new OrderRequest(orderId,user_id,provider_id,serviceId,serviceName,accepted,rejected,strDate,timeZone,
                            basePrice,perMinuteCost,perKmCost,totalMinutes,totalKms,approxCost1,approxCost2,userLat,userLong,providerLat,
                            providerLong,payment_type,orderType,notes);
                    makeOrder(orderRequest);
                }else{
                    Tools.showToast(getContext(),response.body().getMessage().toString());
                }

            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                System.out.println(t.getMessage());
                Tools.showToast(getContext(),"Something wrong, try again later");

            }
        });

    }


    private void makeOrder(OrderRequest orderRequest) {
        DatabaseViewModel databaseViewModel=new DatabaseViewModel();
        databaseViewModel.addUserDatabase(orderRequest);
        databaseViewModel.successAddOrderRequest.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    System.out.println("Data added successfully");
                    progressBar.setVisibility(View.GONE);
                    lyt_order.setVisibility(View.GONE);
                    lyt_no_provider.setVisibility(View.GONE);
                    lyt_order_success.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getContext(), "ERROR WHILE ADDING DATA IN DATABASE.", Toast.LENGTH_SHORT).show();
                    System.out.println("ERROR WHILE ADDING DATA IN DATABASE.");
                    progressBar.setVisibility(View.GONE);
                    lyt_order.setVisibility(View.VISIBLE);
                    lyt_no_provider.setVisibility(View.GONE);
                    lyt_order_success.setVisibility(View.GONE);
                }
            }
        });
    }




}