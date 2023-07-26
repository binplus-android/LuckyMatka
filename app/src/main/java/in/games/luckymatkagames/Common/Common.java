package in.games.luckymatkagames.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import in.games.luckymatkagames.Activity.GameListActivity;
import in.games.luckymatkagames.Activity.MainActivity;
import in.games.luckymatkagames.AppController;
import in.games.luckymatkagames.Config.BaseUrl;
import in.games.luckymatkagames.Config.Constants;
import in.games.luckymatkagames.Interfaces.OnConfigData;
import in.games.luckymatkagames.Interfaces.OnGetMatka;
import in.games.luckymatkagames.Interfaces.OnGetWallet;
import in.games.luckymatkagames.Model.ConfigModel;
import in.games.luckymatkagames.Model.MatkasObjects;
import in.games.luckymatkagames.Model.TableModel;
import in.games.luckymatkagames.Model.TimeSlots;
import in.games.luckymatkagames.Model.WalletObjects;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.ConnectivityReceiver;
import in.games.luckymatkagames.utils.CustomJsonRequest;
import in.games.luckymatkagames.utils.CustomVolleyJsonArrayRequest;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;
import in.games.luckymatkagames.utils.ToastMsg;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_WALLET_AMOUNT;
import static in.games.luckymatkagames.Config.BaseUrl.URL_INDEX;
import static in.games.luckymatkagames.Config.BaseUrl.URL_TIME_SLOTS;
import static in.games.luckymatkagames.Config.BaseUrl.URL_USER_STATUS;
import static in.games.luckymatkagames.Config.BaseUrl.Url_single_matka;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;
import static in.games.luckymatkagames.Config.Constants.KEY_MSG;
import static in.games.luckymatkagames.Config.Constants.KEY_WALLET;
import static in.games.luckymatkagames.Fragment.HomeFragment.finalMatkaID;

import androidx.annotation.RequiresApi;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 20,May,2020
 */
public class Common {
    Context context;
    Session_management session_management;
    String value_wallet="",server_msg="Please wait, Loading";
    int amt_wallet=0;
    String regexStr = "^[0-9]*$";
    Pattern pattern_ifsc = Pattern.compile("[a-zA-Z]{4}[0-9]{7}");
    Pattern pattern_ifsc_2 = Pattern.compile("[a-zA-Z]{4}[0-9]{7}");
    LoadingBar loadingBar;
    public Common(Context context) {
        this.context = context;
        session_management = new Session_management(context);
        loadingBar = new LoadingBar(context);
    }
    public void getMsg() {
        {
            String json_tag="json_splash_request";
            HashMap<String,String> params=new HashMap<String, String>();
            postRequestString(URL_INDEX, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        Log.e("common getMsg",response.toString());
                        JSONArray array = new JSONArray(response.toString());
                        JSONObject dataObj=array.getJSONObject(0);
                        server_msg = dataObj.getString("server_loading_msg");
                        session_management.putString(server_msg);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    String msg=VolleyErrorMessage(error);
                    if(!msg.isEmpty())
                    {
                        Toast.makeText (context, ""+msg, Toast.LENGTH_SHORT).show ( );
                    }
                }
            });
        }
    }
    public void showToast(String s) {
        Toast.makeText(context,""+s, Toast.LENGTH_SHORT).show();
    }

    public void getUserStatus(Activity activity) {
        ToastMsg toastMsg = new ToastMsg(activity);
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        Log.e("param",params.toString());
        postRequest(URL_USER_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_USER_STATUS",response.toString());
                loadingBar.dismiss();
                try{
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp){
                        JSONObject objData = object.getJSONObject("data");
                        if (objData.getString("login_status").equals("1")&&objData.getString("mobileno").equals("0")) {
                        toastMsg.toastIconError(object.getString("message"));
                        session_management.logoutSession();
                        activity.finish();
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                showVolleyError(error);
            }
        });
    }

    public String VolleyErrorMessage(VolleyError error) {
        String str_error = "";
        if (error instanceof TimeoutError) {
            if(checkNullString (session_management.getString ().get (KEY_MSG)).equalsIgnoreCase (""))
            {
                str_error="Maintenance work";
            }
            else {
                str_error=session_management.getString ().get (KEY_MSG);
            }
        } else if (error instanceof AuthFailureError) {
            str_error = "Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
         //   str_error = "Server not responding please try again later";

            if(checkNullString (session_management.getString ().get (KEY_MSG)).equalsIgnoreCase (""))
            {
                str_error="Maintenance work";
            }
            else {
                str_error=session_management.getString ().get (KEY_MSG);
            }

            //TODO
        } else if (error instanceof NetworkError) {
            str_error = "No Internet Connection";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error = "An Unknown error occur";
        } else if (error instanceof NoConnectionError) {
            str_error = "No Internet Connection";
        } else {
            str_error = "Something Went Wrong";
        }

        return str_error;
    }

    public void errorMessageDialog(String message)
    {

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        Button btnOk=(Button)dialog.findViewById(R.id.btnOK);
        dialog.setCanceledOnTouchOutside(false);
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
        dialog.show();

        txtMessage.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public long getTimeDifference(String time) {
        long diff_e_s=0;
        Date date = new Date();
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        String cur_time = parseFormat.format(date);
        try {
            final Date s_time = parseFormat.parse(cur_time.trim());
            Date e_time = parseFormat.parse(time.trim());
            diff_e_s = e_time.getTime() - s_time.getTime();
            Log.e("dddddd","curr - "+s_time.toString()+" -end - "+e_time.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return diff_e_s;
    }

    public String get24To12Format(String timestr)
    {
        String tm="";
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");

        try {
            Date _24Hourst = _24HourSDF.parse(timestr);
            tm = _12HourSDF.format(_24Hourst);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tm;
    }

    public void showVolleyError(VolleyError error)
    {
        if(error.equals (null)&&error.equals ("")){


    }
        else {
            String msg = VolleyErrorMessage (error);
            if (!msg.isEmpty ( )) {
                showToast ("" + msg);
            }
        }

    }

    public String getSumOfPoints(List<TableModel> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + Integer.parseInt(list.get(i).getPoints());
        }

        return String.valueOf(sum);
    }

    public void setBidsDialog( final List<TableModel> list, final String m_id, final String c, final String game_id, final String w, final String dashName, final LoadingBar progressDialog, final Button btnSave, final String start_time, final String end_time) {
        Log.e("dfghj", "setBidsDialog: "+list);
        insertData(list,m_id,c,game_id,w,dashName,progressDialog,btnSave,start_time,end_time);
    }

    public void insertData(List<TableModel> list, String m_id, String c, String game_id, String w, String dashName, LoadingBar progressDialog, Button btnSave, final String start_time, final String end_time) {
          amt_wallet=0;
        JSONArray jsonArray = new JSONArray();
        for(int j=0;j<list.size();j++)
        {
            amt_wallet=amt_wallet+Integer.parseInt(list.get(j).getPoints());
        }
        {
            int er = list.size();
            if (er <= 0) {
                String message = "Please Add Some Bids";
                errorMessageDialog(message);
                return;

            } else {
                try {
                    int amt = 0;
                    ArrayList list_digits = new ArrayList();
                    ArrayList list_type = new ArrayList();
                    ArrayList list_points = new ArrayList();
                    int rows = list.size();

                    for (int i = 0; i < rows; i++) {

                        TableModel tableModel = list.get(i);

                        String asd = tableModel.getDigits();
                        String asd1 = tableModel.getPoints();
                        String asd2 = tableModel.getType();
                        int b = 9;

                        if (asd2.equalsIgnoreCase("Close")) {
                            b = 1;
                        } else if (asd2.equalsIgnoreCase("Open")) {
                            b = 0;
                        }

                        amt = amt + Integer.parseInt(asd1);

                        char quotes = '"';
                        list_digits.add(quotes + asd + quotes);
                        list_points.add(asd1);
                        list_type.add(b);

                    }


                    String id = session_management.getUserDetails().get(KEY_ID).trim();
                    String matka_id = m_id.trim();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("points", list_points);
                    jsonObject.put("digits", list_digits);
                    jsonObject.put("bettype", list_type);
                    jsonObject.put("user_id", id);
                    jsonObject.put("matka_id", matka_id);
                    jsonObject.put("game_date", c);

                    //jsonObject.put("wallet_type",value_wallet);
                    jsonObject.put("wallet_type","wallet_points");
//                jsonObject.put("game_date", "15/04/2021");
                    jsonObject.put("game_id", game_id);

                    jsonArray.put(jsonObject);

                    if (amt_wallet > Integer.parseInt(session_management.getUserDetails().get(KEY_WALLET))) {
                        showToast("Your requested main amount exceeded");
                    }
                    else {
                        btnSave.setEnabled(false);
                        updateWalletAmount(jsonArray, progressDialog, dashName, m_id,start_time,end_time,value_wallet);
                        amt_wallet=0;
                    }

                }
                catch (Exception ex) {
                    Toast.makeText(context, "Err" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            Log.e("common_btn_wallet", "onClick: "+value_wallet + amt_wallet);
        }

    }

    public int getTimeFormatStatus(String time)
    {
        //02:00 PM;
        String[] t =time.split(" ");
        String time_type= t[1];
        String[] t1 =t[0].split(":");
        int tm=Integer.parseInt(t1[0]);

        if(time_type.equals("AM"))
        {

        }
        else
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }
        return tm;

    }


    public void getMatkaAndWallet(final String matka_id, final OnGetMatka onGetMatka){

        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        params.put("matka_id",matka_id);

        postRequest(Url_single_matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {

                try{
                    int mId=Integer.parseInt(matka_id);
                    JSONObject response=new JSONObject(resp);
                    if(response.getBoolean("responce")){
                        JSONArray arr=response.getJSONArray("matka");
                        JSONObject jsonObject=arr.getJSONObject(0);
                        MatkasObjects matkasObjects=new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        if(mId<=finalMatkaID){
                            matkasObjects.setName(jsonObject.getString("name"));
                            matkasObjects.setStart_time(jsonObject.getString("start_time"));
                            matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                            matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                            matkasObjects.setNumber(jsonObject.getString("number"));
                            matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                            matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                            matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                            matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                            matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                            matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                            matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                            matkasObjects.setStatus(jsonObject.getString("status"));
//                            matkasObjects.setColor_code(jsonObject.getString("bg_color"));
                        }else{
                            matkasObjects.setName(jsonObject.getString("s_game_time"));
                            matkasObjects.setNumber(jsonObject.getString("s_game_number"));
                            matkasObjects.setBid_start_time(jsonObject.getString("s_game_time"));
                            matkasObjects.setBid_end_time(jsonObject.getString("s_game_end_time"));

                        }


                        JSONArray arrWallet=response.getJSONArray("wallet");
                        String wAmt=arrWallet.getJSONObject(0).getString("wallet_points");
                        session_management.updateWallet(wAmt);
                        onGetMatka.onGetMatka(matkasObjects);

                    }else{
                        showToast("Something Went Wrong");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showVolleyError(error);
            }
        });
    }

    public String[] getStartEndTime(MatkasObjects model){
        String[] strTIme=new String[2];
        String endTime,startTime;
        String day=new SimpleDateFormat("EEEE").format(new Date());
        if(day.equalsIgnoreCase("Sunday")){
            if(getValidTime(model.getStart_time(), model.getEnd_time())){
                startTime=model.getStart_time();
                endTime=model.getEnd_time();
            }else{
                startTime=model.getBid_start_time();
                endTime=model.getBid_end_time();
            }
        }else if(day.equalsIgnoreCase("Saturday")){
            if(getValidTime(model.getSat_start_time(), model.getSat_end_time())){
                startTime=model.getSat_start_time();
                endTime=model.getSat_end_time();
            }else{
                startTime=model.getBid_start_time();
                endTime=model.getBid_end_time();
            }
        }else{
            startTime=model.getBid_start_time();
            endTime=model.getBid_end_time();
        }
        strTIme[0]= startTime;
        strTIme[1]= endTime;
        return strTIme;
    }

    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else return !sTime.equalsIgnoreCase("00:00:00.000000") || !eTime.equalsIgnoreCase("00:00:00.000000");
    }

    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String time1 = startTime;
        String time2 = endTime;

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
        tArr[0]=as;
        tArr[1]=c;
        return tArr;
    }

    public int getBetType(long[] tArr){
        // as<0 => open,close
        //c>0 =>nothing but biding closed
        //else=>close
        long as=tArr[0];
        long c=tArr[1];
        if(as<0){
            return 2;
        }else if(c>0){
            return 0;
        }else{
            return 1;
        }
    }

    public void getConfigData(final OnConfigData onConfigData){
        HashMap<String,String> params=new HashMap<>();
        postRequest(URL_TIME_SLOTS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("Common", "onResponse: "+ resp);
                try{
                    JSONObject object = new JSONObject(resp);
                    ArrayList<ConfigModel> list=new ArrayList<>();
                    ArrayList<TimeSlots> tlist=new ArrayList<>();
                    Gson gson =new Gson();
                    Type typeList=new TypeToken<List<ConfigModel>>(){}.getType();
                    list=gson.fromJson(object.getJSONArray("config").toString(),typeList);
                    Type typeListt=new TypeToken<List<TimeSlots>>(){}.getType();
                    tlist=gson.fromJson(object.getJSONArray("time_slots").toString(),typeList);
                    list.get(0).setTime_slot(object.getJSONArray("time_slots").toString());
                    onConfigData.onGetConfigData(list.get(0));
//                    onConfigData.onGetTimeSlots(tlist.get(0));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });
    }
    public String checkNullNumber(String str){
        if(str==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return "0";
        }else{
            return str;
        }
    }

    public String checkNullString(String str){
        if(str==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return "";
        }else{
            return str;
        }
    }

    public void getWalletAmount(final OnGetWallet onGetWallet){
        HashMap<String,String> params=new HashMap();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        postRequest(URL_GET_WALLET_AMOUNT, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("getWalletAmount", "onResponse: "+ response);
                try{
                    ArrayList<WalletObjects> list=new ArrayList<>();
                    Gson gson=new Gson();
                    Type tyList=new TypeToken<List<WalletObjects>>(){}.getType();
                    list=gson.fromJson(response,tyList);
                    session_management.updateWallet(list.get(0).getWallet_points());
                    Log.e("getWallet_winning", "onResponse: "+list.get(0).getWinning_wallet_points() );
                    session_management.updateWinWallet(list.get(0).getWinning_wallet_points());
                    onGetWallet.onGetWallet(list.get(0));

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showVolleyError(error);
            }
        });
    }

    public void sessionTimeOut()
    {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        Button btnOk=(Button)dialog.findViewById(R.id.btnOK);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        txtMessage.setText("Session TimeOut");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(context, MainActivity.class);
                dialog.dismiss();
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }
        });

    }

    public void updateWalletAmount(final JSONArray jsonArray, final LoadingBar progressDialog, final String matka_name, final String m_id, final String start_time, final String end_time ,String wallet_type)
    {
        ToastMsg toastMsg = new ToastMsg(context);
        final String data= String.valueOf(jsonArray);
        String json_request_tag="json_insert_request";
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("data",data);
        Log.e("params_data_insert",""+params.toString());
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.show();

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_INSERT_DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    Log.d("insert_data",response.toString());
                    JSONObject jsonObject=response;
                    // Toast.makeText(context,""+response.toString(),Toast.LENGTH_LONG).show();
                    final String status=jsonObject.getString("status");
                    progressDialog.dismiss();
                    if(status.equalsIgnoreCase("success"))
                    {
                        toastMsg.toastIconSuccess("Bid Added Successfully.");
                        Intent intent=new Intent(context, GameListActivity.class);
                        intent.putExtra("matkaName",matka_name);
                        intent.putExtra("m_id",m_id);
                        intent.putExtra("end_time",end_time);
                        intent.putExtra("start_time",start_time);
                        intent.putExtra("type","matka");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        context.startActivity(intent);

//                        Toast.makeText(context,"Bid Added Successfully.", Toast.LENGTH_LONG).show();
                    } else if(status.equalsIgnoreCase("failed")) {
                        String sd= status;
                        errorMessageDialog(sd);
                        // Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                    } else if(status.equalsIgnoreCase("timeout")) {

                        final Dialog myDialog=new Dialog(context);
                        myDialog.setContentView(R.layout.dialog_error_message_dialog);
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        TextView txtmessage=(TextView)myDialog.findViewById(R.id.txtmessage);
                        Button btnOK=(Button) myDialog.findViewById(R.id.btnOK);

                        txtmessage.setText("Biding closed for this date");
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                                String sd= status;
                                //errorMessageDialog(context,sd.toString());
                                Intent intent=new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        myDialog.show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context,"Err"+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                String msg=VolleyErrorMessage(error);
                errorMessageDialog(msg);


            }
        });

        increaseResponseTime(customJsonRequest);
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);
    }

    public void increaseResponseTime(CustomJsonRequest customJsonRequest){
        customJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    public void increaseResponseTimeArray(CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest){
        customVolleyJsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    public String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }

    public void postRequest(String url, final HashMap<String, String> params, Response.Listener listener, Response.ErrorListener errorListener){
        if(!ConnectivityReceiver.isConnected()){
         showToast("No Internet Connection");
        }else{
            Log.e("url", ""+url );
            StringRequest stringRequest=new StringRequest(Request.Method.POST,url,listener,errorListener){
                @Override
                protected Map<String, String> getParams(){
                    Log.e("params", ""+params );
                    return params;
                }
            };

            RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                    Constants.REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(mRetryPolicy);
            AppController.getInstance().addToRequestQueue(stringRequest,"req");

        }
    }
    public void postRequestString(String url, HashMap<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Log.e ("postmethod", "postRequest: " + url + "\n" + params);

        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Log.e ("params", "check" + params);
                return params;
                // return super.getParams ( );
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy (Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy (retryPolicy);

        AppController.getInstance ( ).addToRequestQueue (stringRequest, "tag");

    }


    public boolean checkNull(String str){
        return str == null || str.isEmpty() || str.equalsIgnoreCase("null");
    }

    private void requestFocus(EditText et) {
        et.requestFocus();
    }

    public String getCurrDateDay(){
        String str="";
        try{
            str=new SimpleDateFormat("dd-MM-yyyy EEEE").format(new Date());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    public String getCurrDate(){
        String str="";
        try{
            str=new SimpleDateFormat("dd/MM/yyyy ").format(new Date());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }

    public void spinTimer(String end_time, TextView txt_timer)
    {
        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        Date date1 = null;
        long difft = 0;
        try {
            Date current_time=new Date();
            SimpleDateFormat sformat=new SimpleDateFormat("HH:mm:ss");
            String currentTime=sformat.format(current_time);
            Log.e("zsdfg", currentTime);
            date1 = df.parse(currentTime);
            java.util.Date date2 = df.parse(end_time);
             difft = date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        new CountDownTimer(difft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
//                txt_timer.setText(s+text);
                txt_timer.setText(text);
            }

            @Override
            public void onFinish() {
                txt_timer.setText("");
                txt_timer.setTextColor(context.getResources().getColor(R.color.white));
            }
        }.start();
    }

    public void whatsapp(String phone, String message) {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "whatsapp://send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String trimMobileNumber(String mobile)
    {
        int len = mobile.length()-10;
        String str = mobile.substring(len);
        Log.e("numberlength",str);
        if (mobile.equals("")||mobile==null||mobile.isEmpty()||mobile.equalsIgnoreCase("null")){
            return "";
        }else {
            return str;
        }

    }

    public String changeDateFormat(String date){
        Date initDate = null;
        String parsedDate="";
        try {
            initDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            parsedDate = formatter.format(initDate);
            Log.e(" parsedDate",parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String allValidation(String type, EditText et) {
        String val = "";
        String result = "y";
        switch (type) {

            case "name":
                if (et.getText ( ).toString ( ).isEmpty ( )) {
                    et.setError ( "Please fill value");
                    result = "Please fill value";
                } else if (et.getText ( ).toString ( ).matches (regexStr)) {
                    et.setError ( "Please fill character value ");
                    result = "Please fill character value";
                }
                else {
                    result = "correct";
                }
                break;

            case "mobile":
                if (et.getText ( ).toString ( ).isEmpty ( )) {
                    et.setError ("Mobile Number required");
                    et.requestFocus ( );
                    result = "Mobile Number required";
                } else if (et.getText ( ).toString ( ).length ( ) != 10) {
                    et.setError ( "Please fill valid mobile number");
                    et.requestFocus ( );
                    result = "Please fill valid mobile number";
                } else if (Integer.parseInt (String.valueOf (et.getText ( ).toString ( ).charAt (0))) < 6) {
                    et.setError ( "Mobile number should be start with 6 or greater than 6");
                    et.requestFocus ( );
                    result = "Mobile number should be start 6 or greater 6";
                } else {
                    result = "correct";
                }
                break;


            case "account_number":

                if (et.getText ( ).toString ( ).isEmpty ( )) {
                    et.setError ( "Account number required");
                    et.requestFocus ( );
                    result = "Account number required";
                } else if (et.getText ( ).toString ( ).matches ("[a-zA-Z ]+")) {
                    et.setError ( "Please fill valid account number");
                    et.requestFocus ( );
                    result = "Please fill valid account number";
                } else if (et.getText ( ).toString ( ).length ( ) < 9) {
                    et.setError ( "Please fill Valid account number");
                    et.requestFocus ( );
                    result = "Please fill valid account number";
                } else {
                    result = "correct";

                }
                break;

            case "ifsc_code":
                if (et.getText ( ).toString ( ).isEmpty ( )) {
                    et.setError ("IFSC Code required");
                    et.requestFocus ( );
                    result = "IFSC Code required";
                }
//                else if (!pattern_ifsc.matcher (et.getText ( ).toString ( )).matches ( )) {
//                    et.setError ( "Please fill valid IFSC Code");
//                    et.requestFocus ( );
//                    result = "Please fill valid IFSC Code";
//                }
//                else if (!pattern_ifsc_2.matcher (et.getText ( ).toString ( )).matches ( )) {
//                    et.setError ( "Please fill valid IFSC Code");
//                    et.requestFocus ( );
//                    result = "Please fill valid IFSC Code";
//                }

                else {
                    result = "correct";
                }
                break;
        }
        return result;
    }
    public void generateToken(){
        String UUID = OneSignal.getDeviceState().getUserId();
//        String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        session_management.addToken(UUID);
        Log.e("UUID", "generateToken: "+UUID );

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("sadfg",android_id);
        session_management.addDeviceId(android_id);


    }
}


