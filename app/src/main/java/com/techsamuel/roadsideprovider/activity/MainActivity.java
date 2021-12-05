package com.techsamuel.roadsideprovider.activity;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.UserLoginActivity;
import com.techsamuel.roadsideprovider.activity.register.VehicleRegisterActivity;
import com.techsamuel.roadsideprovider.adapter.ServiceAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.OnItemClickListener;
import com.techsamuel.roadsideprovider.listener.ServiceItemClickListener;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener{

    int REQUEST_CODE_AUTOCOMPLETE=2127;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    LocationEngineCallback<LocationEngineResult> locationEngineCallback;
    Marker clickOnMapMaker;
    Marker providerStoreMarker;
    LatLng selectedLocation;
    LatLng lastLocation=null;
    public static UserModel userModel;
    public static SettingsModel settingsModel;
    FloatingActionButton floatingActionButton;
    Style mapboxStyle;
    boolean isActivated;
    TextView providerName;
    ImageView providerImage;
    LinearLayout lytWallet;
    LinearLayout lytCurrentOrders;
    LinearLayout lytPreviousOrders;
    LinearLayout lytMessage;
    LinearLayout lytLanguages;
    LinearLayout lytNotification;
    LinearLayout lytAbout;
    LinearLayout lytTerms;
    LinearLayout lytFaq;
    LinearLayout lytContact;
    LinearLayout lytRate;
    LinearLayout lytExit;
    TextView balance;
    ImageButton logout;
    TextView currency;
    LinearLayout lytProfile;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerService;
    ServiceAdapter serviceAdapter;
    LinearLayout lytregisterVehicle;
    LinearLayout lytSearch;
    String userId;
    TextView userPhone;
    private boolean isVehicleRegistered=false;
    BeautifulProgressDialog beautifulProgressDialog;
    EditText etSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userModel=AppSharedPreferences.readUserModel(Config.SHARED_PREF_USER_MODEL,"");
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        if(userModel.equals("")||settingsModel.equals("")){
            MainActivity.this.finish();
        }
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //Tools.hideSystemUI(this);
        init();
    }



    private void init(){
       // CommonRequests.updateFcmToServer(this);
        AppSharedPreferences.init(this);
        userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        locationEngineCallback=new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult locationEngineResult) {
                lastLocation=new LatLng(locationEngineResult.getLastLocation().getLatitude(),locationEngineResult.getLastLocation().getLongitude());
                updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,""),
                        Config.LANG_CODE,locationEngineResult.getLastLocation().getLatitude(),
                        locationEngineResult.getLastLocation().getLongitude(),AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                        AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());

            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Tools.showToast(MainActivity.this,"Location Updates Failed, please turn on gps");
                updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,""),
                        Config.LANG_CODE,0, 0,
                        AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                        AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());

            }
        };
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
        lytNotification=findViewById(R.id.lyt_notifications);
        lytAbout=findViewById(R.id.lyt_about);
        lytTerms=findViewById(R.id.lyt_terms);
        lytFaq=findViewById(R.id.lyt_faq);
        lytContact=findViewById(R.id.lyt_contact);
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

        currency.setText(settingsModel.getData().getAppCurrency());

        providerName.setText(userModel.getData().getName());
        Glide.with(MainActivity.this).load(Config.BASE_URL+userModel.getData().getUserPhoto()).into(providerImage);
        userPhone.setText(userModel.getData().getPhone());
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
                enableLocationComponent(mapboxStyle);
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
                Intent intent=new Intent(MainActivity.this,PreviousOrderActivity.class);
                startActivity(intent);
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
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

    public void fetchAllServices(){
        Log.d("MainActivity","Called");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ServiceModel> call=apiInterface.getAllServices(Config.DEVICE_TYPE,Config.LANG_CODE);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    serviceAdapter=new ServiceAdapter(MainActivity.this, response.body(), false, new ServiceItemClickListener() {
                        @Override
                        public void onItemClick(ServiceModel.Datum item) {
                            Tools.showToast(MainActivity.this,item.getName());
                        }
                    });
                    recyclerService.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerService.setAdapter(serviceAdapter);
                    Log.d("MainActivity",response.body().getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });

    }

    private void logoutApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.logout_app)
                .setMessage(R.string.logout_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppSharedPreferences.init(MainActivity.this);
                        AppSharedPreferences.clear();
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




    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapboxMap.addPolygon(generatePerimeter(
                        new LatLng(Double.valueOf(userModel.getData().getLatitude()), Double.valueOf(userModel.getData().getLongitude())),
                        Double.valueOf(settingsModel.getData().getRadius()),
                        64));
                enableLocationComponent(style);
                mapboxStyle=style;

            }
        });
    }

    private PolygonOptions generatePerimeter(LatLng centerCoordinates, double radiusInKilometers, int numberOfSides) {
        List<LatLng> positions = new ArrayList<>();
        double distanceX = radiusInKilometers / (111.319 * Math.cos(centerCoordinates.getLatitude() * Math.PI / 180));
        double distanceY = radiusInKilometers / 110.574;

        double slice = (2 * Math.PI) / numberOfSides;

        double theta;
        double x;
        double y;
        LatLng position;
        for (int i = 0; i < numberOfSides; ++i) {
            theta = i * slice;
            x = distanceX * Math.cos(theta);
            y = distanceY * Math.sin(theta);

            position = new LatLng(centerCoordinates.getLatitude() + y,
                    centerCoordinates.getLongitude() + x);
            positions.add(position);
        }
        return new PolygonOptions()
                .addAll(positions)
                .fillColor(Color.BLUE)
                .alpha(0.1f);
    }

    private void setAllProviderStoreLocation(MapboxMap mapboxMap, LatLng lastLocation){

        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ProviderModel> call=apiInterface.getAllProviders(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId,String.valueOf(lastLocation.getLatitude()),String.valueOf(lastLocation.getLongitude()));
        call.enqueue(new Callback<ProviderModel>() {
            @Override
            public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){

                    Tools.showToast(MainActivity.this,response.body().getSize()+" Provider found within your area Zoom-In for more details");
                    for(int i=0;i<response.body().getData().size();i++){
                        //Tools.showToast(MainActivity.this,response.body().getServices().get(i).getServices().toString());
                        Bitmap bitmap=Tools.drawableToBitmap(MainActivity.this.getDrawable(R.drawable.provider_marker));
                        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                        Icon icon=iconFactory.fromBitmap(bitmap);
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(response.body().getData().get(i).getLatitude()),Double.valueOf(response.body().getData().get(i).getLongitude())))
                                .setTitle(response.body().getData().get(i).getStoreName()).setIcon(icon))
                                .setSnippet("Store ID: "+response.body().getData().get(i).getId());

                    }
                    mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            String providerId=marker.getSnippet().replace("Store ID: ","");
                            beautifulProgressDialog.show();
                            showProviderInfo(providerId);
                            return false;
                        }
                    });

                }else{
                    Tools.showToast(MainActivity.this,"No provider found within your area");
                }
            }
            @Override
            public void onFailure(Call<ProviderModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

            }
        });






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
                    getSelectedProviderSerices(providerId,recyclerView);

                    makeOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isVehicleRegistered){
                                Intent intent=new Intent(MainActivity.this,MakeOrderActivity.class);
                                intent.putExtra("provider_id",providerId);
                                startActivity(intent);
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

    private void getSelectedProviderSerices(String providerId, RecyclerView recyclerView) {
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


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.getLocationEngine().getLastLocation(locationEngineCallback);
            setAllProviderStoreLocation(mapboxMap,lastLocation);


        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, permissionsToExplain.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        getRegisteredVehicle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs("geojsonSourceLayerId");
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    LatLng selectedLocation=new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude());

                    setAllProviderStoreLocation(mapboxMap,selectedLocation);
                    mapboxMap.addPolygon(generatePerimeter(selectedLocation,
                            Double.valueOf(settingsModel.getData().getRadius()),
                            64));
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(selectedLocation));
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(selectedLocation)
                                    .zoom(7)
                                    .build()), 4000);
                }
            }
        }
    }
}