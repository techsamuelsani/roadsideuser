package com.techsamuel.roadsideprovider.activity.register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends AppCompatActivity {

    TextView phoneNumber;
    TextView fullName;
    TextView email;
    TextView password;
    TextView confirmPassword;
    Button save;
    FirebaseUser firebaseUser;
    String firebaseId;
    RelativeLayout lytProfilePhoto;
    ImageView profileImage;
    ImageView imageMask;
    int PICK_IMAGE_PROFILE_PHOTO=1;
    MultipartBody.Part profilePhotoFile;
    BeautifulProgressDialog beautifulProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.hideSystemUI(this);
        setContentView(R.layout.activity_user_register);
        init();
        initBeautifulProgressDialog();
    }

    private void initBeautifulProgressDialog(){
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);
    }


    private void init(){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseId=firebaseUser.getUid();
        phoneNumber=findViewById(R.id.phoneNumber);
        fullName=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        lytProfilePhoto=findViewById(R.id.lyt_profile_photo);
        profileImage=findViewById(R.id.profile_image);
        imageMask=findViewById(R.id.image_mask);
        save=findViewById(R.id.save);
        phoneNumber.setText(firebaseUser.getPhoneNumber());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MultipartBody.Part user_phone = MultipartBody.Part.createFormData("user_phone", firebaseUser.getPhoneNumber());
                MultipartBody.Part name = MultipartBody.Part.createFormData("name", fullName.getText().toString());
                MultipartBody.Part emailValue = MultipartBody.Part.createFormData("email", email.getText().toString());
                MultipartBody.Part passwordValue = MultipartBody.Part.createFormData("password", password.getText().toString());
                MultipartBody.Part confirmPasswordValue = MultipartBody.Part.createFormData("confirm_password", confirmPassword.getText().toString());
                MultipartBody.Part device_type = MultipartBody.Part.createFormData("device_type", Config.DEVICE_TYPE);
                saveUserDetailsToServer(user_phone,name,emailValue,passwordValue,confirmPasswordValue,device_type);

            }
        });
        lytProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(PICK_IMAGE_PROFILE_PHOTO);
            }
        });


    }

    private void pickImageFromGallery(int requestCode) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE_PROFILE_PHOTO && resultCode==RESULT_OK && data!=null){
            Uri imageUri=data.getData();
            imageMask.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);
            profileImage.setImageURI(imageUri);
            File file= null;
            try {
                file = Tools.getFile(UserRegisterActivity.this,imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            profilePhotoFile = MultipartBody.Part.createFormData("user_photo", file.getName(), requestBody);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUserDetailsToServer(MultipartBody.Part phoneNumber, MultipartBody.Part fullname, MultipartBody.Part email, MultipartBody.Part password, MultipartBody.Part confirm_password
    ,MultipartBody.Part device_type) {
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<UserModel> call=apiInterface.saveUserDetails(phoneNumber,fullname,profilePhotoFile,email,password,confirm_password,device_type);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                beautifulProgressDialog.dismiss();
                //Tools.showToast(RegisterStepFourActivity.this,response.body().getMessage().toString());
                AppSharedPreferences.init(UserRegisterActivity.this);
                Intent intent = null;
                if(response.body().getStatus()== Config.API_SUCCESS){
                    if(!response.body().getId().equals(0)){
                        AppSharedPreferences.write(Config.SHARED_PREF_USER_ID,response.body().getId().toString());
                        AppSharedPreferences.writeUserModel(Config.SHARED_PREF_USER_MODEL,response.body());
                        Tools.showToast(UserRegisterActivity.this,response.body().getMessage().toString());
                        intent=new Intent(UserRegisterActivity.this, MainActivity.class);
                        UserRegisterActivity.this.startActivity(intent);

                    }
                }else{
                    Tools.showToast(UserRegisterActivity.this,response.body().getMessage().toString());
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("UserRegisterActivity",t.getMessage().toString());
                Tools.showToast(UserRegisterActivity.this,"Connecton to server failed");
                beautifulProgressDialog.dismiss();
            }
        });


    }
}