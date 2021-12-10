package com.techsamuel.roadsideprovider;

public class Config {

    public static final String BASE_URL="https://admin.towietr.com/public/";
    public static final String API_BASE_URL=BASE_URL+"api/";
    public static final String IMAGE_URL=BASE_URL+"uploads/";
    public static final String API_USERNAME="roadsideapp";
    public static final String API_PASSWORD="RoadSide1643*#";
    public static final String LANG_CODE = "en";
    public static final String LOCALE_CODE = "tr";
    public static final Double MINIMUM_RECHARGE_AMOUNT = 10.00;
    public static final Double MAXIMUM_RECHARGE_AMOUNT = 500.00;
    public static final String SHARED_PREF_DEVICE_ID = "device_id";
    public static final long SPLASH_SCREEN_TIMEOUT = 2000;
    public static final String SHARED_PREF_SETTINGS_MODEL = "settings_model";
    public static final String SHARED_PREF_ORDER_MODEL = "order_model";
    public static final String PAGE_PROVIDER = "provider";
    public static final String APP_PAGE = "page";
    public static final String PAGE_CURRENT_ORDERS = "current_orders";
    public static int PHONE_VERIFICATION_CODE=101;
    public static int API_SUCCESS=200;
    public static int API_FAILED=500;
    public static String DEVICE_TYPE="android";
    public static String USER_TYPE="user";
    public static String ORDER_TYPE_DELIVERY="delivery";
    public static String ORDER_TYPE_PICKUP="pickup";
    //All Configuration will go here

    public static long LOCATION_BUTTON_INVISIBLE_TIME=3000; //1 sec =1000

    public static String IZIPAY_PAYMENT_URL_SELF=BASE_URL+"payment/izipay/";
    public static String IZIPAY_CALLBACK_URL=BASE_URL+"payment/payment_success/";



    //All Shared Preferences Key
    public static String SHARED_PREF_KEY_FCM="fcmtoken";
    public static String SHARED_PREF_USER_ID="user_id";
    public static String SHARED_PREF_USER_MODEL="user_model";

    public static Double balance=0.0;


}
