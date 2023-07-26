package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityResetPasswordBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_FORGOT_PASSWORD;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityResetPasswordBinding binding;
    Activity ctx = ResetPasswordActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = ResetPasswordActivity.class.getSimpleName();
    String mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
        initViews();
    }
    void initViews()
    {
        mobile = getIntent().getStringExtra("mobile");
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rel_go:
                validate();
                break;

        }
    }

    void validate()
    {
        String c = binding.etCPass.getText().toString();
        String p = binding.etPass.getText().toString();

       if (p.isEmpty())
        {
            binding.etPass.setError("Password is Required");
        }
       else if (c.isEmpty())
        {
            binding.etCPass.setError("Comfirm Password is Required");
        }
        else
        {
            if (p.equals(c)) {


                if (ConnectivityReceiver.isConnected()) {
                    resetPass(mobile, p);
                } else {
                    common.showToast("No Internet Connection");
                }
            }else {
                common.showToast("Password mismatch");
            }
        }
    }


    void resetPass(String mobile,String pass)
    {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
//        params.put("key","1");
        params.put("mobile",mobile);
        params.put("password",pass);
        Log.e("r_params",params.toString());
        common.postRequest(URL_FORGOT_PASSWORD, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("reset",response.toString());
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String status=object.getString("status");

                    if(status.equals("success"))
                    {
                        common.showToast(object.getString("message"));
                        Intent intent=new Intent(ctx, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        common.showToast(object.getString("message"));
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
                error.printStackTrace();
                common.showVolleyError(error);
            }
        });
    }





}