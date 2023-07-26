package in.games.luckymatkagames.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.games.luckymatkagames.Adapter.WinAdapter;
import in.games.luckymatkagames.Common.Common;
import in.games.luckymatkagames.Model.WinModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_WIN_HISTORY;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;


public class StarWinHistory extends AppCompatActivity implements View.OnClickListener {
RecyclerView rv_win;
    Activity ctx = StarWinHistory.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;

    WinAdapter winAdapter ;
    ImageView img_back;
    ArrayList<WinModel> w_list;
    String matka_id,toDtae="",tofrom="";
    TextView tv_todate, tv_fromdate,tv_no;
    DatePickerDialog.OnDateSetListener tosetListener,fromsetListener;
    Button btn_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_win_history);
        initview();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Win History");
        if (ConnectivityReceiver.isConnected()) {
            common.getUserStatus(this);
            getData (session_management.getUserDetails().get(KEY_ID));
        }else {
            common.showToast("No Internet Connection");
        }
        rv_win.setLayoutManager(new LinearLayoutManager (ctx));
    }

    private void initview() {

        tv_no=findViewById (R.id.tv_no);
        btn_date = findViewById (R.id.btn_date);
        btn_date.setOnClickListener (this);
        tv_todate = findViewById (R.id.tv_todate);
        tv_todate.setFocusableInTouchMode(false);
        tv_fromdate = findViewById (R.id.tv_fromdate);
        tv_fromdate.setFocusableInTouchMode(false);
        tv_todate.setFocusable(false);
        tv_todate.setKeyListener(null);
        tv_fromdate.setFocusable(false);
        tv_fromdate.setKeyListener(null);
        tv_todate.setOnClickListener (this);
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
        img_back=findViewById (R.id.img_back);
        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        session_management = new Session_management(ctx);
        rv_win=findViewById (R.id.rv_win);
        w_list=new ArrayList<> (  );
    }

    private void getData(String user_id) {
        HashMap<String,String> params=new HashMap<String, String>();
        if(!toDtae.equals (""))
        {
            params.put ("to_date", common.changeDateFormat(tofrom));
            params.put ("from_date", common.changeDateFormat(toDtae));
        }
        params.put("user_id",user_id);
        loadingBar.show();
        common.postRequest (URL_WIN_HISTORY, params, new Response.Listener<String> ( ) {
             @Override
             public void onResponse(String response) {
                 Log.e ("Winhhs", "onResponse: "+response );
                 loadingBar.dismiss ();
                 try {
                     JSONObject object = new JSONObject(response);
                     boolean status = object.getBoolean("responce");
                     if (status) {
                         JSONArray data_arr = object.getJSONArray("data");
                         JSONObject obj=data_arr.getJSONObject(0);
                             WinModel gameRateModel = new WinModel ();

                             gameRateModel.setId(obj.getString("id"));
                             gameRateModel.setMatka_id (obj.getString("matka_id"));
                             gameRateModel.setUser_id (obj.getString("user_id"));
                             gameRateModel.setGame_id (obj.getString("game_id"));
                             gameRateModel.setDigits (obj.getString("digits"));
                             gameRateModel.setAmt (obj.getString("amt"));
                             gameRateModel.setBid_id (obj.getString("bid_id"));
                             gameRateModel.setTime (obj.getString("time"));
                             gameRateModel.setType (obj.getString("type"));
                             gameRateModel.setDate (obj.getString("date"));
                             gameRateModel.setName(obj.getString("name"));
                             w_list.add(gameRateModel);
                         }
                     else {
                         Toast.makeText (ctx, ""+object.getString ("data"), Toast.LENGTH_SHORT).show ( );
                     }


                     winAdapter = new WinAdapter ( StarWinHistory.this, w_list);
                     rv_win.setAdapter(winAdapter);
                     if (w_list.size()==0)
                     {
                         tv_no.setVisibility (View.VISIBLE);
                     }
                     else
                     {
                         tv_no.setVisibility (View.GONE);

                     }
                 } catch (Exception e) {
                     loadingBar.dismiss ();
                     e.printStackTrace();
                     common.showToast("Something went wrong");
                 }
             }
         }, new Response.ErrorListener ( ) {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         });



        }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId ( )) {

            case R.id.btn_date:
                if (toDtae.equals ("")||tofrom.equals (""))
                {
                    Toast.makeText (this, "Please select dates", Toast.LENGTH_SHORT).show ( );
                }
                else
                {
                    getData (session_management.getUserDetails().get(KEY_ID));
                }
                break;

            case R.id.tv_fromdate:
                Calendar calendars = Calendar.getInstance ( );
                final int years = calendars.get (Calendar.YEAR);
                final int months = calendars.get (Calendar.MONTH);
                final int days = calendars.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialogs = new DatePickerDialog (StarWinHistory.this, android.R.style.Theme_Material_Light_DarkActionBar, fromsetListener, years, months, days);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog (StarWinHistory.this, android.R.style.Theme_Material_Light_DarkActionBar, tosetListener, year, month, day);
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