package in.games.luckymatkagames.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.luckymatkagames.Adapter.TransactionHistoryAdapter;
import in.games.luckymatkagames.Adapter.WithdrawMethodAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Fragment.WalletFragment;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.AddWithdrawModel;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.Model.TimeSlots;
import in.games.luckymatkagames.Model.WalletObjects;

import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityWithdrawRequestBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.luckymatkagames.Config.BaseUrl.URL_REGISTER;
import static in.games.luckymatkagames.Config.Constants.KEY_ACCOUNNO;
import static in.games.luckymatkagames.Config.Constants.KEY_BANK_NAME;
import static in.games.luckymatkagames.Config.Constants.KEY_HOLDER;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_IFSC;
import static in.games.luckymatkagames.Config.Constants.KEY_PAYTM;
import static in.games.luckymatkagames.Config.Constants.KEY_PHONEPAY;
import static in.games.luckymatkagames.Config.Constants.KEY_TEZ;
import static in.games.luckymatkagames.Config.Constants.KEY_WALLET;

public class WithdrawRequest extends AppCompatActivity implements View.OnClickListener {
    ActivityWithdrawRequestBinding binding ;
   // ArrayList<SpinnerModel> o_list;
   ArrayList<String> o_list;
    ArrayList<Integer> ol_list;
    Activity ctx = WithdrawRequest.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    LinearLayout lin_upi;
    TextView tv_note;
    ArrayList<AddWithdrawModel> wlist ;
    ArrayList<TimeSlots> timeList;
    TransactionHistoryAdapter transactionHistoryAdapter ;
    int wMinAmt=0 ,w_winning_amount=0,wallet_amt=0;
    String wSunday="",wSaturday="" ,wType="",details="",request_status,upi_status;
    Button btn_date,btn_addbank,btn_history;
    String num="",title="";
    ArrayList<MenuModel> methodList ;
    WithdrawMethodAdapter withdrawMethodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    void initViews()
    {
        btn_history=findViewById (R.id.btn_history);
        btn_addbank=findViewById (R.id.btn_addbank);
        btn_history.setOnClickListener (this);
        btn_addbank.setOnClickListener (this);
        tv_note=findViewById (R.id.tv_note);
        lin_upi=findViewById (R.id.lin_upi);
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        o_list = new ArrayList<>();
        ol_list = new ArrayList<>();
        timeList = new ArrayList<>();
        wlist = new ArrayList<>();
        methodList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Withdraw Points");
        o_list.clear();
        o_list.add("SELECT WITHDRAW ACCOUNT");

        binding.rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_phonePe:
                        wType="Phonepe";
                        details = session_management.getUserDetails().get(KEY_PHONEPAY);
                        break;
                    case R.id.rb_gpay:
                        wType="Google Pay";
                        details = session_management.getUserDetails().get(KEY_TEZ);
                        break;
                }
            }
        });

        if (!common.checkNull(session_management.getUserDetails().get(KEY_ACCOUNNO))) {
           //o_list.add(new SpinnerModel ("bank",R.drawable.bank));
            o_list.add("bank");
            ol_list.add(R.drawable.bank);
        }

       if (!common.checkNull(session_management.getUserDetails().get(KEY_PAYTM))) {
           o_list.add("paytm");
           // o_list.add(new SpinnerModel ("paytm",R.drawable.paytm));
            ol_list.add(R.drawable.paytm);
            binding.rbPaytm.setVisibility(View.VISIBLE);
        }else {
           binding.rbPaytm.setVisibility(View.GONE);
       }

        if (!common.checkNull(session_management.getUserDetails().get(KEY_PHONEPAY))) {
            o_list.add("phonepe");
            //o_list.add(new SpinnerModel ("phonepe",R.drawable.phonepe));
            ol_list.add(R.drawable.phonepe);
            binding.rbPhonePe.setVisibility(View.VISIBLE);
        }else {
            binding.rbPhonePe.setVisibility(View.GONE);
        }
      if (!common.checkNull(session_management.getUserDetails().get(KEY_TEZ)))
        {     o_list.add("google pay");
           // o_list.add(new SpinnerModel ("google pay",R.drawable.gpay));
            ol_list.add(R.drawable.gpay);
            binding.rbGpay.setVisibility(View.VISIBLE);
        }else {
          binding.rbGpay.setVisibility(View.GONE);
      }


        ArrayAdapter<Integer> adapter = new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_dropdown_item,
               o_list);
        binding.spinMehtods.setAdapter(adapter);

//
        binding.spinMehtods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               // Log.d ("bavj", "onItemSelected: "+ (o_list.get (i).getName ().toString().toLowerCase()));
                //switch (o_list.get (i).getName ().toString().toLowerCase())
                switch (o_list.get (i).toString ().toLowerCase ())
                {
                    case "paytm":
                        wType="Paytm";
                        details = session_management.getUserDetails().get(KEY_PAYTM);
                        break;
                    case "phonepe":
                        wType="Phonepe";
                        details = session_management.getUserDetails().get(KEY_PHONEPAY);
                        break;
                    case "google pay":
                        wType="Google Pay";
                        details = session_management.getUserDetails().get(KEY_TEZ);
                        break;
                    case "bank":
                        wType = "Bank";
                        details = "Account Holder Name - "+ session_management.getUserDetails().get(KEY_HOLDER) +"\n" +"Account Number - "
                                + session_management.getUserDetails().get(KEY_ACCOUNNO) +"\n"
                                +"Ifsc Code - "+  session_management.getUserDetails().get(KEY_IFSC);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            common.getConfigData(new OnConfigData() {
                @Override
                public void onGetConfigData(ConfigModel model) {
                    if (common.checkNull (model.getWithdraw_text ( )))
                    {
                        binding.tvWithdrawMsg.setText (" ");
                    }
                    else {
                        binding.tvWithdrawMsg.setText (model.getWithdraw_text ( ));
                    }
                    upi_status=common.checkNullNumber (model.getUpi_status ());
                    request_status=common.checkNullString (model.getRequest_status ());
                    if(request_status.equals ("0")){
                        binding.tvOr.setVisibility (View.GONE);
                        binding.relWhatsapp.setVisibility (View.GONE);
                    }
                    else {
//                    binding.tvOr.setVisibility (View.VISIBLE);
//                    binding.relWhatsapp.setVisibility (View.VISIBLE);
                    }

                    if (request_status.equals ("1")){
//                    binding.tvOr.setVisibility (View.VISIBLE);
                    }

                    Log.e("timeslots",model.getTime_slot());
                    try {
                        JSONArray arr = new JSONArray(model.getTime_slot());
                        for (int i = 0 ; i<arr.length();i++)
                        {   TimeSlots timeSlots = new TimeSlots();
                            JSONObject object = arr.getJSONObject(i);
                            timeSlots.setStart_time(object.getString("start_time"));
                            timeSlots.setEnd_time(object.getString("end_time"));
                            timeSlots.setId(object.getString("id"));
                            timeSlots.setStatus(object.getString("status"));
                            timeList.add(timeSlots);
                        }
                        wMinAmt = Integer.parseInt(model.getW_amount());
//                    w_winning_amount = Integer.parseInt(model.getMain_wallet_points());
                        wSaturday = model.getW_saturday();
                        wSunday = model.getW_sunday();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }});
        }else {
            common.showToast("No Internet Connection");
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("btnSubmit", "onClick: "+session_management.getUserDetails().get(KEY_WALLET));
                String point = binding.etPoints.getText().toString();
                if (point.isEmpty()) {
                    binding.etPoints.setError("Enter Points ");
                } else {
                    int points = Integer.parseInt(common.checkNullNumber(point));
                    if (points < wMinAmt) {
                        common.showToast("Minimum Withdraw Amount is " + wMinAmt);
                    } else {
                        if (points > (Integer.parseInt(session_management.getUserDetails().get(KEY_WALLET)))){
                       // if (points > Integer.parseInt(session_management.getUserDetails().get(KEY_WALLET_WINNING))) {
                            common.showToast("Your requested amount exceeded");
                        } else {
                            if (wType.isEmpty()||wType.equalsIgnoreCase ("")||
                                    wType.equalsIgnoreCase ("SELECT WITHDRAW ACCOUNT")) {

                                if (o_list.size()==0){
                                    common.showToast("Please Fill Atlest One Payment Detail");
                                    finish();
                                    Intent i = new Intent(WithdrawRequest.this, WalletFragment.class);
                                    startActivity(i);
                                }else {
                                    common.showToast("Select Any One Withdraw Type");
                                }
                            } else {
                                addWithdrawRequest(point,wType,details);
                            }
                        }
                    }
                }
            }


        });
        binding.rvMenuPayment.setLayoutManager(new LinearLayoutManager(WithdrawRequest.this));
        menuPaymentType();

        binding.rvMenuPayment.addOnItemTouchListener(new RecyclerTouchListener(this, binding.rvMenuPayment, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                num="";
                title="";
                Fragment fm = null;
                Intent i = null;
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

                switch (position)
                {
                    case 0:
//                        i = new Intent(WithdrawRequest.this, BankDetailActivity.class);

                        BottomSheetDialog dialog3 = new BottomSheetDialog(WithdrawRequest.this);
                        dialog3.requestWindowFeature (Window.FEATURE_NO_TITLE);
                        dialog3.setContentView(R.layout.dialog_bank_account);
//                        dialog3.getWindow ( ).setBackgroundDrawable (new ColorDrawable (0));

                        Button btn_bank = (Button) dialog3.findViewById (R.id.btn_save);
                        EditText et_bank = (EditText) dialog3.findViewById (R.id.et_acc_number);
                        EditText et_ifsc = (EditText) dialog3.findViewById (R.id.et_ifsc);
                        Log.e("dxcfvghj",common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));
                        et_bank.setText (common.checkNullString(session_management.getUserDetails().get(KEY_ACCOUNNO)));
                        et_ifsc.setText (common.checkNullString(session_management.getUserDetails().get(KEY_IFSC)));

                        btn_bank.setOnClickListener (new View.OnClickListener ( ) {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                String accno = et_bank.getText ( ).toString ( );
                                String ifsc = et_ifsc.getText ( ).toString ( );

                                if (common.allValidation("account_number", et_bank).equalsIgnoreCase("correct") &&
                                        common.allValidation("ifsc_code", et_ifsc).equalsIgnoreCase("correct")) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        updateBankDetails(session_management.getUserDetails().get(KEY_ID), "", accno, ifsc, "");
                                    } else{
                                        common.showToast("No Internet Connection");
                                    }
                                }
                            }
                        });

                        dialog3.show();
                        dialog3.setCanceledOnTouchOutside (true);
                        dialog3.setCancelable (true);

                        break;
                    case 1:

                        BottomSheetDialog dialog2 = new BottomSheetDialog(WithdrawRequest.this);
                        dialog2.setContentView(R.layout.dialog_phonepe_layout);
                        dialog2.getWindow ();
                        dialog2.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
                        Button btn_ppe = (Button) dialog2.findViewById (R.id.btn_save);
                        EditText et_ppe = (EditText) dialog2.findViewById (R.id.et_mob);

                        et_ppe.setText (common.checkNullString(session_management.getUserDetails().get(KEY_PHONEPAY)));
                        btn_ppe.setOnClickListener (new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                String phonpe = et_ppe.getText ( ).toString ( );
                                if (et_ppe.getText ( ).toString ( ).isEmpty ( )) {
                                    et_ppe.setError ("Required phonepe number");
                                    et_ppe.requestFocus ( );
                                } else if (phonpe.length ( ) != 10) {
                                    et_ppe.setError ("Invalid Number");
                                    et_ppe.requestFocus ( );
                                } else if (Integer.parseInt (String.valueOf (phonpe.charAt (0))) < 6) {
                                    et_ppe.setError ("Invalid Number");
                                    et_ppe.requestFocus ( );
                                } else {
                                    params.put("phonepay",phonpe);
                                    num=phonpe;
                                    title="phonepe";
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
                                    dialog2.dismiss ( );
                                }
                            }
                        });
                        dialog2.show();
                        dialog2.setCanceledOnTouchOutside (true);
                        dialog2.setCancelable (true);


                        //new
//                        i=new Intent(WalletFragment.this, PhonepeFragment.class);
                        //old

                        break;
                    case 3:
//                        i=new Intent(WalletFragment.this, PaytmFragment.class);
                        //old

                        BottomSheetDialog dialog = new BottomSheetDialog(WithdrawRequest.this);
                        dialog.setContentView(R.layout.dialog_paytm_layout);
                        dialog.getWindow ();
                        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
                        Button btn_paytm = (Button) dialog.findViewById (R.id.btn_save);
                        EditText et_paytm = (EditText) dialog.findViewById (R.id.et_mob);

                        et_paytm.setText (common.checkNullString(session_management.getUserDetails().get(KEY_PAYTM)));
                        btn_paytm.setOnClickListener (new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                String paytmno = et_paytm.getText ( ).toString ( );

                                if (et_paytm.getText ( ).toString ( ).isEmpty ( )) {
                                    et_paytm.setError ("Required paytm number");
                                    et_paytm.requestFocus ( );
                                } else if (paytmno.length ( ) != 10) {
                                    et_paytm.setError ("Invalid Number");
                                    et_paytm.requestFocus ( );
                                } else if (Integer.parseInt (String.valueOf (paytmno.charAt (0))) < 6) {
                                    et_paytm.setError ("Invalid Number");
                                    et_paytm.requestFocus ( );
                                } else {
                                    params.put("paytm",paytmno);
                                    num=paytmno;
                                    title="paytm";
                                    if (ConnectivityReceiver.isConnected()) {
                                        if (!num.equals("")) {
                                            Log.e("vgbhjnk",num);
                                            storeDetails((HashMap<String, String>) params, num, title);
                                        }
                                    } else {
                                        common.showToast("No Internet Connection");
                                    }
                                    dialog.dismiss ( );
                                }
                            }
                        });

                        dialog.show();
                        dialog.setCanceledOnTouchOutside (true);
                        dialog.setCancelable (true);
                        break;

                    case 2:
//                        i=new Intent(WalletFragment.this, GpayFragment.class);

                        //new
                        BottomSheetDialog dialog4 = new BottomSheetDialog(WithdrawRequest.this);
                        dialog4.setContentView(R.layout.dialog_googlepay_layout);
                        dialog4.getWindow ();
                        dialog4.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
                        Button btn_gpay = (Button) dialog4.findViewById (R.id.btn_save);
                        EditText et_gpay = (EditText) dialog4.findViewById (R.id.et_mob);
                        Log.e("dxcfvghj",common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));
                        et_gpay.setText (common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));

                        btn_gpay.setOnClickListener (new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                String gpay = et_gpay.getText ( ).toString ( );
                                if (et_gpay.getText ( ).toString ( ).isEmpty ( )) {
                                    et_gpay.setError ("Required google pay number");
                                    et_gpay.requestFocus ( );
                                } else if (gpay.length ( ) != 10) {
                                    et_gpay.setError ("Invalid Number");
                                    et_gpay.requestFocus ( );
                                } else if (Integer.parseInt (String.valueOf (gpay.charAt (0))) < 6) {
                                    et_gpay.setError ("Invalid Number");
                                    et_gpay.requestFocus ( );
                                } else {
                                    params.put("tez",gpay);
                                    num=gpay;
                                    title="google pay";
                                    if (ConnectivityReceiver.isConnected()) {
                                        if (!num.equals("")) {
                                            Log.e("vgbhjnk",num);
                                            storeDetails((HashMap<String, String>) params, num, title);
                                        }
                                    } else {
                                        common.showToast("No Internet Connection");
                                    }
                                    dialog4.dismiss ( );
                                }
                            }
                        });

                        dialog4.show();
                        dialog4.setCanceledOnTouchOutside (true);
                        dialog4.setCancelable (true);

                        break;
//                    case 3:
//                        i = new Intent(getActivity(), UpiDetailsActivity.class);
//                        i.putExtra("title",methodList.get(position).getName());
//                        break;
                }

                if(fm!=null) {
                    FragmentManager fragmentManager = getSupportFragmentManager ( );
                    fragmentManager.beginTransaction ( ).add (R.id.main_frame, fm).addToBackStack (null).commit ( );
                }
                if(i!=null) {
                    startActivity(i);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void menuPaymentType() {
        methodList.clear();
//        list.add(new MenuModel(R.drawable.bank,"Bank Details"));
        methodList.add(new MenuModel(R.drawable.bank,"Bank Account"));
        methodList.add(new MenuModel(R.drawable.phonepe,"PhonePe"));
        methodList.add(new MenuModel(R.drawable.gpay,"Google Pay"));
        methodList.add(new MenuModel(R.drawable.paytm,"PayTm"));
        withdrawMethodAdapter =new WithdrawMethodAdapter(WithdrawRequest.this,methodList);
        binding.rvMenuPayment.setAdapter(withdrawMethodAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    public void addWithdrawRequest(String points,String wType,String details){
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        params.put("points",points);
        params.put("details",details);
        params.put("method",wType);
        params.put("trans_id","WTXN" + System.currentTimeMillis());
        params.put("request_status", "pending");
//        params.put("request_status", "approved");
        params.put("type", "Withdrawal");
        Log.d ("valllpost", "addWithdrawRequest: "+params);

        common.postRequest(URL_INSERT_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("witdrara", "onResponse: "+ resp);
                loadingBar.dismiss();
                try {
                    JSONObject response=new JSONObject(resp);
                    if(response.getBoolean("responce")){
                        common.showToast(""+response.getString("message"));
                        finish();
                        Intent i = new Intent(WithdrawRequest.this, WalletFragment.class);
                        startActivity(i);
                    }else{
                        common.showToast(""+response.getString("error"));
                    }

                }catch (Exception ex){
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

    @Override
    protected void onStart() {
        super.onStart ( );
        common.getWalletAmount (new OnGetWallet ( ) {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
//                binding.tvWallet.setText (walletModel.getWallet_points ( ));
//                binding.tvWalletWinning.setText(walletModel.getWinning_wallet_points());
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_history:
                Intent intent=new Intent ( WithdrawRequest.this,withdraw_history.class );
                startActivity (intent);
                break;

            case R.id.btn_addbank:
                Intent i = new Intent(WithdrawRequest.this, WalletFragment.class);
                startActivity(i);
                break;
        }
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
                    if(resp) {
                        switch (title.toLowerCase()) {

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

                    } else {
                        common.showToast("Something went wrong");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    common.showToast(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showToast(String.valueOf (error));
            }
        });
        common.increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_upi");

    }

    private void updateBankDetails(String user_id,final String bankname, final String  accno, final String ifsc, final String hod_name) {
        loadingBar.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","4");
        params.put("accountno",accno);
        params.put("bankname",bankname);
        params.put("ifsc",ifsc);
        params.put("accountholder",hod_name);
        params.put("user_id",user_id);
        Log.e("bank",params.toString());

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    Log.e("bank",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp) {
                        common.showToast(""+response.getString("message"));
                        session_management.updateAccSection(accno,bankname,ifsc,hod_name);
                        finish();
                    } else {
                        common.showToast("Something went wrong");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    common.showToast(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(msg);

                }
            }
        });
        common.increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_bank");

    }


}