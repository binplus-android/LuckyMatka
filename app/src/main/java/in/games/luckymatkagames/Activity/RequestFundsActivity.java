package in.games.luckymatkagames.Activity;
import static com.shreyaspatil.EasyUpiPayment.model.PaymentApp.GOOGLE_PAY;
import static com.shreyaspatil.EasyUpiPayment.model.PaymentApp.NONE;
import static com.shreyaspatil.EasyUpiPayment.model.PaymentApp.PAYTM;
import static com.shreyaspatil.EasyUpiPayment.model.PaymentApp.PHONE_PE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.games.luckymatkagames.Adapter.TransactionHistoryAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.AddWithdrawModel;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.SpinnerModel;
import in.games.luckymatkagames.Model.WalletObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityRequestFundsBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.luckymatkagames.Config.BaseUrl.Url_req_history;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_WALLET;


public class RequestFundsActivity extends AppCompatActivity implements View.OnClickListener, PaymentStatusListener {
    Session_management session_management ;
    ArrayList<AddWithdrawModel> wlist=new ArrayList<> (  ) ;
    TransactionHistoryAdapter transactionHistoryAdapter ;
    LoadingBar loadingBar ;
    ArrayList<SpinnerModel> o_list=new ArrayList<> (  );
    ArrayList<Integer> ol_list=new ArrayList<> (  );
    Common common ;
    ActivityRequestFundsBinding binding ;
    Activity ctx = RequestFundsActivity.this;
    private EasyUpiPayment mEasyUpiPayment;
    int min_amt = 0;
    String wType="";
    boolean upi_flag = false;
    String upitype="",upi = "", upi_name = "", upi_desc = "", upi_type = "", transactionId = "", upi_status = "",request_type_status="",fund_text="",qrCode_image="",image_status,whatsapp_no="";
    String TAG = RequestFundsActivity.class.getSimpleName();
    RadioButton rb_phonePe,rb_others,rb_paytm,rb_gpay;
    String radio_upipay="",appName="",package_id= "",phonepay_val="";
    ArrayList<String> newList = new ArrayList<String>();
    String toDtae="",tofrom="";
    TextView tv_todate, tv_fromdate;
    DatePickerDialog.OnDateSetListener tosetListener,fromsetListener;
    Button btn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestFundsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
    void initViews() {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        rb_phonePe = findViewById(R.id.rb_phonePe);
        rb_others = findViewById(R.id.rb_others);
        rb_paytm = findViewById(R.id.rb_paytm);
        rb_gpay=findViewById (R.id.rb_gpay);
        btn_date=findViewById (R.id.btn_date);
        tv_todate = findViewById (R.id.tv_todate);
        tv_todate.setFocusableInTouchMode(false);
        tv_fromdate = findViewById (R.id.tv_fromdate);
        tv_fromdate.setFocusableInTouchMode(false);
        tv_todate.setFocusable(false);
        tv_todate.setKeyListener(null);
        tv_fromdate.setFocusable(false);
        tv_fromdate.setKeyListener(null);
        tv_todate.setOnClickListener (this);
        btn_date.setOnClickListener (this);
        tv_fromdate.setOnClickListener (this);
        binding.btnAmt1.setOnClickListener(this);
        binding.btnAmt2.setOnClickListener(this);
        binding.btnAmt10.setOnClickListener(this);

        binding.tvTxt.setVisibility (View.INVISIBLE);
        binding.linWhatsappSection.setVisibility(View.INVISIBLE);
        binding.linRequest.setVisibility(View.INVISIBLE);

        String date = new SimpleDateFormat ("dd/MM/yyyy", Locale.getDefault()).format(new Date ());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        dateFormat.format(cal.getTime());

        tv_todate.setText (dateFormat.format(cal.getTime()));
        toDtae=tv_todate.getText ().toString ();
        tv_fromdate.setText (date) ;
        tofrom=tv_fromdate.getText ().toString ();

        binding.rvTrans.setLayoutManager (new LinearLayoutManager (RequestFundsActivity.this));
        binding.btnSubmit.setOnClickListener(this);
        binding.linWhtsapp.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Money");

        binding.rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_phonePe:
                        radio_upipay="Phonepe";
                        break;
                    case R.id.rb_others:
                        radio_upipay="OTHER";
                        break;
                    case R.id.rb_gpay:
                        radio_upipay="GPay";
                        break;
                    case R.id.rb_paytm:
                        radio_upipay="Paytm";
                        break;
                }
            }
        });
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);


        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());
                binding.tvWalletWinning.setText(walletModel.getWinning_wallet_points());

            }
        });
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                binding.tvWithdrawMsg.setText(model.getAdd_text());
                whatsapp_no = model.getWhatsapp();
                binding.tvWhatsaap.setText(common.trimMobileNumber(model.getWhatsapp()));
                min_amt = Integer.parseInt(common.checkNullNumber(model.getMin_amount()));
                upi = common.checkNullString(model.getUpi());
                upi_name = common.checkNullString(model.getUpi_name());
                upi_desc = common.checkNullString(model.getUpi_desc());
                upi_type = common.checkNullString(model.getUpi_type());
                upi_status = common.checkNullString(model.getUpi_status());
                fund_text = common.checkNullString(model.getAdd_fund_text());
                request_type_status = common.checkNullString(model.getRequest_status());
                image_status = common.checkNullString(model.getUpi_image_status());
                upitype=common.checkNullString(model.getUpi_status ());
//                upitype="1";

                if(upitype.equalsIgnoreCase ("0")) {
                    binding.tvTxt.setVisibility (View.GONE);
                    binding.linWhatsappSection.setVisibility(View.GONE);
                    binding.linRequest.setVisibility(View.VISIBLE);
                    binding.rgOption.setVisibility(View.GONE);
                }else if (upitype.equals("2")){
                    binding.tvTxt.setVisibility (View.GONE);
                    binding.linWhatsappSection.setVisibility(View.VISIBLE);
                    binding.linRequest.setVisibility(View.GONE);
                } else if (upitype.equals("1")) {
                    binding.tvTxt.setVisibility (View.VISIBLE);
                    binding.linWhatsappSection.setVisibility(View.GONE);
                    binding.linRequest.setVisibility(View.VISIBLE);

                }//                image_status = "0";

                qrCode_image = common.checkNullString(model.getUpi_image());
                binding.tvNote.setText(fund_text);
//
//                try {
//
//                if (image_status.equals("0")){
//                    Picasso.with(RequestFundsActivity.this).load(R.drawable.myapp_logo).error(R.drawable.myapp_logo).into(binding.imgLogo);
//                }else {
//                    Picasso.with(RequestFundsActivity.this).load(qrCode_image).error(R.drawable.myapp_logo).into(binding.imgLogo);
//                    binding.imgLogo.setVisibility(View.VISIBLE);
////                    binding.imgLogo.getLayoutParams().height = 200;
//                    binding.imgLogo.getLayoutParams().height = 780;
//                    binding.imgLogo.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//                }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }

//                Log.e("upi_status",upi_status);
//                if(image_status.equals("1")) {
//                    Picasso.with(RequestFundsActivity.this).load(R.drawable.myapp_logo).error(R.drawable.myapp_logo).into(binding.imgLogo);
//                    binding.linAddPoint.setVisibility(View.VISIBLE);
////                    binding.tvWithdrawMsg.setVisibility(View.GONE);
////                    binding.linWhtsapp.setVisibility(View.GONE);
//                    binding.linQrCode.setVisibility(View.GONE);
//                    binding.rbPhonePe.setVisibility(View.VISIBLE);
//                    binding.rbOthers.setVisibility(View.VISIBLE);
//                }
//                else {
//                    if (fund_text.equals("")){
//                        binding.linQrCode.setVisibility(View.GONE);
//                    }
//
//                    Picasso.with(RequestFundsActivity.this).load(qrCode_image).error(R.drawable.myapp_logo).into(binding.imgLogo);
//                    binding.imgLogo.setVisibility(View.VISIBLE);
////                    binding.imgLogo.getLayoutParams().height = 200;
//                    binding.imgLogo.getLayoutParams().height = 780;
//                    binding.imgLogo.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//
//                    binding.linAddPoint.setVisibility(View.VISIBLE);
////                    binding.tvWithdrawMsg.setVisibility(View.VISIBLE);
////                    binding.linWhtsapp.setVisibility(View.GONE);
//                    binding.rbPhonePe.setVisibility(View.GONE);
//                    binding.rbOthers.setVisibility(View.GONE);
////                    binding.linQrCode.setVisibility(View.VISIBLE);
//                }

//                if(request_type_status.equals("0")) {
//                    binding.rbPhonePe.setVisibility(View.GONE);
//                    binding.rbOthers.setVisibility(View.GONE);
//                } else {
//                    binding.rbPhonePe.setVisibility(View.VISIBLE);
//                    binding.rbOthers.setVisibility(View.VISIBLE);
//                }

            }
        });
        }else {
            common.showToast("No Internet Connection");
        }
        installedApps();


        binding.imgCopy.setOnClickListener(new View.OnClickListener() { // set onclick listener to my textview
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(binding.tvNote.getText().toString());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_amt_1:
                binding.etPoints.setText("1");
                break;
            case R.id.btn_amt_2:
                binding.etPoints.setText("2");
                break;
            case R.id.btn_amt_10:
                binding.etPoints.setText("10");
                break;

            case R.id.btn_date:
                if (toDtae.equals ("")||tofrom.equals (""))
                {
                    Toast.makeText (this, "Please select dates", Toast.LENGTH_SHORT).show ( );
                }
                else
                {
                    getRequestData(session_management.getUserDetails().get(KEY_ID));
                }
                break;

            case R.id.tv_fromdate:
                Calendar calendars = Calendar.getInstance ( );
                final int years = calendars.get (Calendar.YEAR);
                final int months = calendars.get (Calendar.MONTH);
                final int days = calendars.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogs = new DatePickerDialog (RequestFundsActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, fromsetListener, years, months, days);
                datePickerDialogs.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                datePickerDialogs.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
                datePickerDialogs.show ( );
                datePickerDialogs.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
                datePickerDialogs.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


                fromsetListener = new DatePickerDialog.OnDateSetListener ( ) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        String outputPattern = "dd/MM/yyyy";
                        SimpleDateFormat outputFormat = new SimpleDateFormat (outputPattern);
                        Date date1 = null;
                        try {
                            date1 = outputFormat.parse (date);

                        } catch (ParseException e) {
                            e.printStackTrace ( );
                        }
                        String d = outputFormat.format (date1);
                        tv_fromdate.setText (d);
                        tofrom = tv_fromdate.getText ( ).toString ( );


                    }
                };
                break;


            case R.id.tv_todate:
                Calendar calendar = Calendar.getInstance ( );
                final int year = calendar.get (Calendar.YEAR);
                final int month = calendar.get (Calendar.MONTH);
                final int day = calendar.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog (RequestFundsActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, tosetListener, year, month, day);
                datePickerDialog.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                datePickerDialog.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
                datePickerDialog.show ( );
                datePickerDialog.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
                datePickerDialog.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


                tosetListener = new DatePickerDialog.OnDateSetListener ( ) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        String outputPattern = "dd/MM/yyyy";
                        SimpleDateFormat outputFormat = new SimpleDateFormat (outputPattern);
                        Date date1 = null;
                        try {
                            date1 = outputFormat.parse (date);

                        } catch (ParseException e) {
                            e.printStackTrace ( );
                        }
                        String d = outputFormat.format (date1);
                        tv_todate.setText (d);
                        toDtae = tv_todate.getText ( ).toString ( );


                    }
                };
                break;
            case R.id.btn_submit:
                String point = binding.etPoints.getText().toString();

//                if(upi.isEmpty ())
//                {
//                    common.showToast("No upi found");
//                }
                 if (point.isEmpty()) {
                    binding.etPoints.setError("Enter Points");
                } else {
                    int points = Integer.parseInt(common.checkNullNumber(point));
                    if (points < min_amt) {
                        common.showToast("Minimum Request Amount is " + min_amt);
                    }
                    else {
                        transactionId = "TXN" + System.currentTimeMillis();
                        String payeeVpa = upi;
                        String payeeName = upi_name;
                        String transactionRefId = transactionId;
                        String description = upi_desc;
                        String amount = point + ".00";

                        if (upitype.equals("0")) {
                            addRequest(session_management.getUserDetails().get(KEY_ID), point, "pending", transactionRefId,radio_upipay);
                        } else {
                            if (radio_upipay.equals("")){
                                common.showToast("Please choose any payment method");
                            }else {
                                if(radio_upipay.equalsIgnoreCase ("OTHER")) {
                                    // payViaUpi(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
                                    try {
                                        newList.clear();
                                        newList.addAll(UpiPayment.getExistingUpiApps(RequestFundsActivity.this));
                                        newList.remove(newList.indexOf("phonepe"));
                                    }catch (Exception exception){
                                        exception.printStackTrace();
                                    }
                                    startUpiPayment (transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
                                }
                                else{
                                    payViaUpi(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
                                }
                            }
//                            payViaUpi(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);
                        }
                    }
                }
                break;
            case R.id.lin_whtsapp:
                common.whatsapp(whatsapp_no,"Hi ! Admin");
                break;

        }
    }

    public void addRequest(String user_id, String points, String status, String txn_id,String type) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("points", points);
        if(radio_upipay.equalsIgnoreCase ("Phonepe"))
        {
            params.put("request_status","pending");
        }
        else {
            params.put("request_status",status);
        }
        params.put("type", "Add");
        params.put("trans_id", txn_id);
        params.put("details", "");
        params.put("method", radio_upipay);
        params.put("upi_name",upi_name);
//        params.put("txn_id", txn_id);
        params.put("wallet", session_management.getUserDetails().get(KEY_WALLET));
        common.postRequest(URL_INSERT_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e(TAG, "onResponse: "+ resp);
                loadingBar.dismiss();
                try {
                    JSONObject resonse = new JSONObject(resp);
                    if (resonse.getBoolean("responce")) {
                        common.showToast(resonse.getString("message"));
                        finish();
                    } else {
                        common.showToast(resonse.getString("error"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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

    private void payViaUpi(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        // START PAYMENT INITIALIZATION
        Log.e("radio_upipay",radio_upipay);
        upi_flag = true;
        mEasyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(description)
                .setAmount(amount)
//                .setPayeeMerchantCode("09907016908")
                .build();

        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this);

     Log.e("radio_upipay",radio_upipay);
        switch (radio_upipay) {
            case "Phonepe":
                mEasyUpiPayment.setDefaultPaymentApp(PHONE_PE);
                break;
            case "Paytm":
                mEasyUpiPayment.setDefaultPaymentApp(PAYTM);
                break;
            case "GPay":
                mEasyUpiPayment.setDefaultPaymentApp(GOOGLE_PAY);
                break;
            default:
                mEasyUpiPayment.setDefaultPaymentApp(NONE);
                break;
        }
        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        // END INITIALIZATION

        // START PAYMENT
        mEasyUpiPayment.startPayment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upi_flag) {
            mEasyUpiPayment.detachListener();
        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails", "" + transactionDetails);
        if (transactionDetails.getStatus().equalsIgnoreCase("success")) {
//            String user_id= Prevalent.currentOnlineuser.getId();
            String user_id = session_management.getUserDetails().get(KEY_ID);
            addRequest(user_id, transactionDetails.getAmount(), "approved", transactionDetails.getTransactionId(),radio_upipay);

        } else {
            common.showToast("Payment Failed.");
        }

    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        common.showToast("Failed");
    }

    @Override
    public void onTransactionCancelled() {
        common.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        common.showToast("App Not Found");
    }
    private void startUpiPayment(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        PaymentDetail payment = new PaymentDetail(payeeVpa,payeeName,
                "","", description,String.valueOf(Double.parseDouble(amount)));

        new UpiPayment(this)
                .setPaymentDetail(payment)
//                .setUpiApps(newList.remove(newList.indexOf("phonepe")))
                .setUpiApps(newList)
                .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                    @Override
                    public void onSuccess(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("success", String.valueOf(transactionDetails));
                        Toast.makeText(RequestFundsActivity.this, "transaction sucess: " + transactionDetails, Toast.LENGTH_LONG).show();
                        if(transactionDetails.getStatus().equalsIgnoreCase("success")) {
                            String user_id = session_management.getUserDetails().get(KEY_ID);
                            addRequest (user_id, amount, "approved", transactionDetails.getTransactionId ( ).toString ( ), radio_upipay);
                        }
                        else {
                            common.showToast("Payment Failed.");
                        }
                    }

                    @Override
                    public void onSubmitted(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("onSubmitted", String.valueOf(transactionDetails));
                        Toast.makeText(RequestFundsActivity.this, "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError( String message) {
                        Log.e("error", String.valueOf(message));
                        Toast.makeText(RequestFundsActivity.this, "transaction failed: " + message, Toast.LENGTH_LONG).show();
                    }

                }).pay();
    }

    public void installedApps()
    {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("Appname", appName);
                if(appName.equalsIgnoreCase ("PhonePe"))
                {
                    phonepay_val="true";
                }
                Log.e ("allval", "installedApps: "+phonepay_val );
            }
        }

        if(phonepay_val.equalsIgnoreCase ("true"))
        {
            rb_phonePe.setVisibility (View.VISIBLE);
        }
        else
        {
            rb_phonePe.setVisibility (View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart ( );
        getRequestData(session_management.getUserDetails().get(KEY_ID));
    }

    public void getRequestData(final String user_id) {

        loadingBar.show();
        String json_tag="json_req";
        final HashMap<String,String> params=new HashMap<String,String>();
        params.put("user_id",user_id);
        if(!toDtae.equals("")) {

            params.put ("to_date", common.changeDateFormat(tofrom));
            params.put ("from_date", common.changeDateFormat(toDtae));
        }
        Log.e("param", params.toString());
        wlist.clear();
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, Url_req_history, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("point_histry", response.toString());
                loadingBar.dismiss();
                try {
                    boolean res = response.getBoolean("responce");
                    if (res) {
                        JSONArray data = response.getJSONArray("data");

                        wlist.clear();
                        for (int i=0;i<data.length();i++) {
                            AddWithdrawModel model = new AddWithdrawModel();
                            JSONObject object = data.getJSONObject(i);
                            model.setRequest_id(object.getString("request_id"));
                            model.setRequest_points(object.getString("request_points"));
                            model.setTime(object.getString("time"));
                            model.setRequest_id(object.getString("user_id"));
                            model.setType (object.getString("type"));
                            model.setRequest_status (object.getString("request_status"));
                            model.setMethod(object.getString("method"));
                            model.setWhatsapp (object.getString("whatsapp"));
                            model.setDetails (object.getString("details"));
                            model.setTrans_id (object.getString("trans_id"));

                            if (object.getString("type").equals("Add")) {
                                wlist.add(model);
                            }
                        }
                        Log.d("w_list", String.valueOf(wlist.size()));
                        if (wlist.size()>0) {
                            binding.rvTrans.setVisibility (View.VISIBLE);
                            transactionHistoryAdapter= new TransactionHistoryAdapter (RequestFundsActivity.this,wlist);
                            binding.rvTrans.setAdapter(transactionHistoryAdapter);
                            transactionHistoryAdapter.notifyDataSetChanged();
                        } else {
                            binding.rvTrans.setVisibility (View.GONE);
                            common.showToast("No History Available");
                        }
                    } else {
                        common.showToast(""+response.get("error").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.VolleyErrorMessage(error);

            }
        });
        common.increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);
    }
}