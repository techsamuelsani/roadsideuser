package com.techsamuel.roadsideprovider.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.CategoryAndSubcategoryModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

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
    String userId;


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
         init();
        return view;
    }

    private void init() {
        priceStartingForm.setText(data.getBasePrice()+" "+settingsModel.getData().getAppCurrency());
        priceKilometers.setText(data.getPricePerKm()+" "+settingsModel.getData().getAppCurrency());
        priceMinutes.setText(data.getPricePerMinute()+" "+settingsModel.getData().getAppCurrency());

        radioPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioDropoff.isChecked()){
                    radioDropoff.setChecked(false);
                }
            }
        });
        radioDropoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioPickup.isChecked()){
                    radioPickup.setChecked(false);
                }
            }
        });
        radioCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioCard.isChecked()){
                    radioCard.setChecked(false);
                }
            }
        });
        radioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioCash.isChecked()){
                    radioCash.setChecked(false);
                }
            }
        });

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fetchNearestProvider(userLocation,data.getServiceId().toString());

    }

    private void fetchNearestProvider(LatLng userLocation,String service_id) {
        progressBar.setVisibility(View.VISIBLE);
        lyt_order.setVisibility(View.GONE);
        lyt_no_provider.setVisibility(View.GONE);
        //Tools.showToast(getContext(),userLocation.toString());
        getNearestProvider(userLocation,service_id);

    }

    private void getNearestProvider(LatLng lastLocation, String service_id){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getNearestProvider(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,
                String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()),service_id);
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                assert response.body() != null;
                if (response.body().getSize() > 0) {
                    assignOrderToAProvider(response.body());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        lyt_order.setVisibility(View.GONE);
                        lyt_no_provider.setVisibility(View.VISIBLE);
                    }
            }
            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("AllProvider",t.getMessage().toString());
                progressBar.setVisibility(View.GONE);
                lyt_order.setVisibility(View.GONE);
                lyt_no_provider.setVisibility(View.VISIBLE);
                //Tools.showToast(getContext(),t.getMessage());
            }
        });

    }

    private void assignOrderToAProvider(ProviderModel providerModel) {
        progressBar.setVisibility(View.GONE);
        lyt_order.setVisibility(View.VISIBLE);
        lyt_no_provider.setVisibility(View.GONE);


    }


}