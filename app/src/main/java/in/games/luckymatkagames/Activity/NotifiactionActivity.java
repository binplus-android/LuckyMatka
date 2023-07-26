package in.games.luckymatkagames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import in.games.luckymatkagames.Common.Common;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.luckymatkagames.Adapter.NotificationAdapter;
import in.games.luckymatkagames.Model.NotifyModel;
import in.games.luckymatkagames.R;
import in.games.luckymatkagames.utils.LoadingBar;
import in.games.luckymatkagames.utils.Session_management;

import static in.games.luckymatkagames.Config.BaseUrl.URL_GET_NOTIFICATIONS;
import static in.games.luckymatkagames.Config.BaseUrl.URL_NOTIFICATIONS;
import static in.games.luckymatkagames.Config.Constants.KEY_ID;

public class NotifiactionActivity extends AppCompatActivity {

    RecyclerView rec_notification;
    NotificationAdapter notificationAdapter;
    LoadingBar loadingBar;
    Common module;
    Session_management sessionMangement;
    Switch aSwitch;
    SwipeRefreshLayout swipe;
    boolean flag=false;
    String user_id;
    TextView txtSwitch;
    ArrayList<NotifyModel> nList;
    in.games.luckymatkagames.databinding.ActivityNotifiactionBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notifiaction);
        binding = in.games.luckymatkagames.databinding.ActivityNotifiactionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setTitle("NoticeBoard/Rules");
        initview();
//        notifications();

        rec_notification.setLayoutManager (new LinearLayoutManager (NotifiactionActivity.this));
//        notificationData ( );
        getNotifications();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
//                    notifications ();

                    getNotifications();
                    swipe.setRefreshing(false);
                }
            }
        });


//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


              //  if(aSwitch.isChecked())
              //  {
//                    if(flag){
//                        setStatus("1");
//                    }
//
//                }
//                else {
//                    setStatus("0");
//                }
         //  }
        //});

       // notifications();
    }

    private void initview() {
        sessionMangement = new Session_management (NotifiactionActivity.this);
        loadingBar = new LoadingBar(NotifiactionActivity.this);
        module = new Common (getApplicationContext ());
        rec_notification=findViewById (R.id.rec_notification);
        aSwitch = findViewById(R.id.notification_switch);
        txtSwitch = findViewById(R.id.text_notification);
        user_id=sessionMangement.getUserDetails().get(KEY_ID);
        swipe = findViewById(R.id.swipe);
        nList=new ArrayList<> (  );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private void notifications(){
        nList.clear();
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_ID));
        module.postRequest(URL_NOTIFICATIONS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "notifications: ", response);
                try{
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success"))
                    {
                        Toast.makeText (NotifiactionActivity.this, ""+object.getString("data"), Toast.LENGTH_SHORT).show ( );
                    }else {
//                      module.errorToast(object.getString("data"));
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });

    }


    private void getNotifications(){
        nList.clear();
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);

        module.postRequest(URL_GET_NOTIFICATIONS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get_notifi", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean resp=object.getBoolean("responce");
                    if(resp) {

                        JSONArray jsonArray = object.getJSONArray ("data");
                        for (int i = 0; i < jsonArray.length ( ); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject (i);
                            NotifyModel matkasObjects = new NotifyModel ( );
                            matkasObjects.setNotification_id (jsonObject1.getString ("notification_id"));
                            matkasObjects.setNotification (jsonObject1.getString ("notification"));
                            matkasObjects.setTime (jsonObject1.getString ("time"));
//                            matkasObjects.setTitle(jsonObject1.getString("title"));

                            nList.add (matkasObjects);
                        }
                        if (nList.size ( ) > 0) {
                            if (rec_notification.getVisibility ( ) == View.GONE) {
                                rec_notification.setVisibility (View.VISIBLE);
                            }

                            notificationAdapter = new NotificationAdapter (NotifiactionActivity.this, nList);
                            rec_notification.setAdapter (notificationAdapter);
                            notificationAdapter.notifyDataSetChanged ( );
                        }


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });

    }

//    public void setStatus(final String st)
//    {
//        loadingBar.show();
//
//        HashMap<String,String> params=new HashMap<>();
//        params.put("user_id",user_id);
//        params.put("status",st);
//        module.postRequest(URL_SET_NOTIFICATIONS_STATUS, params, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("status",response.toString());
//                loadingBar.dismiss();
//                try{
//                    JSONObject object = new JSONObject(response.toString());
//                    boolean resp=object.getBoolean("responce");
//                    if(resp){
////                            common.showToast("Notification Enable.");
//                        //getNotifications();
//                    }else{
////                     common.showToast(response.getString("error"));
//                        if(aSwitch.isChecked())
//                            aSwitch.setChecked(false);
//
//                        txtSwitch.setText("OFF");
//                        rec_notification.setVisibility(View.GONE);
//                    }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loadingBar.dismiss();
//                module.showVolleyError(error);
//            }
//        });
//
//    }
}