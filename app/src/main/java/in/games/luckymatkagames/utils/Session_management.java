package in.games.luckymatkagames.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import in.games.luckymatkagames.Activity.LoginActivity;

import static in.games.luckymatkagames.Config.Constants.*;


public class Session_management {

    SharedPreferences prefs;

    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();


    }

    public void createLoginSession(String id, String name, String username
            , String mobile, String email, String address, String city, String pincode, String accountno,
                                   String bank_name, String ifsc, String holder, String paytm, String tez, String phonepay,
                                   String dob, String wallet, String gender,String ref_code, String wallet_winning,String share_link) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_ACCOUNNO, accountno);
        editor.putString(KEY_BANK_NAME, bank_name);
        editor.putString(KEY_IFSC, ifsc);
        editor.putString(KEY_HOLDER, holder);
        editor.putString(KEY_PAYTM, paytm);
        editor.putString(KEY_TEZ, tez);
        editor.putString(KEY_PHONEPAY, phonepay);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_WALLET, wallet);
        editor.putString(KEY_WALLET_WINNING, wallet_winning);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_CODE,ref_code);
        editor.putString(KEY_SHARE_LINK,share_link);
//        editor.putString(KEY_GIF_IMAGE,"");

        editor.commit();
    }
//
//    /**
//     * Get stored session data
//     */

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, ""));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, ""));
        user.put(KEY_USER_NAME, prefs.getString(KEY_USER_NAME, ""));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, ""));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, ""));
        user.put(KEY_ADDRESS, prefs.getString(KEY_ADDRESS, ""));
        user.put(KEY_CITY, prefs.getString(KEY_CITY, ""));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, ""));
        user.put(KEY_ACCOUNNO, prefs.getString(KEY_ACCOUNNO, ""));
        user.put(KEY_BANK_NAME, prefs.getString(KEY_BANK_NAME, ""));
        user.put(KEY_IFSC, prefs.getString(KEY_IFSC, ""));
        user.put(KEY_HOLDER, prefs.getString(KEY_HOLDER, ""));
        user.put(KEY_PAYTM, prefs.getString(KEY_PAYTM, ""));
        user.put(KEY_TEZ, prefs.getString(KEY_TEZ, ""));
        user.put(KEY_PHONEPAY, prefs.getString(KEY_PHONEPAY, ""));
        user.put(KEY_WALLET, prefs.getString(KEY_WALLET, ""));
        user.put(KEY_WALLET_WINNING, prefs.getString(KEY_WALLET_WINNING, ""));
        user.put(KEY_DOB, prefs.getString(KEY_DOB, ""));
        user.put(KEY_GENDER, prefs.getString(KEY_GENDER, ""));
        user.put(KEY_SHARE_LINK,prefs.getString(KEY_SHARE_LINK,""));
        user.put(KEY_CODE,prefs.getString(KEY_CODE,""));
        return user;
    }


    public void updateAccSection(String acc_no, String bank_name, String ifsc, String holder)
    {
        editor.putString(KEY_ACCOUNNO, acc_no);
        editor.putString(KEY_BANK_NAME, bank_name);
        editor.putString(KEY_IFSC, ifsc);
        editor.putString(KEY_HOLDER, holder);
        editor.commit();
    }
    public void updateAddressSection(String address, String city, String pincode)
    {
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pincode);
        editor.apply();
    }
    public void updatePaymentSection(String tez, String paytm, String phonepay)
    {
        editor.putString(KEY_TEZ, tez);
        editor.putString(KEY_PAYTM, paytm);
        editor.putString(KEY_PHONEPAY, phonepay);
        editor.apply();
    }
    public void updateTezSection(String tez)
    {
        editor.putString(KEY_TEZ, tez);
        editor.apply();
    }
    public void updatePaytmSection(String paytm)
    {
        editor.putString(KEY_PAYTM, paytm);
        editor.apply();
    }
    public void updatePhonePaySection(String phonepay)
    {
        editor.putString(KEY_PHONEPAY, phonepay);
        editor.apply();
    }
    public void updateEmailSection( String mobile, String name)
    {
//        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_NAME, name);
        editor.apply();
    }
    public void updateWallet(String wallet)
    {
        editor.putString(KEY_WALLET, wallet);
        editor.apply();
    }
    public  void updateWinWallet(String wallet_winning){
        editor.putString(KEY_WALLET_WINNING, wallet_winning);
        editor.apply();

    }
    public void updateItem( String key ,String value)
    {
        editor.putString(key, value);
        editor.apply();
    }

    public void createGifSesssion(String gif){
        editor.putString(KEY_GIF_IMAGE,gif);

        editor.commit();
    }
    public HashMap<String,String> getGifDetails()
    {
        HashMap<String, String> gif = new HashMap<String, String>();

        gif.put(KEY_GIF_IMAGE, prefs.getString(KEY_GIF_IMAGE, ""));
        return gif;
    }
    public void updateGIF( String url)
    {
        editor.putString(KEY_GIF_IMAGE, url);
        editor.apply();
    }
    public HashMap<String,String> getString()
    {
        HashMap<String, String> val = new HashMap<String, String>();

        val.put(KEY_MSG, prefs.getString(KEY_MSG, ""));
       return  val;
    }
    public void putString( String value)
    {
        editor.putString(KEY_MSG, value);
        editor.apply();
    }
    public void addDeviceId(String android_id) {
        editor.putString(DEVICE_ID,android_id);
        editor.commit();
    }


    public String getDeviceId() {
        return prefs.getString(DEVICE_ID,"");
    }

    public void addToken(String token) {
        editor.putString(DEVICE_TOKEN,token);
        editor.commit();
    }

    public String getToken() {
        return prefs.getString(DEVICE_TOKEN,"");
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
        Intent logout = new Intent(context, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);

    }

//    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
