package in.games.luckymatkagames.Activity;

import static in.games.luckymatkagames.Config.BaseUrl.URL_CHANGE_PASSWORD;
import static in.games.luckymatkagames.Config.Constants.KEY_MOBILE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityChangeBinding;
import in.games.luckymatkagames.databinding.ActivityResetPasswordBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

public class
ChangeActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityChangeBinding binding;
    Activity ctx = ChangeActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = ResetPasswordActivity.class.getSimpleName();
    String mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_change);
        binding = ActivityChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
        }else {
            common.showToast("No Internet Connection");
        }

    }
    void initViews()
    {
        session_management = new Session_management(ctx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        mobile = session_management.getUserDetails ().get (KEY_MOBILE);
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
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

    void validate() {
        String o=binding.etOldpass.getText ().toString ();
        String p = binding.etPass.getText().toString();
        if (o.isEmpty()) {
            binding.etOldpass.setError("Old Password is Required");
        } else if (p.isEmpty()) {
            binding.etPass.setError("New Password is Required");
        } else {
            if (ConnectivityReceiver.isConnected()) {
                ChangePass(mobile, p,o);
            } else {
                common.showToast("No Internet Connection");
            }
        }
    }

    void ChangePass(String mobile,String pass,String old)
    {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("old_password",old);
        params.put("mobile",mobile);
        params.put("new_password",pass);
        Log.e("r_params",params.toString());
        common.postRequest(URL_CHANGE_PASSWORD, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("reset",response.toString());
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String status=object.getString("status");

                    if(status.equals("success"))
                    {
                        common.showToast(object.getString("message"));
                        Intent intent=new Intent(ctx, MainActivity.class);
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

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged (hasCapture);
    }
}