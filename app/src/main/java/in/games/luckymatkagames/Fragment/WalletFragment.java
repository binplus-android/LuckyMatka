package in.games.luckymatkagames.Fragment;


import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.games.luckymatkagames.Activity.BankDetailActivity;
import in.games.luckymatkagames.Activity.MainActivity;
import in.games.luckymatkagames.Activity.RequestFundsActivity;
import in.games.luckymatkagames.Activity.WithdrawRequest;
import in.games.luckymatkagames.Adapter.MenuAdapter;
import in.games.luckymatkagames.Adapter.TransactionHistoryAdapter;
import in.games.luckymatkagames.Adapter.WithdrawMethodAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.AddWithdrawModel;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MenuModel;
import in.games.luckymatkagames.Model.WalletObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityMainBinding;
import in.games.luckymatkagames.databinding.ActivityWithdrawRequestBinding;
import in.games.luckymatkagames.databinding.FragmentWalletBinding;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.RecyclerTouchListener;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.Url_req_history;
import static in.games.luckymatkagames.Config.Constants.KEY_ACCOUNNO;
import static in.games.luckymatkagames.Config.Constants.KEY_BANK_NAME;
import static in.games.luckymatkagames.Config.Constants.KEY_HOLDER;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_IFSC;
import static in.games.luckymatkagames.Config.Constants.KEY_PAYTM;
import static in.games.luckymatkagames.Config.Constants.KEY_PHONEPAY;
import static in.games.luckymatkagames.Config.Constants.KEY_TEZ;


public class WalletFragment extends AppCompatActivity implements View.OnClickListener {
    FragmentWalletBinding binding ;
    Session_management session_management ;
    Common common ;
    LoadingBar loadingBar ;
    ArrayList<MenuModel> list,methodList ;
    ArrayList<AddWithdrawModel> wlist ;
    MenuAdapter menuAdapter;
    TransactionHistoryAdapter transactionHistoryAdapter ;
    String toDtae="",tofrom="",url="";
    TextView tv_todate, tv_fromdate;
    Button btn_date;
    DatePickerDialog.OnDateSetListener tosetListener,fromsetListener;
    WithdrawMethodAdapter withdrawMethodAdapter;
    String num="",title="",telegram_ink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        common.getUserStatus(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wallet");
        menuPaymentType();
        binding.rvMenuPayment.addOnItemTouchListener(new RecyclerTouchListener(WalletFragment.this, binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {
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
                        i = new Intent(WalletFragment.this, BankDetailActivity.class);

                        break;
                    case 1:
                        //new

                         i=new Intent(WalletFragment.this,PhonepeFragment.class);

                        //old

//                        Dialog dialog = new Dialog(WalletFragment.this);
//
//                        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
//                        dialog.setContentView (R.layout.dialog_paytm_layout);
//                        dialog.show ( );
//                        Button btn_paytm = (Button) dialog.findViewById (R.id.btn_save);
//                        EditText et_paytm = (EditText) dialog.findViewById (R.id.et_mob);
//
//                        et_paytm.setText (common.checkNullString(session_management.getUserDetails().get(KEY_PAYTM)));
//                        btn_paytm.setOnClickListener (new View.OnClickListener ( ) {
//                            @Override
//                            public void onClick(View v) {
//                                String paytmno = et_paytm.getText ( ).toString ( );
//
//                                if (et_paytm.getText ( ).toString ( ).isEmpty ( )) {
//                                    et_paytm.setError ("Required paytm number");
//                                    et_paytm.requestFocus ( );
//                                } else if (paytmno.length ( ) != 10) {
//                                    et_paytm.setError ("Invalid Number");
//                                    et_paytm.requestFocus ( );
//                                } else if (Integer.parseInt (String.valueOf (paytmno.charAt (0))) < 6) {
//                                    et_paytm.setError ("Invalid Number");
//                                    et_paytm.requestFocus ( );
//                                } else {
//                                    params.put("paytm",paytmno);
//                                    num=paytmno;
//                                    title="paytm";
//                                    if (ConnectivityReceiver.isConnected())
//                                    {
//                                        if (!num.equals("")) {
//                                            Log.e("vgbhjnk",num);
//                                            storeDetails((HashMap<String, String>) params, num, title);
//                                        }
//                                    }
//                                    else
//                                    {
//                                        common.showToast("No Internet Connection");
//                                    }
//                                    dialog.dismiss ( );
//                                }
//                            }
//                        });
//
//                        dialog.setCanceledOnTouchOutside (true);

                        break;
                    case 2:
                        //new
                        i=new Intent(WalletFragment.this,PaytmFragment.class);


                        //old
//                        Dialog dialog2 = new Dialog (WalletFragment.this);
//
//                        dialog2.requestWindowFeature (Window.FEATURE_NO_TITLE);
//                        dialog2.setContentView (R.layout.dialog_phonepe_layout);
//                        dialog2.show ( );
//                        Button btn_ppe = (Button) dialog2.findViewById (R.id.btn_save);
//                        EditText et_ppe = (EditText) dialog2.findViewById (R.id.et_mob);
//
//                        et_ppe.setText (common.checkNullString(session_management.getUserDetails().get(KEY_PHONEPAY)));
//                        btn_ppe.setOnClickListener (new View.OnClickListener ( ) {
//                            @Override
//                            public void onClick(View v) {
//                                String phonpe = et_ppe.getText ( ).toString ( );
//                                if (et_ppe.getText ( ).toString ( ).isEmpty ( )) {
//                                    et_ppe.setError ("Required phonepe number");
//                                    et_ppe.requestFocus ( );
//                                } else if (phonpe.length ( ) != 10) {
//                                    et_ppe.setError ("Invalid Number");
//                                    et_ppe.requestFocus ( );
//                                } else if (Integer.parseInt (String.valueOf (phonpe.charAt (0))) < 6) {
//                                    et_ppe.setError ("Invalid Number");
//                                    et_ppe.requestFocus ( );
//                                } else {
//                                    params.put("phonepay",phonpe);
//                                    num=phonpe;
//                                    title="phonepe";
//                                    if (ConnectivityReceiver.isConnected())
//                                    {
//                                        if (!num.equals("")) {
//                                            Log.e("vgbhjnk",num);
//                                            storeDetails((HashMap<String, String>) params, num, title);
//                                        }
//                                    }
//                                    else
//                                    {
//                                        common.showToast("No Internet Connection");
//                                    }
//                                    dialog2.dismiss ( );
//                                }
//                            }
//                        });
//                        dialog2.setCanceledOnTouchOutside (true);

                        break;
                    case 3:
                        i=new Intent(WalletFragment.this,GpayFragment.class);

                        //new


//                        Dialog dialog3 = new Dialog (WalletFragment.this);
//
//                        dialog3.requestWindowFeature (Window.FEATURE_NO_TITLE);
//                        dialog3.setContentView (R.layout.dialog_googlepay_layout);
//                        dialog3.show ( );
//                        Button btn_gpay = (Button) dialog3.findViewById (R.id.btn_save);
//                        EditText et_gpay = (EditText) dialog3.findViewById (R.id.et_mob);
//                        Log.e("dxcfvghj",common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));
//                        et_gpay.setText (common.checkNullString(session_management.getUserDetails().get(KEY_TEZ)));
//
//                        btn_gpay.setOnClickListener (new View.OnClickListener ( ) {
//                            @Override
//                            public void onClick(View v) {
//                                String gpay = et_gpay.getText ( ).toString ( );
//                                if (et_gpay.getText ( ).toString ( ).isEmpty ( )) {
//                                    et_gpay.setError ("Required google pay number");
//                                    et_gpay.requestFocus ( );
//                                } else if (gpay.length ( ) != 10) {
//                                    et_gpay.setError ("Invalid Number");
//                                    et_gpay.requestFocus ( );
//                                } else if (Integer.parseInt (String.valueOf (gpay.charAt (0))) < 6) {
//                                    et_gpay.setError ("Invalid Number");
//                                    et_gpay.requestFocus ( );
//                                } else {
//                                    params.put("tez",gpay);
//                                    num=gpay;
//                                    title="google pay";
//                                    if (ConnectivityReceiver.isConnected())
//                                    {
//                                        if (!num.equals("")) {
//                                            Log.e("vgbhjnk",num);
//                                            storeDetails((HashMap<String, String>) params, num, title);
//                                        }
//                                    }
//                                    else
//                                    {
//                                        common.showToast("No Internet Connection");
//                                    }
//                                    dialog3.dismiss ( );
//                                }
//                            }
//                        });
//
//                        dialog3.setCanceledOnTouchOutside (true);

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

    void  initViews()
    {
        session_management = new Session_management(WalletFragment.this);
        loadingBar= new LoadingBar(WalletFragment.this);
        common = new Common(WalletFragment.this);
        list = new ArrayList<>();
        methodList = new ArrayList<>();
        wlist = new ArrayList<>();
        tv_todate = binding.tvTodate;
        tv_todate.setFocusableInTouchMode(false);
        tv_fromdate = binding.tvFromdate;
        tv_fromdate.setFocusableInTouchMode(false);
        btn_date = binding.btnDate;
        tv_todate.setFocusable(false);
        tv_todate.setKeyListener(null);
        tv_fromdate.setFocusable(false);
        tv_fromdate.setKeyListener(null);
        tv_todate.setOnClickListener (this);
        tv_fromdate.setOnClickListener (this);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        dateFormat.format(cal.getTime());

        tv_todate.setText (dateFormat.format(cal.getTime()));
        toDtae=tv_todate.getText ().toString ();
        tv_fromdate.setText (date) ;
        tofrom=tv_fromdate.getText ().toString ();

        btn_date.setOnClickListener (this);

        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                binding.tvWithdrawMsg.setText(model.getWithdraw_text());
                telegram_ink=model.getTelegram_link ();
            }
        });
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());
            }
        });

        binding.rvMenu.setLayoutManager(new GridLayoutManager(WalletFragment.this,1, RecyclerView.HORIZONTAL,false));
        binding.rvMenuPayment.setLayoutManager(new GridLayoutManager(WalletFragment.this,2));
          // GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
//        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
////                if (position % 4 == 2) {
////                    return 2;
////                }
//                switch (position %4) {
//                    case 0:
//                    case 1:
//                    case 2: case 3:
//                        return 1;
//                    case 4:
////                    case 2:
//                        return 1;
//                    default:
//                        //never gonna happen
//                        return  -1 ;
//                }
//            }
//        });

       // binding.rvMenu.setLayoutManager (glm);
        binding.rvTrans.setLayoutManager(new LinearLayoutManager(WalletFragment.this));
        menuItem();
//        if (ConnectivityReceiver.isConnected())
//        {
//            getRequestData(session_management.getUserDetails().get(KEY_ID));
//        }
        binding.rvMenu.addOnItemTouchListener(new RecyclerTouchListener(WalletFragment.this, binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;
                String name=list.get (position).getName ();

                switch (name)
                {
                    case "ADD":
                        i = new Intent(WalletFragment.this, RequestFundsActivity.class);
                        break;
                    case "WITHDRAW":
                        i = new Intent(WalletFragment.this, WithdrawRequest.class);
                        break;
                    case "BANKS":
//                        i = new Intent(WalletFragment.this, WithdrawMethodsActivity.class);
                        i = new Intent(WalletFragment.this, BankDetailActivity.class);
                        break;
                    case "TELEGRAM":
                        intentT0TelegramId(telegram_ink);
                        break;

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

    public void intentT0TelegramId(String telegrm_link){
        final String appName = "org.telegram.messenger";
        Log.e("common_intentT0TelegramId",telegrm_link);
        try{
            // Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse ("tg://resolve?domain=partsilicon"));
            Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse (telegrm_link));
            intent.setPackage (appName);
            PackageManager pm = getPackageManager();
            if (intent.resolveActivity(pm) != null) {
              startActivity(intent);
            } else {
//                Toast.makeText(RequestActivity.this, "No Valid Link Found", Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegrm_link));
               startActivity(browserIntent);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Toast.makeText(WalletFragment.this, "No Valid Link Found", Toast.LENGTH_LONG).show();
        }

    }
    private void menuItem() {
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                binding.tvWithdrawMsg.setText(model.getWithdraw_text());
//                if (!model.getAdd_point_status().equals("0")){
//                    list.add(new MenuModel(R.drawable.add_fund,"ADD"));
//                }

                if (!model.getBank_status().equals("0")) {
                    list.add(new MenuModel(R.drawable.bank, "BANKS"));
                    // list.add(new MenuModel(R.drawable.icons8_data_transfer_96px,"TRANSFER"));
                }
                if (!model.getWithdraw_status().equals("0")){
                    list.add(new MenuModel(R.drawable.withdraw,"WITHDRAW"));
                }

//                 no value for telegarm
//                if (!model.getAdd_point_status().equals("0")){
//                    list.add(new MenuModel(R.drawable.icon_telegram,"TELEGRAM"));
//                }
                if (list.size()>0) {
                    menuAdapter = new MenuAdapter(WalletFragment.this, list, "wallet");
                    binding.rvMenu.setAdapter(menuAdapter);
                }
            }
        });

    }

//    private void menuItem() {
//        if (!model.getAdd_point_status().equals("0")){
//            list.add(new MenuModel(R.drawable.icons8_add_96px,"ADD"));}
//        if (!model.getWithdraw_status().equals("0")){
//            list.add(new MenuModel(R.drawable.icons8_minus_96px,"WITHDRAW"));
//
//        }
////        list.add(new MenuModel(R.drawable.icons8_add_96px,"TOP UP"));
////        list.add(new MenuModel(R.drawable.icons8_minus_96px,"WITHDRAW"));
//        list.add(new MenuModel(R.drawable.icons8_bank_96px,"BANKS"));
////        list.add(new MenuModel(R.drawable.icons8_data_transfer_96px,"TRANSFER"));
//
//        menuAdapter =new MenuAdapter(getActivity(),list,"wallet");
//        binding.rvMenu.setAdapter(menuAdapter);
//
//    }


    private void getRequestData(final String user_id) {

        loadingBar.show();
        String json_tag="json_req";
        final HashMap<String,String> params=new HashMap<String,String>();
        params.put("user_id",user_id);


        if(!toDtae.equals(""))
        {

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
                    if (res)
                    {
                        JSONArray data = response.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AddWithdrawModel>>() {
                        }.getType();
                        wlist = gson.fromJson(data.toString(), listType);
                        Log.d("w_list", String.valueOf(wlist.size()));

                        if (wlist.size()>0) {
                           transactionHistoryAdapter= new TransactionHistoryAdapter(WalletFragment.this,wlist);
                            binding.rvTrans.setAdapter(transactionHistoryAdapter);
                        }
                        else
                        {
                            common.showToast("No History Available");

                        }
                        if (transactionHistoryAdapter!=null){
                            transactionHistoryAdapter.notifyDataSetChanged();
                        }

                    }
                    else
                    {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_date:
                if (toDtae.equals ("")||tofrom.equals (""))
                {
                    Toast.makeText (WalletFragment.this, "Please select dates", Toast.LENGTH_SHORT).show ( );
                } else {
                    getRequestData(session_management.getUserDetails().get(KEY_ID));
                        url=Url_req_history;
                }
                break;

            case R.id.tv_fromdate:
                Calendar calendars = Calendar.getInstance ( );
                final int years = calendars.get (Calendar.YEAR);
                final int months = calendars.get (Calendar.MONTH);
                final int days = calendars.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogs = new DatePickerDialog (WalletFragment.this, android.R.style.Theme_Material_Light_DarkActionBar, fromsetListener, years, months, days);
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
                final int day= calendar.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog (WalletFragment.this, android.R.style.Theme_Material_Light_DarkActionBar, tosetListener, year, month, day);
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
        }
    }
    private void menuPaymentType() {
        methodList.clear();
//        list.add(new MenuModel(R.drawable.bank,"Bank Details"));
        methodList.add(new MenuModel(R.drawable.bank,"Bank"));
        methodList.add(new MenuModel(R.drawable.phonepe,"PhonePe"));
        methodList.add(new MenuModel(R.drawable.paytm,"PayTm"));
        methodList.add(new MenuModel(R.drawable.gpay,"Google Pay"));
        withdrawMethodAdapter =new WithdrawMethodAdapter(WalletFragment.this,methodList);
        binding.rvMenuPayment.setAdapter(withdrawMethodAdapter);

    }

    @Override
    public void onResume() {
        super.onResume ( );
       // getRequestData(session_management.getUserDetails().get(KEY_ID));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           finish();
           Intent i = new Intent(WalletFragment.this,MainActivity.class);
           startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i = new Intent(WalletFragment.this,MainActivity.class);
        startActivity(i);
    }

}