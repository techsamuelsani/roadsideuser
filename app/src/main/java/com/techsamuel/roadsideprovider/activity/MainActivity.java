package com.techsamuel.roadsideprovider.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.BuildConfig;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.UserLoginActivity;
import com.techsamuel.roadsideprovider.activity.register.VehicleRegisterActivity;
import com.techsamuel.roadsideprovider.adapter.PageAdapter;
import com.techsamuel.roadsideprovider.adapter.ServiceAdapter;
import com.techsamuel.roadsideprovider.adapter.ServiceCategoryAdapter;
import com.techsamuel.roadsideprovider.adapter.ServiceSubCategoryAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseReferenceName;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseViewModel;
import com.techsamuel.roadsideprovider.fragment.MakeOrderFragment;
import com.techsamuel.roadsideprovider.fragment.SubCategoryFragment;
import com.techsamuel.roadsideprovider.helper.DirectionsJSONParser;
import com.techsamuel.roadsideprovider.listener.PageItemClickListener;
import com.techsamuel.roadsideprovider.listener.ServiceCategoryItemClickListener;
import com.techsamuel.roadsideprovider.listener.ServiceItemClickListener;
import com.techsamuel.roadsideprovider.listener.ServiceSubCategoryItemClickListener;
import com.techsamuel.roadsideprovider.model.CategoryAndSubcategoryModel;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderRequest;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ProviderLocation;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceCategoryModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    int REQUEST_CODE_AUTOCOMPLETE=2127;
    LatLng selectedLocation;
    LatLng lastLocation=null;
    public static UserModel userModel;
    public static SettingsModel settingsModel;
    FloatingActionButton floatingActionButton;
    boolean isActivated;
    TextView providerName;
    ImageView providerImage;
    LinearLayout lytWallet;
    LinearLayout lytCurrentOrders;
    LinearLayout lytPreviousOrders;
    LinearLayout lytMessage;
    LinearLayout lytLanguages;
    LinearLayout lytNotification;
//    LinearLayout lytAbout;
//    LinearLayout lytTerms;
//    LinearLayout lytFaq;
//    LinearLayout lytContact;
    LinearLayout lytRate;
    LinearLayout lytExit;
    TextView balance;
    ImageButton logout;
    TextView currency;
    LinearLayout lytProfile;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerService;
    ServiceAdapter serviceAdapter;
    ServiceCategoryAdapter serviceCategoryAdapter;
    LinearLayout lytregisterVehicle;
    LinearLayout lytSearch;
    String userId;
    TextView userPhone;
    private boolean isVehicleRegistered=false;
    BeautifulProgressDialog beautifulProgressDialog;
    EditText etSearch;
    RecyclerView recyclerPage;
    FrameLayout fragmentContainer;

    ImageView serviceBack;
    TextView service_sheet_title;

    GoogleMap mMap;
    Marker currentLocationMarker=null;
    FusedLocationProviderClient fusedLocationClient;

    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    PopUpOrderAcceptedSheet popUpOrderAcceptedSheet;
    ImageView btnShow;
    boolean hasActiveOrder=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSharedPreferences.init(this);
        userModel=AppSharedPreferences.readUserModel(Config.SHARED_PREF_USER_MODEL,"");
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        if(userModel.equals("")||settingsModel.equals("")){
            MainActivity.this.finish();
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init(false);
        initView();
        initSideMenuItem();
        getOrderStatus();
    }

    private void initSideMenuItem(){
        recyclerPage=findViewById(R.id.recycler_page);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<PageModel> call=apiInterface.getPagesByDevicyType(Config.DEVICE_TYPE,Config.LANG_CODE);
        call.enqueue(new Callback<PageModel>() {
            @Override
            public void onResponse(Call<PageModel> call, Response<PageModel> response) {
                //Tools.showToast(MainActivity.this,response.body().getMessage().toString());
                Log.d("PageAdapter",response.body().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    recyclerPage.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    PageAdapter pageAdapter=new PageAdapter(MainActivity.this, response.body(), new PageItemClickListener() {
                        @Override
                        public void onItemClick(PageModel.Datum item) {
                            Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
                            intent.putExtra("title",item.getPageName());
                            intent.putExtra("url",item.getPageUrl());
                            intent.putExtra("content",item.getPageContent());
                            startActivity(intent);

                        }
                    });
                    recyclerPage.setAdapter(pageAdapter);
                }else{
                    Log.d("MainActivity","Failed to update device information");
                }
            }

            @Override
            public void onFailure(Call<PageModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });

    }



    @SuppressLint("MissingPermission")
    private void init(boolean locationBtn){
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lastLocation=new LatLng(location.getLatitude(),location.getLongitude());
                            Log.d("DeviceLocation",location.toString());
                            // Logic to handle location object

                            if(mMap!=null && !locationBtn){
                                //setAllProviderStoreLocation(lastLocation,"0");
                                updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,""),
                                        Config.LANG_CODE,location.getLatitude(),
                                        location.getLongitude(),AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                                        AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());

                            }else if(locationBtn){
                                //setAllProviderStoreLocation(lastLocation,"0");
                                LatLng currentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                                currentLocationMarker=mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
                            }

                        }else{
                            updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,""),
                                    Config.LANG_CODE,0, 0,
                                    AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                                    AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }
                    }
                });


    }

    void initView(){
        initNavigationMenu();
        initBottomSheet();
        beautifulProgressDialog = new BeautifulProgressDialog(MainActivity.this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);

        floatingActionButton=findViewById(R.id.fab);
        providerName=findViewById(R.id.provider_name);
        providerImage=findViewById(R.id.profile_image);
        lytWallet=findViewById(R.id.lyt_wallet);
        lytCurrentOrders=findViewById(R.id.lyt_current_orders);
        lytPreviousOrders=findViewById(R.id.lyt_previous_orders);
        lytMessage=findViewById(R.id.lyt_message);
        lytLanguages=findViewById(R.id.lyt_languages);
        lytNotification=findViewById(R.id.lyt_notifications);
//        lytAbout=findViewById(R.id.lyt_about);
//        lytTerms=findViewById(R.id.lyt_terms);
//        lytFaq=findViewById(R.id.lyt_faq);
//        lytContact=findViewById(R.id.lyt_contact);
        lytRate=findViewById(R.id.lyt_rate);
        lytExit=findViewById(R.id.lyt_exit);
        balance=findViewById(R.id.balance);
        logout=findViewById(R.id.logout);
        lytProfile=findViewById(R.id.lyt_profile);
        lytregisterVehicle=findViewById(R.id.register_vehicle);
        lytSearch=findViewById(R.id.lyt_search);
        userPhone=findViewById(R.id.userPhone);
        currency=findViewById(R.id.currency);
        etSearch=findViewById(R.id.et_search);
        serviceBack=findViewById(R.id.service_back);
//        btnShow=findViewById(R.id.btn_show);
//
//        btnShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popUpOrderAcceptedSheet.show();
//            }
//        });

        currency.setText(settingsModel.getData().getAppCurrency());

        providerName.setText(userModel.getData().getName());
        Glide.with(MainActivity.this).load(Config.BASE_URL+userModel.getData().getUserPhoto()).into(providerImage);
        userPhone.setText(userModel.getData().getPhone());
        Config.balance=Double.valueOf(userModel.getData().getWallet());
        balance.setText(userModel.getData().getWallet());

        lytregisterVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, VehicleRegisterActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                init(true);
            }
        });
        lytExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApp();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutApp();
            }
        });
        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        lytWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,WalletActivity.class);
                startActivity(intent);
            }
        });
        lytMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });
        lytLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent);
            }
        });
        lytCurrentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CurrentOrdersActivity.class);
                startActivity(intent);
            }
        });
        lytPreviousOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lytRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openMarketForRatings(MainActivity.this);
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void updateDeviceInformationToServer(String deviceType,String userType,String userId, String langCode, double latitude, double longitude,
                                                 String fcm, String device_id,String firebase_id) {
        String lat=String.valueOf(latitude);
        String lon=String.valueOf(longitude);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.updateDeviceInformationToServer(deviceType,userType,userId,langCode,lat,lon,fcm,device_id,firebase_id);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                //Tools.showToast(MainActivity.this,response.body().getMessage().toString());
                Log.d("MainActivity",response.body().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Log.d("MainActivity","Device information updated successfully");
                }else{
                    Log.d("MainActivity","Failed to update device information");
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });
    }

    private void initBottomSheet(){
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        recyclerService=findViewById(R.id.recycler_service);
        fragmentContainer=findViewById(R.id.fragment_container);
        service_sheet_title=findViewById(R.id.service_sheet_title);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        fetchAllServices();
        getRegisteredVehicle();

    }

    public void getRegisteredVehicle(){
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<VehicleModel> call=apiInterface.getRegisteredVehicle(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId);
        call.enqueue(new Callback<VehicleModel>() {
            @Override
            public void onResponse(Call<VehicleModel> call, Response<VehicleModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    lytSearch.setVisibility(View.VISIBLE);
                    lytregisterVehicle.setVisibility(View.GONE);
                     isVehicleRegistered = true;
                    Log.d("MainActivity",response.body().getMessage().toString());
                }else{
                    lytSearch.setVisibility(View.GONE);
                    lytregisterVehicle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<VehicleModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });
    }



    private void getOrderStatus() {
        DatabaseViewModel databaseViewModel = new DatabaseViewModel();
        databaseViewModel.fetchOrderRequest();
        databaseViewModel.fetchedOrderRequest.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderRequest orderRequest = snapshot.getValue(OrderRequest.class);
                    if (orderRequest.getUser_id() == Integer.parseInt(userModel.getData().getId()) && !orderRequest.isMarkAsCompleted()) {

                        if (orderRequest.isAccepted() && !orderRequest.isRejected() && !orderRequest.isMarkAsArrived() && !orderRequest.isMarkAsCompleted()) {
                            //Order is Accepted
                            Config.ORDER_ID = orderRequest.getId();
                            getNewProviderLocation(orderRequest.getId(), orderRequest);
                            if (popUpOrderAcceptedSheet == null) {
                                popUpOrderAcceptedSheet = new PopUpOrderAcceptedSheet(MainActivity.this, orderRequest);
                                popUpOrderAcceptedSheet.show();
                            }

                        }
                        if(!orderRequest.isRejected()&&!orderRequest.isMarkAsCompleted()){
                            hasActiveOrder=true;
                        }

                        if(orderRequest.isVehicleFullPhotoSaved()&&!orderRequest.isOrderPaid()){
                            Tools.showToast(MainActivity.this,"Invoice received by user");
                            PopUpInvoiceSheet popUpInvoiceSheet=new PopUpInvoiceSheet(MainActivity.this,orderRequest);
                            popUpInvoiceSheet.show();

                        }

                        if(orderRequest.isOrderPaid()&&!orderRequest.isCashReceived()){

                            Tools.showToast(MainActivity.this,"Waiting for confirmation from provider");
                        }
                        if(orderRequest.isCashReceived()&&!orderRequest.isMarkAsCompleted()){
                            PopUpRatingReviewsSheet popUpRatingReviewsSheet=new PopUpRatingReviewsSheet(MainActivity.this,orderRequest);
                            popUpRatingReviewsSheet.show();
                            Tools.showToast(MainActivity.this,"Waiting for mark as completed and rating and reviews");
                        }



                    }
                }
            }
        });
    }

    class PopUpRatingReviewsSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;

        Context context;
        OrderRequest orderRequest;
        LinearLayout payInvoice;


        public PopUpRatingReviewsSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.popup_rating_reviews_sheet, null);
            payInvoice=view.findViewById(R.id.pay_invoice);
            payInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.MARK_AS_COMPLETED,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            mBottomSheetDialog.cancel();
                            hasActiveOrder=false;
                        }
                    });
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
            //  btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }

    class PopUpInvoiceSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;

        Context context;
        OrderRequest orderRequest;
        LinearLayout payInvoice;


        public PopUpInvoiceSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.popup_invoice_sheet, null);
            payInvoice=view.findViewById(R.id.pay_invoice);
            payInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.IS_ORDER_PAID,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            mBottomSheetDialog.cancel();
                        }
                    });
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
            //  btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }


    class PopUpOrderAcceptedSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;

        Button callTheUser;

        Context context;
        OrderRequest orderRequest;
        ImageView btnHide;


        public PopUpOrderAcceptedSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.order_accept_sheet, null);

            callTheUser = view.findViewById(R.id.call_the_use);

            btnHide=view.findViewById(R.id.btn_hide);


            callTheUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            btnHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.hide();
                   // btnShow.setVisibility(View.VISIBLE);
                }
            });


            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
          //  btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }

    private void getNewProviderLocation(int orderId,OrderRequest orderRequest){
        DatabaseViewModel databaseViewModel=new DatabaseViewModel();
        databaseViewModel.fetchProviderLocation(orderId);
        databaseViewModel.fetchedProviderLocation.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                ProviderLocation providerLocation=dataSnapshot.getValue(ProviderLocation.class);
                drawArrivalRoute(providerLocation,orderRequest);

            }
        });
    }
    Marker providerMarkerRoute;
    Marker userMarkerRoute;
    List<Marker> markersRoute;
    boolean isDrawArrivalRoute=false;

    private void drawArrivalRoute(ProviderLocation providerLocation,OrderRequest orderRequest) {
        // Getting URL to the Google Directions API
        if(providerLocation==null){
            mOrigin=new LatLng(orderRequest.getProviderLat(),orderRequest.getProviderLong());
        }else {
            mOrigin = new LatLng(providerLocation.getProviderLat(), providerLocation.getProviderLong());
        }

        mDestination=new LatLng(orderRequest.getUserLat(),orderRequest.getUserLong());

        if(providerMarkerRoute!=null){
            providerMarkerRoute.remove();
        }
        if(userMarkerRoute!=null){
            userMarkerRoute.remove();
        }

        providerMarkerRoute=mMap.addMarker(new MarkerOptions().position(mOrigin).title("Marker in current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_car)));
        userMarkerRoute=mMap.addMarker(new MarkerOptions().position(mDestination).title("Marker in User location").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car)));

        if(!isDrawArrivalRoute){
            if(markersRoute==null){
                markersRoute=new ArrayList<>();
            }

            if(markersRoute.size()>0){
                markersRoute.clear();
            }
            markersRoute.add(providerMarkerRoute);
            markersRoute.add(userMarkerRoute);
            autoZoomMap(markersRoute);


            String url = getDirectionsUrl(mOrigin, mDestination);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            Log.d("MainActivity",url);
            downloadTask.execute(url);
            isDrawArrivalRoute=true;
        }


    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + BuildConfig.MAPS_API_KEY;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }


    private void autoZoomMap(List<Marker> markers){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        if(markers.size()>0){
            System.out.println(markers.size());
            for (Marker m : markers) {
                b.include(m.getPosition());
            }
            LatLngBounds bounds = b.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,200);
            mMap.animateCamera(cu);
        }
    }

//    public void fetchAllServices(){
//        Log.d("MainActivity","Called");
//        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
//        Call<ServiceModel> call=apiInterface.getAllServices(Config.DEVICE_TYPE,Config.LANG_CODE);
//        call.enqueue(new Callback<ServiceModel>() {
//            @Override
//            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
//                Log.d("MainActivity",response.body().getMessage().toString());
//                if(response.body().getStatus()== Config.API_SUCCESS){
//                    serviceAdapter=new ServiceAdapter(MainActivity.this, response.body(), false, new ServiceItemClickListener() {
//                        @Override
//                        public void onItemClick(ServiceModel.Datum item) {
//                            setAllProviderStoreLocation(mapboxMap,lastLocation,item.getId());
//                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                            Tools.showToast(MainActivity.this,"Searching... "+item.getName()+" service provider in your location");
//                        }
//                    });
//                    recyclerService.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                    recyclerService.setAdapter(serviceAdapter);
//                    Log.d("MainActivity",response.body().getMessage().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServiceModel> call, Throwable t) {
//                Log.d("MainActivity",t.getMessage().toString());
//
//            }
//        });
//
//    }
    private int level=0;
    public void fetchAllServices(){

        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ServiceCategoryModel> call=apiInterface.getServiceCategory();


        call.enqueue(new Callback<ServiceCategoryModel>() {
            @Override
            public void onResponse(Call<ServiceCategoryModel> call, Response<ServiceCategoryModel> response) {
                serviceCategoryAdapter=new ServiceCategoryAdapter(MainActivity.this, response.body(), false, new ServiceCategoryItemClickListener() {
                    @Override
                    public void onItemClick(List<ServiceCategoryModel.Subcategory> subcategories,ServiceCategoryModel.Category category) {

                        ServiceSubCategoryAdapter serviceSubCategoryAdapter=new ServiceSubCategoryAdapter(MainActivity.this, subcategories, false, new ServiceSubCategoryItemClickListener() {
                            @Override
                            public void onItemClick(ServiceCategoryModel.Subcategory subcategory) {
                                CategoryAndSubcategoryModel categoryAndSubcategoryModel=new CategoryAndSubcategoryModel(
                                        subcategory.getName(),
                                        subcategory.getParent(),
                                        subcategory.getServiceId(),
                                        subcategory.getImage(),
                                        subcategory.getBasePrice(),
                                        subcategory.getPriceApplicableOnKm(),
                                        subcategory.getPriceApplicableOnMinute(),
                                        subcategory.getPricePerKm(),
                                        subcategory.getPricePerMinute(),
                                        subcategory.getDescription()
                                );
                                    initiateOrderFragment(categoryAndSubcategoryModel);
                                    level=2;
                            }
                        });

                        recyclerService.setVisibility(View.GONE);

                        if(category.getIsSubcategoryAvailable()){
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, new SubCategoryFragment(serviceSubCategoryAdapter));
                            ft.addToBackStack(null);
                            ft.commit();
                            serviceBack.setVisibility(View.VISIBLE);
                            service_sheet_title.setText(category.getName());
                            level=1;
                        }else{
                            CategoryAndSubcategoryModel categoryAndSubcategoryModel=new CategoryAndSubcategoryModel(
                                    category.getName(),
                                    category.getParent(),
                                    category.getServiceId(),
                                    category.getImage(),
                                    category.getBasePrice(),
                                    category.getPriceApplicableOnKm(),
                                    category.getPriceApplicableOnMinute(),
                                    category.getPricePerKm(),
                                    category.getPricePerMinute(),
                                    category.getDescription()
                            );
                            initiateOrderFragment(categoryAndSubcategoryModel);

                        }

                        serviceBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSupportFragmentManager().popBackStack();
                                if(level!=0){
                                    level--;
                                }
                                //serviceBack.setVisibility(View.GONE);
                                if(level==0){
                                    service_sheet_title.setText(R.string.services);
                                    serviceBack.setVisibility(View.GONE);
                                    recyclerService.setVisibility(View.VISIBLE);
                                }else if(level==1){
                                    service_sheet_title.setText(category.getName());
                                    serviceBack.setVisibility(View.VISIBLE);
                                }else if(level==2){
                                    service_sheet_title.setText("Purchase this service");
                                    serviceBack.setVisibility(View.VISIBLE);
                                }

                            }
                        });

//                        ServiceSubCategoryAdapter serviceSubCategoryAdapter=new ServiceSubCategoryAdapter(MainActivity.this, subcategories, false, new ServiceSubCategoryItemClickListener() {
//                            @Override
//                            public void onItemClick(ServiceCategoryModel.Subcategory subcategory) {
//
//                            }
//                        });
                    }
                });
                    recyclerService.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerService.setAdapter(serviceCategoryAdapter);


            }

            @Override
            public void onFailure(Call<ServiceCategoryModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });

    }

    private void initiateOrderFragment(CategoryAndSubcategoryModel data){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MakeOrderFragment(data,lastLocation,hasActiveOrder));
        ft.addToBackStack(null);
        ft.commit();
        serviceBack.setVisibility(View.VISIBLE);
        service_sheet_title.setText("Purchase this service");
    }


    private void logoutApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.logout_app)
                .setMessage(R.string.logout_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void exitApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.exit_app)
                .setMessage(R.string.exit_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void initNavigationMenu() {
        ImageView drawerIcon=findViewById(R.id.drawer_icon);

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        // open drawer at start

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        double userLat=Double.valueOf(userModel.getData().getLatitude());
        double userLong=Double.valueOf(userModel.getData().getLongitude());
        LatLng userLocation = new LatLng(userLat, userLong);
        Marker userLocationMarker=mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker in User Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12.0f));

    }




    private void setAllProviderStoreLocation(LatLng lastLocation,String service_id){
        mMap.clear();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getAllProviders(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,
                String.valueOf(lastLocation.latitude),String.valueOf(lastLocation.longitude),service_id);
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                CircleOptions circleOptions = new CircleOptions()
                        .center(lastLocation)
                        .radius(Double.parseDouble(settingsModel.getData().getRadius())*1000).fillColor(0x1A0000FF).strokeColor(Color.TRANSPARENT); // In meters
                Circle circle = mMap.addCircle(circleOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        circleOptions.getCenter(), getZoomLevel(circle)));

                if(response.body().getStatus()==Config.API_SUCCESS){

                    if(response.body().getSize()>0){
                        Tools.showToast(MainActivity.this,response.body().getSize()+" Provider found within your area Zoom-In for more details");
                    }else{
                        Tools.showToast(MainActivity.this,"No provider found within your area, try to search in another location");
                    }
                    for(int i=0;i<response.body().getData().size();i++){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(response.body().getData().get(i).getLatitude()),Double.valueOf(response.body().getData().get(i).getLongitude())))
                                .title(response.body().getData().get(i).getStoreName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker)))
                                .setSnippet("Store ID: "+response.body().getData().get(i).getId());

                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            String providerId=marker.getSnippet().replace("Store ID: ","");
                            beautifulProgressDialog.show();
                            showProviderInfo(providerId);
                            return false;
                        }
                    });

                }else{
                    Tools.showToast(MainActivity.this,"Failed: No provider found within your area, try to search in another location");
                }
            }
            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());
                Tools.showToast(MainActivity.this,"Error: No provider found within your area, try to search in another location");

            }
        });


    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel+1;
    }


    private void showProviderInfo(String providerId) {
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getProviderById(providerId);
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.dialog_provider_info);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    TextView storeName=(dialog).findViewById(R.id.store_name);
                    TextView storeDetails=(dialog).findViewById(R.id.store_details);
                    TextView storeLocation=(dialog).findViewById(R.id.store_location);

                    ImageView storeLogo=(dialog).findViewById(R.id.store_logo);
                    Button makeOrder=(dialog).findViewById(R.id.make_order);
                    Button moreInfo=(dialog).findViewById(R.id.more_info);
                    Button reportProvider=(dialog).findViewById(R.id.report_provider);
                    RecyclerView recyclerView=(dialog).findViewById(R.id.recycler_service);
                    getSelectedProviderServices(providerId,recyclerView);

                    makeOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isVehicleRegistered){

                            }else{
                                Tools.showToast(MainActivity.this,"Please register a vehicle in order to make and order");
                            }
                        }
                    });

                    String address=Tools.getAdressFromLatLong(MainActivity.this,response.body().getData().get(0).getLatitude(),response.body().getData().get(0).getLongitude());

                    storeName.setText(response.body().getData().get(0).getStoreName());
                    storeDetails.setText(response.body().getData().get(0).getStoreDetails());
                    storeLocation.setText(address);
                    Glide.with(MainActivity.this).load(Config.BASE_URL+response.body().getData().get(0).getStorePhoto()).into(storeLogo);

                    (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
                beautifulProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());
                beautifulProgressDialog.dismiss();

            }
        });

    }

    private void getSelectedProviderServices(String providerId, RecyclerView recyclerView) {
        Log.d("MainActivity","Called");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ServiceModel> call=apiInterface.getServicesByProviderId(providerId);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    ServiceAdapter serviceAdapter=new ServiceAdapter(MainActivity.this, response.body(), true, new ServiceItemClickListener() {
                        @Override
                        public void onItemClick(ServiceModel.Datum item) {
                            Tools.showToast(MainActivity.this,item.getName());
                        }
                    });
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(serviceAdapter);
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
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRegisteredVehicle();
        balance.setText(String.valueOf(Config.balance));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}