package in.games.luckymatkagames.Activity;

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
import java.util.Map;

import in.games.luckymatkagames.Adapter.AllHistoryAdapter;
import in.games.luckymatkagames.Adapter.StarhisAdapter;
import in.games.luckymatkagames.Adapter.WinAdapter;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.HistryModel;
import in.games.luckymatkagames.Model.SatrhisModel;
import in.games.luckymatkagames.Model.WinModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.databinding.ActivityHistryBinding;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_CASHINO_HISTORY;
import static in.games.luckymatkagames.Config.BaseUrl.URL_CASHINO_WIN;
import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_HISTORY;
import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_NEW_HISTORY;
import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_STARLINE_HISTORY;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;

public class HistryActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityHistryBinding binding ;
    ArrayList<HistryModel> b_list ,w_list ,list;
    Activity ctx = HistryActivity.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    String type,toDtae="",tofrom="",url="";
    AllHistoryAdapter historyAdapter;
    ArrayList<SatrhisModel> hlist;
    StarhisAdapter starhisAdapter;
    ArrayList<WinModel> winlist;
    TextView tv_todate, tv_fromdate,tv_no;
    Button btn_date;
    DatePickerDialog.OnDateSetListener tosetListener,fromsetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
        }else {
            common.showToast("No Internet Connection");
        }

    }
    void initViews() {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        b_list = new ArrayList<>();
        w_list = new ArrayList<>();
        list = new ArrayList<>();
        hlist = new ArrayList<> ( );
        winlist=new ArrayList<> (  );
        type = getIntent().getStringExtra("type");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type.equalsIgnoreCase("bid"))
        {
        getSupportActionBar().setTitle("Bid History");
       // binding.linHisDate.setVisibility(View.GONE);

        }else if (type.equalsIgnoreCase("star")) {
            getSupportActionBar().setTitle("Bid History");
          //  binding.linHisDate.setVisibility(View.GONE);

        } else {
            getSupportActionBar().setTitle("Win History");
          //  binding.linHisDate.setVisibility(View.VISIBLE);

        }
        binding.rvHistry.setLayoutManager(new LinearLayoutManager(ctx));

        tv_todate = findViewById (R.id.tv_todate);
        tv_no=findViewById (R.id.tv_no);

        tv_todate.setFocusableInTouchMode(false);
        tv_fromdate = findViewById (R.id.tv_fromdate);
        tv_fromdate.setFocusableInTouchMode(false);
        btn_date = findViewById (R.id.btn_date);
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

        if(ConnectivityReceiver.isConnected())
        {
            if (type.equalsIgnoreCase("bid")) {
                getBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_NEW_HISTORY);
                url=URL_GET_NEW_HISTORY;
            }else if (type.equalsIgnoreCase("win")){
                getBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_HISTORY);
                url=URL_GET_HISTORY;
            }else if (type.equalsIgnoreCase("star")) {
                getstarBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_NEW_HISTORY);
                url =URL_GET_NEW_HISTORY;
            }else if(type.equalsIgnoreCase("star_win")){
                getstarBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_STARLINE_HISTORY);
                url = URL_GET_STARLINE_HISTORY;
            } else if (type.equalsIgnoreCase ("casino")) {
                String matka_id = getIntent ( ).getStringExtra ("matka_id");
                Log.e ("cash_matka", matka_id);
                getSupportActionBar ( ).setTitle ("Casino History");
                getcashinoBidData (session_management.getUserDetails ( ).get (KEY_ID), matka_id);
            } else if (type.equalsIgnoreCase ("casino_win")) {
                //Toast.makeText (ctx, "fghjk", Toast.LENGTH_SHORT).show ( );
                getCasinoWindata (session_management.getUserDetails ( ).get (KEY_ID));
            }
        } else {
            common.showToast("No Internet Connection");
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    private void getBidData(String user_id,String api_url) {
       loadingBar.show();
       b_list.clear();
       w_list.clear();
       list.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        if(!toDtae.equals (""))
        {
            params.put ("to_date", common.changeDateFormat(tofrom));
            params.put ("from_date", common.changeDateFormat(toDtae));
        }
        if (type.equalsIgnoreCase("bid")){
            params.put("market_type","market");
        }
        Log.e("bid",params.toString());
        CustomJsonRequest jsonObjReq = new CustomJsonRequest(Request.Method.POST,
                api_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("bid_history", response.toString());
               loadingBar.dismiss();
                try {
                    boolean res = response.getBoolean("responce");
                    if (res) {
                        JSONArray data = response.getJSONArray("data");

                        for (int i =0 ;i <data.length();i++)
                        {
                            HistryModel model = new HistryModel();
                            JSONObject obj = data.getJSONObject(i);
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setUser_id(obj.getString("user_id"));

                            model.setDigits(obj.getString("digits"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("bet_type"));
                            model.setStatus(obj.getString("status"));
                            model.setName(obj.getString("market_name"));
                            if (type.equalsIgnoreCase("bid")){
                                model.setPoints(obj.getString("points"));
                            }else {
                                model.setPoints(obj.getString("amt"));
                            }
                            list.add(model);
                        }
                        Log.d("all_list", String.valueOf(list.size()));

                        for (int i = 0 ; i <list.size() ;i++) {
                            if (list.get(i).getStatus().equalsIgnoreCase("win")||list.get(i).getStatus().equalsIgnoreCase("won")) {
                                w_list.add(list.get(i));
                            }
                            b_list.add(list.get(i));



//                            if (list.get(i).getStatus().equalsIgnoreCase("win")||list.get(i).getStatus().equalsIgnoreCase("won")) {
//                                w_list.add(list.get(i));
//                            } else {
//                                if (!list.get(i).getStatus().equalsIgnoreCase("win")&&!list.get(i).getStatus().equalsIgnoreCase("won")) {
//                                    b_list.add(list.get(i));
//                                }
//                            }
                        }
                      if (type.equalsIgnoreCase("bid")) {
                          historyAdapter = new AllHistoryAdapter(ctx,b_list);
                      } else {
                          historyAdapter = new AllHistoryAdapter(ctx,w_list);
                      }
                      binding.rvHistry.setAdapter(historyAdapter);
                      historyAdapter.notifyDataSetChanged();

                    } else {
                       common.showToast(""+response.get("Error").toString());
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
        common.increaseResponseTime(jsonObjReq);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"histry");

    }

    private void getstarBidData(String user_id,String api_url) {

        loadingBar.show();
        w_list.clear();
        list.clear();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        if(!toDtae.equals (""))
        {
            params.put ("to_date", common.changeDateFormat(tofrom));
            params.put ("from_date", common.changeDateFormat(toDtae));
        }
        if (type.equalsIgnoreCase("star")){
            params.put("market_type","starline");
        }
        common.postRequest (api_url, params, new Response.Listener ( ) {
            @Override
            public void onResponse(Object response) {
                loadingBar.dismiss ();
                Log.e ("starres", "onResponse: "+response );
                 try {
                        JSONObject object = new JSONObject(response.toString());
                        boolean res = object.getBoolean("responce");
                        if (res) {
                            JSONArray data = object.getJSONArray("data");
                            w_list.clear();
                            list.clear();
                            for (int i = 0; i < data.length(); i++) {

                                HistryModel model = new HistryModel();
                                JSONObject obj = data.getJSONObject(i);
                                model.setId(obj.getString("id"));
                                model.setMatka_id(obj.getString("matka_id"));
                                model.setGame_id(obj.getString("game_id"));
                                model.setUser_id(obj.getString("user_id"));

                                model.setDigits(obj.getString("digits"));
                                model.setDate(obj.getString("date"));
                                model.setTime(obj.getString("time"));
                                model.setBet_type(obj.getString("bet_type"));
                                model.setStatus(obj.getString("status"));
                                if (type.equalsIgnoreCase("star_win")){
                                    model.setPoints(obj.getString("amt"));
                                    model.setName(obj.getString("s_game_time"));
                                }else {
                                    model.setPoints(obj.getString("points"));
                                    model.setName(obj.getString("market_name"));
                                }
                                if (type.equalsIgnoreCase("star_win") && (obj.getString("status").equalsIgnoreCase("win") || obj.getString("status").equalsIgnoreCase("won"))) {
                                    w_list.add(model);
                                } else if (type.equalsIgnoreCase("star")&&(!obj.getString("status").equalsIgnoreCase("win") && !obj.getString("status").equalsIgnoreCase("won"))){
                                    list.add(model);
                                }else {

                                }

                            }
                            if (list.size() > 0) {
                                Log.d("all_list", String.valueOf(list.size()));
                                historyAdapter = new AllHistoryAdapter(ctx, list);
                                binding.rvHistry.setAdapter(historyAdapter);
                            } else if (w_list.size()>0)
                            {tv_no.setVisibility (View.GONE);
                                historyAdapter = new AllHistoryAdapter(ctx, w_list);
                                binding.rvHistry.setAdapter(historyAdapter);
                            }else {
                                tv_no.setVisibility (View.VISIBLE);
                                Toast.makeText(ctx, "No History Available", Toast.LENGTH_SHORT).show();
                            }
                                if (historyAdapter!=null){

                            historyAdapter.notifyDataSetChanged();
                                }
                        }

                } catch (JSONException e) {
                    e.printStackTrace ( );
                    Toast.makeText (ctx, "Error", Toast.LENGTH_SHORT).show ( );
                    loadingBar.dismiss ();

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                error.printStackTrace();
                Toast.makeText (ctx, ""+error, Toast.LENGTH_SHORT).show ( );

            }
        });

    }

    private void getCasinoWindata(String user_id) {
        loadingBar.show ( );
        b_list.clear ( );
        w_list.clear ( );
        list.clear ( );

        HashMap<String, String> params = new HashMap<String, String> ( );
        params.put ("user_id", user_id);

        loadingBar.show ( );
        common.postRequest (URL_CASHINO_WIN, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("Winhhs", "onResponse: " + response);
                loadingBar.dismiss ( );
                try {
                    JSONObject object = new JSONObject (response);
                    boolean status = object.getBoolean ("responce");


                    if (status) {
                        JSONArray data_arr = object.getJSONArray ("data");
                        JSONObject obj = data_arr.getJSONObject (0);
                        WinModel gameRateModel = new WinModel ( );
                        gameRateModel.setMatka_id (obj.getString ("matka_id"));
                        gameRateModel.setUser_id (obj.getString ("user_id"));
                        gameRateModel.setGame_id (obj.getString ("game_id"));
                        gameRateModel.setDigits (obj.getString ("digits"));
                        gameRateModel.setTime (obj.getString ("time"));
                        gameRateModel.setDate (obj.getString ("date"));
                        gameRateModel.setName (obj.getString ("name"));
                        gameRateModel.setAmt (obj.getString("amt"));
                        gameRateModel.setBid_id (obj.getString("bid_id"));

                        winlist.add (gameRateModel);
                        WinAdapter historyAdapter = new WinAdapter (ctx, winlist);
                        binding.rvHistry.setAdapter (historyAdapter);
                        historyAdapter.notifyDataSetChanged ( );

                    } else {
                        Toast.makeText (ctx, "" + object.getString ("data"), Toast.LENGTH_SHORT).show ( );
                    }

                } catch (JSONException e) {
                    e.printStackTrace ( );

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


    }
    private void getcashinoBidData(String user_id, String matka_id) {

        loadingBar.show ( );
        b_list.clear ( );
        w_list.clear ( );
        list.clear ( );
        HashMap<String, String> params = new HashMap<String, String> ( );
        params.put ("user_id", user_id);
        params.put ("matka_id", matka_id);
        common.postRequest (URL_CASHINO_HISTORY, params, new Response.Listener ( ) {
            @Override
            public void onResponse(Object response) {
                loadingBar.dismiss ( );
                Log.e ("casinores", "onResponse: " + response);
                try {
                    JSONArray data = new JSONArray (response.toString ( ));
                    // Toast.makeText (ctx, ""+data, Toast.LENGTH_SHORT).show ( );
                    for (int i = 0; i < data.length ( ); i++) {
                        SatrhisModel model = new SatrhisModel ( );
                        JSONObject obj = data.getJSONObject (i);
                        model.setId (obj.getString ("id"));
                        model.setMatka_id (obj.getString ("matka_id"));
                        model.setGame_id (obj.getString ("game_id"));
                        model.setUser_id (obj.getString ("user_id"));
                        model.setPoints (obj.getString ("points"));
                        model.setDigits (obj.getString ("digits"));
                        model.setDate (obj.getString ("date"));
                        model.setTime (obj.getString ("time"));
                        model.setS_game_time (obj.getString ("start_time"));
                        model.setBet_type (obj.getString ("bet_type"));
                        model.setStatus (obj.getString ("status"));


                        model.setPlay_for (obj.getString ("play_for"));
                        model.setPlay_on (obj.getString ("play_on"));
                        model.setDay (obj.getString ("day"));

                        String date = getIntent().getStringExtra("date");
                        if (date.equals (obj.getString ("date"))) {
                            hlist.add (model);
                        } else {
                            // Toast.makeText (ctx, "Error", Toast.LENGTH_SHORT).show ( );
                        }


                        if (hlist.size ( ) > 0) {
                            Log.d ("all_list", String.valueOf (hlist.size ( )));
                            starhisAdapter = new StarhisAdapter(ctx, hlist);
                            binding.rvHistry.setAdapter (starhisAdapter);
                        } else {
                            Toast.makeText (ctx, "No History Available", Toast.LENGTH_SHORT).show ( );
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace ( );
                    Toast.makeText (ctx, "Error", Toast.LENGTH_SHORT).show ( );
                    loadingBar.dismiss ( );

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                Toast.makeText (ctx, "" + error, Toast.LENGTH_SHORT).show ( );

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_date:
                if (toDtae.equals ("")||tofrom.equals (""))
                {
                    Toast.makeText (this, "Please select dates", Toast.LENGTH_SHORT).show ( );
                }
                else
                {
                    if (type.equalsIgnoreCase("bid")) {
                        getBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_NEW_HISTORY);
                        url=URL_GET_NEW_HISTORY;
                    }else if (type.equalsIgnoreCase("win")){
                        getBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_HISTORY);
                        url=URL_GET_HISTORY;
                    }else if (type.equalsIgnoreCase("star"))
                    {
                        getstarBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_NEW_HISTORY);
                        url =URL_GET_NEW_HISTORY;
                    }else if(type.equalsIgnoreCase("star_win")){
                        getstarBidData(session_management.getUserDetails().get(KEY_ID),URL_GET_STARLINE_HISTORY);
                        url = URL_GET_STARLINE_HISTORY;
                    }
                }
                break;

            case R.id.tv_fromdate:
                Calendar calendars = Calendar.getInstance ( );
                final int years = calendars.get (Calendar.YEAR);
                final int months = calendars.get (Calendar.MONTH);
                final int days = calendars.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogs = new DatePickerDialog (HistryActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, fromsetListener, years, months, days);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog (HistryActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, tosetListener, year, month, day);
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