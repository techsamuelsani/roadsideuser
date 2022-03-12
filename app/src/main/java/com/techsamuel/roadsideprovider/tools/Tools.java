package com.techsamuel.roadsideprovider.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.activity.CurrentOrdersActivity;
import com.techsamuel.roadsideprovider.activity.MainActivity;
import com.techsamuel.roadsideprovider.activity.OrderDetailsActivity;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.model.OrderModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tools {

    public static void hideSystemUI(Activity activity) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static boolean validatePhoneNumber(String number){
        boolean isValid=false;
        number=number.replaceAll("[\\D]","");
        if(number.length()>=9 && number.length()<=13){
            isValid=true;
        }
        return isValid;

    }
    public static void showToast( Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    public static void deserializeString(String value){
        ArrayList<String> errorlist = new ArrayList<String>();
        //Verify list data
        for (String error : errorlist) {
            System.out.println(error);
        }
    }

    public static int generateRandomInt(int minimum,int maximum){
        Random rn = new Random();
        int range = maximum - minimum + 1;
        int randomNum =  rn.nextInt(range) + minimum;
        return randomNum;
    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static <T extends Comparable<T>> List<Integer> sortIndex(List<T> in) {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            index.add(i);
        }

        Collections.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer idx1, Integer idx2) {
                return in.get(idx1).compareTo(in.get(idx2));
            }
        });

        return index;
    }

    public static String getAdressFromLatLong(Context context,String latitude, String longitude) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String address="Problem with getting address from store location";

        try {
            addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
            address = addresses.get(0).getAddressLine(0);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String  // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
        return address;


    }



    public static double distanceInKm(LatLng start, LatLng end){
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.getLatitude());
            location1.setLongitude(start.getLongitude());
            Location location2 = new Location("locationB");
            location2.setLatitude(end.getLatitude());
            location2.setLongitude(end.getLongitude());
            double distance = location1.distanceTo(location2);
            return distance/1000;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }
    public static double timeInMintues(LatLng start, LatLng end){
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.getLatitude());
            location1.setLongitude(start.getLongitude());
            Location location2 = new Location("locationB");
            location2.setLatitude(end.getLatitude());
            location2.setLongitude(end.getLongitude());
            double distance = location1.distanceTo(location2);
            return (distance/1000)*60/50;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }


    private void showConfirmDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Want to reload your previous data?");
        builder.setMessage("Want to reload your previous saved form data then click OK or click Cancel for entering form data from beginning");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public static String getRealPathFromURI (Context context,Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    public static Uri getImageUriFromBitmap(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void getOrderDetailsById(Context context,String id) {
        AppSharedPreferences.init(context);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrderModel> call=apiInterface.getOrderDetailsById(Config.DEVICE_TYPE,Config.LANG_CODE,id);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    AppSharedPreferences.writeOrderModel(Config.SHARED_PREF_ORDER_MODEL,response.body());

                }
            }
            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Log.d("CurrentOrdersActivity",t.getMessage().toString());

            }
        });

    }

    public static void openMarketForRatings(Context context){
        try {
            Uri uri = Uri.parse("market://details?id="+context.getPackageName()+"");
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goMarket);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName()+"");
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goMarket);
        }
    }







}
