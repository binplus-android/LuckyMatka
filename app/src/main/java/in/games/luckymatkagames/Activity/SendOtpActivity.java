package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.SmsListener;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivitySendOtpBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.SmsReceiver;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GENERATE_OTP;

public class SendOtpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySendOtpBinding binding ;
    Activity ctx = SendOtpActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = SendOtpActivity.class.getSimpleName();

    //for otp
    public static final String OTP_REGEX = "[0-9]{3,6}";
    EditText et_otp;
    TextView tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    void initViews()
    {
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
        String m = binding.etMobile.getText().toString();

        if (m.isEmpty())
        {
            binding.etMobile.setError("Mobile Number is Required");

        }
        else if (m.length()!=10) {
            binding.etMobile.setError ("Invalid Mobile No.");
            binding.etMobile.requestFocus ( );
        }
        else if (Integer.parseInt (String.valueOf (m.charAt (0))) < 6) {
            binding.etMobile.setError ("Invalid Mobile No.");
            binding.etMobile.requestFocus ( );
        }
        else
        {
            if (ConnectivityReceiver.isConnected())
            {
                sendOtp(m,URL_GENERATE_OTP);
            }
        }
    }

    void sendOtp(String m ,String url)
    {   loadingBar.show();
        String otp =common.getRandomKey(6);

        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",m);
        params.put("otp",otp);
        common.postRequest(url, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String res=object.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
//                       Intent i = new Intent(ctx,VerifyOtpActivity.class);
//                       i.putExtra("mobile",m);
//                       i.putExtra("otp",otp);
//                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                       startActivity(i);
                        String api_otp=otp;

                       openBottomSheet(m,otp,api_otp);

                    }
                    else
                    {
                        common.showToast(object.getString("message"));
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

    private void openBottomSheet(String mobile,String otp,String api_otp) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SendOtpActivity.this);
        bottomSheetDialog.setContentView(R.layout.dialog_otp);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        et_otp=bottomSheetDialog.findViewById (R.id.et_otp);
        TextView tv_ok,tv_cancle;
        tv_cancle=bottomSheetDialog.findViewById (R.id.tv_cancel);
        tv_ok=bottomSheetDialog.findViewById (R.id.tv_ok);
        tv_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                otpValidate (mobile,et_otp.getText ().toString (),api_otp);
            }
        });
        tv_cancle.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
               bottomSheetDialog.dismiss ();
            }
        });

        common.getConfigData(new OnConfigData () {
            @Override
            public void onGetConfigData(ConfigModel model) {
                if (model.getMsg_status().equals("1"))
                {
                    getSmsOtp();
                }
                else
                {
//                    countDownTimer=new CountDownTimer (5000,1000) {
//                        @Override
//                        public void onTick(long l) {
//
//                        }
//
//                        @Override
//                        public void onFinish() {
                            et_otp.setText(otp);
//                        }
//                    };
//                    countDownTimer.start();
                }

            }
        });
        bottomSheetDialog.show();
        bottomSheetDialog.setCanceledOnTouchOutside (false);
        bottomSheetDialog.setCancelable (false);
    }

    public void getSmsOtp()
    {
        try
        {


            SmsReceiver.bindListener(new SmsListener () {
                @Override
                public void messageReceived(String messageText) {

                    //From the received text string you may do string operations to get the required OTP
                    //It depends on your SMS format
                    Log.e("Message",messageText);
                    // Toast.makeText(SmsVerificationActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    // If your OTP is six digits number, you may use the below code

                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals("")))
                    {
                        et_otp.setText(otp);


                    }

                    //           Toast.makeText(SmsVerificationActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception ex)
        {
            // Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    void otpValidate(String  mobile,String otp,String api_otp)
    {
        String otpp = otp;
        if (otpp.isEmpty())
        {
            common.showToast("Otp is Required");
        }

        else
        {
            if (otpp.equals(api_otp))
            {
                Intent i = new Intent(ctx,ResetPasswordActivity.class);
                i.putExtra("mobile",mobile);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish ();
            }
            else
            {
                common.showToast("Invalid Otp ");
            }
        }

    }
}