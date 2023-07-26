package in.games.luckymatkagames.Fragment;

import static in.games.luckymatkagames.Config.BaseUrl.URL_REGISTER;
import static in.games.luckymatkagames.Config.Constants.KEY_ACCOUNNO;
import static in.games.luckymatkagames.Config.Constants.KEY_BANK_NAME;
import static in.games.luckymatkagames.Config.Constants.KEY_HOLDER;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_IFSC;
import static in.games.luckymatkagames.Config.Constants.KEY_PAYTM;
import static in.games.luckymatkagames.Config.Constants.KEY_PHONEPAY;
import static in.games.luckymatkagames.Config.Constants.KEY_TEZ;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.databinding.FragmentGpayBinding;

import in.games.luckymatkagames.databinding.FragmentPaytmBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;


public class PaytmFragment extends AppCompatActivity {
    FragmentPaytmBinding binding;
    Session_management session_management;
    Common common;
    String num="", title="";
    LoadingBar loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate (savedInstanceState);
        binding = FragmentPaytmBinding.inflate (getLayoutInflater ( ));
        setContentView (binding.getRoot ( ));
        intview ( );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Paytm Upi");
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



        binding.etMob.setText (common.checkNullString(session_management.getUserDetails().get(KEY_PAYTM)));
        binding.btnSave.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String paytmno = binding.etMob.getText ( ).toString ( );

                if (binding.etMob.getText ( ).toString ( ).isEmpty ( )) {
                    binding.etMob.setError ("Required paytm number");
                    binding.etMob.requestFocus ( );
                } else if (paytmno.length ( ) != 10) {
                    binding.etMob.setError ("Invalid Number");
                    binding.etMob.requestFocus ( );
                } else if (Integer.parseInt (String.valueOf (paytmno.charAt (0))) < 6) {
                    binding.etMob.setError ("Invalid Number");
                    binding.etMob.requestFocus ( );
                } else {
                    params.put("paytm",paytmno);
                    num=paytmno;
                    title="paytm";
                    if (ConnectivityReceiver.isConnected())
                    {
                        if (!num.equals("")) {
                            Log.e("vgbhjnk",num);
                            storeDetails((HashMap<String, String>) params, num, title);
                        }
                    }
                    else
                    {
                        common.showToast("No Internet Connection");
                    }

                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private void intview() {
        
        session_management=new Session_management ( PaytmFragment.this);
        common=new Common (PaytmFragment.this);
        loadingBar=new LoadingBar (PaytmFragment.this);
        common.getUserStatus(this);

    }

    private void storeDetails(HashMap<String ,String >params ,String teznumber,String title){
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
                        finish ();
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