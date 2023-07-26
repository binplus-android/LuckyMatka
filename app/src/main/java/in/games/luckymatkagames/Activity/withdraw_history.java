package in.games.luckymatkagames.Activity;

import static in.games.luckymatkagames.Config.BaseUrl.Url_req_history;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
import java.util.Locale;

import in.games.luckymatkagames.Adapter.TransactionHistoryAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.AddWithdrawModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityWithdrawHistoryBinding;
import in.games.luckymatkagames.databinding.ActivityWithdrawRequestBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

public class withdraw_history extends AppCompatActivity implements View.OnClickListener {
    ActivityWithdrawHistoryBinding binding ;
    String toDtae="",tofrom="";
    TextView tv_todate, tv_fromdate;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    TransactionHistoryAdapter transactionHistoryAdapter ;
    DatePickerDialog.OnDateSetListener tosetListener,fromsetListener;
    Activity ctx = withdraw_history.this;
    Button btn_date;
    ArrayList<AddWithdrawModel> wlist ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = ActivityWithdrawHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initview();

        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            getRequestData(session_management.getUserDetails().get(KEY_ID));
        }else {
            common.showToast("No Internet Connection");
        }
    }

    private void initview() {
        binding.rvTrans.setLayoutManager(new LinearLayoutManager(withdraw_history.this));
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

        String date = new SimpleDateFormat ("dd/MM/yyyy", Locale.getDefault()).format(new Date ());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        dateFormat.format(cal.getTime());

        tv_todate.setText (dateFormat.format(cal.getTime()));
        toDtae=tv_todate.getText ().toString ();
        tv_fromdate.setText (date) ;
        tofrom=tv_fromdate.getText ().toString ();


        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);

        wlist = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Withdraw History");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

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

                        for (int i=0;i<data.length();i++) {
                            JSONObject object = data.getJSONObject(i);
                            AddWithdrawModel model = new AddWithdrawModel();
                            model.setText (object.getString ("method"));
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

                            if (object.getString("type").equals("Withdrawal")) {
                                wlist.add(model);
                            }
                        }
                        Log.d("w_list", String.valueOf(wlist.size()));
                        if (wlist.size()>0) {
                            binding.rvTrans.setVisibility (View.VISIBLE);
                            transactionHistoryAdapter= new TransactionHistoryAdapter (withdraw_history.this,wlist);
                            binding.rvTrans.setAdapter(transactionHistoryAdapter);
                            transactionHistoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {binding.rvTrans.setVisibility (View.GONE);
                            common.showToast("No History Available");
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
        switch (v.getId())
        {


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


                DatePickerDialog datePickerDialogs = new DatePickerDialog (withdraw_history.this, android.R.style.Theme_Material_Light_DarkActionBar, fromsetListener, years, months, days);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog (withdraw_history.this, android.R.style.Theme_Material_Light_DarkActionBar, tosetListener, year, month, day);
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
}