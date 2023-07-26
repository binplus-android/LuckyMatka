package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityMpinBinding;
import in.games.luckymatkagames.databinding.ActivityVerifyOtpBinding;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.CREATE_MPIN;
import static in.games.luckymatkagames.Config.BaseUrl.FORGOT_MPIN;
import static in.games.luckymatkagames.Config.BaseUrl.MPIN_LOGIN;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_MOBILE;

public class MpinActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMpinBinding binding ;
    Activity ctx = MpinActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = MpinActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
       // setContentView (R.layout.activity_mpin);
        binding = ActivityMpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);
        binding.tvFmpin.setOnClickListener(this);
        binding.tvLogout.setOnClickListener(this);
        binding.relGen.setOnClickListener (this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_gen:
                generateMpin ();
                break;
        case R.id.tv_fmpin:
            Dialog dialog ;
            dialog=new Dialog(MpinActivity.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_forgot_mpin);
            dialog.show();

            Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
            EditText et_mpin=(EditText)dialog.findViewById (R.id.et_mpin);
            EditText et_mob=(EditText)dialog.findViewById (R.id.et_mob);
            et_mob.setText(session_management.getUserDetails().get(KEY_MOBILE));
            btn_gen.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String mpin = et_mpin.getText ().toString ();
                    String mob = et_mob.getText().toString();
                    if (mob.isEmpty())
                    {
                        et_mob.setError ("M-Pin Required");
                    } else if(mpin.isEmpty ()){
                        et_mpin.setError ("M-Pin Required");
                        et_mpin.requestFocus ();
                    }else if (mpin.length()!=4)
                    {
                        et_mpin.setError ("4 Digit M-Pin Required");
                        et_mpin.requestFocus ();
                    } else {
                        getForgteMPIN(mob,mpin);
                        dialog.dismiss();
                    }

                }



            });

            dialog.setCanceledOnTouchOutside (true);
        break;

        case R.id.rel_go:
        validate();
        break;

        case R.id.tv_logout:
            session_management.logoutSession ();
            startActivity(new Intent (ctx,LoginActivity.class));
            finish ();
        break;
    }
}

    private void validate() {
        String mpin = binding.etMpin.getText().toString();
        if (mpin.isEmpty())
        {
            binding.etMpin.setError("M-Pin is Required");

        }
        else if (mpin.length()!=4) {
            binding.etMpin.setError ("4 Digit M-Pin Required");
            binding.etMpin.requestFocus ( );
        }
        else {
            mpinLogin(mpin);
        }
    }

    private void mpinLogin(final String mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
//        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",session_management.getUserDetails().get(KEY_MOBILE));

        common.postRequest(MPIN_LOGIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        Intent i = new Intent(MpinActivity.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText (ctx, ""+object.getString("data"), Toast.LENGTH_SHORT).show ( );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });
    }
    private void getForgteMPIN(String mob, String mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("mobile",mob);

        common.postRequest(FORGOT_MPIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        Toast.makeText (ctx, ""+  object.getString("message"), Toast.LENGTH_SHORT).show ( );

//                        sessionMangement.updateMpin(mpin);
                    }else {
                        Toast.makeText (ctx, "Something went wrong", Toast.LENGTH_SHORT).show ( );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });
    }

    private void getMPINData(final String mpin, EditText et_mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("user_id", session_management.getUserDetails().get(KEY_ID));
        params.put("mobile",session_management.getUserDetails().get(KEY_MOBILE));

        common.postRequest(CREATE_MPIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        Toast.makeText (ctx, ""+object.getString("message"), Toast.LENGTH_SHORT).show ( );
//                        sessionMangement.updateMpin(mpin);
                    }else {
                        Toast.makeText (ctx, "Something went wrong", Toast.LENGTH_SHORT).show ( );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });
    }

    private void generateMpin() {
        Dialog dialog ;
        dialog=new Dialog(MpinActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mpin);
        dialog.show();


        Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
        EditText et_mpin=(EditText)dialog.findViewById (R.id.et_mpin);
        btn_gen.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String mpin = et_mpin.getText ().toString ();
                if(et_mpin.getText ().toString ().isEmpty ()){
                    et_mpin.setError ("M-Pin Required");
                    et_mpin.requestFocus ();
                }else if (mpin.length()!=4)
                {
                    et_mpin.setError ("4 Digit M-Pin Required");
                    et_mpin.requestFocus ();
                } else {
//                        Toast.makeText (getContext (), "Success", Toast.LENGTH_SHORT).show ( );
//                       dialog.dismiss ();
                    getMPINData(mpin,et_mpin);
                    dialog.dismiss();
                }
            }
        });

        dialog.setCanceledOnTouchOutside (true);

    }
    }