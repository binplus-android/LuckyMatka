package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.SmsListener;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityVerifyOtpBinding;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.SmsReceiver;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GENERATE_OTP;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityVerifyOtpBinding binding ;
    Activity ctx = VerifyOtpActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = VerifyOtpActivity.class.getSimpleName();
    String otp ="",mobile="";
    CountDownTimer countDownTimer ,countDownTimer1;
    public static final String OTP_REGEX = "[0-9]{3,6}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
//
    void initViews()
    {
        otp = getIntent().getStringExtra("otp");
        mobile = getIntent().getStringExtra("mobile");
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.tvTimer.setOnClickListener(this);
        startTimer();
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                if (model.getMsg_status().equals("1"))
                {
                    getSmsOtp();
                }
                else
                {
                    countDownTimer=new CountDownTimer(5000,1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            binding.otpView.setText(otp);
                        }
                    };
                    countDownTimer.start();
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_login:
                startActivity(new Intent(ctx,LoginActivity.class));
                break;
            case R.id.rel_go:
                validate();
                break;
            case R.id.tv_timer:
                if (binding.tvTimer.getText().toString().equals("Resend Otp"))
                {
                    sendOtp(mobile,URL_GENERATE_OTP);
                }
                break;

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
                loadingBar.dismiss ();
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String res=object.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {

                       startTimer();

                    }
                    else
                    {
                        common.showToast(object.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
loadingBar.dismiss ();
            }
        });
    }

    public void getSmsOtp()
    {
        try
        {


            SmsReceiver.bindListener(new SmsListener() {
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
                       binding.otpView.setText(otp);


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


    void startTimer()
    {
        countDownTimer1 = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "  %02d : %02d ",

                        TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(l) % 60);

                binding.tvTimer.setText("Please wait... "+ text);
                binding.tvTimer.setTextColor(getResources().getColor(R.color.dark_gray));
            }
            @Override
            public void onFinish() {
              binding.tvTimer.setText("Resend Otp");
              binding.tvTimer.setTextColor(getResources().getColor(R.color.red));

            }
        };
        countDownTimer1.start();
    }

    void validate()
    {
            String otpp = binding.otpView.getText().toString();
            if (otpp.isEmpty())
            {
                common.showToast("Otp is Required");
            }
            else
            {
                if (otpp.equals(otp))
                {
                    Intent i = new Intent(ctx,ResetPasswordActivity.class);
                    i.putExtra("mobile",mobile);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }

    }
}