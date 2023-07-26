package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityLoginBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_LOGIN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding binding ;
    Activity ctx = LoginActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        common.getMsg();
        common.generateToken();

    }

    void initViews()
    {
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);
        binding.relRegister.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        binding.tvPass.setOnClickListener(this);
        binding.tvPass.setPaintFlags(binding.tvPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rel_go:
                validate();
                break;

            case R.id.relRegister:
                startActivity(new Intent(ctx,RegisterActivity.class));
                break;

            case R.id.tv_pass:
//                startActivity(new Intent(ctx,SendOtpActivity.class));
                break;

            case R.id.tv_register:
                startActivity(new Intent(ctx,RegisterActivity.class));
                break;

        }
    }

    void validate()
    {
        String m = binding.etMobile.getText().toString();
        String p = binding.etPass.getText().toString();
        if (m.isEmpty())
        {
            binding.etMobile.setError("Mobile Number is Required");

        }
        else if (p.isEmpty())
        {
            binding.etPass.setError("Password is Required");
        }
        else
        {
            if (ConnectivityReceiver.isConnected())
            {
                makeLogin(m,p);
            }
        }
    }


    public void makeLogin(String mobile ,String pas)
    {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobileno",mobile);
        params.put("password",pas);
        params.put("imei",session_management.getDeviceId());
        params.put("token", session_management.getToken());
        Log.e("params_login", "makeLogin: "+params );
        common.postRequest(URL_LOGIN, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("login",response.toString());
                try {
                    JSONObject object = new JSONObject(String.valueOf(response));
                    if (object.getBoolean("responce"))
                    {
                        JSONObject jsonObject = object.getJSONObject("data");
                        String id=common.checkNullString(jsonObject.getString("id"));
                        String name=common.checkNullString(jsonObject.getString("name"));
                        String username=common.checkNullString(jsonObject.getString("username"));
                        String mobile=common.checkNullString(jsonObject.getString("mobileno"));
                        String email=common.checkNullString(jsonObject.getString("email"));
                        String address=common.checkNullString(jsonObject.getString("address"));
                        String city=common.checkNullString(jsonObject.getString("city"));
                        String pincode=common.checkNullString(jsonObject.getString("pincode"));
                        String accno=common.checkNullString(jsonObject.getString("accountno"));
                        String bank=common.checkNullString(jsonObject.getString("bank_name"));
                        String ifsc=common.checkNullString(jsonObject.getString("ifsc_code"));
                        String holder=common.checkNullString(jsonObject.getString("account_holder_name"));
                        String paytm=common.checkNullString(jsonObject.getString("paytm_no"));
                        String tez=common.checkNullString(jsonObject.getString("tez_no"));
                        String phonepay=common.checkNullString(jsonObject.getString("phonepay_no"));
                        String wallet=common.checkNullString(jsonObject.getString("winning_wallet_points"));
                        String dob=common.checkNullString(jsonObject.getString("dob"));
                        String gender=common.checkNullString(jsonObject.getString("gender"));
                        String ref_code=common.checkNullString(jsonObject.getString("user_ref"));
                        String wallet_winning=common.checkNullString(jsonObject.getString("wallet_points"));
                        String ref_share_link=common.checkNullString(jsonObject.getString("share_link"));

                        String p = jsonObject.getString("password");
                        Log.e("sdfgh",ref_code);
                        if (pas.equals(p)) {
                            Log.e(TAG, "onResponse: "+id+"\n"+bank+"\n "+ifsc+"\n"+accno+"\n"+holder );
                            session_management.createLoginSession(id,name,username,mobile,email,address
                                    ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,dob,wallet,gender,ref_code,wallet_winning,ref_share_link);
                            toastMsg.toastIconSuccess("Login Successful");
                            Intent intent = new Intent(ctx,MainActivity.class);

                            intent.putExtra("username", jsonObject.getString("username"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            toastMsg.toastIconError("Password is not correct");
                        }
                    }
                    else
                    {
                        toastMsg.toastIconError(object.getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

}