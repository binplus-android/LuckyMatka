package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.databinding.ActivityUpiDetailsBinding;
import in.games.luckymatkagames.databinding.ActivityWithdrawMethodsBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_REGISTER;
import static in.games.luckymatkagames.Config.Constants.KEY_ACCOUNNO;
import static in.games.luckymatkagames.Config.Constants.KEY_BANK_NAME;
import static in.games.luckymatkagames.Config.Constants.KEY_HOLDER;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_IFSC;
import static in.games.luckymatkagames.Config.Constants.KEY_PAYTM;
import static in.games.luckymatkagames.Config.Constants.KEY_PHONEPAY;
import static in.games.luckymatkagames.Config.Constants.KEY_TEZ;

public class UpiDetailsActivity extends AppCompatActivity {
    ActivityUpiDetailsBinding binding ;

    Activity ctx = UpiDetailsActivity.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    String title ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpiDetailsBinding.inflate(getLayoutInflater());
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
        title = getIntent().getStringExtra("title");
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upi Details");
        binding.tvTitle.setText(title);
        switch (title.toLowerCase())
        {
            case "paytm":
                binding.etPoints.setText(common.checkNullString(session_management.getUserDetails().get(KEY_PAYTM)));
                break;
            case "phonepe":
                binding.etPoints.setText(common.checkNullString(session_management.getUserDetails().get(KEY_PHONEPAY)));
                break;
            case "google pay":
                binding.etPoints.setText(common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));
                break;
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    void validate()
    {
        String num  = binding.etPoints.getText().toString();
        if (num.isEmpty())
        {
            binding.etPoints.setError("Required");

        }
        else if (num.length()!=10) {
            binding.etPoints.setError ("Invalid Mobile No.");
            binding.etPoints.requestFocus ( );
        }
        else if (Integer.parseInt (String.valueOf (num.charAt (0))) < 6) {
            binding.etPoints.setError ("Invalid Mobile No.");
            binding.etPoints.requestFocus ( );
        }

        else
        {
            Map<String,String> params=new HashMap<>();
            params.put("key","4");
            params.put("tez",session_management.getUserDetails().get(KEY_TEZ));
            params.put("paytm",session_management.getUserDetails().get(KEY_PAYTM));
            params.put("phonepay",session_management.getUserDetails().get(KEY_PHONEPAY));
            params.put("accountno",session_management.getUserDetails().get(KEY_ACCOUNNO));
            params.put("bankname",session_management.getUserDetails().get(KEY_BANK_NAME));
            params.put("ifsc",session_management.getUserDetails().get(KEY_IFSC));
            params.put("accountholder",session_management.getUserDetails().get(KEY_HOLDER));
            params.put("user_id",session_management.getUserDetails().get(KEY_ID));

            switch (title.toLowerCase())
            {
                case "paytm":
                    params.put("paytm",num);
                    break;
                case "phonepe":
                    params.put("phonepay",num);
                    break;
                case "google pay":
                    params.put("tez",num);
                    break;
            }

            if (ConnectivityReceiver.isConnected())
            {
                storeDetails((HashMap<String, String>) params,num);
            }
            else
            {
                common.showToast("No Internet Connection");
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    private void storeDetails(HashMap<String ,String >params ,String teznumber){
        loadingBar.show();


        Log.e("Account",params.toString());

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST,URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    Log.e("Tez",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        switch (title.toLowerCase())
                        {
                            case "google pay":
                                session_management.updateTezSection(teznumber);
                                break;
                            case "paytm":
                                session_management.updatePaytmSection(teznumber);
                                break;
                                case "phonepe":
                                session_management.updatePhonePaySection(teznumber);
                                break;
                        }


                        //  session_management.updateAccSection(accno,bankname,ifsc,hod_name);
                        common.showToast(""+response.getString("message"));

                    }
                    else
                    {
                        common.showToast("Something went wrong");
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    common.showToast(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                //String msg=common.VolleyErrorMessage(error);
//                if(!msg.isEmpty())
//                {
                    common.showToast(String.valueOf (error));
//                }
            }
        });
        common.increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_upi");

    }

}