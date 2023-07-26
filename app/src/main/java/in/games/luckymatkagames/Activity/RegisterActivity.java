package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityRegisterBinding;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_REGISTER;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
ActivityRegisterBinding binding;
    Activity ctx = RegisterActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String TAG = RegisterActivity.class.getSimpleName();
    String link="";
    String android_id="";
    String deviceIMEI="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        common.getMsg();

    }

    void initViews()
    {
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        android_id = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        session_management.addDeviceId(android_id);
        binding.relGo.setOnClickListener(this);
        binding.relLogin.setOnClickListener(this);
            if (getIntent().getExtras() != null) {
                link = getIntent().getStringExtra("link");
                if (!link.isEmpty()) {
                    try {
                        String[] myArr = link.split("=");
                        String ref = myArr[1];
                        binding.etReferCode.setText(ref);
                        binding.etReferCode.setEnabled(false);
                        Log.e("ref_code", ref);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.relLogin:
                startActivity(new Intent(ctx,LoginActivity.class));
                break;
            case R.id.rel_go:
                validate();
                break;
        }
    }


    void validate()
    {
        String uname = binding.etUsername.getText().toString();
        String mobile = binding.etMobile.getText().toString();
        String email = binding.etEmail.getText().toString();
        String pass = binding.etPass.getText().toString();
        String mpin = binding.etMpin.getText().toString();
        if (common.allValidation("name",binding.etUsername).equals("correct"))
        {
            if( mobile.isEmpty()) {
            binding.etMobile.setError("Mobile Number is Required");
        } else if (mobile.length()!=10) {
            binding.etMobile.setError ("Invalid Mobile No.");
            binding.etMobile.requestFocus ( );
        } else if (Integer.parseInt (String.valueOf (mobile.charAt (0))) < 6) {
            binding.etMobile.setError ("Invalid Mobile No.");
            binding.etMobile.requestFocus ( );
        } else if(email.isEmpty()) {
            binding.etEmail.setError("Email Address is Required");
        } else if(!(email.matches (emailPattern))){
            binding.etEmail.setError("Invalid Email Address");
        } else if (pass.isEmpty()) {
            binding.etPass.setError("Password is Required");
        } else {
            registerUser(uname,mobile,email,pass);
        }
        }
    }

    void registerUser(String name , String mobile , String email , String pass)
    {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("key","1");
        params.put("username",name);
        params.put("email",email);
        params.put("mobile",mobile);
//        params.put("mpin",mpin);
        params.put("password",pass);
//        params.put ("ref_code",ref);
        Log.e("r_params",params.toString());
        common.postRequest(URL_REGISTER, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("register",response.toString());
                try {
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp)
                    {
                        common.showToast(""+ object.getString("message"));
                        Intent intent=new Intent(ctx, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        common.showToast(object.getString("error"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });
    }

    private void doSomthing() {
        deviceIMEI = getDeviceIMEI(RegisterActivity.this);
        //andGoToYourNextStep
    }
    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Activity activity) {

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            else
                //deviceUniqueIdentifier = tm.getDeviceId();
                if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length())
                    deviceUniqueIdentifier = "0";
        }
        return deviceUniqueIdentifier;
    }

    public String getDeviceName() {
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        /*
         * getDeviceId() returns the unique device ID.
         * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String id=Build.ID;
        String idd= Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);;
        String serial=Build.SERIAL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

}