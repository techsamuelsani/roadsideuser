package com.techsamuel.roadsideprovider.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.DialogServiceAdapter;
import com.techsamuel.roadsideprovider.adapter.DialogVehicleAdapter;
import com.techsamuel.roadsideprovider.adapter.ServiceAdapter;
import com.techsamuel.roadsideprovider.adapter.ToolboxUploadinPhotosGridAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.listener.OnItemClickListener;
import com.techsamuel.roadsideprovider.listener.VehicleItemOnclickListener;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.VehicleModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.SpacingItemDecoration;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOrderActivity extends AppCompatActivity {
    //Toolbar toolbar;
    String userId;
    String providerId;
    EditText selectService;
    EditText selectVehicle;
    BeautifulProgressDialog beautifulProgressDialog;
    EditText selectLocation;
    EditText selectType;
    EditText orderDescription;
    TextView selectImages;
    TextView selectAttachment;
    Button calculateBtn;
    ImageButton doneBtn;
    LinearLayout lytLocation;

    int PICK_IMAGE_MULTIPLE = 1;
    ToolboxUploadinPhotosGridAdapter toolboxUploadinPhotosGridAdapter;
    List<String> imagesEncodedList;
    String imageEncoded;
    RecyclerView recyclerView;
    LinearLayout lytOrderImages;
    LinearLayout lytSelectImages;


    ArrayList<String> selectedServiceId;
    ArrayList<String> choosenServiceId;
    ArrayList<String> selectedServiceName;
    ArrayList<String> choosenServiceName;
    String selectedVehicleId;
    String selectedVehicleName;
    double selectedLat;
    double selectedLong;
    String orderType="";
    String choosenOrderType;
    String serviceDescription;
    List<MultipartBody.Part> serviceImages=new ArrayList<MultipartBody.Part>();



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
         beautifulProgressDialog = new BeautifulProgressDialog(MakeOrderActivity.this,
                BeautifulProgressDialog.withImage,
                "Please wait");

    }
    private void init(){
        Intent intent=getIntent();
        providerId=intent.getStringExtra("provider_id");
        //Tools.showToast(MakeOrderActivity.this,providerId);
        selectService=findViewById(R.id.select_service);
        selectVehicle=findViewById(R.id.select_vehicle);

        selectLocation=findViewById(R.id.select_location);
        selectType=findViewById(R.id.select_type);
        orderDescription=findViewById(R.id.order_description);
        selectImages=findViewById(R.id.select_images);
        selectAttachment=findViewById(R.id.select_attachment);
        calculateBtn=findViewById(R.id.calculate_btn);
        doneBtn=findViewById(R.id.done_btn);
        lytLocation=findViewById(R.id.lyt_location);
        lytOrderImages=findViewById(R.id.lyt_order_images);
        recyclerView=findViewById(R.id.recyclerView);
        lytSelectImages=findViewById(R.id.lyt_select_images);


        selectedServiceId=new ArrayList<>();
        choosenServiceId=new ArrayList<>();
        selectedServiceName=new ArrayList<>();
        choosenServiceName=new ArrayList<>();
        serviceImages=new ArrayList<>();


        selectVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(false);
            }
        });
        selectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(true);
            }
        });
        selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderTypeDialog();
            }
        });
        lytSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Add images"), PICK_IMAGE_MULTIPLE);
            }
        });


    }
    private void showOrderTypeDialog(){
        final Dialog dialog = new Dialog(MakeOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_select_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenOrderType=orderType;
                if(choosenOrderType.equals(Config.ORDER_TYPE_DELIVERY)){
                    selectType.setText("Delivery");
                    lytLocation.setVisibility(View.VISIBLE);
                }else if(choosenOrderType.equals(Config.ORDER_TYPE_PICKUP)){
                    selectType.setText("Pickup");
                    lytLocation.setVisibility(View.GONE);
                }else{
                    selectType.setText("Delivery/Pickup");
                    lytLocation.setVisibility(View.GONE);
                }
                dialog.cancel();
            }
        });


        RadioButton radioDelivery=(dialog).findViewById(R.id.radio_delivery);
        RadioButton radioPickup=(dialog).findViewById(R.id.radio_pickup);

        radioDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioDelivery.isChecked()){
                    orderType=Config.ORDER_TYPE_DELIVERY;

                }
            }
        });

        radioPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioPickup.isChecked()){
                    orderType=Config.ORDER_TYPE_PICKUP;

                }
            }
        });

        dialog.show();
    }



    private void showDialog(boolean isService){
        final Dialog dialog = new Dialog(MakeOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_select_service);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        RecyclerView recyclerView=dialog.findViewById(R.id.recycler_service);
        beautifulProgressDialog.show();
        if(isService){
            getProviderSerivces(providerId,recyclerView,dialog);
        }else{
            getRegisteredVehicle(recyclerView,dialog);
        }


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isService){
                    selectService.setText(selectedServiceName.toString());
                    choosenServiceId=selectedServiceId;
                    choosenServiceName=selectedServiceName;
                    //Tools.showToast(MakeOrderActivity.this,choosenServiceId.toString());
                    dialog.cancel();
                }
            }
        });
    }

    private void getRegisteredVehicle(RecyclerView recyclerView,Dialog dialog){
        Log.d("MainActivity","Called");
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<VehicleModel> call=apiInterface.getRegisteredVehicle(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,userId);
        call.enqueue(new Callback<VehicleModel>() {
            @Override
            public void onResponse(Call<VehicleModel> call, Response<VehicleModel> response) {
                Log.d("MainActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    DialogVehicleAdapter dialogVehicleAdapter=new DialogVehicleAdapter(MakeOrderActivity.this, response.body(), new VehicleItemOnclickListener() {
                        @Override
                        public void onItemClick(VehicleModel.Datum item) {
                            selectedVehicleName=item.getVmake()+" "+item.getVmodel()+" "+item.getVyear();
                            selectedVehicleId=item.getId();
                            selectVehicle.setText(selectedVehicleName);
                            dialog.dismiss();
                        }
                    });
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(MakeOrderActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(dialogVehicleAdapter);
                    dialog.show();
                    Log.d("MainActivity",response.body().getMessage().toString());
                }else{

                    Tools.showToast(MakeOrderActivity.this,"No vehicle registerd, please register a vehicle");
                }
                beautifulProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<VehicleModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());

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
                        public void onItemClick(ServiceModel.Datum item,boolean isChecked) {
                            if(isChecked){
                                selectedServiceId.add(item.getId());
                                selectedServiceName.add(item.getName());
                            }else{
                                selectedServiceId.remove(item.getId());
                                selectedServiceName.remove(item.getName());
                            }
                            //Tools.showToast(MakeOrderActivity.this,item.getName());
                        }
                    });
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(MakeOrderActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(serviceAdapter);
                    dialog.show();
                    Log.d("MainActivity",response.body().getMessage().toString());
                }
                beautifulProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try{
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> imageUri = new ArrayList<Uri>();
                    serviceImages.clear();
                    imageUri.add(mImageUri);
                    recyclerView.setVisibility(View.VISIBLE);
                    lytOrderImages.setVisibility(View.GONE);
                    toolboxUploadinPhotosGridAdapter=new ToolboxUploadinPhotosGridAdapter(MakeOrderActivity.this,imageUri);
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(MakeOrderActivity.this, 2), true));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(toolboxUploadinPhotosGridAdapter);
                    //toolboxFile=new MultipartBody.Part[services.size()];
                    for(int i=0;i<imageUri.size();i++){
                        File file=Tools.getFile(MakeOrderActivity.this,imageUri.get(i));
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part=MultipartBody.Part.createFormData("service_images[]", file.getName(), requestBody);
                        serviceImages.add(part);

                    }

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> imageUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            imageUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                            serviceImages.clear();
                            recyclerView.setVisibility(View.VISIBLE);
                            lytOrderImages.setVisibility(View.GONE);
                            toolboxUploadinPhotosGridAdapter=new ToolboxUploadinPhotosGridAdapter(MakeOrderActivity.this,imageUri);
                            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(MakeOrderActivity.this, 2), true));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(toolboxUploadinPhotosGridAdapter);
                            //toolboxFile=new MultipartBody.Part[services.size()];
                            for(int i=0;i<imageUri.size();i++){
                                File file=Tools.getFile(MakeOrderActivity.this,imageUri.get(i));
                                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                                MultipartBody.Part part=MultipartBody.Part.createFormData("service_images[]", file.getName(), requestBody);
                                serviceImages.add(part);

                            }
                    }
                }
            }

        }catch (Exception e){
            Tools.showToast(MakeOrderActivity.this, "Something went wrong");
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
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